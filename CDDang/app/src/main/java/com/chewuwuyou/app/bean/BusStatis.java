package com.chewuwuyou.app.bean;

import java.io.Serializable;

/**
 * 商家数据统计
 * 
 * @author yuyong
 * 
 */
public class BusStatis implements Serializable {

	// {
	// "jinRiChengJiao": 2,
	// "zuoRiChengJiao": 1,
	// "benYueChengJiao": 3,
	// "shangYueChengJiao": 0,
	// "jinRiChengJiaoMount": 333.01,
	// "zuoRiChengJiaoMount": 0.1,
	// "benYueChengJiaoMount": 333.11,
	// "shangYueChengJiaoMount": 0,
	// "jinRi": 2,
	// "zuoRi": 1,
	// "benYue": 3,
	// "shangYue": 0,
	// "type": 1,
	// "myZone": "四川省",
	// "zones": [
	// {
	// "zoneName": "成都市",
	// "thisZoneBenYueChengJiao": 2,
	// "thisZoneShangYueChengJiao": 0,
	// "thisZoneBenYueChengJiaoAmount": 333.01,
	// "thisZoneShangYueChengJiaoAmount": 0,
	// "thisZoneBenYue": 2,
	// "thisZoneShangYue": 0
	// },
	// {
	// "zoneName": "达州市",
	// "thisZoneBenYueChengJiao": 1,
	// "thisZoneShangYueChengJiao": 0,
	// "thisZoneBenYueChengJiaoAmount": 0.1,
	// "thisZoneShangYueChengJiaoAmount": 0,
	// "thisZoneBenYue": 1,
	// "thisZoneShangYue": 0
	// }
	// ]
	// }
	//

	// private int jinRiChengJiao;// 今日成交
	// private int zuoRiChengJiao;// 昨日成交
	// private int benYueChengJiao;// 本月成交
	// private int shangYueChengJiao;// 上月成交
	//
	// private double jinRiChengJiaoMount;// 今日成交金额
	// private double zuoRiChengJiaoMount;// 昨日成交金额
	// private double benYueChengJiaoMount;// 本月成交金额
	// private double shangYueChengJiaoMount;// 上月成交金额

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cityName;
	private String orderNumOne;
	private String orderNumTwo;

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getOrderNumOne() {
		return orderNumOne;
	}

	public void setOrderNumOne(String orderNumOne) {
		this.orderNumOne = orderNumOne;
	}

	public String getOrderNumTwo() {
		return orderNumTwo;
	}

	public void setOrderNumTwo(String orderNumTwo) {
		this.orderNumTwo = orderNumTwo;
	}

}
