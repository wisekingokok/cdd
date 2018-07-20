package com.chewuwuyou.app.transition_service;

import com.chewuwuyou.app.transition_entity.RedPacketParam;
import com.chewuwuyou.app.transition_entity.ResponseBean;
import com.chewuwuyou.app.transition_entity.ResponseNBean;
import com.chewuwuyou.app.transition_entity.SimpleEntity;
import com.chewuwuyou.app.transition_entity.TransferAccountDetail;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface SimpleService {

    @FormUrlEncoded
    @POST("api/get")
    Observable<SimpleEntity> get(
            @Header("session") String session,
            @Field("id") String id);

    @FormUrlEncoded
    @POST("geocoding")
    Observable<SimpleEntity> get(@Field("a") String a);

    @POST("api/finance/redpacket/v1/getRedPacketParam")
    Observable<ResponseNBean<RedPacketParam>> getRedPacketParam(@Query("userId") String a);

    @POST("api/finance/redpacket/v1/getRedPacketParam")
    Observable<ResponseBean<TransferAccountDetail>> getTransferDetail(@Query("userId") String a);
}