package com.chewuwuyou.app.transition_model;

import com.chewuwuyou.app.transition_entity.RedInit;
import com.chewuwuyou.app.transition_entity.RedPacketDetailEntity;
import com.chewuwuyou.app.transition_entity.ResponseBean;
import com.chewuwuyou.app.transition_entity.ResponseNBean;
import com.chewuwuyou.app.transition_entity.SendRedBean;
import com.chewuwuyou.app.transition_entity.UserBean;
import com.chewuwuyou.app.transition_entity.ResponseNBean;
import com.chewuwuyou.app.transition_service.RedPacketService;
import com.chewuwuyou.app.transition_utils.HttpBase;

import java.util.Observable;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by CLOUD on 2016/10/13.
 */
public class RedPacketModel extends BaseModel {


    /**
     * @param userId      userid
     * @param redPacketId 红包id
     * @param pageNum     每页的数量
     * @param page        页数
     * @return
     */
    public rx.Observable<ResponseNBean<RedPacketDetailEntity>> getRedDetail(String userId, String redPacketId, int pageNum, int page) {


        return HttpBase.getInstance().createApi(RedPacketService.class).redPacketDetail(userId, redPacketId, pageNum, page);

    }

    /**
     * 发送红包
     *
     * @param sendRedBean
     * @return
     */
    public rx.Observable<ResponseBean<RedPacketDetailEntity>> sendPacket(SendRedBean sendRedBean) {
        return HttpBase.getInstance().createApi(RedPacketService.class).sendRedPacket(sendRedBean.getUserId(), sendRedBean.getType(), sendRedBean.getMoney(), sendRedBean.getFriendAccid(), sendRedBean.getSize(), sendRedBean.getLeaveMessage(), sendRedBean.getPayType());

    }

    public rx.Observable<ResponseNBean<RedInit>> sendPacketGroup(SendRedBean sendRedBean) {
/*
     return    new Retrofit.Builder().baseUrl("http://192.168.8.48:99/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RedPacketService.class)
                .sendRedPacket(sendRedBean.getUserId(), sendRedBean.getType(), sendRedBean.getMoney(), sendRedBean.getFriendAccid(), sendRedBean.getSize(), sendRedBean.getLeaveMessage(), sendRedBean.getPayType(),sendRedBean.getUnitPrice());*/
        return HttpBase.getInstance().createApi(RedPacketService.class).sendRedPacket(sendRedBean.getUserId(), sendRedBean.getType(), sendRedBean.getMoney(), sendRedBean.getFriendAccid(), sendRedBean.getSize(), sendRedBean.getLeaveMessage(), sendRedBean.getPayType(), sendRedBean.getUnitPrice());

    }


}




