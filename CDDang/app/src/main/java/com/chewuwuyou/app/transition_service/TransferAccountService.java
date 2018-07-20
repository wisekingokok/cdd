package com.chewuwuyou.app.transition_service;

import com.chewuwuyou.app.transition_entity.ResponseNBean;
import com.chewuwuyou.app.transition_entity.TransferAccountDetail;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ZQ on 2016/10/19.
 */

public interface TransferAccountService {
    @POST("api/finance/transfer/v1/getTransferDetail")
    Observable<ResponseNBean<TransferAccountDetail>> getTransferDetail(@Query("transferId") String a);
}
