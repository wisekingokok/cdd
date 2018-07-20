
package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CarBrand implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String first_letter;
    private String slug;
    private String logo_img;
    private String name;
    private String baike_link;
    private String country_type;
    private String intro;

    public String getFirst_letter() {
        return first_letter;
    }

    public void setFirst_letter(String first_letter) {
        this.first_letter = first_letter;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getLogo_img() {
        return logo_img;
    }

    public void setLogo_img(String logo_img) {
        this.logo_img = logo_img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBaike_link() {
        return baike_link;
    }

    public void setBaike_link(String baike_link) {
        this.baike_link = baike_link;
    }

    public String getCountry_type() {
        return country_type;
    }

    public void setCountry_type(String country_type) {
        this.country_type = country_type;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public static CarBrand parse(String brandJson) {
        Gson g = new Gson();
        CarBrand brand = g.fromJson(brandJson, CarBrand.class);
        return brand;
    }

    public static List<CarBrand> parseBrands(String brandJsons) {
    	List<CarBrand> brands=null;
        Gson g = new Gson();
        brands = g.fromJson(brandJsons, new TypeToken<List<CarBrand>>() {}.getType());
        return brands;
    }
}
