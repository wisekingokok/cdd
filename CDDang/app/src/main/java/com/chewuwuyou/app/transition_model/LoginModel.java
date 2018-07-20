package com.chewuwuyou.app.transition_model;

import com.chewuwuyou.app.transition_entity.ResponseBean;
import com.chewuwuyou.app.transition_entity.RongBean;
import com.chewuwuyou.app.transition_entity.UserBean;
import com.chewuwuyou.app.transition_service.UserService;
import com.chewuwuyou.app.transition_utils.HttpBase;

import retrofit2.Retrofit;

/**
 * Created by yuyong on 16/10/13.
 * 登录model
 */

public class LoginModel extends BaseModel {
    /**
     * @param version
     * @param telephone
     * @param password
     * @param deviceCode
     * @return
     */
    public rx.Observable<ResponseBean<UserBean>> loginApp(String version, String telephone, String password, String deviceCode) {
//        Retrofit retrofit = getOldRetrofit();
//        return retrofit.create(UserService.class).login(version, telephone, password, deviceCode);
        return HttpBase.getOldInstance().createApi(UserService.class).login(version, telephone, password, deviceCode);
    }

    /**
     * @param userId
     * @param status      0:token失效  1:token未失效
     * @param name
     * @param portraitUri
     * @return
     */
    public rx.Observable<RongBean> loginRong(String userId, String status, String name, String portraitUri) {
//        Retrofit retrofit = getDefaultRetrofit();
//        return retrofit.create(UserService.class).loginRong(userId, status, name, portraitUri);
        return HttpBase.getInstance().createApi(UserService.class).loginRong(userId, status, name, portraitUri);
    }

}
