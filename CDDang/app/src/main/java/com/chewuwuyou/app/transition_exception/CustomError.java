package com.chewuwuyou.app.transition_exception;

/**
 * 异常code
 * Created by xxy on 2016/8/20 0020.
 */
public class CustomError {

    public static final int CODE_CONNECTION_ERROR = 1000;

    public static final int CODE_UNKOWN_ERROR = 1099;
    public static final int CODE_UNKOWN_CUSTOM_ERROR = 1101;
    public static final int CODE_SERVER_RESPONSE = 1100;
    public static final int CODE_SERVER_JSON_ERROR = 1102;
    public static final int NULLPOINTER_EXCEPTION = 2000;//空指针异常
    /**
     * 登录超时
     */
    public static final int LOGIN_PAST = 0;
    /**
     * 其他手机登录
     */
    public static final int LOGINE_IN_OTHER_PHONE = 15;
    /**
     * 角色转变
     */
    public static final int NOT_BUSINESS = 110;
    /**
     * 未设置车辆
     */
    public static final int NO_SET_CAR_ERROR = 411;

    /**
     * 没有提醒
     */
    public static final int NO_REDMINE_ERROR = 910;
}
