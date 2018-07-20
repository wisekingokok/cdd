package com.chewuwuyou.app.bean;

import java.io.Serializable;

/**
 * @describe:账户提现
 * @author:liuchun
 */
public class AccountWithdrawal implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String accountName;// 名称
	private String accountPhone;// 电话
	private String isRadio;// 单选

	public AccountWithdrawal(String accountName, String accountPhone) {
		this.accountName = accountName;
		this.accountPhone = accountPhone;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountPhone() {
		return accountPhone;
	}

	public void setAccountPhone(String accountPhone) {
		this.accountPhone = accountPhone;
	}

	public String getIsRadio() {
		return isRadio;
	}

	public void setIsRadio(String isRadio) {
		this.isRadio = isRadio;
	}

}
