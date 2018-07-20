package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CarEventAccess implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 别人的参加申请情况 -- 只有是自己发布的并且未过期的才会有
	private Integer id;// 申请ID
	private String name;// 申请人昵称
	private String url;// 申请人头像

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public static List<CarEventAccess> parserSeries(String json) {
		List<CarEventAccess> accesses = null;
		Gson g = new Gson();
		accesses = g.fromJson(json, new TypeToken<List<CarEventAccess>>() {
		}.getType());
		return accesses;
	}

}
