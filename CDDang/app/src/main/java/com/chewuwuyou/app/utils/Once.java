package com.chewuwuyou.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by CLOUD on 2016/8/26.只出现一次
 */
public class Once {


    SharedPreferences mSharedPreferences;
    Context mContext;

    public Once(Context context) {
        mSharedPreferences = context.getSharedPreferences("once", Context.MODE_PRIVATE);
        mContext = context;
    }

    public void show(String tagKey, OnceCallback callback) {
        boolean isSecondTime = mSharedPreferences.getBoolean(tagKey, false);
        if (!isSecondTime) {
            callback.onOnce();
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(tagKey, true);
            editor.apply();
            editor.commit();
        }
    }

    public void show(int tagKeyResId, OnceCallback callback) {
        show(mContext.getString(tagKeyResId), callback);
    }

    public interface OnceCallback {
        void onOnce();
    }

    public static void clear(String key, Context mContext) {
        mContext.getSharedPreferences("once", Context.MODE_PRIVATE).edit().remove(key).apply();

    }

}
