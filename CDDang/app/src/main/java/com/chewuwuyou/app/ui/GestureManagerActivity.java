
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
import com.chewuwuyou.app.utils.LockPatternUtil;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.PreferenceContract;
import com.chewuwuyou.app.utils.PreferenceUtils;
import com.chewuwuyou.app.utils.Tools;

public class GestureManagerActivity extends CDDBaseActivity implements View.OnClickListener {


    private static final int CANCEL_GUSTRUE = 10;
    private static final int SETPATTERN_GUSTURE = 11;
    private TextView mTitleTV;// 标题
    private ImageButton mBackIBtn;// 返回上一页


    private RelativeLayout mTitleHeight;// 标题布局高度
    private CheckBox mCheckBox_open;// 开启支付进入钱包的支付密码
    private CheckBox mCheckBox_orbit;//开启手势轨迹显示
    //修改手势密码
    private RelativeLayout mRelativeLayout_modify;
    private RelativeLayout mRelativeLayout_orbit;
    private RelativeLayout mRelativeLayout_forget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_manager);
        initView();
        initData();
        initEvent();


    }

    @Override
    protected void initView() {
        mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
        mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
        mCheckBox_open = (CheckBox) findViewById(R.id.open_paypass_cb);
        mCheckBox_orbit = (CheckBox) findViewById(R.id.open_paypass_orbit_check);
        mRelativeLayout_orbit = (RelativeLayout) findViewById(R.id.paypass_setting_orbit);
        mRelativeLayout_modify = (RelativeLayout) findViewById(R.id.paypass_setting_modify);
        mRelativeLayout_forget = (RelativeLayout) findViewById(R.id.paypass_setting_forget);
        mRelativeLayout_forget.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        isTitle(mTitleHeight);// 根据不同手机判断
        mTitleTV.setText("手势密码管理");


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

    @Override
    protected void initEvent() {
        mBackIBtn.setOnClickListener(this);

        mRelativeLayout_modify.setOnClickListener(this);

        mCheckBox_open
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton arg0,
                                                 boolean arg1) {
                        if (arg1) {
                            MyLog.i("控制开启");
                            PreferenceUtils.putBoolean(PreferenceContract.IS_OPEN_GESTURE, true, GestureManagerActivity.this);
                            show();
                        } else {
                            MyLog.i("控制关闭");
                            PreferenceUtils.putBoolean(PreferenceContract.IS_OPEN_GESTURE, false, GestureManagerActivity.this);
                            hide();
                        }

                    }
                });

        mCheckBox_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCheckBox_open.isChecked()) {

                    if (LockPatternUtil.hasPattern(GestureManagerActivity.this)) {
                        //跳转到手势验证页面
                        Intent i = new Intent(GestureManagerActivity.this, ConfirmPatternActivity.class);
                        startActivityForResult(i, CANCEL_GUSTRUE);
                    }

                } else {
                    MyLog.i("应该是bug...");
                }


            }
        });


//手势轨迹开关
        mCheckBox_orbit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    PreferenceUtils.putBoolean(PreferenceContract.KEY_PATTERN_VISIBLE, true, GestureManagerActivity.this);
                } else {
                    PreferenceUtils.putBoolean(PreferenceContract.KEY_PATTERN_VISIBLE, false, GestureManagerActivity.this);
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
        if (PreferenceUtils.getBoolean(PreferenceContract.KEY_PATTERN_VISIBLE, false, this)) {
            mCheckBox_orbit.setChecked(true);
        } else {
            mCheckBox_orbit.setChecked(false);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {
            case CANCEL_GUSTRUE:

                if (resultCode == Activity.RESULT_OK) {
                    Log.i("tag-->", "关闭了手势验证");
                    LockPatternUtil.clearPattern(this);

                    setResult(RESULT_OK);
                    finish();

                } else {

                    mCheckBox_open.setChecked(true);
                    PreferenceUtils.putBoolean(PreferenceContract.IS_OPEN_GESTURE, true, GestureManagerActivity.this);

                    Log.i("tag-->", "没关闭手势验证");

                }

                break;

            case SETPATTERN_GUSTURE:

                if (resultCode != Activity.RESULT_OK) {

                    mCheckBox_open.setChecked(false);
                    PreferenceUtils.putBoolean(PreferenceContract.IS_OPEN_GESTURE, false, GestureManagerActivity.this);
                    Log.i("tag-->", "没有完成手势验证");
                    hide();


                }

                break;
            default:
                break;


        }


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


            case R.id.paypass_setting_modify:

                Intent i = new Intent(this, AlterGustureActivity.class);
                startActivity(i);

                break;


            case R.id.paypass_setting_forget:

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

                        LockPatternUtil.clearPattern(GestureManagerActivity.this);
                        PayPasswordManagerActivity.closeGesture();
                        Tools.clearInfo(GestureManagerActivity.this);
                        Intent intent = new Intent(GestureManagerActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);


                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {


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


}
