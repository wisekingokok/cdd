package com.chewuwuyou.app.bean;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ZanerOrInvolverItem {

	/*
	 *         "noteName": "",
        "id": "3620",
        "userName": "1003620",
        "sex": "0",
        "nick": "宇宙开发局局长",
        "url": "ISD:/upload/20150527134120712.jpg",
        "signature": "在于我们如何去做自己不版高清视频点击进入店铺的宝贝"
	 */
	
	private String noteName;//"noteName": "",
	private String id; //"3620"
	private String userName; //"1003620",
	private String sex; //"0",
	private String nick; //"宇宙开发局局长"
	private String url; //"ISD:/upload/20150527134120712.jpg",
	private String signature; //"在于我们如何去做自己不版高清视频点击进入店铺的宝贝"
	public String getNoteName() {
		return noteName;
	}
	public void setNoteName(String noteName) {
		this.noteName = noteName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	public static List<ZanerOrInvolverItem> parseList(String json) {
		Gson g = new Gson();
		List<ZanerOrInvolverItem> list = g.fromJson(json, new TypeToken<List<ZanerOrInvolverItem>>() {
		}.getType());
		return list;
	}

	public static ZanerOrInvolverItem parse(String json) {
		ZanerOrInvolverItem item = null;
		if (json != null) {
			Gson g = new Gson();
			item = g.fromJson(json, ZanerOrInvolverItem.class);
		}
		return item;
	}
}


