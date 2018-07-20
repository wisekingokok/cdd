package com.chewuwuyou.app.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.List;

/**
 * Created by CLOUD on 2016/9/13.
 */
public class SearchUser implements Serializable {


    /**
     * userId : 11187
     * name : 1823
     * portraitUri :
     * token :
     * isBusiness : 0
     * remarks :
     * phone : 18771449534
     * nickname :
     * isFriend : 0   0不是好友，1就是好友
     */

    private int userId;
    private String name;
    private String portraitUri;
    private String token;
    private int isBusiness;
    private String remarks;
    private String phone;
    private String nickname;
    private int isFriend;

    public static List<SearchUser> parseList(String SearchUserJson) {
        List<SearchUser> searchUsers = null;
        if (SearchUserJson != null) {
            Gson g = new Gson();
            searchUsers = g.fromJson(SearchUserJson, new TypeToken<List<SearchUser>>() {
            }.getType());
        }
        return searchUsers;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getIsBusiness() {
        return isBusiness;
    }

    public void setIsBusiness(int isBusiness) {
        this.isBusiness = isBusiness;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(int isFriend) {
        this.isFriend = isFriend;
    }
}
