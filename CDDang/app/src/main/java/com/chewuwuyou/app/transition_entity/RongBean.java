package com.chewuwuyou.app.transition_entity;

import java.io.Serializable;

/**
 * Created by yuyong on 16/10/13.
 */

public class RongBean implements Serializable{


    /**
     * code : 0
     * message :
     * data : {"userId":23513,"name":"余勇","portraitUri":"http://101.204.230.251/spng//upload/2016092614201823513132.jpg","token":"29i1l/xldT8w0s7OmSRRGn/vzfTttgyxrAgwW+Z/TDaEAAp/f7hItHpn00W2Gb76ghgrL+K1Ng8C7qpZ5pMfwQ==","isBusiness":0,"remarks":"","phone":"","nickname":"","isFriend":0}
     */

    private int code;
    private String message;
    /**
     * userId : 23513
     * name : 余勇
     * portraitUri : http://101.204.230.251/spng//upload/2016092614201823513132.jpg
     * token : 29i1l/xldT8w0s7OmSRRGn/vzfTttgyxrAgwW+Z/TDaEAAp/f7hItHpn00W2Gb76ghgrL+K1Ng8C7qpZ5pMfwQ==
     * isBusiness : 0
     * remarks :
     * phone :
     * nickname :
     * isFriend : 0
     */

    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int userId;
        private String name;
        private String portraitUri;
        private String token;
        private int isBusiness;
        private String remarks;
        private String phone;
        private String nickname;
        private int isFriend;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPortraitUri() {
            return portraitUri;
        }

        public void setPortraitUri(String portraitUri) {
            this.portraitUri = portraitUri;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public int getIsBusiness() {
            return isBusiness;
        }

        public void setIsBusiness(int isBusiness) {
            this.isBusiness = isBusiness;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getIsFriend() {
            return isFriend;
        }

        public void setIsFriend(int isFriend) {
            this.isFriend = isFriend;
        }
    }
}
