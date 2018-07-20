package com.chewuwuyou.app.bean;

import java.io.Serializable;

import com.google.gson.Gson;

/**
 * @describe:圈动态的赞实体
 * @author:XH
 * @version
 * @created:
 */

public class QuanZanItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;// 2
	private String noteName;// "\u8001\u987e"
	private String nickName;// ""
	private String friendId;// 19
	private String url;

	// private String sex;// 帖子发起人的性别

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

	public static QuanZanItem parse(String zanJson) {
		QuanZanItem zan = null;
		if (zanJson != null) {
			Gson g = new Gson();
			zan = g.fromJson(zanJson, QuanZanItem.class);
		}
		return zan;
	}
}
