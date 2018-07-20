package com.chewuwuyou.app.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;

public class JpushUtil {
    public static final String PREFS_NAME = "JPUSH_EXAMPLE";
    public static final String PREFS_DAYS = "JPUSH_EXAMPLE_DAYS";
    public static final String PREFS_START_TIME = "PREFS_START_TIME";
    public static final String PREFS_END_TIME = "PREFS_END_TIME";
    public static final String KEY_APP_KEY = "JPUSH_APPKEY";

    public static boolean isEmpty(String s) {
        if (null == s)
            return true;
        if (s.length() == 0)
            return true;
        if (s.trim().length() == 0)
            return true;
        return false;
    }

    // 校验Tag Alias 只能是数字,英文字母和中文
    public static boolean isValidTagAndAlias(String s) {
        Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]{0,}$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    // 取得AppKey
    public static String getAppKey(Context context) {
        Bundle metaData = null;
        String appKey = null;
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai)
                metaData = ai.metaData;
            if (null != metaData) {
                appKey = metaData.getString(KEY_APP_KEY);
                if ((null == appKey) || appKey.length() != 24) {
                    appKey = null;
                }
            }
        } catch (NameNotFoundException e) {

        }
        return appKey;
    }

    // 取得版本号
    public static String GetVersion(Context context) {
        try {
            PackageInfo manager = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return manager.versionName;
        } catch (NameNotFoundException e) {
            return "Unknown";
        }
    }


    public static void showToast(final String toast, final Context context) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }).start();
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    /**
     * 极光推送设置标签
     *
     * @return
     */
    public static Set<String> pushSetTag() {
        String role = CacheTools.getUserData("role");
        String daiLiType = CacheTools.getUserData("daiLitype");
        String province = CacheTools.getUserData("loginProvince");
        String city = CacheTools.getUserData("loginCity");
        String district = CacheTools.getUserData("loginDistrict");
        Set<String> set = new HashSet<String>();
        if (role.contains("1")) {// 用户
            set.add("CDDUSER");
        }
        if (role.contains("2")) {// A类商家
            set.add("CDDTYPEA");
        }
        if (role.contains("3")) {// B类商家
            set.add("CDDTYPEB");
        }
        if (daiLiType.equals("1")) {// 省代
            set.add("CDDPROAGEN");
        }
        if (daiLiType.equals("2")) {// 市代
            set.add("CDDCITAGEN");
        }
        if (!TextUtils.isEmpty(province)) {//省标签
            set.add(province);
        }
        if (!TextUtils.isEmpty(city)) {//市标签
            set.add(city);
        }
        if (!TextUtils.isEmpty(district)) {//市区标签
            set.add(district);
        }
        // set.add("VER"+getAppVersionName(getApplicationContext()).substring(0,
        // 5).replace(".", "_"));//版本号暂时不用
        return set;
    }


}
