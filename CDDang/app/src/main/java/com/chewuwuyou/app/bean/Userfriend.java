package com.chewuwuyou.app.bean;

import com.chewuwuyou.rong.bean.WholeGroup;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.List;

/**
 * Created by CLOUD on 2016/9/14.
 */
public class Userfriend implements Serializable {


    /**
     * userId : 14036
     * name : 徐州彭浩
     * portraitUri : ftest
     */
    private int id;

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public boolean isOpen;//滑动是否展开

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String userId;
    private String name;//这是备注 。。。。。
    private String portraitUri;
    private boolean isSelected;//是否选中
    private String nickname;//
    private String phone;

    private int isFriend;
    private String remarks;

    public int getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(int isFriend) {
        this.isFriend = isFriend;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Userfriend() {
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Userfriend(String userId, String name) {
        this.userId = userId;
        this.name = name;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public static List<Userfriend> parseList(String chatUserJson) {
        Gson g = new Gson();
        List<Userfriend> users = g.fromJson(chatUserJson,
                new TypeToken<List<Userfriend>>() {
                }.getType());
        return users;
    }


}
