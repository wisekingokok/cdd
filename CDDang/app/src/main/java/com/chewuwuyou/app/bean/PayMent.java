package com.chewuwuyou.app.bean;

import java.io.Serializable;

public class PayMent implements Serializable {

	private static final long serialVersionUID = 1L;
	private String payMent;// 支付方式
	private int payMentImgsId;// 支付图标
	private String payMentDes;// 支付方式描述
	private boolean isChecked;// 是否选中
	private boolean isChoose;// 是否可以选择

	public String getPayMent() {
		return payMent;
	}

	public void setPayMent(String payMent) {
		this.payMent = payMent;
	}

	public int getPayMentImgsId() {
		return payMentImgsId;
	}

	public void setPayMentImgsId(int payMentImgsId) {
		this.payMentImgsId = payMentImgsId;
	}

	public String getPayMentDes() {
		return payMentDes;
	}

	public void setPayMentDes(String payMentDes) {
		this.payMentDes = payMentDes;
	}

	public boolean getIsChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public boolean getIsChoose() {
		return isChoose;
	}

	public void setChoose(boolean isChoose) {
		this.isChoose = isChoose;
	}
	
}
