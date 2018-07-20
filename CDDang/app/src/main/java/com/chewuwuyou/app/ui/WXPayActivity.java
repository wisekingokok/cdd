package com.chewuwuyou.app.ui;

import net.sourceforge.simcpux.Constants;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.utils.WalletUtil;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

@SuppressLint("HandlerLeak")
public class WXPayActivity extends CDDBaseActivity implements OnClickListener {

	private TextView mTitleTV;
	private ImageButton mBackIBtn;
	private TextView mPayMoneyTV;// 支付金额
	private TextView mOrderMoneyTV;// 订单金额
	private TextView mShouXuFeiMoneyTV;// 手续费用

	private String mOrderNum, mOrderMoney, mOrderType, mTaskId;// 订单号、订单金额、订单类型
	private double mShouxufei, mPayMoney;// 手续费、支付金额
	private Button mPayBtn;

	final IWXAPI msgApi = WXAPIFactory.createWXAPI(WXPayActivity.this, null);
	// private ProgressDialog mWXPayDialog;
	private boolean isGetRateSuccess = false;
	public static WXPayActivity mActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		msgApi.registerApp(Constants.APP_ID);
		setContentView(R.layout.wx_pay_ac);
		mActivity = this;
		initView();
		initData();
		initEvent();

	}

	@Override
	protected void initView() {
		mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
		mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mPayMoneyTV = (TextView) findViewById(R.id.pay_money_tv);
		mOrderMoneyTV = (TextView) findViewById(R.id.order_money_tv);
		mShouXuFeiMoneyTV = (TextView) findViewById(R.id.shouxu_money_tv);
		mPayBtn = (Button) findViewById(R.id.pay_btn);
	}

	@Override
	protected void initData() {
		mTitleTV.setText("微信支付");
		mOrderNum = getIntent().getStringExtra("orderNumber");
		mOrderType = getIntent().getStringExtra("orderType");
		mOrderMoney = getIntent().getStringExtra("orderMoney");
		mTaskId = getIntent().getStringExtra("taskId");
		getRate();// 请求出手续费后再显示

	}

	@Override
	protected void initEvent() {
		mBackIBtn.setOnClickListener(this);
		mPayBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			finishActivity();
			break;
		case R.id.pay_btn:// 发起微信支付
			MyLog.i("YUY", "微信支付参数orderMoney = " + mOrderMoney + " orderNum = "
					+ mOrderNum + " orderType = " + mOrderType + " payMoney = "
					+ mPayMoney);
			if (isGetRateSuccess == true) {
				getPayPurpose();
			} else {
				ToastUtil.toastShow(WXPayActivity.this, "支付信息不正确");
			}

			break;
		default:
			break;
		}
	}

	/**
	 * 获取费率
	 */
	private void getRate() {

		requestNet(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:

					try {
						JSONObject jo = new JSONObject(msg.obj.toString());
						// inOut 1、 入账2、出账 rate 费率 type 1、微信 2、支付宝
						MyLog.i("YUY", "所有费率 = " + msg.obj.toString());

						JSONArray rates = jo.getJSONArray("rates");

						for (int i = 0; i < rates.length(); i++) {
							JSONObject jora = rates.getJSONObject(i);

							if (jora.getInt("inOut") == 1
									&& jora.getInt("type") == 1) {// 入账的微信支付费率
								double mPayRate = Double.valueOf(jora
										.getString("rate"));// 获取到的费率
								mShouxufei = WalletUtil.getSxf(Double
										.valueOf(mOrderMoney) * mPayRate);// 动态算出手续费
								mPayMoney = Double.valueOf(mOrderMoney)
										+ mShouxufei;
								mOrderMoneyTV.setText(mOrderMoney);
								mShouXuFeiMoneyTV.setText(String
										.valueOf(mShouxufei));
								mPayMoneyTV.setText(String.valueOf(mPayMoney));
								isGetRateSuccess = true;
							}

						}

					} catch (JSONException e) {
						e.printStackTrace();
					}

					break;

				case Constant.NET_DATA_FAIL:
					ToastUtil.toastShow(WXPayActivity.this,
							((DataError) msg.obj).getErrorMessage());
					break;
				default:
					break;
				}
			}
		}, null, NetworkUtil.GET_RATE, false, 0);

	}

	/**
	 * 调起支付意图,成功后再调起支付 motify start by yuyong 新增支付前发起支付意图
	 */
	private void getPayPurpose() {
		AjaxParams params = new AjaxParams();
		params.put("taskId", mTaskId);
		params.put("payPurpose", Constant.PAY_PURPOSE_TYPE.ORDER_PAY);
		requestNet(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:
					Constant.PAY_INTO = 1;// 标识订单支付
					createPayOrder();
					break;
				case Constant.NET_DATA_FAIL:
					ToastUtil.toastShow(WXPayActivity.this,
							((DataError) msg.obj).getErrorMessage());
					break;
				default:
					break;
				}

			}
		}, params, NetworkUtil.WX_PAY_PURPOSE, false, 0);

	}

	@SuppressWarnings("static-access")
	private void createPayOrder() {
		int WxpayMoney = (int) (mPayMoney * 100);
		AjaxParams params = new AjaxParams();
		params.put("body", mOrderType);
		params.put("out_trade_no", mOrderNum);
		params.put("spbill_create_ip", "127.0.0.1");
		params.put("total_fee", String.valueOf(WxpayMoney));// 微信支付的是分所以要乘100
		params.put("trade_type", "APP");
		new NetworkUtil().postMulti(NetworkUtil.WXPAY_NOTIFY_URL, params,
				new AjaxCallBack<String>() {
					@Override
					public void onSuccess(String t) {
						super.onSuccess(t);
						// 从t里面获取"prepay_id"
						try {
							JSONObject jo = new JSONObject(t);
							JSONObject data = jo.getJSONObject("data");
							MyLog.i("YUY",
									"调用微信支付返回数据 = "
											+ jo.getJSONObject("data")
													.toString());
							PayReq req = new PayReq();
							// "sign": "46FA2F2B6C2B282BEA9F23D0489FB2CD",
							// "appid": "wx2d83346d15e1f3e2",
							// "noncestr": "2d969e2cee8cfa07ce7ca0bb13c7a36d",
							// "package": "Sign=WXPay",
							// "partnerid": "1267830001",
							// "timestamp": "1464795204"
							req.appId = data.getString("appid");
							req.nonceStr = data.getString("noncestr");
							req.packageValue = data.getString("package");
							req.partnerId = data.getString("partnerid");
							req.prepayId = data.getString("prepayid");
							req.timeStamp = data.getString("timestamp");
							req.sign = data.getString("sign");

							MyLog.e("YUY",
									"微信支付参数 = " + data.getString("appid") + " "
											+ data.getString("noncestr") + " "
											+ data.getString("package") + " "
											+ data.getString("partnerid") + " "
											+ data.getString("prepayid") + " "
											+ data.getString("timestamp") + " "
											+ data.getString("sign"));
							sendPayReq(req);

						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						super.onFailure(t, errorNo, strMsg);
					}

					@Override
					public void onStart() {
						super.onStart();
						ProgressDialog.show(WXPayActivity.this, null,
								getString(R.string.getting_prepayid));
					}

				});
	}

	private void sendPayReq(PayReq req) {
		msgApi.registerApp(Constants.APP_ID);
		msgApi.sendReq(req);
		// if (mWXPayDialog != null) {
		// mWXPayDialog.dismiss();
		// finishActivity();
		// }
	}
}
