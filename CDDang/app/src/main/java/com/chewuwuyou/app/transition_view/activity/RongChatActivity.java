package com.chewuwuyou.app.transition_view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.transition_view.fragment.RongChatMsgFragment;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * 聊天
 * Created by xxy on 2016/9/12 0012.
 */
public class RongChatActivity extends FragmentActivity {
    private RongChatMsgFragment myChatFragment;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.em_activity_chat);
//        switch (RongApi.getCurrentConnectionStatus()) {
//            case CONNECTED://连接成功。
//                buildFragment();
//                break;
//            case CONNECTING://连接中
//                buildFragment();
//                break;
//            case DISCONNECTED://断开连接
//                RongApi.reconnect(getConnectCallback());
//                break;
//            case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线。
//                Toast.makeText(RongChatActivity.this, "帐号已在其他手机登录", Toast.LENGTH_SHORT).show();
//                break;
//            case NETWORK_UNAVAILABLE://网络不可用。
//                Toast.makeText(RongChatActivity.this, "网络不可用,请检查网络连接", Toast.LENGTH_SHORT).show();
//                break;
//            case SERVER_INVALID://服务器异常或无法连接。
//                Toast.makeText(RongChatActivity.this, "IM服务异常", Toast.LENGTH_SHORT).show();
//                break;
//            case TOKEN_INCORRECT://Token 不正确。
//                Toast.makeText(RongChatActivity.this, "网络不可用,请检查网络连接", Toast.LENGTH_SHORT).show();
//                break;
//        }
        buildFragment();
    }

    private RongIMClient.ConnectCallback getConnectCallback() {
        return new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                Log.e("RongChatMsgFragment", "token失效");
            }

            @Override
            public void onSuccess(String s) {
                buildFragment();
                Log.e("RongChatMsgFragment", "重连成功");
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e("RongChatMsgFragment", "重连失败");
            }
        };
    }

    private void buildFragment() {
        Intent intent = getIntent();
        myChatFragment = RongChatMsgFragment.getInstance((Conversation.ConversationType) intent.getSerializableExtra(RongChatMsgFragment.KEY_TYPE), intent.getStringExtra(RongChatMsgFragment.KEY_TARGET));
        getSupportFragmentManager().beginTransaction().add(R.id.container, myChatFragment).commit();
    }

    @Override
    public void onBackPressed() {
        if (myChatFragment != null && !myChatFragment.onBackPressed()) return;
        super.onBackPressed();
    }
}
