package com.chewuwuyou.app.transition_service;

import com.chewuwuyou.app.transition_entity.RateFee;
import com.chewuwuyou.app.transition_entity.TransferConfig;
import com.chewuwuyou.rong.bean.NewBaseNetworkBean;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by xxy on 2016/10/19 0019.
 */

public interface TransferService {

    /**
     * 获取某人当日已转金额
     *
     * @param accid
     * @return
     */
    @FormUrlEncoded
    @POST("api/finance/transfer/v1/getTodayQuota")
    Observable<NewBaseNetworkBean<String>> getTodayQuota(@Field("accidOut") String accid);

    /**
     * 计算转账手续费
     *
     * @param amount
     * @return
     */
    @FormUrlEncoded
    @POST("api/finance/transfer/v1/getTransferRateFee")
    Observable<NewBaseNetworkBean<RateFee>> getTransferRateFee(@Field("amount") String amount);

    /**
     * 获取转账规则
     *
     * @return
     */
    @POST("api/finance/transfer/v1/getLatestTransferRule")
    Observable<NewBaseNetworkBean<TransferConfig>> getLatestTransferRule();

    /**
     * 获取是否设置支付密码
     *
     * @return
     */
    @POST("api/finance/payPasswordTest")
    Observable<String> payPasswordTest();
}
