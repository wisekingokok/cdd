package com.chewuwuyou.app.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.chewuwuyou.app.AppContext;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.LockPatternUtil;
import com.chewuwuyou.app.utils.MD5Util;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.PreferenceContract;
import com.chewuwuyou.app.utils.PreferenceUtils;
import com.chewuwuyou.app.utils.Tools;

import net.tsz.afinal.annotation.view.ViewInject;

public class SafeSettingActivity extends CDDBaseActivity implements View.OnClickListener {


    private LinearLayout mPasswordManagerLayout;// 密码管理
    private LinearLayout mPhoneBindLayout;// 手机绑定
    private TextView mTitleTV;// 标题
    private ImageButton mBackIBtn;// 返回上一
    private LinearLayout mAccountManagerLL;// 账户管理
    private RelativeLayout mTitleHeight;// 标题布局高度
    public  SafeSettingActivity settingActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_settting);
        settingActivity = this;
        initView();// 初始化
        initData();// 逻辑处理
        initEvent();// 点击事件

    }

    @Override
    protected void initView() {
        mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
        mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
        mPasswordManagerLayout = (LinearLayout) findViewById(R.id.password_manager_layout);
        mPasswordManagerLayout.setOnClickListener(this);
        mPhoneBindLayout = (LinearLayout) findViewById(R.id.phone_bind_layout);
        mPhoneBindLayout.setOnClickListener(this);
    }

    @Override
    protected void initData() {

        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        isTitle(mTitleHeight);// 根据不同手机判断
        mTitleTV.setText("安全设置");

    }

    @Override
    protected void initEvent() {
        mBackIBtn.setOnClickListener(this);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
//            case R.id.account_manager_ll:
//                StatService.onEvent(SafeSettingActivity.this,
//                        "ClickMain1ActivityAccManager", "点击主界面账户管理");
//                if (CacheTools.getUserData("role") != null) {
//                    Intent accountIntent = new Intent(SafeSettingActivity.this,
//                            AccountManagerActivity.class);
//                    startActivity(accountIntent);
//                } else {
//                    Intent lIntent = new Intent(SafeSettingActivity.this,
//                            LunchPageActivity.class);
//                    startActivity(lIntent);
//                    finishActivity();
//                }
//
//
//                break;
            case R.id.password_manager_layout:// 密码管理
                StatService.onEvent(SafeSettingActivity.this,
                        "clickForgetPassword", "点击忘记密码");
                Intent forgetPasswordIntent = new Intent(
                        SafeSettingActivity.this, ForgetPasswordActivity.class);
                startActivity(forgetPasswordIntent);
                break;

            case R.id.phone_bind_layout:// 找回 密码
                // 不能进行手机绑定的修改 改为找回密码
                StatService.onEvent(SafeSettingActivity.this,
                        "clickResetPassword", "点击重置密码");
                Intent mIntent = new Intent(SafeSettingActivity.this,
                        UserPasswordManagerActivity.class);
                startActivity(mIntent);
                break;

            default:
                break;
        }
    }





}
