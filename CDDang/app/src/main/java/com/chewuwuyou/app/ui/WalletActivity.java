package com.chewuwuyou.app.ui;

import java.text.DecimalFormat;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.tools.ZysUtilsBtn;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.IsNetworkUtil;
import com.chewuwuyou.app.utils.LockPatternUtil;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.PatternLockUtils;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.utils.WalletUtil;
import com.chewuwuyou.app.widget.WaveView;

/**
 * 我的钱包 yuyong
 */
public class WalletActivity extends CDDBaseActivity implements OnClickListener {

    private ImageButton mBackIBtn;
    private TextView mTitleTV;
    // private ImageButton mMoreIBtn;
    // private Button mCccountWithdrawals;
    private TextView mBillDetailTV;// 账单明细
    private Button mBtnBalanceRecharge, mBtnBalanceReflect;//充值,提现

    private RelativeLayout mCashAccountRL, mSafeSettingRL;// 提现账户,安全设置
    private TextView mAvailableAccountTv;// 可提现账户
    private TextView mMyIncomeTv;// 余额
    private DecimalFormat mDF = new DecimalFormat("#0.00");// 保留两位小数
    private int mWithDrawNum = 0;// 可用提现账户
    private TextView mSafeSettingDesTV;// 安全设置描述
    private RelativeLayout mTitleHeight;// 标题布局高度
    private WaveView mWaveView;// 波纹

    //手势确认标识
    private boolean mConfirmStarted = false;
    private static final String KEY_CONFIRM_STARTED = "confirm_started";


