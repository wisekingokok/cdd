package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CarModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String parent;
	private String brand;
	private String fac;
	private List<Serie> series;

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getFac() {
		return fac;
	}

	public void setFac(String fac) {
		this.fac = fac;
	}

	public List<Serie> getSeries() {
		return series;
	}

	public void setSeries(List<Serie> series) {
		this.series = series;
	}

	public static CarModel parse(String brandJson) {
		Gson g = new Gson();
		CarModel brand = g.fromJson(brandJson, CarModel.class);
		return brand;
	}

	public static List<CarModel> parseBrands(String brandJsons) {
		Gson g = new Gson();
		List<CarModel> brands = g.fromJson(brandJsons,
				new TypeToken<List<CarModel>>() {
				}.getType());
		return brands;
	}
}
