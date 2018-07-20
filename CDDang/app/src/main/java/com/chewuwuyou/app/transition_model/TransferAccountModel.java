package com.chewuwuyou.app.transition_model;

import com.chewuwuyou.app.transition_entity.RateFee;
import com.chewuwuyou.app.transition_entity.TransferConfig;
import com.chewuwuyou.app.transition_service.TransferService;
import com.chewuwuyou.app.transition_service.UserService;
import com.chewuwuyou.rong.bean.NewBaseNetworkBean;
import com.chewuwuyou.rong.bean.RongUserBean;

import java.util.List;

import rx.Observable;

/**
 * Created by xxy on 2016/10/18 0018.
 */

public class TransferAccountModel extends BaseModel {

    public Observable<NewBaseNetworkBean<List<RongUserBean>>> getUserById(String userId) {
        return getOldRetrofit().create(UserService.class).getUserById(userId);
    }

    public Observable<NewBaseNetworkBean<String>> getTodayQuota(String accid) {
        return getDefaultRetrofit().create(TransferService.class).getTodayQuota(accid);
    }

    public Observable<NewBaseNetworkBean<TransferConfig>> getLatestTransferRule() {
        return getDefaultRetrofit().create(TransferService.class).getLatestTransferRule();
    }

    public Observable<NewBaseNetworkBean<RateFee>> getTransferRateFee(String amount) {
        return getDefaultRetrofit().create(TransferService.class).getTransferRateFee(amount);
    }
}
