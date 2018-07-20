package com.chewuwuyou.app.bean;

import java.io.Serializable;

/**
 * Created by yuyong on 2016/9/10.
 */
public class PoiAddress implements Serializable {
    private String imgUrl;//图片地址
    private String poiAddress;//基地址
    private String poiAddressDel;//详细地址
    private boolean isSelect;//是否选中
    private double lat;//经度
    private double lng;//纬度

    public PoiAddress(String imgUrl, String poiAddress, String poiAddressDel, boolean isSelect,double lat,double lng) {
        this.imgUrl = imgUrl;
        this.poiAddress = poiAddress;
        this.poiAddressDel = poiAddressDel;
        this.isSelect = isSelect;
        this.lat=lat;
        this.lng=lng;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getPoiAddress() {
        return poiAddress;
    }

    public void setPoiAddress(String poiAddress) {
        this.poiAddress = poiAddress;
    }

    public String getPoiAddressDel() {
        return poiAddressDel;
    }

    public void setPoiAddressDel(String poiAddressDel) {
        this.poiAddressDel = poiAddressDel;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
