package com.chewuwuyou.app.bean;

import java.io.Serializable;

public class FuWenBen implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private FWB_TYPE type = null;

	private float width = 0;

	private float height = 0;

	private String content = null;

	public FWB_TYPE getType() {
		return type;
	}

	public void setType(FWB_TYPE type) {
		this.type = type;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
