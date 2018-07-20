package com.chewuwuyou.app.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.AppContext;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.LockPatternUtil;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.PreferenceContract;
import com.chewuwuyou.app.utils.PreferenceUtils;

/**
 * 支付密码管理
 *
 * @author yuyong
 */
public class PayPasswordManagerActivity extends CDDBaseActivity implements
        OnClickListener {

    private TextView mTitleTV;// 标题
    private ImageButton mBackIBtn;// 返回上一页

    private TextView mUpdatePasswordTV;// 修改支付密码
    private TextView mForgetPasswordTV;// 忘记支付密码
    private RelativeLayout mTitleHeight;// 标题布局高度
    private int Identification;

    //手势密码管理
    private CheckBox mCheckBox_open;// 开启支付进入钱包的支付密码
    private CheckBox mCheckBox_orbit;//开启手势轨迹显示
    //修改手势密码
    private RelativeLayout mRelativeLayout_modify;
    private RelativeLayout mRelativeLayout_orbit;
    private static final int CANCEL_GUSTRUE = 10;
    private static final int SETPATTERN_GUSTURE = 11;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_password_manager_ac);
        initView();// 初始化
        initData();// 逻辑处理
        initEvent();// 点击事件
    }

    @Override
    protected void initView() {
        mUpdatePasswordTV = (TextView) findViewById(R.id.update_pay_password_tv);
        mForgetPasswordTV = (TextView) findViewById(R.id.forget_pay_password_tv);
        mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
        mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
        //手势密码
        mCheckBox_open = (CheckBox) findViewById(R.id.open_paypass_cb);
        mCheckBox_orbit = (CheckBox) findViewById(R.id.open_paypass_orbit_check);
        mRelativeLayout_orbit = (RelativeLayout) findViewById(R.id.paypass_setting_orbit);
        mRelativeLayout_modify = (RelativeLayout) findViewById(R.id.paypass_setting_modify);
    }

    /**
     * 逻辑处理
     */
    @Override
    protected void initData() {
        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        isTitle(mTitleHeight);// 根据不同手机判断
        mTitleTV.setText("支付密码管理");

        //手势密码
        if (!PreferenceUtils.getBoolean(PreferenceContract.IS_OPEN_GESTURE, false, this)) {
            Log.i("test", "手势关闭");
            mCheckBox_open.setChecked(false);
            hide();
        } else {
            Log.i("test", "手势打开");
            mCheckBox_open.setChecked(true);
            show();
        }
        if (PreferenceUtils.getBoolean(PreferenceContract.KEY_PATTERN_VISIBLE, false, this)) {
            mCheckBox_orbit.setChecked(true);
        } else {
            mCheckBox_orbit.setChecked(false);
        }

    }

    /**
     * 事件监听
     */
    @Override
    protected void initEvent() {
        mBackIBtn.setOnClickListener(this);
        mUpdatePasswordTV.setOnClickListener(this);
        mForgetPasswordTV.setOnClickListener(this);
        mRelativeLayout_modify.setOnClickListener(this);

        mCheckBox_open
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton arg0,
                                                 boolean arg1) {
                        if (arg1) {
                            MyLog.i("控制开启");
                            PreferenceUtils.putBoolean(PreferenceContract.IS_OPEN_GESTURE, true, PayPasswordManagerActivity.this);
                            show();
                        } else {
                            MyLog.i("控制关闭");
                            PreferenceUtils.putBoolean(PreferenceContract.IS_OPEN_GESTURE, false, PayPasswordManagerActivity.this);
                            hide();
                        }

                    }
                });

        mCheckBox_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCheckBox_open.isChecked()) {
                    //跳转到手势设置页面
                    new AlertDialog.Builder(PayPasswordManagerActivity.this)
                            .setTitle("提示")
                            .setMessage("只有进入钱包才需要手势密码")
                            .setOnKeyListener(keylistener)
                            .setCancelable(false)
                            .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent mIntent = new Intent(PayPasswordManagerActivity.this, com.chewuwuyou.app.ui.SetPatternActivity.class);
                                    startActivityForResult(mIntent, SETPATTERN_GUSTURE);

                                }
                            })
                            .create().show();

                } else {
                    MyLog.i("去关闭手势页面");
                    if (LockPatternUtil.hasPattern(PayPasswordManagerActivity.this)) {
                        //跳转到手势验证页面
                        Intent i = new Intent(PayPasswordManagerActivity.this, ConfirmPatternActivity.class);
                        startActivityForResult(i, CANCEL_GUSTRUE);
                    }
                }


            }
        });


