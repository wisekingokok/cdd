
package com.chewuwuyou.app.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ToastUtil {

    private static Toast mToast = null;
    public static int LENGTH_LONG = Toast.LENGTH_LONG;

    /**
     * 普通文本消息提示
     *
     * @param context
     * @param id
     */
    public static void showToast(Context context, int id) {
        // 创建一个Toast提示消息
        if (mToast != null)
            mToast.setText(id);
        else
            mToast = Toast.makeText(context, id, Toast.LENGTH_SHORT);
        // 设置Toast提示消息在屏幕上的位置
        mToast.setGravity(Gravity.CENTER, 0, 0);
        // 显示消息
        mToast.show();
    }

    /**
     * 普通文本消息提示
     *
     * @param context
     * @param content
     */
    public static void toastShow(Context context, String content) {
        // 创建一个Toast提示消息
        if (mToast != null)
            mToast.setText(content);
        else
            mToast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        // 设置Toast提示消息在屏幕上的位置
        mToast.setGravity(Gravity.CENTER, 0, 0);
        // 显示消息mOldPhone
        mToast.show();
    }

    /**
     * 带图片消息提示
     *
     * @param context
     * @param ImageResourceId
     * @param text
     * @param duration
     */
    public static void ImageToast(Context context, int ImageResourceId,
                                  CharSequence text, int duration) {
        // 创建一个Toast提示消息
        mToast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        // 设置Toast提示消息在屏幕上的位置
        mToast.setGravity(Gravity.CENTER, 0, 0);
        // 获取Toast提示消息里原有的View
        View toastView = mToast.getView();
        // 创建一个ImageView
        ImageView img = new ImageView(context);
        img.setImageResource(ImageResourceId);
        // 创建一个LineLayout容器
        LinearLayout ll = new LinearLayout(context);
        // 向LinearLayout中添加ImageView和Toast原有的View
        ll.addView(img);
        ll.addView(toastView);
        // 将LineLayout容器设置为toast的View
        mToast.setView(ll);
        // 显示消息
        mToast.show();
    }

}
