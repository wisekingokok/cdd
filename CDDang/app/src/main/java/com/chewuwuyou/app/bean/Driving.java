package com.chewuwuyou.app.bean;

import java.io.Serializable;

/**
 * @describe:行车实体
 * @author:liuchun
 */
public class Driving implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int drivingId;// 小标
	private String drivingName;// 名称

	public int getDrivingId() {
		return drivingId;
	}

	public void setDrivingId(int drivingId) {
		this.drivingId = drivingId;
	}

	public String getDrivingName() {
		return drivingName;
	}

	public void setDrivingName(String drivingName) {
		this.drivingName = drivingName;
	}

	@Override
	public String toString() {
		return "Driving [drivingId=" + drivingId + ", drivingName=" + drivingName + "]";
	}

}