//手势轨迹开关
        mCheckBox_orbit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    PreferenceUtils.putBoolean(PreferenceContract.KEY_PATTERN_VISIBLE, true, PayPasswordManagerActivity.this);
                } else {
                    PreferenceUtils.putBoolean(PreferenceContract.KEY_PATTERN_VISIBLE, false, PayPasswordManagerActivity.this);
                }
            }
        });

    }


    private void hide() {
        mRelativeLayout_modify.setVisibility(View.GONE);
        mRelativeLayout_orbit.setVisibility(View.GONE);
    }

    private void show() {
        mRelativeLayout_orbit.setVisibility(View.VISIBLE);
        mRelativeLayout_modify.setVisibility(View.VISIBLE);
        mCheckBox_orbit.setChecked(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            //关闭手势成功与否
            case CANCEL_GUSTRUE:

                if (resultCode == Activity.RESULT_OK) {
                    Log.i("tag-->", "关闭了手势验证");
                    LockPatternUtil.clearPattern(this);

                } else {
                    mCheckBox_open.setChecked(true);
                    PreferenceUtils.putBoolean(PreferenceContract.IS_OPEN_GESTURE, true, PayPasswordManagerActivity.this);
                    Log.i("tag-->", "没关闭手势验证");

                }

                break;
//设置手势成功与否的情况
            case SETPATTERN_GUSTURE:
                if (resultCode != Activity.RESULT_OK) {
                    mCheckBox_open.setChecked(false);
                    PreferenceUtils.putBoolean(PreferenceContract.IS_OPEN_GESTURE, false, PayPasswordManagerActivity.this);
                    Log.i("tag-->", "没有完成手势验证");
                    hide();
                } else {

                    Log.i("tag-->", "完成手势验证");

//                    new AlertDialog.Builder(this) //
//
//
//                            .setMessage("手势密码设置成功")
//
//                            .setCancelable(false)
//                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//
//                                }
//                            })
//
//                            .create().show();
                }

                break;
            default:
                break;


        }
    }

    //dialog对话框，监听返回键，强制用户重新登录
    DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                return true;
            } else {
                return false;
            }
        }
    };


    public static void closeGesture() {

        PreferenceUtils.putBoolean(PreferenceContract.IS_OPEN_GESTURE, false, AppContext.getInstance().getApplicationContext());
        PreferenceUtils.putBoolean(PreferenceContract.KEY_PATTERN_VISIBLE, false, AppContext.getInstance());

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
            case R.id.update_pay_password_tv:// 修改支付密码
                Identification = Constant.CLICK_UPDATE_PAYPASS;
                intent = new Intent(PayPasswordManagerActivity.this,
                        UpdatePayPasswordActivity.class);
                intent.putExtra("again", "0");// 1 密码不一致，重新输入
                intent.putExtra("Identification", Identification);
                startActivity(intent);
                break;
            case R.id.forget_pay_password_tv:// 忘记支付密码
                Identification = Constant.CLICK_FORGET_PAYPASS;
                intent = new Intent(PayPasswordManagerActivity.this,
                        VerifyPayActivity.class);
                intent.putExtra("Identification", Identification);
                startActivity(intent);
                break;
            case R.id.paypass_setting_modify:
                Intent i = new Intent(this, AlterGustureActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }

//    @Override
//    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//
//        //检测是否  手势已经设置完成
//        if (TextUtils.equals(key, PreferenceContract.KEY_PATTERN_SHA1)) {
//
//
//            Log.i("test", "设置完成");
//
//        } else {
//            mCheckBox_open.setChecked(false);
//            LockPatternUtil.clearPattern(this);
//        }
//
//
//    }
}
