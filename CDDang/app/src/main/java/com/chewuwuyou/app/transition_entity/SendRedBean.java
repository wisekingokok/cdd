package com.chewuwuyou.app.transition_entity;

import java.io.Serializable;

/**
 * Created by yuyong on 16/10/18.
 */

public class SendRedBean implements Serializable {

    private String userId;             //发送者ID
    private String type;               //红包类型（0 单个红包 、 1 普通红包 、2 拼手气红包）
    private String money;              //金额
    private String friendAccid;        //好友id或者群id
    private int size;                  //红包个数
    private String leaveMessage;       //留言或备注
    private String payType;            //支付方式
    private String unitPrice;          //单个红包金额（普通群红包必传字段）

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getFriendAccid() {
        return friendAccid;
    }

    public void setFriendAccid(String friendAccid) {
        this.friendAccid = friendAccid;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getLeaveMessage() {
        return leaveMessage;
    }

    public void setLeaveMessage(String leaveMessage) {
        this.leaveMessage = leaveMessage;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public String toString() {
        return "SendRedBean{" +
                "userId='" + userId + '\'' +
                ", type='" + type + '\'' +
                ", money='" + money + '\'' +
                ", friendAccid='" + friendAccid + '\'' +
                ", size=" + size +
                ", leaveMessage='" + leaveMessage + '\'' +
                ", payType='" + payType + '\'' +
                ", unitPrice='" + unitPrice + '\'' +
                '}';
    }
}
