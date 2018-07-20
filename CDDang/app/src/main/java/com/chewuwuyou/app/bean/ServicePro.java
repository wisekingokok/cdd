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
public class ServicePro implements Serializable{

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
	public ServicePro(int id, double fees, int type, String projectName,int projectNum, String projectImg, String serviceDesc,String serviceFolder) {
		super();
		this.id = id;
		this.fees = fees;
		this.type = type;
		this.projectName = projectName;
		this.projectNum = projectNum;
		this.projectImg = projectImg;
		this.serviceDesc = serviceDesc;
		this.serviceFolder = serviceFolder;
	}
    
	public String getServiceFolder() {
		return serviceFolder;
	}


	public void setServiceFolder(String serviceFolder) {
		this.serviceFolder = serviceFolder;
	}


	public ServicePro() {
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


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static ServicePro parse(String serviceProJson) {
		ServicePro myBusi = null;
		if (serviceProJson != null) {
			Gson g = new Gson();
			myBusi = g.fromJson(serviceProJson, ServicePro.class);
		}
		return myBusi;
	}

	public static List<ServicePro> parseList(String serviceProJson) {
		List<ServicePro> myBusins = null;
		if (serviceProJson != null) {
			Gson g = new Gson();
			myBusins = g.fromJson(serviceProJson,
					new TypeToken<List<ServicePro>>() {
					}.getType());
		}
		return myBusins;
	}
}
