package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:商家拟定的所有服务的价格
 * @author:yuyong
 * @date:2015-10-9下午3:14:52
 * @version:1.2.1
 */
public class ProPrice implements Serializable {

	// "id": 1,
	// "fees": 10,
	// "price": 1,
	// "type": 2,
	// "projectName": "车辆年审",
	// "projectNum": 201

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private int fees;
	private int price;
	private int type;
	private String projectName;
	private int projectNum;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFees() {
		return fees;
	}

	public void setFees(int fees) {
		this.fees = fees;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public int getProjectNum() {
		return projectNum;
	}

	public void setProjectNum(int projectNum) {
		this.projectNum = projectNum;
	}

	public static ProPrice parse(String propriceJson) {
		ProPrice proPrice = null;
		if (propriceJson != null) {
			Gson g = new Gson();
			proPrice = g.fromJson(propriceJson, ProPrice.class);
		}
		return proPrice;
	}

	public static List<ProPrice> parseList(String propriceJson) {
		List<ProPrice> proPrices = null;
		if (propriceJson != null) {
			Gson g = new Gson();
			proPrices = g.fromJson(propriceJson,
					new TypeToken<List<ProPrice>>() {
					}.getType());
		}
		return proPrices;
	}

}
