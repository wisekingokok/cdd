package com.chewuwuyou.app.eventbus;

/**
 * Created by CLOUD on 2016/9/19.删除好友更新消息
 */

public class MessageEvent {

    public final String friendID;
    public final String groupId;

    public MessageEvent(String friendID, String groupId) {
        this.friendID = friendID;
        this.groupId = groupId;
    }
}
