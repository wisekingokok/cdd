package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:
 * @author:yuyong
 * @date:2015-10-14下午6:05:03
 * @version:1.2.1
 */
public class JYTask implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// "id": "938",
	// "paymentAmount": "2404.0000",
	// "me": "0",0为收到的，1为发出的
	// "type": "2",
	// "pubishTime": "2015-07-09 16:56:46.0",
	// "projectName": "",
	// "status": "2"
	//
	private String id;
	private String paymentAmount;
	private String me;
	private String type;
	private String pubishTime;
	private String projectName;
	private String status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(String paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getMe() {
		return me;
	}

	public void setMe(String me) {
		this.me = me;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPubishTime() {
		return pubishTime;
	}

	public void setPubishTime(String pubishTime) {
		this.pubishTime = pubishTime;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public static JYTask parse(String userJson) {
		JYTask u = null;
		if (userJson != null) {
			Gson g = new Gson();
			u = g.fromJson(userJson, JYTask.class);
		}
		return u;
	}

	public static List<JYTask> parseList(String userJson) {
		List<JYTask> csts = null;
		if (userJson != null) {
			Gson g = new Gson();
			csts = g.fromJson(userJson, new TypeToken<List<JYTask>>() {
			}.getType());
		}
		return csts;
	}
}
