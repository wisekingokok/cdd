package com.chewuwuyou.app.transition_entity;

import java.io.Serializable;

/**
 * 仅仅用于代码范例
 *
 * Created by Yogi on 16/9/24.
 */

public class SimpleEntity implements Serializable {
    public String lon;
    public String level;
    public String address;
    public String cityName;
    public String alevel;
    public String lat;

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAlevel() {
        return alevel;
    }

    public void setAlevel(String alevel) {
        this.alevel = alevel;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}
