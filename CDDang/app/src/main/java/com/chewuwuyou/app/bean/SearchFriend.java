package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:
 * @author:yuyong
 * @date:2015-8-18下午8:02:19
 * @version:1.2.1
 */
public class SearchFriend implements Serializable {
	// "noteName": "",
	// "ziji": "0",
	// "id": 1,
	// "sex": "0",
	// "nickName": "车叮当",
	// "tel": "18180762781",
	// "userName": "1000001",
	// "diqu": "浙江温州",
	// "friend": "1",
	// "url": "ISD:/upload/20150527161353925.jpeg",
	// "qianming": "心情美丽",
	// "aiche": "阿斯顿马丁/Rapide"

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String noteName;
	private String ziji;
	private int id;
	private String sex;
	private String nickName;
	private String tel;
	private String userName;
	private String diqu;
	private String friend;
	private String url;
	private String qianming;
	private String aiche;

	public String getNoteName() {
		return noteName;
	}

	public void setNoteName(String noteName) {
		this.noteName = noteName;
	}

	public String getZiji() {
		return ziji;
	}

	public void setZiji(String ziji) {
		this.ziji = ziji;
	}

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

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDiqu() {
		return diqu;
	}

	public void setDiqu(String diqu) {
		this.diqu = diqu;
	}

	public String getFriend() {
		return friend;
	}

	public void setFriend(String friend) {
		this.friend = friend;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getQianming() {
		return qianming;
	}

	public void setQianming(String qianming) {
		this.qianming = qianming;
	}

	public String getAiche() {
		return aiche;
	}

	public void setAiche(String aiche) {
		this.aiche = aiche;
	}

	public static SearchFriend parse(String chatUserJson) {
		Gson g = new Gson();
		SearchFriend user = g.fromJson(chatUserJson, SearchFriend.class);
		return user;
	}

	public static List<SearchFriend> parseList(String chatUserJson) {
		Gson g = new Gson();
		List<SearchFriend> users = g.fromJson(chatUserJson,
				new TypeToken<List<SearchFriend>>() {
				}.getType());
		return users;
	}
}
