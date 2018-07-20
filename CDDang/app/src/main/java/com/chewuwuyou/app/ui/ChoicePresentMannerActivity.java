package com.chewuwuyou.app.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.simcpux.Constants;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.alipay.AlipayUtil;
import com.chewuwuyou.app.alipay.Result;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.bean.PayMent;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.utils.WalletUtil;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * @describe:关于车当当
 * @author:yuyong
 * @version 1.1.0
 * @created:2014-12-29下午6:26:04
 */
public class ChoicePresentMannerActivity extends CDDBaseActivity implements
		OnClickListener {

	private ListView mMoneyPresentManner;// 选择支付方式
	private TextView mRechanrageFormalities, mRechanrageAmount, mTextTV,
			mRechanragePayAmount;// 手续费用，充值金额,标题,支付金额
	private ImageButton mSubHeaderLeft;// 左边返回
	private String mMoney;
	private String mFormalities;
	public static ChoicePresentMannerActivity mChoicePresentMannerActivity;
	private String[] paymentStr = { "支付宝快捷支付" };//微信支付
	private Integer[] paymentImgIds = { R.drawable.zhifubao,};// R.drawable.weixin 
	private String[] paymentDesStr = { "推荐有支付宝账号的用户使用"};//, "推荐已安装微信客户端的用户使用" 
	private List<PayMent> mPayMents;// 支付方式的描述
	private PayMent mPayMent;// 支付方式
	private PayMentAdapter mAdapter;
	private Button mBalanceRechargeBtn;// 去支付
	private int mRateType = 1;// 费率类型:支付宝入账费率:1,微信入账费率
	private double mAlipayRate, mWxRate;// 支付宝费率、微信费率
	private boolean mGetRateSuccess = false;// 标识费率是否请求成功
	final IWXAPI msgApi = WXAPIFactory.createWXAPI(
			ChoicePresentMannerActivity.this, null);
	private ProgressDialog mWXPayDialog;
	private String mResultPayMoney;// 调起支付意图返回的支付金额
	private LinearLayout mLinearProcedures;
	private DecimalFormat df = new DecimalFormat("0.00");
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.RQF_PAY:
				if (msg.obj != null) {
					Result result = new Result(msg.obj.toString());
					if ("9000".equals(result.getResultStatus())) {
						// 操作成功
						Intent intent = new Intent(
								ChoicePresentMannerActivity.this,
								ChoiceCompleteActivity.class);
						intent.putExtra("czResult", 1);
						intent.putExtra("payMoney", mResultPayMoney);
						startActivity(intent);
						finishActivity();
					} else if ("6001".equals(result.getResultStatus())) {
						finishActivity();
						ToastUtil.toastShow(ChoicePresentMannerActivity.this,
								"取消充值");
					} else if ("4000".equals(result.getResultStatus())) {
						Intent intent = new Intent(
								ChoicePresentMannerActivity.this,
								ChoiceCompleteActivity.class);
						intent.putExtra("czResult", 0);
						startActivity(intent);

					}
				}
				break;
			default:
				break;
			}

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choice_present_manner);
		mChoicePresentMannerActivity = this;
		initView();
		initData();
		initEvent();

	}

	/**
	 * 初始化控件
	 */
	@Override
	protected void initView() {
		mTextTV = (TextView) findViewById(R.id.sub_header_bar_tv);
		mSubHeaderLeft = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mMoneyPresentManner = (ListView) findViewById(R.id.money_present_manner);
		mRechanrageFormalities = (TextView) findViewById(R.id.rechanrage_formalities_charge);
		mRechanrageAmount = (TextView) findViewById(R.id.rechanrage_top_amount);
		mRechanragePayAmount = (TextView) findViewById(R.id.rechanrage_pay_amount);
		mBalanceRechargeBtn = (Button) findViewById(R.id.btn_balance_recharge);
		mLinearProcedures = (LinearLayout) findViewById(R.id.linear_procedures);

	}

	/**
	 * 逻辑处理
	 */
	@Override
	protected void initData() {

		Intent intent = getIntent();
		mMoney = intent.getStringExtra("money");// 获取充值的金额

		mTextTV.setText("钱包充值");
		getRate();

		mPayMents = new ArrayList<PayMent>();
		for (int i = 0; i < paymentStr.length; i++) {
			mPayMent = new PayMent();
			mPayMent.setPayMent(paymentStr[i]);
			mPayMent.setPayMentImgsId(paymentImgIds[i]);
			mPayMent.setPayMentDes(paymentDesStr[i]);
			if (i == 0) {// 默认选中钱包支付
				mPayMent.setChecked(true);
			}
			// 可考虑钱包支付冻结时不可点击
			mPayMent.setChoose(true);
			mPayMents.add(mPayMent);
		}

		mAdapter = new PayMentAdapter();
		mMoneyPresentManner.setAdapter(mAdapter);

	}

	/**
	 * 事件监听
	 */
	@Override
	protected void initEvent() {
		mSubHeaderLeft.setOnClickListener(this);
		mBalanceRechargeBtn.setOnClickListener(this);
		mMoneyPresentManner.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
					mRateType = 1;// 支付宝充值类型

				} else {
					mRateType = 2;// 微信充值类型
				}
				showRateData();
				for (int i = 0; i < mPayMents.size(); i++) {
					if (position == i) {
						mPayMents.get(position).setChoose(true);
						mPayMents.get(position).setChecked(true);
					} else {
						mPayMents.get(i).setChoose(false);
						mPayMents.get(i).setChecked(false);
					}
				}
				mAdapter.notifyDataSetChanged();
			}
		});
	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:// 返回上一页
			finishActivity();
			break;
		case R.id.btn_balance_recharge:// 提交
			mBalanceRechargeBtn.setClickable(false);
			createCZOrder();
			break;
		default:
			break;
		}
	}

	/**
	 * 创建一个支付宝充值订单
	 */
	private void createCZOrder() {
		double money = Double.parseDouble(mMoney);
		if (money <= 0) {
			ToastUtil.toastShow(ChoicePresentMannerActivity.this, "充值金额不正确");
			mBalanceRechargeBtn.setClickable(true);
			return;
		}
		AjaxParams params = new AjaxParams();
		params.put("amount", mMoney);
		requestNet(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:
					MyLog.i("YUY", "发起支付宝充值订单返回  =  " + msg.obj);
					// {"amount":100,"id":18,"serialNum":"CDD_CZ210804556_5"}
					try {
						JSONObject jo = new JSONObject(msg.obj.toString());
						mResultPayMoney = jo.getString("amount");
						if (mRateType == 1) {// 支付宝充值
							payOrder(jo.getString("serialNum"), "车当当充值订单",
									"车当当支付宝充值订单", mResultPayMoney);
						} else {// 微信充值
							Constant.PAY_INTO = 2;// 标识订单支付
							Constant.PAY_MONEY = mResultPayMoney;
							createPayOrder(mResultPayMoney, "车当当充值订单",
									jo.getString("serialNum"));
						}
						mBalanceRechargeBtn.setClickable(true);
					} catch (JSONException e) {
						e.printStackTrace();
					}

					break;
				case Constant.NET_DATA_FAIL:
					mBalanceRechargeBtn.setClickable(true);
					ToastUtil.toastShow(ChoicePresentMannerActivity.this,
							((DataError) msg.obj).getErrorMessage());
					break;

				default:
					mBalanceRechargeBtn.setClickable(true);
					break;
				}
			}
		}, params, NetworkUtil.ALIPAY_RECHARGE_ORDER, false, 0);
	}

	/**
	 * 获取费率
	 */
	private void getRate() {
		NetworkUtil.postNoHeader(NetworkUtil.GET_RATE, null,
				new AjaxCallBack<String>() {
					@Override
					public void onSuccess(String t) {
						super.onSuccess(t);
						MyLog.i("YUY", "所有费率 = " + t);
						try {
							JSONObject jo = new JSONObject(t);
							// inOut 1、 入账2、出账 rate 费率 type 1、微信 2、支付宝
							mGetRateSuccess = true;
							if (jo.getInt("result") == 1) {
								JSONArray rates = jo.getJSONObject("data")
										.getJSONArray("rates");
								for (int i = 0; i < rates.length(); i++) {
									JSONObject jora = rates.getJSONObject(i);
									if (jora.getInt("inOut") == 1
											&& jora.getInt("type") == 2) {// 入账的微信支付费率
										mAlipayRate = Double.valueOf(jora
												.getString("rate"));
									} else if (jora.getInt("inOut") == 1
											&& jora.getInt("type") == 1) {
										mWxRate = Double.valueOf(jora
												.getString("rate"));
									}
								}
								showRateData();
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						super.onFailure(t, errorNo, strMsg);
						ToastUtil.toastShow(ChoicePresentMannerActivity.this,
								"请求数据失败");
					}
				});

	}

	/**
	 * 通过费率计算金额等
	 */
	private void showRateData() {

		mRechanragePayAmount.setText(df.format(Double.valueOf(mMoney)));
		if (mGetRateSuccess == false) {
			return;
		}
		if (mRateType == 1) {
			if (mAlipayRate != 0.0) {
				// if (WalletUtil.getSxf(Double.valueOf(mMoney) * mAlipayRate) <
				// 2) {
				// mFormalities = "2";
				// } else {
				mFormalities = WalletUtil.getSxf(Double.valueOf(mMoney)
						* mAlipayRate)
						+ "";
				// }
			} else {
				mFormalities = "0";
			}

		} else {
			if (mWxRate != 0.0) {
				// if (WalletUtil.getSxf(Double.valueOf(mMoney) * mWxRate) < 2)
				// {
				// mFormalities = "2";
				// } else {
				mFormalities = WalletUtil.getSxf(Double.valueOf(mMoney)
						* mWxRate)
						+ "";
				// }
			} else {
				mFormalities = "0";
			}
			mFormalities = WalletUtil.getSxf(Double.valueOf(mMoney) * mWxRate)
					+ "";
		}

		if (Double.parseDouble(mFormalities) <= 0) {
			mLinearProcedures.setVisibility(View.GONE);
		} else {
			mRechanrageFormalities.setText(df.format(mFormalities));// 动态算出手续费
			mRechanrageAmount
					.setText(df.format((Double.parseDouble(mMoney) - Double
							.parseDouble(mFormalities)) + ""));
		}
	}

	/**
	 * 支付宝充值
	 */
	private void payOrder(String orderNum, String orderType,
			String orderDescription, String payMoney) {
		MyLog.i("YUY", "支付信息  orderNum = " + orderNum + "==orderType = "
				+ orderType + "==orderdeDescription = " + orderDescription
				+ "== payMoney = " + payMoney);
		final String orderInfo = AlipayUtil.getOrderInfo(orderNum, orderType,
				orderDescription, payMoney);
		new Thread() {
			@Override
			public void run() {
				PayTask alipay = new PayTask(ChoicePresentMannerActivity.this);
				// 设置为沙箱模式，不设置默认为线上环境
				// alipay.setSandBox(true);
				String result = alipay.pay(orderInfo, true);
				MyLog.i("YUY", "支付结果" + result);
				Message msg = new Message();
				msg.what = Constant.RQF_PAY;
				msg.obj = result;
				mHandler.sendMessage(msg);
				mBalanceRechargeBtn.setClickable(true);
			}
		}.start();
	}

	/**
	 * 微信发起充值订单
	 * 
	 * @param payMoney
	 * @param orderType
	 * @param orderNum
	 */
	private void createPayOrder(String payMoney, String orderType,
			String orderNum) {
		int WxpayMoney = ((int) Double.parseDouble(mMoney))*100;
		AjaxParams params = new AjaxParams();
		params.put("body", orderType);
		params.put("out_trade_no", orderNum);
		params.put("spbill_create_ip", "127.0.0.1");
		params.put("total_fee", String.valueOf(WxpayMoney));// 微信支付的是分所以要乘100
		params.put("trade_type", "APP");
		NetworkUtil.postMulti(NetworkUtil.WXPAY_NOTIFY_URL, params,
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
						mBalanceRechargeBtn.setClickable(true);
					}

					@Override
					public void onStart() {
						super.onStart();
						mWXPayDialog = ProgressDialog.show(
								ChoicePresentMannerActivity.this, null,
								getString(R.string.getting_prepayid));
					}

				});
	}

	private void sendPayReq(PayReq req) {
		mBalanceRechargeBtn.setClickable(true);
		msgApi.registerApp(Constants.APP_ID);
		msgApi.sendReq(req);
		if (mWXPayDialog != null) {
			mWXPayDialog.dismiss();
		}
	}

	class PayMentAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mPayMents.size();
		}

		@Override
		public Object getItem(int position) {
			return mPayMents.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ListItemView listItemView = null;
			if (convertView == null) {
				listItemView = new ListItemView();
				convertView = getLayoutInflater().inflate(
						R.layout.payment_item, null);
				listItemView.paymentIV = (ImageView) convertView
						.findViewById(R.id.payment_iv);
				listItemView.paymentTV = (TextView) convertView
						.findViewById(R.id.payment_tv);
				listItemView.paymentDesTV = (TextView) convertView
						.findViewById(R.id.payment_des_tv);
				listItemView.paymentRB = (RadioButton) convertView
						.findViewById(R.id.payment_rb);

				convertView.setTag(listItemView);
			} else {
				listItemView = (ListItemView) convertView.getTag();
			}
			if (position == 2) {
				listItemView.paymentDesTV.setTextColor(getResources().getColor(
						R.color.red));
			} else {
				listItemView.paymentDesTV.setTextColor(getResources().getColor(
						R.color.common_text_color));
			}

			if (mPayMents.get(position).getIsChoose() == false) {
				listItemView.paymentRB.setVisibility(View.GONE);
			} else {
				listItemView.paymentRB.setVisibility(View.VISIBLE);
			}
			listItemView.paymentIV.setImageResource(mPayMents.get(position)
					.getPayMentImgsId());
			listItemView.paymentTV
					.setText(mPayMents.get(position).getPayMent());
			listItemView.paymentDesTV.setText(mPayMents.get(position)
					.getPayMentDes());
			listItemView.paymentRB.setChecked(mPayMents.get(position)
					.getIsChecked());

			return convertView;
		}

		class ListItemView {
			ImageView paymentIV;// 支付图标
			TextView paymentTV;// 支付名称
			TextView paymentDesTV;// 支付描述
			RadioButton paymentRB;// 是否选中
		}

	}
}
