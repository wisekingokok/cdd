package com.chewuwuyou.app.ui;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.TrafficBrokerAdapter;
import com.chewuwuyou.app.bean.ServicePro;
import com.chewuwuyou.app.bean.TrafficBusinessListBook;
import com.chewuwuyou.app.eventbus.EventBusAdapter;
import com.chewuwuyou.app.utils.AlertDialog;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ServiceUtils;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.PullToRefreshBase;
import com.chewuwuyou.app.widget.PullToRefreshBase.OnRefreshListener2;
import com.chewuwuyou.app.widget.PullToRefreshListView;
import de.greenrobot.event.EventBus;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:违章服务，车辆服务，驾证服务商家信息列表
 * @author:刘春
 */
public class VehicleServiceActivity extends CDDBaseActivity implements
		OnClickListener, OnRefreshListener2<ListView> {

	private TextView mTitleTV, mSubHeaderBarRightTV,mChooseCityTv, mType, mTip;// 标题，服务须知，暂无数据,显示选择或定位城市,业务类型,类型描述
	private ImageButton mBackIBtn;// 返回上一页
	private ImageView mLogo;// 类型图片
	private RelativeLayout mTitleHeight;// 标题高度，用于沉浸式状态栏
	private int mServiceType;// 接收服务的额类型 1 违章 2车辆 3驾证
	private PullToRefreshListView mBusinessList;
	private String mProvinceName, mCityName, mAreaName;// 省，市，区名称
	private int mProvinceId, mCityId, mAreaId;// 省，市，区 id
	private List<TrafficBusinessListBook> mResponseList;// 商家列表
	private TrafficBrokerAdapter mTrafficBrokerAdapter;// 商家列表适配器
	private int mCurcor = 0;// 传递当前页数
	private int mProId;// 项目Id
	private boolean mIsRefreshing = false;// 翻页要用
	public static String mAddress;// 地址
	public MyLocationListenner myListener = new MyLocationListenner();// 定位
	private ServicePro broker;
	private LinearLayout mNetworkAbnormalLayout;
	private TextView mNetworkAgain;//点击重新加载

	@ViewInject(id = R.id.data_is_empty_tv)
	private TextView mDataIsEmptyTV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏标题栏
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vehicle_service_ac);
		initView();
		initData();
		initEvent();

	}

	/**
	 * 初始化控件
	 */
	@Override
	protected void initView() {
		mNetworkAgain = (TextView) findViewById(R.id.network_again);
		mNetworkAbnormalLayout = (LinearLayout) findViewById(R.id.network_abnormal_layout);
		mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
		mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mSubHeaderBarRightTV = (TextView) findViewById(R.id.sub_header_bar_right_tv);
		mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);// 标题高度
		mBusinessList = (PullToRefreshListView) findViewById(R.id.business_list);
		mChooseCityTv = (TextView) findViewById(R.id.choose_city_tv);
		mType = (TextView) findViewById(R.id.type);
		mTip = (TextView) findViewById(R.id.tip);
		mLogo = (ImageView) findViewById(R.id.logo);
	}

	/**
	 * 逻辑处理
	 */
	@Override
	protected void initData() {

		// 注册EventBus
		EventBus.getDefault().register(this);

		isTitle(mTitleHeight);// 根据不同手机判断

		Bundle bundle = getIntent().getExtras();
		broker = (ServicePro) bundle.getSerializable("servicedata");

		serviceType(broker.getType());// 判断点击过来是哪个类型
		mProId = broker.getId();

		mTip.setText(broker.getServiceDesc());// 业务类型描述
		ImageUtils.displayImage(broker.getProjectImg(), mLogo, 10);// 图片类型显示

		showLocation();// 定位

		mSubHeaderBarRightTV.setVisibility(View.VISIBLE);
		mSubHeaderBarRightTV.setText("服务流程");
		mType.setText(ServiceUtils.getProjectName(broker.getProjectNum() + ""));

		mResponseList = new ArrayList<TrafficBusinessListBook>();

		mTrafficBrokerAdapter = new TrafficBrokerAdapter(VehicleServiceActivity.this, mResponseList, broker,mChooseCityTv.getText().toString(), getIntent().getIntExtra("serviceType", 0));
		mBusinessList.setAdapter(mTrafficBrokerAdapter);
		TextView tv = new TextView(this);
		tv.setGravity(Gravity.CENTER);
		tv.setText("可能网络较差，请重新刷新数据");
		tv.setTextColor(getResources().getColor(R.color.empty_text_color));
		mBusinessList.setEmptyView(tv);

	}

	/**
	 * EventBus接收传递的数据
	 * 
	 * @param busAdapter
	 */
	public void onEventMainThread(EventBusAdapter busAdapter) {
		if(busAdapter.getBusinessPersonalCenterCollection()!=null){
			if(!TextUtils.isEmpty(busAdapter.getBusinessPersonalCenterCollection().toString())){
				String Collection = busAdapter.getBusinessPersonalCenterCollection().toString();// 接收状态值
				mResponseList.get(busAdapter.getPosition()).setIsfavorite(
						Integer.parseInt(Collection));// 更改收藏状态
				mTrafficBrokerAdapter.notifyDataSetChanged();// 更新适配器
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);// 反注册EventBus
	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {

		Intent intent;
		switch (v.getId()) {
		case R.id.sub_header_bar_right_tv:// 服务须知
			intent = new Intent(VehicleServiceActivity.this,
					ServiceFlowActivity.class);
			intent.putExtra("serviceType",
					getIntent().getIntExtra("serviceType", 0));
			startActivity(intent);
			break;
		case R.id.sub_header_bar_left_ibtn:// 返回上一页
			finishActivity();
			break;
		case R.id.network_again://重新加载
			getBusinessList();
		    break;
		case R.id.choose_city_tv:// 选择城市
			Constant.CITY_CHOOSE = 1;
			intent = new Intent(VehicleServiceActivity.this,
					AreaSelectActivity.class);
			startActivityForResult(intent, 20);
			break;

		default:
			break;
		}
	}

	/**
	 * 事件监听
	 */
	@Override
	protected void initEvent() {
		mSubHeaderBarRightTV.setOnClickListener(this);
		mBackIBtn.setOnClickListener(this);
		mBusinessList.setOnRefreshListener(this);
		mChooseCityTv.setOnClickListener(this);
		mNetworkAgain.setOnClickListener(this);
	}

	/**
	 * 判断是哪个服务类型
	 */
	public void serviceType(int type) {
		switch (type) {
		case 1:
			mServiceType = Constant.SERVICE_TYPE.ILLEGAL_SERVICE;
			mTitleTV.setText("商家列表");
			break;
		case 2:
			mServiceType = Constant.SERVICE_TYPE.CAR_SERVICE;
			mTitleTV.setText("商家列表");
			break;
		case 3:
			mServiceType = Constant.SERVICE_TYPE.LICENCE_SERVICE;
			mTitleTV.setText("商家列表");
			break;
		default:
			break;
		}
	}

	/**
	 * 显示位置信息
	 */
	private void showLocation() {
		if (getIntent().getIntExtra("isWzdmActivity", 0) == 1) {// 从违章代码查询进
			mProvinceName = getIntent().getStringExtra("province");
			mCityName = getIntent().getStringExtra("city");
			mAreaName = getIntent().getStringExtra("district");

			mChooseCityTv.setText(mProvinceName + mCityName + mAreaName);
			Region(mProvinceName, mCityName, mAreaName);// 根据省市区名称查询Id
		} else {
			// 获取缓存的地址信息
			mProvinceName = CacheTools.getUserData("province");
			mCityName = CacheTools.getUserData("city");
			mAreaName = CacheTools.getUserData("district");
			if (!TextUtils.isEmpty(mCityName)) {
				mChooseCityTv.setText(mProvinceName + mCityName + mAreaName);
				Region(mProvinceName, mCityName, mAreaName);// 根据省市区名称查询Id
			} else {
				AlertDialog dialog = new AlertDialog(
						VehicleServiceActivity.this).builder();
				dialog.setTitle("定位信息");
				dialog.setMsg("暂未定位到您的位置信息！");
				dialog.setPositiveButton("重新定位", new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						getMyLocation();
					}
				});
				dialog.setNegativeButton("选择地区", new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(VehicleServiceActivity.this,
								AreaSelectActivity.class);
						Constant.CITY_CHOOSE=1;
						startActivityForResult(intent, 20);
					}
				});
				dialog.setCancelable(false);
				dialog.show();
			}
		}
	}

	/**
	 * 根据城市名称查询Id
	 */
	public void Region(String province, String city, String district) {
		AjaxParams params = new AjaxParams();
		params.put("provinceName", province);
		params.put("cityName", city);
		params.put("districtName", district);
		mChooseCityTv.setText(province + city + district);
		mAddress = mChooseCityTv.getText().toString();
		requestNet(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:
					try {
						JSONObject jo = new JSONObject(msg.obj.toString());
						mProvinceId = jo.getInt("provinceId");
						mCityId = jo.getInt("cityId");
						mAreaId = jo.getInt("districtId");
						getBusinessList();
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;

				default:
					break;
				}
			}
		}, params, NetworkUtil.INQUIRE_CITY_ID, false, 0);
	}

	/**
	 * 根据城市iD 访问数据
	 * 
	 * @param refresh
	 */
	private void getRefreshList(final boolean refresh) {
		if (refresh) {
			mCurcor = 0;
		}
		AjaxParams params = new AjaxParams();

		params.put("proId", String.valueOf(mProId));
		params.put("start", String.valueOf(mCurcor));
		params.put("provinceId", mProvinceId + "");
		params.put("cityId", mCityId + "");
		params.put("districtId", mAreaId + "");
		params.put("serviceType", String.valueOf(mServiceType));
		requestNet(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:
					List<TrafficBusinessListBook> response = TrafficBusinessListBook.parseList(msg.obj
							.toString());
					mIsRefreshing = false;
					mBusinessList.onRefreshComplete();
					if (refresh) {
						mBusinessList.onLoadMore();
						mResponseList.clear();
						if (response == null) {
							mNetworkAbnormalLayout.setVisibility(View.VISIBLE);
						}
					} else {
						if (response == null) {
							ToastUtil.toastShow(VehicleServiceActivity.this,
									"没有更多数据了");
							return;
						}
					}
					mResponseList.addAll(response);
					mCurcor = mResponseList.size();
					if (response.size() < 1) {
						if (msg.obj.toString().equals("[]")) {
							ToastUtil.toastShow(VehicleServiceActivity.this,
									"没有更多数据了");
						}
						mBusinessList.onLoadComplete();
					}
					mTrafficBrokerAdapter.notifyDataSetChanged();
					break;
				default:
					mBusinessList.onRefreshComplete();
					mIsRefreshing = false;
					break;
				}
			}
		}, params, NetworkUtil.SELECT_BUSSINESS_LIST, false, 1);
	}

	/**
	 * 根据城市iD 访问数据
	 * 
	 * @param
	 */
	private void getBusinessList() {
		mCurcor = 0;
		AjaxParams params = new AjaxParams();
		params.put("proId", String.valueOf(mProId));
		params.put("start", String.valueOf(mCurcor));
		params.put("provinceId", mProvinceId + "");
		params.put("cityId", mCityId + "");
		params.put("districtId", mAreaId + "");
		params.put("serviceType", String.valueOf(mServiceType));
		requestNet(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:


					mIsRefreshing = false;
					mBusinessList.onRefreshComplete();
					mBusinessList.onLoadMore();
					mResponseList.clear();
					// 为超哥的锅做兼容
					JSONArray jsonArray = null;
					try {
						jsonArray = new JSONArray(msg.obj.toString());
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jo = jsonArray.getJSONObject(i);
							if (jo.get("url") instanceof String) {
								jo.put("url", jo.getString("url"));
							} else {
								jo.put("url", jo.getJSONObject("url")
										.getString("url"));
							}
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
					List<TrafficBusinessListBook> response = TrafficBusinessListBook.parseList(jsonArray.toString());
					mResponseList.addAll(response);
					if (mResponseList.size() != 0) {
						mBusinessList.setVisibility(View.VISIBLE);
						mNetworkAbnormalLayout.setVisibility(View.GONE);
						mTrafficBrokerAdapter.notifyDataSetChanged();
					} else {
						mDataIsEmptyTV.setVisibility(View.VISIBLE);
						mBusinessList.setVisibility(View.GONE);
						mNetworkAbnormalLayout.setVisibility(View.GONE);
					}
					mCurcor = mResponseList.size();
					mTrafficBrokerAdapter.notifyDataSetChanged();
					break;
				default:
					break;
				}
			}
		}, params, NetworkUtil.SELECT_BUSSINESS_LIST, false, 0);
	}

	/**
	 * 回調方法
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {

			mCityName = data.getStringExtra("city");
			mProvinceName = data.getStringExtra("province");
			mAreaName = data.getStringExtra("district");
			if (mAreaName.equals("全部")) {
				mAreaName = "";
			}
			if (TextUtils.isEmpty(mAreaName)) {
				mAreaId = 0;
				if (mProvinceName.equals(mCityName)) {
					mChooseCityTv.setText(mCityName);
				} else {
					mChooseCityTv.setText(mProvinceName + mCityName);
				}
			} else {
				if (mProvinceName.equals(mCityName)
						&& mCityName.equals(mAreaName)) {
					mChooseCityTv.setText(mAreaName);
				} else if (mCityName.equals(mProvinceName)
						&& !mCityName.equals(mAreaName)) {
					mChooseCityTv.setText(mCityName + mAreaName);
				} else if (mCityName.equals(mAreaName)
						&& !mProvinceName.equals(mAreaName)) {
					mChooseCityTv.setText(mAreaName);
				} else {
					mChooseCityTv
							.setText(mProvinceName + mCityName + mAreaName);
				}

			}
			mAddress = mChooseCityTv.getText().toString();
			if (data.getStringExtra("cityId") == null) {
				Region(mProvinceName, mCityName, mAreaName);// 根据城市名称查询ID
			} else {
				if (!TextUtils.isEmpty(data.getStringExtra("districtId"))
						&& !TextUtils.isEmpty(data.getStringExtra("cityId"))
						&& !TextUtils
								.isEmpty(data.getStringExtra("provinceId"))) {
					mAreaId = Integer.parseInt(data
							.getStringExtra("districtId"));
					mCityId = Integer.parseInt(data.getStringExtra("cityId"));
					mProvinceId = Integer.parseInt(data
							.getStringExtra("provinceId"));
					getBusinessList();
				}
			}
			mAddress = mChooseCityTv.getText().toString();

		}
	}

	/**
	 * 下拉加载
	 * @param refreshView
	 */
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		String label = DateUtils.formatDateTime(getApplicationContext(),
				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);

		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
		if (!mIsRefreshing&&!mChooseCityTv.getText().toString().contains("定位中")) {
			mIsRefreshing = true;
			getRefreshList(true);
		} else {
			mBusinessList.onRefreshComplete();
		}
	}

	/**
	 * 上啦刷新
	 * 
	 * @param refreshView
	 */
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!mIsRefreshing) {
			mIsRefreshing = true;
			getRefreshList(false);
		} else {
			mBusinessList.onRefreshComplete();
		}
	}

	/**
	 * 定位自己的位置
	 */
	public void getMyLocation() {
		mLocationClient = new LocationClient(VehicleServiceActivity.this);
		mLocationClient.registerLocationListener(myListener);
		setLocationOption();// 设定定位参数
		mLocationClient.start();// 开始定位
	}

	// 定位
	// 设置相关参数
	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.disableCache(false);// 禁止启用缓存定位
		option.setPoiNumber(5); // 最多返回POI个数
		option.setScanSpan(5000); // 设置发起定位请求的间隔时间为5000ms
		option.setPoiDistance(1000); // poi查询距离
		option.setPoiExtraInfo(true); // 是否需要POI的电话和地址等详细信息
		mLocationClient.setLocOption(option);

	}

	/**
	 * 监听函数，更新位置
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		// 接收位置信息
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				CacheTools.setUserData("province", location.getProvince());
				CacheTools.setUserData("city", location.getCity());
				CacheTools.setUserData("district", location.getDistrict());
				CacheTools.setUserData("Lat", location.getLatitude() + "");
				CacheTools.setUserData("Lng", location.getLongitude() + "");
				mCityName = CacheTools.getUserData("city");
				mProvinceName = CacheTools.getUserData("province");
				mAreaName = CacheTools.getUserData("district");
				mChooseCityTv.setText(mProvinceName + mCityName + mAreaName);
				mAddress = mChooseCityTv.getText().toString();
				Region(mProvinceName, mCityName, mAreaName);// 定位成功后继续查询城市id及区的id
				// 退出时销毁定位
				mLocationClient.stop();

			}
		}

		// 接收POI信息函数
		@Override
		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
		}
	}
}
