package com.chewuwuyou.app.bean;

import java.io.Serializable;

import com.google.gson.Gson;

/**
 * @describe:圈动态的赞实体
 * @author:XH
 * @version
 * @created:
 * 
 */

public class ZanItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;// "id": 8,
	private String noteName;// "noteName": "我",
	private String nickName;// "车当当客服001",
	private String friendId;// 3448
	private String url;// "ISD:/upload/20150904092736282.jpg",
	private String sex;// "0",
	private String userName;// "1003448",

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNoteName() {
		return noteName;
	}

	public void setNoteName(String noteName) {
		this.noteName = noteName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getFriendId() {
		return friendId;
	}

	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public static ZanItem parse(String zanJson) {
		ZanItem zan = null;
		if (zanJson != null) {
			Gson g = new Gson();
			zan = g.fromJson(zanJson, ZanItem.class);
		}
		return zan;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

}
