package com.chewuwuyou.rong.bean;

import io.rong.message.RecallNotificationMessage;

/**
 * Created by xxy on 2016/9/10 0010.
 */
public class RecallMsgBean {
    private int msgId;
    private RecallNotificationMessage recallNotificationMessage;

    public RecallMsgBean() {
    }

    public RecallMsgBean(int msgId, RecallNotificationMessage recallNotificationMessage) {
        this.msgId = msgId;
        this.recallNotificationMessage = recallNotificationMessage;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public RecallNotificationMessage getRecallNotificationMessage() {
        return recallNotificationMessage;
    }

    public void setRecallNotificationMessage(RecallNotificationMessage recallNotificationMessage) {
        this.recallNotificationMessage = recallNotificationMessage;
    }
}
