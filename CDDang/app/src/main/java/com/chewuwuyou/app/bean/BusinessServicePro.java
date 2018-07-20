package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:车务服务选项实体
 * @author:yuyong
 * @date:2015-6-23下午2:53:48
 * @version:1.2.1
 */
public class BusinessServicePro implements Serializable{

	// "id": 1,
	// "fees": 10,
	// "type": 2,
	// "projectName": "车辆年检",
	// "projectNum": 201

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id; //项目Id
	private double fees;//规费
	private int type;//项目类型
	private String projectName;//项目名称
	private int projectNum;//项目编号
	private String projectImg;// 图片地址
	private String serviceDesc;// 描述
	private String serviceFolder;
	private double servicePrice;//服务费
	private String location;
	public BusinessServicePro(int id, double fees, int type, String projectName,int projectNum, String projectImg, String serviceDesc,String serviceFolder,double servicePrice,String location) {
		super();
		this.id = id;
		this.fees = fees;
		this.type = type;
		this.projectName = projectName;
		this.projectNum = projectNum;
		this.projectImg = projectImg;
		this.serviceDesc = serviceDesc;
		this.serviceFolder = serviceFolder;
		this.servicePrice = servicePrice;
		this.location = location;
	}
    
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public double getServicePrice() {
		return servicePrice;
	}

	public void setServicePrice(double servicePrice) {
		this.servicePrice = servicePrice;
	}

	public String getServiceFolder() {
		return serviceFolder;
	}


	public void setServiceFolder(String serviceFolder) {
		this.serviceFolder = serviceFolder;
	}


	public BusinessServicePro() {
		super();
	}



	public String getServiceDesc() {
		return serviceDesc;
	}

	
	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}

	public void setFees(double fees) {
		this.fees = fees;
	}

	public String getProjectImg() {
		return projectImg;
	}

	public void setProjectImg(String projectImg) {
		this.projectImg = projectImg;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getFees() {
		return fees;
	}

	public void setFees(int fees) {
		this.fees = fees;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public int getProjectNum() {
		return projectNum;
	}

	public void setProjectNum(int projectNum) {
		this.projectNum = projectNum;
	}

	/**
	 * @return the serviceFolder
	 */
	// public JSONArray getServiceFolder() {
	// return serviceFolder;
	// }
	//
	// /**
	// * @param serviceFolder
	// * the serviceFolder to set
	// */
	// public void setServiceFolder(JSONArray serviceFolder) {
	// this.serviceFolder = serviceFolder;
	// }

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static BusinessServicePro parse(String serviceProJson) {
		BusinessServicePro myBusi = null;
		if (serviceProJson != null) {
			Gson g = new Gson();
			myBusi = g.fromJson(serviceProJson, BusinessServicePro.class);
		}
		return myBusi;
	}

	public static List<BusinessServicePro> parseList(String serviceProJson) {
		List<BusinessServicePro> myBusins = null;
		if (serviceProJson != null) {
			Gson g = new Gson();
			myBusins = g.fromJson(serviceProJson,
					new TypeToken<List<BusinessServicePro>>() {
					}.getType());
		}
		return myBusins;
	}
}
