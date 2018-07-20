package com.chewuwuyou.app.transition_entity;

/**
 * Created by ZQ on 2016/10/17.
 */

public class RedPacketParam {


    /**
     * type :
     * dateSize : 0
     * dateMoney : 200
     * oneSizeMoney : 200
     */

    private String type;
    private int dateSize;
    private double dateMoney;
    private double oneSizeMoney;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDateSize() {
        return dateSize;
    }

    public void setDateSize(int dateSize) {
        this.dateSize = dateSize;
    }

    public double getDateMoney() {
        return dateMoney;
    }

    public void setDateMoney(double dateMoney) {
        this.dateMoney = dateMoney;
    }

    public double getOneSizeMoney() {
        return oneSizeMoney;
    }

    public void setOneSizeMoney(double oneSizeMoney) {
        this.oneSizeMoney = oneSizeMoney;
    }
}
