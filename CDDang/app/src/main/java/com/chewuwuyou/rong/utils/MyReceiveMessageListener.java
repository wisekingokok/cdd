package com.chewuwuyou.rong.utils;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.transition_view.activity.RongChatActivity;
import com.chewuwuyou.app.transition_view.fragment.RongChatMsgFragment;
import com.chewuwuyou.app.utils.AppUtils;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.rong.bean.CDDYWZMsg;
import com.chewuwuyou.rong.bean.ChangeGroupBean;
import com.chewuwuyou.rong.bean.ContactNotificationMessage;
import com.chewuwuyou.rong.bean.RecallMsgBean;

import de.greenrobot.event.EventBus;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.RecallNotificationMessage;
import io.rong.message.TextMessage;


/**
 * 接收消息监听器的实现，所有接收到的消息、通知、状态都经由此处设置的监听器处理。包括私聊消息、讨论组消息、群组消息、聊天室消息以及各种状态。
 */
public class MyReceiveMessageListener implements RongIMClient.OnReceiveMessageListener {
    private Context context;
    private NotificationManager mNotificationManager;
    public static int NotificationID = 123278;

    public MyReceiveMessageListener(Context context) {
        this.context = context;
    }

    /**
     * 收到消息的处理。
     *
     * @param message 收到的消息实体。
     * @param left    剩余未拉取消息数目。
     * @return
     */
    @Override
    public boolean onReceived(Message message, int left) {//开发者根据自己需求自行处理
        Log.e("onReceived", "onReceived--" + message.getTargetId() + "  " + message.getConversationType() + " " + message.getObjectName());
        String type = message.getObjectName();
        if (type == null) {
            Log.e("ReceiveMessageListener", "收到一个ObjectName为NULL的消息,不发送!");
            return false;
        }
        if (type.equals(RongMsgType.CDD_TXT_MSG)) {
            EventBus.getDefault().post(message);
            CDDYWZMsg cddywzMsg = (CDDYWZMsg) message.getContent();
            showNotification(message.getConversationType(), message.getTargetId(), "消息提醒", cddywzMsg.getContent(), "新消息:" + cddywzMsg.getContent());
        } else if (type.equals(RongMsgType.GIF_TXT_MSG)) {
            EventBus.getDefault().post(message);
            showNotification(message.getConversationType(), message.getTargetId(), "消息提醒", "[表情]", "新消息:[表情]");
        } else if (type.equals(RongMsgType.CDD_LBS_MSG)) {
            EventBus.getDefault().post(message);
            showNotification(message.getConversationType(), message.getTargetId(), "消息提醒", "[位置]", "新消息:[位置]");
        } else if (type.equals(RongMsgType.RC_TXT_MSG)) {
            EventBus.getDefault().post(message);
            TextMessage textMessage = (TextMessage) message.getContent();
            showNotification(message.getConversationType(), message.getTargetId(), "消息提醒", textMessage.getContent(), "新消息:" + textMessage);
        } else if (type.equals(RongMsgType.RC_VC_MSG)) {
            EventBus.getDefault().post(message);
            showNotification(message.getConversationType(), message.getTargetId(), "消息提醒", "[语音]", "新消息:[语音]");
        } else if (type.equals(RongMsgType.RC_IMG_MSG)) {
            EventBus.getDefault().post(message);
            showNotification(message.getConversationType(), message.getTargetId(), "消息提醒", "[图片]", "新消息:[图片]");
        } else if (type.equals(RongMsgType.RC_IMG_TXT_MSG)) {
            Log.e("ReceiveMessageListener", "收到一个rong图文消息,不发送!");
        } else if (type.equals(RongMsgType.RC_LBS_MSG)) {
            Log.e("ReceiveMessageListener", "收到一个rong图文消息,不发送!");
        } else if (type.equals(RongMsgType.RC_INFONTF_MSG)) {
            EventBus.getDefault().post(message);
            Log.e("ReceiveMessageListener", "收到一个rong小灰条通知消息");
        } else if (type.equals(RongMsgType.RC_CONTACTNTF_MSG)) {
            MyLog.i("收到验证");
            EventBus.getDefault().post((ContactNotificationMessage) message.getContent());
            showNotification(message.getConversationType(), message.getTargetId(), "系统提醒", "收到好友请求！", "系统提醒:收到好友请求！");
        } else if (type.equals(RongMsgType.RC_PROFILENTF_MSG)) {
            Log.e("ReceiveMessageListener", "收到一个rong资料更新通知消息,不发送!");
        } else if (type.equals(RongMsgType.RC_CMDNTF_MSG)) {
            Log.e("ReceiveMessageListener", "收到一个rong通用命令通知消息,不发送!");
        } else if (type.equals(RongMsgType.RC_TYPSTS_MSG)) {
            Log.e("ReceiveMessageListener", "收到一个rong正在输入通知消息,不发送!");
        } else if (type.equals(RongMsgType.RC_CHE_MSG)) {
//            EventBus.getDefault().post(message);
            EventBus.getDefault().post(new RecallMsgBean(message.getMessageId(), (RecallNotificationMessage) message.getContent()));
        } else if (type.equals(RongMsgType.CDD_TASK_MSG)) {
            if (message.getTargetId().equals(Constant.USER_ID_TYPE.TASK_HALL)) {
                CacheTools.setUserData("isRead", "0");
            }
            EventBus.getDefault().post(message);
            showNotification(message.getConversationType(), message.getTargetId(), "系统提醒", "您所在地区有订单!", "系统提醒:您所在地区有订单!");
        } else if (type.equals(RongMsgType.RONG_GROUP_NOTIFITION)) {//群信息变更消息
            EventBus.getDefault().post(message);
            EventBus.getDefault().post(new ChangeGroupBean(message.getTargetId()));
        } else if (type.equals(RongMsgType.CDD_HB_MSG)) {
            EventBus.getDefault().post(message);
        } else if (type.equals(RongMsgType.CDD_ZZ_MSG)) {
            EventBus.getDefault().post(message);
        } else {
            Log.e("ReceiveMessageListener", "收到一个未知类型的消息!");
        }
        return false;
    }

