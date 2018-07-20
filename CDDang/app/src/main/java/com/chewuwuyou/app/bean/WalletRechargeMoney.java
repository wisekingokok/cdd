package com.chewuwuyou.app.bean;

import java.io.Serializable;

/**
 * @describe:爱车账目实体类
 * @author:yuyong
 * @version 1.1.0
 * @created:2014-10-23下午3:45:37
 */
public class WalletRechargeMoney implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String money;
	private boolean isSelected;
	
	
	
	public WalletRechargeMoney(String money, boolean isSelected) {
		super();
		this.money = money;
		this.isSelected = isSelected;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	

}
