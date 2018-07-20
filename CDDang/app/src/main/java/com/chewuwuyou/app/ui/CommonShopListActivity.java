package com.chewuwuyou.app.ui;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.CommonShopListAdapter;
import com.chewuwuyou.app.adapter.DrivingSchoolListAdapter;
import com.chewuwuyou.app.bean.Shop;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.Constant.ShopType;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.PullToRefreshBase;
import com.chewuwuyou.app.widget.PullToRefreshBase.OnRefreshListener2;
import com.chewuwuyou.app.widget.PullToRefreshListView;

/**
 * 
 * @Title:chedingdang
 * @Copyright:chewuwuyou
 * @Description:包含4s店、汽车美容、维修保养、汽车配件的商家查询
 * @author:yuyong
 * @date:2015-3-18下午5:44:47
 * @version:1.1.2
 */
public class CommonShopListActivity extends BaseActivity implements
		OnClickListener, OnRefreshListener2<ListView> {

	private TextView mTitleTV;
	private ImageButton mBackBtn;
	private SearchView mSearchShopSV;// 搜索店铺的view
	private PullToRefreshListView mPullToRefreshListView;// 搜索店铺的列表
	// private List<Shop> mAllShop;// 搜索的所有的店铺
	private List<Shop> mQueryShop;// 查询要显示的店铺
	private TextView mChooseCityTV; // 选择城市
	private RelativeLayout mChooseCarBrandRL;// 选择品牌
	private TextView mShopDiscription;// 店铺申明描述
	private CommonShopListAdapter mAdapter;// 店铺adapter
	private ImageView mShopIconIV;// 店铺图标
	private TextView mCarBrandTV;// 选择品牌名称
	private int shopType;// 店铺类型
	private String mCity, mProvince;// 查询的城市
	private JSONObject mJsonObject;// 驾校查询的数据
	private String mLat, mLng;// 定位城市的经纬度
	private int shopImgId;// 店铺展示的图标
	private ImageView mCheckMapIV;// 查看地图标注
	private String brand = "";// 选择的品牌查询
	private int mCurcor = 0;// 翻页要用
	private boolean mIsRefreshing = false;// 翻页要用
	private List<Shop> mData;

	private String nowLoc = "1";
	private boolean mIsSetEmptyTV = false;
	private JSONArray mJsonArray;
	private String str;
	private DrivingSchoolListAdapter adapter;
	private RelativeLayout mTitleHeight;//标题布局高度
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_shop_list_ac);
		initView();
	}

	private void initView() {
		mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
		mCity = CacheTools.getUserData("city");
		mProvince = CacheTools.getUserData("province");
		mLat = CacheTools.getUserData("Lat");
		mLng = CacheTools.getUserData("Lng");
		if (TextUtils.isEmpty(mCity)) {
			mCity = "成都市";
		}else if(TextUtils.isEmpty(mProvince)){
			mProvince = "四川省";
		}
		mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
		mBackBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mSearchShopSV = (SearchView) findViewById(R.id.search_shop_sv);
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.shop_list);
		mChooseCityTV = (TextView) findViewById(R.id.choose_city_tv);
		mChooseCarBrandRL = (RelativeLayout) findViewById(R.id.choose_car_brand_rl);
		mShopDiscription = (TextView) findViewById(R.id.shop_state_description_tv);
		mShopIconIV = (ImageView) findViewById(R.id.shop_icon_iv);
		mCarBrandTV = (TextView) findViewById(R.id.car_brand_tv);
		mCheckMapIV = (ImageView) findViewById(R.id.map_iv);

		if (TextUtils.isEmpty(CacheTools.getUserData("province"))
				&& TextUtils.isEmpty(CacheTools.getUserData("city"))) {
			mChooseCityTV.setText("定位失败");
			if (!TextUtils.isEmpty(mCity)&&!TextUtils.isEmpty(mProvince)) {
				mChooseCityTV.setText(mProvince + mCity);
			} else {
				AlertDialog.Builder dialog = new AlertDialog.Builder(
						CommonShopListActivity.this);
				dialog.setTitle("定位信息");
				dialog.setMessage("暂未定位到您的位置信息！");
				dialog.setPositiveButton("重新定位",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								getMyLocation();
								arg0.dismiss();
							}
						});
				dialog.setNegativeButton("选择地区",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								Intent intent = new Intent(
										CommonShopListActivity.this,
										ChooseCityActivity.class);
								startActivityForResult(intent, 20);
								arg0.dismiss();
							}
						});
				dialog.show();
			}
		} else {
			mChooseCityTV.setText(CacheTools.getUserData("province")
					+ CacheTools.getUserData("city"));
		}

		mCheckMapIV.setOnClickListener(this);
		mBackBtn.setOnClickListener(this);
		mChooseCityTV.setOnClickListener(this);
		mCarBrandTV.setOnClickListener(this);
		mPullToRefreshListView.setOnRefreshListener(this);
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
		mQueryShop = new ArrayList<Shop>();
		shopType = getIntent().getExtras().getInt("shopType");// 判断是通过什么店跳转过来

		switch (shopType) {
		case ShopType.FOURSSTORES:
			mSearchShopSV.setQueryHint(getResources().getString(
					R.string.search_query_shop_hint));
			shopImgId = R.drawable.icon_qcmr;
			mTitleTV.setText(R.string.four_shop_query_title);
			mShopDiscription.setText(R.string.fours_shop_description);
			mShopIconIV.setImageResource(R.drawable.icon_4s);
			// initFoursStores(city, "1", brand);
			break;
		case ShopType.CARBEAUTY:
			mSearchShopSV.setQueryHint(getResources().getString(
					R.string.search_query_shop_hint));
			shopImgId = R.drawable.icon_qcmr;
			mTitleTV.setText(R.string.carbeauty_title);
			mShopDiscription.setText(R.string.carbeauty_description);
			mShopIconIV.setImageResource(R.drawable.icon_xiche);
			mChooseCarBrandRL.setVisibility(View.GONE);
			// initCarBeauty(city, "1", "");
			break;
		case ShopType.MAINTENANCE:
			mSearchShopSV.setQueryHint(getResources().getString(
					R.string.search_query_shop_hint));
			shopImgId = R.drawable.icon_carwx;
			mTitleTV.setText(R.string.maintenance_title);
			mShopDiscription.setText(R.string.maintenance_description);
			mShopIconIV.setImageResource(R.drawable.icon_weixiu);
			mChooseCarBrandRL.setVisibility(View.GONE);
			// initMainTenance(city, "1", "");
			break;
		case ShopType.AUTOPARTS:
			mSearchShopSV.setQueryHint(getResources().getString(
					R.string.search_query_shop_hint));
			shopImgId = R.drawable.icon_qcmr;
			mTitleTV.setText(R.string.autoparts_title);
			mShopDiscription.setText(R.string.autoparts_descriptin);
			mShopIconIV.setImageResource(R.drawable.icon_peijian);
			// initAutoParts(city, "1", brand);
			break;
		case ShopType.DRIVING_SCHOOL:
			mSearchShopSV.setQueryHint(getResources().getString(
					R.string.search_query_jiaxiao_hint));
			shopImgId = R.drawable.icon_qcmr;
			initDrivingSchool();
			break;
		}
		mSearchShopSV.setSubmitButtonEnabled(false);
		if (mData == null) {
			mData = new ArrayList<Shop>();
		}
		if (mAdapter == null) {
			mAdapter = new CommonShopListAdapter(CommonShopListActivity.this,
					mQueryShop, shopImgId);
		}
		mPullToRefreshListView.setAdapter(mAdapter);
		getDate(true);

		mSearchShopSV.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String newText) {
				try {
					if (TextUtils.isEmpty(newText)) {
						mPullToRefreshListView.getRefreshableView()
								.clearTextFilter();
						mQueryShop.clear();
						mQueryShop.addAll(mData);
						mAdapter.notifyDataSetChanged();

					} else {
						mPullToRefreshListView.getRefreshableView()
								.setFilterText(newText);
						if (null != mData) {
							mQueryShop.clear();
							for (Shop shop : mData) {
								if (shop.getTitle().contains(newText)
										|| shop.getCity().contains(newText)
										|| shop.getTelephone()
												.contains(newText)
										|| shop.getMobile().contains(newText)) {
									mQueryShop.add(shop);
								}
							}

						}
						mAdapter.notifyDataSetChanged();
					}
				} catch (Exception e) {
					MyLog.i("YUY",
							"ShopQuery   e.getLocalizedMessage() = "
									+ e.getLocalizedMessage());
					e.printStackTrace();
				}

				return false;
			}

			@Override
			public boolean onQueryTextChange(String arg0) {
				JSONArray jArray = new JSONArray();
				try {

					for (int i = 0; i < mJsonArray.length(); i++) {
						if (mJsonArray.getJSONArray(i).getString(0)
								.contains(arg0)) {

							jArray.put(mJsonArray.getJSONArray(i));
						}
						adapter = new DrivingSchoolListAdapter(
								CommonShopListActivity.this, jArray,
								R.drawable.icon_qcmr);
						mPullToRefreshListView.setAdapter(adapter);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				return false;
			}
		});
	}

	private void getDate(final boolean isRefresh) {
		if (isRefresh) {
			mCurcor = 0;
		}

		AjaxParams params = new AjaxParams();
		switch (shopType) {
		case ShopType.FOURSSTORES:
			// shopImgId = R.drawable.icon_carmr;
			// initFoursStores(city, "1", brand);
			params.put("country", mCity);
			params.put("type", "4");
			params.put("distance", "20000");
			params.put("nowLoc", nowLoc);
			params.put("brand", brand);
			params.put("lng", mLng);
			params.put("lat", mLat);
			break;
		case ShopType.CARBEAUTY:
			// shopImgId = R.drawable.icon_carmr;
			// initCarBeauty(city, "1", "");
			MyLog.e("YUY", "查询维修美容参数 = " + "country = " + mCity + " nowLoc = "
					+ nowLoc + " brand = " + brand + " lng = " + mLng
					+ " lat = " + mLat);
			params.put("country", mCity);
			params.put("type", "2");
			params.put("distance", "20000");
			params.put("nowLoc", nowLoc);
			params.put("brand", "");
			params.put("lng", mLng);
			params.put("lat", mLat);
			break;
		case ShopType.MAINTENANCE:
			// shopImgId = R.drawable.icon_carwx;
			// initMainTenance(city, "1", "");
			params.put("country", mCity);
			params.put("type", "1");
			params.put("distance", "20000");
			params.put("nowLoc", nowLoc);
			params.put("brand", "");
			params.put("lng", mLng);
			params.put("lat", mLat);
			break;
		case ShopType.AUTOPARTS:
			// shopImgId = R.drawable.icon_carmr;
			// initAutoParts(city, "1", brand);
			params.put("country", mCity);
			params.put("type", "3");
			params.put("distance", "20000");
			params.put("nowLoc", nowLoc);
			params.put("lng", mLng);
			params.put("lat", mLat);
			params.put("brand", "");
			break;
		case ShopType.DRIVING_SCHOOL:
			shopImgId = R.drawable.icon_qcmr;
			initDrivingSchool();
			break;
		}
		if (shopType != ShopType.DRIVING_SCHOOL) {
			mPullToRefreshListView.setRefreshing();
			params.put("start", String.valueOf(mCurcor));
			requestNet(new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					if (!mIsSetEmptyTV) {
						TextView tv = new TextView(CommonShopListActivity.this);
						tv.setGravity(Gravity.CENTER);
						tv.setText("没有商铺");
						tv.setTextColor(getResources().getColor(
								R.color.empty_text_color));
						mPullToRefreshListView.setEmptyView(tv);
						mIsSetEmptyTV = true;
					}

					switch (msg.what) {
					case Constant.NET_DATA_SUCCESS:
						mIsRefreshing = false;
						mPullToRefreshListView.onRefreshComplete();
						List<Shop> mRefreshData = Shop.parseList(msg.obj
								.toString());
						if (isRefresh) {
							mPullToRefreshListView.onLoadMore();
							mData.clear();
							mQueryShop.clear();
						} else {
							// 是加载后面的订单
							if (mRefreshData == null) {
								ToastUtil.toastShow(
										CommonShopListActivity.this, "没有更多数据了");
								return;
							}
						}
						mData.addAll(mRefreshData);
						mQueryShop.addAll(mRefreshData);
						mAdapter.notifyDataSetChanged();
						mCurcor = mData.size();
						if (mRefreshData.size() < 20) {
							mPullToRefreshListView.onLoadComplete();
						}
						break;

					default:
						mPullToRefreshListView.onLoadComplete();
						break;
					}
				}
			}, params, NetworkUtil.NATIVE_SERVICE_BUSINESS, false, 1);

			mPullToRefreshListView
					.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
