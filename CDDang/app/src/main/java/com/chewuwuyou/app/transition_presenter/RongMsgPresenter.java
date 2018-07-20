package com.chewuwuyou.app.transition_presenter;

import android.content.Context;

import com.chewuwuyou.app.transition_model.RongMsgModel;
import com.chewuwuyou.app.transition_view.fragment.RongMessageFragment;

import java.util.List;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by xxy on 2016/10/17 0017.
 */

public class RongMsgPresenter extends BasePresenter {
    RongMsgModel rongMsgModel = new RongMsgModel();
    RongMessageFragment rongMessageFragment;

    public RongMsgPresenter(Context context, RongMessageFragment rongMessageFragment) {
        super(context);
        this.rongMessageFragment = rongMessageFragment;
    }

    public void onRefresh() {
        rongMsgModel.onRefresh(context, new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                rongMessageFragment.setData(conversations);
                if (conversations == null || conversations.size() <= 0)
                    rongMessageFragment.showNullView();
                else
                    rongMessageFragment.disNullView();
                rongMessageFragment.setRefreshing();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                rongMessageFragment.setRefreshing();
            }
        });
    }
}
