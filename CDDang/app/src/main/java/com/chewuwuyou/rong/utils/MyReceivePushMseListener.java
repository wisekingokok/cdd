package com.chewuwuyou.rong.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.chewuwuyou.app.transition_view.activity.RongChatActivity;
import com.chewuwuyou.app.transition_view.fragment.RongChatMsgFragment;

import java.util.List;

import io.rong.imlib.model.Conversation;
import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

/**
 * Created by xxy on 2016/9/13 0013.
 */
public class MyReceivePushMseListener extends PushMessageReceiver {
    public static final String RE_TAG = "re_tag";
    public static final String RE_ID_TAG = "re_id_tag";

    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage message) {
        Log.e("-push-", "--" + message);
        return false;
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage message) {
        Intent intent = new Intent(context, RongChatActivity.class);
        Conversation.ConversationType conversationType = null;
        switch (message.getConversationType()) {
            case GROUP:
                conversationType = Conversation.ConversationType.GROUP;
                intent.putExtra(RongChatMsgFragment.KEY_TYPE, Conversation.ConversationType.GROUP);
                break;
            case PRIVATE:
                conversationType = Conversation.ConversationType.PRIVATE;
                intent.putExtra(RongChatMsgFragment.KEY_TYPE, Conversation.ConversationType.PRIVATE);
                break;
            case APP_PUBLIC_SERVICE:
                conversationType = Conversation.ConversationType.APP_PUBLIC_SERVICE;
                intent.putExtra(RongChatMsgFragment.KEY_TYPE, Conversation.ConversationType.APP_PUBLIC_SERVICE);
                break;
            case CHATROOM:
                conversationType = Conversation.ConversationType.CHATROOM;
                intent.putExtra(RongChatMsgFragment.KEY_TYPE, Conversation.ConversationType.CHATROOM);
                break;
            case CUSTOMER_SERVICE:
                conversationType = Conversation.ConversationType.CUSTOMER_SERVICE;
                intent.putExtra(RongChatMsgFragment.KEY_TYPE, Conversation.ConversationType.CUSTOMER_SERVICE);
                break;
            case DISCUSSION:
                conversationType = Conversation.ConversationType.DISCUSSION;
                intent.putExtra(RongChatMsgFragment.KEY_TYPE, Conversation.ConversationType.DISCUSSION);
                break;
            case NONE:
                conversationType = Conversation.ConversationType.NONE;
                intent.putExtra(RongChatMsgFragment.KEY_TYPE, Conversation.ConversationType.NONE);
                break;
            case PUBLIC_SERVICE:
                conversationType = Conversation.ConversationType.PUBLIC_SERVICE;
                intent.putExtra(RongChatMsgFragment.KEY_TYPE, Conversation.ConversationType.PUBLIC_SERVICE);
                break;
            case PUSH_SERVICE:
                conversationType = Conversation.ConversationType.PUSH_SERVICE;
                intent.putExtra(RongChatMsgFragment.KEY_TYPE, Conversation.ConversationType.PUSH_SERVICE);
                break;
            case SYSTEM:
                conversationType = Conversation.ConversationType.SYSTEM;
                intent.putExtra(RongChatMsgFragment.KEY_TYPE, Conversation.ConversationType.SYSTEM);
                break;
        }
        intent.putExtra(RongChatMsgFragment.KEY_TARGET, message.getTargetId());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
        startActivity(context, message.getTargetId(), conversationType);
        return true;
    }

    private void startActivity(Context context, String id, Conversation.ConversationType conversationType) {
        //判断app进程是否存活
//        if(isAppAlive(context, "com.chewuwuyou.app")){
//            //如果存活的话，就直接启动DetailActivity，但要考虑一种情况，就是app的进程虽然仍然在
//            //但Task栈已经空了，比如用户点击Back键退出应用，但进程还没有被系统回收，如果直接启动
//            //DetailActivity,再按Back键就不会返回MainActivity了。所以在启动
//            //DetailActivity前，要先启动MainActivity。
//            Intent mainIntent = new Intent(context, MainActivity.class);
//            //将MainAtivity的launchMode设置成SingleTask, 或者在下面flag中加上Intent.FLAG_CLEAR_TOP,
//            //如果Task栈中有MainActivity的实例，就会把它移到栈顶，把在它之上的Activity都清理出栈，
//            //如果Task栈不存在MainActivity实例，则在栈顶创建
//            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            Intent detailIntent = new Intent(context, DetailActivity.class);
//            detailIntent.putExtra("name", "电饭锅");
//            detailIntent.putExtra("price", "58元");
//            detailIntent.putExtra("detail", "这是一个好锅, 这是app进程存在，直接启动Activity的");
//            Intent[] intents = {mainIntent, detailIntent};
//            context.startActivities(intents);
//        }else {
        //如果app进程已经被杀死，先重新启动app，将DetailActivity的启动参数传入Intent中，参数经过
        //SplashActivity传入MainActivity，此时app的初始化已经完成，在MainActivity中就可以根据传入             //参数跳转到DetailActivity中去了
        Log.i("NotificationReceiver", "the app process is dead");
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage("com.chewuwuyou.app");
        launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        Bundle args = new Bundle();
        args.putString(RongChatMsgFragment.KEY_TARGET, id);
        args.putSerializable(RongChatMsgFragment.KEY_TYPE, conversationType);
        launchIntent.putExtra(RE_TAG, args);
        context.startActivity(launchIntent);
//        }
    }


    public static boolean isAppAlive(Context context, String packageName) {
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos
                = activityManager.getRunningAppProcesses();
        for (int i = 0; i < processInfos.size(); i++) {
            if (processInfos.get(i).processName.equals(packageName)) {
                Log.i("NotificationLaunch",
                        String.format("the %s is running, isAppAlive return true", packageName));
                return true;
            }
        }
        Log.i("NotificationLaunch",
                String.format("the %s is not running, isAppAlive return false", packageName));
        return false;
    }
}
