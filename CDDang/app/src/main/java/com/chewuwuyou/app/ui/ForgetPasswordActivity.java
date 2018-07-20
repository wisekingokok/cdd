package com.chewuwuyou.app.ui;

import java.util.Random;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.baidu.mobstat.StatService;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.DialogUtil;
import com.chewuwuyou.app.utils.MD5Util;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.RegularUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.EditTextWithDelete;

/**
 * @version 1.1.0
 * @describe:忘记密码
 * @author:yuyong
 * @created:2014-10-23下午2:49:47
 */
public class ForgetPasswordActivity extends CDDBaseActivity implements
        OnClickListener {
    // @ViewInject(id = R.id.sub_header_bar_left_ibtn, click = "onAction")
    // private ImageButton mBackBtn;
    // @ViewInject(id = R.id.sub_header_bar_tv)
    // private TextView mHeaderTV;
    @ViewInject(id = R.id.forget_password_back_iv, click = "onAction")
    private ImageView mBackIV;
    @ViewInject(id = R.id.forget_password_phone)
    private EditTextWithDelete mRegisterPhone;// 注册电话
    @ViewInject(id = R.id.forget_password_auth_code_et)
    private EditText mRegisterAuthCode;// 注册验证码
    @ViewInject(id = R.id.forget_password_password)
    private EditText mRegisterPassword;// 注册密码
    @ViewInject(id = R.id.forget_password_password_ok)
    private EditText mRegisterPasswordOk;// 确认密码
    @ViewInject(id = R.id.get_auth_code_btn, click = "onAction")
    private Button mGetAuthCodeBtn;// 获取验证码
    @ViewInject(id = R.id.sub_header_bar_right_ibtn, click = "onAction")
    private ImageButton mHeaderRightBtn;// 联系客服
    private Button mUpdatePasswordBtn;// 修改密码
    //    private String mAuthCode = "";// 存储服务器返回的验证码
    private String telephone = "";// 注册电话
    //    private String tempPhone;
    private boolean mGetAuthCode = false;
    private TimeCount time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forger_password_ac);
        // mHeaderTV.setText(R.string.forget_password_title);
        initView();
        initData();
        initEvent();
    }

    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            mGetAuthCodeBtn.setClickable(true);
            mGetAuthCodeBtn.setText("获取验证码");
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            mGetAuthCodeBtn.setText(millisUntilFinished / 1000 + "秒");
            mGetAuthCodeBtn.setClickable(false);
        }
    }

    public void onAction(View v) {
        switch (v.getId()) {
            case R.id.forget_password_back_iv:
                finishActivity();
                break;
            case R.id.sub_header_bar_right_ibtn:
                DialogUtil.showContactServiceDialog(ForgetPasswordActivity.this);
                break;
            case R.id.get_auth_code_btn:// 获取验证码
                StatService.onEvent(ForgetPasswordActivity.this,
                        "clickForgetPassGetAuthCodeBtn", "点击获取验证码按钮");
                mGetAuthCodeBtn.setClickable(false);
                telephone = mRegisterPhone.getText().toString().trim();
                final String regTelephone = "^1[0-9]{10}$";
                if (telephone.matches(regTelephone)) {// 判断是否为电话号码
                    sendAuthcode();
                } else {
                    ToastUtil.showToast(ForgetPasswordActivity.this, R.string.please_input_true_telephone);
                    mGetAuthCodeBtn.setClickable(true);
                }
                break;

        }

    }

    /**
     * 提示信息
     */
    public void showToast(int stringId) {
        ToastUtil.showToast(ForgetPasswordActivity.this, stringId);
    }

    /**
     * 获取验证码
     */
    public void sendAuthcode() {
//        int intCount = 0;
//
//        intCount = (new Random()).nextInt(9999);
//
//        if (intCount < 1000) {
//            intCount += 1000;
//        }
//        authCode = String.valueOf(intCount);
        time = new TimeCount(120000, 1000);// 构造CountDownTimer对象
        // final String authcodetime = mGetAuthCodeBtn.getText().toString();
        AjaxParams params = new AjaxParams();
        params.put("telephone", telephone);
        requestNet(new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        mGetAuthCodeBtn.setClickable(true);
                        ToastUtil.showToast(ForgetPasswordActivity.this,
                                R.string.phone_no_register);
                        break;
                    case Constant.NET_DATA_FAIL:
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                sendMsg(telephone);
                                // SmsUtil.sendSmsCodeUseTemp(telephone,authCode);
                                time.start();
                            }
                        }).start();
                        mGetAuthCodeBtn.setClickable(true);
                        break;
                    default:
                        break;
                }

            }
        }, params, NetworkUtil.TEST_TELEPHONE, false, 0);

    }

    /**
     * 发送短信
     */
    private void sendMsg(String telephone) {

        AjaxParams params = new AjaxParams();
        params.put("key", MD5Util.get32MD5("cDDang"));
        params.put("plat", "1");
        params.put("mode", "1");
        params.put("telephone", telephone);
        NetworkUtil.postMulti(NetworkUtil.SEND_PHONE_MSG, params,
                 new AjaxCallBack<String>() {
                    @Override
                    public void onSuccess(String t) {
                        super.onSuccess(t);
                        mGetAuthCode = true;//标识获取验证码成功
                        //JSONObject jo = new JSONObject(t.toString());
//                            mAuthCode = jo.getJSONObject("data").getString(
//                                    "code");


                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo, String strMsg) {
                        super.onFailure(t, errorNo, strMsg);
                        mGetAuthCode=false;
                        ToastUtil.toastShow(ForgetPasswordActivity.this, "获取验证码失败，请重新获取");
                    }
                });

    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(ForgetPasswordActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(ForgetPasswordActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forget_password_submit_btn:
                StatService.onEvent(ForgetPasswordActivity.this,
                        "clickForgetPasswordSubmitBtn", "点击提交按钮");
                String username = mRegisterPhone.getText().toString().trim();
                mUpdatePasswordBtn.setClickable(false);
                String password = mRegisterPassword.getText().toString().trim();
                String passwordok = mRegisterPasswordOk.getText().toString().trim();
                String authcode = mRegisterAuthCode.getText().toString().trim();
                if (TextUtils.isEmpty(username)) {
                    showToast(R.string.please_input_true_telephone);
                    mUpdatePasswordBtn.setClickable(true);
                } else if (username.length() < 11) {
                    showToast(R.string.phone_error);
                    mUpdatePasswordBtn.setClickable(true);
                } else if (mGetAuthCode == false) {
                    ToastUtil.toastShow(ForgetPasswordActivity.this, "请先获取验证码");
                } else if (TextUtils.isEmpty(authcode)) {
                    showToast(R.string.please_input_authcode);
                    mUpdatePasswordBtn.setClickable(true);
                } else if (TextUtils.isEmpty(password)) {
                    showToast(R.string.please_input_password);
                    mUpdatePasswordBtn.setClickable(true);
                } else if (TextUtils.isEmpty(passwordok)) {
                    showToast(R.string.please_two_input_password);
                    mUpdatePasswordBtn.setClickable(true);
                } else if (TextUtils.isEmpty(mRegisterAuthCode.getText().toString())) {
                    showToast(R.string.authcode_erro);
                    mUpdatePasswordBtn.setClickable(true);
                } else if (!password.equals(passwordok)) {
                    showToast(R.string.password_is_refrences);
                    mUpdatePasswordBtn.setClickable(true);
                } else if (password.length() < 6 || passwordok.length() < 6) {
                    showToast(R.string.password_length_error);
                    mUpdatePasswordBtn.setClickable(true);
                } else if (!password.matches(RegularUtil.verifyPassword)
                        || !passwordok.matches(RegularUtil.verifyPassword)) {
                    mRegisterPassword.setText("");
                    mRegisterPasswordOk.setText("");
                    showToast(R.string.password_gs_error);
                    mUpdatePasswordBtn.setClickable(true);
                    // } else if
                    // (telephone.equals(CacheTools.getUserData("forget_telehone")))
                    // {
                }
                //motify start by yuyong 均在服务器作验证
//                else if (!tempPhone.equals(mRegisterPhone.getText().toString())) {
//                    showToast(R.string.register_phone_bind_code_error);
//                    mUpdatePasswordBtn.setClickable(true);
//                }
//                else if (!mAuthCode.equals(authcode)) {//判断服务器返回的验证码和输入的不一致
//                    ToastUtil.toastShow(ForgetPasswordActivity.this, "验证码不正确");
//                    mUpdatePasswordBtn.setClickable(true);
//                }
                else {
                    MyLog.e("YUY", "----修改密码参数 username = " + username + " code = " + authcode);
                    AjaxParams params = new AjaxParams();
                    params.put("telephone", username);
                    params.put("password", MD5Util.getMD5(password));
                    params.put("code", authcode);
                    requestNet(new Handler() {

                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            switch (msg.what) {
                                case Constant.NET_DATA_SUCCESS:
                                    mUpdatePasswordBtn.setClickable(true);
                                    Intent intent = new Intent(
                                            ForgetPasswordActivity.this,
                                            LoginActivity.class);
                                    startActivity(intent);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);//修改，取消静态应用activity，重新登录清空栈内所有activity。
                                    mUpdatePasswordBtn.setClickable(true);
                                    finishActivity();
                                    //TODO 请注明为什么要在这里关闭这类Activity
//                                    if (SafeSettingActivity.settingActivity != null)
//                                        SafeSettingActivity.settingActivity.finishActivity();
//                                    if (MainActivityEr.activity != null)
//                                        MainActivityEr.activity.finishActivity();
//                                    if (SettingActivity.settingActivity != null)
//                                        SettingActivity.settingActivity.finishActivity();
//                                        SafeSettingActivity.settingActivity.finishActivity();
//                                        MainActivityEr.activity.finishActivity();
//                                        SettingActivity.settingActivity.finishActivity();

                                    ToastUtil.toastShow(ForgetPasswordActivity.this,
                                            "密码修改成功");
                                    break;
                                case Constant.NET_DATA_FAIL:
                                    ToastUtil.toastShow(ForgetPasswordActivity.this,
                                            ((DataError) msg.obj).getErrorMessage());
                                    mUpdatePasswordBtn.setClickable(true);
                                    break;
                                case Constant.NET_DATA_EXCEPTION:
                                case Constant.NET_REQUEST_FAIL:
                                    ToastUtil.toastShow(ForgetPasswordActivity.this, "密码修改失败");
                                    mUpdatePasswordBtn.setClickable(true);
                                    break;
                                default:
                                    ToastUtil.toastShow(ForgetPasswordActivity.this, "密码修改失败");
                                    mUpdatePasswordBtn.setClickable(true);
                                    break;
                            }
                        }

                    }, params, NetworkUtil.FORGET_PASSWORD_URL, false, 0);

                    break;

                }

                break;

            default:
                break;
        }
    }

    @Override
    protected void initView() {
        mUpdatePasswordBtn = (Button) findViewById(R.id.forget_password_submit_btn);

    }

    @Override
    protected void initData() {
        // mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        // isTitle(mTitleHeight);//根据不同手机判断

    }

    @Override
    protected void initEvent() {
        mUpdatePasswordBtn.setOnClickListener(this);
    }
}
