package com.chewuwuyou.app.transition_service;

import com.chewuwuyou.app.transition_entity.RedPacketDetailEntity;
import com.chewuwuyou.rong.bean.NewBaseNetworkBean;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by xxy on 2016/10/18 0018.
 */

public interface RedBService {
    @FormUrlEncoded
    @POST("api/finance/redpacket/v1/whetherGrabRedPacket")
    Observable<NewBaseNetworkBean<RedPacketDetailEntity>> whetherGrabRedPacket(@Field("userId") String userId, @Field("redPacketId") String redPacketId);
}
