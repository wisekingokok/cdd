package com.chewuwuyou.app.transition_entity;

import com.chewuwuyou.app.bean.AllGroup;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yuyong on 16/10/13.
 */

public class EstablishGroupSuccess implements Serializable{

    private String code;
    private String message;
    private EstablishGroupData data;

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

    public EstablishGroupData getData() {
        return data;
    }

    public void setData(EstablishGroupData data) {
        this.data = data;
    }
}
