package com.chewuwuyou.rong.bean;

import io.rong.imlib.model.Conversation;

/**
 * Created by Administrator on 2016/9/10 0010.
 */
public class ClearMessagesBean {
    private Conversation.ConversationType conversationType;
    private String targetId;

    public ClearMessagesBean() {
    }

    public ClearMessagesBean(Conversation.ConversationType conversationType, String targetId) {
        this.conversationType = conversationType;
        this.targetId = targetId;
    }

    public Conversation.ConversationType getConversationType() {
        return conversationType;
    }

    public void setConversationType(Conversation.ConversationType conversationType) {
        this.conversationType = conversationType;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }
}
