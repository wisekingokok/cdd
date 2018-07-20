package com.chewuwuyou.app.ui;

import java.io.StringReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.bean.PayMent;
import com.chewuwuyou.app.bean.Task;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.KeyboardUtil;
import com.chewuwuyou.app.utils.KeyboardUtil.InputFinishListener;
import com.chewuwuyou.app.utils.MD5Util;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ServiceUtils;
import com.chewuwuyou.app.utils.ToastUtil;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.unionpay.UPPayAssistEx;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:统一支付:支付宝支付、微信支付、钱包支付
 * @author:yuyong
 * @date:2015-7-8下午9:04:27
 * @version:1.2.1
 */

public class CommonPayActivity extends CDDBaseActivity implements
        OnClickListener {

    private KeyboardView keyboardView;
    private KeyboardUtil keyBoard;
    private TextView mTitleTV;
    private ImageButton mBackIBtn;
    private TextView mServiceNameTV;
    private TextView mServicePriceTV;
    private Button mPayBtn;
    public String mTaskId;
    public String mOrderType;
    private String mOrderMoney;
    private String mOrderDescription;
    private String mOrderNumber;
    private ListView mPayMentList;// 支付方式集合
    // 银联目前为测试环境，暂时注释
    private String[] paymentStr = {"支付宝快捷支付", "钱包支付"};// "微信支付",
    private Integer[] paymentImgIds = {R.drawable.zhifubao,
            R.drawable.wallet_icon};// R.drawable.weixin,
    private String[] paymentDesStr = {"推荐有支付宝账号的用户使用", "余额优先支付"};// ,
    // "推荐已安装微信客户端的用户使用",

    private List<PayMent> mPayMents;// 支付方式的描述
    private PayMent mPayMent;// 支付方式
    /*****************************************************************
     * mMode参数解释： "00" - 启动银联正式环境 "01" - 连接银联测试环境
     *****************************************************************/
    private final String mMode = "00";

    // 微信支付
    // private PayReq req;
    final IWXAPI msgApi = WXAPIFactory
            .createWXAPI(CommonPayActivity.this, null);
    // private TextView show;
    // private Map<String, String> resultunifiedorder;
    // private StringBuffer sb;
    private String mWxPayMoney;
    private PayMentAdapter mAdapter;

    private int mPayPosition = 0;// 支付方式(0:支付宝支付1：微信支付:2:钱包支付)
    // private ProgressDialog mWXPayDialog;
    private Task mTask;

    // private String mYinlian_Url =
    // "http://114.215.210.8:8081/cwwyw_admin/unionpay/makePayTnForUP";
    private String mYinlian_Url = "http://192.168.8.137:8080/cwwyw_admin/unionpay/makePayTnForUP";
    private String mPayPassWord;
    // private DecimalFormat mDF = new DecimalFormat("#0.00");// 保留两位小数
    public static CommonPayActivity mInstance = null;
    private String mCheckingAccount = "true";// 检测钱包支付是否可使用 冻结返回false 未冻结返回金额
    private double mWalletNum = 0.0;// 记录钱包余额
    private String mIsSetPayPass = "false";// 记录是否设置支付密码
    private RelativeLayout mTitleHeight;// 标题布局高度
    private boolean mIsGetWalletNum = false;
    private TextView mPayErrorText;
    DecimalFormat mDF = new DecimalFormat("######0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_pay_ac);
        mInstance = this;
        initView();
        initData();
        initEvent();
    }

    @Override
    protected void initView() {
        mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
        mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
        mServiceNameTV = (TextView) findViewById(R.id.service_name_tv);
        mServicePriceTV = (TextView) findViewById(R.id.service_price_tv);
        mPayBtn = (Button) findViewById(R.id.pay_btn);
        mPayMentList = (ListView) findViewById(R.id.payment_list);

    }

    @Override
    protected void initData() {
        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        isTitle(mTitleHeight);// 根据不同手机判断
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
        mPayMentList.setAdapter(mAdapter);

        mTitleTV.setText("在线支付");
        mTask = (Task) getIntent().getSerializableExtra(Constant.TASK_SER);
        mTaskId = mTask.getId();
        mOrderType = mTask.getType();
        mOrderNumber = mTask.getOrderNum();
        mServiceNameTV.setText(ServiceUtils.getProjectName(mTask
                .getProjectName()));
        // modify end bu yuyong 2016/06/15
        mOrderMoney = getIntent().getStringExtra("payMoney");
        mOrderDescription = getIntent().getStringExtra("orderDescription");
        if (TextUtils.isEmpty(mOrderDescription)) {
            mOrderDescription = mOrderType;
        }
        mServicePriceTV.setText(mDF.format(Double.valueOf(mOrderMoney)));
        mWxPayMoney = String.valueOf((int) (Double.valueOf(mOrderMoney) * 100));
        MyLog.i("YUY", "微信支付金额 = " + mWxPayMoney);
        // 判断是否设置支付密码
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        MyLog.i("YUY", "是否设置支付密码 = " + msg.obj);
                        // 返回re false未设置 true已设置

                        try {
                            JSONObject jo = new JSONObject(msg.obj.toString());
                            // judgeIsSetPayPass(jo.getString("re"));
                            mIsSetPayPass = jo.getString("re");
                            if (mIsSetPayPass.equals("true")) {
                                getWalletInfo();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;

                    default:
                        break;
                }

            }
        }, null, NetworkUtil.WHETHER_PASSWORD, false, 0);

    }

    /**
     * 检测余额及账户冻结情况
     */
    private void getWalletInfo() {
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        // 未冻结返回余额 冻结了返回冻结信息
                        MyLog.i("YUY", "余额及账户冻结信息 = " + msg.obj.toString());
                        // 1、重组支付方式的显示 2、判断支付方式是否可点击
                        try {
                            JSONObject jo = new JSONObject(msg.obj.toString());
                            mCheckingAccount = jo.getString("re");
                            if (mCheckingAccount.equals("false")) {// 账户被冻结
                                mPayMents.get(1)
                                        .setPayMentDes("您的账户已被冻结，请选择其他支付方式");
                                mPayMents.get(0).setChecked(true);
                                mPayMents.get(0).setChoose(true);
                                // mPayMents.get(1).setChecked(false);
                                mPayMents.get(1).setChecked(false);
                                mPayMents.get(1).setChoose(false);// 设置钱包支付不能选择
                                // mPayMents.get(1).setChoose(true);
                                mPayPosition = 0;
                                mAdapter.notifyDataSetChanged();
                            } else {
                                // xxxxxxxxxxxx
                                mIsGetWalletNum = true;// 设置通过请求获得钱包标识
                                mWalletNum = Double.valueOf(mCheckingAccount);
                                if (mWalletNum < Double.valueOf(mOrderMoney)) {
                                    mPayMents.get(1).setPayMentDes(
                                            "您的账户余额不足，请选择其他支付方式");
                                    mPayMents.get(0).setChecked(true);
                                    // mPayMents.get(1).setChecked(false);
                                    mPayMents.get(1).setChecked(false);
                                    mPayMents.get(1).setChoose(false);// 设置钱包支付不能选择
                                    mPayMents.get(0).setChoose(true);
                                    // mPayMents.get(1).setChoose(true);
                                    mPayPosition = 0;
                                    mAdapter.notifyDataSetChanged();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;

                    default:
                        break;
                }
            }
        }, null, NetworkUtil.CHECKING_ACCOUNT, false, 1);
    }

    @Override
    protected void initEvent() {

        mBackIBtn.setOnClickListener(this);
        mPayBtn.setOnClickListener(this);
        mPayMentList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long id) {

                if (mIsSetPayPass.equals("false") && position == 1) {//
                    fanDialog();
                }

                if (!mCheckingAccount.equals("false")
                        && mWalletNum >= Double.valueOf(mOrderMoney)) {// 判断钱包未被冻结及钱包余额不足支付的情况都不能选择钱包支付
                    mPayPosition = position;
                    for (int i = 0; i < mPayMents.size(); i++) {
                        if (position == i) {
                            mPayMents.get(i).setChecked(true);
                        } else {
                            mPayMents.get(i).setChecked(false);
                        }

                    }
                } else if (mCheckingAccount.equals("false") && position == 1) {// 不能使用余额支付又去点击余额支付就提示不能选择余额支付
                    ToastUtil.toastShow(CommonPayActivity.this,
                            "您的账户已被冻结，请选择其他支付方式");

                } else if (mIsGetWalletNum == true
                        && mWalletNum < Double.valueOf(mOrderMoney)
                        && position == 1) {// 通过请求获得钱包余额 并且余额小于当前支付金额 选择钱包支付时提示
                    ToastUtil.toastShow(CommonPayActivity.this,
                            "您的账户余额不足，请选择其他支付方式");
                } else {
                    mPayPosition = position;
                    for (int i = 0; i < mPayMents.size(); i++) {
                        if (position == i) {
                            mPayMents.get(i).setChecked(true);
                        } else {
                            mPayMents.get(i).setChecked(false);
                        }

                    }
                }
                mAdapter.notifyDataSetChanged();// 刷新适配器
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn:
                finishActivity();
                break;
            case R.id.pay_btn:

                // if (getIntent().getIntExtra("isDATING", 0) == 1) {// 由业务大厅进入担保支付
                // MyLog.i("YUY","offerId = "+getIntent().getStringExtra("offerId"));
                // AjaxParams params = new AjaxParams();
                // params.put("offerId", getIntent().getStringExtra("offerId"));
                // requestNet(new Handler() {
                // @Override
                // public void handleMessage(Message msg) {
                // super.handleMessage(msg);
                // switch (msg.what) {
                // case Constant.NET_DATA_SUCCESS:
                // MyLog.i("YUY", "B类订单支付 =  " + msg.obj.toString());
                // startPay();
                // break;
                // case Constant.NET_DATA_FAIL:
                // ToastUtil.toastShow(CommonPayActivity.this,
                // ((DataError) msg.obj).getErrorMessage());
                // break;
                // default:
                // break;
                // }
                // }
                // }, params, NetworkUtil.SETUP_ONE_PRICE, false, 0);
                // } else {
                // startPay();
                // }

                startPay();
                break;
            default:
                break;
        }
    }

    // 开始支付
    protected void startPay() {
        MyLog.e("YUY", "min = " + mTask.getMin());
        //对价格限制进行拦截
//        if (Double.valueOf(mOrderMoney) < Double.valueOf(mTask.getMin()) || Double.valueOf(mOrderMoney) > Double.valueOf(mTask.getMax())) {
//            ToastUtil.toastShow(CommonPayActivity.this, "支付价格不正确，不能支付");
//            return;
//        }

        if (mPayPosition == 0) {// 支付宝支付
            Intent intent = new Intent(CommonPayActivity.this,
                    AliPayActivity.class);
            intent.putExtra("orderType", mOrderType);
            intent.putExtra("orderMoney", mOrderMoney);// 支付金额
            intent.putExtra("orderNumber", mOrderNumber);// 订单号
            intent.putExtra("orderDesc", mOrderDescription);
            intent.putExtra("taskId", mTask.getId());
            startActivity(intent);
            // payOrder(mOrderNumber, mOrderType, mOrderDescription);

        } else if (mPayPosition == 1) {// 微信支付

            getPaymentPassword(Constant.CLICK_BALANCE_PAY);
//			
//			if (!msgApi.isWXAppInstalled()) {
//				ToastUtil.toastShow(CommonPayActivity.this, "没有安装微信");
//				return;
//			}
//			if (!msgApi.isWXAppSupportAPI()) {
//				ToastUtil.toastShow(CommonPayActivity.this, "当前版本不支持支付功能");
//				return;
//			}
//			Intent intent = new Intent(CommonPayActivity.this,
//					WXPayActivity.class);
//			intent.putExtra("orderType", mOrderType);
//			intent.putExtra("orderMoney", mOrderMoney);// 支付金额
//			intent.putExtra("orderNumber", mOrderNumber);// 订单号
//			intent.putExtra("taskId", mTask.getId());
//			startActivity(intent);

            // createPayOrder();// 修改为算出手续费率后再进行支付
        } else if (mPayPosition == 2) {// 钱包仿支付宝支付


        } else {// 银联支付
            AjaxParams params = new AjaxParams();
            // 需改为mOrderMoney 因为微信支付的金额单位为分，银联支付金额单位为元
            params.put("money", mWxPayMoney);
            params.put("desc", mOrderType + mOrderDescription);
            params.put("orderId", mTaskId);
            NetworkUtil.postNoHeader(mYinlian_Url, params,
                    new AjaxCallBack<String>() {
                        @Override
                        public void onSuccess(String t) {
                            super.onSuccess(t);
                            MyLog.i("YUY", "银联返回 = " + t);
                            try {
                                JSONObject jo = new JSONObject(t.toString());
                                // 银联支付
                                int ret = UPPayAssistEx.startPay(
                                        CommonPayActivity.this, null, null, jo
                                                .getJSONObject("data")
                                                .getString("tn"), mMode);
                                if (ret == UPPayAssistEx.PLUGIN_NOT_FOUND) {
                                    // 安装Asset中提供的UPPayPlugin.apk
                                    UPPayAssistEx
                                            .installUPPayPlugin(CommonPayActivity.this);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Throwable t, int errorNo,
                                              String strMsg) {
                            super.onFailure(t, errorNo, strMsg);
                        }
                    });
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*************************************************
         * 步骤3：处理银联手机支付控件返回的支付结果
         ************************************************/
        if (data == null) {
            return;
        }
        // if (data.getIntExtra("errCode", 1) == 0) {// 微信支付成功返回处理
        // addOrder(mTaskId, mOrderType);
        // return;
        // }
        String msg = "";
        /*
         * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
		 */
        String str = data.getExtras().getString("pay_result");
        if (str.equalsIgnoreCase("success")) {
            // addOrder(mTaskId, mOrderType);
            msg = "支付成功！";
        } else if (str.equalsIgnoreCase("fail")) {
            msg = "支付失败！";
        } else if (str.equalsIgnoreCase("cancel")) {
            msg = "用户取消了支付";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("支付结果通知");
        builder.setMessage(msg);
        builder.setInverseBackgroundForced(true);
        // builder.setCustomTitle();
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public Map<String, String> decodeXml(String content) {

        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if ("xml".equals(nodeName) == false) {
                            // 实例化student对象
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
            Log.e("orion", e.toString());
        }
        return null;

    }

    // private void sendPayReq(PayReq req) {
    // msgApi.registerApp(Constants.APP_ID);
    // msgApi.sendReq(req);
    // if (mWXPayDialog != null) {
    // mWXPayDialog.dismiss();
    // }
    // }

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

    // private void createPayOrder() {
    // AjaxParams params = new AjaxParams();
    // params.put("body", mOrderType);
    // params.put("out_trade_no", mOrderNumber);
    // params.put("spbill_create_ip", "127.0.0.1");
    // params.put("total_fee", mWxPayMoney);
    // params.put("trade_type", "APP");
    // new NetworkUtil().postMulti(NetworkUtil.WXPAY_NOTIFY_URL, params,
    // new AjaxCallBack<String>() {
    // @Override
    // public void onSuccess(String t) {
    // super.onSuccess(t);
    // // 从t里面获取"prepay_id"
    // JSONObject jo;
    // try {
    // jo = new JSONObject(t);
    // JSONObject data = jo.getJSONObject("data");
    // PayReq req = new PayReq();
    // req.appId = data.getString("appid");
    // req.nonceStr = data.getString("noncestr");
    // req.packageValue = data.getString("package");
    // req.partnerId = data.getString("partnerid");
    // req.prepayId = data.getString("prepayid");
    // req.timeStamp = data.getString("timestamp");
    // req.sign = data.getString("sign");
    //
    // MyLog.e("YUY",
    // "微信支付参数 = " + data.getString("appid") + " "
    // + data.getString("noncestr") + " "
    // + data.getString("package") + " "
    // + data.getString("partnerid") + " "
    // + data.getString("prepayid") + " "
    // + data.getString("timestamp") + " "
    // + data.getString("sign"));
    // sendPayReq(req);
    // finishActivity();
    //
    // } catch (JSONException e) {
    // e.printStackTrace();
    // }
    //
    // }
    //
    // @Override
    // public void onFailure(Throwable t, int errorNo,
    // String strMsg) {
    // super.onFailure(t, errorNo, strMsg);
    // }
    //
    // @Override
    // public void onStart() {
    // super.onStart();
    // mWXPayDialog = ProgressDialog.show(
    // CommonPayActivity.this, null,
    // getString(R.string.getting_prepayid));
    // }
    //
    // });
    // }

    public void createWalletPayDetailsDialog(Context context) {
        final Dialog dialog = new Dialog(context, R.style.myDialogTheme);
        LayoutInflater inflater = LayoutInflater.from(context);
        final LinearLayout layout = (LinearLayout) inflater.inflate(
                R.layout.wallet_pay_details_layout, null);
        dialog.setContentView(layout);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.mystyle); // 添加动画

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); // 设置宽度
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        TextView orderNameTV = (TextView) layout
                .findViewById(R.id.order_name_tv);
        TextView orderNumTV = (TextView) layout
                .findViewById(R.id.order_number_tv);
        TextView orderPayTV = (TextView) layout
                .findViewById(R.id.order_pay_money);
        orderNameTV
                .setText(ServiceUtils.getProjectName(mTask.getProjectName()));
        orderNumTV.setText(mTask.getOrderNum());
        orderPayTV.setText(mDF.format(Double.valueOf(mOrderMoney)));
        ImageView closePayIV = (ImageView) layout
                .findViewById(R.id.wallet_pay_close_iv);
        Button payBtn = (Button) layout.findViewById(R.id.ok_pay_btn);
        closePayIV.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        payBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                createWalletPayDialog(CommonPayActivity.this);
            }
        });

    }

    @SuppressWarnings("deprecation")
    public void createWalletPayDialog(final Context context) {
        final Dialog dialog = new Dialog(context, R.style.myDialogTheme);
        LayoutInflater inflater = LayoutInflater.from(context);
        final LinearLayout layout = (LinearLayout) inflater.inflate(
                R.layout.wallet_pay_layout, null);
        dialog.setContentView(layout);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.myrightstyle); // 添加动画

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); // 设置宽度
        dialog.getWindow().setAttributes(lp);

        ImageView closePayIV = (ImageView) layout
                .findViewById(R.id.wallet_pay_close_iv);
        TextView mForgetPayPassTV = (TextView) layout
                .findViewById(R.id.forget_pay_password_tv);
        mPayErrorText = (TextView) layout.findViewById(R.id.text_error);
        mForgetPayPassTV.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, VerifyPayActivity.class);
                intent.putExtra("clilckType", Constant.CLICK_FORGET_PAYPASS);
                intent.putExtra("Identification", Constant.CLICK_FORGET_PAYPASS);
                startActivity(intent);
            }
        });
        closePayIV.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                createWalletPayDetailsDialog(context);
            }
        });
        LinearLayout layout_input = (LinearLayout) layout
                .findViewById(R.id.layout_input);

        keyboardView = (KeyboardView) layout.findViewById(R.id.keyboard_view);
        keyBoard = new KeyboardUtil(this, this, keyboardView, layout_input,
                new InputFinishListener() {

                    @Override
                    public void inputHasOver(String text) {
                        mPayPassWord = text;
                        walletPay(dialog);
                    }
                });
        keyBoard.showKeyboard();
        dialog.show();
        layout_input.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                keyBoard.showKeyboard();
                return false;
            }
        });
    }

    /**
     * 钱包支付
     *
     * @param dialog
     */
    private void walletPay(final Dialog dialog) {
        MyLog.i("YUY", "支付金额 = " + mOrderMoney +"taskId =  "+mTaskId+ "paypass"+mPayPassWord);
        AjaxParams params = new AjaxParams();
        params.put("amount", mOrderMoney);
        params.put("taskId", mTaskId);
        params.put("payPass", MD5Util.getMD5(mPayPassWord));
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.obj != null) {
                    switch (msg.what) {
                        case Constant.NET_DATA_SUCCESS:
                            MyLog.i("YUY", "支付结果 = " + msg.obj.toString());
                            try {
                                JSONObject jo = new JSONObject(msg.obj.toString());
                                ToastUtil.toastShow(CommonPayActivity.this,
                                        jo.getString("re"));
                                if (jo.getString("re").equals("余额付款成功")) {
                                    dialog.dismiss();
                                    finishActivity();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            break;
                        case Constant.NET_DATA_FAIL:
                            MyLog.i("YUY", "支付失败 = " + msg.obj);

                            mPayErrorText.setText(((DataError) msg.obj)
                                    .getErrorMessage().toString());
                            mPayErrorText.setVisibility(View.VISIBLE);
                            // ToastUtil.toastShow(CommonPayActivity.this,
                            // ((DataError) msg.obj).getErrorMessage());
                            keyBoard.clearText();
                            if (((DataError) msg.obj).getErrorMessage().equals(
                                    "支付密码输入错误,你的账户已被冻结")) {
                                dialog.dismiss();
                                Intent intent = new Intent(CommonPayActivity.this,
                                        AccountBlockingActivity.class);
                                startActivity(intent);
                            }
                            break;
                        default:
                            ToastUtil.toastShow(CommonPayActivity.this, "钱包支付失败，请重新支付");
                            dialog.dismiss();
                            break;
                    }
                } else {
                    ToastUtil.toastShow(CommonPayActivity.this, "网络异常");
                    dialog.dismiss();
                }


            }

        }, params, NetworkUtil.BALANCE_PAYMENT, false, 0);
    }

    /**
     * 访问网络判断是否设置支付密码
     */
    private void getPaymentPassword(final int clickType) {

        if (Constant.IS_SET_PAYPASS == 1) {
            createWalletPayDetailsDialog(CommonPayActivity.this);
            return;
        }

        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        MyLog.i("YUY", "是否设置支付密码 = " + msg.obj);
                        // 返回re false未设置 true已设置

                        try {
                            JSONObject jo = new JSONObject(msg.obj.toString());
                            judgeIsSetPayPass(jo.getString("re"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;

                    default:
                        break;
                }

            }
        }, null, NetworkUtil.WHETHER_PASSWORD, false, 0);
    }

    /**
     * 判断是否设置支付密码
     *
     * @param isSetPayPass
     */
    private void judgeIsSetPayPass(String isSetPayPass) {
        if (isSetPayPass.equals("false")) {// 未设置
            if (mInstance != null) {
                fanDialog();

            }
        } else {// 调余额
            Constant.IS_SET_PAYPASS = 1;// 修改支付密码已设置的标识
            createWalletPayDetailsDialog(CommonPayActivity.this);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mInstance = null;
    }

    /**
     * 提示是否设置支付密码
     */
    public void fanDialog() {
        new com.chewuwuyou.app.utils.AlertDialog(CommonPayActivity.this)
                .builder().setTitle("您还未设置支付密码")
                .setMsg("为了您的账户安全，建议您前往设置")
                .setPositiveButton("确定", new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {

                        Constant.IS_PAGE = 1;// 将值存放到常用变量中去判断是否从在线支付进去
                        Intent intent = new Intent(CommonPayActivity.this, SetPassWord.class);// 设置密码
                        intent.putExtra("again", "0");
                        intent.putExtra("payWord", "1");
                        intent.putExtra("clickType", Constant.CLICK_SETTING_PAYPASS);
                        intent.putExtra("Identification", Constant.CLICK_SETTING_PAYPASS);
                        startActivity(intent);
                        finishActivity();

                    }
                }).setNegativeButton("取消", new OnClickListener() {

            @Override
            public void onClick(View arg0) {

            }
        }).show();
    }
}
