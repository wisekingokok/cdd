package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Shop implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String startDate;
	private String businessName;
	private String location;
	private String introduce;
	private String contact;
	private String isfavorite;
	private String city;
	private int id;
	private double distance;
	private String title;
	private int userId;
	private String longitude;
	private int commentsize;
	private String qq;
	private String storeLevel;
	private List<Map<String, String>> services;
	private String businessScope;
	private int favoritesize;
	private int star;
	private int todaysize;
	private List<Service> interaction;
	private String url;
	private String category;
	private String address;
	private String brandscope;
	private String expireDate;
	private String images;
	private String realName;
	private String latitude;
	private String telephone;
	private String mobile;

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
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

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getIsfavorite() {
		return isfavorite;
	}

	public void setIsfavorite(String isfavorite) {
		this.isfavorite = isfavorite;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public int getCommentsize() {
		return commentsize;
	}

	public void setCommentsize(int commentsize) {
		this.commentsize = commentsize;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getStoreLevel() {
		return storeLevel;
	}

	public void setStoreLevel(String storeLevel) {
		this.storeLevel = storeLevel;
	}

	public List<Map<String, String>> getServices() {
		return services;
	}

	public void setServices(List<Map<String, String>> services) {
		this.services = services;
	}

	public String getBusinessScope() {
		return businessScope;
	}

	public void setBusinessScope(String businessScope) {
		this.businessScope = businessScope;
	}

	public int getFavoritesize() {
		return favoritesize;
	}

	public void setFavoritesize(int favoritesize) {
		this.favoritesize = favoritesize;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public int getTodaysize() {
		return todaysize;
	}

	public void setTodaysize(int todaysize) {
		this.todaysize = todaysize;
	}

	public List<Service> getInteraction() {
		return interaction;
	}

	public void setInteraction(List<Service> interaction) {
		this.interaction = interaction;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBrandscope() {
		return brandscope;
	}

	public void setBrandscope(String brandscope) {
		this.brandscope = brandscope;
	}

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public static Shop prase(String jsonStr) {
		Shop map = null;
		if (jsonStr != null) {
			Gson g = new Gson();
			map = g.fromJson(jsonStr, new TypeToken<Map<String, List<Shop>>>() {
			}.getType());
		}
		return map;
	}

	public static List<Shop> parseList(String shopsJson) {
		Gson g = new Gson();
		List<Shop> shops = g.fromJson(shopsJson, new TypeToken<List<Shop>>() {
		}.getType());
		return shops;
	}

}
