package com.chewuwuyou.rong.bean;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class GroupList {

    private int code;
    private String message;
    private List<WholeGroup> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<WholeGroup> getData() {
        return data;
    }

    public void setData(List<WholeGroup> data) {
        this.data = data;
    }

    public static List<GroupList> parseList(String json) {
        Gson g = new Gson();
        List<GroupList> list = g.fromJson(json, new TypeToken<List<GroupList>>() {
        }.getType());
        return list;
    }

    public static GroupList parse(String tieJson) {
        GroupList item = null;
        if (tieJson != null) {
            Gson g = new Gson();
            item = g.fromJson(tieJson, GroupList.class);
        }
        return item;
    }
}
