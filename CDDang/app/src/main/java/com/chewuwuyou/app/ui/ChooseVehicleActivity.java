package com.chewuwuyou.app.ui;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.ChooseVehicleAdapter;
import com.chewuwuyou.app.bean.CarCityItem;
import com.chewuwuyou.app.bean.CarModel;
import com.chewuwuyou.app.bean.CarProvinceItem;
import com.chewuwuyou.app.bean.IllegalScore;
import com.chewuwuyou.app.bean.Vehicle;
import com.chewuwuyou.app.bean.VehicleIllegal;
import com.chewuwuyou.app.bean.Violation;
import com.chewuwuyou.app.bean.ViolationDate;
import com.chewuwuyou.app.bean.WeizhangChildItem;
import com.chewuwuyou.app.bean.WeizhangGroupItem;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.DialogUtil;
import com.chewuwuyou.app.utils.ErrorCodeUtil;
import com.chewuwuyou.app.utils.MD5Util;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.widget.PullToRefreshBase;
import com.chewuwuyou.app.widget.PullToRefreshBase.OnRefreshListener;
import com.chewuwuyou.app.widget.PullToRefreshListView;

/**
 * @describe:违章查询选择车辆
 * @author:yuyong
 * @version 1.1.0
 * @created:2014-11-21上午11:56:28
 */
