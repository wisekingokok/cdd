package com.chewuwuyou.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by CLOUD on 2016/8/19.
 */
public class CompanyService implements Parcelable {


    /**
     * area : 东城区
     * avatar :
     * city : 北京市
     * content : 我是内容。。。
     * id : 14
     * nickName : songwang
     * province : 北京市
     * publishTime : 2016-08-19 16:09:19
     * shieldReason :
     * shieldTime :
     * shieldUserid :
     * title : 我是标题。。。
     * type : 1
     * userId : 22057
     */

    private String area;
    private String avatar;
    private String city;
    private String content;
    private String id;
    private String nickName;
    private String province;
    private String publishTime;
    private String shieldReason;
    private String shieldTime;
    private String shieldUserid;
    private String title;
    private String type;
    private String userId;
    private String businessId;

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String mBusinessId) {
        businessId = mBusinessId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getShieldReason() {
        return shieldReason;
    }

    public void setShieldReason(String shieldReason) {
        this.shieldReason = shieldReason;
    }

    public String getShieldTime() {
        return shieldTime;
    }

    public void setShieldTime(String shieldTime) {
        this.shieldTime = shieldTime;
    }

    public String getShieldUserid() {
        return shieldUserid;
    }

    public void setShieldUserid(String shieldUserid) {
        this.shieldUserid = shieldUserid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.area);
        dest.writeString(this.avatar);
        dest.writeString(this.city);
        dest.writeString(this.content);
        dest.writeString(this.id);
        dest.writeString(this.nickName);
        dest.writeString(this.province);
        dest.writeString(this.publishTime);
        dest.writeString(this.shieldReason);
        dest.writeString(this.shieldTime);
        dest.writeString(this.shieldUserid);
        dest.writeString(this.title);
        dest.writeString(this.type);
        dest.writeString(this.userId);
    }

    public CompanyService() {
    }

    protected CompanyService(Parcel in) {
        this.area = in.readString();
        this.avatar = in.readString();
        this.city = in.readString();
        this.content = in.readString();
        this.id = in.readString();
        this.nickName = in.readString();
        this.province = in.readString();
        this.publishTime = in.readString();
        this.shieldReason = in.readString();
        this.shieldTime = in.readString();
        this.shieldUserid = in.readString();
        this.title = in.readString();
        this.type = in.readString();
        this.userId = in.readString();
    }

    public static final Creator<CompanyService> CREATOR = new Creator<CompanyService>() {
        @Override
        public CompanyService createFromParcel(Parcel source) {
            return new CompanyService(source);
        }

        @Override
        public CompanyService[] newArray(int size) {
            return new CompanyService[size];
        }
    };


    public static List<CompanyService> parseList(String bannerJson) {
        List<CompanyService> csts = null;
        if (bannerJson != null) {
            Gson g = new Gson();
            csts = g.fromJson(bannerJson, new TypeToken<List<CompanyService>>() {
            }.getType());
        }
        return csts;
    }


}
