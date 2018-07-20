package com.chewuwuyou.rong.utils;

import android.text.TextUtils;

import com.chewuwuyou.app.utils.ToastUtil;

import io.rong.message.TextMessage;

/**
 * Created by yuyong on 2016/9/8.
 * 消息相关工具类
 */
public class MsgUtil {
    /**
     * 发送位置信息
     *
     * @param latitude
     * @param longitude
     * @param locationAddress
     */
    public void sendLocationMsg(double latitude, double longitude, String locationAddress) {
        String msg = "#LOC" + locationAddress + "{" + latitude + ","
                + longitude + "}" + "COL#";


    }

    /**
     * 发送GIF
     *
     * @param msg
     */
    public static void sendGIF(String msg) {
        String messageContent = "#EMO" + msg.substring(0, msg.length() - 4) + ".gif" + "OME#";
    }

}
