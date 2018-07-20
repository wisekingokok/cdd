package com.chewuwuyou.app.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.chewuwuyou.app.AppContext;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.LockPatternUtil;
import com.chewuwuyou.app.utils.MD5Util;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.Tools;
import com.chewuwuyou.app.widget.LockPatternView;

import net.tsz.afinal.http.AjaxParams;

import java.nio.channels.CancelledKeyException;
import java.util.List;

/**
 * tangming  2016,7,30
 * 修改手势验证的密码
 */
public class AlterGustureActivity extends CDDBaseActivity implements View.OnClickListener {


    private static final int ALTERGUSTRUE_CODE = 20;
    LockPatternView lockPatternView;

    TextView messageTv;

    Button checkpasswordBtn;

    private ImageButton mBackBtn;

    private TextView mHeaderTV;

    private static final long DELAYTIME = 600l;

    private String gesturePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_gusture);
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
        mHeaderTV.setText("验证手势密码");
        mHeaderTV.setOnClickListener(this);


        //得到当前用户的手势密码
        lockPatternView = (LockPatternView) findViewById(R.id.lockPatternView);
        messageTv = (TextView) findViewById(R.id.messageTv);
        checkpasswordBtn = (Button) findViewById(R.id.checkPasswordBtn);
        checkpasswordBtn.setOnClickListener(this);
        gesturePassword = LockPatternUtil.getPattern(this);

        lockPatternView.setOnPatternListener(patternListener);
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
//                else
//                {
//                    promit();
//                }
                AppContext.getInstance().setCount(AppContext.getInstance().getCount() - 1);
                break;
            case CORRECT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                alterGestureSuccess();
                break;
        }
    }

    private void promit() {


        if (AppContext.getInstance().getCount() == 0) {
            forceReLogin();

        } else

        {

            new AlertDialog.Builder(this) //

                    .setTitle("登录密码错误")
                    .setMessage("您还可以再输入" + AppContext.getInstance().getCount() + "次")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(@NonNull DialogInterface dialog, int which) {
                        }
                    })
                    .create().show();


        }


        AppContext.getInstance().setCount(AppContext.getInstance().getCount() - 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ALTERGUSTRUE_CODE && resultCode == Activity.RESULT_OK) {
            Log.i("tag-->", "成功修改了手势验证");
            this.finishActivity();

        } else {


            Log.i("tag-->", "没成功手势验证");

        }


    }

    /**
     * 手势登录成功 或者 忘记了手势输入密码
     */
    private void alterGestureSuccess() {

        Intent intent = new Intent(this, SetPatternActivity.class);
        startActivityForResult(intent, ALTERGUSTRUE_CODE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn:

                finishActivity();
                break;

            case R.id.checkPasswordBtn:

                checkPassword();


            default:
                break;

        }

    }

    AlertDialog.Builder mAlertDialog;//

    private void checkPassword() {

        //验证密码前先还原提示语
        messageTv.setText(Status.DEFAULT.strId);
        messageTv.setTextColor(getResources().getColor(Status.DEFAULT.colorId));

        mAlertDialog = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.view_edittext, null);

        final EditText password = (EditText) view.findViewById(R.id.input);
        password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);


        mAlertDialog.setTitle("请输入登录密码")
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String s = password.getText().toString();

                        verifyPassword(MD5Util.getMD5(s));


                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                    }
                })
                .create().show();


    }

    private void verifyPassword(String mS) {


        // String url = "http://192.168.8.49:8080/mhwcw/api/user/checkPassword";
        AjaxParams params = new AjaxParams();
        params.put("pass", mS);
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:

                        if ("success".equals(String.valueOf(msg.obj)))

                            alterGestureSuccess();

                        else

                            promit();
                        break;
                    case Constant.NET_DATA_FAIL:


                        DataError mDataError = DataError.parse(String.valueOf(msg.obj));
                        showToastMessage(mDataError.getErrorMessage(), Toast.LENGTH_SHORT);

                        break;
                    default:
                        break;


                }


            }
        }, params, NetworkUtil.VERIFY_PASSWORD, false, 1);

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

                        LockPatternUtil.clearPattern(AlterGustureActivity.this);
                        PayPasswordManagerActivity.closeGesture();
                        Tools.clearInfo(AlterGustureActivity.this);
                        Intent intent = new Intent(AlterGustureActivity.this, LoginActivity.class);
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

    private void reLogin() {


        new AlertDialog.Builder(this) //

                .setTitle("提示")
                .setMessage("忘记手势密码，需要重新登录")

                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        LockPatternUtil.clearPattern(AlterGustureActivity.this);
                        Tools.clearInfo(AlterGustureActivity.this);
                        Intent intent = new Intent(AlterGustureActivity.this, LoginActivity.class);
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
    /**
     * 忘记手势密码（去账号登录界面）
     */


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
