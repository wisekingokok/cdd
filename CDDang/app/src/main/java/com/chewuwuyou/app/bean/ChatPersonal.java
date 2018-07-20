package com.chewuwuyou.app.bean;

import java.io.Serializable;

/**
 * @describe:用户信息
 * @author:liuchun
 */
public class ChatPersonal implements Serializable {

	private boolean isSelected;//是否选中
	private String name;//名称
	private String img;//头像

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}



}
