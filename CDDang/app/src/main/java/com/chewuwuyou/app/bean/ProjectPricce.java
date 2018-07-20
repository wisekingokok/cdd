package com.chewuwuyou.app.bean;

import java.io.Serializable;

public class ProjectPricce implements Serializable {

	
	private static final long serialVersionUID = 1L;

	private String name;
	private String price;
    private String projectNum;
    private String type;
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getProjectNum() {
		return projectNum;
	}
	public void setProjectNum(String projectNum) {
		this.projectNum = projectNum;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

}
