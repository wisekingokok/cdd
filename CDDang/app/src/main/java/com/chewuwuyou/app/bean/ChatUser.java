package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @describe:即时通讯用户的实体
 * @author:yuyong
 * @version 1.1.0
 * @created:2015-2-12下午5:31:05
 */
public class ChatUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// "id": "", //id
	// "userName": "", //用户名
	// "nickName": "", //昵称，没有则为空
	// "sex": "", //性别 0男1女3未知
	// "url": "", //头像url或者为空表示没有
	// "ziji": "", //0是自己，1不是自己
	private String id;
	private String userName;
	private String nickName;
	private String sex;
	private String url;
	private String ziji;
	private String diqu;
	private String aiche;

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

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
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

	public String getZiji() {
		return ziji;
	}

	public void setZiji(String ziji) {
		this.ziji = ziji;
	}

	public String getDiqu() {
		return diqu;
	}

	public void setDiqu(String diqu) {
		this.diqu = diqu;
	}

	public String getAiche() {
		return aiche;
	}

	public void setAiche(String aiche) {
		this.aiche = aiche;
	}

	public static ChatUser parse(String chatUserJson) {
		Gson g = new Gson();
		ChatUser user = g.fromJson(chatUserJson, ChatUser.class);
		return user;
	}

	public static List<ChatUser> parseCitys(String chatUserJson) {
		Gson g = new Gson();
		List<ChatUser> users = g.fromJson(chatUserJson,
				new TypeToken<List<ChatUser>>() {
				}.getType());
		return users;
	}
}
