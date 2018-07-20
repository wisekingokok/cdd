package com.chewuwuyou.app.bean;

import java.io.Serializable;

/**
 * @version 1.1.0
 * @describe:爱车账目实体类
 * @author:yuyong
 * @created:2014-10-23下午3:45:37
 */
public class CarBrandBook implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String brandName;
    private String brandImage;


    public CarBrandBook(String brandName, String brandImage) {
        super();
        this.brandName = brandName;
        this.brandImage = brandImage;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandImage() {
        return brandImage;
    }

    public void setBrandImage(String brandImage) {
        this.brandImage = brandImage;
    }

    @Override
    public String toString() {
        return "CarBrandBook [brandName=" + brandName + ", brandImage="
                + brandImage + "]";
    }


}