//							Intent intent = new Intent(
//									CommonShopListActivity.this,
//									ShopDetailsActivity.class);
//							intent.putExtra("shopType", shopType);
//							Bundle bundle = new Bundle();
//							bundle.putSerializable(Constant.SHOP_SER_KEY,
//									mQueryShop.get(arg2 - 1));
//							intent.putExtras(bundle);
//							startActivity(intent);
						}
					});
		}

	}

	/**
	 * 驾校查询
	 */
	@SuppressWarnings("rawtypes")
	private void initDrivingSchool() {
		mJsonArray = new JSONArray();
		mPullToRefreshListView.disabledToPull();
		mTitleTV.setText(R.string.driving_school_query);
		mShopDiscription.setVisibility(View.GONE);
		mShopIconIV.setVisibility(View.GONE);
		mChooseCarBrandRL.setVisibility(View.GONE);
		findViewById(R.id.sm_tv).setVisibility(View.GONE);
		try {
			mJsonObject = new JSONObject(getFromAssets("jiaxiao1.json"));
			Iterator iter = mJsonObject.keys();
			while (iter.hasNext()) {
				str = (String) iter.next();
				if (mCity.equals(str)) {
					mJsonArray = mJsonObject.getJSONArray(str);
				}
			}
			adapter = new DrivingSchoolListAdapter(CommonShopListActivity.this,mJsonArray, R.drawable.icon_qcmr);
			mPullToRefreshListView.setAdapter(adapter);
			/**
			 * 查看驾校详情
			 */
			mPullToRefreshListView
					.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
//							try {
////								Intent intent = new Intent(
////										CommonShopListActivity.this,
////										ShopDetailsActivity.class);
////								intent.putExtra("data", mJsonArray
////										.getJSONArray(arg2 - 1).toString());
////								intent.putExtra("isDrivingSchool", 1);
////								intent.putExtra("shopType", shopType);
////								startActivity(intent);
//							} catch (JSONException e) {
//								e.printStackTrace();
//							}
						}
					});
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (50 == resultCode) {
			// try {

			brand = data.getStringExtra("brandname");
			mCarBrandTV.setText(brand);
			// InputStream is = getAssets().open(
			// "logo/" + data.getStringExtra("brandlogo"));
			// mCarBrandIV.setImageBitmap(BitmapFactory.decodeStream(is));
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			nowLoc = "0";
			getDate(true);
			// switch (shopType) {
			// case ShopType.FOURSSTORES:// 4S店
			// shopImgId = R.drawable.icon_carmr;
			// initFoursStores(city, "0", brand);
			// break;
			// case ShopType.CARBEAUTY:// 汽车美容
			// shopImgId = R.drawable.icon_carmr;
			// initCarBeauty(city, "0", "");
			// break;
			// case ShopType.MAINTENANCE:// 维修美容
			// shopImgId = R.drawable.icon_carwx;
			// initMainTenance(city, "0", "");
			// break;
			// case ShopType.AUTOPARTS: // 汽车配件
			// shopImgId = R.drawable.icon_carmr;
			// initAutoParts(city, "0", brand);
			// break;
			// case ShopType.DRIVING_SCHOOL:// 驾校查询
			// shopImgId = R.drawable.icon_carjx;
			// initDrivingSchool();
			// break;
			//
			// }
		}

		if (RESULT_OK == resultCode) {
			mChooseCityTV.setText(data.getStringExtra("province")
					+ data.getStringExtra("city"));
			mCity = data.getStringExtra("city");
			nowLoc = "0";
			getDate(true);
			// switch (shopType) {
			// case ShopType.FOURSSTORES:// 4S店
			// shopImgId = R.drawable.icon_carmr;
			// initFoursStores(city, "0", brand);
			// break;
			// case ShopType.CARBEAUTY:// 汽车美容
			// shopImgId = R.drawable.icon_carmr;
			// initCarBeauty(city, "0", "");
			// break;
			// case ShopType.MAINTENANCE:// 维修美容
			// shopImgId = R.drawable.icon_carwx;
			// initMainTenance(city, "0", "");
			// break;
			// case ShopType.AUTOPARTS: // 汽车配件
			// shopImgId = R.drawable.icon_carmr;
			// initAutoParts(city, "0", brand);
			// break;
			// case ShopType.DRIVING_SCHOOL:// 驾校查询
			// shopImgId = R.drawable.icon_carjx;
			// initDrivingSchool();
			// break;
			//
			// }
		}

	}

	/**
	 * 汽车配件
	 * 
	 * @param city
	 *            查询城市
	 * @param nowLoc
	 *            是否为当前所在城市查询，1是，0不是
	 * @param brand
	 *            通过品牌查询 首次进入设brand为空
	 */
	// private void initAutoParts(String city, String nowLoc, String brand) {
	// mTitleTV.setText(R.string.autoparts_title);
	// mShopDiscription.setText(R.string.autoparts_descriptin);
	// mShopIconIV.setImageResource(R.drawable.icon_peijian);
	// AjaxParams params = new AjaxParams();
	// params.put("country", city);
	// params.put("type", "3");
	// params.put("distance", "20000");
	// params.put("nowLoc", nowLoc);
	// if (nowLoc.equals("1")) {
	// params.put("lng", mLng);
	// params.put("lat", mLat);
	// }
	//
	// requestNet(mHandler, params, NetworkUtil.NATIVE_SERVICE_BUSINESS,
	// false, 0);
	// }

	/**
	 * 维修保养
	 * 
	 * @param city
	 *            查询城市
	 * @param nowLoc
	 *            是否为当前所在城市查询，1是，0不是
	 * @param brand
	 *            通过品牌查询 首次进入设brand为空
	 */
	// private void initMainTenance(String city, String nowLoc, String brand) {
	// mTitleTV.setText(R.string.maintenance_title);
	// mShopDiscription.setText(R.string.maintenance_description);
	// mShopIconIV.setImageResource(R.drawable.icon_weixiu);
	// mChooseCarBrandRL.setVisibility(View.GONE);
	// AjaxParams params = new AjaxParams();
	// params.put("country", city);
	// params.put("type", "1");
	// params.put("distance", "20000");
	// params.put("nowLoc", nowLoc);
	// params.put("brand", brand);
	// if (nowLoc.equals("1")) {
	// params.put("lng", mLng);
	// params.put("lat", mLat);
	//
	// }
	// requestNet(mHandler, params, NetworkUtil.NATIVE_SERVICE_BUSINESS,
	// false, 0);
	// }

	/**
	 * 汽车美容
	 * 
	 * @param city
	 *            查询城市
	 * @param nowLoc
	 *            是否为当前所在城市查询，1是，0不是
	 * @param brand
	 *            通过品牌查询 首次进入设brand为空
	 */
	// private void initCarBeauty(String city, String nowLoc, String brand) {
	//
	// mTitleTV.setText(R.string.carbeauty_title);
	// mShopDiscription.setText(R.string.carbeauty_description);
	// mShopIconIV.setImageResource(R.drawable.icon_xiche);
	// mChooseCarBrandRL.setVisibility(View.GONE);
	// AjaxParams params = new AjaxParams();
	// params.put("country", city);
	// params.put("type", "2");
	// params.put("distance", "20000");
	// params.put("nowLoc", nowLoc);
	// params.put("brand", brand);
	// if (nowLoc.equals("1")) {
	// params.put("lng", mLng);
	// params.put("lat", mLat);
	//
	// }
	// requestNet(mHandler, params, NetworkUtil.NATIVE_SERVICE_BUSINESS,
	// false, 0);
	// }

	/**
	 * 4S店
	 * 
	 * @param city
	 *            查询城市
	 * @param nowLoc
	 *            是否为当前所在城市查询，1是，0不是
	 * @param brand
	 *            通过品牌查询 首次进入设brand为空
	 */
	// private void initFoursStores(String city, String nowLoc, String brand) {
	// mTitleTV.setText(R.string.four_shop_query_title);
	// mShopDiscription.setText(R.string.fours_shop_description);
	// mShopIconIV.setImageResource(R.drawable.icon_4s);
	// AjaxParams params = new AjaxParams();
	// params.put("country", city);
	// params.put("type", "4");
	// params.put("distance", "20000");
	// params.put("nowLoc", nowLoc);
	// params.put("brand", brand);
	// if (nowLoc.equals("1")) {
	// params.put("lng", mLng);
	// params.put("lat", mLat);
	//
	// }
	// requestNet(mHandler, params, NetworkUtil.NATIVE_SERVICE_BUSINESS,
	// false, 0);
	// }

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			finishActivity();
			break;
		case R.id.choose_city_tv:
			Intent intent = new Intent(CommonShopListActivity.this,
					ChooseCityActivity.class);
			startActivityForResult(intent, 30);
			break;
		case R.id.car_brand_tv:
			Intent chooseIntent = new Intent(CommonShopListActivity.this,
					SelectBrandActivity.class);
			startActivityForResult(chooseIntent, 20);
			break;
//		case R.id.map_iv:
//			Intent shopMapntent = new Intent(CommonShopListActivity.this,
//					CommonShopMapActivity.class);
//			shopMapntent.putExtra("flag", redicTo.CHECK_LABLE);
//			shopMapntent.putExtra("shopList", mQueryShop.toArray());
//			shopMapntent.putExtra("shopType", shopType);
//			startActivity(shopMapntent);
//			break;
		default:
			break;
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		String label = DateUtils.formatDateTime(getApplicationContext(),
				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);

		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

		if (!mIsRefreshing) {
			mIsRefreshing = true;
			getDate(true);
		} else {
			mPullToRefreshListView.onRefreshComplete();
		}

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!mIsRefreshing) {
			mIsRefreshing = true;
			getDate(false);
		} else {
			mPullToRefreshListView.onRefreshComplete();
		}
	}

}
