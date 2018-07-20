package com.chewuwuyou.app.transition_entity;

import com.chewuwuyou.rong.bean.WholeGroup;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yuyong on 16/10/13.
 */

public class BaseListBean implements Serializable{

    private String code;
    private String message;
    private List<WholeGroup> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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
}
