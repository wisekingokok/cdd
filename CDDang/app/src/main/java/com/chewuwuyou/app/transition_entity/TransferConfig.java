package com.chewuwuyou.app.transition_entity;

/**
 * Created by Administrator on 2016/10/19 0019.
 */

public class TransferConfig {

    /**
     * id : 34
     * createdAt : 1476698415000
     * delFlag : 0
     * remarks : 转账规则为费率，最小金额为10.5元，最大金额为30.7元，费率为1%。
     * updatedAt : 1476840006000
     * type : 1
     * minMoney : 1.6
     * maxMoney : 5.7
     * money :
     * singleQuota : 5
     * todayQuota : 80
     * rate : 0.01
     * effectiveDate : 1476762944000
     * invalidDate :
     * status : 0
     * createAccid : 23204
     * updateAccid : 23204
     * operateName : DesertSnow
     * effectiveStatus : 0
     */

    private int id;
    private String createdAt;
    private int delFlag;
    private String remarks;
    private String updatedAt;
    private int type;
    private double minMoney;
    private double maxMoney;
    private String money;
    private int singleQuota;
    private int todayQuota;
    private double rate;
    private String effectiveDate;
    private String invalidDate;
    private int status;
    private int createAccid;
    private int updateAccid;
    private String operateName;
    private String effectiveStatus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getMinMoney() {
        return minMoney;
    }

    public void setMinMoney(double minMoney) {
        this.minMoney = minMoney;
    }

    public double getMaxMoney() {
        return maxMoney;
    }

    public void setMaxMoney(double maxMoney) {
        this.maxMoney = maxMoney;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getSingleQuota() {
        return singleQuota;
    }

    public void setSingleQuota(int singleQuota) {
        this.singleQuota = singleQuota;
    }

    public int getTodayQuota() {
        return todayQuota;
    }

    public void setTodayQuota(int todayQuota) {
        this.todayQuota = todayQuota;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getInvalidDate() {
        return invalidDate;
    }

    public void setInvalidDate(String invalidDate) {
        this.invalidDate = invalidDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCreateAccid() {
        return createAccid;
    }

    public void setCreateAccid(int createAccid) {
        this.createAccid = createAccid;
    }

    public int getUpdateAccid() {
        return updateAccid;
    }

    public void setUpdateAccid(int updateAccid) {
        this.updateAccid = updateAccid;
    }

    public String getOperateName() {
        return operateName;
    }

    public void setOperateName(String operateName) {
        this.operateName = operateName;
    }

    public String getEffectiveStatus() {
        return effectiveStatus;
    }

    public void setEffectiveStatus(String effectiveStatus) {
        this.effectiveStatus = effectiveStatus;
    }
}
