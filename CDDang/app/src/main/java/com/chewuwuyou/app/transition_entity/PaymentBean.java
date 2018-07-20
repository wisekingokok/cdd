package com.chewuwuyou.app.transition_entity;

import java.io.Serializable;

/**
 * Created by yuyong on 16/10/17.
 */

public class PaymentBean  implements Serializable{
    private int resId;
    private String payment;
    private String payDesc;
    private boolean isCheck;

    public PaymentBean(int resId, String payment, String payDesc, boolean isCheck) {
        this.resId = resId;
        this.payment = payment;
        this.payDesc = payDesc;
        this.isCheck = isCheck;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getPayDesc() {
        return payDesc;
    }

    public void setPayDesc(String payDesc) {
        this.payDesc = payDesc;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
