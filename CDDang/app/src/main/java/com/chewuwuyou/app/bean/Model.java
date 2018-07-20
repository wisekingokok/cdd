package com.chewuwuyou.app.bean;

import java.io.Serializable;

public class Model implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fac;
    private String name;
    private String icon;
    private String price;
    public String getFac() {
        return fac;
    }
    public void setFac(String fac) {
        this.fac = fac;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    
}