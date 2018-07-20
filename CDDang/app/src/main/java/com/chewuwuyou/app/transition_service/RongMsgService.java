package com.chewuwuyou.app.transition_service;

import android.content.Context;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.transition_entity.OpenRed;
import com.chewuwuyou.app.transition_entity.RedPacketDetailEntity;
import com.chewuwuyou.app.transition_entity.RedPacketParam;
import com.chewuwuyou.app.transition_entity.ResponseNBean;
import com.chewuwuyou.app.transition_model.BaseModel;
import com.chewuwuyou.rong.utils.RongApi;

import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by xxy on 2016/10/17 0017.
 */

public interface RongMsgService{

    /**
     * 抢红包
     */
    @FormUrlEncoded
    @POST("api/finance/redpacket/v1/grabRedPacket")
    Observable<ResponseNBean<RedPacketDetailEntity>> getOpenRed(@Field("userId") String userId, @Field("redPacketId") String redPacketId);

}
