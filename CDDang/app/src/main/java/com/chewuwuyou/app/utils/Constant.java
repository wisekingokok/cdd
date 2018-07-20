package com.chewuwuyou.app.utils;

import android.provider.ContactsContract;

public class Constant {

    // 在另外一台手机登陆
    public static final int LOGIN_IN_OTHER_PHONE = 110;
    // 更新UI
    public static final int UPDATE_UI = 100;
    // 请求成功数据改变result为1的时候
    public static final int NET_DATA_SUCCESS = 102;
    // result返回为0的时候，
    public static final int NET_DATA_FAIL = 103;
    // 网络数据异常不是规范的数据
    public static final int NET_DATA_EXCEPTION = 104;
    // 网络请求失败
    public static final int NET_REQUEST_FAIL = 105;
    // 请求数据为空时
    public static final int NET_DATA_NULL = 106;
    // 选择车型
    public static final int CAR_TYPE = 200;
    public static final int NET_JSON_ERROR = 1001;
    // 支付宝
    public static final int RQF_PAY = 1;

    // 传输数据
    public static final int SEND_ADAPTER = 911;
    public static final int SEND_Handler = 1;// Handler传递数据状态
    //手势解锁
    public static final String SHOW_ORBIT = "show_orbit";
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String NIKE_NAME = "nike_name";
    public static final String HEAD_URL = "head_url";

    public static int CCCOUNT_WITHDRAWLS = 0;


    public static String GROUP_YE_SIZE = "10";//群显示的条数

    // 商家中心拍照保存图片
    public static final String SAVED_IMAGE_DIR_PATH = "cwwy/photo";
    public static final int REQUEST_CODE_CAPTURE_CAMEIA = 201;
    // 油价和停车场跳转到服务列表
    public static final int OIL_SKIPTO_SERVICE = 301;

    public static final String CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    public static final int IO_BUFFER_SIZE = 2 * 1024;

    // 实体
    // 传递任务实体的标识
    public static final String TASK_SER = "com.cwwy.app1.bean.task.ser";
    public static final String TRAFFIC_SER = "com.cwwy.app1.bean.traffic.ser";
    // 传入驾证实体的标识
    public static final String LICENSE_SER = "com.cwwy.app1.bean.license.ser";
    // 传入车实体的标识
    public static final String VEHICLE_SER = "com.cwwy.app1.bean.vehicle.ser";
    //
    public static final String BTC_SER = "com.cwwy.app1.bean.btcorder.ser";
    public static final String BTB_SER = "com.cwwy.app1.bean.btborder.ser";
    public static final String ORDER_SER_KEY = "com.chewuwuyou.app.bean.btborder_ser";
    // 店铺实体
    public static final String SHOP_SER_KEY = "com.chewuwuyou.app.bean.shop.ser";
    // 圈翻页的时候每页多少项
    public static final int QUAN_PAGE_SIZE = 10;
    // 帖子的时候每页多少项
    public static final int TIE_PAGE_SIZE = 10;
    // 活动的时候每页多少项
    public static final int YUE_PAGE_SIZE = 10;
    // 圈翻页的时候每页多少项
    public static final int QUAN_DETAIL_PAGE_SIZE = 20;
    // 帖子的时候每页多少项
    public static final int TIE_DETAIL_PAGE_SIZE = 20;
    // 活动的时候每页多少项
    public static final int YUE_DETAIL_PAGE_SIZE = 20;

    // 速度与激情板块
    public static final int HOT_BANKUAI_MARK = 1;

