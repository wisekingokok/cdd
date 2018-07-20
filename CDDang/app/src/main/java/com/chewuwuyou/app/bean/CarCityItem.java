package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CarCityItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int  city_id;//int	城市对应的ID
	private String city_name;//int	城市名称
	private String car_head;// string	车牌头前两位
	private int  engineno;//int	需要输入发动机号位数（"-1"：全部输入，"0"：不需要输入，其他的显示几位输入发动机号后面几位）
	private int classno;// int	需要输入车架号位数（"-1"：全部输入，"0"：不需要输入，其他的显示几位输入车架号后面几位）
	private int registno;//int	需要输入证书编号位数（"-1"：全部输入，"0"：不需要输入，其他的显示几位输入证书编号后面几位）
	
	public int getEngineno() {
		return engineno;
	}
	public void setEngineno(int engineno) {
		this.engineno = engineno;
	}
	public int getCity_id() {
		return city_id;
	}
	public void setCity_id(int city_id) {
		this.city_id = city_id;
	}
	public String getCity_name() {
		return city_name;
	}
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	public int getClassno() {
		return classno;
	}
	public void setClassno(int classno) {
		this.classno = classno;
	}
	public String getCar_head() {
		return car_head;
	}
	public void setCar_head(String car_head) {
		this.car_head = car_head;
	}
	
	public static List<CarCityItem> parseList(String json) {
		Gson g = new Gson();
		List<CarCityItem> list = g.fromJson(json, new TypeToken<List<CarCityItem>>() {
		}.getType());
		return list;
	}

	public static CarCityItem parse(String tieJson) {
		CarCityItem item = null;
		if (tieJson != null) {
			Gson g = new Gson();
			item = g.fromJson(tieJson, CarCityItem.class);
		}
		return item;
	}
	
	public int getRegistno() {
		return registno;
	}
	
	public void setRegistno(int registno) {
		this.registno = registno;
	}
}
