package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Created by zengys on 2016/5/6.
 */
public class Balance implements Serializable {
    // 返回数据
    // "amount": 0.01,
    // "icon": "",
    // "time": "2016-05-12 15:02:48.0",
    // "personalAccountId": 19,
    // "nickName": "",
    // "tradeNum": "",
    // "userName": "",
    // "realName": "",
    // "type": "5",
    // "access": "",
    // "cashOrderStatus": "1"

    // jo.put("personalAccountId", map.get("pid"));
    // jo.put("amount", map.get("a"));
    // jo.put("type", map.get("t"));
    // jo.put("access", map.get("ac"));// 支付方式支付
    // jo.put("time", map.get("ti"));
    // jo.put("icon", map.get("url"));
    // jo.put("nickName", map.get("nname"));
    // jo.put("realName", map.get("rname"));
    // jo.put("userName", map.get("uname"));
    // jo.put("tradeNum", map.get("num"));// 批次号或账单号
    // jo.put("cashOrderStatus", map.get("os"));// 1，发布；2，审核中；3，通过；4，拒绝；5，操作失败
    ///access:1，支付宝；2，微信；3，银联

// type 1，充值；2，提现成功；3，提现失败；//4，订单入账；5，余额支付；6，支付方式支付；//7，余额支付退回余额；8，支付方式支付退回支付方式；/
// /9，支付方式支付退回到余额,10,提现申请, 11,异常支付

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private double amount;
    private String icon;
    private String time;
    private int personalAccountId;
    private String nickName;
    private String tradeNum;// 批次号
    private String userName;
    private String realName;
    private String type;//11异常退款，
    private String access;//支付方式:1、支付宝2、微信3、银联，为空就是余额支付
    private String cashOrderStatus;
    private String liushui;
    private String accountno;
    private String transactionNum;// 订单号
    private String tradeNumm;
    private String account;

    private String sxfAmount;
    private String dzAmount;
    private String fuwufei;//平台服务费


    public String getSxfAmount() {
        return sxfAmount;
    }

    public void setSxfAmount(String mSxfAmount) {
        sxfAmount = mSxfAmount;
    }

    public String getDzAmount() {
        return dzAmount;
    }

    public void setDzAmount(String mDzAmount) {
        dzAmount = mDzAmount;
    }

    public String getTradeNumm() {
        return tradeNumm;
    }

    public void setTradeNumm(String mTradeNumm) {
        tradeNumm = mTradeNumm;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String mAccount) {
        account = mAccount;
    }

    /**
     * @return the accountno
     */
    public String getAccountno() {
        return accountno;
    }

    /**
     * @param accountno the accountno to set
     */
    public void setAccountno(String accountno) {
        this.accountno = accountno;
    }

    /**
     * @return the liushui
     */
    public String getLiushui() {
        return liushui;
    }

    /**
     * @param liushui the liushui to set
     */
    public void setLiushui(String liushui) {
        this.liushui = liushui;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPersonalAccountId() {
        return personalAccountId;
    }

    public void setPersonalAccountId(int personalAccountId) {
        this.personalAccountId = personalAccountId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getTradeNum() {
        return tradeNum;
    }

    public void setTradeNum(String tradeNum) {
        this.tradeNum = tradeNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getCashOrderStatus() {
        return cashOrderStatus;
    }

    public void setCashOrderStatus(String cashOrderStatus) {
        this.cashOrderStatus = cashOrderStatus;
    }

    public String getTransactionNum() {
        return transactionNum;
    }

    public void setTransactionNum(String transactionNum) {
        this.transactionNum = transactionNum;
    }

    public String getFuwufei() {
        return fuwufei;
    }

    public void setFuwufei(String fuwufei) {
        this.fuwufei = fuwufei;
    }

    public static Balance parse(String BalanceJson) {
        Balance o = null;
        if (BalanceJson != null) {
            Gson g = new Gson();
            o = g.fromJson(BalanceJson, Balance.class);
        }
        return o;
    }

    public static List<Balance> parseList(String BalanceJson) {
        List<Balance> Balances = null;
        if (BalanceJson != null) {
            Gson g = new Gson();
            Balances = g.fromJson(BalanceJson, new TypeToken<List<Balance>>() {
            }.getType());
        }
        return Balances;
    }
}
