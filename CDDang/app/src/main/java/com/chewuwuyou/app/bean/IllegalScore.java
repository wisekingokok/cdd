
package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @describe:违章扣分实体
 * @author:yuyong
 * @version 1.1.0
 * @created:2014-12-16下午5:18:02
 */
public class IllegalScore implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String serviceName;
    private String scoreReduce;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getScoreReduce() {
        return scoreReduce;
    }

    public void setScoreReduce(String scoreReduce) {
        this.scoreReduce = scoreReduce;
    }

    public static IllegalScore parse(String scorejson) {
        Gson g = new Gson();
        IllegalScore license = g.fromJson(scorejson, IllegalScore.class);
        return license;
    }

    public static List<IllegalScore> parseList(String scorejson) {
        Gson g = new Gson();
        List<IllegalScore> licenses = g.fromJson(scorejson,
                new TypeToken<List<IllegalScore>>() {
                }.getType());
        return licenses;
    }

}
