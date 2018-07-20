package com.chewuwuyou.app.bean;

import java.io.Serializable;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:
 * @author:yuyong
 * @date:2015-5-16下午3:49:51
 * @version:1.2.1
 */
public class CarAssistantServiceItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int imgResId;
	private String nameTV;

	public int getImgResId() {
		return imgResId;
	}

	public void setImgResId(int imgResId) {
		this.imgResId = imgResId;
	}

	public String getNameTV() {
		return nameTV;
	}

	public void setNameTV(String nameTV) {
		this.nameTV = nameTV;
	}

}
