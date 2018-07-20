
package com.chewuwuyou.app.utils;

/**
 * 正则表达式工具类
 * 
 * @author Administrator
 */
public class RegularUtil {

    // 判断是否为邮箱格式
    //public static final String verifyEmail = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";有问题，验证不过
    public static final String verifyEmail = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
    // 判断是否为电话号码
    public static final String verifyTelephone = "^1[0-9]{10}$";
    // 判断是否为帐号
    public static final String verifyUsername = "^[a-zA-Z][a-zA-Z0-9_]{3,15}$";
    // 判断密码格式是否正确
    public static final String verifyPassword="^[a-zA-Z0-9_]{6,16}$";
    // 判断发动机号 由字母和数字组成的6-19位字符
    public static final String verifyEngineNumber = "^[a-zA-Z0-9]{6,19}$";
    // 判断车架号有且仅有17位，字母开头，由字母和数字组成
    public static final String verifyVehicleFrameNumber = "^[a-zA-Z][a-zA-Z0-9]{16}$";
    // 判断车牌号码
//    public static final String verifyPlateNumber = "^[\u4e00-\u9fa5][a-zA-Z0-9]{6}$";
    public static final String verifyPlateNumber = "^[\u4e00-\u9fa5][a-zA-Z][a-zA-Z0-9]{5}$";
    // 判断Contact的正则表达式
    public static final String verifyContact = "^[a-zA-Z0-9\u4e00-\u9fa5]{1,16}$";
    // 判断档案编号
    public static final String verifyFileNumber = "^[0-9]{12}$";
    // 判断驾证编号
    public static final String verifyLicenceNumber = "^[0-9]{17}[0-9A-Za-z]$";
    // 判断车辆价格的正则
    public static final String verifyVehiclePrice = "^[0-9]{4,8}$";
    // 判断车务无忧号
    public static final String verifyCwwyno = "^[a-z]+[a-zA-Z0-9_]*$";
    // 判断备注不能全部空格
    public static final String verifyBlank = ".*[^ ].*";
    // 判断金额
    public static final String verifyRecordMoney = "^[0-9]{5}$";
    // 经纪人去掉价格正则
    public static final String verifyService = ",[0-9]+";
}
