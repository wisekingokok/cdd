package com.chewuwuyou.app.bean;


/**
 * @describe:余额提现
 * @author:liuchun
 */
public class ChoiceWithdrawal {
	private int id;
	private String accountImg;// 图片
	private String accountName;// 名称
	private String accountPhone;// 电话
	private boolean isRadio;// 单选

	public ChoiceWithdrawal(String accountName, String accountPhone,
			boolean isRadio) {
		this.accountName = accountName;
		this.accountPhone = accountPhone;
		this.isRadio = isRadio;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAccountImg() {
		return accountImg;
	}

	public void setAccountImg(String accountImg) {
		this.accountImg = accountImg;
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

	public boolean isRadio() {
		return isRadio;
	}

	public void setRadio(boolean isRadio) {
		this.isRadio = isRadio;
	}

}
