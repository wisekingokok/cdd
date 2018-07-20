package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @describe：个人信息
 * @author ：caixuemei
 * @created ：2014-8-4下午8:33:30
 */
public class PersonalInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// {"birthday":"2015-03-10","sex":0,"location":"重庆市北碚区","nickName":"哈哈哈","carBrand":"阿尔法·罗密欧\/4C","userNameStatus":0,"userName":"cwwy_18302855661","signature":"","url":"\/upload\/20150312171902946.png"}
	private String url;// 头像地址
	private String nickName;// 昵称
	private String userName;// 车务无忧号
	private int userNameStatus;// 车务无忧号状态
	private int sex;// 性别
	private String location;// 地区
	private String carBrand;// 爱车型号
	private String signature;// 个性签名
	private String birthday;// 生日
	
	private int tieZis;
	private int quanWens;
	private int yueYues;

	public int getTieZis() {
		return tieZis;
	}

	public void setTieZis(int tieZis) {
		this.tieZis = tieZis;
	}

	public int getQuanWens() {
		return quanWens;
	}

	public void setQuanWens(int quanWens) {
		this.quanWens = quanWens;
	}

	public int getYueYues() {
		return yueYues;
	}

	public void setYueYues(int yueYues) {
		this.yueYues = yueYues;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getUserNameStatus() {
		return userNameStatus;
	}

	public void setUserNameStatus(int userNameStatus) {
		this.userNameStatus = userNameStatus;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCarBrand() {
		return carBrand;
	}

	public void setCarBrand(String carBrand) {
		this.carBrand = carBrand;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public static PersonalInfo parse(String personalInfoJson) {
		PersonalInfo personalInfo = null;
		try {
			if (personalInfoJson != null && !"".equals(personalInfoJson.trim())) {
				Gson g = new Gson();
				personalInfo = g.fromJson(personalInfoJson, PersonalInfo.class);
			}
		} catch (Exception e) {
		}
		return personalInfo;
	}

	public static List<PersonalInfo> parseList(String personalInfolistJson) {
		Gson g = new Gson();
		List<PersonalInfo> personalInfos = g.fromJson(personalInfolistJson,
				new TypeToken<List<PersonalInfo>>() {
				}.getType());
		return personalInfos;
	}

}
