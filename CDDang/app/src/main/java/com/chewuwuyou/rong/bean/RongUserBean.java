package com.chewuwuyou.rong.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xxy on 2016/9/17 0017.
 */
public class RongUserBean {

    /**
     * DDNumber : 1021681
     * birthday :
     * sex : 0
     * busType : 0
     * location : 北京市
     * registertime : 2016-07-14 13:48:09
     * noteName :
     * starsit :
     * age : 0
     * userId : 21681
     * headUrl : /upload/2016091714270921681012.jpg
     * businessId :
     * busUserId :
     * black : 4
     * personInfo :
     * isBusOrKeFu : 0
     * urls : [{"id":"39711","url":"/upload/2016091714270921681012.jpg"},{"id":"23750","url":"/upload/2016080117312121681775.jpg"},{"id":"23743","url":"/upload/2016080117281221681778.jpg"},{"id":"23730","url":"/upload/2016080117223121681967.jpg"},{"id":"39710","url":"/upload/2016091714230921681866.jpg"}]
     * carBrand : 宝马/宝马1系
     * hobby :
     * imageBack : /upload/2016091916481621681391.jpg
     * sign : 空间里
     * handlerId : 0
     * nick : 12456
     * pro : 模特
     * realName :
     * mailingAddress : {"region":"","id":"","phone":"","receiver":"","address":"","zipCode":"","defaultAddress":""}
     * telephone : 15883635438
     * friend : 0
     */

    private String DDNumber;
    private String birthday;
    private String sex;
    private String busType;
    private String location;
    private String registertime;
    private String noteName;
    private String starsit;
    private String age;
    private String userId;
    private String headUrl;
    private String businessId;
    private String busUserId;
    private String black;
    private String personInfo;
    private int isBusOrKeFu;
    private String carBrand;
    private String hobby;
    private String imageBack;
    private String sign;
    private String handlerId;
    private String nick;
    private String pro;
    private String realName;
    private String userName;
    /**
     * region :
     * id :
     * phone :
     * receiver :
     * address :
     * zipCode :
     * defaultAddress :
     */

    private MailingAddressBean mailingAddress;
    private String telephone;
    private String friend;

    /**
     * id : 39711
     * url : /upload/2016091714270921681012.jpg
     */

    public RongUserBean() {
    }


    public RongUserBean(String userId, String nick, String url) {
        this.userId = userId;
        this.nick = nick;
        this.urls = new ArrayList<>();
        urls.add(new UrlsBean(url));
    }

    private List<UrlsBean> urls;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDDNumber() {
        return DDNumber;
    }

    public void setDDNumber(String DDNumber) {
        this.DDNumber = DDNumber;
    }

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

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public String getLocation() {
        return location;
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

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBusUserId() {
        return busUserId;
    }

    public void setBusUserId(String busUserId) {
        this.busUserId = busUserId;
    }

    public String getBlack() {
        return black;
    }

    public void setBlack(String black) {
        this.black = black;
    }

    public String getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(String personInfo) {
        this.personInfo = personInfo;
    }

    public int getIsBusOrKeFu() {
        return isBusOrKeFu;
    }

    public void setIsBusOrKeFu(int isBusOrKeFu) {
        this.isBusOrKeFu = isBusOrKeFu;
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

    public String getImageBack() {
        return imageBack;
    }

    public void setImageBack(String imageBack) {
        this.imageBack = imageBack;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(String handlerId) {
        this.handlerId = handlerId;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPro() {
        return pro;
    }

    public void setPro(String pro) {
        this.pro = pro;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public MailingAddressBean getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(MailingAddressBean mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }

    public List<UrlsBean> getUrls() {
        return urls;
    }

    public void setUrls(List<UrlsBean> urls) {
        this.urls = urls;
    }

    public static class MailingAddressBean {
        private String region;
        private String id;
        private String phone;
        private String receiver;
        private String address;
        private String zipCode;
        private String defaultAddress;

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getReceiver() {
            return receiver;
        }

        public void setReceiver(String receiver) {
            this.receiver = receiver;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getZipCode() {
            return zipCode;
        }

        public void setZipCode(String zipCode) {
            this.zipCode = zipCode;
        }

        public String getDefaultAddress() {
            return defaultAddress;
        }

        public void setDefaultAddress(String defaultAddress) {
            this.defaultAddress = defaultAddress;
        }
    }

    public static class UrlsBean {
        private String id;
        private String url;

        public UrlsBean() {
        }

        public UrlsBean(String url) {
            this.url = url;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
