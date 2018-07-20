package com.chewuwuyou.rong.view;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.rong.bean.TokenBean;
import com.chewuwuyou.rong.bean.UserBean;
import com.chewuwuyou.rong.utils.Constant;
import com.chewuwuyou.rong.utils.RongApi;
import com.google.gson.Gson;

import net.tsz.afinal.http.AjaxCallBack;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by xxy on 2016/9/6 0006.
 */
public class RongServerChatActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.em_activity_chat);
        //TODO 调用APP_PUBLIC_SERVICE 会话列表不显示客服聊天记录参考书签
        RongServerChatMsgFragment fragment = RongServerChatMsgFragment.getInstance(Conversation.ConversationType.CUSTOMER_SERVICE, Constant.SERVER_ID);
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
    }
}
