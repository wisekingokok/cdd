package com.chewuwuyou.rong.bean;

/**
 * Created by xxy on 2016/9/17 0017.
 */
public class NewBaseNetworkBean<T> {

    /**
     * code : 0
     * message :
     * data : [{"birthday":"","sex":"0","location":"北京市","busType":"0","registertime":"2016-07-14 13:48:09","noteName":"","starsit":"","age":"0","userId":"21681","headUrl":"/upload/2016091714230921681866.jpg","busUserId":"","black":"4","personInfo":"","isBusOrKeFu":"0","urls":[{"id":"39710","url":"/upload/2016091714230921681866.jpg"},{"id":"23750","url":"/upload/2016080117312121681775.jpg"},{"id":"23743","url":"/upload/2016080117281221681778.jpg"},{"id":"23730","url":"/upload/2016080117223121681967.jpg"},{"id":"39711","url":"/upload/2016091714270921681012.jpg"}],"carBrand":"奥迪/奥迪A4L","hobby":"","imageBack":"/upload/2016082609545021681831.jpg","sign":"空间里","handlerId":"0","nick":"大象","pro":"","telephone":"15883635438","mailingAddress":{"region":"","id":"","phone":"","receiver":"","address":"","zipCode":"","defaultAddress":""},"friend":"0"}]
     */

    private int code;
    private String message;
    /**
     * birthday :
     * sex : 0
     * location : 北京市
     * busType : 0
     * registertime : 2016-07-14 13:48:09
     * noteName :
     * starsit :
     * age : 0
     * userId : 21681
     * headUrl : /upload/2016091714230921681866.jpg
     * busUserId :
     * black : 4
     * personInfo :
     * isBusOrKeFu : 0
     * urls : [{"id":"39710","url":"/upload/2016091714230921681866.jpg"},{"id":"23750","url":"/upload/2016080117312121681775.jpg"},{"id":"23743","url":"/upload/2016080117281221681778.jpg"},{"id":"23730","url":"/upload/2016080117223121681967.jpg"},{"id":"39711","url":"/upload/2016091714270921681012.jpg"}]
     * carBrand : 奥迪/奥迪A4L
     * hobby :
     * imageBack : /upload/2016082609545021681831.jpg
     * sign : 空间里
     * handlerId : 0
     * nick : 大象
     * pro :
     * telephone : 15883635438
     * mailingAddress : {"region":"","id":"","phone":"","receiver":"","address":"","zipCode":"","defaultAddress":""}
     * friend : 0
     */

    private T data;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
