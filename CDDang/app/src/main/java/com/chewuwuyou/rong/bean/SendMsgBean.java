package com.chewuwuyou.rong.bean;

import io.rong.imlib.model.Message;

/**
 * Created by xxy on 2016/9/17 0017.
 */
public class SendMsgBean {
    /**
     * 刷新几条
     */
    public int reCount;
    public Message message;

    public SendMsgBean() {
    }

    public SendMsgBean(Message message) {
        this.message = message;
    }

    public SendMsgBean(int reCount, Message message) {
        this.reCount = reCount;
        this.message = message;
    }
}
