package com.chewuwuyou.app.ui;

import net.tsz.afinal.http.AjaxParams;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.alipay.Result;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.tools.EditInputFilter;
import com.chewuwuyou.app.utils.AlertDialog;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ServiceUtils;
import com.chewuwuyou.app.utils.ToastUtil;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:商家对商家发新的任务
 * @author:yuyong
 * @date:2015-6-8上午10:56:34
 * @version:1.2.1
 */
public class BusinessNewOrderActivity extends CDDBaseActivity implements
		OnClickListener {

	private TextView mTitleTV;
	private ImageButton mBackIBtn;
	private TextView mOrderServiceLocationTV;// 办理地区
	private int mServiceType = 0;// 订单类型
	private EditText mPayMoneyET;// 填写支付金额
	private Button mPayBtn;// 支付
	private int payment = 1;// 区分交易方式 默认为平台担保支付 1 选择商家支付为2
	private double payMoney = 0.0;// 支付金额
	private String mOrderType = "违章服务";
	private String orderDescription;
	private String taskId;
	private TextView mOrderContentTV, mOrderDescriptionType;// 订单类型、订单项目
	private LinearLayout businessServiceType;
	private RelativeLayout mTitleHeight;// 标题布局高度

	private TextView mOrderClauseTV_Two;
	private TextView mOrderClauseTV;

	private LinearLayout mLinearXieyi;

	private TextView mReleaseOption;//是否需要票据
	private RadioButton mReleaseNeed,mReleaseNoNeed;//需要票据，不需要票据

	private boolean isSelected = false;
	private boolean isUnchecked = false;


	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {

			case Constant.RQF_PAY:
				mPayBtn.setClickable(true);
				if (msg.obj != null) {
					Result result = new Result(msg.obj.toString());
					if ("9000".equals(result.getResultStatus())) {
						mPayBtn.setClickable(true);
						// 操作成功
						MyLog.i("YUY", "支付成功，修改订单状态的参数  taskId = " + taskId
								+ " orderType = " + mOrderType);
						AlertDialog dialog = new AlertDialog(
								BusinessNewOrderActivity.this).builder();
						dialog.setTitle("支付详情");
						dialog.setMsg("支付成功！请关注订单状态。");
						dialog.show();
						addOrder(taskId, mOrderType);
						finishActivity();
					} else if ("6001".equals(result.getResultStatus())) {
						mPayBtn.setClickable(true);
						finishActivity();
						ToastUtil.toastShow(BusinessNewOrderActivity.this,
								"支付异常,在订单列表里可以看到订单");

						// 操作已经取消
					} else if ("4000".equals(result.getResultStatus())) {
						mPayBtn.setClickable(true);
						// addOrder(mTask.getId());
						AlertDialog dialog = new AlertDialog(
								BusinessNewOrderActivity.this).builder();
						dialog.setTitle("支付详情");
						dialog.setMsg("支付失败！请在订单详情里重新完成支付。");
						dialog.show();
					}
				}

				break;
			case Constant.NET_DATA_SUCCESS:
				mPayBtn.setClickable(true);
				AlertDialog dialog = new AlertDialog(
						BusinessNewOrderActivity.this).builder();
				dialog.setMsg("发布订单成功！");
				dialog.setNegativeButton("继续发布", new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						finishActivity();
					}
				});
				if (CacheTools.getUserData("role").contains("2")
						|| CacheTools.getUserData("role").contains("3")) {// 区分是否使用户发布
					dialog.setPositiveButton("进入秘书服务", new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							mPayBtn.setClickable(true);
							Intent intent = new Intent(
									BusinessNewOrderActivity.this,
									MyWorkActivity.class);
							startActivity(intent);
							finishActivity();
						}
					});
				} else {
					mPayBtn.setClickable(true);
					dialog.setPositiveButton("进入订单管理", new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Intent intent = new Intent(
									BusinessNewOrderActivity.this,
									TaskManagerActivity.class);
							startActivity(intent);
							finishActivity();
						}
					});

				}
				dialog.setCancelable(false);
				dialog.show();

				break;
			case Constant.NET_DATA_FAIL:
				mPayBtn.setClickable(true);
				ToastUtil.toastShow(BusinessNewOrderActivity.this,
						((DataError) msg.obj).getErrorMessage());
				break;

			default:
				mPayBtn.setClickable(true);
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_order_ac);
		initView();
		initData();
		initEvent();
	}

	@Override
	protected void initView() {
		mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
		mOrderClauseTV = (TextView) findViewById(R.id.order_clause);// 服务条款
		mOrderClauseTV_Two = (TextView) findViewById(R.id.order_clause_two);
		mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mOrderServiceLocationTV = (TextView) findViewById(R.id.order_description_et);// 办理地区
		mPayBtn = (Button) findViewById(R.id.pay_order_btn);
		mPayMoneyET = (EditText) findViewById(R.id.pay_money_et);
		mOrderContentTV = (TextView) findViewById(R.id.order_content_tv);
		mOrderDescriptionType = (TextView) findViewById(R.id.order_description_type);
		businessServiceType = (LinearLayout) findViewById(R.id.business_service_type);
		mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
		mLinearXieyi = (LinearLayout) findViewById(R.id.linear_sanfang_xieyi);
		mReleaseNeed = (RadioButton) findViewById(R.id.release_need);
		mReleaseNoNeed = (RadioButton) findViewById(R.id.release_no_need);
		mReleaseOption = (TextView) findViewById(R.id.release_option);
	}

	@Override
	protected void initData() {
		mTitleTV.setText(R.string.new_order_title);
		Intent intent = getIntent();
		mServiceType = intent.getIntExtra("serviceType", 0);
		mOrderServiceLocationTV.setText(intent.getStringExtra("region"));// 显示办理地区
		isTitle(mTitleHeight);// 根据不同手机判断

		/* private String proId,projectNum;//接收传递过来的Id和服务类型 */
		if (mServiceType == 1) {
			mOrderType = "违章服务";
		} else if (mServiceType == 2) {
			businessServiceType.setVisibility(View.VISIBLE);
			mOrderType = "车辆服务";
			mOrderDescriptionType.setText(ServiceUtils.getProjectName(intent
					.getStringExtra("projectNum")));
		} else if (mServiceType == 3) {
			businessServiceType.setVisibility(View.VISIBLE);
			mOrderType = "驾证服务";
			mOrderDescriptionType.setText(ServiceUtils.getProjectName(intent
					.getStringExtra("projectNum")));
		}
		if (mOrderType.equals("违章服务")) {
			mLinearXieyi.setVisibility(View.VISIBLE);
		}

		mOrderContentTV.setText(mOrderType);
	}

	@Override
	protected void initEvent() {
		mOrderClauseTV_Two.setOnClickListener(this);
		mBackIBtn.setOnClickListener(this);
		mPayBtn.setOnClickListener(this);
		mOrderClauseTV.setOnClickListener(this);
		mReleaseNeed.setOnClickListener(this);
		mReleaseNoNeed.setOnClickListener(this);
		/* 设置输入的服务费最大值为99999.99,最小值为0.00 */
		InputFilter[] filters = { new EditInputFilter(5000),
				new InputFilter.LengthFilter(7) /* 这里限制输入的长度为7个字母 */};
		mPayMoneyET.setFilters(filters);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			finishActivity();
			break;
		case R.id.release_need:
			isSelected = true;
			isUnchecked = false;
			mReleaseOption.setVisibility(View.GONE);
				break;
		case R.id.release_no_need:
			isUnchecked = true;
			isSelected = false;
			mReleaseOption.setVisibility(View.GONE);
				break;
		case R.id.order_clause:
			intent = new Intent(this, AgreementActivity.class);
			intent.putExtra("type", 2);
			startActivity(intent);
			break;

		case R.id.order_clause_two:
			intent = new Intent(this, AgreementActivity.class);
			intent.putExtra("type", 3);
			startActivity(intent);
			break;
		case R.id.pay_order_btn:
			orderDescription = mOrderServiceLocationTV.getText().toString();
			String inputPayMoney = mPayMoneyET.getText().toString().trim();
			String businessId = getIntent().getStringExtra("businessId");
			if (TextUtils.isEmpty(inputPayMoney)) {
				ToastUtil.toastShow(BusinessNewOrderActivity.this, "请输入订单金额");
			} else if (Double.valueOf(inputPayMoney) < 10) {
				ToastUtil
						.toastShow(BusinessNewOrderActivity.this, "订单金额不低于10元");
			}else if(isSelected!=true&&isUnchecked!=true){
				mReleaseOption.setVisibility(View.VISIBLE);
				return;
			}else {
				payMoney = Double.valueOf(inputPayMoney);
				AjaxParams params = new AjaxParams();

				if (getIntent().getIntExtra("isBusOrKefu", 0) == Constant.CHAT_USER_ROLE.BUSINESS) {// 从即时通讯给商家下单
				} else if (getIntent().getIntExtra("isBusOrKefu", 0) == Constant.CHAT_USER_ROLE.SERVER) {// 从即时通讯给客服下单
					params.put(
							"handlerId",
							String.valueOf(getIntent().getExtras().getInt(
									"handlerId")));
				}
				params.put("userDescription", orderDescription);
				params.put("userBId", businessId);
				// TODO 欠缺对客服下单的判断
				if (!TextUtils.isEmpty(mOrderServiceLocationTV.getText()
						.toString().trim())) {
					params.put("taskLocation", mOrderServiceLocationTV
							.getText().toString().trim());
				} else {
					params.put(
							"taskLocation",
							CacheTools.getUserData("province")
									+ CacheTools.getUserData("city")
									+ CacheTools.getUserData("district"));
				}
				MyLog.i("YUY",
						"发布的服务项目  = "
								+ getIntent().getStringExtra("projectNum"));
				params.put("spSta", String.valueOf(payment));
				params.put("paymentAmount", String.valueOf(payMoney));
				params.put("projectName",
						getIntent().getStringExtra("projectNum"));

				if(isSelected==true){
					params.put("provideBill", "1");
				}else{
					params.put("provideBill", "0");
				}
				if (mServiceType == Constant.SERVICE_TYPE.CAR_SERVICE) {
					requestNet(mHandler, params,
							NetworkUtil.VEHICLE_SERVICE_URL, false, 0);
				} else if (mServiceType == Constant.SERVICE_TYPE.ILLEGAL_SERVICE) {
					requestNet(mHandler, params,
							NetworkUtil.VIOLATION_SERVICE_URL, false, 0);
				} else if (mServiceType == Constant.SERVICE_TYPE.LICENCE_SERVICE) {
					requestNet(mHandler, params,
							NetworkUtil.LICENCE_SERVICE_URL, false, 0);
				}
				mPayBtn.setClickable(false);
			}

			break;
		default:
			break;
		}
	}

	/**
	 * 支付完成后更改订单状态
	 * 
	 * @param taskId
	 */

	public void addOrder(String taskId, String serviceType) {
		AjaxParams params = new AjaxParams();
		params.put("taskId", taskId);
		params.put("amount", "0");
		params.put("type", serviceType);
		requestNet(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:
					finishActivity();
					break;
				default:
				case Constant.NET_DATA_FAIL:
					ToastUtil.toastShow(BusinessNewOrderActivity.this,
							"修改订单状态失败!");
					break;
				}
			}
		}, params, NetworkUtil.UPDATE_ORDER_STATUS, false, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			mPayBtn.setClickable(true);
			finishActivity();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