    private void showNotification(final Conversation.ConversationType conversationType, final String targentId, final String title, final String content, final String ticker) {
        if (AppUtils.isAppIsInBackground(context)) {//在后台
            RongApi.getConversationNotificationStatus(conversationType, targentId, new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
                @Override
                public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {
                    switch (conversationNotificationStatus) {
                        case DO_NOT_DISTURB:
                            Log.e("ReceiveMessageListener", "消息免打扰，不提示!");
                            break;
                        case NOTIFY:
                            Log.e("YUY", "conversationType = " + conversationType + " targentId = " + targentId);
                            buildNotification(conversationType, targentId, title, content, ticker);
                            break;
                    }
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.e("ReceiveMessageListener", "获取消息免打扰状态失败，不提示!");
                }
            });
        }
    }

    private void buildNotification(Conversation.ConversationType conversationType, String targentId, String title, String content, String ticker) {
        if (mNotificationManager == null)
            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        Intent intent = new Intent(context, RongChatActivity.class);
        switch (conversationType) {
            case GROUP:
                intent.putExtra(RongChatMsgFragment.KEY_TYPE, Conversation.ConversationType.GROUP);
                break;
            case PRIVATE:
                intent.putExtra(RongChatMsgFragment.KEY_TYPE, Conversation.ConversationType.PRIVATE);
                break;
            case APP_PUBLIC_SERVICE:
                intent.putExtra(RongChatMsgFragment.KEY_TYPE, Conversation.ConversationType.APP_PUBLIC_SERVICE);
                break;
            case CHATROOM:
                intent.putExtra(RongChatMsgFragment.KEY_TYPE, Conversation.ConversationType.CHATROOM);
                break;
            case CUSTOMER_SERVICE:
                intent.putExtra(RongChatMsgFragment.KEY_TYPE, Conversation.ConversationType.CUSTOMER_SERVICE);
                break;
            case DISCUSSION:
                intent.putExtra(RongChatMsgFragment.KEY_TYPE, Conversation.ConversationType.DISCUSSION);
                break;
            case NONE:
                intent.putExtra(RongChatMsgFragment.KEY_TYPE, Conversation.ConversationType.NONE);
                break;
            case PUBLIC_SERVICE:
                intent.putExtra(RongChatMsgFragment.KEY_TYPE, Conversation.ConversationType.PUBLIC_SERVICE);
                break;
            case PUSH_SERVICE:
                intent.putExtra(RongChatMsgFragment.KEY_TYPE, Conversation.ConversationType.PUSH_SERVICE);
                break;
            case SYSTEM:
                intent.putExtra(RongChatMsgFragment.KEY_TYPE, Conversation.ConversationType.SYSTEM);
                break;
        }
        intent.putExtra(RongChatMsgFragment.KEY_TARGET, targentId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentTitle(title)//设置通知栏标题
                .setContentText(content) //设置通知栏显示内容
                .setContentIntent(pendingIntent) //设置通知栏点击意图
                //  .setNumber(number) //设置通知集合的数量
                .setTicker(ticker) //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setSmallIcon(R.drawable.ic_launcher);//设置通知小ICON
        mNotificationManager.notify(NotificationID, mBuilder.build());
    }
}



