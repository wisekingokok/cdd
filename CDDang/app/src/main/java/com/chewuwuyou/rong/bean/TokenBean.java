package com.chewuwuyou.rong.bean;

/**
 * Created by Administrator on 2016/9/6 0006.
 */
public class TokenBean {


    /**
     * code : 0
     * message :
     * data : {"userId":49030,"name":"大象","portraitUri":"http://101.204.230.251/spng//upload/2016080117223121681967.jpg","token":"BmauusPsTTebyCVf8W9Zzn/vzfTttgyxrAgwW+Z/TDYesH1Z8MUyF+Leg2YsajEmIkcLP4+QxzKg3B6gKMPDwg=="}
     */

    private int code;
    private String message;
    /**
     * userId : 49030
     * name : 大象
     * portraitUri : http://101.204.230.251/spng//upload/2016080117223121681967.jpg
     * token : BmauusPsTTebyCVf8W9Zzn/vzfTttgyxrAgwW+Z/TDYesH1Z8MUyF+Leg2YsajEmIkcLP4+QxzKg3B6gKMPDwg==
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
    }
}
