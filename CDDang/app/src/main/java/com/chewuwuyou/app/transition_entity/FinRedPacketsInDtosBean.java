package com.chewuwuyou.app.transition_entity;

import java.io.Serializable;

/**
 * Created by yuyong on 16/10/13.
 */

public class FinRedPacketsInDtosBean implements Serializable{

    private int accid;
    private int redPacketsOutId;
    private int money;
    private String leaveMessage;
    private String nickName;
    private String headImg;
    private int theBest;
    private String createAt;

    public int getAccid() {
        return accid;
    }

    public void setAccid(int accid) {
        this.accid = accid;
    }

    public int getRedPacketsOutId() {
        return redPacketsOutId;
    }

    public void setRedPacketsOutId(int redPacketsOutId) {
        this.redPacketsOutId = redPacketsOutId;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getLeaveMessage() {
        return leaveMessage;
    }

    public void setLeaveMessage(String leaveMessage) {
        this.leaveMessage = leaveMessage;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public int getTheBest() {
        return theBest;
    }

    public void setTheBest(int theBest) {
        this.theBest = theBest;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }
}
