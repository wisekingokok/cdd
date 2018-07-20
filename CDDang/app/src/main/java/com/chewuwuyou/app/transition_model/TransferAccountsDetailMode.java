package com.chewuwuyou.app.transition_model;

import com.chewuwuyou.app.transition_entity.ResponseBean;
import com.chewuwuyou.app.transition_entity.ResponseNBean;
import com.chewuwuyou.app.transition_entity.SimpleEntity;
import com.chewuwuyou.app.transition_entity.TransferAccountDetail;
import com.chewuwuyou.app.transition_service.SimpleService;
import com.chewuwuyou.app.transition_service.TransferAccountService;
import com.chewuwuyou.app.transition_utils.HttpBase;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by ZQ on 2016/10/10.
 */

public class TransferAccountsDetailMode extends BaseModel {
    public Observable<ResponseNBean<TransferAccountDetail>> getTransferDetail(String transferId) {

        return HttpBase.getInstance().createApi(TransferAccountService.class)
                .getTransferDetail(transferId);
    }
}
