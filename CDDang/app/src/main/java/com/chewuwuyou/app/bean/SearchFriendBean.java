package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:搜索好友的实体类
 * @author:yuyong
 * @date:2015-5-29下午1:55:09
 * @version:1.2.1
 */
public class SearchFriendBean implements Serializable {

	// "noteName": "",
	// "ziji": "0",0还不是好友，1已经是好友
	// "id": 3394,
	// "sex": "0",
	// "nickName": "余大傻",
	// "userName": "1003394",
	// "diqu": "辽宁省鞍山市",
	// "friend": "0",
	// "url": "",
	// "qianming": "",
	// "aiche": "宝马/宝马1系两厢"

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String noteName;
	private String ziji;
	private int id;
	private String sex;
	private String nickName;
	private String userName;
	private String diqu;
	private String friend;
	private String url;
	private String qianming;
	private String aiche;
	private String tel;
	private String userId;

	private  String contactName;

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

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

	public static SearchFriendBean parse(String searchFriendJson) {
		SearchFriendBean sfb = null;
		if (searchFriendJson != null) {
			Gson g = new Gson();
			sfb = g.fromJson(searchFriendJson, SearchFriendBean.class);
		}
		return sfb;
	}

	public static List<SearchFriendBean> parseList(String searchFriendJson) {
		List<SearchFriendBean> sfbs = null;
		if (searchFriendJson != null) {
			Gson g = new Gson();
			sfbs = g.fromJson(searchFriendJson,
					new TypeToken<List<SearchFriendBean>>() {
					}.getType());
		}
		return sfbs;
	}
}
