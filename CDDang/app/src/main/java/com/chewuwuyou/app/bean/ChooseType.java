package com.chewuwuyou.app.bean;

import java.io.Serializable;

/**
 * 收藏发布服务选择服务选项
 */
public class ChooseType implements Serializable {

	private static final long serialVersionUID = 1L;
	private String typeName;// 服务名称
	private boolean isChoose;// 是否选中
	private int serviceType;// 服务类型

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public boolean isChoose() {
		return isChoose;
	}

	public void setChoose(boolean isChoose) {
		this.isChoose = isChoose;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getServiceType() {
		return serviceType;
	}

	public void setServiceType(int serviceType) {
		this.serviceType = serviceType;
	}

}
