package com.chewuwuyou.app.utils;

import android.text.TextUtils;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:服务器集群的工具类
 * @author:yuyong
 * @date:2015-5-13上午10:23:27
 * @version:1.2.1
 */
public class ServerUtils {
    // public static Map<String, String> mServerMap;

    // @SuppressWarnings("static-access")
    // public static void getServer() {
    // new NetworkUtil().postMulti(NetworkUtil.GET_SERVERS, null,
    // new AjaxCallBack<String>() {
    // @Override
    // public void onSuccess(String t) {
    // super.onSuccess(t);
    // try {
    // JSONObject jo = new JSONObject(t);
    // MyLog.i("YUY", "jo = "+jo.toString());
    // if (jo.getInt("result") == 1) {
    // CacheTools.setUserData("servers",
    // jo.getString("data"));
    // mServerMap = JsonUtil.getMapForJson(jo
    // .getString("data"));
    //
    // }
    // } catch (JSONException e) {
    // e.printStackTrace();
    // }
    //
    // }
    //
    // @Override
    // public void onFailure(Throwable t, int errorNo,
    // String strMsg) {
    // super.onFailure(t, errorNo, strMsg);
    // }
    // });
    //
    // }

    /**
     * 服务器集群 即时通讯获取图片、语音地址 遵守即时通讯的协议
     *
     * @param serverIP
     * @return
     */
    public static String getChatServerIP(String serverIP) {
        // #IMGISD:/chatImg/20150513172447668.jpgGMI#
        // if (mServerMap == null) {
        // getServer();
        // }
        // if (mServerMap != null) {//http://image.cddang.com
        //#IMG/chatImg/201606281913325672.jpegGMI#
        if (serverIP.contains("#IMG") && serverIP.contains("GMI#")) {
            return tongYiUrl(serverIP.replace("#IMG", "").replace("GMI#", ""));
        } else {
            return tongYiUrl(serverIP);
        }
        // }
        // return null;
    }

    /**
     * 用于获取非即时通讯地方的图片ip
     *
     * @param serverIP
     * @return
     */
    public static String getServerIP(String serverIP) {
        // if (mServerMap == null) {
        // getServer();
        // }
        if (TextUtils.isEmpty(serverIP)) {
            return "";
        }
        return tongYiUrl(serverIP);
    }

    /**
     * 获取即时通讯语音地址
     *
     * @param serverIP
     * @return
     */
    public static String getVoiceServiceIP(String serverIP) {
        // #VOIISD:/chatVoice/20150514141159230.m4a{1}IOV#
//		 if (mServerMap == null) {
//		 getServer();
//		 }
//		 if (serverIP.contains(":")) {
//		 int start = serverIP.indexOf(":") + 1;
//		 int end = serverIP.indexOf("{");
//		 if (mServerMap == null) {
//		 getServer();
//		 }
//		 return "http://"
//		 + mServerMap.get(serverIP.split(":")[0].substring(4))
//		 + "/spng" + serverIP.substring(start, end);
//		 } else {
//		 return "http://" + mServerMap.get("VSD") + "/spng"
//		 + serverIP.substring(7, serverIP.indexOf("{"));
//		 }
//		 }
//		http://image.cddang.com/spng//chatVoice/2016071916414519533589.m4a{4}
        if (serverIP.contains("#VOI") && serverIP.contains("IOV#")) {
            int end = serverIP.replace("#VOI", "").replace("IOV#", "").indexOf("{");
            return tongYiUrl(serverIP.replace("#VOI", "").replace("IOV#", "").substring(0, end));
        } else {
            return tongYiUrl(serverIP);
        }

    }

    private static String tongYiUrl(String serverIP) {
        if (serverIP.contains("http") || serverIP.contains("https")) {
            return serverIP;
        } else if (serverIP.contains(":")) {
            String[] divides = serverIP.split(":");
            return "http://image.cddang.com/spng/" + divides[1];
        } else if (NetworkUtil.BASE_URL.contains("101.204.230.251")) {//预生产环境显示图片
            return "http://101.204.230.251/spng/" + serverIP;
        }else if(NetworkUtil.BASE_URL.contains("192.168.8.4")){//预生产测试环境
            return "http://192.168.8.4:8083/mhwcw/";
        } else {
            return "http://image.cddang.com/spng/" + serverIP;
        }
    }

    /**
     * 返回缓存在本地的文件地址
     *
     * @param serverIP
     * @return
     */
    public static String getFileLocalPath(String serverIP) {
        String url;
        if (serverIP.contains("http") || serverIP.contains("https")) {
            url = serverIP;
        } else if (serverIP.contains(":")) {
            String[] divides = serverIP.split(":");
            url = "http://image.cddang.com" + "/spng" + divides[1];
        } else {
            url = "http://image.cddang.com" + "/spng" + serverIP;
        }
        return "file://"
                + ImageLoader.getInstance().getDiscCache().get(url).getPath();

    }

    /**
     * 获取图片的显示地址
     *
     * @return
     */
    public static String getImgServer(String serverIP) {
        if (serverIP.contains("#IMG") && serverIP.contains("GMI#")) {//聊天发送图片地址的格式
            return tongYiUrl(serverIP.replace("#IMG", "").replace("GMI#", ""));
        } else if (serverIP.contains("http") || serverIP.contains("https")) {//普通图片地址返回
            return serverIP;
        } else if (serverIP.contains(":")) {//区分公网集群的图片地址返回
            return "http://image.cddang.com/spng/" + serverIP.split(":")[1];
        } else if (NetworkUtil.BASE_URL.contains("101.204.230.251")) {//预生产环境显示图片
            return "http://101.204.230.251/spng/" + serverIP;
        } else {//其他情况
            return "http://image.cddang.com/spng/" + serverIP;
        }
    }
}
