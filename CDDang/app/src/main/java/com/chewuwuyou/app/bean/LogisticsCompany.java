package com.chewuwuyou.app.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.List;

/**
 * @describe:物流公司名称
 * @author:liuchun
 */
public class LogisticsCompany implements Serializable {

	private String commpanyCode;
	private String commpanyName;//品牌
	private String sortLetters;  //显示数据拼音的首字母

	public String getCommpanyCode() {
		return commpanyCode;
	}

	public void setCommpanyCode(String commpanyCode) {
		this.commpanyCode = commpanyCode;
	}
	public String getCommpanyName() {
		return commpanyName;
	}

	public void setCommpanyName(String commpanyName) {
		this.commpanyName = commpanyName;
	}

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

	public static LogisticsCompany parse(String bannerJson) {
		LogisticsCompany u = null;
		if (bannerJson != null) {
			Gson g = new Gson();
			u = g.fromJson(bannerJson, LogisticsCompany.class);
		}
		return u;
	}

	public static List<LogisticsCompany> parseList(String bannerJson) {
		List<LogisticsCompany> csts = null;
		if (bannerJson != null) {
			Gson g = new Gson();
			csts = g.fromJson(bannerJson, new TypeToken<List<LogisticsCompany>>() {
			}.getType());
		}
		return csts;
	}
}
