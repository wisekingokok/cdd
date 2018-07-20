
package com.chewuwuyou.app;

import java.util.List;
import java.util.Stack;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 *
 * @author xiajy
 * @version 1.0
 * @created 2012-3-21
 */
public class AppManager {

    private static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 清除栈中所有的页面，只在退出时调用
     */
    public void finishAllActivity() {
        for (Activity a : activityStack) {
            if (a != null && !a.isFinishing()) {
                a.finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    @SuppressWarnings("deprecation")
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
        }
    }

    /**
     * 判断activity是否在堆栈中
     *
     * @param cls
     * @param context
     * @return
     */
    public static boolean isExsitMianActivity(Class<?> cls, Context context) {
        boolean flag = false;
        if(activityStack==null)
            return false;
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * 判断某一个类是否存在任务栈里面
     *
     * @return
     */
//    public static boolean isExsitMianActivity(Class<?> cls, Context context) {
//        Intent intent = new Intent(context, cls);
//        ComponentName cmpName = intent.resolveActivity(context.getPackageManager());
//        boolean flag = false;
//        if (cmpName != null) { // 说明系统中存在这个activity
//            ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
//            List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);
//            for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
//                if (taskInfo.baseActivity.equals(cmpName)) { // 说明它已经启动了
//                    flag = true;
//                    break;  //跳出循环，优化效率
//                }
//            }
//        }
//        return flag;
//    }
}
