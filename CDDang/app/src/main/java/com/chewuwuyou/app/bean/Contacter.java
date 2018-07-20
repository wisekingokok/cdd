package com.chewuwuyou.app.bean;

/**
 * Created by CLOUD on 2016/9/20.
 */
public class Contacter {

    String name;
    String tel;

    public Contacter(String name, String tel) {
        this.name = name;
        this.tel = tel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
