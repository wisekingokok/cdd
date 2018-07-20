package com.chewuwuyou.app.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.List;

/**
 * 地区
 *
 * @author zengys
 */
public class AreaInfo implements Serializable {

    private Integer id;// 地区ID
    private Integer provinceId;// 省ID
    private Integer cityId;// 市ID
    private String provinceName;// 第一级 (省)地名
    private String provinceRemark;// 对第一级地区的说明,主要是区别 省、直辖市、自治区
    private String cityName;// 第二级(市)地名
    private String districtName;// 第三级(区)地名

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceRemark() {
        return provinceRemark;
    }

    public void setProvinceRemark(String provinceRemark) {
        this.provinceRemark = provinceRemark;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public static List<AreaInfo> parseList(String userJson) {
        List<AreaInfo> csts = null;
        if (userJson != null) {
            Gson g = new Gson();
            csts = g.fromJson(userJson, new TypeToken<List<AreaInfo>>() {
            }.getType());
        }
        return csts;
    }
}
