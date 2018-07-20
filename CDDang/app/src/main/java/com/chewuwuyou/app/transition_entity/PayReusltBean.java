package com.chewuwuyou.app.transition_entity;

import java.io.Serializable;

/**
 * 支付结果
 * Created by yuyong on 16/10/19.
 */

public class PayReusltBean implements Serializable{


    /**
     * code : 0
     * message :
     * data : 发送成功！
     */

    private int code;
    private String message;
    private String data;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
