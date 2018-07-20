package com.chewuwuyou.app.bean;

import java.io.Serializable;

public class YueInvolverItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;//3485,
    private String url;// "/upload/20150312164253400.jpg"
    private String name;//"\u9759\u9759"
    private String sex;

    
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
