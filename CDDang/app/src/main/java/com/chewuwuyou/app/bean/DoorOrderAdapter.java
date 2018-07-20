package com.chewuwuyou.app.bean;

/**
 * @describe:订单
 * @author:liuchun
 */
public class DoorOrderAdapter {
	private int orderId;// 订单Id
	private String orderType;// 订单类型
	private String orderName;// 联系人
	private String orderMoney;// 订单金额
	private String orderTime;// 时间
	private String orderDate;// 订单日期
	
	public DoorOrderAdapter(int orderId, String orderType, String orderName,
			String orderMoney, String orderTime, String orderDate) {
		super();
		this.orderId = orderId;
		this.orderType = orderType;
		this.orderName = orderName;
		this.orderMoney = orderMoney;
		this.orderTime = orderTime;
		this.orderDate = orderDate;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public String getOrderMoney() {
		return orderMoney;
	}

	public void setOrderMoney(String orderMoney) {
		this.orderMoney = orderMoney;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
}
