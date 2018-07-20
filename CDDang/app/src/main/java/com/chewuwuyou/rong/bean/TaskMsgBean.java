package com.chewuwuyou.rong.bean;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by yuyong on 16/9/18.
 * 订单提醒实体
 */
public class TaskMsgBean implements Serializable {


    /**
     * flag : 3
     * price : 100.00
     * projectNum : 201
     * orderNum : CDD153024832_4922
     * taskId : 4922
     * projectImg : /project/clnj.png
     * status : 1
     */

    private String flag;
    private String price;
    private String projectNum;
    private String orderNum;
    private String taskId;
    private String projectImg;
    private String status;
    private String userId;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProjectNum() {
        return projectNum;
    }

    public void setProjectNum(String projectNum) {
        this.projectNum = projectNum;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getProjectImg() {
        return projectImg;
    }

    public void setProjectImg(String projectImg) {
        this.projectImg = projectImg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static TaskMsgBean parse(String avJson) {
        TaskMsgBean av = null;
        if (avJson != null) {
            Gson g = new Gson();
            av = g.fromJson(avJson, TaskMsgBean.class);
        }
        return av;
    }

}
