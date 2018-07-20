package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class WeizhangGroupItem implements Serializable {
	/*
	 * 	"status": 2001,
	"total_score": 0,
	"total_money": 100,
	"count": 2,
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static WeizhangGroupItem parse(String json) {
		WeizhangGroupItem item = null;
		if (json != null) {
			Gson g = new Gson();
			item = g.fromJson(json, WeizhangGroupItem.class);
		}
		return item;
	}
	private String city_name;
	private int status;
	private int total_score;
	private double total_money;
	private int count;
	private List<WeizhangChildItem> historys;
	
	public String getCity_name() {
		return city_name;
	}
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getTotal_score() {
		return total_score;
	}
	public void setTotal_score(int total_score) {
		this.total_score = total_score;
	}
	public double getTotal_money() {
		return total_money;
	}
	public void setTotal_money(double total_money) {
		this.total_money = total_money;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}

	public List<WeizhangChildItem> getHistorys() {
		return historys==null?new ArrayList<WeizhangChildItem>():historys;
	}
	public void setHistorys(List<WeizhangChildItem> historys) {
		this.historys = historys;
	}
	
}
