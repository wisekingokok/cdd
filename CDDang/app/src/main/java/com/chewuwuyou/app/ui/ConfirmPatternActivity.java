/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package com.chewuwuyou.app.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.chewuwuyou.app.AppContext;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.LockPatternUtil;
import com.chewuwuyou.app.utils.PatternLockUtils;
import com.chewuwuyou.app.utils.PreferenceContract;
import com.chewuwuyou.app.utils.PreferenceUtils;
import com.chewuwuyou.app.utils.Tools;
import com.chewuwuyou.app.widget.LockPatternView;

import net.tsz.afinal.annotation.view.ViewInject;

import java.util.List;


public class ConfirmPatternActivity extends CDDBaseActivity implements View.OnClickListener {

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


    LockPatternView lockPatternView;

    TextView messageTv;

    Button forgetGestureBtn;

    private ImageButton mBackBtn;

    private TextView mHeaderTV;

    private static final long DELAYTIME = 600l;

    private String gesturePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_comfirm);

        this.init();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    private void init() {
        mBackBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
        mBackBtn.setOnClickListener(this);

        mHeaderTV = (TextView) findViewById(R.id.sub_header_bar_tv);
        mHeaderTV.setText("手势密码");
        mHeaderTV.setOnClickListener(this);
        //得到当前用户的手势密码
        lockPatternView = (LockPatternView) findViewById(R.id.lockPatternView);
        messageTv = (TextView) findViewById(R.id.messageTv);
        forgetGestureBtn = (Button) findViewById(R.id.forgetGestureBtn);
        forgetGestureBtn.setOnClickListener(this);

        gesturePassword = LockPatternUtil.getPattern(this);

        lockPatternView.setOnPatternListener(patternListener);
        //触觉感知
        // lockPatternView.setTactileFeedbackEnabled(true);
        if (LockPatternUtil.isStealth(this))
            lockPatternView.setInStealthMode(false);
        else
            lockPatternView.setInStealthMode(true);
        updateStatus(Status.DEFAULT);
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

                        LockPatternUtil.clearPattern(ConfirmPatternActivity.this);
                        PayPasswordManagerActivity.closeGesture();
                        Tools.clearInfo(ConfirmPatternActivity.this);
                        Intent intent = new Intent(ConfirmPatternActivity.this, LoginActivity.class);
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

        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn:

                finishActivity();
                break;

            case R.id.forgetGestureBtn:


                reLogin();


                break;
            default:
                break;

        }

    }

    private void reLogin() {




        new AlertDialog.Builder(this) //

                .setTitle("提示")
                .setMessage("忘记手势密码，需要重新登录")

                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        LockPatternUtil.clearPattern(ConfirmPatternActivity.this);
                        Tools.clearInfo(ConfirmPatternActivity.this);
                        Intent intent = new Intent(ConfirmPatternActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        AppContext.getInstance().setCount(4);
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
        DEFAULT(R.string.gesture_old, R.color.grey_a5a5a5),
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
