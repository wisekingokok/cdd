package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

public class YiHang implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private float height = 0;

	private List<FuWenBen> content = null;

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public List<FuWenBen> getContent() {
		return content;
	}

	public void setContent(List<FuWenBen> content) {
		this.content = content;
	}

}
