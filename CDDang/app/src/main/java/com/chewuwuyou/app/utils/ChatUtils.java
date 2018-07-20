package com.chewuwuyou.app.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ParseException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.PersonalInfo;
import com.chewuwuyou.app.ui.PersonalHomeActivity2;
import com.chewuwuyou.eim.model.IMMessage;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:聊天工具类
 * @author:yuyong
 * @date:2015-4-26下午2:13:50
 * @version:1.2.1
 */
@SuppressLint("SimpleDateFormat")
public class ChatUtils {
    /**
     * 判断发送消息的类型 包含图片、gif动图、语音、表情文本、普通文本等
     *
     * @param messageContent
     * @return
     */
    public static int getChatMessageType(String messageContent) {
        if (messageContent.contains("#EMO") && messageContent.contains("OME#")) {
            // gif图片
            return Constant.CHAT_MESSAGE_TYPE.GIF_IMG;
        } else if (messageContent.contains("#IMG")
                && messageContent.contains("GMI#")) {
            // 普通图片
            return Constant.CHAT_MESSAGE_TYPE.IMAGE;
        } else if (messageContent.contains("[") && messageContent.contains("]")) {
            // 包含表情
            return Constant.CHAT_MESSAGE_TYPE.YWZ_TXT;
        } else if ((messageContent.contains("sdcard") && messageContent
                .contains("Record"))
                || (messageContent.contains("#VOI") && messageContent
                .contains("IOV#"))) {
            // 语音/sdcard/MyVoiceForder/Record
            return Constant.CHAT_MESSAGE_TYPE.VOICE;
        } else if (messageContent.contains("#LOC")
                && messageContent.contains("COL#")) {
            // 位置
            return Constant.CHAT_MESSAGE_TYPE.LOCATION;
        } else {
            // 普通文本
            return Constant.CHAT_MESSAGE_TYPE.TXT;
        }
    }

    /**
     * 设置位置信息 messageContent:#LOC美年广场{30.025,128.1545}COL#
     *
     * @param messageContent
     * @param mLocationTV
     */
    public static void setLocation(String messageContent, TextView mLocationTV) {
        try {
            int start = messageContent.indexOf("C") + 1;
            int end = messageContent.indexOf("{");
            mLocationTV.setText(messageContent.substring(start, end));
        } catch (Exception e) {
            mLocationTV.setText(messageContent);
        }

    }

    /**
     * 根据content设置图片显示 messageContent:#IMG12312.jpegGMI#
     *
     * @param messageContent
     * @param mIV
     */
    public static void setImage(Context context, String messageContent,
                                ImageView mIV) {
        try {
            String url = messageContent.substring(5,
                    messageContent.length() - 4);
            MyLog.i("YUY", "--------图片地址------" + url);
            FinalBitmap.create(context).display(mIV,
                    ServerUtils.getServerIP(url));
            // ImageUtils.showImg(mIV, NetworkUtil.IMAGE_BASE_URL + url,
            // R.drawable.ad1);
        } catch (Exception e) {
        }

    }

    /**
     * 获取位置信息的经纬度
     *
     * @param messageContent
     * @return
     */
    public static double[] getLocationLat(String messageContent) {
        double[] latlng = new double[2];
        int latStart = messageContent.indexOf("{") + 1;
        int latEnd = messageContent.indexOf(",");
        double lat = Double.valueOf(messageContent.substring(latStart, latEnd));
        int lngStart = messageContent.indexOf(",") + 1;
        int lngEnd = messageContent.indexOf("}");
        double lng = Double.valueOf(messageContent.substring(lngStart, lngEnd));
        latlng[0] = lat;
        latlng[1] = lng;
        MyLog.i("YUY", "---------获取的经纬度-------" + latlng[0] + "---" + latlng[1]);
        return latlng;
    }

    public static String setFileName() {
        return CacheTools.getUserData("user") + System.currentTimeMillis()
                + ".jpg";

    }

    // #VOIISD:/chatVoice/20150514152514477.m4a{4}IOV#
    // public static String getVoiceUrl(String messageContent) {
    // try {
    // int voiceUrlStart = messageContent.indexOf(":") + 1;
    // int voiceUrlEnd = messageContent.indexOf("{");
    //
    // return NetworkUtil.IMAGE_BASE_URL
    // + messageContent.substring(voiceUrlStart, voiceUrlEnd);
    // } catch (Exception e) {
    //
    // }
    // return messageContent;
    // }

    /**
     * 计算两个时间的间隔天数
     *
     * @param smdate
     * @param bdate
     * @return
     * @throws ParseException
     * @throws java.text.ParseException
     */
    public static boolean stringDaysBetween(String smdate, String bdate)
            throws ParseException, java.text.ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_minutes = (time2 - time1) / (1000 * 60);
        MyLog.i("YUY", "----------两次消息的间隔时间----" + between_minutes);
        if (between_minutes < 3) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 计算两个时间的间隔天数
     *
     * @param smdate
     * @param bdate
     * @return
     */
    public static boolean daysBetween(long smdate, long bdate) {
        return (bdate - smdate) / (1000 * 60) < 3;
    }

