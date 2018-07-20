package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 任务实体类
 * 
 * @author Administrator
 */
public class Task implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * "id": 3915, "location": "四川省，成都市", "province": "四川省", "city": "成都市",
	 * "district": "", "offerCnt": 0, "orderCnt": 0, "zfbAccount": "",
	 * "wszfAccount": "", "upAccount": "", "businessId": 13468, "facilitatorId":
	 * 2809, "taskLocation": ",,", "taskProvince": "", "taskCity": "",
	 * "taskDistrict": "", "type": "违章服务", "status": "1", "pubishTime":
	 * "2016-05-19 12:36:19", "handleTime": { "date": 19, "day": 4, "hours": 12,
	 * "minutes": 36, "month": 4, "nanos": 0, "seconds": 24, "time":
	 * 1463632584000, "timezoneOffset": -480, "year": 116 }, "projectName":
	 * "101", "contact": "骨灰盒", "contactTele": "12000000013", "userDescription":
	 * "成都市武侯区", "description": "", "servicePrice": "0", "paymentAmount": 1,
	 * "orderNum": "CDD123619071_3915", "isVip": 0, "isdoortodoorservice": 0,
	 * "appointmentadd": "", "appointmenttime": "", "payType": "0",
	 * "vehicleAdd": "", "hpzl": "", "brand": "", "modelNumber": "", "buyTime":
	 * "", "frameNumber": "", "engineNumber": "", "plateNumber": "",
	 * "licenseType": "", "licenseNum": "", "fileNumber": "", "registTime": "",
	 * "valid": "", "illegaldetail": [], "subtasks": [], "idcardPic1": "",
	 * "idcardPic2": "", "profilePic": "", "vehiclelicensePic1": "",
	 * "vehiclelicensePic2": "", "licensePic1": "", "licensePic2": "", "flag":
	 * "3", "userId": "2809", "exactPhone": "15928914689", "name": "成都车务代办张强",
	 * "url": "ISD:/upload/20151024151348023.jpeg", "sex": "0", "serv": "1"
	 */

	private String handleTime;
	private String city;
	private String province;
	private String district;
	private String servicePrice;
	private String payType;
	private List<Offer> offers;
	private String id; // 任务id
	private String taskLocation; // 任务归属地
	private String location;// 发布方所在地
	private String taskProvince;
	private String taskCity;
	private String taskDistrict;
	private int offerCnt;// 报价次数
	private String isPinglun;// 是否评价
	private String orderCnt;// 接单人数
	private String zfbAccount;// 支付宝账户
	private String upAccount;// 银联账户
	private String wszfAccount;// 微信账户
	private String businessId;// 商家id
	private String facilitatorId;// 接单人的ID
	private String type; // 任务类别 违章服务 车辆服务 驾证服务 综合服务
	private String licenseType;// 驾照类型
	private String status;// 任务状态
	private String pubishTime;// 发布时间
	private String projectName;// 项目名称
	private String contact;// 联系人
	private String contactTele;// 联系电话
	private String userDescription;// 商家对商家下单任务内容

	private String appointmentadd;// 服务地址
	private String description;// 描述
	private Double paymentAmount; // 支付金额123
	private String orderNum; // 订单号
	private String isVip;// 是否为vip任务 0不是/1是
	private String vehicleAdd; // 车辆归属地
	private String hpzl; // 号牌种类 （车辆类型 如02表示小型汽车）
	private String brand;// 品牌
	private String modelNumber;// 车型
	private String buyTime; // 购车年份
	private String frameNumber;// 车架号
	private String engineNumber;// 发动机号
	private String appointmenttime;// 上门服务时间
	private String plateNumber;// 车牌号
	private int isdoortodoorservice;// 是否上门服务

	private String licenseNum;// 驾证号
	private String fileNumber;// 档案编号
	private String registTime;// 登记时间
	private String valid;// 有效期
	private String flag;// "1"用户给我的订单,"2"商家给我的订单,3派发的订单
	private List<SubTask> subtasks;// 子任务
	private String url;// 处理者的头像
	private String serv;// 是商家还是客服 :1是商家，2是客服
	private String name;// 处理人的名称
	private String userId;
	private String reason;
	private String exactPhone;
	private String expressNo;//物流运单号
	private String companyNo;//物流公司码
	private String revExpressNo;//反向运单号
	private String revCompanyNo;//反向物流公司码
	private String provideBill;
	private String exceptionFlag;//根据返回的状态
	private String max;//最大可确认价格
	private String min;//最小可确认价格

	public String getHandleTime() {
		return handleTime;
	}

	public void setHandleTime(String handleTime) {
		this.handleTime = handleTime;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getServicePrice() {
		return servicePrice;
	}

	public void setServicePrice(String servicePrice) {
		this.servicePrice = servicePrice;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public List<Offer> getOffers() {
		return offers;
	}

	public void setOffers(List<Offer> offers) {
		this.offers = offers;
	}

	public String getExceptionFlag() {
		return exceptionFlag;
	}

	public void setExceptionFlag(String exceptionFlag) {
		this.exceptionFlag = exceptionFlag;
	}

	public String getProvideBill() {
		return provideBill;
	}

	public void setProvideBill(String provideBill) {
		this.provideBill = provideBill;
	}

	private List<IllegalDetailsData> illegaldetail;

	public boolean isCheck = false;

	public String getExpressNo() {
		return expressNo;
	}

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}

	public String getCompanyNo() {
		return companyNo;
	}

	public void setCompanyNo(String companyNo) {
		this.companyNo = companyNo;
	}

	public String getRevExpressNo() {
		return revExpressNo;
	}

	public void setRevExpressNo(String revExpressNo) {
		this.revExpressNo = revExpressNo;
	}

	public String getRevCompanyNo() {
		return revCompanyNo;
	}

	public void setRevCompanyNo(String revCompanyNo) {
		this.revCompanyNo = revCompanyNo;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean check) {
		isCheck = check;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getTaskLocation() {
		return taskLocation;
	}

	public void setTaskLocation(String taskLocation) {
		this.taskLocation = taskLocation;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPubishTime() {
		return pubishTime;
	}

	public void setPubishTime(String pubishTime) {
		this.pubishTime = pubishTime;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getAppointmentadd() {
		return appointmentadd;
	}

	public void setAppointmentadd(String appointmentadd) {
		this.appointmentadd = appointmentadd;
	}

	public String getAppointmenttime() {
		return appointmenttime;
	}

	public void setAppointmenttime(String appointmenttime) {
		this.appointmenttime = appointmenttime;
	}

	public String getContactTele() {
		return contactTele;
	}

	public void setContactTele(String contactTele) {
		this.contactTele = contactTele;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(Double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getIsVip() {
		return isVip;
	}

	public void setIsVip(String isVip) {
		this.isVip = isVip;
	}

	public String getVehicleAdd() {
		return vehicleAdd;
	}

	public void setVehicleAdd(String vehicleAdd) {
		this.vehicleAdd = vehicleAdd;
	}

	public String getHpzl() {
		return hpzl;
	}

	public void setHpzl(String hpzl) {
		this.hpzl = hpzl;
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

	public String getBuyTime() {
		return buyTime;
	}

	public void setBuyTime(String buyTime) {
		this.buyTime = buyTime;
	}

	public String getFrameNumber() {
		return frameNumber;
	}

	public void setFrameNumber(String frameNumber) {
		this.frameNumber = frameNumber;
	}

	public String getEngineNumber() {
		return engineNumber;
	}

	public void setEngineNumber(String engineNumber) {
		this.engineNumber = engineNumber;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public int getIsdoortodoorservice() {
		return isdoortodoorservice;
	}

	public void setIsdoortodoorservice(int isdoortodoorservice) {
		this.isdoortodoorservice = isdoortodoorservice;
	}

	public String getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}

	public String getLicenseNum() {
		return licenseNum;
	}

	public void setLicenseNum(String licenseNum) {
		this.licenseNum = licenseNum;
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

	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public List<SubTask> getSubtasks() {
		return subtasks;
	}

	public void setSubtasks(List<SubTask> subtasks) {
		this.subtasks = subtasks;
	}

	public String getUserDescription() {
		return userDescription;
	}

	public void setUserDescription(String userDescription) {
		this.userDescription = userDescription;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getServ() {
		return serv;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setServ(String serv) {
		this.serv = serv;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getExactPhone() {
		return exactPhone;
	}

	public void setExactPhone(String exactPhone) {
		this.exactPhone = exactPhone;
	}

	public List<IllegalDetailsData> getIllegaldetail() {
		return illegaldetail;
	}

	public void setIllegaldetail(List<IllegalDetailsData> illegaldetail) {
		this.illegaldetail = illegaldetail;
	}

	public String getZfbAccount() {
		return zfbAccount;
	}

	public void setZfbAccount(String zfbAccount) {
		this.zfbAccount = zfbAccount;
	}

	public String getUpAccount() {
		return upAccount;
	}

	public void setUpAccount(String upAccount) {
		this.upAccount = upAccount;
	}

	public String getWszfAccount() {
		return wszfAccount;
	}

	public void setWszfAccount(String wszfAccount) {
		this.wszfAccount = wszfAccount;
	}

	public int getOfferCnt() {
		return offerCnt;
	}

	public void setOfferCnt(int offerCnt) {
		this.offerCnt = offerCnt;
	}

	public String getTaskProvince() {
		return taskProvince;
	}

	public void setTaskProvince(String taskProvince) {
		this.taskProvince = taskProvince;
	}

	public String getTaskCity() {
		return taskCity;
	}

	public void setTaskCity(String taskCity) {
		this.taskCity = taskCity;
	}

	public String getTaskDistrict() {
		return taskDistrict;
	}

	public void setTaskDistrict(String taskDistrict) {
		this.taskDistrict = taskDistrict;
	}

	public String getIsPinglun() {
		return isPinglun;
	}

	public void setIsPinglun(String isPinglun) {
		this.isPinglun = isPinglun;
	}

	public String getOrderCnt() {
		return orderCnt;
	}

	public void setOrderCnt(String orderCnt) {
		this.orderCnt = orderCnt;
	}

	public String getFacilitatorId() {
		return facilitatorId;
	}

	public void setFacilitatorId(String facilitatorId) {
		this.facilitatorId = facilitatorId;
	}

	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}

	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}

	public static Task parse(String userJson) {
		Task u = null;
		if (userJson != null) {
			Gson g = new Gson();
			u = g.fromJson(userJson, Task.class);
		}
		return u;
	}

	public static List<Task> parseList(String userJson) {
		List<Task> csts = null;
		if (userJson != null) {
			Gson g = new Gson();
			csts = g.fromJson(userJson, new TypeToken<List<Task>>() {
			}.getType());
		}
		return csts;
	}
}
