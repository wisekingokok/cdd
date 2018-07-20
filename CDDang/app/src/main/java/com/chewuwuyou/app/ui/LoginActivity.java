package com.chewuwuyou.app.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.chewuwuyou.app.AppContext;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.transition_entity.UserBean;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.ErrorCodeUtil;
import com.chewuwuyou.app.utils.IsNetworkUtil;
import com.chewuwuyou.app.utils.MD5Util;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ServerUtils;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.utils.Tools;
import com.chewuwuyou.app.utils.VersionUtil;
import com.chewuwuyou.app.widget.EditTextWithDelete;
import com.chewuwuyou.rong.bean.TokenBean;
import com.chewuwuyou.rong.utils.RongApi;
import com.google.gson.Gson;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import io.rong.imlib.RongIMClient;

/**
 * @version 1.1.0
 * @describe:用户登录
 * @author:yuyong
 * @created:2014-10-21 下午2:24:06
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(id = R.id.loginback_iv)
    ImageView mBackIV;
    @ViewInject(id = R.id.login_btn)
    Button mLoginBtn;
    @ViewInject(id = R.id.username)
    EditTextWithDelete mUserName;
    @ViewInject(id = R.id.password)
    EditTextWithDelete mPassword;
    @ViewInject(id = R.id.forget_password_tv)
    private TextView mForgetPasswordTV;// 忘记密码
    private String username, password;
    private ProgressDialog mProgressDialog;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.NET_DATA_SUCCESS:
                    try {
                        JSONObject jo = new JSONObject(msg.obj.toString());
                        Gson gson=new Gson();
                        UserBean userBean=gson.fromJson(jo.toString(),UserBean.class);
                        boolean isSave=userBean.saveInfo(AppContext.getInstance());
                        MyLog.e("--","save userInfo "+isSave);
                        CacheTools.setUserData("token", jo.getString("token"));
                        CacheTools.setUserData("user", jo.getString("telephone"));
                        CacheTools.setUserData("userId", String.valueOf(jo.getInt("id")));
                        CacheTools.setUserData("password", jo.getString("password"));
                        CacheTools.setUserData("telephone", jo.getString("telephone"));
                        CacheTools.setUserData("loginpassword", jo.getString("password"));
                        CacheTools.setUserData("role", jo.getString("role"));
                        CacheTools.setUserData("businessId", jo.getString("userBid"));
                        CacheTools.setUserLoginData("user", jo.getString("telephone"));
                        CacheTools.setUserLoginData("password", jo.getString("password"));
                        CacheTools.setUserData("cacheDir", getFilesDir() + "");// 对文件的缓存路径进行缓存
                        CacheTools.setUserData("daiLitype", jo.getString("daiLitype"));// 缓存代理类型1：省代理 2：市代理
                        CacheTools.setUserData("nickName", jo.getString("name"));
                        CacheTools.setUserData("loginProvince", jo.getString("province"));
                        CacheTools.setUserData("loginCity", jo.getString("city"));
                        CacheTools.setUserData("loginDistrict", jo.getString("district"));
                        CacheTools.setUserData("provinceId", jo.getString("provinceId"));
                        CacheTools.setUserData("cityId", jo.getString("cityId"));
                        CacheTools.setUserData("districtId", jo.getString("districtId"));
                        CacheTools.setUserData("url", ServerUtils.getImgServer(jo.getString("url")));
                        CacheTools.setUserData("realName", jo.getString("realName"));
                        CacheTools.setUserData("sex", jo.getInt("sex"));
                        CacheTools.setUserData("age",jo.getString("age"));
                        unregisterJPush();// 注销
                        initUserJpush();
                        CacheTools.setUserData("isNotification", Constant.JPUSH_STATUS.JPUSH_OPEN);
                        loginRong();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case Constant.NET_DATA_FAIL:
                    mProgressDialog.dismiss();
                    ToastUtil.toastShow(LoginActivity.this, ((DataError) msg.obj).getErrorMessage());
                    break;
                default:
                    mProgressDialog.dismiss();
                    ToastUtil.toastShow(LoginActivity.this, "网络连接异常，请稍后再试...");
                    break;

            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_ac);
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        mForgetPasswordTV.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
        mForgetPasswordTV.setTextColor(getResources().getColor(R.color.new_blue));
        mUserName.setFocusable(true);
        mUserName.setFocusableInTouchMode(true);
        mUserName.requestFocus();
    }

    private void initData() {
        if (TextUtils.isEmpty(CacheTools.getUserLoginData("user"))) {
            mUserName.setHint(R.string.login_please_input_username);
        } else {
            mUserName.setText(CacheTools.getUserLoginData("user"));
        }
        mPassword.setHint(R.string.login_please_input_password);
        CacheTools.setUserData("version", VersionUtil.getVersion(LoginActivity.this));
    }

    private void initEvent() {
        mUserName.addTextChangedListener(textWatcher);
        mLoginBtn.setOnClickListener(this);
        mBackIV.setOnClickListener(this);
        mForgetPasswordTV.setOnClickListener(this);
    }

    TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
        }

        @Override
        public void afterTextChanged(Editable arg0) {
            if (mUserName.length() < 11) {
                mPassword.setText("");
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishActivity();
            return true;
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(LoginActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(LoginActivity.this);
    }

    /**
     * 登录融云
     */
    private void loginRong() {
        Log.e("---","userId = "+CacheTools.getUserData("userId")+" name = "+CacheTools.getUserData("nickName") +" pro= "+CacheTools.getUserData("url"));
        String url = NetworkUtil.GET_RONG_TOKEN + CacheTools.getUserData("userId");
        Log.e("------","url = "+url);
        AjaxParams params = new AjaxParams();
        params.put("status", "1");//0: token失效 1：token未失效
        params.put("name", CacheTools.getUserData("nickName"));
        params.put("portraitUri", CacheTools.getUserData("url"));
        new NetworkUtil().postMulti(url, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                Log.e("token", s);
                try {
                    JSONObject jo = new JSONObject(s);
                    if(ErrorCodeUtil.doErrorCode(LoginActivity.this,jo.getInt("code"),jo.getString("message"))==false){
                        return;
                    }
                    if (jo.getInt("code") == 0) {
                        Gson gson = new Gson();
                        TokenBean tokenBean = gson.fromJson(s, TokenBean.class);
                        String token = tokenBean.getData().getToken();
                        CacheTools.setUserData("rongToken", token);
                        CacheTools.setUserData("rongUserId", tokenBean.getData().getUserId() + "");
                        CacheTools.setUserData("rongName", tokenBean.getData().getName());
                        CacheTools.setUserData("rongPortraitUri", tokenBean.getData().getPortraitUri());
                        connect(token, getApplicationContext());
                    } else {
                        mProgressDialog.dismiss();
                        ToastUtil.toastShow(LoginActivity.this, jo.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                Log.e("onFailure", "----------" + strMsg);
                Toast.makeText(LoginActivity.this, "网络连接失败，请检查网络是否连接", Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }


        });

    }

    /**
     * 建立与融云服务器的连接
     *
     * @param token
     */
    public void connect(String token, Context context) {
        if (context.getApplicationInfo().packageName.equals(AppContext.getCurProcessName(context.getApplicationContext()))) {
            /**
             * IMLib SDK调用第二步,建立与服务器的连接
             */
            RongApi.connect(context, token, new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {
                    Log.e("onTokenIncorrect", "Token失效");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "网络连接失败，请检查网络是否连接", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onSuccess(String s) {
                    Log.e("onSuccess", "rong连接成功");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(LoginActivity.this, MainActivityEr.class);
                            startActivity(intent);
                            finishActivity();
                        }
                    });
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.e("onError", "rong连接失败");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "网络连接失败，请检查网络是否连接", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    /**
     * 获取token
     */
    private void Group() {
        NetworkUtil.get(NetworkUtil.QI_NIU_TOKEN, null, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    CacheTools.setUserData("qiniutoken", jsonObject.getString("data"));
                    mProgressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                mProgressDialog.dismiss();
                MyLog.e("---", "获取七牛token失败");
                finishActivity();
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.login_btn:
                loginApp();
                break;
            case R.id.forget_password_tv:
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
                finishActivity();
                break;
            case R.id.loginback_iv:
                finishActivity();
                break;
        }

    }

    /**
     * 登录
     */
    private void loginApp() {
        StatService.onEvent(LoginActivity.this, "clickLoginBtn", "点击登录按钮");
        username = mUserName.getText().toString().trim();
        password = MD5Util.getMD5(mPassword.getText().toString());
        if (verifyInfo(username, password)) {
            AjaxParams params = new AjaxParams();
            params.put("version", VersionUtil.getVersion(LoginActivity.this));
            params.put("telephone", username);
            params.put("password", password);
            params.put("deviceCode", Tools.getPhoneIM(LoginActivity.this));
            if(CacheTools.getUserData("version")==null){
                MyLog.e("YUY","版本号为空");
                CacheTools.setUserData("version","3.3.0");
            }
            Log.e("--", "登录信息 = "+VersionUtil.getVersion(LoginActivity.this)+" "+username+" "+password+" "+Tools.getPhoneIM(LoginActivity.this));
            requestNet(mHandler, params, NetworkUtil.LOGIN_URL, false, 1);

            mProgressDialog = new ProgressDialog(LoginActivity.this);
            mProgressDialog.setMessage("正在登录中...");
            mProgressDialog.setCancelable(false);
            try {
                mProgressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 验证登录信息
     *
     * @param telePhone 电话号码
     * @param password  密码
     * @return
     */
    public boolean verifyInfo(String telePhone, String password) {
        if (TextUtils.isEmpty(telePhone)) {//电话号码为空
            ToastUtil.toastShow(LoginActivity.this, "请输入电话号码");
        } else if (telePhone.length() < 11) {
            ToastUtil.toastShow(LoginActivity.this, "电话号码输入不正确");
        } else if (TextUtils.isEmpty(password)) {//判断密码为空
            ToastUtil.toastShow(LoginActivity.this, "密码不能为空");
        } else if (password.length() < 6) {//密码长度判断
            ToastUtil.toastShow(LoginActivity.this, "密码长度不正确");
        } else if (IsNetworkUtil.isNetworkAvailable(LoginActivity.this) == false) {
            ToastUtil.toastShow(LoginActivity.this, "请检查网络是否连接");
        } else {
            return true;
        }
        return false;
    }


}