    public static int SETPASSWORD = 0;
    /**
     * 订单状态模块
     *
     * @author Administrator
     */
    public static final int ORDER_START_PAYMENTS = 3;// 待付款
    public static final int ORDER_START_PENDING_CONFIRMATION = 1;// 待商家确认
    public static final int ORDER_START_COMPLETE = 10;// 订单完成
    public static final int ORDER_START_SERVICE_COMPLETE = 6;// 商家服务完成
    public static final int ORDER_START_SERVICE = 5;// 商家服务中
    public static final int ORDER_START_CANCEL_ORDER = 2;// 订单已取消
    public static final int ORDER_START_APPLY_REFUND = 11;// 申请退款中
    public static final int ORDER_START_REFUND_PROCESSING = 12;// 退款处理中
    public static final int ORDER_START_REFUND_COMPLETE = 13;// 退款完成
    public static final int ORDER_START_ALREADY_PAID = 4;// 已付款
    public static final int ORDER_START_ORDER_COMPLETE = 7;// 订单已完成
    public static final int ORDER_START_ORDER_BALANCE = 29;// 结算入余额
    public static final int ORDER_START_REFUND_COMLETE2 = 32;// 退款入余额
    public static final int ORDER_OPERATE_COMLETE2 = 33;// 运营进入订单流程
    public static int IS_PAGE;// 判断是否从在线支付进去的 1
    public static int IS_SECURITY;// 判断是否从安全设置进去的 2


    //

    public static class License {
        public static final int RESULT_FROM_EDIT = 1;
        public static final int RESULT_FROM_CHOOSE_CITY = 2;
    }

    public static class Vehicle {
        public static final int RESULT_FROM_EDIT = 1;
        // 选择品牌和车型
        public static final int CHOICE_MODEL = 20;
    }

    // 商家id
    public static final String BUSINESS_ID = "0";

    public static class Personal_Center {
        public static final int RESULT_FROM_EMAIL_BIND = 1;
        public static final int RESULT_FROM_EMAIL_UPDATE = 2;
    }

    public static class Business {
        public static final int RESULT_FROM_CHOOSE_CITY = 1;
        public static final int RESULT_FROM_BUSINESS_DETAIL = 2;
    }

    public static class Yue {
        public static final int RESULT_FROM_CHOOSE_CITY = 2;
    }

    // 即时聊天传递用户实体

    public static final String CHAT_USER_SER = "com.chewuwuyou.chatuser_ser";

    public enum YueYueType {
        /**
         * 旅游
         */
        TRAVEL,

        /**
         * 饭饭
         */
        DINNER,

        /**
         * 观影
         */
        FILM,

        /**
         * 购物
         */
        SHOPPING,

        /**
         * 泡吧
         */
        BAR,

        /**
         * 麻将
         */
        MAHJONG,

        /**
         * 运动
         */
        SPORTS,

        /**
         * 遛宠
         */
        PET

    }

    public enum HotTieType {
        /**
         * 车展 0
         */
        CARSHOW,

        /**
         * 自驾 1
         */
        SELFTRAVELL,

        /**
         * 改装 2
         */
        REEQUIP,

        /**
         * 风景 3
         */
        SCENERY,

        /**
         * 城市 4
         */
        CITY,

        /**
         * 摩托车 5
         */
        MOTORCYCLE
    }

    public static class ShopType {
        /**
         * 4s店
         */
        public static final int FOURSSTORES = 1;
        /**
         * 汽车美容
         */
        public static final int CARBEAUTY = 2;
        /**
         * 维修保养
         */
        public static final int MAINTENANCE = 3;
        /**
         * 汽车配件
         */
        public static final int AUTOPARTS = 4;
        /**
         * 驾校查询
         */
        public static final int DRIVING_SCHOOL = 5;
    }

    public static class redicTo {
        /**
         * 查看地图标注
         */
        public static final int CHECK_LABLE = 1;

        /**
         * 查看路线
         */
        public static final int CHECK_WAY = 2;
    }

    public static class AssistantType {

        /**
         * 汽车配件
         */
        public static final int AUTOPARTS = 1;

        /**
         * 4s店
         */
        public static final int FOURSSTORES = 2;

        /**
         * 加油站
         */
        public static final int GAS_STATION = 3;
        /**
         * 停车场
         */
        public static final int PARK_BUD = 4;

        /**
         * 维修保养
         */
        public static final int MAINTENANCE = 5;

        /**
         * 汽车美容
         */
        public static final int CARBEAUTY = 6;

        /**
         * 驾校
         */
        public static final int DRIVING_SCHOOL = 7;

    }

    public static class QUAN_MSG_BUNDLE {

