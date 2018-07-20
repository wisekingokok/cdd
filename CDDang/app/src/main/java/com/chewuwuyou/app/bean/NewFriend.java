package com.chewuwuyou.app.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.List;

/**
 * Created by CLOUD on 2016/9/18.
 */
public class NewFriend implements Serializable {


    private String userTag;

    public static final int AGREE = 1;//同意
    public static final int REFUSE = 2;//拒绝
    private int id;//afinal数据库主键
    private String userId;
    private String name;
    private String portraitUri;
    private String remarks;
    private String phone;
    private String token;
    private String nickname;


    public String getUserTag() {
        return userTag;
    }

    public void setUserTag(String userTag) {
        this.userTag = userTag;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private int addState;//整形 0是指默认值,没被任何操作，1是同意，2是拒绝

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortraitUri() {
        return portraitUri;
    }

    public void setPortraitUri(String portraitUri) {
        this.portraitUri = portraitUri;
    }

    public int getAddState() {
        return addState;
    }

    public void setAddState(int addState) {
        this.addState = addState;
    }

    public static List<NewFriend> parseList(String s) {
        List<NewFriend> friends = null;
        if (s != null) {
            Gson g = new Gson();
            friends = g.fromJson(s, new TypeToken<List<NewFriend>>() {
            }.getType());
        }
        return friends;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
