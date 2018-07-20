package com.chewuwuyou.app.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.chewuwuyou.app.AppStart;
import com.chewuwuyou.app.bean.SearchFriend;
import com.chewuwuyou.app.eventbus.EventBusAdapter;
import com.chewuwuyou.app.ui.BusinessHallActivity;
import com.chewuwuyou.app.ui.LoginActivity;
import com.chewuwuyou.app.ui.MainActivityEr;
import com.chewuwuyou.app.ui.NewRobOrderDetailsActivity;
import com.chewuwuyou.app.ui.OrderActivity;
import com.chewuwuyou.app.ui.SearchFriendActivity;
import com.chewuwuyou.app.ui.ToquoteAcitivity;
import com.chewuwuyou.eim.activity.im.ChatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;
import de.greenrobot.event.EventBus;

/**
 * 自定义接收器
 * <p/>
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";
    private static String mOrderNum = null;//订单号
    private static String oedearNumber;//订单状态
    private static String flag;
    private static String mType;//消息类型

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.e(TAG, "[MyReceiver] onReceive - " + intent.getAction()
                + ", extras: " + printBundle(bundle) + "-------------------------JpushIn =  " + JPushInterface.ACTION_MESSAGE_RECEIVED);
        Intent intent1;
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle
                    .getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            // send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
                .getAction())) {
            Log.e(TAG,
                    "[MyReceiver] 接收到推送下来的自定义消息: "
                            + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//            processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
                .getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle
                    .getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
                .getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            // 打开自定义的Activity
            if (CacheTools.getUserData("role") == null) {
                Intent i = new Intent(context, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            } else if (flag.equals("3") && (oedearNumber.equals("27")) && mOrderNum.contains("CDD") && !TextUtils.isEmpty(mOrderNum)){
                intent1 = new Intent(context, BusinessHallActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                EventBusAdapter bsAdapter = new EventBusAdapter();
                bsAdapter.setOrderRemind("0");
                EventBus.getDefault().post(bsAdapter);
                context.startActivity(intent1);
            }else if ((flag.equals("2") || flag.equals("1")) && (oedearNumber.equals("27") || oedearNumber.equals("28")) && mOrderNum.contains("CDD") && !TextUtils.isEmpty(mOrderNum)) {
                intent1 = new Intent(context, ToquoteAcitivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.putExtra("taskId", mOrderNum.split("_")[1]);
                context.startActivity(intent1);
            } else if (!TextUtils.isEmpty(mOrderNum) && flag.equals("3") && (oedearNumber.equals("27") || oedearNumber.equals("28")) && mOrderNum.contains("CDD")) {
                intent1 = new Intent(context, NewRobOrderDetailsActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.putExtra("taskId", mOrderNum.split("_")[1]);
                context.startActivity(intent1);
            } else if (!TextUtils.isEmpty(mOrderNum) && mOrderNum.contains("CDD")) {
                intent1 = new Intent(context, OrderActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.putExtra("taskId", mOrderNum.split("_")[1]);
                context.startActivity(intent1);
            } else if (mType.equals("2")) {//聊天推送
                intent1 = new Intent(context, ChatActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.putExtra("to", mOrderNum );
                intent1.putExtra("title", "");
                context.startActivity(intent1);
            } else if (mType.equals("3")) {//加好友
                intent1 = new Intent(context, SearchFriendActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent1);
            } else {
                intent1 = new Intent(context, MainActivityEr.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent1);
            }

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
                .getAction())) {
            Log.d(TAG,
                    "[MyReceiver] 用户收到到RICH PUSH CALLBACK: "
                            + bundle.getString(JPushInterface.EXTRA_EXTRA));
            // 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
            // 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
                .getAction())) {
            boolean connected = intent.getBooleanExtra(
                    JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction()
                    + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {


            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(
                            bundle.getString(JPushInterface.EXTRA_EXTRA));
                    MyLog.e("YUY", "推送收到的消息 = " + json);
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" + myKey + " - "
                                + json.optString(myKey) + "]");
                        mOrderNum = json.optString("info");
                        oedearNumber = json.optString("status");
                        flag = json.optString("flag");
                        mType = json.optString("type");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
        MyLog.e("YUY", "xxxxxxxxxxxxxxxxxxxxxxxprocessCustomMessage");
        if (AppStart.isForeground) {
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Intent msgIntent = new Intent(AppStart.MESSAGE_RECEIVED_ACTION);
            msgIntent.putExtra(AppStart.KEY_MESSAGE, message);
            if (!TextUtils.isEmpty(extras)) {
                try {
                    JSONObject extraJson = new JSONObject(extras);
                    if (null != extraJson && extraJson.length() > 0) {
                        msgIntent.putExtra(AppStart.KEY_EXTRAS, extras);
                    }
                } catch (JSONException e) {

                }

            }
            context.sendBroadcast(msgIntent);

        }
    }
}
