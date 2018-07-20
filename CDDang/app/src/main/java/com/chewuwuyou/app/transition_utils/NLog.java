package com.chewuwuyou.app.transition_utils;

import android.util.Log;

import com.chewuwuyou.app.transition_constant.Constants;


/**
 * Created by Yogi on 16/9/24.
 */

public class NLog {
    public static void i(Object obj){
        Log.i(Constants.APP_NAME, obj == null ? "null" : obj.toString());
    }

    public static void d(Object obj){
        Log.d(Constants.APP_NAME, obj == null ? "null" : obj.toString());
    }

    public static void e(Throwable t){
        Log.e(Constants.APP_NAME, t.getMessage(), t);
    }
}
