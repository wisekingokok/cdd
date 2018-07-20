
package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @describe：驾证信息实体类
 * @author ：caixuemei
 * @created ：2014-7-30下午8:19:54
 */
public class License implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id; // 驾证id
    private String licenseNum; // 驾证号
    private String licenseCity; // 驾证归属地
    private String licenseType; // 驾证类型
    private String fileNumber; // 档案编号
    private String registTime; // 登记时间
    private String owner;// 驾证持有人姓名
    private String valid;// 驾证有效期
    private int isVip;// 是否是VIP
    private String vipStartTime;// VIP开始时间
    private String vipEndTime;// VIP结束时间
    private String noteName;// 备注名
    private String licensePic1;
    private String licensePic2;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLicenseNum() {
        return licenseNum;
    }

    public void setLicenseNum(String licenseNum) {
        this.licenseNum = licenseNum;
    }

    public String getLicenseCity() {
        return licenseCity;
    }

    public void setLicenseCity(String licenseCity) {
        this.licenseCity = licenseCity;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getRegistTime() {
        return registTime;
    }

    public void setRegistTime(String registTime) {
        this.registTime = registTime;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public int getIsVip() {
        return isVip;
    }

    public void setIsVip(int isVip) {
        this.isVip = isVip;
    }

    public String getVipStartTime() {
        return vipStartTime;
    }

    public void setVipStartTime(String vipStartTime) {
        this.vipStartTime = vipStartTime;
    }

    public String getVipEndTime() {
        return vipEndTime;
    }

    public void setVipEndTime(String vipEndTime) {
        this.vipEndTime = vipEndTime;
    }

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    public String getLicensePic1() {
        return licensePic1;
    }

    public void setLicensePic1(String licensePic1) {
        this.licensePic1 = licensePic1;
    }

    public String getLicensePic2() {
        return licensePic2;
    }

    public void setLicensePic2(String licensePic2) {
        this.licensePic2 = licensePic2;
    }

    public static License parse(String licenseJson) {
        Gson g = new Gson();
        License license = g.fromJson(licenseJson, License.class);
        return license;
    }

    public static List<License> parseList(String licenseListJson) {
        Gson g = new Gson();
        List<License> licenses = g.fromJson(licenseListJson,
                new TypeToken<List<License>>() {
                }.getType());
        return licenses;
    }

}
