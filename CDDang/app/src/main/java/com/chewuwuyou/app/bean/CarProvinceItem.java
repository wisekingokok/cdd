package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CarProvinceItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int province_id;// 2,
	private String province_short_name;// "豫"
	private String province_name;// "河南"
	private List<CarCityItem> citys;

	public int getProvince_id() {
		return province_id;
	}

	public void setProvince_id(int province_id) {
		this.province_id = province_id;
	}

	public String getProvince_short_name() {
		return province_short_name;
	}

	public void setProvince_short_name(String province_short_name) {
		this.province_short_name = province_short_name;
	}

	public String getProvince_name() {
		return province_name;
	}

	public void setProvince_name(String province_name) {
		this.province_name = province_name;
	}

	public List<CarCityItem> getCitys() {
		return citys;
	}

	public void setCitys(List<CarCityItem> citys) {
		this.citys = citys;
	}

	public static List<CarProvinceItem> parseList(String json) {
		Gson g = new Gson();
		List<CarProvinceItem> list = g.fromJson(json, new TypeToken<List<CarProvinceItem>>() {
		}.getType());
		return list;
	}

	public static CarProvinceItem parse(String tieJson) {
		CarProvinceItem item = null;
		if (tieJson != null) {
			Gson g = new Gson();
			item = g.fromJson(tieJson, CarProvinceItem.class);
		}
		return item;
	}
}
