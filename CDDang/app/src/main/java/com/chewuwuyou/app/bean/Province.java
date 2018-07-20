
package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Province implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
    private String province;
    private List<String> citys;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public List<String> getCitys() {
        return citys;
    }

    public void setCitys(List<String> citys) {
        this.citys = citys;
    }

    public static Province parse(String jsonobj) {
        Gson g = new Gson();
        Province province = g.fromJson(jsonobj, Province.class);
        return province;
    }

    public static List<Province> parses(String jsonarr) {
        List<Province> provinces = null;
        Gson g = new Gson();
        provinces = g.fromJson(jsonarr, new TypeToken<List<Province>>() {
        }.getType());
        return provinces;
    }
}
