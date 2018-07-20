package com.chewuwuyou.app.ui;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxParams;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.MD5Util;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;

/**
 * @version 1.1.0
 * @describe:密码管理
 * @author:yuyong
 * @created:2014-11-7下午5:18:24
 */
public class UserPasswordManagerActivity extends BaseActivity {
    @ViewInject(id = R.id.sub_header_bar_left_ibtn)
    private ImageButton mBackBtn;
    @ViewInject(id = R.id.sub_header_bar_tv)
    private TextView mHeaderTV;
    @ViewInject(id = R.id.now_password)
    private EditText mNowPasswordET;// 原密码
    @ViewInject(id = R.id.new_password)
    private EditText mNewPasswordET;// 新密码
    @ViewInject(id = R.id.new_password_ok)
    private EditText mNewPasswordOkET;// 确认新密码
    @ViewInject(id = R.id.password_manager_btn, click = "onAction")
    private Button mUpdatePasswordBtn;// 修改密码
    private RelativeLayout mTitleHeight;// 标题布局高度
    TextWatcher mWatcher = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {
            try {

                String temp = s.toString();

                String tem = temp.substring(temp.length() - 1, temp.length());

                char[] temC = tem.toCharArray();

                int mid = temC[0];

                if (mid >= 48 && mid <= 57) {// 数字
                    return;
                }

                if (mid >= 65 && mid <= 90) {// 大写字母
                    return;
                }

                if (mid >= 97 && mid <= 122) {// 小写字母
                    return;
                }

                if (mid == 95) {// 下划线
                    return;
                }

                s.delete(temp.length() - 1, temp.length());

            } catch (Exception e) {

            }

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {
            // TODO Auto-generated method stub

        }
    };

    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case Constant.NET_DATA_SUCCESS:
                    ToastUtil.showToast(UserPasswordManagerActivity.this,
                            R.string.update_password_success);
                    requestNet(new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            // TODO Auto-generated method stub
                            super.handleMessage(msg);
                            switch (msg.what) {
                                case Constant.NET_DATA_SUCCESS:
                                    MyLog.i("YUY", "====注销成功===");
                                    Intent intent = new Intent(
                                            UserPasswordManagerActivity.this,
                                            LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);//修改，取消静态应用activity，重新登录清空栈内所有activity。
                                    CacheTools.clearUserData("telephone");
                                    CacheTools.clearUserData("loginpassword");
                                    CacheTools.clearUserData("role");
                                    CacheTools.clearAllObject();
                                    unregisterJPush();
                                    startActivity(intent);
                                    finishActivity();
//							SafeSettingActivity.settingActivity.finishActivity();
//							SettingActivity.settingActivity.finishActivity();
//							MainActivityEr.activity.finishActivity();//关闭首页
                                    break;
                                case Constant.NET_DATA_FAIL:
                                    ToastUtil.toastShow(
                                            UserPasswordManagerActivity.this,
                                            ((DataError) msg.obj).getErrorMessage());
                                    break;
                                default:
                                    break;
                            }
                        }
                    }, null, NetworkUtil.EXIT_LOGIN_URL, false, 0);
                    break;

            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_manager_ac);

        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        isTitle(mTitleHeight);// 根据不同手机判断
        mHeaderTV.setText(R.string.password_manager_title);
        mBackBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finishActivity();
            }
        });
        mNowPasswordET.requestFocus();
        mNowPasswordET.addTextChangedListener(mWatcher);
        mNewPasswordET.addTextChangedListener(mWatcher);
        mNewPasswordOkET.addTextChangedListener(mWatcher);
    }

    public void onAction(View v) {

        String oldPassword = mNowPasswordET.getText().toString().trim();
        // 得到输入的新密码
        String newpassword = mNewPasswordET.getText().toString().trim();
        String newPassword1 = mNewPasswordET.getText().toString().trim();
        // 得到输入的重复密码
        String repeatpassword = mNewPasswordOkET.getText().toString();
        // 判断
        if (TextUtils.isEmpty(oldPassword)) {
            ToastUtil.showToast(UserPasswordManagerActivity.this,
                    R.string.please_input_old_password);
        } else if (TextUtils.isEmpty(newpassword)) {
            ToastUtil.showToast(UserPasswordManagerActivity.this,
                    R.string.please_input_newpassword);
        } else if (TextUtils.isEmpty(repeatpassword)) {
            ToastUtil.showToast(UserPasswordManagerActivity.this,
                    R.string.please_input_newpassword_ok);
        } else if (newpassword.length() != newPassword1.length()
                || repeatpassword.length() != repeatpassword.trim().length()) {
            ToastUtil.showToast(UserPasswordManagerActivity.this,
                    R.string.password_uninclude_space);
            mNewPasswordET.setText("");
            mNewPasswordOkET.setText("");
            mNewPasswordET.setFocusable(true);
            mNewPasswordET.setFocusableInTouchMode(true);
            mNewPasswordET.requestFocus();
            mNewPasswordET.requestFocusFromTouch();
        } else if (newpassword.length() < 6 || repeatpassword.length() < 6) {
            ToastUtil.showToast(UserPasswordManagerActivity.this,
                    R.string.password_length);
        } else if (!newpassword.equals(repeatpassword)) {
            ToastUtil.showToast(UserPasswordManagerActivity.this,
                    R.string.twice_password_error);
        } else if (oldPassword.equals(newpassword)) {
            ToastUtil.toastShow(UserPasswordManagerActivity.this, "不能和原密码一致");
        } else {
            AjaxParams params = new AjaxParams();
            params.put("password", MD5Util.getMD5(newpassword));
            params.put("oldPass", MD5Util.getMD5(oldPassword));
            // 向服务器请求数据
            requestNet(mHandle, params, NetworkUtil.UPDATE_PASSWORD_URL, true,
                    0);
        }

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        StatService.onPause(UserPasswordManagerActivity.this);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        StatService.onResume(UserPasswordManagerActivity.this);
    }
}
