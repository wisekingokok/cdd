package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/*
 * 注意：由于暂时无法获取数据，所以在assets中有个同名的json文件。可以联网获取数据就，记得删除json数据！
 * */
public class CityName implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String cityName;
	private int provinceId;
	private int citySort;
	private String provinceName;
	private String provinceRemark;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public int getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}

	public int getCitySort() {
		return citySort;
	}

	public void setCitySort(int citySort) {
		this.citySort = citySort;
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

	public static CityName parse(String jsonobj) {
		Gson g = new Gson();

		CityName cityname = g.fromJson(jsonobj, CityName.class);
		return cityname;
	}

	public static List<CityName> parses(String jsonarr) {
		List<CityName> citynames = null;
		Gson g = new Gson();
		citynames = g.fromJson(jsonarr, new TypeToken<List<CityName>>() {
		}.getType());
		return citynames;
	}

}
