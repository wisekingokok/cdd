package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @describe:商家入驻审核
 * @author:liuchun
 */
public class ExamineBook implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	private String exist;
	private String id;
	private String userId;
	private String address;
	private String provinceId;
	private String provinceName;
	private String cityId;
	private String cityName;
	private String districtId;
	private String districtName;
	private String category;
	private String identityType;
	private String realName;
	private String identityNo;
	private String identityImageUrl;
	private String headImageUrl;
	private String status;
	private String identityBeiImageUrl;
	private String auditComment;
	private String licenseImageUrl;
	private String storeFrontImageUrl;
	private String companyName;
	private String companyContact;
	private String licenseNo;
	private String handImageUrl;

	public String getHandImageUrl() {
		return handImageUrl;
	}

	public void setHandImageUrl(String handImageUrl) {
		this.handImageUrl = handImageUrl;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public String getExist() {
		return exist;
	}

	public void setExist(String exist) {
		this.exist = exist;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getDistrictId() {
		return districtId;
	}

	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getIdentityType() {
		return identityType;
	}

	public void setIdentityType(String identityType) {
		this.identityType = identityType;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getIdentityNo() {
		return identityNo;
	}

	public void setIdentityNo(String identityNo) {
		this.identityNo = identityNo;
	}

	public String getIdentityImageUrl() {
		return identityImageUrl;
	}

	public void setIdentityImageUrl(String identityImageUrl) {
		this.identityImageUrl = identityImageUrl;
	}

	public String getHeadImageUrl() {
		return headImageUrl;
	}

	public void setHeadImageUrl(String headImageUrl) {
		this.headImageUrl = headImageUrl;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAuditComment() {
		return auditComment;
	}

	public void setAuditComment(String auditComment) {
		this.auditComment = auditComment;
	}

	public String getIdentityBeiImageUrl() {
		return identityBeiImageUrl;
	}

	public void setIdentityBeiImageUrl(String identityBeiImageUrl) {
		this.identityBeiImageUrl = identityBeiImageUrl;
	}

	
	public String getLicenseImageUrl() {
		return licenseImageUrl;
	}

	public void setLicenseImageUrl(String licenseImageUrl) {
		this.licenseImageUrl = licenseImageUrl;
	}

	public String getStoreFrontImageUrl() {
		return storeFrontImageUrl;
	}

	public void setStoreFrontImageUrl(String storeFrontImageUrl) {
		this.storeFrontImageUrl = storeFrontImageUrl;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyContact() {
		return companyContact;
	}

	public void setCompanyContact(String companyContact) {
		this.companyContact = companyContact;
	}

	public static ExamineBook parse(String favoriteJson) {
		ExamineBook f = null;
		if (favoriteJson != null) {
			Gson g = new Gson();
			f = g.fromJson(favoriteJson, ExamineBook.class);
		}
		return f;
	}

	public static List<ExamineBook> parseList(String favoriteListJson) {
		List<ExamineBook> favorites = null;
		if (favoriteListJson != null) {
			Gson g = new Gson();
			favorites = g.fromJson(favoriteListJson,new TypeToken<List<ExamineBook>>() {}.getType());
		}
		return favorites;
	}
	

	
}
