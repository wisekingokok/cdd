/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package com.chewuwuyou.app.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.chewuwuyou.app.AppContext;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.PersonalInfo;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.LockPatternUtil;
import com.chewuwuyou.app.utils.Tools;
import com.chewuwuyou.app.widget.LockPatternView;

import java.util.List;


public class LoginPatternActivity extends CDDBaseActivity implements View.OnClickListener {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//
//
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    protected boolean isStealthModeEnabled() {
//        return !PreferenceUtils.getBoolean(PreferenceContract.KEY_PATTERN_VISIBLE,
//                PreferenceContract.DEFAULT_PATTERN_VISIBLE, this);
//    }
//
//    @Override
//    protected boolean isPatternCorrect(List<PatternView.Cell> pattern) {
//        return PatternLockUtils.isPatternCorrect(pattern, this);
//    }
//
//    @Override
//    protected void onForgotPassword() {
//
//        startActivity(new Intent(this, ResetPatternActivity.class));
//
//        // Finish with RESULT_FORGOT_PASSWORD.
//        super.onForgotPassword();
//    }
    /**
     * tangming  2016,7,30
     */

    private static final String TAG = "LoginGestureActivity";

    private PersonalInfo mPersonalInfo;// 个人信息实体
    private ImageView mImageView_head;
    private TextView mTextView_username;
    private TextView mTextView_id;


    LockPatternView lockPatternView;

    //错误的次数提示信息
    TextView messageTv;

    Button mButton_mangerPassword;

    private ImageButton mBackBtn;

    private TextView mHeaderTV;

    private static final long DELAYTIME = 600l;

    private String gesturePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_login);

        this.init();
        //   initData();
        initView();
    }

    @Override
    protected void initView() {

        if (!TextUtils.isEmpty(CacheTools.getUserData(Constant.NIKE_NAME))) {
            mTextView_username.setText(CacheTools.getUserData(Constant.NIKE_NAME));
        }
        if (!TextUtils.isEmpty(CacheTools.getUserData(Constant.USER_ID))) {
            mTextView_id.setText("ID：" + CacheTools.getUserData(Constant.USER_ID));
        }


        if (!TextUtils.isEmpty(CacheTools.getUserData(Constant.HEAD_URL)))
            ImageUtils.displayImage(CacheTools.getUserData(Constant.HEAD_URL), mImageView_head, 10, R.drawable.user_fang_icon, R.drawable.user_fang_icon);


    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    private void init() {

        mImageView_head = (ImageView) findViewById(R.id.user_portrait);
        mTextView_id = (TextView) findViewById(R.id.user_id);
        mTextView_username = (TextView) findViewById(R.id.user_name);
        mBackBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
        mBackBtn.setOnClickListener(this);
        mHeaderTV = (TextView) findViewById(R.id.sub_header_bar_tv);
        mHeaderTV.setText("验证手势密码");
        mHeaderTV.setOnClickListener(this);
        //得到当前用户的手势密码
        lockPatternView = (LockPatternView) findViewById(R.id.lockPatternView);
        messageTv = (TextView) findViewById(R.id.messageTv);
        mButton_mangerPassword = (Button) findViewById(R.id.managerGestureBtn);
        mButton_mangerPassword.setOnClickListener(this);

        gesturePassword = LockPatternUtil.getPattern(this);

        lockPatternView.setOnPatternListener(patternListener);
        //触觉感知
        // lockPatternView.setTactileFeedbackEnabled(true);
        //手势轨迹
        gestureGuijiAndNewPassword();
        updateStatus(Status.DEFAULT);
    }

    private void gestureGuijiAndNewPassword() {
        if (LockPatternUtil.isStealth(this))
            lockPatternView.setInStealthMode(false);
        else
            lockPatternView.setInStealthMode(true);

        gesturePassword = LockPatternUtil.getPattern(this);

    }

    private LockPatternView.OnPatternListener patternListener = new LockPatternView.OnPatternListener() {

        @Override
        public void onPatternStart() {
            lockPatternView.removePostClearPatternRunnable();
        }

        @Override
        public void onPatternComplete(List<LockPatternView.Cell> pattern) {
            if (pattern != null) {
                if (LockPatternUtil.checkPattern(pattern, gesturePassword)) {
                    updateStatus(Status.CORRECT);
                } else {
                    updateStatus(Status.ERROR);
                }
            }
        }
    };

    /**
     * 更新状态
     *
     * @param status
     */
    private void updateStatus(Status status) {
        if (status == Status.ERROR) {

            StringBuilder mStringBuilder = new StringBuilder();
            mStringBuilder.append(getString(status.strId));
            mStringBuilder.append(",您还有" + AppContext.getInstance().getCount() + "次机会");

            messageTv.setText(mStringBuilder.toString());


        } else
            messageTv.setText(status.strId);

        messageTv.setTextColor(getResources().getColor(status.colorId));
        switch (status) {
            case DEFAULT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case ERROR:
                lockPatternView.setPattern(LockPatternView.DisplayMode.ERROR);
                lockPatternView.postClearPatternRunnable(DELAYTIME);

                if (AppContext.getInstance().getCount() <= 0) {
                    forceReLogin();
                }
                AppContext.getInstance().setCount(AppContext.getInstance().getCount() - 1);
                break;
            case CORRECT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                loginGestureSuccess();
                break;
        }
    }

    private void forceReLogin() {

        new AlertDialog.Builder(this) //

                .setTitle("提示")
                .setMessage("由于输入次数过多，请重新登录")
                .setOnKeyListener(keylistener)
                .setCancelable(false)
                .setPositiveButton("重新登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        LockPatternUtil.clearPattern(LoginPatternActivity.this);
                        PayPasswordManagerActivity.closeGesture();
                        Tools.clearInfo(LoginPatternActivity.this);
                        Intent intent = new Intent(LoginPatternActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        AppContext.getInstance().setCount(4);

                    }
                })

                .create().show();


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

    /**
     * 手势验证成功
     */
    private void loginGestureSuccess() {

        //验证次数重置为0
        AppContext.getInstance().setCount(4);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn:

                finishActivity();
                break;

            case R.id.managerGestureBtn:


                Intent mIntent = new Intent(this, GestureManagerActivity.class);

                messageTv.setText(Status.DEFAULT.strId);
                messageTv.setTextColor(getResources().getColor(Status.DEFAULT.colorId));

                startActivityForResult(mIntent, 10);


                break;
            default:
                break;

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK) {

            Log.i("--->", "close");
//关闭了手势验证
            finish();

        } else {
            Log.i("--->", "contine  on");
            //验证手势轨迹
            gestureGuijiAndNewPassword();


        }


    }

    private void reLogin() {


        new AlertDialog.Builder(this) //

                .setTitle("提示")
                .setMessage("忘记手势密码，需要重新登录")

                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        LockPatternUtil.clearPattern(LoginPatternActivity.this);
                        PayPasswordManagerActivity.closeGesture();
                        Tools.clearInfo(LoginPatternActivity.this);
                        Intent intent = new Intent(LoginPatternActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                    }
                })
                .create().show();


    }

    private enum Status {
        //默认的状态
        DEFAULT(R.string.gesture_default, R.color.grey_a5a5a5),
        //密码输入错误
        ERROR(R.string.gesture_error, R.color.red_f4333c),
        //密码输入正确
        CORRECT(R.string.gesture_correct, R.color.grey_a5a5a5);

        private Status(int strId, int colorId) {
            this.strId = strId;
            this.colorId = colorId;
        }

        private int strId;
        private int colorId;
    }

}
