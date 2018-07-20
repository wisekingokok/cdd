package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Offer implements Serializable {

	// "id": 1,
	// "offer": 100,
	// "time": "2016-03-12 10:55:10.0",
	// "busName": "沈先生",
	// "userId": "1",
	// "exactPhone": "18180762781",
	// "url": "ISD:/upload/20150728153116862.jpg",
	// "taskFinished": 122,
	// "star": 4

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private double offer;
	private String time;
	private String busName;
	private String userId;
	private String exactPhone;
	private String url;
	private int taskFinished;
	private float star;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getOffer() {
		return offer;
	}

	public void setOffer(double offer) {
		this.offer = offer;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getBusName() {
		return busName;
	}

	public void setBusName(String busName) {
		this.busName = busName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getExactPhone() {
		return exactPhone;
	}

	public void setExactPhone(String exactPhone) {
		this.exactPhone = exactPhone;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getTaskFinished() {
		return taskFinished;
	}

	public void setTaskFinished(int taskFinished) {
		this.taskFinished = taskFinished;
	}

	public float getStar() {
		return star;
	}

	public void setStar(float star) {
		this.star = star;
	}

	public static Offer parse(String userJson) {
		Offer u = null;
		if (userJson != null) {
			Gson g = new Gson();
			u = g.fromJson(userJson, Offer.class);
		}
		return u;
	}

	public static List<Offer> parseList(String userJson) {
		List<Offer> csts = null;
		if (userJson != null) {
			Gson g = new Gson();
			csts = g.fromJson(userJson, new TypeToken<List<Offer>>() {
			}.getType());
		}
		return csts;
	}
}
