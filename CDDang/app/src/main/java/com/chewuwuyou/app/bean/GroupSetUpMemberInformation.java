package com.chewuwuyou.app.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by Administrator on 2016/9/14.
 */
public class GroupSetUpMemberInformation {

    private int code;
    private String message;
    private List<GroupSetUpMemberInformationEr> data;


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

    public List<GroupSetUpMemberInformationEr> getData() {
        return data;
    }

    public void setData(List<GroupSetUpMemberInformationEr> data) {
        this.data = data;
    }


    public static GroupSetUpMemberInformation parse(String brandJson) {
        Gson g = new Gson();
        GroupSetUpMemberInformation brand = g.fromJson(brandJson, GroupSetUpMemberInformation.class);
        return brand;
    }

    public static List<GroupSetUpMemberInformation> parseList(String personalInfolistJson) {
        Gson g = new Gson();
        List<GroupSetUpMemberInformation> personalInfos = g.fromJson(personalInfolistJson,
                new TypeToken<List<GroupSetUpMemberInformation>>() {
                }.getType());
        return personalInfos;
    }
}
