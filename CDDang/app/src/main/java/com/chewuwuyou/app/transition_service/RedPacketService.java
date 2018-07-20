package com.chewuwuyou.app.transition_service;

import com.chewuwuyou.app.transition_entity.PayReusltBean;
import com.chewuwuyou.app.transition_entity.RedInit;
import com.chewuwuyou.app.transition_entity.RedPacketDetailEntity;
import com.chewuwuyou.app.transition_entity.ResponseBean;
import com.chewuwuyou.app.transition_entity.ResponseNBean;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by CLOUD on 2016/10/14.
 */

public interface RedPacketService {

    /**
     * @param userId      用户id
     * @param redPacketId 红包Id
     * @param pageNum
     * @param pageSize
     * @return
     */

    @FormUrlEncoded
    @POST("api/finance/redpacket/v1/selectRedpacketInfo")
    Observable<ResponseNBean<RedPacketDetailEntity>> redPacketDetail(@Field("userId") String userId, @Field("redPacketId") String redPacketId,
                                                                     @Field("pageNum") int pageNum, @Field("pageSize") int pageSize);

    /**
     * 确认发红包接口
     *
     * @param userId
     * @param redPacketId
     * @param pwd
     * @return
     */
    @FormUrlEncoded
    @POST("api/finance/redpacket/v1/sendRedPackets")
    Observable<PayReusltBean> sendRedPacket(@Field("userId") String userId, @Field("redPacketId") String redPacketId, @Field("password") String pwd);


    /**
     * 发送单个或群随机红包
     *
     * @param userId
     * @param type
     * @param money
     * @param friendAccid
     * @param size
     * @param leaveMessage
     * @param payType
     * @return
     */
    @FormUrlEncoded
    @POST("api/finance/redpacket/v1/sendRedPacketsInit")
    Observable<ResponseBean<RedPacketDetailEntity>> sendRedPacket(@Field("userId") String userId, @Field("type") String type,
                                                                  @Field("money") String money, @Field("friendAccid") String friendAccid, @Field("size") int size, @Field("leaveMessage") String leaveMessage, @Field("payType") String payType);

    @POST("api/finance/redpacket/v1/sendRedPacketsInit")
    Observable<ResponseNBean<RedInit>> sendRedPacket(@Query("userId") String userId, @Query("type") String type,
                                                     @Query("money") String money, @Query("friendAccid") String friendAccid, @Query("size") int size, @Query("leaveMessage") String leaveMessage, @Query("payType") String payType,
                                                     @Query("unitPrice") String unitPrice);


    /**
     * 转账接口
     *
     * @param amount
     * @param payPass
     * @param meAccId
     * @param otherAccId
     * @param leaveMessage
     * @return
     */
    @FormUrlEncoded
    @POST("api/finance/transfer/v1/transferMoney")
    Observable<ResponseNBean<String>> transfer(@Field("amount") String amount, @Field("payPass") String payPass,
                                               @Field("meAccId") String meAccId, @Field("otherAccId") String otherAccId, @Field("leaveMessage") String leaveMessage);


}
