package com.chewuwuyou.app.transition_utils;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.JpushUtil;
import com.chewuwuyou.app.utils.MyLog;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by yuyong on 16/10/14.
 */

public class JpushUtils {

    private Context context;
    private static final int MSG_SET_ALIAS = 1001;
    private static final int MSG_SET_TAGS = 1002;

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    MyLog.i("YUY", "推送Alias ----- " + logs);
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    MyLog.i("YUY", "推送Alias ----- " + logs);
                    if (JpushUtil.isConnected(context.getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    } else {
                        MyLog.i("YUY", "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    MyLog.i("YUY", "推送Alias ----- " + logs);
                    break;
            }
            MyLog.e("YUY", "推送Alias ----- " + logs);

        }

    };

    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    MyLog.i("YUY", "推送tag ----- " + logs);
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    MyLog.i("YUY", "推送tag ----- " + logs);
                    if (JpushUtil.isConnected(context.getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
                    } else {
                        Log.i("YUY", "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    MyLog.i("YUY", "推送tag ----- " + logs);
                    break;
            }
            MyLog.e("YUY", "推送tag ----- " + logs);
        }

    };


    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d("YUY", "Set alias in handler.");
                    JPushInterface.setAliasAndTags(context, (String) msg.obj, null, mAliasCallback);
                    break;

                case MSG_SET_TAGS:
                    Log.d("YUY", "Set tags in handler.");
                    JPushInterface.setAliasAndTags(context, null, (Set<String>) msg.obj, mTagsCallback);
                    break;

                default:
                    Log.i("YUY", "Unhandled msg - " + msg.what);
                    break;
            }
        }
    };

    /**
     * 初始化极光推送并设置标签和别名
     */
    public void initUserJpush() {
        final String alias = "cwwy" + CacheTools.getUserData("userId");
        MyLog.e("YUY", "推送别名 ----------" + alias);
        MyLog.e("YUY", "推送registerId = " + JPushInterface.getRegistrationID(context.getApplicationContext()));
        JPushInterface.init(context.getApplicationContext());// 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
        JPushInterface.resumePush(context.getApplicationContext());
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, JpushUtil.pushSetTag()));//调用JPush API设置Tag
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias)); //调用JPush API设置Alias
    }


    /**
     * 注销极光推送
     */
    public void unregisterJPush() {
        JPushInterface.stopPush(context.getApplicationContext());
    }


}
