package com.chewuwuyou.app.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.List;

public class ChatUserInfo implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String id;
    private String userName;
    private String sex;
    private String nick;
    private String url;
    private String noteName;
    private String signature;

    public ChatUserInfo() {
    }

    public ChatUserInfo(String id, String nick) {
        this.id = id;
        this.nick = nick;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public static ChatUserInfo parse(String chatUserJson) {
        Gson g = new Gson();
        ChatUserInfo user = g.fromJson(chatUserJson, ChatUserInfo.class);
        return user;
    }

    public static List<ChatUserInfo> parseList(String chatUserJson) {
        Gson g = new Gson();
        List<ChatUserInfo> users = g.fromJson(chatUserJson,
                new TypeToken<List<ChatUserInfo>>() {
                }.getType());
        return users;
    }

}
