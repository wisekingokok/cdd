package com.chewuwuyou.app.transition_entity;

import java.io.Serializable;

/**
 * 转账参数
 * Created by yuyong on 16/10/19.
 */

public class TransferParamBean implements Serializable {

    private String amount;//金额
    private String payPass;//支付密码
    private String meAccId;//我的用户ID
    private String otherAccId;//目标用户ID
    private String leaveMessage;//转账说明


    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPayPass() {
        return payPass;
    }

    public void setPayPass(String payPass) {
        this.payPass = payPass;
    }

    public String getMeAccId() {
        return meAccId;
    }

    public void setMeAccId(String meAccId) {
        this.meAccId = meAccId;
    }

    public String getOtherAccId() {
        return otherAccId;
    }

    public void setOtherAccId(String otherAccId) {
        this.otherAccId = otherAccId;
    }

    public String getLeaveMessage() {
        return leaveMessage;
    }

    public void setLeaveMessage(String leaveMessage) {
        this.leaveMessage = leaveMessage;
    }
}
