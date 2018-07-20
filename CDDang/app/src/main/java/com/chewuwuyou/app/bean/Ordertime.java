package com.chewuwuyou.app.bean;

import java.util.List;

public class Ordertime {

	private String time; // 时间

	
	
	public Ordertime(String time, String orderresulttype,String originStatus) {
		super();
		this.time = time;
		this.orderresulttype = orderresulttype;
		this.orderresult=originStatus;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<Ordertime> getOrdertime() {
		return ordertime;
	}

	public void setOrdertime(List<Ordertime> ordertime) {
		this.ordertime = ordertime;
	}

	private String date;// 日期
	private String ordertimetype;// 订单时间类型，如：订单创建时间、付款时间
	private String orderresulttype;// 订单结果类型
	private String orderresult;// 订单结果
	private List<Ordertime> ordertime;

	public String getOrdertimetype() {
		return ordertimetype;
	}

	public void setOrdertimetype(String ordertimetype) {
		this.ordertimetype = ordertimetype;
	}

	public String getOrderresulttype() {
		return orderresulttype;
	}

	public void setOrderresulttype(String orderresulttype) {
		this.orderresulttype = orderresulttype;
	}

	public String getOrderresult() {
		return orderresult;
	}

	public void setOrderresult(String orderresult) {
		this.orderresult = orderresult;
	}

	public Ordertime(String time, String date, String ordertimetype,
			String orderresulttype, String orderresult,
			List<Ordertime> ordertime) {
		// TODO Auto-generated constructor stub
		super();

		this.time = time;
		this.date = date;
		this.ordertimetype = ordertimetype;
		this.orderresulttype = orderresulttype;
		this.orderresult = orderresult;
		this.ordertime = ordertime;
	}

	
}
