package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class WeizhangChildItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;// 违章记录编号 2155626
	private int car_id;// 汽车编号  1497817
	private String status;// 违章记录是否处理（"Y":已处理，"N":未处理） "N"
	private int fen;// 违章扣分 0
	private String officer;// "重庆市公安局交通巡逻警察总队丰都大队",
	private String occur_date;// 违章时间
	private String occur_area;// 违章所在地
	private int city_id;// 违章所在的城市（与省市配置对应） 145 
	private int province_id;// 违章所在的省份（与省市配置对应） 10
	private String code;// 违章代码 1047
	private String info;// 违章内容  机动车未按规定临时停车
	private int money;// 罚款金额  0
	private String archive;// 证书编号
	private String city_name;// "恩施"
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCar_id() {
		return car_id;
	}
	public void setCar_id(int car_id) {
		this.car_id = car_id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getFen() {
		return fen;
	}
	public void setFen(int fen) {
		this.fen = fen;
	}
	public String getOfficer() {
		return officer;
	}
	public void setOfficer(String officer) {
		this.officer = officer;
	}
	public String getOccur_date() {
		return occur_date;
	}
	public void setOccur_date(String occur_date) {
		this.occur_date = occur_date;
	}
	public String getOccur_area() {
		return occur_area;
	}
	public void setOccur_area(String occur_area) {
		this.occur_area = occur_area;
	}
	public int getCity_id() {
		return city_id;
	}
	public void setCity_id(int city_id) {
		this.city_id = city_id;
	}
	public int getProvince_id() {
		return province_id;
	}
	public void setProvince_id(int province_id) {
		this.province_id = province_id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public String getArchive() {
		return archive;
	}
	public void setArchive(String archive) {
		this.archive = archive;
	}
	public String getCity_name() {
		return city_name;
	}
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	
	public static List<WeizhangChildItem> parseList(String json) {
		Gson g = new Gson();
		List<WeizhangChildItem> list = g.fromJson(json, new TypeToken<List<WeizhangChildItem>>() {
		}.getType());
		return list;
	}

	public static WeizhangChildItem parse(String tieJson) {
		WeizhangChildItem item = null;
		if (tieJson != null) {
			Gson g = new Gson();
			item = g.fromJson(tieJson, WeizhangChildItem.class);
		}
		return item;
	}

}
