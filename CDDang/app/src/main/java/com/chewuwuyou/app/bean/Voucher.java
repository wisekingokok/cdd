package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @describe:抵用券
 * @author:yuyong
 * @version 1.1.0
 * @created:2015-1-26下午3:05:02
 */
public class Voucher implements Serializable {

	// "amount":100 //金额,
	// "id": 1, //编号
	// "createTime": "",//创建时间
	// "expiredDate": "2015-02-15",//过期日
	// "type": "注册车当当赠送"//抵用券类别

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double amount;
	private int id;
	private String createTime;
	private String expiredDate;
	private String type;

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(String expiredDate) {
		this.expiredDate = expiredDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public static Voucher parse(String voucherJson) {
		Voucher vo = null;
		if (voucherJson != null) {
			Gson g = new Gson();
			vo = g.fromJson(voucherJson, Voucher.class);
		}
		return vo;
	}

	public static List<Voucher> parseList(String voucherJson) {
		List<Voucher> vos = null;
		if (voucherJson != null) {
			Gson g = new Gson();
			vos = g.fromJson(voucherJson, new TypeToken<List<Voucher>>() {
			}.getType());
		}
		return vos;
	}
}
