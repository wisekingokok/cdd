package com.chewuwuyou.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.multidex.MultiDex;

import com.baidu.mapapi.SDKInitializer;
import com.cheshouye.api.client.WeizhangIntentService;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.Tools;
import com.chewuwuyou.rong.bean.CDDGifMsg;
import com.chewuwuyou.rong.bean.CDDHBMsg;
import com.chewuwuyou.rong.bean.CDDLBSMsg;
import com.chewuwuyou.rong.bean.CDDTaskMsg;
import com.chewuwuyou.rong.bean.CDDYWZMsg;
import com.chewuwuyou.rong.bean.CDDZZMsg;
import com.chewuwuyou.rong.bean.ContactNotificationMessage;
import com.chewuwuyou.rong.bean.InformationNotificationMessage;
import com.chewuwuyou.rong.utils.MyConnectionStatusListener;
import com.chewuwuyou.rong.utils.MyRecallMessageListener;
import com.chewuwuyou.rong.utils.MyReceiveMessageListener;
import com.chewuwuyou.app.transition_view.activity.RongChatActivity;
import com.chewuwuyou.app.transition_view.fragment.RongChatMsgFragment;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.socialize.PlatformConfig;

import java.util.LinkedList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import io.rong.imlib.AnnotationNotFoundException;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.message.GroupNotificationMessage;

public class AppContext extends Application {

    //手势密码 输入5次错误提示
    public int count = 4;

    public int getCount() {
        return count;
    }

    public void setCount(int mCount) {
        count = mCount;
    }

    private List<Activity> activityList = new LinkedList<Activity>();
    private static AppContext mInstance = null;

    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    // 遍历所有Activity并finish
    public void exit() {
        for (Activity activity : activityList) {
            if (activity != null)
                activity.finish();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
//         JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
        mInstance = this;
        CrashReport.initCrashReport(getApplicationContext(), "900017475", false);//Bugly日志初始化
        // CrashReport.testJavaCrash();//测试崩溃日志搜集
        StrictMode.ThreadPolicy oldPolicy = StrictMode.allowThreadDiskReads();
        // 从磁盘读取数据
        StrictMode.setThreadPolicy(oldPolicy);
        String processName = Tools.getProcessName(this, android.os.Process.myPid());
        initImageLoader(getApplicationContext());
        if (processName != null) {
            boolean defaultProcess = processName.equals("com.chewuwuyou.app");
            if (defaultProcess) {
                Intent weizhangIntent = new Intent(this, WeizhangIntentService.class);
                weizhangIntent.putExtra("appId", 888);
                weizhangIntent.putExtra("appKey", "98d5ad2b0bd453805a2fdd445e9e8085");
                startService(weizhangIntent);
                if (!Tools.isEmulator(this)) { // 判断是否是模拟器
                    CacheTools.initCache(getApplicationContext());
                    NetworkUtil.initNetwork(getApplicationContext());
                    // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
                    SDKInitializer.initialize(this);
                } else {
                    // SDKInitializer.initialize(this);
                }
                JPushInterface.init(getApplicationContext()); // 初始化 JPush
            }

            initShare();
        }
        initRong();
    }

    public void initRong() {
        /**
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIMClient 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {
            RongIMClient.init(this);
        }
        try {
            RongIMClient.registerMessageType(CDDGifMsg.class);
            RongIMClient.registerMessageType(CDDYWZMsg.class);
            RongIMClient.registerMessageType(CDDLBSMsg.class);
            RongIMClient.registerMessageType(CDDTaskMsg.class);
            RongIMClient.registerMessageType(CDDHBMsg.class);
            RongIMClient.registerMessageType(CDDZZMsg.class);
            RongIMClient.registerMessageType(GroupNotificationMessage.class);
            RongIMClient.registerMessageType(InformationNotificationMessage.class);
            RongIMClient.registerMessageType(ContactNotificationMessage.class);//加好友相关消息
        } catch (AnnotationNotFoundException e) {
            e.printStackTrace();
        }
        RongIMClient.setOnReceiveMessageListener(new MyReceiveMessageListener(this));
        RongIMClient.setRecallMessageListener(new MyRecallMessageListener());
        RongIMClient.setConnectionStatusListener(new MyConnectionStatusListener());
    }

    private void initShare() {

        PlatformConfig.setQQZone("1103015801", "u6FqhMEkW2GQWFKX");
        //   PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad");
        PlatformConfig.setWeixin("wx2d83346d15e1f3e2", "337acceb5c97d1a6f421df40d92c5e56");

    }

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid)
                return appProcess.processName;
        }
        return null;
    }

    public static AppContext getInstance() {
        return mInstance;
    }

    public String getUserId() {
        return CacheTools.getUserData("userId");
    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .threadPoolSize(4)
                .build();
        ImageLoader.getInstance().init(config);
    }


    /**
     * 发起一个聊天
     *
     * @param context
     * @param to      和谁聊天
     */
    public static void createChat(Context context, String to) {
        Intent intent = new Intent(context, RongChatActivity.class);
        intent.putExtra(RongChatMsgFragment.KEY_TARGET, to);
        intent.putExtra(RongChatMsgFragment.KEY_TYPE, Conversation.ConversationType.PRIVATE);
        context.startActivity(intent);
    }

}
