package com.chewuwuyou.app.ui;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.utils.MD5Util;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.RegexValidateUtil;
import com.chewuwuyou.app.utils.RegularUtil;
import com.chewuwuyou.app.utils.SmsContent;
import com.chewuwuyou.app.utils.ToastUtil;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * @version 1.1.0
 * @describe:用户注册
 * @author:yuyong
 * @created:2014-10-23下午2:48:33
 */
public class RegisterActivity extends BaseActivity {
    @ViewInject(id = R.id.register_phone)
    private EditText mRegisterPhone;// 注册电话
    @ViewInject(id = R.id.register_auth_code_et)
    private EditText mRegisterAuthCode;// 注册验证码
    @ViewInject(id = R.id.register_password)
    private EditText mRegisterPassword;// 注册密码
    @ViewInject(id = R.id.register_password_ok)
    private EditText mRegisterPasswordOk;// 确认密码
    @ViewInject(id = R.id.get_auth_code_btn, click = "onAction")
    private Button mGetAuthCodeBtn;// 获取验证码
    @ViewInject(id = R.id.register_btn, click = "onAction")
    private Button mHeaderRightBtn;// 注册
    private String telephone = "";// 注册电话
    private TimeCount time;
    @ViewInject(id = R.id.xieyi_tv, click = "onAction")
    private TextView mXieyiTV;// 点击查看服务协议
    @ViewInject(id = R.id.register_back_iv, click = "onAction")
    private ImageView mBackIV;
    private SmsContent mSmsContent;// 监听验证码的接受并自动填充
    public static RegisterActivity mInstance;
    private boolean mGetAuthCode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_register_ac);
        mInstance = this;
        // mHeaderTV.setText(R.string.user_register_title);
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
            case R.id.xieyi_tv:
                Intent intent = new Intent(RegisterActivity.this,
                        AgreementActivity.class);
                intent.putExtra("type", 2);
                startActivity(intent);
                break;
            case R.id.register_back_iv:
                finishActivity();
                break;
            case R.id.get_auth_code_btn:// 获取验证码
                mGetAuthCodeBtn.setClickable(false);
                telephone = mRegisterPhone.getText().toString().trim();
                // 判断是否为电话号码
                //  final String regTelephone = "^1[0-9]{10}$";
                if (RegexValidateUtil.checkCellphone(telephone)) {
                    sendAuthcode();
                    Acp.getInstance(RegisterActivity.this).request(new AcpOptions.Builder()
                                    .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE
                                            , Manifest.permission.CAMERA)
                                    .build(),
                            new AcpListener() {
                                @Override
                                public void onGranted() {
                                    getSms();
                                }

                                @Override
                                public void onDenied(List<String> permissions) {
                                    ToastUtil.toastShow(RegisterActivity.this, permissions.toString() + "权限拒绝");
                                }
                            });
                } else {
                    ToastUtil.showToast(RegisterActivity.this, R.string.please_input_true_telephone);
                    mGetAuthCodeBtn.setClickable(true);
                }
                break;

            case R.id.register_btn:// 注册
                final String username = mRegisterPhone.getText().toString().trim();
                final String password = mRegisterPassword.getText().toString().trim();
                String passwordok = mRegisterPasswordOk.getText().toString().trim();
                final String authcode = mRegisterAuthCode.getText().toString().trim();

                if (TextUtils.isEmpty(username)) {
                    showToast(R.string.please_input_true_telephone);
                } else if (TextUtils.isEmpty(authcode)) {
                    showToast(R.string.please_input_authcode);
                } else if (TextUtils.isEmpty(password)) {
                    showToast(R.string.please_input_password);
                } else if (password.length() != password.length()) {
                    showToast(R.string.password_error);
                    mRegisterPassword.setText("");
                    mRegisterPassword.setText("");
                } else if (TextUtils.isEmpty(passwordok)) {
                    showToast(R.string.please_two_input_password);
                } else if (password.length() < 6 || passwordok.length() < 6) {
                    showToast(R.string.password_length_error);
                } else if (!password.matches(RegularUtil.verifyPassword)
                        || !passwordok.matches(RegularUtil.verifyPassword)) {
                    showToast(R.string.password_gs_error);
                } else if (password.length() != password.trim().length()
                        || passwordok.length() != passwordok.trim().length()) {
                    showToast(R.string.password_error);
                } else if (!password.trim().equals(passwordok.trim())) {
                    showToast(R.string.password_is_refrences);
                } else {
                    AjaxParams params = new AjaxParams();
                    params.put("phone", username);
                    params.put("code", authcode);
                    NetworkUtil.postNoHeader(NetworkUtil.DO_GET_AUTHCODE, params, new AjaxCallBack<String>() {
                        @Override
                        public void onSuccess(String s) {
                            super.onSuccess(s);
                            try {
                                JSONObject jo = new JSONObject(s);
                                if (jo.getInt("code") != 0) {
                                    ToastUtil.toastShow(RegisterActivity.this, jo.getString("message"));
                                } else {
                                    Intent commitIntent = new Intent(RegisterActivity.this,
                                            RegisterCommitInfoActivity.class);
                                    commitIntent.putExtra("telephone", username);
                                    commitIntent.putExtra("password", MD5Util.getMD5(password));
                                    commitIntent.putExtra("code", authcode);
                                    startActivity(commitIntent);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Throwable t, int errorNo, String strMsg) {
                            super.onFailure(t, errorNo, strMsg);
                            ToastUtil.toastShow(RegisterActivity.this, "验证信息失败");
                        }
                    });

                }

        }

    }

    /**
     * 动态获取短信
     */
    private void getSms() {
        mSmsContent = new SmsContent(RegisterActivity.this, new Handler(), mRegisterAuthCode);
        this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, mSmsContent);
    }

    /**
     * 提示信息
     */
    public void showToast(int stringId) {
        ToastUtil.showToast(RegisterActivity.this, stringId);

    }

    /**
     * 获取验证码
     */
    public void sendAuthcode() {
        time = new TimeCount(120000, 1000);// 构造CountDownTimer对象
        telephone = mRegisterPhone.getText().toString().trim();
        AjaxParams params = new AjaxParams();
        params.put("phone", telephone);
        NetworkUtil.postNoHeader(NetworkUtil.GET_AUTHCODE, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                try {
                    JSONObject jo = new JSONObject(s);
                    MyLog.i("YUY", "注册后去验证码 = " + s);
                    if (jo.getInt("code") != 0) {
                        mGetAuthCodeBtn.setClickable(true);
                        ToastUtil.toastShow(RegisterActivity.this, jo.getString("message"));
                    } else {
                        ToastUtil.toastShow(RegisterActivity.this, "获取验证码成功");
                        time.start();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.toastShow(RegisterActivity.this, "发送验证码失败");
                mGetAuthCodeBtn.setClickable(true);
            }
        });
//        requestNet(new Handler() {
//
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                switch (msg.what) {
//                    case Constant.NET_DATA_SUCCESS:
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                sendMsg(telephone);
//                                time.start();
//                            }
//                        }).start();
//                        break;
//                    case Constant.NET_DATA_FAIL:
//                        ToastUtil.toastShow(RegisterActivity.this,
//                                ((DataError) msg.obj).getErrorMessage());
//                        mGetAuthCodeBtn.setClickable(true);
//                        break;
//                    case Constant.NET_REQUEST_FAIL:
//                        ToastUtil.toastShow(RegisterActivity.this, "获取验证码失败,请重新获取");
//                        mGetAuthCodeBtn.setClickable(true);
//                        break;
//                    default:
//                        break;
//                }
//
//            }
//        }, params, NetworkUtil.TEST_TELEPHONE, false, 0);

    }

    /**
     * 发送短信
     */
//    private void sendMsg(String telephone) {
//
//        AjaxParams params = new AjaxParams();
//        params.put("key", MD5Util.get32MD5("cDDang"));
//        params.put("plat", "1");// 代表平台:1安卓
//        params.put("mode", "0");
//        params.put("telephone", telephone);
//        NetworkUtil.postMulti(NetworkUtil.SEND_PHONE_MSG, params,
//                new AjaxCallBack<String>() {
//                    @Override
//                    public void onSuccess(String t) {
//                        super.onSuccess(t);
//                        MyLog.i("YUY", "获取验证码---------" + t);
//                        mGetAuthCode = true;//标识已获取验证码
//                    }
//
//                    @Override
//                    public void onFailure(Throwable t, int errorNo, String strMsg) {
//                        super.onFailure(t, errorNo, strMsg);
//                        MyLog.e("YUY", "获取验证码-----------" + strMsg);
//                    }
//                });
//
//    }
    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(RegisterActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(RegisterActivity.this);
    }
}
