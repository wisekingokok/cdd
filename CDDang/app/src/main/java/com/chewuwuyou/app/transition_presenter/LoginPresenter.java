package com.chewuwuyou.app.transition_presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.chewuwuyou.app.AppContext;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.transition_constant.Constants;
import com.chewuwuyou.app.transition_entity.ResponseBean;
import com.chewuwuyou.app.transition_entity.RongBean;
import com.chewuwuyou.app.transition_entity.UserBean;
import com.chewuwuyou.app.transition_exception.CustomException;
import com.chewuwuyou.app.transition_model.LoginModel;
import com.chewuwuyou.app.transition_view.activity.LoginActivity;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.IsNetworkUtil;
import com.chewuwuyou.app.utils.MD5Util;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.ServerUtils;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.rong.utils.RongApi;

import io.rong.imlib.RongIMClient;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by yuyong on 16/10/13.
 */

public class LoginPresenter extends BasePresenter {
    private LoginActivity activity;
    private LoginModel loginModel;

    public LoginPresenter(LoginActivity activity) {
        super(activity);
        this.activity = activity;
        loginModel = new LoginModel();
    }

    /**
     * 登录
     *
     * @param version
     * @param telephone
     * @param password
     * @param deviceCode
     */
    public void login(String version, String telephone, String password, String deviceCode) {
        if (verifyInfo(telephone, password)) {
            rx.Observable<ResponseBean<UserBean>> observable = loginModel.loginApp(version, telephone, MD5Util.getMD5(password), deviceCode);
            Action1<ResponseBean<UserBean>> action = new Action1<ResponseBean<UserBean>>() {
                @Override
                public void call(ResponseBean<UserBean> userBeanResponseBean) {
                    if (userBeanResponseBean.getData().saveInfo(AppContext.getInstance())) {
                        UserBean userBean = UserBean.getInstall(activity);
                        if (userBean != null) {
                            loginRong(userBean.getId(), Constants.RONG_TOKEN_NOT_DISABLED, userBean.getName(), userBean.getUrl());
                            saveUserInfo(userBean);
                        }
                    }
                }
            };
            observable.compose(this.<ResponseBean<UserBean>>applySchedulers()).subscribe(defaultSubscriber(action));
        }
    }

    /**
     * 登录融云
     *
     * @param userId
     * @param rongTokenStatus
     * @param nickName
     * @param headUrl
     */
    public void loginRong(String userId, String rongTokenStatus, String nickName, String headUrl) {
        rx.Observable<RongBean> observable = loginModel.loginRong(userId, rongTokenStatus, nickName, headUrl);
        observable.compose(this.<RongBean>applySchedulers()).subscribe(defaultSubscriber(new Action1<RongBean>() {
            @Override
            public void call(RongBean rongBean) {
                //TODO 缓存融云登录信息为实体
                CacheTools.setUserData("rongToken", rongBean.getData().getToken());
                CacheTools.setUserData("rongUserId", String.valueOf(rongBean.getData().getUserId()));
                CacheTools.setUserData("rongName", rongBean.getData().getNickname());
                CacheTools.setUserData("rongPortraitUri", rongBean.getData().getPortraitUri());
                MyLog.e("YUY", "----" + rongBean.getData().getToken());
                connect(rongBean.getData().getToken(), activity);
            }
        }, null, new Func1<CustomException, Boolean>() {
            @Override
            public Boolean call(CustomException e) {
                return true;
            }
        }));
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
                    Toast.makeText(activity, "网络连接失败，请检查网络是否连接", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(String s) {
                    Log.e("onSuccess", "rong连接成功");
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activity.intoFirstPage();
                        }
                    });

                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.e("onError", "rong连接失败");
                    Toast.makeText(activity, "网络连接失败，请检查网络是否连接", Toast.LENGTH_SHORT).show();
                }
            });
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
            ToastUtil.showToast(activity, R.string.please_input_telephone);
        } else if (telePhone.length() < 11) {
            ToastUtil.showToast(activity, R.string.telephone_input_error);
        } else if (TextUtils.isEmpty(password)) {//判断密码为空
            ToastUtil.showToast(activity, R.string.password_not_empty);
        } else if (password.length() < 6) {//密码长度判断
            ToastUtil.showToast(activity, R.string.pwd_length_error);
        } else if (IsNetworkUtil.isNetworkAvailable(activity) == false) {
            ToastUtil.showToast(activity, R.string.internet_error);
        } else {
            return true;
        }
        return false;
    }

    /**
     * 兼容之前的缓存
     *
     * @param userBean
     */
    public void saveUserInfo(UserBean userBean) {
        boolean isSave = userBean.saveInfo(AppContext.getInstance());
        MyLog.e("--", "save userInfo " + isSave);
        CacheTools.setUserData("token", userBean.getToken());
        CacheTools.setUserData("user", userBean.getTelephone());
        CacheTools.setUserData("userId", userBean.getId());
        CacheTools.setUserData("password", userBean.getPassword());
        CacheTools.setUserData("telephone", userBean.getTelephone());
        CacheTools.setUserData("loginpassword", userBean.getPassword());
        CacheTools.setUserData("role", String.valueOf(userBean.getRole()));
        CacheTools.setUserData("businessId", userBean.getUserBid());
        CacheTools.setUserLoginData("user", userBean.getTelephone());
        CacheTools.setUserLoginData("password", userBean.getPassword());
        CacheTools.setUserData("daiLitype", String.valueOf(userBean.getDaiLitype()));// 缓存代理类型1：省代理 2：市代理
        CacheTools.setUserData("nickName", userBean.getName());
        CacheTools.setUserData("loginProvince", userBean.getProvince());
        CacheTools.setUserData("loginCity", userBean.getCity());
        CacheTools.setUserData("loginDistrict", userBean.getDistrict());
        CacheTools.setUserData("provinceId", userBean.getProvinceId());
        CacheTools.setUserData("cityId", userBean.getCityId());
        CacheTools.setUserData("districtId", userBean.getDistrictId());
        CacheTools.setUserData("url", ServerUtils.getImgServer(userBean.getUrl()));
        CacheTools.setUserData("realName", userBean.getRealName());
        CacheTools.setUserData("sex", userBean.getSex());
        CacheTools.setUserData("age", userBean.getAge());
        unregisterJPush();// 注销
        initUserJpush();
        CacheTools.setUserData("isNotification", Constant.JPUSH_STATUS.JPUSH_OPEN);


    }
}


