package com.chewuwuyou.app.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.List;

/**
 * @describe:商家实体
 * @author:liuchun
 */
public class TrafficBusinessListBook implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String businessName;// 名称
    private double fees;// 规费
    private double servicePrice;// 服务费
    private String id;// 商家ID
    private int star;// 评分
    private String userId;// 商家Id
    private String images;// 商家头像
    private int isfavorite;// 是否收藏 1为收藏，0为未收藏
    private String url;
    private String realName;
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public double getFees() {
        return fees;
    }

    public void setFees(double fees) {
        this.fees = fees;
    }

    public double getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(double servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getIsfavorite() {
        return isfavorite;
    }

    public void setIsfavorite(int isfavorite) {
        this.isfavorite = isfavorite;
    }

    public static TrafficBusinessListBook parse(String serviceProJson) {
        TrafficBusinessListBook myBusi = null;
        if (serviceProJson != null) {
            Gson g = new Gson();
            myBusi = g.fromJson(serviceProJson, TrafficBusinessListBook.class);
        }
        return myBusi;
    }

    public static List<TrafficBusinessListBook> parseList(String serviceProJson) {
        List<TrafficBusinessListBook> myBusins = null;
        if (serviceProJson != null) {
            Gson g = new Gson();
            myBusins = g.fromJson(serviceProJson,
                    new TypeToken<List<TrafficBusinessListBook>>() {
                    }.getType());
        }
        return myBusins;
    }
}