        public static final String KEY_QUAN = "key_quan_wen";
        public static final String KEY_QUAN_PING = "key_quan_ping";

    }

    public static class CAR_FRIEND_TYPE {
        /**
         * 附近的车友
         */
        public static final int NEAR_BY_CAR_FIREND = 1;
        /**
         * 同品牌车友
         */
        public static final int SAME_BRAND_CAR_FRIEND = 2;
    }

    public static final String PERSONINFO_SER = "com.chewuwuyou.app.bean.personinfo";
    /**
     * 个性强最多显示图片的张数
     */
    public static final int PIC_MAX_NUMBER = 8;
    /**
     * 照片墙每张图片的大小
     */
    public static final int PIC_PUTX = 480;
    public static final int PIC_PUTY = 480;

    public static class CHAT_MESSAGE_TYPE {
        /**
         * 普通文本
         */
        public static final int TXT = 0;
        /**
         * 图片
         */
        public static final int IMAGE = 1;
        /**
         * 录像
         */
        public static final int VIDEO = 2;
        /**
         * 语音
         */
        public static final int VOICE = 3;
        /**
         * gif图片
         */
        public static final int GIF_IMG = 4;

        /**
         * 位置
         */
        public static final int LOCATION = 5;
        /**
         * 带有表情
         */
        public static final int YWZ_TXT = 6;
        /**
         * 文件
         */
        public static final int FILE = 7;
        /**
         * 通话
         */
        public static final int VOICE_CALL = 8;
        /**
         * 视频通话
         */
        public static final int VIDEO_CALL = 9;

    }

    /**
     * 聊天表情的页数
     */
    public static final int NUM_PAGE = 5;
    /**
     * 每页显示的表情数
     */
    public static final int EVERY_PAGE_NUM = 20;

    /**
     * 服务类型
     */
    public static class SERVICE_TYPE {
        /**
         * 车辆服务
         */
        public static final int CAR_SERVICE = 2;
        /**
         * 驾证服务
         */
        public static final int LICENCE_SERVICE = 3;
        /**
         * 违章服务
         */
        public static final int ILLEGAL_SERVICE = 1;
    }

    /**
     * 订单角色
     */
    public static class ORDERROLE {
        /**
         * 所有角色订单
         */
        public static final String ALL_ORDER = "0";
        /**
         * 用户订单
         */
        public static final String USER_ORDER = "1";
        /**
         * 商家订单
         */
        public static final String BUSINESS_ORDER = "2";
        /**
         * 外派订单
         */
        public static final String ASSIGEMENT_ORDER = "3";
    }

    /**
     * 订单类型
     */
    public static class ORDERTYPE {
        /**
         * 所有类型订单
         */
        public static final String ALL_ORDER = "0";
        /**
         * 违章服务
         */
        public static final String ILLEGAL_SERVICE = "1";
        /**
         * 车辆服务
         */
        public static final String VEHILCE_SERVICE = "2";
        /**
         * 驾证服务
         */
        public static final String LICENCE_SERVICE = "3";
    }

    /**
     * 订单状态
     */
    public static class ORDERSTATUS {
        /**
         * 所有状态订单
         */
        public static final String ALL_ORDER = "";
        /**
         * 未完成订单
         */
        public static final String UNFINISH_ORDER = "0";
        /**
         * 已完成订单
         */
        public static final String FINISH_ORDER = "1";
    }

    /**
     * 申请退款
     */
    public static class TUIKUAN {
        /**
         * 申请退款
         */
        public static final String APPLY_TUIKUAN = "1";
        /**
         * 同意退款
         */
        public static final String AGREE_TUIKUAN = "2";
        /**
         * 取消退款
         */
        public static final String CANCEL_TUIKUAN = "4";

        /**
         * 退款成功
         */
    }

    /**
     * 选择违章服务
     */
    public static final int CHOOSE_ILLEGAL_SERVICE = 25;

    public static class ORDERSTATUSMANAGE {
        /**
         * 待处理订单
         */
        public static final int TODAY_ORDER = 1;
        /**
         * 服务中订单
         */
        public static final int NO_COMFIRM_PRICE = 2;

