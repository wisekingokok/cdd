
package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @describe:系统通知
 * @author:yuyong
 * @version 1.1.0
 * @created:2015-1-14下午6:28:30
 */
public class SystemNotification implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String serviceType;
    private String title;
    private String content;



    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static SystemNotification parse(String notificatioinJson) {
        SystemNotification notifi = null;
        if (notificatioinJson != null) {
            Gson g = new Gson();
            notifi = g.fromJson(notificatioinJson, SystemNotification.class);
        }
        return notifi;
    }

    public static List<SystemNotification> parseList(String notificatioinJson) {
        List<SystemNotification> notifis = null;
        if (notificatioinJson != null) {
            Gson g = new Gson();
            notifis = g.fromJson(notificatioinJson,
                    new TypeToken<List<SystemNotification>>() {
                    }.getType());
        }
        return notifis;
    }
}
