package com.chewuwuyou.app.bean;

import java.io.Serializable;

/**
 * @describe:物流流水号
 * @author:liuchun
 */
public class TracesEntity implements Serializable {
	private String AcceptTime;
	private String AcceptStation;

	public String getAcceptTime() {
		return AcceptTime;
	}

	public void setAcceptTime(String acceptTime) {
		AcceptTime = acceptTime;
	}

	public String getAcceptStation() {
		return AcceptStation;
	}

	public void setAcceptStation(String acceptStation) {
		AcceptStation = acceptStation;
	}
}
