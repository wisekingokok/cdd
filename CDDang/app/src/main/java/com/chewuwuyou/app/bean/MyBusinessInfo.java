package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @describe:商家自己的信息
 * @author:yuyong
 * @version 1.1.0
 * @created:2014-12-1下午8:59:46
 */
public class MyBusinessInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// "id": 2,
	// "category": "0",
	// "businessName": "xx",
	// "location": "1510120",
	// "telephone": "11212313212",
	// "mobile": "",
	// "commentsize":10,//评论数
	// "favoritesize":33,//被关注数
	// "todaysize":22,//关注新增条数
	// "url":"upload/xxx.png"//头像
	// "ordersize":444 //订单条数
	// "expireDate":"","startDate":""商家认证过期时间和开始时间
	private int id;
	private String businessName;
	private String location;
	private String mobile;
	private int commentsize;
	private int favoritesize;
	private int todaysize;
	private String url;
	private String ordersize;
	private String expireDate;
	private String startDate;
	private String consumeNum;
	private String realName;// 名称. 例子： 我的名片UI 名称便使用的这个字段
	private String telephone;// 电话
	private int role;// 角色
	private String address;// 地址
	private String province;
	private String city;
	private String district;
	private String type;
	private List<String> images;

	private HeadImageUrl headImageUrl;

	public static class HeadImageUrl {
		private String url;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}
	}

	private float start;

	public float getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public HeadImageUrl getHeadImageUrl() {
		return headImageUrl;
	}

	public void setHeadImageUrl(HeadImageUrl headImageUrl) {
		this.headImageUrl = headImageUrl;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public int getCommentsize() {
		return commentsize;
	}

	public void setCommentsize(int commentsize) {
		this.commentsize = commentsize;
	}

	public int getFavoritesize() {
		return favoritesize;
	}

	public void setFavoritesize(int favoritesize) {
		this.favoritesize = favoritesize;
	}

	public int getTodaysize() {
		return todaysize;
	}

	public void setTodaysize(int todaysize) {
		this.todaysize = todaysize;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getOrdersize() {
		return ordersize;
	}

	public void setOrdersize(String ordersize) {
		this.ordersize = ordersize;
	}

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getConsumeNum() {
		return consumeNum;
	}

	public void setConsumeNum(String consumeNum) {
		this.consumeNum = consumeNum;
	}

	public static MyBusinessInfo parse(String myBusiJson) {
		MyBusinessInfo myBusi = null;
		if (myBusiJson != null) {
			Gson g = new Gson();
			myBusi = g.fromJson(myBusiJson, MyBusinessInfo.class);
		}
		return myBusi;
	}

	public static List<MyBusinessInfo> parseList(String myBusiJson) {
		List<MyBusinessInfo> myBusins = null;
		if (myBusiJson != null) {
			Gson g = new Gson();
			myBusins = g.fromJson(myBusiJson, new TypeToken<List<MyBusinessInfo>>() {
			}.getType());
		}
		return myBusins;
	}

}
