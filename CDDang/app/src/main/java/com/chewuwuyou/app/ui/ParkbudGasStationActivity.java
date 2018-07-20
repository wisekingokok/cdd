package com.chewuwuyou.app.ui;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.NativeBDAdapter;
import com.chewuwuyou.app.utils.Constant.AssistantType;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.ActionSheet;
import com.chewuwuyou.app.widget.ActionSheet.MenuItemClickListener;

/**
 * @describe:停车助手和加油助手
 * @author:yuyong
 * @version 1.1.0
 * @created:2015-1-8下午2:25:25
 */
public class ParkbudGasStationActivity extends BaseActivity implements
		View.OnClickListener, MenuItemClickListener{
	@ViewInject(id = R.id.sub_header_bar_left_ibtn)
	private ImageButton mBackBtn;
	@ViewInject(id = R.id.sub_header_bar_tv)
	private TextView mHeaderTV;
	// @ViewInject(id = R.id.park_lable_btn)
	// private Button mParkLableBtn;
	// @ViewInject(id = R.id.park_list_iv)
	// private ImageView mParkListIV;
	@ViewInject(id = R.id.head_page_iv)
	private ImageView mHeadPageIV;//
	/**
	 * 标题
	 */
	private ListView mPullToRefreshListView;
	private List<PoiInfo> mData;
	private NativeBDAdapter mAdapter;
	private PoiSearch mPoiSearch;
	private double mLat, mLng;
	public MyLocationListenner myListener;
	LocationClient mLocClient;
	private RelativeLayout mTitleHeight;//标题布局高度
	boolean isFirstLoc = true;// 是否首次定位
	private SearchView mSearchShopSV;// 搜索店铺的view
	/**
	 * 搜索类型
	 */
	private String mPoiType;
	/**
	 * 停车签
	 */
	private int mAssistantType;
	private OnGetPoiSearchResultListener poiListener;

	private String loc;

	// 使用高德地图导航的Uri
	private String mGaoDeUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.native_service_list);
		initView();
		initData();
		initEvent();
		showWaitingDialog();
	}

	private void initView() {
		mPullToRefreshListView = (ListView) findViewById(R.id.listView);
	}

	private void initData() {
		mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
		mAssistantType = getIntent().getExtras().getInt("assistantType");
		
		switch (mAssistantType) {
		case AssistantType.PARK_BUD:
			mPoiType = "维修保养";
			break;
		case AssistantType.GAS_STATION:
			mPoiType = "加油";
			break;
		case AssistantType.AUTOPARTS:
			mPoiType = "汽车配件";
			break;
		case AssistantType.FOURSSTORES:
			mPoiType = "汽车4s店";
			break;
		case AssistantType.MAINTENANCE:
			mPoiType = "停车场";
			break;
		case AssistantType.CARBEAUTY:
			mPoiType = "汽车美容";
			break;
		case AssistantType.DRIVING_SCHOOL:
			mPoiType = "驾校服务";
			break;
		default:
			break;
		}
		mHeaderTV.setText(mPoiType);
		myListener = new MyLocationListenner();
		mLocClient = new LocationClient(this);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setAddrType("all");
		mLocClient.setLocOption(option);
		mLocClient.start();

		if (mData == null) {
			mData = new ArrayList<PoiInfo>();
		}

		if (mAdapter == null) {
			mAdapter = new NativeBDAdapter(ParkbudGasStationActivity.this,
					mData);
		}
		mPullToRefreshListView.setAdapter(mAdapter);

		if (mPoiSearch == null) {
			mPoiSearch = PoiSearch.newInstance();
		}
		if (poiListener == null) {
			poiListener = new OnGetPoiSearchResultListener() {
				@Override
				public void onGetPoiResult(final PoiResult result) {
					// 获取POI检索结果
					if (result != null && result.getAllPoi() != null) {
						mData = result.getAllPoi();
						dismissWaitingDialog();
						mAdapter = new NativeBDAdapter(getApplicationContext(),
								mData, mLat, mLng);
						mPullToRefreshListView.setAdapter(mAdapter);
						mAdapter.notifyDataSetChanged();
					}

				}

				@Override
				public void onGetPoiDetailResult(PoiDetailResult result) {
					// 获取Place详情页检索结果
				}
			};
		}
		mSearchShopSV = (SearchView) findViewById(R.id.search_shop_sv);
		Class<?> argClass = mSearchShopSV.getClass();
		try {
			Field ownField = argClass.getDeclaredField("mSearchPlate");
			ownField.setAccessible(true);
			View mView;
			try {
				mView = (View) ownField.get(mSearchShopSV);
				mView.setBackgroundResource(R.drawable.search_view_text);
			} catch (IllegalArgumentException e) {
				
				e.printStackTrace();
			} catch (IllegalAccessException e) {
			
				e.printStackTrace();
			}

		} catch (NoSuchFieldException e) {
		}
	}

	private void initEvent() {
		mBackBtn.setOnClickListener(this);
		// mParkListIV.setOnClickListener(this);
		mLocClient.registerLocationListener(myListener);

		// mPullToRefreshListView.setOnRefreshListener(this);
		mPullToRefreshListView
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						loc = mData.get(arg2).location.latitude + ","
								+ mData.get(arg2).location.longitude + ","
								+ mData.get(arg2).name;

						mGaoDeUri = "androidamap://navi?sourceApplication=appname&poiname="
								+ mData.get(arg2).name
								+ "&lat="
								+ mData.get(arg2).location.latitude
								+ "&lon="
								+ mData.get(arg2).location.longitude
								+ "&dev=1&style=2";
						// 直接导航参考
						// http://developer.baidu.com/map/uri-introandroid.htm
						setTheme(R.style.ActionSheetStyleIOS7);
						ActionSheet menuView = new ActionSheet(
								ParkbudGasStationActivity.this);
						menuView.setCancelButtonTitle("取 消");// before add items
						menuView.addItems("百度地图", "高德地图");
						menuView.setItemClickListener(ParkbudGasStationActivity.this);
						menuView.setCancelableOnTouchMenuOutside(true);
						menuView.showMenu();
					}
				});

		mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
		
		
