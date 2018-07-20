package com.chewuwuyou.eim.activity.im;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.chewuwuyou.app.AppContext;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.ChatUtils;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.eim.activity.ActivitySupport;
import com.chewuwuyou.eim.comm.Constant;
import com.chewuwuyou.eim.manager.MessageManager;
import com.chewuwuyou.eim.manager.NoticeManager;
import com.chewuwuyou.eim.manager.XmppConnectionManager;
import com.chewuwuyou.eim.model.IMMessage;
import com.chewuwuyou.eim.model.Notice;
import com.chewuwuyou.eim.util.DateUtil;
import com.chewuwuyou.eim.util.StringUtil;

import net.tsz.afinal.http.AjaxParams;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * 聊天对话.
 *
 * @author sxk
 */
public abstract class AClientChatActivity extends ActivitySupport {

    private Chat chat = null;
    private static int pageSize = 20;
    protected String title;// 聊天人的名称

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.NEW_MESSAGE_ACTION);
    }

    @Override
    protected void onPause() {
        // unregisterReceiver(receiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
