/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package com.chewuwuyou.app.ui;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.utils.LockPatternUtil;
import com.chewuwuyou.app.utils.PatternLockUtils;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.LockPatternIndicator;
import com.chewuwuyou.app.widget.LockPatternView;

import java.util.ArrayList;
import java.util.List;

public class SetPatternActivity extends CDDBaseActivity implements View.OnClickListener {

    private ImageButton mBackBtn;

    private TextView mHeaderTV;

    private TextView mTextView_reset;


    LockPatternIndicator lockPatternIndicator;

    LockPatternView lockPatternView;

    //   Button resetBtn;

    TextView messageTv;

    private List<LockPatternView.Cell> mChosenPattern = null;
    private String mPassWord;

    private static final long DELAYTIME = 600L;
    private static final String TAG = "CreateGestureActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_gesture);
        init();
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
        mHeaderTV.setText("设置手势密码");


        mTextView_reset = (TextView) findViewById(R.id.sub_header_bar_right_tv);
        mTextView_reset.setOnClickListener(this);

        lockPatternIndicator = (LockPatternIndicator) this.findViewById(R.id.lockPatterIndicator);
        lockPatternView = (LockPatternView) findViewById(R.id.lockPatternView);


        messageTv = (TextView) findViewById(R.id.messageTv);


        lockPatternView.setOnPatternListener(patternListener);
    }

    /**
     * 手势监听
     */
    private LockPatternView.OnPatternListener patternListener = new LockPatternView.OnPatternListener() {

        @Override
        public void onPatternStart() {
            lockPatternView.removePostClearPatternRunnable();
            //updateStatus(Status.DEFAULT, null);
            lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
        }

        @Override
        public void onPatternComplete(List<LockPatternView.Cell> pattern) {
            //Log.e(TAG, "--onPatternDetected--");
            if (mChosenPattern == null && pattern.size() >= 4) {
                mChosenPattern = new ArrayList<LockPatternView.Cell>(pattern);
                updateStatus(Status.CORRECT, pattern);


            } else if (mChosenPattern == null && pattern.size() < 4) {
                updateStatus(Status.LESSERROR, pattern);
            } else if (mChosenPattern != null) {
                if (mChosenPattern.equals(pattern)) {
                    updateStatus(Status.CONFIRMCORRECT, pattern);
                } else {
                    updateStatus(Status.CONFIRMERROR, pattern);
                }
            }
        }
    };

    /**
     * 更新状态
     *
     * @param status
     * @param pattern
     */
    private void updateStatus(Status status, List<LockPatternView.Cell> pattern) {
        messageTv.setTextColor(getResources().getColor(status.colorId));
        messageTv.setText(status.strId);
        switch (status) {
            case DEFAULT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case CORRECT:
                updateLockPatternIndicator();
                mTextView_reset.setVisibility(View.VISIBLE);
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case LESSERROR:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case CONFIRMERROR:
                lockPatternView.setPattern(LockPatternView.DisplayMode.ERROR);
                lockPatternView.postClearPatternRunnable(DELAYTIME);
                break;
            case CONFIRMCORRECT:
                saveChosenPattern(pattern);
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                setLockPatternSuccess();
                break;
        }
    }

    /**
     * 更新 Indicator
     */
    private void updateLockPatternIndicator() {
        if (mChosenPattern == null)
            return;
        lockPatternIndicator.setIndicator(mChosenPattern);
    }

    /**
     * 重新设置手势
     */

    void resetLockPattern() {
        Log.i("test-->", "重设");
        mChosenPattern = null;
        lockPatternIndicator.setDefaultIndicator();
        updateStatus(Status.DEFAULT, null);
        lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
    }

    /**
     * 成功设置了手势密码(跳到首页)
     */
    private void setLockPatternSuccess() {

        showToastMessage("密码设置成功", Toast.LENGTH_LONG);

        messageTv.postDelayed(new Runnable() {
            @Override
            public void run() {
                setResult(RESULT_OK);
                SetPatternActivity.this.finish();
            }
        }, 1000);
    }

    /**
     * 保存手势密码
     */
    private void saveChosenPattern(List<LockPatternView.Cell> cells) {

        LockPatternUtil.setPattern(cells, this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn:

                setResult(RESULT_CANCELED);
                finish();
                break;

            case R.id.sub_header_bar_right_tv:

                resetLockPattern();


            default:
                break;

        }
    }

    private enum Status {
        //默认的状态，刚开始的时候（初始化状态）
        DEFAULT(R.string.create_gesture_default, R.color.grey_a5a5a5),
        //第一次记录成功
        CORRECT(R.string.create_gesture_correct, R.color.grey_a5a5a5),
        //连接的点数小于4（二次确认的时候就不再提示连接的点数小于4，而是提示确认错误）
        LESSERROR(R.string.create_gesture_less_error, R.color.red_f4333c),
        //二次确认错误
        CONFIRMERROR(R.string.create_gesture_confirm_error, R.color.red_f4333c),
        //二次确认正确
        CONFIRMCORRECT(R.string.create_gesture_confirm_correct, R.color.grey_a5a5a5);

        private Status(int strId, int colorId) {
            this.strId = strId;
            this.colorId = colorId;
        }

        private int strId;
        private int colorId;
    }


    @Override
    public void onBackPressed() {


    }
}
