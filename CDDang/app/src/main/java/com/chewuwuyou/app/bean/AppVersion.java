package com.chewuwuyou.app.bean;

import com.google.gson.Gson;

/**
 * 版本更新信息
 *
 * @author yuyong
 */
public class AppVersion {

    // jo.put("ver", ver.getVer());//版本
    // jo.put("isForce", ver.getIsForce());//是否强制更新
    // jo.put("log", ver.getLog());//提示信息
    // jo.put("url", ver.getUrl());//

    // jo.put("imageURL", sm.getImageURL());//显示图片
    // jo.put("linkURL", sm.getLinkURL());//链接地址
    // jo.put("publishTime", sm.getPublishTime());//发布时间
    // jo.put("toTime", sm.getToTime());//到期时间
    private String ver;
    private int isForce;
    private String log;
    private String url;
    private String imageURL;
    private String linkURL;
    private String publishTime;
    private String toTime;
    private String sizeM;

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public int getIsForce() {
        return isForce;
    }

    public void setIsForce(int isForce) {
        this.isForce = isForce;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getLinkURL() {
        return linkURL;
    }

    public void setLinkURL(String linkURL) {
        this.linkURL = linkURL;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getSizeM() {
        return sizeM;
    }

    public void setSizeM(String sizeM) {
        this.sizeM = sizeM;
    }

    public static AppVersion parse(String avJson) {
        AppVersion av = null;
        if (avJson != null) {
            Gson g = new Gson();
            av = g.fromJson(avJson, AppVersion.class);
        }
        return av;
    }
}