        /**
         * 已完成订单
         */
        public static final int WAIT_PAY = 3;
        /**
         * 已取消订单
         */
        public static final int IN_SERVICE = 4;
        /**
         * 退款的订单
         */
        public static final int CUSTOMERS_CHEDAN = 5;
        /**
         * 纠纷中订单
         */
        public static final int CUSTOMERS_EVALUATE = 6;


    }

    public static class ORDER_TYPE {
        /**
         * 收到的订单(包括：商家订单、用户订单)
         */
        public static final int GET_ORDER = 1;
        /**
         * 发出的订单
         */
        public static final int OVERSEAS_ORDER = 2;

    }

    /**
     * @author yuyong
     *         <p/>
     *         1，充值；2，提现成功；3，提现失败； 4，订单入账；5，余额支付；6，支付方式支付；
     *         7，余额支付退回余额；8，支付方式支付退回支付方式； 9，支付方式支付退回到余额
     */

    public static class BALANCE_TYPE {

        /**
         * 充值
         */
        public static final int RECHARGE = 1;

        /**
         * 提现成功
         */
        public static final int WITH_DRAW_SUCCESS = 2;
        /**
         * 提现失败
         */
        public static final int WITH_DRAW_FAILURE = 3;
        /**
         * 订单入账
         */
        public static final int ORDER_ENTER = 4;
        /**
         * 余额支付
         */
        public static final int BALANCE_PAY = 5;
        /**
         * 支付方式支付
         */
        public static final int PAYMENT_PAY = 6;
        /**
         * 余额支付退回余额
         */
        public static final int BALANCE_PAY_TUI = 7;
        /**
         * 支付方式支付退回支付方式
         */
        public static final int PAYMENT_PAY_TUI = 8;
        /**
         * 支付方式支付退回到余额
         */
        public static final int PAYMENT_PAY_TUI_BALANCE = 9;
        /**
         * 提现申请
         */
        public static final int WITH_DRAW_APPLY = 10;
        public static final int EXCEPTION_REFUND_RECHARGE = 11;
        public static final int REDPACHKETOUT = 12;
        public static final int REDPACKETIN = 13;
        public static final int REDPACKETTUIKUAI = 14;
    }

    /**
     * 账单详情实体
     */
    public static final String BALANCE_DETAIL_BEAN = "balance_detail_bean";

    /**
     * 点击提现账户
     */

    public static final int CLICK_TIXIAN_ACCOUNT = 1;
    /**
     * 点击安全设置
     */
    public static final int CLICK_SAFE_SETTING = 2;
    /**
     * 点击余额提现
     */
    public static final int CLICK_BALANCE_TIXIAN = 3;
    /**
     * 点击钱包支付
     */
    public static final int CLICK_BALANCE_PAY = 4;
    /**
     * 点击设置密码
     */
    public static final int CLICK_SETTING_PAYPASS = 1;

    /**
     * 点击修改密码
     */
    public static final int CLICK_UPDATE_PAYPASS = 5;
    /**
     * 点击忘记密码
     */
    public static final int CLICK_FORGET_PAYPASS = 6;

    /**
     * 记录支付密码是否设置 0为未设置 1为已设置 可能会存在内存回收问题 后面改进
     */

    public static int IS_SET_PAYPASS = 0;

    /**
     * 由业务大厅进入担保支付
     */
    public static final int DATING_INTO_PAY = 1;

    /**
     * 记录是否设置过提现账户， 0表示没有提现账户，1表示有提现账户
     */
    public static int IS_TIXIAN_ZHANGHU = 0;

    /**
     * 记录由取消订单进入
     */
    public static String mType = "0";

    /**
     * 举报类型 圈子
     */
    public static String juBaoTypeQuan = "4";

    /**
     * 运营位的类型
     */
    public static class BANNER_TYPE {
        /**
         * 首页banner
         */
        public static final String HOME_PAGE_BANNER = "1";
        /**
         * 活动banner
         */
        public static final String ACTIVITY_BANNER = "2";
        /**
         * 车圈banner
         */
        public static final String CARQUAN_BANNER = "3";
    }

