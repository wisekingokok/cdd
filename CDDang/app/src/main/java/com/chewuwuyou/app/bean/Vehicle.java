package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 车辆信息实体类
 * 
 * @author Administrator
 */
public class Vehicle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// api response
	private String id; // 车辆id
	private String brand;// 车辆品牌
	private String modelNumber;// 品牌类型
	private String plateNumber;// 车牌号"苏A5386R"
	private String plateNumStype;// 车辆类型 "c1"
	private String vehicleAdd;// 241#湘潭
	private String registTime;// 车辆登记日期
	private String annualSurveyTime;// 年检日期
	private String insuranceTime;// 保险日期
	private String engineNumber;// 车辆发动机号
	private String frameNumber;// 车架号
	private int defaultCar;// 默认车辆标识
	// yu yong add
	private String buyTime;// 购车时间
	private int isVip;// 是否是VIP
	private String vipStartTime;// VIP开始时间
	private String vipEndTime;// VIP结束时间
	private String hpzl;// 号牌类型
	private String noteName;// 备注名
	private String vehiclelicensePic1;// 行驶证正页
	private String vehiclelicensePic2;// 行驶证副页
	private int city_id;
	private String city_name;
	private List<WeizhangGroupItem> weiZhangGroups;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModelNumber() {
		return modelNumber;
	}

	public void setModelNumber(String modelNumber) {
		this.modelNumber = modelNumber;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public String getPlateNumStype() {
		return plateNumStype;
	}

	public void setPlateNumStype(String plateNumStype) {
		this.plateNumStype = plateNumStype;
	}

	public String getVehicleAdd() {
		return vehicleAdd;
	}

	public void setVehicleAdd(String vehicleAdd) {
		this.vehicleAdd = vehicleAdd;
	}

	public String getRegistTime() {
		return registTime;
	}

	public void setRegistTime(String registTime) {
		this.registTime = registTime;
	}

	public String getAnnualSurveyTime() {
		return annualSurveyTime;
	}

	public void setAnnualSurveyTime(String annualSurveyTime) {
		this.annualSurveyTime = annualSurveyTime;
	}

	public String getInsuranceTime() {
		return insuranceTime;
	}

	public void setInsuranceTime(String insuranceTime) {
		this.insuranceTime = insuranceTime;
	}

	public String getEngineNumber() {
		return engineNumber;
	}

	public void setEngineNumber(String engineNumber) {
		this.engineNumber = engineNumber;
	}

	public String getFrameNumber() {
		return frameNumber;
	}

	public void setFrameNumber(String frameNumber) {
		this.frameNumber = frameNumber;
	}

	public String getBuyTime() {
		return buyTime;
	}

	public void setBuyTime(String buyTime) {
		this.buyTime = buyTime;
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

	public String getHpzl() {
		return hpzl;
	}

	public void setHpzl(String hpzl) {
		this.hpzl = hpzl;
	}

	public String getNoteName() {
		return noteName;
	}

	public void setNoteName(String noteName) {
		this.noteName = noteName;
	}

	public int getDefaultCar() {
		return defaultCar;
	}

	public void setDefaultCar(int defaultCar) {
		this.defaultCar = defaultCar;
	}

	public String getVehiclelicensePic1() {
		return vehiclelicensePic1;
	}

	public void setVehiclelicensePic1(String vehiclelicensePic1) {
		this.vehiclelicensePic1 = vehiclelicensePic1;
	}

	public String getVehiclelicensePic2() {
		return vehiclelicensePic2;
	}

	public void setVehiclelicensePic2(String vehiclelicensePic2) {
		this.vehiclelicensePic2 = vehiclelicensePic2;
	}

	public static Vehicle parse(String userJson) {
		Vehicle u = null;
		if (userJson != null) {
			Gson g = new Gson();
			u = g.fromJson(userJson, Vehicle.class);
		}
		return u;
	}

	public static List<Vehicle> parseList(String userJson) {
		List<Vehicle> csts = null;
		if (userJson != null) {
			Gson g = new Gson();
			csts = g.fromJson(userJson, new TypeToken<List<Vehicle>>() {
			}.getType());
		}
		return csts;
	}

	public int getCity_id() {
		return city_id;
	}

	public void setCity_id(int city_id) {
		this.city_id = city_id;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public List<WeizhangGroupItem> getWeiZhangGroups() {
		return weiZhangGroups;
	}

	public void setWeiZhangGroups(List<WeizhangGroupItem> weiZhangGroups) {
		this.weiZhangGroups = weiZhangGroups;
	}
}
