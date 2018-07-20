package com.chewuwuyou.app.transition_entity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.chewuwuyou.app.AppContext;
import com.chewuwuyou.app.transition_utils.LastingUtils;

import java.io.Serializable;

/**
 * 登录用户
 * Created by xxy on 2016/7/21.
 */
public class UserBean implements Serializable {

    private String realName;
    private String id;
    private String telephone;
    private String email;
    private String userName;
    private int userNameStatus;
    private String password;
    private String gps;
    private String location;
    private String name;
    private String url;
    private String sex;
    private int role;
    private String vipStartTime;
    private String vipEndTime;
    private String token;
    private String orgId;
    private String userBid;
    private String province;
    private String city;
    private String district;
    private int daiLitype;
    private String provinceId;
    private String cityId;
    private String districtId;
    private String age;
    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserNameStatus() {
        return userNameStatus;
    }

    public void setUserNameStatus(int userNameStatus) {
        this.userNameStatus = userNameStatus;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getVipStartTime() {
        return vipStartTime;
    }

    public void setVipStartTime(String vipStartTime) {
        this.vipStartTime = vipStartTime;
    }

    public String getVipEndTime() {
        return vipEndTime;
    }

    public void setVipEndTime(String vipEndTime) {
        this.vipEndTime = vipEndTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getUserBid() {
        return userBid;
    }

    public void setUserBid(String userBid) {
        this.userBid = userBid;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getDaiLitype() {
        return daiLitype;
    }

    public void setDaiLitype(int daiLitype) {
        this.daiLitype = daiLitype;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    private static final String user_key = "user_key";
    private static UserBean _install;

    /**
     * 获取实例,如果有缓存系统将获取缓存中的对象
     *
     * @return
     */
    public static UserBean getInstall(Application appContext) {
        if (_install == null) {
            _install = (UserBean) LastingUtils.readObject(appContext, user_key);
//            if (_install == null) {
//                _install = new UserBean();
//            }
        }
        return _install;
    }

    /**
     * 获取实例,如果有缓存系统将获取缓存中的对象
     *
     * @return
     */
    public static UserBean getInstall(Activity activity) {
        return getInstall((AppContext) activity.getApplication());
    }

    public static UserBean getInstall(Context context) {
        return getInstall((AppContext) context.getApplicationContext());
    }

    /**
     * 保存当前用户信息
     */
    public boolean saveInfo(AppContext appContext) {
        boolean bol = LastingUtils.saveObject(appContext, this, user_key);
        if (bol) _install = this;
        return bol;
    }

    /**
     * 保存当前用户信息
     *
     * @param context
     * @return
     */
    public boolean saveInfo(Context context) {
        return saveInfo((AppContext) context.getApplicationContext());
    }

    /**
     * 保存当前用户信息
     */
    public boolean saveInfo(Activity activity) {
        return saveInfo((AppContext) activity.getApplication());
    }

    /**
     * 清空存储
     *
     * @param context
     * @return
     */
    public static boolean delete(Context context) {
        boolean bol = LastingUtils.delete(context, user_key);
        if (bol) _install = null;
        return bol;
    }

    private String requestToken = null;

    public String getRequestToken() {
        if (requestToken != null) return requestToken;
        StringBuffer buffer = new StringBuffer();
        buffer.append(telephone).append(":")
                .append(password).append(":android:")
                .append(userBid).append(":")
                .append(token);
        requestToken = buffer.toString();
        return requestToken;
    }
}
