package com.chewuwuyou.app.ui;

import java.text.DecimalFormat;
import java.util.List;

import net.tsz.afinal.http.AjaxParams;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.bean.ServicePriceData;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:车辆服务新的提交页面
 * @author:yuyong
 * @date:2015-6-24下午5:36:39
 * @version:1.2.1
 */
public class VehicleServiceSubmitNewActivity extends CDDBaseActivity implements
		OnClickListener {

	private TextView mTitleTV;
	private ImageButton mBackIBtn;
	private TextView mServiceProjectTV;
	private TextView mProjectPriceTV;
	private TextView mVisitingServicePriceTV;
	private RelativeLayout mVisitingServiceTimeRL;
	private TextView mSumPriceTV;
	private Button mComfrimPayBtn;
	private TextView mTKTV, mTKTV_Two;// 查看服务条款
	private double mProjectPrice = 0.0;// 项目规费
	private int iSVisitingService = 0;// 是否需要上门服务
	private double mVisitingServicePrice = 0.0;// 上门服务费
	private double mAgencyPrice = 0.0;// 代理服务费
	private double mSumPrice = 0.0;// 总服务费
	private List<ServicePriceData> mServicePriceDatas;// 通过服务选项请求到的服务费及上门服务费
	private int mServiceType;// 区分服务类型 2为车辆服务 3为驾证服务
	private String mRequestUrl;// 定义请求的服务地址
	private RelativeLayout mTitleHeight;// 标题布局高度
	private TextView mVehcleRegion, mVehicleServiceCharge;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vehicle_service_submit_new_ac);
		initView();
		initData();
		initEvent();
	}

	@Override
	protected void initView() {
		mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
		mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mVehcleRegion = (TextView) findViewById(R.id.vehcle_region);
		mServiceProjectTV = (TextView) findViewById(R.id.service_project_name_tv);
		mProjectPriceTV = (TextView) findViewById(R.id.service_project_price_tv);
		mVisitingServicePriceTV = (TextView) findViewById(R.id.visiting_service_price_tv);
		mVisitingServiceTimeRL = (RelativeLayout) findViewById(R.id.visiting_service_time_rl);
		mSumPriceTV = (TextView) findViewById(R.id.sum_price_tv);
		mComfrimPayBtn = (Button) findViewById(R.id.comfrim_pay_btn);
		mVehicleServiceCharge = (TextView) findViewById(R.id.vehicle_service_charge);
		mTKTV = (TextView) findViewById(R.id.cdd_fwtk_tv);
		mTKTV_Two = (TextView) findViewById(R.id.cdd_fwtk_two);
	}

	@Override
	protected void initData() {

		mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);// 根据不同手机判断
		mServiceType = getIntent().getExtras().getInt("serviceType");
		mVehcleRegion.setText(getIntent().getStringExtra("region"));// 显示办理地区
		mTitleTV.setText("新建订单");
		mServiceProjectTV.setText(getIntent().getStringExtra("serviceProject"));
		if (mServiceType == Constant.SERVICE_TYPE.CAR_SERVICE) {
			mRequestUrl = NetworkUtil.VEHICLE_SERVICE_URL;
		} else {
			mRequestUrl = NetworkUtil.LICENCE_SERVICE_URL;
		}
		AjaxParams params = new AjaxParams();
		params.put("toid", getIntent().getStringExtra("businessId"));
		params.put("type", String.valueOf(mServiceType));
		params.put("ids", getIntent().getStringExtra("projectNum"));

		requestNet(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:

					MyLog.i("YUY", "请求的服务价格---" + msg.obj.toString());
					mServicePriceDatas = ServicePriceData.parserList(msg.obj
							.toString());
					for (int i = 0; i < mServicePriceDatas.size(); i++) {
						if (mServiceType == Constant.SERVICE_TYPE.CAR_SERVICE
								&& mServicePriceDatas.get(i).getProjectNum()
										.equals("212")) {
							// mGetVisitingServicePrice =
							// Double.parseDouble(mServicePriceDatas
							// .get(i).getServicePrice());
						} else if (mServiceType == Constant.SERVICE_TYPE.LICENCE_SERVICE
								&& mServicePriceDatas.get(i).getProjectNum()
										.equals("309")) {
							// mGetVisitingServicePrice =
							// Double.parseDouble(mServicePriceDatas
							// .get(i).getServicePrice());
						} else {
							mProjectPrice = Double.parseDouble(mServicePriceDatas
									.get(i).getServiceFee());
							mAgencyPrice = Double.parseDouble(mServicePriceDatas
									.get(i).getServicePrice());
						}
					}
					computeServicePrice();
					break;

				default:
					break;
				}
			}
		}, params, NetworkUtil.GET_SERVICEPRICE_BY_SERVICE, false, 0);
	}

	@Override
	protected void initEvent() {
		mTKTV_Two.setOnClickListener(this);
		mBackIBtn.setOnClickListener(this);
		mComfrimPayBtn.setOnClickListener(this);
		// mChooseVehicleLL.setOnClickListener(this);
		mVisitingServiceTimeRL.setOnClickListener(this);
		mTKTV.setOnClickListener(this);
		// 监听是否上门服务
		// mIsVisitingServiceCB
		// .setOnCheckedChangeListener(new OnCheckedChangeListener() {
		//
		// @Override
		// public void onCheckedChanged(CompoundButton arg0,
		// boolean arg1) {
		// if (arg1 == false) {
		// iSVisitingService = 0;
		// mVisitingServiceInfoLL.setVisibility(View.GONE);
		// mVisitingServicePriceRL.setVisibility(View.GONE);
		// mVisitingServicePrice = 0.0;
		// computeServicePrice();
		// mVisitingServicePriceTV.setTextColor(getResources()
		// .getColor(R.color.common_text_color));
		//
		// } else {
		// iSVisitingService = 1;
		// mVisitingServiceInfoLL.setVisibility(View.VISIBLE);
		// mVisitingServicePriceRL.setVisibility(View.VISIBLE);
		// mVisitingServicePrice = mGetVisitingServicePrice;// 赋值上门服务费
		// computeServicePrice();
		// mVisitingServicePriceTV
		// .setTextColor(getResources()
		// .getColor(
		// R.color.illegal_service_price_color));
		//
		// }
		// }
		// });
	}

	/**
	 * 
	 * 计算最终服务费
	 */
	protected void computeServicePrice() {

		mSumPrice = mProjectPrice + mVisitingServicePrice + mAgencyPrice;
		MyLog.i("YUY", "总计费用---" + mSumPrice);

		DecimalFormat df = new DecimalFormat("######0.00");
		mProjectPriceTV.setText("￥" + df.format(mProjectPrice) + "元");// 支付金额
		mVisitingServicePriceTV.setText("￥" + mVisitingServicePrice + "元");
		mSumPriceTV.setText("￥" + df.format(mSumPrice) + "元");
		mVehicleServiceCharge.setText("¥" + df.format(mAgencyPrice) + "元");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			finishActivity();
			break;
		// case R.id.visiting_service_time_rl:
		// DialogUtil.showTimeDialog(VehicleServiceSubmitNewActivity.this,
		// mVisitingServiceTimeTV);
		// break;
		case R.id.cdd_fwtk_tv:
			Intent yssmIntent = new Intent(
					VehicleServiceSubmitNewActivity.this,
					AgreementActivity.class);
			yssmIntent.putExtra("type", 2);
			startActivity(yssmIntent);
			break;

		case R.id.cdd_fwtk_two:
			Intent xieIntent = new Intent(VehicleServiceSubmitNewActivity.this,
					AgreementActivity.class);
			xieIntent.putExtra("type", 3);
			startActivity(xieIntent);
			break;
		// case R.id.choose_vehicle_ll:
		// Intent intent = new Intent(VehicleServiceSubmitNewActivity.this,
		// ChooseVehicleActivity.class);
		// intent.putExtra("tag", 2);
		// startActivityForResult(intent, 20);
		// break;
		case R.id.comfrim_pay_btn:
			AjaxParams params = new AjaxParams();
			// if (iSVisitingService == 1) {// 判断是否需要上门服务
			// String visitingServiceTime = mVisitingServiceTimeTV.getText()
			// .toString();
			// String visitingServiceAddress = mVisitingServiceAddressET
			// .getText().toString();
			// if (TextUtils.isEmpty(visitingServiceTime)) {
			// ToastUtil.showToast(VehicleServiceSubmitNewActivity.this,
			// R.string.please_choose_yysj);
			// } else if (DateTimeUtil.compareTime1(visitingServiceTime,
			// VehicleServiceSubmitNewActivity.this) == false) {
			// ToastUtil.showToast(VehicleServiceSubmitNewActivity.this,
			// R.string.yysj_erro);
			// } else if (TextUtils.isEmpty(visitingServiceAddress)) {
			// ToastUtil.showToast(VehicleServiceSubmitNewActivity.this,
			// R.string.please_input_yydz);
			// } else {
			// params.put("appointmenttime", mVisitingServiceTimeTV
			// .getText().toString());
			// params.put("appointmentadd", mVisitingServiceAddressET
			// .getText().toString());
			// if (mVehicle != null) {
			// params.put("plateNumber", mVehicle.getPlateNumber()
			// + "");
			// }
			// params.put("projectName",
			// getIntent().getStringExtra("projectNum"));
			// params.put("paymentAmount", String.valueOf(mSumPrice));
			// params.put("userBId", getIntent().getStringExtra("businessId"));
			// params.put("isdoortodoorservice",
			// String.valueOf(iSVisitingService));
			// // params.put("spSta", "1");
			//
			// System.out.println("++++++++++aas+" + mRequestUrl);
			// requestNet(new Handler() {
			//
			// @Override
			// public void handleMessage(Message msg) {
			// super.handleMessage(msg);
			// switch (msg.what) {
			// case Constant.NET_DATA_SUCCESS:
			// AlertDialog.Builder dialog = new AlertDialog.Builder(
			// VehicleServiceSubmitNewActivity.this);
			// dialog.setTitle("温馨提示");
			// dialog.setMessage("是否前往订单管理页面");
			// dialog.setPositiveButton("前往",
			// new DialogInterface.OnClickListener() {
			//
			// @Override
			// public void onClick(
			// DialogInterface arg0, int arg1) {
			// // stub
			// Intent intent = new Intent(
			// VehicleServiceSubmitNewActivity.this,
			// TaskManagerActivity.class);
			// startActivity(intent);
			// finishActivity();
			// }
			// });
			// dialog.setNegativeButton("取消",
			// new DialogInterface.OnClickListener() {
			//
			// @Override
			// public void onClick(
			// DialogInterface arg0, int arg1) {
			// // stub
			// finishActivity();
			// }
			// });
			// dialog.show();
			// break;
			// case Constant.NET_DATA_FAIL:
			// ToastUtil.toastShow(
			// VehicleServiceSubmitNewActivity.this,
			// ((DataError) msg.obj).getErrorMessage());
			// break;
			//
			// default:
			// break;
			// }
			// }
			//
			// }, params, mRequestUrl, false, 0);
			//
			// } else {
			if(getIntent().getIntExtra("isBusOrKefu", 0)==Constant.CHAT_USER_ROLE.BUSINESS){//从即时通讯给商家下单
			}else if(getIntent().getIntExtra("isBusOrKefu", 0)==Constant.CHAT_USER_ROLE.SERVER){//从即时通讯给客服下单
				params.put("handlerId", String.valueOf(getIntent().getExtras().getInt("handlerId")));
			}
			params.put("projectName", getIntent().getStringExtra("projectNum"));
			params.put("paymentAmount", String.valueOf(mSumPrice));
			params.put("userBId", getIntent().getStringExtra("businessId"));
			params.put("isdoortodoorservice", String.valueOf(iSVisitingService));
			// params.put("spSta", "1");
			params.put("taskLocation", mVehcleRegion.getText().toString());
			requestNet(new Handler() {

				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					switch (msg.what) {
					case Constant.NET_DATA_SUCCESS:
						AlertDialog.Builder dialog = new AlertDialog.Builder(
								VehicleServiceSubmitNewActivity.this);
						dialog.setTitle("温馨提示");
						dialog.setMessage("是否前往订单管理页面");
						dialog.setPositiveButton("前往",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										Intent intent = new Intent(
												VehicleServiceSubmitNewActivity.this,
												TaskManagerActivity.class);
										startActivity(intent);
										finishActivity();
									}
								});
						dialog.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										finishActivity();
									}
								});
						dialog.show();
						break;
					case Constant.NET_DATA_FAIL:
						ToastUtil.toastShow(
								VehicleServiceSubmitNewActivity.this,
								((DataError) msg.obj).getErrorMessage());
						break;

					default:
						break;
					}
				}

			}, params, mRequestUrl, false, 0);

			// }

			break;

		default:
			break;
		}
	}

}
