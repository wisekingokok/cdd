package com.chewuwuyou.app.transition_entity;

/**
 * Created by ZQ on 2016/10/14.
 */

public class TransferAccountDetail {
    /**
     * actual : 2.40
     * fee : 1.60
     * collectorPerson : 瑞文
     * transferPerson : DesertSnow
     * money : 4.00
     * exists : 0
     * time : 2016-10-19 10:40:55
     */

    private String actual;//实际到账金额
    private String fee;//手续费
    private String collectorPerson;//收账人昵称
    private String transferPerson;//转账人昵称
    private String money;//转账金额
    private String exists;//0：存在该转账记录，1：不存在该转账记录
    private String time;//转账时间，2016-10-19 11:14:55

    public String getActual() {
        return actual;
    }

    public void setActual(String actual) {
        this.actual = actual;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getCollectorPerson() {
        return collectorPerson;
    }

    public void setCollectorPerson(String collectorPerson) {
        this.collectorPerson = collectorPerson;
    }

    public String getTransferPerson() {
        return transferPerson;
    }

    public void setTransferPerson(String transferPerson) {
        this.transferPerson = transferPerson;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getExists() {
        return exists;
    }

    public void setExists(String exists) {
        this.exists = exists;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    /**
     * money : 5000
     * actual : 4999
     * fee : 1
     * time : 2001-01-10 00:00
     */


}
