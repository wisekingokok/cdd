package com.chewuwuyou.app.transition_model;

import android.content.Context;

import com.chewuwuyou.app.R;
import com.chewuwuyou.rong.utils.RongApi;

import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by xxy on 2016/10/17 0017.
 */

public class RongMsgModel extends BaseModel {

    public void onRefresh(Context context, RongIMClient.ResultCallback<List<Conversation>> resultCallback) {
        String[] remindUserIdArr = context.getResources().getStringArray(R.array.no_show_array);
        for (int i = 0; i < remindUserIdArr.length; i++) {
            RongApi.removeConversation(Conversation.ConversationType.PRIVATE, remindUserIdArr[i], new RongIMClient.ResultCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean aBoolean) {
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                }
            });
        }
        List<Conversation.ConversationType> conversationTypes = new ArrayList<Conversation.ConversationType>();
        conversationTypes.add(Conversation.ConversationType.CUSTOMER_SERVICE);
        conversationTypes.add(Conversation.ConversationType.GROUP);
        conversationTypes.add(Conversation.ConversationType.PRIVATE);
        RongApi.getConversationList(resultCallback, conversationTypes);
    }
}
