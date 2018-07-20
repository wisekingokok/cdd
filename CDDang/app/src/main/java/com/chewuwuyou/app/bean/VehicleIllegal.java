package com.chewuwuyou.app.bean;

import java.io.Serializable;

/**
 * @describe:车辆违章信息
 * @author:yuyong
 * @version 1.1.0
 * @created:2015-1-20下午6:24:18
 */
public class VehicleIllegal implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String plateNumber;
	private String vehicleImageUrl;
	private String vehicleIllegalNum;
	private String vehicleIllegalMoney;
	private String vehicleIllegalScore;
	private String noteName;
	private String cityName;

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public String getVehicleImageUrl() {
		return vehicleImageUrl;
	}

	public void setVehicleImageUrl(String vehicleImageUrl) {
		this.vehicleImageUrl = vehicleImageUrl;
	}

	public String getVehicleIllegalNum() {
		return vehicleIllegalNum;
	}

	public void setVehicleIllegalNum(String vehicleIllegalNum) {
		this.vehicleIllegalNum = vehicleIllegalNum;
	}

	public String getVehicleIllegalMoney() {
		return vehicleIllegalMoney;
	}

	public void setVehicleIllegalMoney(String vehicleIllegalMoney) {
		this.vehicleIllegalMoney = vehicleIllegalMoney;
	}

	public String getVehicleIllegalScore() {
		return vehicleIllegalScore;
	}

	public void setVehicleIllegalScore(String vehicleIllegalScore) {
		this.vehicleIllegalScore = vehicleIllegalScore;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getNoteName() {
		return noteName;
	}

	public void setNoteName(String noteName) {
		this.noteName = noteName;
	}

}
