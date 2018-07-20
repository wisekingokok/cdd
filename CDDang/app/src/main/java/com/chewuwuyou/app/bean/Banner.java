package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 运营位
 * 
 * @author yuyong
 * 
 */
public class Banner implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// "imageUrl":
	// "http://img05.tooopen.com/images/20140604/sy_62331342149.jpg",
	// "tiaoType": 1,
	// "tiaoUrl":
	// "http://img05.tooopen.com/images/20150531/tooopen_sy_127457023651.jpg"
	private String imageUrl;// 运营位图片
	private int tiaoType;// 跳网页还是跳功能
	private String tiaoUrl;// 跳的地址

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public int getTiaoType() {
		return tiaoType;
	}

	public void setTiaoType(int tiaoType) {
		this.tiaoType = tiaoType;
	}

	public String getTiaoUrl() {
		return tiaoUrl;
	}

	public void setTiaoUrl(String tiaoUrl) {
		this.tiaoUrl = tiaoUrl;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Banner [imageUrl=" + imageUrl + ", tiaoType=" + tiaoType
				+ ", tiaoUrl=" + tiaoUrl + "]";
	}

	public static Banner parse(String bannerJson) {
		Banner u = null;
		if (bannerJson != null) {
			Gson g = new Gson();
			u = g.fromJson(bannerJson, Banner.class);
		}
		return u;
	}

	public static List<Banner> parseList(String bannerJson) {
		List<Banner> csts = null;
		if (bannerJson != null) {
			Gson g = new Gson();
			csts = g.fromJson(bannerJson, new TypeToken<List<Banner>>() {
			}.getType());
		}
		return csts;
	}

}