public class ChooseVehicleActivity extends BaseActivity implements
		OnRefreshListener<ListView> {
	@ViewInject(id = R.id.sub_header_bar_left_ibtn, click = "onAction")
	private ImageButton mBackBtn;
	@ViewInject(id = R.id.sub_header_bar_tv)
	private TextView mHeaderTV;
	@ViewInject(id = R.id.vehicle_empty_tv)
	private TextView mEmptyTV;
	private List<Vehicle> mVehicles;
	private PullToRefreshListView mPullToRefreshListView;
	private List<CarModel> carModels;
	private ChooseVehicleAdapter mAdapter;
	@ViewInject(id = R.id.sub_header_bar_right_tv, click = "onAction")
	private TextView mAddVehicleBtn;
	private VehicleIllegal mVehicleIllegal;// 车辆违章信息实体
	private String illegalNr = "";
	private List<VehicleIllegal> mVehicleIllegals;
	private ViolationDate mViolation = null;
	private List<IllegalScore> mIllegalScores;
	private boolean mIsRefreshing = false;
	private List<CarProvinceItem> mData;
	private String mVehicleType;// 车辆类型:(小型汽车：02)
	private String mVehiclePlateNum;// 车牌号吗
	private String mFrameNum;// 车架号
	private String mEngineNum;// 发动机号
	private String mQueryIllegalUrl;
	private String mCityId;
	private RelativeLayout mTitleHeight;// 标题布局高度
	public static ChooseVehicleActivity mChooseVehicleActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exchange_vehicle_ac);
		mChooseVehicleActivity = this;
		initView();

	}

	/**
	 * 初始化布局
	 */
	private void initView() {
		mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);// 根据不同手机判断
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.vehicle_lv);
		mHeaderTV.setText(R.string.vehicle_manager_title);
		mVehicleIllegals = new ArrayList<VehicleIllegal>();
		if (mAdapter == null) {
			mAdapter = new ChooseVehicleAdapter(ChooseVehicleActivity.this,
					mVehicleIllegals);
			mPullToRefreshListView.setAdapter(mAdapter);
		}
		mPullToRefreshListView.setOnRefreshListener(this);

		mPullToRefreshListView
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Intent intent = new Intent(ChooseVehicleActivity.this,
								WeizhangActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable(Constant.VEHICLE_SER,
								mVehicles.get(arg2 - 1));
						intent.putExtras(bundle);
						startActivity(intent);
					}
				});

		mData = CarProvinceItem
				.parseList(getFromAssets("get_all_configs.json"));
		carModels = CarModel.parseBrands(getFromAssets("mobile_models"));
		mAddVehicleBtn.setVisibility(View.VISIBLE);
		mAddVehicleBtn.setText("添加");
	}

	/**
	 * 访问网络绑定数据
	 */
	@SuppressWarnings("static-access")
	private void getVehicle() {
		mPullToRefreshListView.setRefreshing();
		new NetworkUtil().postMulti(NetworkUtil.VEHICLE_MANAGE, null,
				new AjaxCallBack<String>() {
					@Override
					public void onSuccess(String t) {
						super.onSuccess(t);
						mIsRefreshing = false;
						mPullToRefreshListView.onRefreshComplete();
						// dismissWaitingDialog();
						try {
							JSONObject jo = new JSONObject(t);
							if (jo.getInt("result") == 1) {
								MyLog.i("YUY", "======车辆信息===" + jo.toString());
								mVehicles = Vehicle.parseList(jo
										.getString("data"));
								if (mVehicles != null && !mVehicles.isEmpty()) {
									mEmptyTV.setVisibility(View.GONE);
								} else {
									mEmptyTV.setVisibility(View.VISIBLE);
								}
								mVehicleIllegals.clear();
								if (mVehicles != null) {
									Vehicle vehicle = null;
									for (int i = 0, size = mVehicles.size(); i < size; i++) {
										vehicle = mVehicles.get(i);
										mVehicleIllegal = new VehicleIllegal();
										mVehicleIllegal.setPlateNumber(vehicle
												.getPlateNumber());
										mVehicleIllegal
												.setVehicleImageUrl(getVehicleImage(
														vehicle.getBrand(),
														vehicle.getModelNumber(),
														carModels));
										mVehicleIllegal.setNoteName(vehicle
												.getNoteName());
										mVehicleIllegals.add(mVehicleIllegal);
										queryIllegal(i);
									}

								}
							} else if (jo.getJSONObject("data").getInt(
									"errorCode") == ErrorCodeUtil.IndividualCenter.LOGINED_IN_OTHER_PHONE) {
								DialogUtil
										.loginInOtherPhoneDialog(ChooseVehicleActivity.this);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						super.onFailure(t, errorNo, strMsg);
						// dismissWaitingDialog();
						mPullToRefreshListView.onRefreshComplete();
					}

					@Override
					public void onStart() {
						super.onStart();
						// showWaitingDialog();
					}

				});
	}

	public void onAction(View v) {
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			finish();
			break;
		case R.id.sub_header_bar_right_tv:
			Intent intent = new Intent(ChooseVehicleActivity.this,
					AddVehicleActivity.class);
			intent.putExtra("tag", 1);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(ChooseVehicleActivity.this);
		// if (!mIsRefreshing) {
		// mIsRefreshing = true;
		getVehicle();
		// }

	}

	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPause(ChooseVehicleActivity.this);
	}

	public void queryIllegal(final int position) {

		String province_short_name = String.valueOf(mVehicles.get(position)
				.getPlateNumber().charAt(0));
		String car_head = mVehicles.get(position).getPlateNumber()
				.substring(0, 1);
		if (province_short_name.equals("苏")) {// 使用江苏的查询方式 现同意修改为车首页接口查询
			queryJiangSuIllegal(position);
		} else {

			mVehicleType = mVehicles.get(position).getHpzl();
			mVehiclePlateNum = mVehicles.get(position).getPlateNumber();
			mFrameNum = mVehicles.get(position).getFrameNumber();
			mEngineNum = mVehicles.get(position).getEngineNumber();
			mCityId = String.valueOf(getCityId(province_short_name, car_head));
			String carInfoDetails = "{hphm=" + mVehiclePlateNum + "&classno="
					+ mFrameNum + "&engineno=" + mEngineNum + "&city_id="
					+ mCityId + "&car_type=" + mVehicleType + "}";

			MyLog.i("YUY", "carInfoDetails  = " + carInfoDetails);
			String carInfo = null;
			try {
				carInfo = URLEncoder.encode(carInfoDetails, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			long time = System.currentTimeMillis();
			String sign = "888" + carInfoDetails + String.valueOf(time)
					+ "98d5ad2b0bd453805a2fdd445e9e8085";

			mQueryIllegalUrl = NetworkUtil.CSY_QUERY_ILLEGAL_URL + "timestamp="
					+ String.valueOf(time) + "&car_info=" + carInfo + "&sign="
					+ MD5Util.getMD5(sign) + "&app_id=888";
			MyLog.i("YUY", "请求全地址 = " + mQueryIllegalUrl);

			NetworkUtil.postNoHeader(mQueryIllegalUrl, null,
					new AjaxCallBack<String>() {
						@Override
						public void onSuccess(String t) {
							super.onSuccess(t);
							MyLog.i("YUY", "违章详情   = " + t);
							WeizhangGroupItem weizhangGroupItem = WeizhangGroupItem
									.parse(t.toString());
							List<WeizhangGroupItem> weiZhangGroups = new ArrayList<WeizhangGroupItem>();
							weiZhangGroups.add(weizhangGroupItem);
							mVehicles.get(position).setWeiZhangGroups(
									weiZhangGroups);
							VehicleIllegal vehicleIllegal = mVehicleIllegals
									.get(position);
							vehicleIllegal.setVehicleIllegalNum(String
									.valueOf(weizhangGroupItem.getCount()));
							vehicleIllegal.setVehicleIllegalScore(String
									.valueOf(weizhangGroupItem.getTotal_score()));
							vehicleIllegal.setVehicleIllegalMoney(String
									.valueOf(weizhangGroupItem.getTotal_money()));
							mAdapter.notifyDataSetChanged();
						}

						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							super.onFailure(t, errorNo, strMsg);
							MyLog.i("YUY", "查询失败" + strMsg);
						}
					});
		}

	}

	// 查询江苏的违章 现统一改为车首页接口查询,留着备用
	@SuppressWarnings("static-access")
	public void queryJiangSuIllegal(final int position) {
		final AjaxParams params = new AjaxParams();
		Vehicle vehicle = mVehicles.get(position);
		String plateNumber = vehicle.getPlateNumber();
		String frameNumber = vehicle.getFrameNumber();
		String engineNumber = vehicle.getEngineNumber();
		params.put("plateNumber", plateNumber);
		if (plateNumber.contains("苏E") && frameNumber.length() > 6) {
			params.put(
					"engineNumber",
					frameNumber.substring(frameNumber.length() - 7,
							frameNumber.length()));
		} else {

			if (engineNumber.length() != 0) {
				params.put("engineNumber", engineNumber.substring(
						engineNumber.length() - 6, engineNumber.length()));
			}

		}
		new NetworkUtil().postMulti(NetworkUtil.VIOLATION_QUERY_URL, params,
				new AjaxCallBack<String>() {
					@Override
					public void onSuccess(String t) {
						super.onSuccess(t);
						MyLog.i("YUY", "车辆违章信息 = " + t);
						try {
							JSONObject jo = new JSONObject(t.toString());
							if (jo.getInt("result") == 1) {
								mViolation = ViolationDate.parse(jo
										.getString("data"));
								if (Integer.parseInt(mViolation.getCount()) > 0) {
									mVehicleIllegals.get(position)
											.setVehicleIllegalNum(
													mViolation.getCount());
								} else {
									mVehicleIllegals.get(position)
											.setVehicleIllegalNum("0");
								}

								double illegalMoney = 0.0;
								illegalNr = "";
								for (int i = 0, size = mViolation.getItem()
										.size(); i < size; i++) {
									illegalMoney += Double.valueOf(mViolation
											.getItem().get(i).getWfje());
									illegalNr += mViolation.getItem().get(i)
											.getWfnr()
											+ "-";
								}
								if (illegalMoney > 0) {
									mVehicleIllegals
											.get(position)
											.setVehicleIllegalMoney(
													String.valueOf(illegalMoney));
								} else {
									mVehicleIllegals.get(position)
											.setVehicleIllegalMoney("0");
								}

							}
							if (!TextUtils.isEmpty(illegalNr)) {
								getScoreByIllegalDe(illegalNr, position,
										mViolation);
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}

						mAdapter.notifyDataSetChanged();
					}
				});
	}

	/**
	 * 根据违章服务的内容查询扣分
	 * 
	 * @param illegalDetails
	 *            :违章详情
	 * @param illegalMoney
	 *            :违章金额
	 */
	@SuppressWarnings("static-access")
	private void getScoreByIllegalDe(String illegalDetails, final int position,
			final ViolationDate mViolation) {
		AjaxParams params = new AjaxParams();
		params.put("ids",
				illegalDetails.substring(0, illegalDetails.length() - 1));
		new NetworkUtil().postMulti(NetworkUtil.QUERY_SCORE, params,
				new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {
						super.onSuccess(t);
						int sumScore = 0;
						try {
							JSONObject jo = new JSONObject(t.toString());
							if (jo.getInt("result") == 1) {
								List<WeizhangChildItem> historys = new ArrayList<WeizhangChildItem>();
								mIllegalScores = IllegalScore.parseList(jo
										.getString("data"));
								for (int i = 0, size = mViolation.getItem()
										.size(); i < size; i++) {
									Violation violation = mViolation.getItem()
											.get(i);
									int innerSumScore = 0;
									for (int j = 0, len = mIllegalScores.size(); j < len; j++) {
										if (violation.getWfnr().contains(
												mIllegalScores.get(j)
														.getServiceName())) {
											innerSumScore += Integer
													.parseInt(mIllegalScores
															.get(j)
															.getScoreReduce());
											break;
										}
									}
									WeizhangChildItem child = new WeizhangChildItem();
									child.setFen(innerSumScore);
									child.setMoney(Integer.parseInt(violation
											.getWfje()));
									child.setOccur_area(violation.getWfdz());
									child.setOccur_date(violation.getWfsj());
									child.setInfo(violation.getWfnr());
									child.setStatus("N");
									historys.add(child);
									sumScore += innerSumScore;
								}
								if (sumScore > 0) {
									mVehicleIllegals.get(position)
											.setVehicleIllegalScore(
													String.valueOf(sumScore));
								} else {
									mVehicleIllegals.get(position)
											.setVehicleIllegalScore("0");
								}
								mAdapter.notifyDataSetChanged();
								WeizhangGroupItem weiZhangGroupItem = new WeizhangGroupItem();
								if (!TextUtils.isEmpty(mVehicleIllegals.get(
										position).getVehicleIllegalNum())) {
									weiZhangGroupItem.setCount(Integer
											.parseInt(mVehicleIllegals.get(
													position)
													.getVehicleIllegalNum()));
								}
								if (!TextUtils.isEmpty(mVehicleIllegals.get(
										position).getVehicleIllegalMoney())) {
									weiZhangGroupItem.setTotal_money(Double
											.valueOf(mVehicleIllegals.get(
													position)
													.getVehicleIllegalMoney()));
								} else {
									weiZhangGroupItem.setTotal_money(0.0);
								}
								weiZhangGroupItem.setTotal_score(sumScore);
								weiZhangGroupItem.setHistorys(historys);
								if (mVehicles.get(position).getWeiZhangGroups() == null) {
									mVehicles.get(position).setWeiZhangGroups(
											new ArrayList<WeizhangGroupItem>());
									mVehicles.get(position).getWeiZhangGroups()
											.add(weiZhangGroupItem);
								}

							}

						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				});
	}

	/**
	 * 下拉刷新
	 */
	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {

		if (!mIsRefreshing) {
			mIsRefreshing = true;
			getVehicle();
		} else {
			mPullToRefreshListView.onRefreshComplete();
		}

	}

	private int getCityId(String province_short_name, String car_head) {
		for (CarProvinceItem province : mData) {
			if (province.getProvince_short_name().equals(province_short_name)) {
				for (CarCityItem city : province.getCitys()) {
					if (city.getCar_head().equals(car_head)) {
						return city.getCity_id();

					}
				}
				return province.getCitys().get(0).getCity_id();

			}
		}
		return 0;
	}
}
