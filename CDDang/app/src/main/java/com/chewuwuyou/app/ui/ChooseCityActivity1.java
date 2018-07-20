package com.chewuwuyou.app.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.CityAdapter;
import com.chewuwuyou.app.adapter.CityChoiceAdapter;
import com.chewuwuyou.app.bean.CityName;
import com.chewuwuyou.app.bean.DistrictName;
import com.chewuwuyou.app.bean.LatelyCity;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.PinYinUtils;
import com.chewuwuyou.app.widget.BladeView;
import com.chewuwuyou.app.widget.BladeView.OnItemClickListener;
import com.chewuwuyou.app.widget.PinnedHeaderListView;
import com.chewuwuyou.eim.db.DBctiy;

/**
 * @describe:选择城市
 * @author:xiajy
 * @version 1.1.0
 * @created:2014-11-21上午9:48:34
 */
public class ChooseCityActivity1 extends BaseActivity implements
		OnQueryTextListener {

	@ViewInject(id = R.id.sub_header_bar_left_ibtn)
	private ImageButton mBackBtn;
	@ViewInject(id = R.id.sub_header_bar_tv)
	private TextView mHeaderTV;
	private static final String FORMAT = "^[a-z,A-Z].*$";
	private PinnedHeaderListView mListView;
	private BladeView mLetter;
	private CityAdapter mAdapter;
	// private List<CityName> mProvinces;
	// 首字母集
	public static List<String> mSections;
	// 根据首字母存放数据
	private Map<String, List<CityName>> mMap;
	// 首字母位置集
	private List<Integer> mPositions;
	// 首字母对应的位置
	private Map<String, Integer> mIndexer;

	private ListView mSubList;
	private LinearLayout mSubLayout;
	private ImageView mImgHide;

	private List<CityName> mCityNames;
	private List<DistrictName> mDistrictNames;
	private List<CityName> mData;
	// 获取的区的集合
	private List<String> mDistricts = null;
	// 存储最近三个位置
	// private String[] preModels;
	// // 最近的列表界面所在的容器
	// private LinearLayout premodel_linearLayout;
	// private List<CityName> mUpdateData;
	// private View headView;
	private EditText mEditText;

	private Intent intent;
	// private LinearLayout premodel_linearLayout;

	private CityChoiceAdapter cityApapter;
	private DBctiy dbCity = new DBctiy(this);

	private TextView premodel1_tv, premodel2_tv, premodel3_tv, nowmodel_tv;
	private InputMethodManager manager;

	private String city, province, district;// 接收定位 市 省 区
	private List<LatelyCity> mRecentList;// 最近访问城市集合
	private RelativeLayout mTitleHeight;//标题布局高度
	/**
	 * 更新数据
	 */
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.SEND_ADAPTER:
				getDistrict(((CityName) msg.obj));
				// if (!(StoreInquiryCitys.mPremodel == null ||
				// StoreInquiryCitys.mPremodel
				// .size() == 0)) {
				// for (CityName city : StoreInquiryCitys.mPremodel) {
				// if (!(city.getCityName().equals(((CityName) msg.obj)
				// .getCityName()))) {
				// StoreInquiryCitys.mPremodel
				// .addFirst(((CityName) msg.obj));
				// }
				// }
				// }
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_city_ac);
		init();// 初始化控件
		initData();// 业务逻辑处理
		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		/* CacheTools.getUserData("userId"); */

	}

	/**
	 * 点击空白处关闭软键盘
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (getCurrentFocus() != null
					&& getCurrentFocus().getWindowToken() != null) {
				manager.hideSoftInputFromWindow(getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 初始化控件
	 */
	public void init() {
		// 最近三个位置的显示
		// premodel_linearLayout = (LinearLayout)
		// findViewById(R.id.premodel_linearLayout);
		// 当前位置的显示
		nowmodel_tv = (TextView) findViewById(R.id.nowmodel_tv);
		mEditText = (EditText) findViewById(R.id.search_view);

		premodel1_tv = (TextView) findViewById(R.id.premodel1_tv);
		premodel2_tv = (TextView) findViewById(R.id.premodel2_tv);
		premodel3_tv = (TextView) findViewById(R.id.premodel3_tv);

		mSubList = (ListView) findViewById(R.id.model_list1);
		mSubLayout = (LinearLayout) findViewById(R.id.model_linearlayout1);
		mImgHide = (ImageView) findViewById(R.id.img_hide1);
		mListView = (PinnedHeaderListView) findViewById(R.id.brand_display1);
		mListView.setTextFilterEnabled(true);
		mLetter = (BladeView) findViewById(R.id.brand_letterlistview1);

		mRecentList = dbCity.seltelCtiy();// 查询最近访问城市

		displayCity(mRecentList);// 显示城市

		nowmodel_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (nowmodel_tv.getText().toString().trim().equals("定位中.....")) {
				} else {
					intent = new Intent();
					intent.putExtra("city", city.trim()); // 返回定位城市
					intent.putExtra("province", province.trim()); // 返回定位城市
					intent.putExtra("district", district.trim()); // 返回定位城市
					setResult(RESULT_OK, intent);
					finishActivity();
				}
			}
		});

		premodel1_tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				cityRecent(mRecentList, 0);

			}
		});
		premodel2_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				cityRecent(mRecentList, 1);
			}
		});
		premodel3_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				cityRecent(mRecentList, 2);
			}
		});
		// 显示最近三个位置的布局容器
		// premodel_linearLayout = (LinearLayout)
		// findViewById(R.id.premodel_linearLayout);
		mHeaderTV.setText("城市选择");

		// 查看是否有当前城市的缓存，如果没有就重新定位，并讲定位结果存入缓存
		if (CacheTools.getUserData("city") == null) {
			getMyLocation();
		}
		// 设置当前城市
		city = CacheTools.getUserData("city");
		province = CacheTools.getUserData("province");
		district = CacheTools.getUserData("district");
		if (TextUtils.isEmpty(city)) {
			nowmodel_tv.setText("定位中.....");
		} else {
			nowmodel_tv.setText(city + district);
		}

		mBackBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finishActivity();
			}
		});
		// mContext = this;

	}

	private void initData() {
		mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
		mData = new ArrayList<CityName>();
		// 首字母集
		mSections = new ArrayList<String>();
		// 根据首字母存放数据
		mMap = new HashMap<String, List<CityName>>();
		// 首字母位置集
		mPositions = new ArrayList<Integer>();
		// 首字母对应的位置
		mIndexer = new HashMap<String, Integer>();
		if (CacheTools.getUserData("citysData") != null) {
			mCityNames = CityName.parses(CacheTools.getUserData("citysData")
					.toString());
			// 遍历存储实体类的集合
			for (CityName cityName : mCityNames) {
				// 获取实体类城市名的首汉字的首字母
				String firstLetter = PinYinUtils.getFirstSpell(
						cityName.getCityName()).toUpperCase();
				if (firstLetter.matches(FORMAT)) {
					// 当首字母已经存在，那就讲本首字母对应的城市存储到首字母对应的集合中
					if (mSections.contains(firstLetter)) {
						if (cityName.getCityName().equals("长春市")) {
							mMap.get("C").add(cityName);
						} else if (cityName.getCityName().equals("长沙市")) {
							mMap.get("C").add(cityName);
						} else if (cityName.getCityName().equals("长治市")) {
							mMap.get("C").add(cityName);
						} else if (cityName.getCityName().equals("厦门市")) {
							mMap.get("X").add(cityName);
						} else {
							mMap.get(firstLetter).add(cityName);
						}
					} else {

						// 当首字母不存在，那就在首字母集合中添加首字母，并且为本首字母添加一个集合用于存储存放的数据
						mSections.add(firstLetter);
						List<CityName> list = new ArrayList<CityName>();
						list.add(cityName);
						mMap.put(firstLetter, list);
						/*
						 * if(cityName.getCityName().equals("重庆市")){
						 * mMap.get("C").add(cityName); }
						 */
					}
				} else {
					// 热门车型
					if (mSections.contains("#")) {
						mMap.get("#").add(cityName);
					} else {
						mSections.add("#");
						List<CityName> list = new ArrayList<CityName>();
						list.add(cityName);
						mMap.put("#", list);
					}
				}
			}
			for (int i = 0; i < mMap.size(); i++) {
				if (mMap.get("Z").get(i).getCityName().equals("重庆市")) {
					CityName cityName = new CityName();
					cityName.setCityName(mMap.get("Z").get(i).getCityName());
					cityName.setId(mMap.get("Z").get(i).getId());
					cityName.setProvinceId(mMap.get("Z").get(i).getProvinceId());
					cityName.setProvinceName(mMap.get("Z").get(i)
							.getProvinceName());
					cityName.setProvinceRemark(mMap.get("Z").get(i)
							.getProvinceRemark());
					mMap.get("C").add(cityName);
					mMap.remove(mMap.get("Z").remove(i));
				}
			}
			// 遍历mMap的所有值，需要将值按字母的顺序排好序进行存储
			Set<String> set = mMap.keySet();
			List<String> sort = new ArrayList<String>();
			for (Object key : set) {
				sort.add(key + "");
			}
			Collections.sort(sort);

			for (int i = 0; i < sort.size(); i++) {
				// 遍历值的集合
				for (CityName cityName : mMap.get(sort.get(i))) {
					mData.add(cityName);
				}
			}
			// 对首字母集合进行排序，然后遍历首字母集合，将首字母放入列表进行显示
			Collections.sort(mSections);

			int position = 0;
			for (int i = 0, size = mSections.size(); i < size; i++) {
				mIndexer.put(mSections.get(i), position);// 存入map中，key为首字母字符串，value为首字母在listview中位置
				mPositions.add(position);// 首字母在listview中位置，存入list中
				position += mMap.get(mSections.get(i)).size();// 计算下一个首字母在listview的位置
			}
			// 在异步线程只能怪更新UI
			initView();

		} else {
			// 获取市
			requestNet(new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					switch (msg.what) {
					case Constant.NET_DATA_SUCCESS:

						try {
							JSONObject jo = new JSONObject(msg.obj.toString());
							mCityNames = CityName.parses(jo.getJSONArray(
									"cities").toString());

							// 遍历存储实体类的集合
							for (CityName cityName : mCityNames) {
								// 获取实体类城市名的首汉字的首字母
								String firstLetter = PinYinUtils.getFirstSpell(
										cityName.getCityName()).toUpperCase();
								if (firstLetter.matches(FORMAT)) {
									// 当首字母已经存在，那就讲本首字母对应的城市存储到首字母对应的集合中
									if (mSections.contains(firstLetter)) {
										mMap.get(firstLetter).add(cityName);
									} else {
										// 当首字母不存在，那就在首字母集合中添加首字母，并且为本首字母添加一个集合用于存储存放的数据
										mSections.add(firstLetter);
										List<CityName> list = new ArrayList<CityName>();
										list.add(cityName);
										mMap.put(firstLetter, list);
									}
								} else {
									// 热门车型
									if (mSections.contains("#")) {
										mMap.get("#").add(cityName);
									} else {
										mSections.add("#");
										List<CityName> list = new ArrayList<CityName>();
										list.add(cityName);
										mMap.put("#", list);
									}
								}
							}
							// 遍历mMap的所有值，需要将值按字母的顺序排好序进行存储
							Set<String> set = mMap.keySet();
							List<String> sort = new ArrayList<String>();
							for (Object key : set) {
								sort.add(key + "");
							}
							Collections.sort(sort);
							for (int i = 0; i < sort.size(); i++) {
								// 遍历值的集合
								for (CityName cityName : mMap.get(sort.get(i))) {
									mData.add(cityName);
								}
							}
							// 对首字母集合进行排序，然后遍历首字母集合，将首字母放入列表进行显示
							Collections.sort(mSections);

							int position = 0;
							for (int i = 0, size = mSections.size(); i < size; i++) {
								mIndexer.put(mSections.get(i), position);// 存入map中，key为首字母字符串，value为首字母在listview中位置
								mPositions.add(position);// 首字母在listview中位置，存入list中
								position += mMap.get(mSections.get(i)).size();// 计算下一个首字母在listview的位置
							}
							// 在异步线程只能怪更新UI
							initView();

						} catch (JSONException e) {
							e.printStackTrace();
						}
						break;
					}
				}
			}, null, NetworkUtil.GET_ALL_CITY, false, 0);

		}

	}

	/**
	 * 初始化控件
	 */
	private void initView() {

		mLetter.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(String s) {
				if (mIndexer.get(s) != null) {
					mListView.setSelection(mIndexer.get(s));
				}
			}
		});
		mAdapter = new CityAdapter(ChooseCityActivity1.this, mData, mSections,
				mPositions, mHandler, mEditText, mListView, mSubLayout);

		mListView.setAdapter(mAdapter); // 第一次绑定

		mListView.setOnScrollListener(mAdapter);
		mListView.setPinnedHeaderView(LayoutInflater.from(this)
				.inflate(R.layout.select_brand_item, mListView, false)
				.findViewById(R.id.title));

		mImgHide.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				StatService.onEvent(ChooseCityActivity1.this,
						"clicChooseCityHideIV", "点击选择城市隐藏图标");
				mSubLayout.setVisibility(View.GONE);
			}
		});

		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// 获取市：点击获取到的城市
				// final String province = mData.get(arg2).getCityName();
				// 获取区:点击的城市对应的区-（通过城市编号获取的区域）
				getDistrict(mData.get(arg2));
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		StatService.onPause(ChooseCityActivity1.this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		StatService.onResume(ChooseCityActivity1.this);
	}

	@Override
	public boolean onQueryTextChange(String arg0) {

		if (TextUtils.isEmpty(arg0)) {
			mListView.clearTextFilter();
		} else {
			mListView.setFilterText(arg0.toString());
		}
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	private List<String> getDistrict(final CityName cityname) {

		mDistricts = new ArrayList<String>();
		AjaxParams params = new AjaxParams();
		params.put("cityId", cityname.getId() + "");

		requestNet(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:
					MyLog.i("YUY", msg.obj.toString());

					try {
						JSONObject jo = new JSONObject(msg.obj.toString());
						mDistrictNames = DistrictName.parses(jo.getJSONArray(
								"districts").toString());

						if (mDistrictNames == null
								|| mDistrictNames.size() == 0) {
							Intent intent = new Intent();
							String district = "";
							intent.putExtra("city", cityname.getCityName());
							intent.putExtra("district", district);
							intent.putExtra("provinceId",
									cityname.getProvinceId() + "");
							intent.putExtra("province",
									cityname.getProvinceName());
							intent.putExtra("cityId", cityname.getId());
							intent.putExtra("districtId", "");
							cityDb(mRecentList, cityname.getId() + "",
									cityname.getProvinceId() + "", null,
									cityname.getProvinceName(),
									cityname.getCityName(), null);

							setResult(RESULT_OK, intent);
							finishActivity();
							return;
						}
						for (DistrictName DistrictNames : mDistrictNames) {
							// 获取区域名字的集合
							mDistricts.add(DistrictNames.getDistrictName());
						}

						mSubLayout.setVisibility(View.VISIBLE);
						if (mDistricts != null) {

							cityApapter = new CityChoiceAdapter(
									ChooseCityActivity1.this, mDistricts);
							mSubList.setAdapter(cityApapter);
							mSubList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

								@SuppressLint("SimpleDateFormat")
								@Override
								public void onItemClick(AdapterView<?> arg0,

								View arg1, int arg2, long arg3) {
									mSubLayout.setVisibility(View.GONE);
									intent = new Intent();

									List<LatelyCity> list = dbCity.seltelCtiy();

									String district = mDistricts.get(arg2);
									intent.putExtra("chooseDistrict",
											cityname.getCityName() + district);
									intent.putExtra("city",
											cityname.getCityName());
									intent.putExtra("district", district);
									intent.putExtra("provinceId",
											cityname.getProvinceId() + "");
									intent.putExtra("province",
											cityname.getProvinceName());
									intent.putExtra("cityId", mDistrictNames
											.get(arg2).getCityId() + "");
									intent.putExtra("districtId",
											mDistrictNames.get(arg2).getId()
													+ "");

									cityDb(list, mDistrictNames.get(arg2)
											.getCityId() + "",
											cityname.getProvinceId() + "",
											mDistrictNames.get(arg2).getId()
													+ "",
											cityname.getProvinceName(),
											cityname.getCityName(), district);
									setResult(RESULT_OK, intent);
									finishActivity();
								}
							});
						} else {
							// 没有区域的城市直接退出当前
							finishActivity();
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
				default:
					break;
				}
			}
		}, params, NetworkUtil.GET_DISTRICT_BY_CITY, false, 0);
		return mDistricts;
	}

	/**
	 * 保存到数据库
	 */
	public void cityDb(List<LatelyCity> list, String cityid, String provinceId,
			String districtId, String provinceName, String cityName,
			String district) {
		if (list.size() == 0) {
			dbCity.addCtiy(cityid, provinceId, districtId, provinceName,
					cityName, district);
		} else if (list.size() >= 3) {
			if (list.get(0).getCity().equals(cityName)
					|| list.get(1).getCity().equals(cityName)
					|| list.get(2).getCity().equals(cityName)) {

			} else {
				if (TextUtils.isEmpty(list.get(0).getDistrict())) {
					if (dbCity.deltelCtiy(list.get(0).getCity()) == 1) {
						dbCity.addCtiy(cityid, provinceId, districtId,
								provinceName, cityName, district);
					}
				} else {
					if (dbCity.deltelDistrict(list.get(0).getDistrict()) == 1) {
						dbCity.addCtiy(cityid, provinceId, districtId,
								provinceName, cityName, district);
					}
				}
			}

		} else if (list.size() == 3) {
			if (TextUtils.isEmpty(list.get(0).getDistrict())) {
				if (!list.get(0).getCity().equals(cityName)
						&& !list.get(1).getCity().equals(cityName)
						&& !list.get(2).getCity().equals(cityName)) {
					dbCity.addCtiy(cityid, provinceId, districtId,
							provinceName, cityName, district);
				} else {

				}
			} else if (!TextUtils.isEmpty(list.get(0).getDistrict())
					&& !TextUtils.isEmpty(list.get(1).getDistrict())
					&& !TextUtils.isEmpty(list.get(2).getDistrict())) {
				if (!list.get(0).getDistrict().equals(district)
						&& !list.get(1).getDistrict().equals(district)
						&& !list.get(2).getDistrict().equals(district)) {
					dbCity.addCtiy(cityid, provinceId, districtId,
							provinceName, cityName, district);
				} else {

				}
			} else if (!TextUtils.isEmpty(list.get(0).getDistrict())
					&& !TextUtils.isEmpty(list.get(1).getDistrict())
					&& !TextUtils.isEmpty(list.get(2).getDistrict())) {
				if (!list.get(1).getDistrict().equals(district)
						&& !list.get(0).getDistrict().equals(district)
						&& !list.get(2).getDistrict().equals(district)) {
					dbCity.addCtiy(cityid, provinceId, districtId,
							provinceName, cityName, district);
				} else {

				}
			} else if (TextUtils.isEmpty(list.get(1).getDistrict())) {
				if (!list.get(1).getCity().equals(cityName)
						&& !list.get(0).getCity().equals(cityName)
						&& list.get(2).getCity().equals(cityName)) {
					dbCity.addCtiy(cityid, provinceId, districtId,
							provinceName, cityName, district);
				} else {

				}
			} else if (!TextUtils.isEmpty(list.get(0).getDistrict())
					&& !TextUtils.isEmpty(list.get(1).getDistrict())
					&& !TextUtils.isEmpty(list.get(2).getDistrict())) {
				if (!list.get(2).getDistrict().equals(district)
						&& !list.get(0).getDistrict().equals(district)
						&& !list.get(1).getDistrict().equals(district)) {
					dbCity.addCtiy(cityid, provinceId, districtId,
							provinceName, cityName, district);
				} else {

				}
			} else if (TextUtils.isEmpty(list.get(2).getDistrict())) {
				if (!list.get(2).getCity().equals(cityName)
						&& !list.get(0).getCity().equals(cityName)
						&& !list.get(1).getCity().equals(cityName)) {
					dbCity.addCtiy(cityid, provinceId, districtId,
							provinceName, cityName, district);
				} else {

				}
			}
		} else if (list.size() == 2) {

			if (TextUtils.isEmpty(list.get(0).getDistrict())) {
				if (!list.get(0).getCity().equals(cityName)
						&& !list.get(1).getCity().equals(cityName)) {
					dbCity.addCtiy(cityid, provinceId, districtId,
							provinceName, cityName, district);
				} else {

				}
			} else if (!TextUtils.isEmpty(list.get(0).getDistrict())
					&& !TextUtils.isEmpty(list.get(1).getDistrict())) {
				if (!list.get(0).getDistrict().equals(district)
						&& !list.get(1).getDistrict().equals(district)) {
					dbCity.addCtiy(cityid, provinceId, districtId,
							provinceName, cityName, district);
				} else {

				}
			} else if (TextUtils.isEmpty(list.get(1).getDistrict())) {

				if (!list.get(1).getCity().equals(cityName)
						&& !list.get(0).getCity().equals(cityName)) {
					dbCity.addCtiy(cityid, provinceId, districtId,
							provinceName, cityName, district);
				} else {

				}
			} else if (!TextUtils.isEmpty(list.get(1).getDistrict())
					&& !TextUtils.isEmpty(list.get(0).getDistrict())) {
				if (!list.get(1).getDistrict().equals(district)
						&& !list.get(0).getDistrict().equals(district)) {
					dbCity.addCtiy(cityid, provinceId, districtId,
							provinceName, cityName, district);
				} else {

				}
			}
		} else if (list.size() == 1) {

			if (TextUtils.isEmpty(list.get(0).getDistrict())) {
				if (!list.get(0).getCity().equals(cityName)) {
					dbCity.addCtiy(cityid, provinceId, districtId,
							provinceName, cityName, district);
				}
			} else {
				if (!list.get(0).getDistrict().equals(district)) {
					dbCity.addCtiy(cityid, provinceId, districtId,
							provinceName, cityName, district);
				}
			}
		}
	}

	/**
	 * 显示城市
	 */
	private void displayCity(List<LatelyCity> mRecentList) {
		/**
		 * 根据不断的状态显示
		 */
		if (mRecentList.size() != 0) {
			if (mRecentList.size() == 1) {
				if (TextUtils.isEmpty(mRecentList.get(0).getDistrict())) {
					premodel1_tv.setText(mRecentList.get(0).getCity());
				} else {
					premodel1_tv.setText(mRecentList.get(0).getDistrict());
				}
				premodel1_tv.setVisibility(View.VISIBLE);
				premodel2_tv.setVisibility(View.GONE);
				premodel3_tv.setVisibility(View.GONE);
			} else if (mRecentList.size() == 2) {
				premodel1_tv.setVisibility(View.VISIBLE);
				premodel2_tv.setVisibility(View.VISIBLE);
				premodel3_tv.setVisibility(View.GONE);

				if (TextUtils.isEmpty(mRecentList.get(0).getDistrict())) {
					premodel2_tv.setText(mRecentList.get(0).getCity());

				} else {
					premodel2_tv.setText(mRecentList.get(0).getDistrict());
				}

				if (TextUtils.isEmpty(mRecentList.get(1).getDistrict())) {
					premodel1_tv.setText(mRecentList.get(1).getCity());
				} else {
					premodel1_tv.setText(mRecentList.get(1).getDistrict());
				}

			} else if (mRecentList.size() == 3) {
				premodel1_tv.setVisibility(View.VISIBLE);
				premodel2_tv.setVisibility(View.VISIBLE);
				premodel3_tv.setVisibility(View.VISIBLE);

				if (TextUtils.isEmpty(mRecentList.get(0).getDistrict())) {
					premodel3_tv.setText(mRecentList.get(0).getCity());
				} else {
					premodel3_tv.setText(mRecentList.get(0).getDistrict());
				}
				if (TextUtils.isEmpty(mRecentList.get(1).getDistrict())) {
					premodel2_tv.setText(mRecentList.get(1).getCity());
				} else {
					premodel2_tv.setText(mRecentList.get(1).getDistrict());
				}
				if (TextUtils.isEmpty(mRecentList.get(2).getDistrict())) {

					premodel1_tv.setText(mRecentList.get(2).getCity());
				} else {
					premodel1_tv.setText(mRecentList.get(2).getDistrict());

				}
			}
		} else {
			premodel1_tv.setVisibility(View.GONE);
			premodel2_tv.setVisibility(View.GONE);
			premodel3_tv.setVisibility(View.GONE);
		}
	}

	/**
	 * 最近访问城市
	 */
	public void cityRecent(List<LatelyCity> list, int recentId) {
		intent = new Intent();
		switch (recentId) {
		case 0:
			if (list.size() == 3) {
				intent.putExtra("chooseDistrict", list.get(2).getCity()
						+ list.get(2).getDistrict());
				intent.putExtra("city", list.get(2).getCity());
				intent.putExtra("district", list.get(2).getDistrict());
				intent.putExtra("provinceId", list.get(2).getProvinceId() + "");
				intent.putExtra("province", list.get(2).getProvince());
				intent.putExtra("cityId", list.get(2).getCityId() + "");
				intent.putExtra("districtId", list.get(2).getDistrictId() + "");
			} else if (list.size() == 2) {
				intent.putExtra("chooseDistrict", list.get(1).getCity()
						+ list.get(1).getDistrict());
				intent.putExtra("city", list.get(1).getCity());
				intent.putExtra("district", list.get(1).getDistrict());
				intent.putExtra("provinceId", list.get(1).getProvinceId() + "");
				intent.putExtra("province", list.get(1).getProvince());
				intent.putExtra("cityId", list.get(1).getCityId() + "");
				intent.putExtra("districtId", list.get(1).getDistrictId() + "");
			} else if (list.size() == 1) {
				intent.putExtra("chooseDistrict", list.get(0).getCity()
						+ list.get(0).getDistrict());
				intent.putExtra("city", list.get(0).getCity());
				intent.putExtra("district", list.get(0).getDistrict());
				intent.putExtra("provinceId", list.get(0).getProvinceId() + "");
				intent.putExtra("province", list.get(0).getProvince());
				intent.putExtra("cityId", list.get(0).getCityId() + "");
				intent.putExtra("districtId", list.get(0).getDistrictId() + "");
			}
			break;
		case 1:
			if (list.size() == 3) {
				intent.putExtra("chooseDistrict", list.get(1).getCity()
						+ list.get(1).getDistrict());
				intent.putExtra("city", list.get(1).getCity());
				intent.putExtra("district", list.get(1).getDistrict());
				intent.putExtra("provinceId", list.get(1).getProvinceId() + "");
				intent.putExtra("province", list.get(1).getProvince());
				intent.putExtra("cityId", list.get(1).getCityId() + "");
				intent.putExtra("districtId", list.get(1).getDistrictId() + "");
			} else if (list.size() == 2) {
				intent.putExtra("chooseDistrict", list.get(0).getCity()
						+ list.get(0).getDistrict());
				intent.putExtra("city", list.get(0).getCity());
				intent.putExtra("district", list.get(0).getDistrict());
				intent.putExtra("provinceId", list.get(0).getProvinceId() + "");
				intent.putExtra("province", list.get(0).getProvince());
				intent.putExtra("cityId", list.get(0).getCityId() + "");
				intent.putExtra("districtId", list.get(0).getDistrictId() + "");
			}
			break;
		case 2:
			intent.putExtra("chooseDistrict",
					list.get(0).getCity() + list.get(0).getDistrict());
			intent.putExtra("city", list.get(0).getCity());
			intent.putExtra("district", list.get(0).getDistrict());
			intent.putExtra("provinceId", list.get(0).getProvinceId() + "");
			intent.putExtra("province", list.get(0).getProvince());
			intent.putExtra("cityId", list.get(0).getCityId() + "");
			intent.putExtra("districtId", list.get(0).getDistrictId() + "");
			break;
		default:
			break;
		}
		setResult(RESULT_OK, intent);
		finishActivity();
	}
}