    public static void showChatMsg(Context context, String messageContent,
                                   Map<String, String> yanWenZiMap, TextView mMsgTV) {
        if (messageContent.contains("[") && messageContent.contains("]")) {
            // 换掉新的表情包
            // ChatInputUtils.displayBigFacePic(context, messageContent,
            // yanWenZiMap);
            mMsgTV.setText(ChatInputUtils.displayBigFacePic(context,
                    messageContent, yanWenZiMap));
        } else if (messageContent.contains("#LOC")
                && messageContent.contains("COL#")) {
            mMsgTV.setText("位置信息");
        } else if (messageContent.contains("#IMG")
                && messageContent.contains("GMI#")) {
            mMsgTV.setText("图片");

        } else if ((messageContent.contains("sdcard") && messageContent
                .contains("Record"))
                || (messageContent.contains("#VOI") && messageContent
                .contains("IOV#"))) {
            mMsgTV.setText("语音");
        } else if (messageContent.contains("#EMO")
                && messageContent.contains("OME#")) {
            mMsgTV.setText("图片");
        } else {
            mMsgTV.setText(messageContent);
        }

    }

    public static String returnMsg(String messageContent) {
        if (messageContent.contains("[") && messageContent.contains("]")) {
            return messageContent;
        } else if (messageContent.contains("#LOC")
                && messageContent.contains("COL#")) {
            return "位置信息";
        } else if (messageContent.contains("#IMG")
                && messageContent.contains("GMI#")) {
            return "图片";

        } else if (messageContent.contains("#EMO")
                && messageContent.contains("OME#")) {
            return "图片";
        } else if ((messageContent.contains("sdcard") && messageContent
                .contains("Record"))
                || (messageContent.contains("#VOI") && messageContent
                .contains("IOV#"))) {
            return "语音";
        } else if (messageContent.contains("#EMO")
                && messageContent.contains("OME#")) {
            return "图片";
        } else {
            return messageContent;
        }

    }

    /**
     * 聊天显示自己的头像
     *
     * @param iv
     */
    public static void disPlayMeImg(final Context context, final ImageView iv) {
        if (CacheTools.getUserData("headUrl") != null) {
            ImageLoader.getInstance().displayImage(
                    CacheTools.getUserData("headUrl"
                            + CacheTools.getUserData("userId")), iv);
        } else {
            NetworkUtil.postMulti(NetworkUtil.SELECT_PERSONAL_DATA, null,
                    new AjaxCallBack<String>() {
                        @Override
                        public void onSuccess(String t) {
                            super.onSuccess(t);
                            try {
                                JSONObject jo = new JSONObject(t.toString());
                                MyLog.i("YUY", "请求用户的个人信息 =" + jo.toString());
                                PersonalInfo mPersonalInfo = PersonalInfo
                                        .parse(jo.getString("data"));
                                CacheTools.setUserData(
                                        "headUrl"
                                                + CacheTools
                                                .getUserData("userId"),
                                        mPersonalInfo.getUrl());
                                if (mPersonalInfo != null) {
                                    ImageUtils.displayImage(
                                            mPersonalInfo.getUrl(), iv, 10,
                                            R.drawable.user_fang_icon,
                                            R.drawable.user_fang_icon);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
        iv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PersonalHomeActivity2.class);
                intent.putExtra("userId",
                        Integer.parseInt(CacheTools.getUserData("userId")));
                intent.putExtra("skip", 1);
                context.startActivity(intent);
            }
        });

    }

    /**
     * 显示他人的头像
     *
     * @param message
     * @param iv
     * @param toChat
     */
    public static void disPlayHisImg(final Context context, IMMessage message,
                                     final ImageView iv, String toChat) {

        if (message.getFromSubJid().equals("xiaodang@iz232jtyxeoz")) {
            iv.setImageResource(R.drawable.xiaodangdi);
        } else if (message.getFromSubJid().equals("xiaoding@iz232jtyxeoz")) {
            iv.setImageResource(R.drawable.xiaodangmei);
        } else {
            final String ids = toChat.split("@")[0];
            AjaxParams params = new AjaxParams();
            params.put("ids", ids);
            // TODO缓存头像的地址
            // if (CacheTools.getUserData("head" + ids) != null) {// 获取缓存头像
            // ImageLoader.getInstance().displayImage(CacheTools.getUserData("head"
            // + ids), iv);
            // } else {
            NetworkUtil.postMulti(NetworkUtil.GET_CHAT_USER_INFO, params,
                    new AjaxCallBack<String>() {
                        @Override
                        public void onSuccess(String t) {
                            super.onSuccess(t);
                            try {
                                JSONObject jo = new JSONObject(t);
                                if (jo.getInt("result") == 1) {
                                    JSONArray jsonArray = new JSONArray(jo
                                            .getJSONArray("data").toString());
                                    // 根据用户ID缓存头像的地址
                                    // CacheTools.setUserData("head" + ids,
                                    // jsonArray.getJSONObject(0).getString("url"));
                                    ImageUtils.displayImage(jsonArray
                                                    .getJSONObject(0).getString("url"),
                                            iv, 10, R.drawable.user_fang_icon,
                                            R.drawable.user_fang_icon);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                MyLog.i("YUY", "进入异常");
                            }

                        }
                    });
            // }
            iv.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,
                            PersonalHomeActivity2.class);
                    intent.putExtra("userId", Integer.parseInt(ids));
                    context.startActivity(intent);
                }
            });
        }
    }
}
