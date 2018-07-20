package com.chewuwuyou.app.bean;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

/**
 * Created by CLOUD on 2016/9/14.
 */
public class SearchGroup implements Serializable {


    private int code;
    private String message;
    private List<SearchGroupData> data;

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

    public List<SearchGroupData> getData() {
        return data;
    }

    public void setData(List<SearchGroupData> data) {
        this.data = data;
    }
    public static SearchGroup parse(String servicePriceJson) {
        Gson g = new Gson();
        SearchGroup sp = g.fromJson(servicePriceJson, SearchGroup.class);
        return sp;
    }
}
