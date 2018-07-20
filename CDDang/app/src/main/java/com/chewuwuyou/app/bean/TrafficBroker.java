package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @describe:
 * @author:yuyong
 * @version 1.1.0
 * @created:2014-11-16下午3:49:35
 */
public class TrafficBroker implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// "id": 1,
	// "businessName": "沈先生",
	// "introduce": "专业从事车辆业务代办10年，专业，快速。为你解决车辆业务麻烦。",
	// "businessScope": "车辆过户,100;车辆上牌,200",
	// "location": "江苏省,常州市",
	// "realName": "沈先生",
	// "mobile": "18180762781",
	// "storeLevel": "3",
	// "qq": "123456",
	// "category": "0"
	// commentsize:0//评论条数
	// "star":5,
	// "isfavorite":1 //1为收藏，0为未关注
	// "interaction": [ //客服数组
	// {
	// "nick": "吊炸天客服",
	// "role": 2,
	// "managId": 119,
	// "userId": 3448
	// },
	// {
	// "nick": "余二妞",
	// "role": 2,
	// "managId": 121,
	// "userId": 3495
	// }
	// ],
	private int id;
	private int userId;
	private int fid;
	private String businessName;
	private String introduce;
	private String location;
	private String realName;
	private String mobile;
	private String qq;
	private String category;
	private int commentsize;
	private int star;
	private int isfavorite;//1：关注 0：未关注
	private int favoritesize;
	private String url;
	private List<Service> interaction;
	private String address;
	// private List<Map<String, String>> services;
	private List<ProjectPricce> services;

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

	public int getFid() {
		return fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
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

	public int getCommentsize() {
		return commentsize;
	}

	public void setCommentsize(int commentsize) {
		this.commentsize = commentsize;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public int getIsfavorite() {
		return isfavorite;
	}

	public void setIsfavorite(int isfavorite) {
		this.isfavorite = isfavorite;
	}

	public int getFavoritesize() {
		return favoritesize;
	}

	public void setFavoritesize(int favoritesize) {
		this.favoritesize = favoritesize;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<Service> getInteraction() {
		return interaction;
	}

	public void setInteraction(List<Service> interaction) {
		this.interaction = interaction;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	// public Map<String, String> getServices() {
	// return services;
	// }
	//
	// public void setServices(Map<String, String> services) {
	// this.services = services;
	// }

	public static TrafficBroker parse(String trafficBrokerJson) {
		TrafficBroker o = null;
		if (trafficBrokerJson != null) {
			Gson g = new Gson();
			o = g.fromJson(trafficBrokerJson, TrafficBroker.class);
		}
		return o;
	}

	public List<ProjectPricce> getServices() {
		return services;
	}

	public void setServices(List<ProjectPricce> services) {
		this.services = services;
	}

	public static List<TrafficBroker> parseList(String trafficBrokerJson) {
		List<TrafficBroker> tras = null;
		if (trafficBrokerJson != null) {
			Gson g = new Gson();
			tras = g.fromJson(trafficBrokerJson,
					new TypeToken<List<TrafficBroker>>() {
					}.getType());
		}
		return tras;
	}
}
