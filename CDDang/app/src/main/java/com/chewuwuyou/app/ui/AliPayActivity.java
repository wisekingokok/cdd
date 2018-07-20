package com.chewuwuyou.app.ui;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.alipay.AlipayUtil;
import com.chewuwuyou.app.alipay.Result;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.utils.WalletUtil;

/**
 * 支付宝支付
 *
 * @author yuyong
 */
public class AliPayActivity extends CDDBaseActivity implements OnClickListener {

    private TextView mTitleTV;
    private ImageButton mBackIBtn;
    private TextView mPayMoneyTV;// 支付金额
    private TextView mOrderMoneyTV;// 订单金额
    private TextView mShouXuFeiMoneyTV;// 手续费用

    private String mOrderNum, mOrderMoney, mOrderType, mOrderDesc, mTaskId;// 订单号、订单金额、订单类型
    private RelativeLayout mRelativeLayout_shouxu;
    private double mShouxufei, mPayMoney;// 手续费、支付金额
    private Button mPayBtn;
    private double mRate = 0.0;// 微信提现的手续费
    private boolean isGetRateSuccess = false;
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
                            AlertDialog.Builder dialog = new AlertDialog.Builder(
                                    AliPayActivity.this);
                            dialog.setTitle("支付详情");
                            dialog.setMessage("支付成功！请关注订单状态。");
                            dialog.show();
                            // addOrder(mTaskId, mOrderType);
                            finishActivity();
                            CommonPayActivity.mInstance.finish();
                        } else if ("6001".equals(result.getResultStatus())) {
                            finishActivity();
                            ToastUtil.toastShow(AliPayActivity.this,
                                    "取消支付,在订单管理里可以看到订单");

                            // 操作已经取消
                        } else if ("4000".equals(result.getResultStatus())) {
                            // addOrder(mTask.getId());
                            AlertDialog.Builder dialog = new AlertDialog.Builder(
                                    AliPayActivity.this);
                            dialog.setTitle("支付详情");
                            dialog.setMessage("支付失败！请在订单详情里重新完成支付。");
                            dialog.show();
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
        setContentView(R.layout.wx_pay_ac);
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
        mRelativeLayout_shouxu = (RelativeLayout) findViewById(R.id.shouxu_relayout);


        mPayBtn = (Button) findViewById(R.id.pay_btn);
    }

    @Override
    protected void initData() {
        mTitleTV.setText("支付宝支付");
        mOrderNum = getIntent().getStringExtra("orderNumber");
        mOrderType = getIntent().getStringExtra("orderType");
        mOrderMoney = getIntent().getStringExtra("orderMoney");
        mOrderDesc = getIntent().getStringExtra("orderDesc");
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
            case R.id.pay_btn:// 发起支付宝支付
                MyLog.i("YUY", "微信支付参数orderMoney = " + mOrderMoney + " orderNum = "
                        + mOrderNum + " orderType = " + mOrderType + " payMoney = "
                        + mPayMoney);
                if (isGetRateSuccess == true) {
                    getPayPurpose();
                } else {
                    ToastUtil.toastShow(AliPayActivity.this, "支付信息不正确");
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
        NetworkUtil.postNoHeader(NetworkUtil.GET_RATE, null,
                new AjaxCallBack<String>() {
                    @Override
                    public void onSuccess(String t) {
                        super.onSuccess(t);
                        MyLog.i("YUY", "所有费率 = " + t);
                        try {
                            JSONObject jo = new JSONObject(t);
                            // inOut 1、 入账2、出账 rate 费率 type 1、微信 2、支付宝
                            if (jo.getInt("result") == 1) {
                                JSONArray rates = jo.getJSONObject("data")
                                        .getJSONArray("rates");
                                for (int i = 0; i < rates.length(); i++) {
                                    JSONObject jora = rates.getJSONObject(i);
                                    if (jora.getInt("inOut") == 1
                                            && jora.getInt("type") == 2) {// 入账的微信支付费率

                                        mRate = Double.valueOf(jora
                                                .getString("rate"));
                                        mShouxufei = WalletUtil.getSxf(Double
                                                .valueOf(mOrderMoney) * mRate);// 动态算出手续费

                                        mPayMoney = Double.valueOf(mOrderMoney)
                                                + mShouxufei;
                                        mOrderMoneyTV.setText(mOrderMoney);
                                        mShouXuFeiMoneyTV.setText(String
                                                .valueOf(mShouxufei));

                                        if (mShouxufei == 0.0) {
                                            mRelativeLayout_shouxu.setVisibility(View.GONE);
                                        }

                                        mPayMoneyTV.setText(String
                                                .valueOf(mPayMoney));
                                        isGetRateSuccess = true;
                                    }
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        super.onFailure(t, errorNo, strMsg);
                        ToastUtil.toastShow(AliPayActivity.this, "请求数据失败");
                    }
                });

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
                        payOrder(mOrderNum, mOrderType, mOrderDesc);
                        break;
                    case Constant.NET_DATA_FAIL:
                        ToastUtil.toastShow(AliPayActivity.this,
                                ((DataError) msg.obj).getErrorMessage());
                        break;
                    default:
                        break;
                }

            }
        }, params, NetworkUtil.ALIPAY_PAY_PURPOSE, false, 0);

    }

    /**
     * 支付订单
     */
    private void payOrder(String orderNum, String orderType,
                          String orderDescription) {
        MyLog.i("YUY", "支付信息  orderNum = " + orderNum + "==orderType = "
                + orderType + "==orderdeDescription = " + orderDescription
                + "== payMoney = " + mOrderMoney);
        final String orderInfo = AlipayUtil.getOrderInfo(orderNum, orderType,
                orderDescription, String.valueOf(mPayMoney));
        new Thread() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(AliPayActivity.this);
                // 设置为沙箱模式，不设置默认为线上环境
                // alipay.setSandBox(true);
                String result = alipay.pay(orderInfo, true);
                MyLog.i("YUY", "支付结果" + result);
                Message msg = new Message();
                msg.what = Constant.RQF_PAY;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        }.start();
    }
}
