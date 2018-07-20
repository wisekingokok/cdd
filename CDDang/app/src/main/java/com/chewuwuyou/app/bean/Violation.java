
package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 违章实体类
 * 
 * @author Administrator
 */
public class Violation implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6804891712801475442L;
    //违章地址、时间、金额、内容
    private String wfdz;
    private String wfsj;
    private String wfje;
    private String wfnr;

    public String getWfdz() {
        return wfdz;
    }

    public void setWfdz(String wfdz) {
        this.wfdz = wfdz;
    }

    public String getWfsj() {
        return wfsj;
    }

    public void setWfsj(String wfsj) {
        this.wfsj = wfsj;
    }

    public String getWfje() {
        return wfje;
    }

    public void setWfje(String wfje) {
        this.wfje = wfje;
    }

    public String getWfnr() {
        return wfnr;
    }

    public void setWfnr(String wfnr) {
        this.wfnr = wfnr;
    }

    public static Violation parse(String userJson) {
        Violation u = null;
        if (userJson != null) {
            Gson g = new Gson();
            u = g.fromJson(userJson, Violation.class);
        }
        return u;
    }

    public static List<Violation> parseList(String userJson) {
        List<Violation> csts = null;
        if (userJson != null) {
            Gson g = new Gson();
            csts = g.fromJson(userJson, new TypeToken<List<Violation>>() {
            }.getType());
        }
        return csts;
    }

}
