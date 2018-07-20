package com.chewuwuyou.app.transition_entity;

/**
 * Created by xxy on 2016/8/15 0015.
 */
public class ResponseBean<T> {


    /**
     * result : 0
     * data : {"errorCode":2,"errorMessage":"用户名或密码错误"}
     */

    private int result;

    private T data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
