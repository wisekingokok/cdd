package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 车辆业务相关材料
 * 
 * @author Administrator
 * 
 */
public class CarSpiecification implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	private String content;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public static List<CarSpiecification> parseList(String CarSpecificationJson) {
		Gson g = new Gson();
		List<CarSpiecification> citys = g.fromJson(CarSpecificationJson,
				new TypeToken<List<CarSpiecification>>() {
				}.getType());
		return citys;
	}
}
