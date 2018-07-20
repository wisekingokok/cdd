
package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @describe:查询违章数据及通过违章内容查询的扣分进行拼接
 * @author:yuyong
 * @version 1.1.0
 * @created:2014-12-3上午11:35:52
 */
public class IllegalData implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String wzdz;// 违章地址
    private String wzsj;// 违章时间
    private String wzje;// 违章金额
    private String wznr;// 违章内容
    private int wzkf;// 违章扣分
    private double fwje;// 服务费
    
    public String getWzdz() {
        return wzdz;
    }

    public void setWzdz(String wzdz) {
        this.wzdz = wzdz;
    }

    public String getWzsj() {
        return wzsj;
    }

    public void setWzsj(String wzsj) {
        this.wzsj = wzsj;
    }

    public String getWznr() {
        return wznr;
    }

    public void setWznr(String wznr) {
        this.wznr = wznr;
    }

    public String getWzje() {
        return wzje;
    }

    public void setWzje(String wzje) {
        this.wzje = wzje;
    }

    public int getWzkf() {
        return wzkf;
    }

    public void setWzkf(int wzkf) {
        this.wzkf = wzkf;
    }

    public double getFwje() {
        return fwje;
    }

    public void setFwje(double fwje) {
        this.fwje = fwje;
    }

    public static IllegalData parse(String illegalJson) {
        IllegalData u = null;
        if (illegalJson != null) {
            Gson g = new Gson();
            u = g.fromJson(illegalJson, IllegalData.class);
        }
        return u;
    }

    public static List<IllegalData> parseList(String illegalJson) {
        List<IllegalData> csts = null;
        if (illegalJson != null) {
            Gson g = new Gson();
            csts = g.fromJson(illegalJson, new TypeToken<List<IllegalData>>() {
            }.getType());
        }
        return csts;
    }
}