//		mSearchShopSV.setOnQueryTextListener(this);
//		mSearchShopSV.setSubmitButtonEnabled(false);
		
		mSearchShopSV.setOnQueryTextListener(new OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String arg0) {
				return false;
			}
			@Override
			public boolean onQueryTextChange(String arg0) {
				
				mPoiSearch.searchNearby(new PoiNearbySearchOption()
				.location(new LatLng(mLat, mLng)).keyword(arg0+mPoiType)
				.radius(5000));
				return false;
			}
		});
	}

	/**
	 * 获取检索信息
	 */
	private void getParkBud() {
		
		mPoiSearch.searchNearby(new PoiNearbySearchOption()
				.location(new LatLng(mLat, mLng)).keyword(mPoiType)
				.radius(5000));
	}

	// @Override
	// public void onRefresh(PullToRefreshBase<ListView> refreshView) {
	// getParkBud();
	// }

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null)
				return;
			mLng = location.getLongitude();
			mLat = location.getLatitude();

			MyLog.i("YUY", "========定位的经纬度==" + mLat + "====" + mLng);
			if (isFirstLoc) {
				isFirstLoc = false;
				// LatLng ll = new LatLng(location.getLatitude(),
				// location.getLongitude());//没用到所以注释掉了
				getParkBud();
			}
		}

		@Override
		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		super.onDestroy();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			finishActivity();
			break;
		// case R.id.park_list_iv:
		// Intent intent = new Intent(ParkbudGasStationActivity.this,
		// ParkLocalLableActivity.class);
		// startActivity(intent);
		// break;
		default:
			break;
		}
	}

	private boolean isInstallByread(String packageName) {
		return new File("/data/data/" + packageName).exists();
	}

	@Override
	public void onItemClick(int itemPosition) {

		switch (itemPosition) {
		case 0:
			if (isInstallByread("com.baidu.BaiduMap")) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				Uri uri = Uri.parse("geo:" + loc);
				intent.setData(uri);
				intent.setPackage("com.baidu.BaiduMap");
				startActivity(intent);
			} else {
				ToastUtil
						.toastShow(ParkbudGasStationActivity.this, "未安装百度地图应用");
			}
			break;
		case 1:
			if (isInstallByread("com.autonavi.minimap")) {
				Intent intent = new Intent("android.intent.action.VIEW",
						android.net.Uri.parse(mGaoDeUri));
				intent.setPackage("com.autonavi.minimap");
				startActivity(intent);
			} else {
				ToastUtil
						.toastShow(ParkbudGasStationActivity.this, "未安装高德地图应用");
			}
			break;
		default:
			break;
		}

	}

	

}