    private int Identification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_wallet);

        comfirm(savedInstanceState);
        initView();
        initData();
        initEvent();

    }

    private void comfirm(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mConfirmStarted = savedInstanceState.getBoolean(KEY_CONFIRM_STARTED);
        }
        if (!mConfirmStarted) {
            LockPatternUtil.confirmPatternIfHas(WalletActivity.this);
            mConfirmStarted = true;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(KEY_CONFIRM_STARTED, mConfirmStarted);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (LockPatternUtil.checkConfirmPatternResult(this, requestCode, resultCode)) {
            Log.d("test", "输出");
            mConfirmStarted = false;
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
        mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
        mBillDetailTV = (TextView) findViewById(R.id.sub_header_bar_right_tv);
        mBillDetailTV.setText("明细");
        mBillDetailTV.setVisibility(View.VISIBLE);
        mCashAccountRL = (RelativeLayout) findViewById(R.id.cash_account_rl);
        mSafeSettingRL = (RelativeLayout) findViewById(R.id.safe_setting_rl);
        mBtnBalanceReflect = (Button) findViewById(R.id.btn_balance_reflect);
        mAvailableAccountTv = (TextView) findViewById(R.id.available_account_tv);// 提现账户
        mMyIncomeTv = (TextView) findViewById(R.id.my_income_tv);// 余额
        mSafeSettingDesTV = (TextView) findViewById(R.id.safe_setting_tv);
        mWaveView = (WaveView) findViewById(R.id.wave_view);
        mBtnBalanceRecharge = (Button) findViewById(R.id.btn_balance_recharge);
    }

    /**
     * 逻辑处理
     */
    @Override
    protected void initData() {
        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        isTitle(mTitleHeight);// 根据不同手机判断
        mTitleTV.setText("我的钱包");
        if (CacheTools.getUserData("walletMoney") != null) {
            double amount = Double.valueOf(CacheTools
                    .getUserData("walletMoney"));
            if (amount == 0) {
                mWaveView.setProgress(10);
            } else if (amount > 0 && amount <= 10000) {
                mWaveView.setProgress(50);
            } else {
                mWaveView.setProgress(80);
            }
        } else {
            mWaveView.setProgress(10);
        }
    }

    /**
     * 监听事件
     */
    @Override
    protected void initEvent() {
        mBackIBtn.setOnClickListener(this);
        // mMoreIBtn.setOnClickListener(this);
        // mCccountWithdrawals.setOnClickListener(this);
        mBillDetailTV.setOnClickListener(this);
        mCashAccountRL.setOnClickListener(this);
        mSafeSettingRL.setOnClickListener(this);
        mBtnBalanceReflect.setOnClickListener(this);
        mBtnBalanceRecharge.setOnClickListener(this);
    }

    /**
     * 点击事件
     */
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn:
                finishActivity();
                break;

            case R.id.sub_header_bar_right_tv:// 查看账单明细
                intent = new Intent(WalletActivity.this, WalletDetailActivity.class);
                startActivity(intent);
                break;

            case R.id.safe_setting_rl:// 安全设置
                Identification = Constant.CLICK_SAFE_SETTING;
                if (IsNetworkUtil.isNetworkAvailable(getApplicationContext())) {
                    if (ZysUtilsBtn.isFastClick()) {
                        return;
                    }
                    getPaymentPassword(Constant.CLICK_SAFE_SETTING);
                }
                else
                    ToastUtil.toastShow(this,"网络不可用，请检查您的网络是否连接");
                // getPaymentPassword(Constant.CLICK_SAFE_SETTING);
                break;
            case R.id.cash_account_rl:// 提现账户
                Identification = Constant.CLICK_TIXIAN_ACCOUNT;
                if (IsNetworkUtil.isNetworkAvailable(getApplicationContext())) {
                    if (ZysUtilsBtn.isFastClick()) {
                        return;
                    }
                    getPaymentPassword(Constant.CLICK_TIXIAN_ACCOUNT);
                }
                else
                    ToastUtil.toastShow(this,"网络不可用，请检查您的网络是否连接");
                // getPaymentPassword(Constant.CLICK_TIXIAN_ACCOUNT);
                // intent = new Intent(WalletActivity.this,
                // CccountWithdrawalsActivity.class);
                // startActivity(intent);
                break;
            case R.id.btn_balance_reflect:// 余额提现
                Identification = Constant.CLICK_BALANCE_TIXIAN;
                if (IsNetworkUtil.isNetworkAvailable(getApplicationContext())) {
                    if (ZysUtilsBtn.isFastClick()) {
                        return;
                    }
                    getPaymentPassword(Constant.CLICK_BALANCE_TIXIAN);
                }
                break;
            case R.id.btn_balance_recharge:// 充值
                intent = new Intent(this, WalletRechargeActivtiy.class);
                startActivity(intent);
            default:
                break;
        }
    }

    /**
     * 访问网络数据获取钱包首页数据
     */
    private void getWallet() {
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        MyLog.i("YUY", "wallet info = " + msg.obj);
                        try {
                            JSONObject jo = new JSONObject(msg.obj.toString());
                            // 显示钱包余额和可用提现账户
                            // {"result":1,"data":{"amount":"0","accountNo":"0"}}
                            mWithDrawNum = Integer.parseInt(jo
                                    .getString("accountNo"));
                            if (mWithDrawNum == 0) {
                                mAvailableAccountTv.setText("未设置提现账户");
                            } else {
                                mAvailableAccountTv.setText("可用账户" + mWithDrawNum);
                                Constant.IS_TIXIAN_ZHANGHU = 1;
                            }
                            double amount = Double.valueOf(jo.getString("amount"));
                            if (amount == 0) {
                                mWaveView.setProgress(10);
                            } else if (amount > 0 && amount <= 10000) {
                                mWaveView.setProgress(50);
                            } else {
                                mWaveView.setProgress(80);
                            }
                            // 缓存钱包余额及可用账户
                            CacheTools.setUserData("walletMoney",
                                    jo.getString("amount"));
                            CacheTools.setUserData("withDrawCount",
                                    jo.getString("accountNo"));
                            mMyIncomeTv.setText(WalletUtil.addComma(String
                                    .valueOf(mDF.format(amount))));
                            if (jo.getString("payPass").equals("true")) {
                                Constant.IS_SET_PAYPASS = 1;
                                mSafeSettingDesTV.setText("已设置支付密码");
                            } else {
                                mSafeSettingDesTV.setText("未设置支付密码");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;
                    case Constant.NET_DATA_FAIL:
                    case Constant.NET_REQUEST_FAIL:
                        if (TextUtils.isEmpty(CacheTools.getUserData("walletMoney"))) {
                            mMyIncomeTv.setText("0.00");
                            mAvailableAccountTv.setText("可用账户0");
                        } else {
                            mMyIncomeTv.setText(CacheTools
                                    .getUserData("walletMoney"));
                            mAvailableAccountTv.setText("可用账户"
                                    + CacheTools.getUserData("withDrawCount"));
                        }

                        break;
                }
            }
        }, null, NetworkUtil.GET_WALLET, false, 1);
    }

    /**
     * 访问网络判断是否设置支付密码 1、优先通过标识检测是否设置支付密码 2、通过网络检测是否设置支付密码
     */
    private void getPaymentPassword(final int clickType) {

        // 检测标识是否为空,不用每次请求网络判断是否设置支付密码
        if (Constant.IS_SET_PAYPASS == 1) {
            MyLog.i("YUY", "已标识支付密码已设置");
            Intent intent = null;
            switch (clickType) {
                case Constant.CLICK_TIXIAN_ACCOUNT://提现账户

				/* 有支付密码时，每次进入提现账户 都先 验证 支付密码成功后才能进入到 提现账户管理UI */

                    intent = new Intent(WalletActivity.this,
                            PayWordVerifyActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("WithDrawNum", mWithDrawNum);
                    intent.putExtras(bundle);

                    break;


                case Constant.CLICK_SAFE_SETTING://安全设置
                    intent = new Intent(WalletActivity.this,
                            PayPasswordManagerActivity.class);// 跳转支付密码管理
                    break;
                case Constant.CLICK_BALANCE_TIXIAN:// 余额提现
                    if (mWithDrawNum > 0) {// 直接跳转提现页面
                        intent = new Intent(WalletActivity.this,
                                BalanceWithdrawalsActivity.class);
                        intent.putExtra("amount",
                                CacheTools.getUserData("walletMoney"));
                    } else {
                        intent = new Intent(WalletActivity.this,
                                AddCcountWithdrawalsActivity.class);
                        intent.putExtra("Identification", Constant.CLICK_BALANCE_TIXIAN);
                        intent.putExtra("inten", 1);//表示没有设置提现账户
                    }
                default:
                    break;
            }
            startActivity(intent);
        } else {
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
                                Intent intent = null;
                                if (jo.getString("re").equals("false")) {// 未设置
                                    // intent = new Intent(WalletActivity.this,
                                    // SetPassWord.class);// 设置密码
                                    // intent.putExtra("again", "0");
                                    // intent.putExtra("payWord", "1");
                                    // intent.putExtra("clickType",
                                    // Constant.CLICK_SETTING_PAYPASS);// 传递是设置密码
                                    payWordDialog();
                                } else {
                                    Constant.IS_SET_PAYPASS = 1;// 修改支付密码已设置的标识
                                    switch (clickType) {
                                        case Constant.CLICK_TIXIAN_ACCOUNT:
                                            intent = new Intent(WalletActivity.this,
                                                    PayWordVerifyActivity.class);

                                            Bundle bundle = new Bundle();
                                            bundle.putInt("WithDrawNum", mWithDrawNum);
                                            intent.putExtras(bundle);
                                            break;
                                        case Constant.CLICK_SAFE_SETTING:
                                            intent = new Intent(WalletActivity.this,
                                                    PayPasswordManagerActivity.class);// 跳转支付密码管理

                                            break;
                                        case Constant.CLICK_BALANCE_TIXIAN:// 余额提现
                                            if (mWithDrawNum > 0) {// 直接跳转提现账户页面
                                                intent = new Intent(
                                                        WalletActivity.this,
                                                        BalanceWithdrawalsActivity.class);
                                                intent.putExtra("amount", CacheTools
                                                        .getUserData("walletMoney"));
                                            } else {
                                                intent = new Intent(
                                                        WalletActivity.this,
                                                        CccountWithdrawalsActivity.class);

                                            }
                                            startActivity(intent);
                                            break;
                                        default:
                                            break;
                                    }

                                }
                                // startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            break;

                        default:
                            break;
                    }

                }
            }, null, NetworkUtil.WHETHER_PASSWORD, false, 1);

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getWallet();// 访问网络获取数据
    }

    /**
     * 弹出未设置支付密码弹框
     */
    private void payWordDialog() {

        new com.chewuwuyou.app.utils.AlertDialog(WalletActivity.this).builder()
                .setTitle("您还未设置支付密码").setMsg("为了您的账户安全，建议您前往设置")
                .setPositiveButton("确定", new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {

                        Constant.IS_SECURITY = 2;// 将值存放到常用变量中去判断是否从安全支付进去
                        Intent intent = new Intent(WalletActivity.this,
                                SetPassWord.class);// 设置密码
                        intent.putExtra("again", "0");
                        intent.putExtra("payWord", "1");
                        intent.putExtra("Identification", Identification);
                        intent.putExtra("clickType", Constant.CLICK_SETTING_PAYPASS);
                        Constant.SETPASSWORD = Constant.CLICK_SETTING_PAYPASS;
                        startActivity(intent);
                    }
                }).setNegativeButton("取消", new OnClickListener() {

            @Override
            public void onClick(View arg0) {
            }
        }).show();

    }

}
