
package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @describe:请求服务价格实体
 * @author:yuyong
 * @version 1.1.0
 * @created:2014-12-2下午5:51:12
 */
public class ServicePriceData implements Serializable {

    // "returnCode": "0", //0：正常 1：异常
    // "serviceName": "0", //服务名
    // "serviceFee": "", //车辆和违章的规费
    // "servicePrice": "", //服务费
    // {"servicePrice":9999,"serviceFee":"30","projectNum":203,"serviceName":"车辆上牌"}

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String projectNum;
    private String serviceName;
    private String serviceFee;
    private String servicePrice;

    public String getProjectNum() {
        return projectNum;
    }

    public void setProjectNum(String projectNum) {
        this.projectNum = projectNum;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(String serviceFee) {
        this.serviceFee = serviceFee;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }

    public static ServicePriceData parse(String servicePriceJson) {
        Gson g = new Gson();
        ServicePriceData sp = g.fromJson(servicePriceJson, ServicePriceData.class);
        return sp;
    }

    public static List<ServicePriceData> parserList(String servicePriceJsons) {
        List<ServicePriceData> series = null;
        Gson g = new Gson();
        series = g.fromJson(servicePriceJsons, new TypeToken<List<ServicePriceData>>() {
        }.getType());
        return series;
    }
}
