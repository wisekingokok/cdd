package com.chewuwuyou.eim.activity.im;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import net.tsz.afinal.http.AjaxParams;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.muc.MultiUserChat;

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

/**
 * 聊天对话.
 *
 * @author sxk
 */
public abstract class AChatActivity extends ActivitySupport {

    private Chat chat = null;
    public List<IMMessage> message_pool = new ArrayList<IMMessage>();
    protected String to;// 聊天人
    protected String room = "";// 聊天所在房间
    private static int pageSize = 20;
    protected String title;// 聊天人的名称

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.NEW_MESSAGE_ACTION);
        registerReceiver(receiver, filter);
        to = getIntent().getStringExtra("to");
        title = getIntent().getStringExtra("title");
        MyLog.e("YUY", "to------------------" + to);
        // to += "@iz232jtyxeoz/Smack";
        if (to == null)
            return;

        // 群聊的时候，这里不需要建设lisenter和chat对
        if (to.contains("conference")) {
            return;
        }

        ChatManager.getInstanceFor(
                XmppConnectionManager.getInstance().getConnection())
                .addChatListener(new ChatManagerListener() {

                    @Override
                    public void chatCreated(Chat arg0, boolean arg1) {
                        Log.d("info", "chatcreate:" + arg1);
                    }
                });

        chat = ChatManager.getInstanceFor(
                XmppConnectionManager.getInstance().getConnection())
                .createChat(to, new MessageListener() {

                    @Override
                    public void processMessage(Chat arg0, Message arg1) {
                        Log.d("info", "message:" + arg1.toXML());
                    }
                });
    }

    @Override
    protected void onPause() {
        // unregisterReceiver(receiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        // 第一次查询
        // 群聊
        if (to.contains("conference")) {
            message_pool = MessageManager.getInstance(context)
                    .getMessageListByRoom(to, 1, pageSize);
        } else {
            message_pool = MessageManager.getInstance(context)
                    .getMessageListByFrom(to, 1, pageSize);
        }

        if (null != message_pool && message_pool.size() > 0)
            Collections.sort(message_pool);

        MyLog.i("YUY", "to = " + to);
        // if (to.contains("conference")) {// 判断为群组聊天
        // NoticeManager.getInstance(context).updateStatusByRoom(to,
        // Notice.READ);
        // } else {
        // 更新某人所有通知
        NoticeManager.getInstance(context).updateStatusByFrom(to, Notice.READ);
        // }
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            MyLog.i("YUY", "aaaaaaaaaaaaaxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
                    + action);
            if (Constant.NEW_MESSAGE_ACTION.equals(action)) {

                IMMessage message = intent
                        .getParcelableExtra(IMMessage.IMMESSAGE_KEY);
                // Log.e("SXKTEST", "from:"+message.getFromSubJid()+"to:"+to);
                // Toast.makeText(AChatActivity.this,
                // "from:"+message.getFromSubJid()+"to:"+to,
                // Toast.LENGTH_LONG).show();
                // 不是同一个人的消息，在本页面直接无视，因为没必要把别人的互相聊天放到这个页面

                // 群聊的情况下conference
                if (!StringUtil.isNullOrEmpty(to) && to.contains("conference")) {
                    // 与群有关的消息
                    if (message.getFromRoom().equals(to)) {
                        message_pool.add(message);
                        receiveNewMessage(message);
                        refreshMessage(message_pool);
                    }
                } else if (!StringUtil.isNullOrEmpty(to) && !StringUtil.isNullOrEmpty(message.getFromSubJid())) {
                    String thisPagePerson = to.substring(0, to.indexOf("@"));
                    String receivingPerson = message.getFromSubJid().substring(
                            0, message.getFromSubJid().indexOf("@"));
                    if (thisPagePerson.equals(receivingPerson)) {
                        message_pool.add(message);
                        receiveNewMessage(message);
                        refreshMessage(message_pool);
                    }
                    // 数据信息都不对，就无视这次收听事件，不更新UI和数据
                } else {
                }

            }
        }

    };

    protected abstract void receiveNewMessage(IMMessage message);

    protected abstract void refreshMessage(List<IMMessage> messages);

    protected List<IMMessage> getMessages() {
        return message_pool;
    }

    protected void sendMessage(String messageContent) throws Exception {
        String time = DateUtil.date2Str(Calendar.getInstance(),
                Constant.MS_FORMART);
        Message message = new Message();
        // message.setProperty(IMMessage.KEY_TIME, time);
        // message.setProperty("type", "chat");

        // message.setFrom("sxk2@iz232jtyxeoz");
        // message.setTo("sxk@iz232jtyxeoz");
        // message.setType(Type.chat);
        // Log.d("send", message.toXML());
        if (messageContent.contains("#VOI") && messageContent.contains("IOV#")) {
            int start = messageContent.indexOf("#VOI");
            message.setBody(messageContent.substring(start));
        } else {
            message.setBody(messageContent);
        }
        if (chat != null) {// 普通聊天
            chat.sendMessage(message);
        } else if (to.contains("conference")) {// 群聊
//			MultiUserChat room = AppContext.getInstance().getRoomByRoomName(to);
//			if (room != null)
//				room.sendMessage(messageContent);
        }
        IMMessage newMessage = new IMMessage();
        newMessage.setMsgType(1);
        // 群聊
        if (to.contains("conference")) {
            // String nick = AppContext.getInstance().getNickByRoomName(to);
            newMessage.setFromSubJid(CacheTools.getUserData("userId"));
            newMessage.setFromRoom(to);
        } else {
            newMessage.setFromSubJid(chat.getParticipant());
            MyLog.e("YUY", "fromSubJid-----------------" + chat.getParticipant());
        }
        if (messageContent.contains("#VOI") && messageContent.contains("IOV#")) {
            int end = messageContent.indexOf("#VOI");
            newMessage.setFileName(messageContent.substring(0, end));
        } else {
            newMessage.setContent(messageContent);
        }
        newMessage.setMsgTo(CacheTools.getUserData("userId"));
        newMessage.setTime(time);
        MyLog.i("YUY",
                "--------------消息类型---"
                        + ChatUtils.getChatMessageType(messageContent));

        newMessage.setType(ChatUtils.getChatMessageType(messageContent));// 设置消息类型
        newMessage.setContent(messageContent);
        message_pool.add(newMessage);
        MessageManager.getInstance(context).saveIMMessage(newMessage);
        // MChatManager.message_pool.add(newMessage);

        // 刷新视图
        refreshMessage(message_pool);

        // 群聊
        if (to.contains("conference")) {
            // 群聊不发推送
            return;
        }
        Presence presence = XmppConnectionManager.getInstance().getConnection()
                .getRoster().getPresence(to);
        if (!presence.isAvailable()) {
            // TODO 余勇发推送
            // 用户不在线，或者离线的时候，我们发发推送提醒下他们来登陆看离线消息。
            // @"api/chat/sendUnavail"
            // @{@"userId":who,@"talk":talk};
            AjaxParams params = new AjaxParams();
            params.put("userId", chat.getParticipant().split("@")[0]);
            params.put("talk", ChatUtils.returnMsg(messageContent));
            requestNet(new Handler() {
                @Override
                public void handleMessage(android.os.Message msg) {
                    // TODO Auto-generated method stub
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case com.chewuwuyou.app.utils.Constant.NET_DATA_SUCCESS:
                            MyLog.i("YUY", "=======发送离线消息成功");
                            break;

                        default:
                            break;
                    }
                }
            }, params, NetworkUtil.SEND_UNAVAIL, false, 1);

        }
    }

    /**
     * 下滑加载信息,true 返回成功，false 数据已经全部加载，全部查完了，
     */
    protected Boolean addNewMessage() {
        List<IMMessage> newMsgList = MessageManager.getInstance(context)
                .getMessageListByFrom(to, message_pool.size(), pageSize);
        if (newMsgList != null && newMsgList.size() > 0) {
            message_pool.addAll(newMsgList);
            Collections.sort(message_pool);
            return true;
        }
        return false;
    }

    protected void resh() {
        // 刷新视图
        refreshMessage(message_pool);
    }

}
