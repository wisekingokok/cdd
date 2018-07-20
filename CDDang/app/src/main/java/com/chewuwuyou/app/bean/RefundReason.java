
package com.chewuwuyou.app.bean;

import java.io.Serializable;

/**
 * @describe:退款理由
 * @author:liuchun
 */
public class RefundReason implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String Reason;
	private boolean isSelected;
	
	
	public RefundReason(String reason, boolean isSelected) {
		super();
		Reason = reason;
		this.isSelected = isSelected;
	}
	public String getReason() {
		return Reason;
	}
	public void setReason(String reason) {
		Reason = reason;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
}
