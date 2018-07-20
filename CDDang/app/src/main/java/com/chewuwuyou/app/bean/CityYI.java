
package com.chewuwuyou.app.bean;


public class CityYI {
	
	
	private int provinceId;//省Id
	private int cityId;//市Id
	private int districtId;//区Id
    private String provinceName;//省
    private String cityName;//市
    private String districtName;//区
    private String cityPinYin;//城市拼音
    
    
    public CityYI() {
		
	}
    
	public CityYI(int provinceId, int cityId, int districtId,
			String provinceName, String cityName, String districtName) {
		super();
		this.provinceId = provinceId;
		this.cityId = cityId;
		this.districtId = districtId;
		this.provinceName = provinceName;
		this.cityName = cityName;
		this.districtName = districtName;
		
	}
	public int getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	public int getDistrictId() {
		return districtId;
	}
	public void setDistrictId(int districtId) {
		this.districtId = districtId;
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
	public String getCityPinYin() {
		return cityPinYin;
	}
	public void setCityPinYin(String cityPinYin) {
		this.cityPinYin = cityPinYin;
	}
    
    
    
    
}
