package com.chewuwuyou.app.bean;

import java.io.Serializable;

/**
 * @describe:服务管理
 * @author:liuchun
 */
public class ServiceAdministration implements Serializable {

//	"id": "20",
//			"serviceId": "0",
//			"projectName": "车辆违章代缴",
//			"projectNum": "101",
//			"type": "1",
//			"projectImg": "\/project\/clwzdj.png",
//			"serviceDesc": "代缴车辆行驶过程中产生的违章罚款。",
//			"serviceFolder": [
//	{
//		"name": "手续",
//			"list": [
//		"领取确认书",
//				"行驶证原件",
//				"如果贴条还需处罚决定书"
//		]
//	}
//	],
//			"fees": "0"
	private int id;
	private int serviceId;
	private String projectName;
	private String projectNum;
	private String type;
	private String projectImg;
	private String serviceDesc;
	private String fees;
	private String price;
	private String sid;
	private boolean mBoolea;
	private int serviceIdentification;

	public ServiceAdministration(int id, int serviceId, String projectName, String projectNum, String type, String projectImg, String serviceDesc, String fees, String price, String sid, boolean mBoolea, int serviceIdentification) {
		this.id = id;
		this.serviceId = serviceId;
		this.projectName = projectName;
		this.projectNum = projectNum;
		this.type = type;
		this.projectImg = projectImg;
		this.serviceDesc = serviceDesc;
		this.fees = fees;
		this.price = price;
		this.sid = sid;
		this.mBoolea = mBoolea;
		this.serviceIdentification = serviceIdentification;
	}

	public int getServiceIdentification() {
		return serviceIdentification;
	}

	public void setServiceIdentification(int serviceIdentification) {
		this.serviceIdentification = serviceIdentification;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public boolean ismBoolea() {
		return mBoolea;
	}

	public void setmBoolea(boolean mBoolea) {
		this.mBoolea = mBoolea;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getFees() {
		return fees;
	}

	public void setFees(String fees) {
		this.fees = fees;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectNum() {
		return projectNum;
	}

	public void setProjectNum(String projectNum) {
		this.projectNum = projectNum;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProjectImg() {
		return projectImg;
	}

	public void setProjectImg(String projectImg) {
		this.projectImg = projectImg;
	}

	public String getServiceDesc() {
		return serviceDesc;
	}

	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}
}
