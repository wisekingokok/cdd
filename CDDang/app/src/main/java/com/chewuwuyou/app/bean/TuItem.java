package com.chewuwuyou.app.bean;

import java.io.Serializable;

public class TuItem implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int id;
    private String url;// 图片地址

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
