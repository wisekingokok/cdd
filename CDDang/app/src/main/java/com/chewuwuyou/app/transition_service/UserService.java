package com.chewuwuyou.app.transition_service;

import com.chewuwuyou.app.transition_entity.ResponseBean;
import com.chewuwuyou.app.transition_entity.RongBean;
import com.chewuwuyou.app.transition_entity.UserBean;
import com.chewuwuyou.rong.bean.NewBaseNetworkBean;
import com.chewuwuyou.rong.bean.RongUserBean;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by yuyong on 16/10/13.
 */

public interface UserService {

    /**
     * @param version    版本号
     * @param userName   用户名
     * @param password   密码
     * @param deviceCode 手机唯一标识
     * @return
     */
    @FormUrlEncoded
    @POST("api/user/loginApp")
    Observable<ResponseBean<UserBean>> login(@Field("version") String version, @Field("telephone") String userName,
                                             @Field("password") String password, @Field("deviceCode") String deviceCode
    );

    /**
     * @param status   0:token失效  1:token未失效
     * @param nickName 昵称
     * @param headUrl  头像
     * @return
     */
    @FormUrlEncoded
    @POST("api/im/user/v1/getToken/{userId}")
    Observable<RongBean> loginRong(@Path("userId") String userId, @Field("status") String status, @Field("name") String nickName,
                                   @Field("portraitUri") String headUrl
    );

    /**
     * 获取用户信息
     *
     * @param ids
     * @return
     */
    @FormUrlEncoded
    @POST("api/user/getPeopleInfo")
    Observable<NewBaseNetworkBean<List<RongUserBean>>> getUserById(@Field("ids") String ids);
}
