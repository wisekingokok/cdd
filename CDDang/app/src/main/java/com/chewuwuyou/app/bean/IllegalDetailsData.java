package com.chewuwuyou.app.bean;

import java.io.Serializable;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:违章详情实体类
 * @author:yuyong
 * @date:2015-7-7上午11:45:42
 * @version:1.2.1
 */
public class IllegalDetailsData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int fen;// 违章扣分 0
	private String occur_area;// 违章所在地
	private String info;// 违章内容 机动车未按规定临时停车
	private int money;// 罚款金额 0

	public int getFen() {
		return fen;
	}

	public void setFen(int fen) {
		this.fen = fen;
	}

	public String getOccur_area() {
		return occur_area;
	}

	public void setOccur_area(String occur_area) {
		this.occur_area = occur_area;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

}
