package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @describe:板块实体
 * @author:XH
 * @version 
 * @created:
 */
public class BanKuaiItem implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;//论坛id
    private String title;//类型
	private String photo;//图标
	private int count;//帖数
	private int today;
	private String subtile;

	
    public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getPhoto() {
		return photo;
	}


	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}


	public int getToday() {
		return today;
	}


	public void setToday(int today) {
		this.today = today;
	}


	public String getSubtile() {
		return subtile;
	}


	public void setSubtile(String subtile) {
		this.subtile = subtile;
	}


	public static List<BanKuaiItem> parseList(String json) {
        Gson g = new Gson();
        List<BanKuaiItem> list = g.fromJson(json,
                new TypeToken<List<BanKuaiItem>>() {
                }.getType());
        return list;
    }
}
