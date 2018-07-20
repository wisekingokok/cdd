package com.chewuwuyou.rong.utils;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Administrator on 2016/8/13 0013.
 */
public class KeyboardUtil {

    /**
     * 影藏键盘
     */
    public static void dismissKeyboard(Activity activity) {
        try {
            activity.getCurrentFocus().clearFocus();
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
