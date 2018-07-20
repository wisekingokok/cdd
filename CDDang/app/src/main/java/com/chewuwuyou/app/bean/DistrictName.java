package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class DistrictName implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int provinceId;
	private int id;
	private String provinceRemark;
	private String provinceName;
	private String districtName;
	private int cityId;
	private String cityName;
	public int getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProvinceRemark() {
		return provinceRemark;
	}
	public void setProvinceRemark(String provinceRemark) {
		this.provinceRemark = provinceRemark;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	public static DistrictName parse(String jsonobj) {
		Gson g = new Gson();
		DistrictName districtname = g.fromJson(jsonobj, DistrictName.class);
		return districtname;
	}

	public static List<DistrictName> parses(String jsonarr) {
		List<DistrictName> districtnames = null;
		Gson g = new Gson();
		districtnames = g.fromJson(jsonarr, new TypeToken<List<DistrictName>>() {
		}.getType());
		return districtnames;
	}
	
}
