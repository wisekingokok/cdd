package com.chewuwuyou.rong.utils;

/**
 * Created by yuyong on 2016/9/8.
 */
public class Constant {

    public static class MsgType {
        /**
         * 表情
         */
        public static final int FACE = 1;
        /**
         * 大表情
         */
        public static final int BIG_EMOTION = 2;
        /**
         * 位置信息
         */
        public static final int LOCATION = 2;
    }

    /**
     * 查看百度地图调起位置信息
     */
    public static final int REQUEST_CODE_LOCATION = 1001;

    /**
     * 百度地图位置信息
     */
    public static final String BAIDU_LOCATION = "location_ser";

    public static class USER_ID_TYPE {
        /**
         * 发送系统消息的ID
         */
        public static final String SYSTEM_MSG = "1";
        /**
         * 发送订单提醒的ID
         */
        public static final String ORDER_MSG = "2";
        /**
         * 发送添加好友信息ID
         */
        public static final String ADD_FRIEND = "3";
        /**
         * 发送业务大厅的消息ID
         */
        public static final String TASK_HALL = "4";

    }

    /**
     * 测试环境客服ID
     */
//    public static final String TEST_SERVER_ID = "KEFU147383690276954";
    /**
     * 正式环境客服ID
     */
    public static final String SERVER_ID="KEFU147512988371929";
    /**
     * 位置信息
     */
    public static final String LOC_MSG="location_msg";
}
