package com.chewuwuyou.app.bean;

import java.io.Serializable;

/**
 * @describe:爱车账目实体类
 * @author:yuyong
 * @version 1.1.0
 * @created:2014-10-23下午3:45:37
 */
public class CarChildBook implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String imgName;
	private String price;
	
	
	public CarChildBook(String name, String imgName, String price) {
		super();
		this.name = name;
		this.imgName = imgName;
		this.price = price;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImgName() {
		return imgName;
	}
	public void setImgName(String imgName) {
		this.imgName = imgName;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	
	
	
}
