package com.chewuwuyou.app.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.List;

public class ValueTask implements Serializable {
    private String id;
    private String userId;
    private String facilitatorId;
    private String taskLocation;
    private String type;
    private String status;
    private String userDeleteStatus;
    private String facilitatorDeleteStatus;
    private String pubishTime;
    private String handleTime;
    private String projectName;
    private String contact;
    private String contactTele;
    private String userDescription;
    private String description;
    private String servicePrice;
    private double paymentAmount;
    private String isVip;
    private String isdoortodoorservice;
    private String appointmentadd;
    private String appointmenttime;
    private String payType;
    private String businessId;
    private String provinceId;
    private String provinceName;
    private String cityName;
    private String districtName;
    private String cityId;
    private String districtId;
    private String offerCnt;
    private String orderCnt;
    private String orderNum;
    private String flag;
    private String url;
    private String name;
    public boolean isCheck = false;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFacilitatorId() {
        return facilitatorId;
    }

    public void setFacilitatorId(String facilitatorId) {
        this.facilitatorId = facilitatorId;
    }

    public String getTaskLocation() {
        return taskLocation;
    }

    public void setTaskLocation(String taskLocation) {
        this.taskLocation = taskLocation;
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

    public String getUserDeleteStatus() {
        return userDeleteStatus;
    }

    public void setUserDeleteStatus(String userDeleteStatus) {
        this.userDeleteStatus = userDeleteStatus;
    }

    public String getFacilitatorDeleteStatus() {
        return facilitatorDeleteStatus;
    }

    public void setFacilitatorDeleteStatus(String facilitatorDeleteStatus) {
        this.facilitatorDeleteStatus = facilitatorDeleteStatus;
    }

    public String getPubishTime() {
        return pubishTime;
    }

    public void setPubishTime(String pubishTime) {
        this.pubishTime = pubishTime;
    }

    public String getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(String handleTime) {
        this.handleTime = handleTime;
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

    public String getContactTele() {
        return contactTele;
    }

    public void setContactTele(String contactTele) {
        this.contactTele = contactTele;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getIsVip() {
        return isVip;
    }

    public void setIsVip(String isVip) {
        this.isVip = isVip;
    }

    public String getIsdoortodoorservice() {
        return isdoortodoorservice;
    }

    public void setIsdoortodoorservice(String isdoortodoorservice) {
        this.isdoortodoorservice = isdoortodoorservice;
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

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }


    public String getOfferCnt() {
        return offerCnt;
    }

    public void setOfferCnt(String offerCnt) {
        this.offerCnt = offerCnt;
    }

    public String getOrderCnt() {
        return orderCnt;
    }

    public void setOrderCnt(String orderCnt) {
        this.orderCnt = orderCnt;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public static ValueTask parse(String userJson) {
        ValueTask u = null;
        if (userJson != null) {
            Gson g = new Gson();
            u = g.fromJson(userJson, ValueTask.class);
        }
        return u;
    }

    public static List<ValueTask> parseList(String userJson) {
        List<ValueTask> csts = null;
        if (userJson != null) {
            Gson g = new Gson();
            csts = g.fromJson(userJson, new TypeToken<List<ValueTask>>() {
            }.getType());
        }
        return csts;
    }


}
