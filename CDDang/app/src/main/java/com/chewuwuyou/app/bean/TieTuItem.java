package com.chewuwuyou.app.bean;

import java.io.Serializable;

public class TieTuItem implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String url;// 图片地址
    private float w;//图片宽度预处理
    private float h;//图片高度预处理
    
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public float getW() {
		return w;
	}

	public void setW(float w) {
		this.w = w;
	}

	public float getH() {
		return h;
	}

	public void setH(float h) {
		this.h = h;
	}
    
}
