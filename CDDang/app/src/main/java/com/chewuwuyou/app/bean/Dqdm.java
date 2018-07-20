package com.chewuwuyou.app.bean;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Dqdm {
	// "illegalCode": "130200",
	// "province": "河北省",
	// "city": "唐山市",
	// "area": "唐山市"

	private String illegalCode;
	private String province;
	private String city;
	private String area;

	public String getIllegalCode() {
		return illegalCode;
	}

	public void setIllegalCode(String illegalCode) {
		this.illegalCode = illegalCode;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public static Dqdm parse(String WzdmJson) {
		Dqdm o = null;
		if (WzdmJson != null) {
			Gson g = new Gson();
			o = g.fromJson(WzdmJson, Dqdm.class);
		}
		return o;
	}

	public static List<Dqdm> parseList(String WzdmJson) {
		List<Dqdm> Wzdms = null;
		if (WzdmJson != null) {
			Gson g = new Gson();
			Wzdms = g.fromJson(WzdmJson, new TypeToken<List<Dqdm>>() {
			}.getType());
		}
		return Wzdms;
	}
}
