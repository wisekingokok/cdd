package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:客服实体
 * @author:yuyong
 * @date:2015-6-8下午6:37:40
 * @version:1.2.1
 */
public class Service implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	  managId : 783;
//    nick : "\U5f20\U5f3a";
//    role : 1;
//    telephone : 15928914689;
//    url : "";
//    userId : 2809;
	private String nick;
	private int role;
	private int managId;
	private int userId;
	private String telephone;
	private String url;
	
	

	public Service() {
		super();
	}

	public Service(String nick, int role, int managId, int userId,
			String telephone, String url) {
		super();
		this.nick = nick;
		this.role = role;
		this.managId = managId;
		this.userId = userId;
		this.telephone = telephone;
		this.url = url;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public int getManagId() {
		return managId;
	}

	public void setManagId(int managId) {
		this.managId = managId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public static List<Service> parseList(String json) {
		Gson g = new Gson();
		List<Service> list = g.fromJson(json, new TypeToken<List<Service>>() {
		}.getType());
		return list;
	}

	public static Service parse(String Json) {
		Service item = null;
		if (Json != null) {
			Gson g = new Gson();
			item = g.fromJson(Json, Service.class);
		}
		return item;
	}

}
