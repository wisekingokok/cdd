package com.chewuwuyou.app.transition_model;

import android.util.Log;

import com.chewuwuyou.app.transition_entity.RedInit;
import com.chewuwuyou.app.transition_entity.RedPacketParam;
import com.chewuwuyou.app.transition_entity.ResponseBean;
import com.chewuwuyou.app.transition_entity.ResponseNBean;
import com.chewuwuyou.app.transition_entity.SendRedBean;
import com.chewuwuyou.app.transition_service.RedPacketService;
import com.chewuwuyou.app.transition_service.SimpleService;
import com.chewuwuyou.app.transition_utils.HttpBase;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observable;

/**
 * Created by ZQ on 2016/10/17.
 */

public class GroupRedPacketSendMode extends BaseModel {

    public Observable<ResponseNBean<RedPacketParam>> getRedPacketParam(String userId){
        return HttpBase.getInstance().createApi(SimpleService.class).getRedPacketParam(userId);
    }
   /* public Observable<ResponseNBean<RedInit>> sendredpacket( SendRedBean sendRedBean){
        Log.e("tag",sendRedBean.toString());
        return    new Retrofit.Builder().baseUrl("http://192.168.8.48:99/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RedPacketService.class)
                .sendRedPacket(sendRedBean.getUserId(), sendRedBean.getType(), sendRedBean.getMoney(), sendRedBean.getFriendAccid(), sendRedBean.getSize(), sendRedBean.getLeaveMessage(), sendRedBean.getPayType(),sendRedBean.getUnitPrice());
//        return HttpBase.getInstance().createApi(RedPacketService.class).sendRedPacket(userId);
    }*/
}
