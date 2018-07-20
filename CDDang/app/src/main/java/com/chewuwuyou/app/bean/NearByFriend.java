package com.chewuwuyou.app.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.List;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:
 * @author:yuyong
 * @date:2015-4-9下午2:22:18
 * @version:1.2.1
 */
public class NearByFriend implements Serializable {
    private static final long serialVersionUID = 1L;

    public static List<NearByFriend> parseFriends(String nearByFriendJson) {
        Gson g = new Gson();
        List<NearByFriend> nearByFriends = g.fromJson(nearByFriendJson, new TypeToken<List<NearByFriend>>() {
        }.getType());
        if (nearByFriends == null)
            return null;
        return nearByFriends;
    }


    private String ziji;
    private String noteName;
    private String isFriend;
    private int sex;
    private String nickName;
    private String userId;
    private int age;
    private double lng;
    private String url;
    private double lat;
    private String signature;

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getZiji() {
        return ziji;
    }

    public void setZiji(String ziji) {
        this.ziji = ziji;
    }

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    public String getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(String isFriend) {
        this.isFriend = isFriend;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
