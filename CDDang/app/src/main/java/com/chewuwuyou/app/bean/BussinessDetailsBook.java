package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @describe:商家详情实体
 * @author:liuchun
 */
public class BussinessDetailsBook implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private int userId;
	private String businessName;
	private String introduce;
	private String businessScope;
	private String location;
	private String realName;
	private String mobile;
	private String storeLevel;
	private String qq;
	private String category;
	private String address;
	private String zfbAccount;
	private String wszfAccount;
	private String upAccount;
	private List<Service> interaction;
	private float star;
	private String url;
	private int commentsize;
	private int consumeNum;
	private String expireDate;
	private String images;
	private String startDate;
	private int isfavorite;
	private String companyName;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

// private List<ServicePro> wzdj;
	// private List<ServicePro> jzfw;
	// private List<ServicePro> clfw;

	public int getCommentsize() {
		return commentsize;
	}

	public void setCommentsize(int commentsize) {
		this.commentsize = commentsize;
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

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setConsumeNum(int consumeNum) {
		this.consumeNum = consumeNum;
	}

	public float getStar() {
		return star;
	}

	public void setStar(float star) {
		this.star = star;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getIsfavorite() {
		return isfavorite;
	}

	public void setIsfavorite(int isfavorite) {
		this.isfavorite = isfavorite;
	}

	public List<Service> getInteraction() {
		return interaction;
	}

	public void setInteraction(List<Service> interaction) {
		this.interaction = interaction;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getBusinessScope() {
		return businessScope;
	}

	public void setBusinessScope(String businessScope) {
		this.businessScope = businessScope;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getStoreLevel() {
		return storeLevel;
	}

	public void setStoreLevel(String storeLevel) {
		this.storeLevel = storeLevel;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
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

	public String getZfbAccount() {
		return zfbAccount;
	}

	public void setZfbAccount(String zfbAccount) {
		this.zfbAccount = zfbAccount;
	}

	public String getWszfAccount() {
		return wszfAccount;
	}

	public void setWszfAccount(String wszfAccount) {
		this.wszfAccount = wszfAccount;
	}

	public String getUpAccount() {
		return upAccount;
	}

	public void setUpAccount(String upAccount) {
		this.upAccount = upAccount;
	}

	public int getConsumeNum() {
		return consumeNum;
	}
}
