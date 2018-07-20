package com.chewuwuyou.app.utils;

import android.util.Log;

public class MyLog {
    private static boolean isLogD = true;

    private static boolean isLogE = true;

    private static boolean isLogI = true;

    private static boolean isLogW = true;

    private static boolean isLogV = true;

    public static void d(String tag, String msg) {
        if (isLogD) {
            Log.d(tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (isLogD) {
            Log.d(tag, msg, tr);
        }
    }

    public static void e(String tag, String msg) {
        if (isLogE) {
            Log.e(tag, msg);
        }

    }

    public static void e(String tag, String msg, Throwable tr) {
        if (isLogE) {
            Log.e(tag, msg, tr);
        }
    }

    public static void i(String tag, String msg) {
        if (isLogI) {
            Log.i(tag, msg);
        }

    }

    public static void i(String msg) {
        if (isLogI) {
            Log.i("app---->", msg);
        }

    }

    public static void i(String tag, String msg, Throwable tr) {
        if (isLogI) {
            Log.i(tag, msg, tr);
        }
    }

    public static void w(String tag, String msg) {
        if (isLogW) {
            Log.w(tag, msg);
        }

    }

    public static void w(String tag, Throwable tr) {
        if (isLogW) {
            Log.w(tag, tr);
        }

    }

    public static void w(String tag, String msg, Throwable tr) {
        if (isLogW) {
            Log.w(tag, msg, tr);
        }
    }

    public static void v(String tag, String msg) {
        if (isLogV) {
            Log.v(tag, msg);
        }

    }

    public static void v(String tag, String msg, Throwable tr) {
        if (isLogV) {
            Log.v(tag, msg, tr);
        }
    }
}
