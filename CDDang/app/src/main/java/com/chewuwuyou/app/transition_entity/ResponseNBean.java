package com.chewuwuyou.app.transition_entity;

/**
 * Created by xxy on 2016/8/15 0015.
 */
public class ResponseNBean<T> {


    /**
     * result : 0
     * data : {"errorCode":2,"errorMessage":"用户名或密码错误"}
     */

    private int code;
    private String message;
    private T data;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
