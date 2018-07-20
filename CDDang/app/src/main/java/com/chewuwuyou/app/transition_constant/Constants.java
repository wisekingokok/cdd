package com.chewuwuyou.app.transition_constant;

import com.chewuwuyou.app.utils.Constant;

/**
 * Created by Yogi on 16/9/24.
 */

public class Constants {

    public static final String APP_NAME = "CDD";

    /**
     * 用户相关
     */
    public static class UserTag {
        /**
         * 普通用户
         */
        public static final int ROLE_USER = 1;
        /**
         * A类商家(品牌商家)
         **/
        public static final int ROLE_A = 12;
        /**
         * B类商家(会员商家)
         */
        public static final int ROLE_B = 13;

        /**
         * 省代
         */
        public static final int DAI_LI_TYPE_SHENG = 1;
        /**
         * 市代
         */
        public static final int DAI_LI_TYPE_SHI = 2;
        /**
         * 区代
         */
        public static final int DAI_LI_TYPE_QU = 3;

        /**
         * 是好友
         */
        public static final int IS_FRIEND = 1;

        /**
         * 不是好友
         */
        public static final int NO_FRIEND = 0;
        //black=4表示：甲乙两人互相不是黑名单用户 1：甲的黑名单只有乙         2：乙的黑名单只有甲,3：情况很多（甲乙分别的黑名单用户加起来大于等于2）
        /**
         * 甲乙两人互相不是黑名单用户
         */
        public static final int NO_BLACK = 4;
        /**
         * 甲的黑名单只有乙
         */
        public static final int HE_IN_BLACK = 1;
        /**
         * 乙的黑名单只有甲
         */
        public static final int YOU_IN_HE_BLACK = 2;
        /**
         * 情况很多（甲乙分别的黑名单用户加起来大于等于2）
         */
        public static final int YOU_AND_HE_IN_BLACK = 3;
    }

    /**
     * 融云token未失效
     */
    public static final String RONG_TOKEN_NOT_DISABLED = "1";

    /**
     * 红包类型
     */
    public class RED_PACKET_TYPE {
        /**
         * 个人红包
         */
        public static final String PERSON_RED_PACKET = "0";
        /**
         * 群普通红包
         */
        public static final String GROUP_PT_RED_PACKET = "1";
        /**
         * 群随机红包
         */
        public static final String GROUP_SJ_RED_PACKET = "2";

    }

    /**
     * 发送红包成功
     */
    public static final String SEND_RED_PACKET_SUCCESS = "1002";

    /**
     * 拆红包
     */
    public class REDMOVW_RED{
        /**
         * 抢完红包
         */
        public static final String RED_GRAB = "2";
        /**
         * 过期红包
         */
        public static final String RED_OVERDUE = "3";
    }

    /**
     * 红包过期
     */
    public class RED_ROLE{
        /**
         * 抢完红包
         */
        public static final int LSSUE = 0;
    }
}
