
package com.chewuwuyou.app.bean;

import java.io.Serializable;

import com.google.gson.Gson;

/**
 * 用户
 * 
 * @author zh
 */
public class User implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;// 用户编号
    private String userBid;// 商家id
    private String telephone;// 电话
    private String email;// 邮箱
    private String userName;// 用户名
    private String password;// 密码
    private String realName;// 姓名
    private String identityCard;// 身份证号
    private String alipayAccount;// 支付宝帐号
    private String gps;// GPS点
    private String location;// 地区
    private String role;// 用户角色

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserBid() {
        return userBid;
    }

    public void setUserBid(String userBid) {
        this.userBid = userBid;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getAlipayAccount() {
        return alipayAccount;
    }

    public void setAlipayAccount(String alipayAccount) {
        this.alipayAccount = alipayAccount;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public static User parse(String userJson) {
        User u = null;
        if (userJson != null) {
            Gson g = new Gson();
            u = g.fromJson(userJson, User.class);
        }
        return u;
    }
}
