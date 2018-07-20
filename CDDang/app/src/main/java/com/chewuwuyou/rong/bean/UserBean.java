package com.chewuwuyou.rong.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/7 0007.
 */
public class UserBean implements Serializable {
    private String id;

    public UserBean() {
    }

    public UserBean(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
