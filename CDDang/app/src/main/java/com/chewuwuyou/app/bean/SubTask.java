
package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @describe:
 * @author:yuyong
 * @version 1.1.0
 * @created:2014-12-4下午9:25:19
 */
public class SubTask implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
    private String location;
    private String businessName;
    private String status;
    private String paymentAmount;
    private String orderNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public static SubTask parse(String subTaskJson) {
        SubTask u = null;
        if (subTaskJson != null) {
            Gson g = new Gson();
            u = g.fromJson(subTaskJson, SubTask.class);
        }
        return u;
    }

    public static List<SubTask> parseList(String subTaskJson) {
        List<SubTask> csts = null;
        if (subTaskJson != null) {
            Gson g = new Gson();
            csts = g.fromJson(subTaskJson, new TypeToken<List<SubTask>>() {
            }.getType());
        }
        return csts;
    }
}
