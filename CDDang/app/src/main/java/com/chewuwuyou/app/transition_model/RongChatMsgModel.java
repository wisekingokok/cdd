package com.chewuwuyou.app.transition_model;

import com.chewuwuyou.app.transition_entity.RedPacketDetailEntity;
import com.chewuwuyou.app.transition_service.IMGroupService;
import com.chewuwuyou.app.transition_service.RedBService;
import com.chewuwuyou.app.transition_service.UserService;
import com.chewuwuyou.rong.bean.GroupBean;
import com.chewuwuyou.rong.bean.NewBaseNetworkBean;
import com.chewuwuyou.rong.bean.RongUserBean;
import com.chewuwuyou.rong.utils.RongApi;

import java.util.List;

import io.rong.imlib.ICustomServiceListener;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.CSCustomServiceInfo;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import rx.Observable;


/**
 * Created by xxy on 2016/10/17 0017.
 */

public class RongChatMsgModel extends BaseModel {

    /**
     * 取本地消息
     *
     * @param isPull
     * @param addSize
     */
    public void getLatestMessages(boolean isPull, int addSize, int oldCount, Conversation.ConversationType conversationType, String targetId, RongIMClient.ResultCallback<List<Message>> resultCallback) {
        int count = oldCount + (isPull ? 10 : 0) + addSize;
        RongApi.getLatestMessages(conversationType, targetId, count < 10 ? 10 : count, resultCallback);
    }

    public Observable<NewBaseNetworkBean<GroupBean>> getGroupById(String targetId) {
        return getDefaultRetrofit().create(IMGroupService.class).getGroupById(targetId);
    }

    public Observable<NewBaseNetworkBean<Integer>> isInGroup(String targetId, String sendId) {
        return getDefaultRetrofit().create(IMGroupService.class).isInGroup(targetId, sendId);
    }

    public Observable<NewBaseNetworkBean<List<RongUserBean>>> getUserById(String targetId) {
        return getOldRetrofit().create(UserService.class).getUserById(targetId);
    }

    public void startCustomService(String targetId, CSCustomServiceInfo csInfo, ICustomServiceListener iCustomServiceListener) {
        RongIMClient.getInstance().startCustomService(targetId, iCustomServiceListener, csInfo);
    }

    public Observable<NewBaseNetworkBean<RedPacketDetailEntity>> whetherGrabRedPacket(String Userid, String redBid) {
        return getDefaultRetrofit().create(RedBService.class).whetherGrabRedPacket(Userid, redBid);
    }
}
