package com.chewuwuyou.app.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.List;

/**
 * @describe:邮寄地址
 * @author:liuchun
 */
public class MailAddress implements Serializable {

	private int defaultAddress;// 1:默认地址，0:非默认地址
	private String receiver;//收货人姓名
	private String phone;//收货人电话
	private String address;//收货人地址
	private int id;//邮寄地址编号
	private String region;
	private String zipCode;

	public MailAddress(int defaultAddress, String receiver, String phone, String address, int id, String region, String zipCode) {
		this.defaultAddress = defaultAddress;
		this.receiver = receiver;
		this.phone = phone;
		this.address = address;
		this.id = id;
		this.region = region;
		this.zipCode = zipCode;
	}

	public int getDefaultAddress() {
		return defaultAddress;
	}

	public void setDefaultAddress(int defaultAddress) {
		this.defaultAddress = defaultAddress;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public static MailAddress parse(String bannerJson) {
		MailAddress u = null;
		if (bannerJson != null) {
			Gson g = new Gson();
			u = g.fromJson(bannerJson, MailAddress.class);
		}
		return u;
	}

	public static List<MailAddress> parseList(String bannerJson) {
		List<MailAddress> csts = null;
		if (bannerJson != null) {
			Gson g = new Gson();
			csts = g.fromJson(bannerJson, new TypeToken<List<MailAddress>>() {
			}.getType());
		}
		return csts;
	}
}
