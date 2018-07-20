package com.chewuwuyou.rong.utils;

import com.chewuwuyou.rong.bean.RecallMsgBean;

import de.greenrobot.event.EventBus;
import io.rong.imlib.RongIMClient;
import io.rong.message.RecallNotificationMessage;

/**
 * Created by xxy on 2016/9/10 0010.
 */
public class MyRecallMessageListener implements RongIMClient.RecallMessageListener {
    @Override
    public void onMessageRecalled(int msgId, RecallNotificationMessage recallNotificationMessage) {
        EventBus.getDefault().post(new RecallMsgBean(msgId,recallNotificationMessage));
    }
}