    public static class JPUSH_STATUS {
        /**
         * 开启
         */
        public static final String JPUSH_OPEN = "1";
        /**
         * 关闭
         */
        public static final String JPUSH_CLOSE = "2";
    }

    /**
     * 及时通讯判断用户角色 0：普通用户；1：商家；2：客服
     *
     * @author yuyong
     */
    public static class ROLE_TYPE {
        /**
         * 用户
         */
        public static final int USER = 0;
        /**
         * 商家
         */
        public static final int BUSINESS = 1;
        /**
         * 客服
         */
        public static final int SERVER = 2;

    }

    /**
     * 审核通过
     *
     * @author yuyong
     */
    public static class BUSINESS_PASS_STATUS {
        /**
         * 不存在
         */
        public static final int REGIST_NOT_PASS = 0;
        /**
         * 存在
         */
        public static final int REGIST_PASS = 1;

    }

    /**
     * 商家认证的状态 0:发布，1:审核中，2:通过，3:拒绝
     *
     * @author yuyong
     */
    public static class BUSINESS_REGIST_STATUS {

        /**
         * 未注册过 可以注册
         */
        public static final int REGIST_START = 0;
        /**
         * 已注册审核中
         */
        public static final int REGIST_DOING = 1;
        /**
         * 审核已通过
         */
        public static final int REGIST_PASS = 2;
        /**
         * 拒绝申请
         */
        public static final int REGIST_NOT_PASS = 3;

        /**
         * 原来是商家现在是普通用户
         */
        public static final int REGIST_FOUR_PASS = 4;


    }

    /**
     * 聊天时的用户角色
     *
     * @author yuyong
     */
    public static class CHAT_USER_ROLE {
        /**
         * 普通用户
         */
        public static final int USER = 0;
        /**
         * 商家
         */
        public static final int BUSINESS = 1;
        /**
         * 客服
         */
        public static final int SERVER = 2;
    }

    /**
     * 支付意图类型
     *
     * @author yuyong
     */
    public static class PAY_PURPOSE_TYPE {
        /**
         * 订单付款(支付宝付款、微信付款)
         */
        public static final String ORDER_PAY = "1";
        /**
         * 充值付款
         */
        public static final String RECHARGE = "2";

    }

    /**
     * 服务项目实体
     */
    public static final String SERVICE_SHITI = "com.chewuwuyou.app.bean.service.pro";
    /**
     * 区分支付入口 1、订单支付 2、充值
     */
    public static int PAY_INTO = 0;
    public static String PAY_MONEY = "0.0";// 充值金额

    /**
     * 记录 从哪个UI进入城市选择
     */
    public static int CITY_CHOOSE;// 记录用户ID
    public static boolean CITY_SHOW_ALL = false;//选择地图显示全部

    /**
     * 消息转发
     */
    public static int SEND_MSG_BY_HISTORY = 1;

    /**
     * 订单异常
     */
    public static class Order_Abnormal {

        public static final int ORDER_ONE = 0;//不存在异常

        public static final int ORDER_TWO = 1;//异常

        public static final int ORDER_THREE = 2;//
    }

    //黑名单listname
    public static String Black_List = "black_list";


    public static String GROUP_LIST = "0";//群列表
    public static String GROUP_SET_UP = "1";//群设置添加成员
    public static String WHOLE_GROUP = "2";//全部群
    public static String FORWARD_GROUP = "6";//转发
    public static String GROUP_ZHUANFA = "7";//转发群


    /**
     * //群列表搜索
     */
    public static String GROUP_LIST_SEARCH = "1";
    /**
     * /群设置搜索
     */
    public static String GROUP_SETUP_SEARCH = "2";
    /**
     * 移交群管理权
     */
    public static String GROUP_MANGMENT_SEARCH = "3";
    /**
     * 创建群搜索
     */
    public static String GROUP_FRIENDS = "4";
    /**
     * 删除群成员
     */
    public static String DELETE_GROUP = "5";

    /**
     * 访问数据成功
     */
    public static final int GET_DATA_SUCCESS = 0;
    /**
     * 七牛上传图片拼接
     */

    public static final int UPLOAD_PORTRAIT_GROUP = 2;

}
