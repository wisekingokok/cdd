package com.chewuwuyou.app.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.List;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:车友实体
 * @author:yuyong
 * @date:2015-4-10下午4:04:39
 * @version:1.2.1
 */
public class PersonHome implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    // "userName": "1000005",
    // "isBusOrKeFu": 1,
    // "busUserId": 5,
    // "handlerId": 0,
    // "imageBack": "ISD:/upload/20150914172112147.jpeg",
    // "urls": [
    // {
    // "id": 3484,
    // "url": "ISD:/upload/20151028142119685.jpg"
    // },
    // {
    // "id": 3485,
    // "url": "ISD:/upload/20151028142146790.jpg"
    // },
    // {
    // "id": 3486,
    // "url": "ISD:/upload/20151028142522765.jpg"
    // },
    // {
    // "id": 3524,
    // "url": "IS1:/upload/20151029173437318.jpg"
    // },
    // {
    // "id": 6959,
    // "url": "IS1:/upload/20160521215044665.jpeg"
    // },
    // {
    // "id": 6960,
    // "url": "IS9:/upload/20160521215055918.jpeg"
    // }
    // ],
    // "nick": "聊几句",
    // "sign": "停机了",
    // "DDNumber": "1000005",
    // "userId": 5,
    // "age": "0",
    // "starsit": "",
    // "sex": "0",
    // "pro": "????",
    // "location": "??????",
    // "carBrand": "???",
    // "hobby": "???",
    // "personInfo": "???????????",
    // "registertime": "2015-04-01 14:58:20.0",
    // "birthday": "2015-03-31",
    // "friend": "1",
    // "noteName": ""

    private String sex;
    private String location;
    private String registertime;
    // private String[] urls;
    private List<TuItem> urls;
    private String carBrand;
    private String hobby;// 爱好
    private String noteName;//这是备注。。。。。
    private String sign;// 车友心情
    private String nick;
    private String userId;
    private String pro;// 职业
    private String userName;
    private String personInfo;// 个人说明
    private String imageBack;// 照片墙背景
    private String friend;//1是好友，0不是好友
    private String black;//black=4表示：甲乙两人互相不是黑名单用户 1：甲的黑名单只有乙         2：乙的黑名单只有甲,3：情况很多（甲乙分别的黑名单用户加起来大于等于2）
    private String starsit;
    private String age;
    private int isBusOrKeFu;// 0：普通用户；1：商家；2：客服
    private String busUserId; // 商家的UserId
    private int handlerId;// 客服的handleId
    private String telephone;// 用户电话
    private String birthday;//生日

    private String requestRemarks;//备注内容

    private String isRequestFriend;//isRequestFriend 申请状态 0未申请  1申请


    public String getRequestRemarks() {
        return requestRemarks;
    }

    public void setRequestRemarks(String requestRemarks) {
        this.requestRemarks = requestRemarks;
    }

    public String getIsRequestFriend() {
        return isRequestFriend;
    }

    public void setIsRequestFriend(String isRequestFriend) {
        this.isRequestFriend = isRequestFriend;
    }

    public String getBlack() {
        return black;
    }

    public void setBlack(String mBlack) {
        black = mBlack;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    private String busType;

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }


    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLocation() {
        return location == null ? "city" : location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRegistertime() {
        return registertime;
    }

    public void setRegistertime(String registertime) {
        this.registertime = registertime;
    }

    public List<TuItem> getUrls() {
        return urls;
    }

    public void setUrls(List<TuItem> urls) {
        this.urls = urls;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPro() {
        return pro;
    }

    public void setPro(String pro) {
        this.pro = pro;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(String personInfo) {
        this.personInfo = personInfo;
    }

    public String getImageBack() {
        return imageBack;
    }

    public void setImageBack(String imageBack) {
        this.imageBack = imageBack;
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }

    public String getStarsit() {
        return starsit;
    }

    public void setStarsit(String starsit) {
        this.starsit = starsit;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public int getIsBusOrKeFu() {
        return isBusOrKeFu;
    }

    public void setIsBusOrKeFu(int isBusOrKeFu) {
        this.isBusOrKeFu = isBusOrKeFu;
    }

    public String getBusUserId() {
        return busUserId;
    }

    public void setBusUserId(String busUserId) {
        this.busUserId = busUserId;
    }

    public int getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(int handlerId) {
        this.handlerId = handlerId;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public static PersonHome parse(String personHomeJson) {
        PersonHome personHome = null;
        if (personHomeJson != null) {
            Gson g = new Gson();
            personHome = g.fromJson(personHomeJson, PersonHome.class);
        }
        return personHome;
    }

    public static List<PersonHome> parseList(String personHomeJson) {
        List<PersonHome> personHomes = null;
        if (personHomeJson != null) {
            Gson g = new Gson();
            personHomes = g.fromJson(personHomeJson,
                    new TypeToken<List<PersonHome>>() {
                    }.getType());
        }
        return personHomes;
    }
}
