package com.chewuwuyou.app.transition_model;

import com.chewuwuyou.app.transition_entity.PayReusltBean;
import com.chewuwuyou.app.transition_entity.RedInit;
import com.chewuwuyou.app.transition_entity.ResponseNBean;
import com.chewuwuyou.app.transition_entity.TransferResult;
import com.chewuwuyou.app.transition_service.RedPacketService;
import com.chewuwuyou.app.transition_utils.HttpBase;

import retrofit2.http.Field;
import rx.Observable;

/**
 * Created by yuyong on 16/10/19.
 */

public class PayModel extends BaseModel {
    /**
     * 确认发送红包
     *
     * @param userId
     * @param redPacketId
     * @param pwd
     */
    public Observable<PayReusltBean> sendRedPacket(String userId, String redPacketId, String pwd) {
        return HttpBase.getInstance().createApi(RedPacketService.class).sendRedPacket(userId, redPacketId, pwd);
    }

    /**
     * 转账
     * @param amout
     * @param payPass
     * @param meAccId
     * @param otherAccId
     * @param leaveMessage
     * @return
     */
    public Observable<ResponseNBean<String>> transfer(String amout, String payPass, String meAccId, String otherAccId, String leaveMessage) {
       return HttpBase.getInstance().createApi(RedPacketService.class).transfer(amout, payPass, meAccId, otherAccId, leaveMessage);
    }


}
