package com.chewuwuyou.app.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.chewuwuyou.app.BuildConfig;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.apache.http.Header;

import java.io.File;

@SuppressWarnings("deprecation")
public class NetworkUtil {


    private static FinalHttp fhMulti = new FinalHttp();
    private static FinalHttp fh = new FinalHttp();
    private static PersistentCookieStore cookieStore;

    /**
     * 正式上线环境
     */
    public static String BASE_URL = BuildConfig.BASE_URL;
    public static String XMPP_HOST_URL = BuildConfig.XMPP_HOST_URL;
    public static String IMAGE_BASE_URL = BuildConfig.IMAGE_BASE_URL;
    /**
     * 预发布环境
     */
//    public static String BASE_URL = "http://120.55.188.215/mhwcw/";
//    public static String XMPP_HOST_URL = "http://apiyfb.cddang.com";
//    public static String IMAGE_BASE_URL = "http://120.55.138.140/spng/";
    /**
     * 开发环境
     */
//    public static String BASE_URL = "http://101.204.230.251/mhwcw/";
//    public static String XMPP_HOST_URL = "http://192.168.8.235:6080/";// 114.55.102.191
//    public static String IMAGE_BASE_URL = "http://120.55.138.140/spng/";

    /**
     * 测试环境
     */
//    public static String BASE_URL = "http://101.204.230.251:8081/mhwcw/";
//    public static String XMPP_HOST_URL = "http://192.168.8.236:6080/";
//    public static String IMAGE_BASE_URL = "http://120.55.138.140/spng/";
//
//

    /**
     * 潘厚成服务测试
     */
//    public static String BASE_URL = "http://192.168.8.102:7070/mhwcw/";
//    public static String XMPP_HOST_URL = "http://192.168.8.236:6080/";
//    public static String IMAGE_BASE_URL = "http://120.55.138.140/spng/";

    /**
     * 李雪健 接口
     */
//    public static String BASE_URL = "http://192.168.8.47:8080/mhwcw/";
//    public static String XMPP_HOST_URL = "http://192.168.8.235:6080/";
//    public static String IMAGE_BASE_URL = "http://120.55.138.140/spng/";

    /**
     * 黄志鹏
     */
//    public static String BASE_URL = "http://192.168.8.59:8080/mhwcw/";
//    public static String XMPP_HOST_URL = "http://192.168.8.235:6080/";
//    public static String IMAGE_BASE_URL = "http://120.55.138.140/spng/";


    /**
     * 李超接口
     */
//    public static String BASE_URL = "http://192.168.8.44:8080/mhwcw/";
//    public static String XMPP_HOST_URL = "http://apiyfb.cddang.com";
//    public static String IMAGE_BASE_URL = "http://120.55.138.140/spng/";
    /**
     * 曹正华
     */
//    public static String BASE_URL = "http://192.168.8.48:8080/mhwcw/";
//    public static String XMPP_HOST_URL = "http://192.168.8.235:6080/";
//    public static String IMAGE_BASE_URL = "http://120.55.138.140/spng/";
    /**
     * 吴杰
     */
//    public static String BASE_URL = "http://192.168.8.101:8080/mhwcw/";
//    public static String XMPP_HOST_URL = "http://192.168.8.235:6080/";
//    public static String IMAGE_BASE_URL = "http://120.55.138.140/spng/";

    // 客服电话
    public static final String phonenum = "118-114";
    public static final String LOGIN_URL = BASE_URL + "api/user/loginApp";
    // 车辆服务
    public static final String VEHICLE_SERVICE_URL = BASE_URL
            + "api/carservice/newTask";
    // 添加车辆
    public static final String ADD_CAR_URL = BASE_URL
            + "api/carservice/addVehicle";
    // 驾证服务
    public static final String LICENCE_SERVICE_URL = BASE_URL
            + "api/licenceservice/newTask";
    // 请求今日油价的URL
    // public static final String
    // OIL_PRICES_TODAY_URL="http://192.168.1.2:8080/mhwcw/json/oilCity.json";
    public static final String OIL_PRICES_TODAY_URL = "http://apis.juhe.cn/cnoil/oil_city?key=db5f4376988c583c0083d8e55c5bd841";

    // 订单管理
    public static final String ORDER_MANAGER_URL = BASE_URL
            + "api/order/getOrder";
    // 修改订单状态
    public static final String ADD_ORDER = BASE_URL + "api/order/addOrder";

    // 修改订单状态并扣去抵用卷
    public static final String UPDATE_ORDER_STATUS = BASE_URL
            + "api/voucherservice/payVoucherForTask";
    // 查询收藏
    public static final String SELECT_FAVORITE_URL = BASE_URL
            + "api/favoritesService/getUserFavoritesList";
    // 判断是否已经存在收藏了该商家
    public static final String IS_EXIST_FAVORITE_URL = BASE_URL
            + "api/favoritesService/isExistFavorite";
    // 增加收藏信息
    public static final String ADD_FACORITES_URL = BASE_URL
            + "api/favoritesService/addFavorites";
    // 删除我的收藏商家fff
    public static final String DELETE_FAVORITES_URL = BASE_URL
            + "api/favoritesService/deleteFavorites";

    // 收藏 取消收藏
    public static final String DELETE_COLLECTION_URL = BASE_URL
            + "api/favoritesService/toggleFavorites";
    //收支统计
    public static final String IN_OUT_STATISTIC = BASE_URL + "/api/statistic/inOutStatistic";
    //收发订单统计
    public static final String ORDER_STATISTIC = BASE_URL + "/api/statistic/orderStatistic";
    //收支统计
    public static final String IN_OUT_RECORD_DATA = BASE_URL + "/api/statistic/inOutRecordData";
    // 任务管理URL
    public static final String TASK_MANAGER_URL = BASE_URL + "api/task/getTask";
    // 关闭任务
    public static final String CLOSE_TASK = BASE_URL + "api/task/closeTask";

    // 确认完成
    public static final String CONFIRM_TASK_URL = BASE_URL
            + "api/task/confirmTask2";
    // 确认完成不走结算入余额
    public static final String CONFIRM_TASK_URL1 = BASE_URL
            + "api/task/confirmTask";
    // 查询个人信息
    public static final String SELECT_PERSONAL_DATA = BASE_URL
            + "/api/individualCenter/getInfo";
    // 修改个人信息
    public static final String UPDATE_INFO_URL = BASE_URL
            + "api/individualCenter/updateInfo";
    // 修改车务无忧号
    public static final String UPDATE_USERNAME_URL = BASE_URL
            + "api/individualCenter/updateUserName";
    // 上传头像
    public static final String UPLOAD_PICTURE = BASE_URL
            + "api/individualCenter/uploadIconB";

    // 车辆管理
    public static final String VEHICLE_MANAGE = BASE_URL
            + "api/carservice/getVehicleList";
    // 修改车辆
    public static final String UPDATE_VEHICLE_URL = BASE_URL
            + "api/carservice/updateVehicle";
    public static final String IS_IN_GROUP = XMPP_HOST_URL + "/api/im/group/v1/getGroupMemberByGroupIdAndAccid";
    // 删除车辆
    public static final String DELETE_VEHICLE_URL = BASE_URL
            + "api/carservice/deleteVehicle";

    // 驾证信息查询
    public static final String DRIVERS_LICENSE_INFO_SELECT = BASE_URL
            + "api/licenceservice/getLicenceList";
    // 添加驾证信息
    public static final String ADD_LICENSE_URL = BASE_URL
            + "api/licenceservice/addLicence";
    // 修改驾证信息
    public static final String UPDATE_LICENCE_URL = BASE_URL
            + "api/licenceservice/addLicence";
    // 删除驾证信息
    public static final String DELETE_LICENCE_URL = BASE_URL
            + "api/licenceservice/deleteLicence";

    // btc未完成订单
    public static final String BTC_UNFINISH_ORDER = BASE_URL
            + "api/businesscenter/getUnfinishedOder";
    // btb订单查询
    public static final String BTB_ORDER_URL = BASE_URL
            + "api/businesscenter/getbusiToMeOrder";
    // btb开始服务
    public static final String BTB_ORDER_START_SERVICE = BASE_URL
            + "api/businesscenter/startBizService";
    // btb完成服务
    public static final String BTB_ORDER_FINISH_SERVICE = BASE_URL
            + "api/businesscenter/finishBizService";

    // 查询我给商家的订单
    public static final String METB_ORDER_URL = BASE_URL
            + "api/businesscenter/getMeToBusiOrder";
    // 违章查询
    public static final String VIOLATION_QUERY_URL = BASE_URL
            + "api/wzservice/querywz";
    // 违章服务
    public static final String VIOLATION_SERVICE_URL = BASE_URL
            + "api/wzservice/newTask";
    // 查询商家
    public static final String SELECT_BUSSINESS = BASE_URL
            + "api/businesscenter/getBussi";
    // 商家对商家生成订单
    public static final String BTB_ORDER_PRODUCE = BASE_URL
            + "api/businesscenter/addbusiOrder";
    // 商家对商家的订单支付完成后更新状态
    public static final String BTB_ORDER_UPDATE_STATUS = BASE_URL
            + "api/businesscenter/updatebusiOrder";
    // 修改商家图片
    public static final String UPDATE_BUSINESS_IMAGE = BASE_URL
            + "api/businesscenter/uploadimg";
    // 我给其他商家的订单：确认完成
    public static final String METB_ORDER_CONFIRM_FINISH = BASE_URL
            + "api/businesscenter/confirmM2BService";
    // 已完成订单
    public static final String BTC_FINISH_ORDER = BASE_URL
            + "api/businesscenter/getfinishedOder";
    // 商家开始服务
    public static final String START_SERVICE = BASE_URL
            + "api/businesscenter/startService";
    // 商家服务完成
    public static final String FINISH_SERVICE = BASE_URL
            + "api/businesscenter/finishService";
    // 商家关闭服务
    public static final String BUSINESS_CLOSE_SERVICE = BASE_URL
            + "api/businesscenter/closeService";
    // 删除我给其他商家的订单
    public static final String DELECT_METB_ORDER = BASE_URL
            + "api/businesscenter/deletebusiOrder";
    // 商家查询自己的详细信息
    public static final String QUERY_ME_BUSINESS = BASE_URL
            + "api/businesscenter/getMyBusiness";
    // 商家修改自己的基本信息
    public static final String UPDATE_BUSINESS_INFO = BASE_URL
            + "api/businesscenter/updateBusiness";
    // 查询车辆
    public static final String GET_VEHICLE = BASE_URL
            + "api/carservice/getVehicle";
    // 商家对商家生成订单
    public static final String ADD_BUSINESS_ORDER = BASE_URL
            + "api/businesscenter/addbusiOrder";
    // 判断用户是否注册
    public static final String TEST_USERNAME = BASE_URL
            + "/api/user/testUserName";
    // 商家通过电话号码查询所有订单
    public static final String BUSINESS_QUERY_ALL_ORDER = BASE_URL
            + "api/businesscenter/searchOrder";
    // 用户注册
    public static final String USER_REGISTER = BASE_URL + "api/user/addUser";
    // 检测电话是否注册
    public static final String TEST_TELEPHONE = BASE_URL
            + "/api/user/testTelephone";
    // 通过手机修改密码
    public static final String UPDATE_PASSWORD_URL = BASE_URL
            + "api/individualCenter/updatePassword";
    // 忘记密码
    public static final String FORGET_PASSWORD_URL = BASE_URL
            + "api/user/forgetPassword";
    // 驾证违章查询
    public static final String LICENCE_VIOLATION_URL = BASE_URL
            + "api/wzservice/queryDriverInfo";
    // 判断邮箱是否绑定
    public static final String AUTH_EMAIL_URL = BASE_URL
            + "api/securitysetting/isEmailBind";
    // 邮箱绑定
    public static final String EMAIL_BIND_URL = BASE_URL
            + "api/securitysetting/bindEmail";
    // 判断邮箱是否被占用
    public static final String EMAIL_IS_BIND = BASE_URL
            + "api/securitysetting/emailIsUse";

    // 修改邮箱
    public static final String TEST_EMAIL_URL = BASE_URL
            + "/api/user/testEmail";
    // 修改邮箱
    public static final String UPDATE_EMAIL_URL = BASE_URL
            + "api/individualCenter/updateEmail";
    // 解除邮箱绑定
    public static final String EMAIL_UNBINDEMAIL_URL = BASE_URL
            + "api/securitysetting/unbindEmail";
    // 判断手机是否绑定
    public static final String AUTH_PHONEBIND_URL = BASE_URL
            + "api/securitysetting/isPhoneBind";
    // 绑定手机
    public static final String BIND_PHONE_URL = BASE_URL
            + "api/securitysetting/bindPhone";
    // 判断手机是否被占用
    public static final String IS_BIND_PHONE = BASE_URL
            + "api/securitysetting/phoneIsUse";
    // VIP查询所有车辆
    public static final String VIP_QUERY_ALL_VEHICLE = BASE_URL
            + "api/vip/getVehicleList";
    // VIP生成订单
    public static final String VIP_PRODUCE_ORDER = BASE_URL
            + "api/vip/getOrderNumber";
    // 判断是否是VIP
    public static final String IS_VIP_URL = BASE_URL + "api/vip/isVip";
    // 申请vip
    public static final String VIP_APPLY_URL = BASE_URL + "api/vip/applyVip";
    // 获取非vip的车辆
    public static final String GET_NO_VEHICLE_VIP = BASE_URL
            + "api/vip/getVehicleListNoVip";
    // 查询vip车辆
    public static final String VIP_GET_VEHICLE = BASE_URL
            + "api/vip/getRenewalsVehicle";
    // vip发布任务
    public static final String VIP_RELEASE_TASK = BASE_URL
            + "api/vip/vipPublicTask";
    // 查询vip驾证
    public static final String VIP_GET_LICENCE = BASE_URL
            + "api/vip/getRenewalsLicence";
    // vip续费
    public static final String VIP_RENEW_URL = BASE_URL + "api/vip/toRenewals";
    // 本地服务查询商家
    public static final String NATIVE_SERVICE_BUSINESS = BASE_URL
            + "api/localservice/getwxmr";
    // 本地服务获取商家信息
    public static final String NATIVE_SERVICE_TESTDRIVE_INFOS = BASE_URL
            + "api/localservice/getTestDriveInfos";
    // 通过通知的typeId查询任务详情
    public static final String QUERY_TASK_BYID = BASE_URL
            + "api/notification/getTaskById";
    // vip通过车牌号查询 业务使用详细信息
    public static final String GET_VIP_VEHICLE_INFO = BASE_URL
            + "api/vip/getVipVehicleInfo";
    // 本地服务添加预约试驾
    public static final String ADD_TESTDRIVE_INFOS = BASE_URL
            + "api/localservice/addTestDriveInfo";
    // 本地服务收藏商家
    public static final String ADD_FAVORITES = BASE_URL
            + "api/individualCenter/addFavorites";
    // 本地服务判断是否收藏商家
    public static final String IS_EXIST_FAVORITE = BASE_URL
            + "api/favoritesService/isExistFavorite";
    // 扫一扫根据商家id查询商家
    public static final String GET_BIZ_BY_ID = BASE_URL
            + "api/localservice/getBizByID";
    // 获取通知数据
    public static final String GET_NOTIFICATION_LIST = BASE_URL
            + "api/notification/getnotification";
    // 更新通知状态
    public static final String UPDATE_NOTIFICATION_STATUS = BASE_URL
            + "api/notification/updatestatus";
    // 保险服务
    public static final String ADD_INSURANCE_INFO = BASE_URL
            + "api/insurance/addInsuranceInfo";
    // 退出登录
    public static final String EXIT_LOGIN_URL = BASE_URL + "api/user/logout";
    // 判断vip是否发布相同的任务
    public static final String VIP_IS_SAME_TASK = BASE_URL
            + "api/vip/isSameTask";
    // 上传证件的地址
    public static final String UPLOAD_ZJ_IMG = BASE_URL + "api/upload/img";
    // 通过服务选项获取服务价格
    public static final String GET_SERVICEPRICE_BY_SERVICE = BASE_URL
            + "api/service/getService";
    // 通过商家查询评论
    public static final String QUERY_COMMENT_BY_BUSINESS = BASE_URL
            + "api/comment/getcomments";

    // 查询商家所有订单
    public static final String GET_ALL_BUSI_ORDER = BASE_URL
            + "api/businesscenter/getallOrder";
    // 商家拆分订单api
    public static final String BUSINESS_CF_ORDER = BASE_URL
            + "api/businesscenter/addbusiOrder1";
    // 商家更新价格
    public static final String BUSINESS_UPDATE_PRICE = BASE_URL
            + "api/businesscenter/updatePaymentAmount";
    public static final String DELETA_TASK = BASE_URL + "api/task/deleteTask";
    // 通过任务id查询任务详情
    public static final String GETTASK_BYTASKID = BASE_URL
            + "api/businesscenter/gettaskbyid";

    // 对任务评价
    public static final String ADD_EVALUATE = BASE_URL
            + "api/comment/addcomment";
    //地址搜索
    public static final String SEARCH_LOCATION = BASE_URL + "/api/address/searchLocation";
    // 查询我的收藏商家
    public static final String QUERY_BUSINESS_LIST = BASE_URL
            + "api/favoritesService/getBusinessFavoritesList";
    //修改第一张头像
    public static final String CHANGE_IMAGE_SORT = BASE_URL + "/api/imageservice/changeImageSort";
    // 通过商家id查询商家信息
    public static final String QUERY_BUSINESS_INFO_BYID = BASE_URL
            + "api/businesscenter/getBizById";
    // 投诉
    public static final String ADD_COMPLAIN = BASE_URL
            + "api/complain/addcomplain";
    // 意见反馈api/complain/addfeedback
    public static final String ADD_FEEDBACK = BASE_URL
            + "api/complain/addfeedback";
    // 查询扣分
    public static final String QUERY_SCORE = BASE_URL
            + "api/service/getWeiZhangKnowledge";

    // 商家查看对自己的投诉建议
    public static final String BUSINESS_QUERY_SUGGESTION = BASE_URL
            + "api/complain/getcomplain";
    // 车务无忧版本信息
    public static final String APP_VERSION = BASE_URL
            + "/json/MobileAppVersion.json";

    // 通过车牌号查询车辆信息
    public static final String SELECT_VEHICLE_BY_PLATE = BASE_URL
            + "api/carservice/getVehicleByPlate";
    // 通过档案编号查询驾证
    public static final String SELECT_LICENSE_BY_FILE = BASE_URL
            + "api/licenceservice/getLicenceByFile";
    // 获取系统提醒
    public static final String GET_REMIND_URL = BASE_URL
            + "api/notification/getTiXing";
    // 获取抵用券
    public static final String GET_VOUCHER_URL = BASE_URL
            + "api/voucherservice/getVoucherBalance";
    // 使用抵用券
    public static final String USE_VOUCHER = BASE_URL
            + "api/voucherservice/addVoucher";
    // 账户余额
    public static final String GET_ACCOUNT_BALANCE = BASE_URL
            + "api/billservice/getAccountBalance";

    // 获取抵用券列表
    public static final String GET_VOUCHER_LIST = BASE_URL
            + "api/voucherservice/getVoucherList";

    // 即时通讯查找好友
    public static final String CHAT_SEARCH_FRIEND = BASE_URL
            + "api/chat/findFri";
    // 即时通讯查找我的房间
    public static final String MY_ROOMS = BASE_URL + "api/chat/myRooms";
    // 即时通讯查找房间内信息
    public static final String IN_ROOM = BASE_URL + "api/chat/inRoom";
    // 二手车在线
    public static final String USED_CAR_ONLINE = "http://www.gongpingjia.com/api/cars/evaluation/chewuwuyou/";
    // 获取用户头像和昵称
    public static final String GET_CHAT_USER_INFO = BASE_URL
            + "api/user/getUserInfo";
    // 查看附近的车友
    public static final String NEARBY_CAR_FRIEND = BASE_URL
            + "/api/user/getNearBys";
    // 查看更多附近的车友
    public static final String MORE_NEARBY_CAR_FRIEND = BASE_URL
            + "/api/user/getThesePeople";
    // 上传位置信息
    public static final String SET_UP_LOCATION = BASE_URL
            + "api/user/setUpMyLocation";
    // 获取同品牌车友
    public static final String SAME_BRAND_CAR_FRIEND = BASE_URL
            + "api/user/sameBrand";

    // 获取车友信息
    public static final String GET_USER_INFO = BASE_URL
            + "api/user/getPeopleInfo";
    // 个人中心更新照片墙
    // public static final String UPDATE_PERSON_PIC_INFO = BASE_URL
    // + "api/upload/uploadIcon";

    // 提交背景墙图片
    public static final String UPLOADE_PIC_BG = BASE_URL
            + "api/individualCenter/uploadback";
    // 移除照片墙的某张照片
    public static final String REMOVE_USER_PIC = BASE_URL
            + "api/individualCenter/removeIcon";
    // 聊天发送图片
    public static final String UPLOADE_CAHT_IMG = BASE_URL
            + "api/upload/chatImgB";

    // 请求集群服务器
    public static final String GET_SERVERS = BASE_URL + "api/upload/defini";
    // 给用户发送离线推送
    public static final String SEND_UNAVAIL = BASE_URL + "api/chat/sendUnavail";
    // 朋友圈
    public static final String ALL_QUAN = BASE_URL + "api/quan/allQuanWen";// 获取所有圈文
    public static final String DEL_QUAN = BASE_URL + "api/quan/delQuanWen";// 删除一个圈文
    public static final String PING_QUAN = BASE_URL + "api/quan/pingQuanWen";// 评
    public static final String REPLY_QUAN_PING = BASE_URL
            + "api/quan/replyQuanPing";// 回评
    public static final String DEL_QUAN_PING = BASE_URL
            + "api/quan/delQuanPing";// 删评
    public static final String QUAN_TOGGLE_ZAN = BASE_URL
            + "api/quan/toggleZan";// 切换赞
    public static final String PUBLISH_QUAN = BASE_URL + "api/quan/publish";// 发布一个圈文
    public static final String GET_MY_QUAN = BASE_URL + "api/quan/quanPublish";// 获取我的圈文
    public static final String UPLOAD_QUAN_IMG = BASE_URL
            + "api/upload/quanImgB";// 上传图片
    public static final String QUAN_DETAIL = BASE_URL + "api/quan/quanDetail";// 详情

    // BBS
    public static final String GET_ALL_BANKUAI = BASE_URL
            + "api/bbs/allBanKuai";// 加载所有的论坛
    public static final String ALL_TIE = BASE_URL + "api/bbs/allTieZi";// 获取所有帖子
    public static final String DEL_TIE = BASE_URL + "api/bbs/delTieZi";// 删除一个帖子
    public static final String PING_TIE = BASE_URL + "api/bbs/pingTieZi";// 评帖子

    public static final String REPLY_TIE_PING = BASE_URL
            + "api/bbs/replyTiePing";// 回评
    public static final String DEL_TIE_PING = BASE_URL + "api/bbs/delTiePing";// 删评
    public static final String TIE_TOGGLE_ZAN = BASE_URL + "api/bbs/toggleZan";// 切换赞
    public static final String PUBLISH_TIE = BASE_URL + "api/bbs/publish";// 发布一个帖子
    public static final String GET_MY_TIE = BASE_URL + "api/bbs/BBSPublish";// 获取我的帖子
    public static final String UPLOAD_TIE_IMG = BASE_URL + "api/upload/tieImgB";// 上传图片
    public static final String TIE_DETAIL = BASE_URL + "api/bbs/tieZiDetail";// 帖子详情

    // 活动
    public static final String ALL_YUE = BASE_URL + "api/yue/allYueYue";// 获取所有活动
    public static final String DEL_YUE = BASE_URL + "api/yue/delYueYue";// 删除一个活动
    public static final String PING_YUE = BASE_URL + "api/yue/pingYue";// 评活动
    public static final String REPLY_YUE_PING = BASE_URL
            + "api/yue/replyYuePing";// 回评
    public static final String DEL_YUE_PING = BASE_URL + "api/yue/delYuePing";// 删评
    public static final String YUE_TOGGLE_ZAN = BASE_URL + "api/yue/toggleZan";// 切换赞
    public static final String PUBLISH_YUE = BASE_URL + "api/yue/publish";// 发布一个活动
    public static final String GET_MY_YUE = BASE_URL + "api/yue/yuePublish";// 获取我的活动
    public static final String UPLOAD_YUE_IMG = BASE_URL + "api/upload/yueImgB";// 上传图片
    public static final String YUE_DETAIL = BASE_URL + "api/yue/yueDetail";// 活动详情
    public static final String YUE_APPLY = BASE_URL + "api/yue/apply";// 申请加入活动
    public static final String YUE_DENY_APPLY = BASE_URL + "api/yue/deny";// 拒绝申请加入活动
    public static final String GET_CONTACTS_FRIEND = BASE_URL
            + "api/chat/findFris";// 获取通讯录的好友

    public static final String UPLOADE_CHAT_VOICE = BASE_URL
            + "api/upload/chatVoice";// 聊天发送语音
    public static final String ADD_FRIEND_SEND = BASE_URL + "api/chat/sendAdd";// 加好友发推送
    public static final String RELEVANCE_ORDER = BASE_URL
            + "api/businesscenter/allBetUs";// 查询与商家之间的关联订单
    public static final String QUERY_INROOM = BASE_URL + "api/chat/inRoom";// 查询群成员的信息
    public static final String GET_ALL_PRO = BASE_URL + "api/service/getPro";

    public static final String GET_ALL_CONFIG = "http://www.cheshouye.com/api/weizhang/get_all_config";
    public static final String APPLY_FOR_TUIKUAN = BASE_URL + "api/task/newMakeTui";// 申请退款
    public static final String COLLECT_ME_URL = BASE_URL
            + "api/favoritesService/whoLikeMe";// 收藏我的
    public static final String AGREE_DIRECT = BASE_URL
            + "api/businesscenter/agreeDirect";
    public static final String QUERY_BRANDS = "http://192.168.56.1:8080/mhwcw/api/carInfo/getBrandNameList";

    public static final String MAKE_ACCU = BASE_URL + "api/accusation/makeAccu";// 评帖子
    public static final String GET_YINLIAN_TN = BASE_URL
            + "api/pay/makePayTnForUP";// 获取银联tn
    public static final String APP_VERSION_URL = BASE_URL + "api/ver/and";// 获得APP版本
    public static final String BAIDU_QUERY_ILLEGAL = "http://api.open.baidu.com/pae/traffic/api/query";

    public static final String CONFIRM_ZHIFU_PRICE = BASE_URL
            + "api/businesscenter/directConfirm";// 商家B确认直付接口

    public static final String CONFIRM_EARLY_PAY = BASE_URL
            + "api/businesscenter/makeDirectPay";// 商家A确认自己已支付

    public static final String A_CONFIRM_SERVICE_FINISH = BASE_URL
            + "api/businesscenter/directFinishService";// 商家A确认服务已完成

    public static final String B_START_SERVICE = BASE_URL
            + "api/businesscenter/directStartService";// 商家B看到收到款后开始服务

    public static final String B_END_SERVICE = BASE_URL
            + "api/businesscenter/directEndService";// 商家B确认服务完成

    public static final String GET_ALL_PRO_PRICE = BASE_URL
            + "api/service/getPriceDetail";// 商家获取所有服务及价格

    public static final String UPDATE_SERVICE_PRICE = BASE_URL
            + "api/service/defineService";// 商家更新服务价格
    public static final String BUS_INCOME_ANALYSIS = BASE_URL
            + "api/businesscenter/busIncomeAnalysis";// 查询总收益、未结算、已结算及task简略信息
    public static final String ORDER_FENERA_SITUATION = BASE_URL
            + "api/businesscenter/orderGeneralSituation";// 查询订单状态统计
    public static final String FINISH_NOTE_SERVICE = BASE_URL
            + "api/businesscenter/finishNoteService";// 完成录入订单
    public static final String CLOSE_NOTE_SERVICE = BASE_URL
            + "api/businesscenter/closeNoteService";// 关闭录入的订单
    public static final String QUERY_A_TASK = BASE_URL + "api/task/getTaskInfo";// 查询单个订单
    // public static final String CREATE_PAY_ORDER =
    // "http://115.29.193.7:8081/cwwyw_admin/wxPay/createPayOrder";// 查询单个订单

    public static final String SETTING_PAY_ACCOUNT = BASE_URL
            + "api/businesscenter/setUpAccount";// 设置支付宝账户
    public static final String QUERY_ACCOUNT = BASE_URL
            + "api/businesscenter/getAcc";// 查看账户

    public static final String PAY_NOTE_SERVICE = BASE_URL
            + "api/businesscenter/payNoteService";// 记账订单-确认支付

    public static final String START_NOTE_SERVICE = BASE_URL
            + "api/businesscenter/startNoteService";// 记账订单-开始服务

    public static final String CSY_QUERY_ILLEGAL_URL = "http://www.cheshouye.com/api/weizhang/query_task?";// 车首页查询违章

    public static final String WXPAY_NOTIFY_URL = BASE_URL
            + "api/wxPay/createPayOrder";

    public static final String ALIPAY_NOTIFY_URL = BASE_URL
            + "api/alipay/notify_url";
    // 发送短信
    public static final String SEND_PHONE_MSG = BASE_URL + "api/mes/makeMes";

    public static final String B_NEW_TASK = BASE_URL
            + "api/businesscenter/newBTask";// B类商家
    // 发布任务

    public static final String GET_NEW_B_TASK = BASE_URL
            + "api/businesscenter/getNewBTask";// 获取所有B类订单

    public static final String BUSINESS_ENTER = BASE_URL
            + "api/businesscenter/registAsBusiness";// 商家入驻

    public static final String MAKE_OFFER = BASE_URL
            + "api/businesscenter/makeOffer";// 商家报价(我要接单)

    public static final String GET_OFFER = BASE_URL
            + "api/businesscenter/getOffer";// 查看一个B类订单的报价信息

    public static final String UPDATE_OFFER = BASE_URL
            + "api/businesscenter/changeOffer";// 修改B类订单报价

    public static final String SETUP_ONE_PRICE = BASE_URL
            + "api/businesscenter/setUpOnePrice";// 采纳一个报价
    public static final String ID_CARD_IMG = BASE_URL
            + "api/upload/identityCardForRegB";// 身份证图片
    public static final String ID_CARD_BACK = BASE_URL
            + "api/upload/identityBeiImageUrlB";// 身份证背面
    public static final String HEAD_IMG = BASE_URL
            + "api/upload/headImageForRegB";// 提交手持证件
    public static final String HOLD_IMG = BASE_URL
            + "api/upload/handImageForRegB";// 上传头像
    public static final String STORE_IMG = BASE_URL
            + "api/upload/storeFrontImageForRegB";// 上传店头图片
    public static final String LICENSE_IMG = BASE_URL
            + "api/upload/licenseImageForRegB";// 营业执照图片

    public static final String SEARCH_BUSINESS_REGIST_STATUS = BASE_URL
            + "api/businesscenter/searchForRegistStatus";// 查看商家入驻情况

    public static final String GET_ALL_PROVINCE = BASE_URL
            + "api/address/getAllProvince";// 获取所有省

    public static final String GET_ALL_CITY = BASE_URL
            + "api/address/getAllCity";// 获取所有市

    public static final String GET_DISTRICT_BY_CITY = BASE_URL
            + "api/address/getDistrictByCity";// 根据市查询地区

    public static final String GET_ALL_DISTRICT = BASE_URL
            + "api/address/getAllDistrict";

    // 账户提现列表
    public static final String GET_ALL_WITHDRAWALS = BASE_URL
            + "api/finance/withdrawAccountManagementList";
    // 添加账户
    public static final String Add_WITHDRAWALS = BASE_URL
            + "api/finance/withdrawAccountManagementAdd";
    // 默认提现账户
    public static final String GET_DEFAULT_WITHDRAWALS = BASE_URL
            + "api/finance/withdrawAccountManagementUpdate";
    // 删除提现账户
    public static final String DELETE_WITHDRAWALS = BASE_URL
            + "api/finance/withdrawAccountManagementDelete";
    // 钱包首页
    public static final String GET_WALLET = BASE_URL
            + "api/finance/getWalletInfo";

    // 检索是否设置支付密码
    public static final String WHETHER_PASSWORD = BASE_URL
            + "api/finance/payPasswordTest";

    // 提交问题以及设置支付密码
    public static final String SUBMIT_PAYMENT_PASSWORD = BASE_URL
            + "api/finance/setPayPassword";

    // 判断旧密码是否正确
    public static final String JUDGE_PAY_PASSWORD = BASE_URL
            + "api/finance/judgePayPassword";
    // 修改新的支付密码
    public static final String UPDATE_PAY_PASSWORD = BASE_URL
            + "api/finance/updatePayPassword";

    // 忘记密码获得密保问题
    public static final String TWO_QUESTION = BASE_URL
            + "api/finance/twoQuestion";
    // 查看明细
    public static final String QUERY_MINGXI = BASE_URL
            + "api/finance/getWalletDetail";
    // 忘记支付密码的验证
    public static final String FORGET_PAY_PASSWORD = BASE_URL
            + "api/finance/forgetPayPassWord";
    // /提现查询默认账户及费率/makeAWithdrawPage
    public static final String MAKE_AWITHDRAW_PAGE = BASE_URL
            + "api/finance/makeAWithdrawPage";
    // 提现makeAWithdraw
    public static final String MAKE_AWITHDRAW = BASE_URL
            + "api/finance/makeAWithdraw";

    // 钱包支付/balancePayment
    public static final String BALANCE_PAYMENT = BASE_URL
            + "api/finance/balancePayment";

    // 检测当前账户的冻结情况及余额checkingAccount
    public static final String CHECKING_ACCOUNT = BASE_URL
            + "api/finance/checkingAccount";
    // /本接口可以自动根据当前用户是省代还是市代，查询其下辖情况。如果type是1则是省代，如果是2则是市代。
    // 因数据和新表t_task_history有关，所以以前的老订单不参与本接口统计。
    public static final String CHECK_AGENCY_FOR_ORDER = BASE_URL
            + "api/businesscenter/checkAgencyForOrder";
    // 省代查询本省商家情况
    public static final String CHECK_AGENCY_FOR_BUS = BASE_URL
            + "api/businesscenter/checkAgencyForBus";

    // 查看评论
    public static final String SEE_COMMENT = BASE_URL
            + "api/task/getPingLunByTaskId";

    // 删除账单明细
    public static final String DECEL_MINGXI = BASE_URL
            + "api/finance/deleteMingXi";
    // 身份证背面
    public static final String ID_CARD_IMGB = BASE_URL
            + "api/upload/identityCardForRegB";// 身份证图片
    // 获取提现手续费
    public static final String GET_RATE = BASE_URL
            + "api/finance/getChargeRate";
    // 获取运营推广
    public static final String GET_BANNER = BASE_URL + "api/ver/banner";

    // 通过省获取市
    public static final String GET_CITY_BY_PROVINCE = BASE_URL
            + "api/address/getCityByProvince";
    // 支付宝支付意图调起
    public static final String ALIPAY_PAY_PURPOSE = BASE_URL
            + "api/alipay/recordPayPurposeInfo";
    // 微信支付意图调起
    public static final String WX_PAY_PURPOSE = BASE_URL
            + "api/wxPay/recordPayPurposeInfo";
    // 查询城市ID
    public static final String INQUIRE_CITY_ID = BASE_URL
            + "/api/address/getLocationIdByLocationName";
    // 发起一个支付宝的充值订单
    public static final String ALIPAY_RECHARGE_ORDER = BASE_URL
            + "api/finaceRecharge/createFinaceRechargeOrder";
    // 支付宝充值回调
    public static final String ALIPAY_CZ_NOTIFY_URL = BASE_URL
            + "api/alipay/notify_url4Recharge";
    // 根据好评、中评、差评获取评论
    public static final String GET_ALL_COMMENT = BASE_URL
            + "api/comment/getAllComments";

    public static void initNetwork(Context context) {
        cookieStore = new PersistentCookieStore(context);
        fh.configCookieStore(cookieStore);
        fh.configUserAgent(Tools.getPhoneUA(context));
        fh.configTimeout(10000);
        fhMulti.configUserAgent(Tools.getPhoneUA(context));
    }


    public static FinalHttp getFhMulti() {
        return fhMulti;
    }

    public static void setFhMulti(FinalHttp mFhMulti) {
        fhMulti = mFhMulti;
    }

    private static String tokenMethod() {

        return CacheTools.getUserData("telephone") + ":"
                + CacheTools.getUserData("loginpassword") + ":android:"
                + CacheTools.getUserData("businessId") + ":"
                + CacheTools.getUserData("token");
    }

    private static String tokenMethodMulti() {
        // 测试缓存问题 update start by yuyong 2016/03/18 redmine:335 由于每次请求没有session
        fhMulti.configCookieStore(cookieStore);
        fhMulti.configTimeout(10000);
        // update end by yuyong 2016/03/18
        MyLog.e("YUY", "token = " + CacheTools.getUserData("telephone") + ":"
                + CacheTools.getUserData("loginpassword") + ":android:"
                + CacheTools.getUserData("businessId") + ":"
                + CacheTools.getUserData("token"));
        return CacheTools.getUserData("telephone") + ":"
                + CacheTools.getUserData("loginpassword") + ":android:"
                + CacheTools.getUserData("businessId") + ":"
                + CacheTools.getUserData("token");
    }

    // add start by yuyong 2016/04/09 退出应用清除用到的所有cookie

    public static void removeCookie(Context context) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        CookieSyncManager.getInstance().sync();
    }

    // add start by yuyong 2016/04/09

    public static void get(String url, AjaxCallBack<? extends Object> callBack) // 用一个完整url获取一个string对象
    {
        fh.addHeader("token", tokenMethod());
        fh.addHeader("version", CacheTools.getUserData("version"));
        fh.get(url, callBack);

    }

    public static void get(String url, AjaxParams params,
                           AjaxCallBack<? extends Object> callBack) // url里面带参数
    {
        fh.addHeader("token", tokenMethod());
        fh.addHeader("version", CacheTools.getUserData("version"));
        fh.get(url, params, callBack);

    }

    public static void get(String url, Header[] headers, AjaxParams params,
                           AjaxCallBack<? extends Object> callBack) // 不带参数，获取json对象或者数组
    {
        fh.addHeader("token", tokenMethod());
        fh.addHeader("version", CacheTools.getUserData("version"));
        fh.get(url, headers, params, callBack);

    }

    public static void post(String url, AjaxParams params, AjaxCallBack<? extends Object> callBack) // 带参数，获取json对象或者数组
    {
        fh.addHeader("token", tokenMethod());
        fh.addHeader("version", CacheTools.getUserData("version"));
        fh.post(url, params, callBack);

    }

    public static void postMulti(String url, AjaxParams params,
                                 AjaxCallBack<? extends Object> callBack) // url里面带参数
    {
        if (!url.equals(NetworkUtil.USER_REGISTER)
                && !url.equals(NetworkUtil.TEST_USERNAME)
                && !url.equals(NetworkUtil.FORGET_PASSWORD_URL)) {
            fhMulti.addHeader("token", tokenMethodMulti());
            MyLog.e("YUY", "登录版本号 = " + CacheTools.getUserData("version"));
            fhMulti.addHeader("version", CacheTools.getUserData("version"));
        }
        fhMulti.post(url, params, callBack);


    }

    public static void postNoHeader(String url, AjaxParams params,
                                    AjaxCallBack<? extends Object> callBack) // 带参数，获取json对象或者数组
    {
        fh.post(url, params, callBack);
    }

    public static FinalHttp getClient() {
        return fh;
    }

    public static void clearCookie() {
        cookieStore.clear();
    }

    /**
     * 获取网络是否连接
     *
     * @param context
     * @return
     */
    public static NetworkInfo getActiveNetwork(Context context) {
        if (context == null)
            return null;
        ConnectivityManager mConnMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mConnMgr == null)
            return null;
        NetworkInfo aActiveInfo = mConnMgr.getActiveNetworkInfo(); // 获取活动网络连接信息
        return aActiveInfo;
    }

    public static final String SELECT_BUSSINESS_LIST = BASE_URL
            + "api/businesscenter/getBussiList";
    /**
     * 商家个人中心
     */
    public static final String SELECT_BUSSINESS_DETAILS = BASE_URL
            + "api/businesscenter/getBusinessInfo";


    /**
     * 取消接单
     */
    public static final String SELECT_DELETE = BASE_URL + "api/businesscenter/deleteOffer";

    /**
     * 服务管理
     */
    public static final String SELECT_SERVICE_ADMINISTRATION = BASE_URL + "/api/service/getPriceDetail";

    /**
     * 删除服务
     */
    public static final String ADD_SERVICE = BASE_URL + "/api/service/addService";
    /**
     * 修改服务
     */
    public static final String EDIT_SERVICE = BASE_URL + "/api/service/editService";
    /**
     * 添加服务
     */
    public static final String ADD_SERVICE_MODIFY = BASE_URL + "/api/service/getOtherPro";
    /**
     * 删除服务
     */
    public static final String DELETE_SERVICE = BASE_URL + "/api/service/deleteService";

    /**
     * 邮寄地址列表查询
     */

    public static final String MAIL_QUERY = BASE_URL + "/api/mailingAddress/getAllMailingAddress";

    /**
     * 添加邮寄地址
     */
    public static final String ADD_MAIL_ADDRESS = BASE_URL + "/api/mailingAddress/addMailingAddress";

    /**
     * 删除邮寄地址
     */
    public static final String DELETE_MAIL_ADDRESS = BASE_URL + "/api/mailingAddress/deleteMailingAddress";

    /**
     * 修改邮寄地址
     */
    public static final String UPDATE_MAIL_ADDRESS = BASE_URL + "/api/mailingAddress/updateMailingAddress";

    /**
     * 修改默认地址
     */
    public static final String UPDATE_MAIL_DEFAULT = BASE_URL + "/api/mailingAddress/setTheDefaultAddress";

    /**
     * 添加物流信息
     */
    public static final String ADD_LOGISTICS = BASE_URL + "/api/task/saveExpressNo";

    /**
     * 检验密码
     */
    public static final String VERIFY_PASSWORD = BASE_URL + "api/user/checkPassword";
    //地区代码查询
    public static final String QUERY_DQDM = BASE_URL + "api/area/getAreaListByCondition";
    //违章代码查询
    public static final String QUERY_WZDM = BASE_URL + "api/weiZhangKnowledge/getWeiZhangKnowledgeListByCode";
    /**
     * 新的获取订单
     */
//    public static final String GET_ORDER=BASE_URL+"api/businesscenter/getOrdersForBusiness";
    //拉黑好友
    public static final String LAHEI_CHEYOU = BASE_URL + "api/blackList/addToBlackListByMeAndOther";
    //判断是否是黑名单用户
    public static final String IS_BLACKFRIEND = BASE_URL + "api/blackList/getBlackListByMeAndOther";

    //解除黑名单限制
    public static final String REMOVE_FROM_BLACK = BASE_URL + "api/blackList/removeFromBlackListByMeAndOther";
    //得到所有黑名单用户
    public static final String GET_ALL_BLACKFRIEND = BASE_URL + "api/blackList/findBlackListListByMeAndNickName";
    //从黑名单列表删除
    public static final String DELETE_FROM_BLACK = BASE_URL + "api/blackList/deleteFromBlackListByMeAndOther";

    /**
     * 工作台筛选订单
     */
    public static final String GET_ALL_ORDER_FOR_BUS = BASE_URL + "api/businesscenter/getAllOrderForBus";

    /**
     * 根据订单号订单
     */
    public static final String ORDER_NUMBER = BASE_URL + "api/businesscenter/getAllOrderForBusForSearch";

    //得到商家优势，疑难杂症
    public static final String SJYS = BASE_URL + "/api/operationDemand/getOperationDemandListByCondition";
    //发布商家优势/疑难杂症
    public static final String ADD_SJYS = BASE_URL + "/api/operationDemand/addOperationDemand";
    /**
     * 业务大厅接单接口
     */
    public static final String BUSINESS_BILLING = BASE_URL + "api/task/getTaskInfoByTaskId";

    /**
     * 同系车友新接口
     */
    public static final String FELLOW_RIDERS = BASE_URL + "api/user/getHomologousCarFriends";

    /**
     * 注册新接口
     */
    public static final String REGISTER_CACHE = BASE_URL + "api/user/registApp";


    /**
     * 获取 token
     */

    public static final String fdsgn = BASE_URL + "api/qiniu/getUpToken";

    /**
     * 获取好友列表
     */
    public static final String FRIEND_GROUPING = XMPP_HOST_URL + "/api/im/friend/v1/FriendList";


    /**
     * 获取群列表
     */
    public static final String GROUP_LIST = XMPP_HOST_URL + "/api/im/group/v1/selectAllImGroupsByUserId";

    /**
     * 添加群
     */
    public static final String ADD_GROUP = XMPP_HOST_URL + "/api/im/group/v1/addImGroup";

    /**
     * 搜索好友
     */
    public static final String SEARCH_USER = XMPP_HOST_URL + "/api/im/friend/v1/selectFriend";
    /**
     * 获取所有好友
     */
    public static final String GET_ALL_FRIEND = XMPP_HOST_URL + "/api/im/friend/v1/friendGroup";

    /**
     * 获取融云token
     */
    public static final String GET_RONG_TOKEN = XMPP_HOST_URL + "/api/im/user/v1/getToken/";

    /**
     * 群成员
     */
    public static final String GROUP_SET_UP = XMPP_HOST_URL + "/api/im/group/v1/getAllGroupMemberInfo";
    /**
     * 群基本信息
     */
    public static final String GROUP_BASIC = XMPP_HOST_URL + "/api/im/group/v1/getImGroupInfoByGroupIdAndUserId";
    /**
     * 获取申请好友列表
     */
    public static final String FRIEND_APPLY_LIST = XMPP_HOST_URL + "/api/im/friend/v1/friendApplyList";
    /**
     * 同意加好友
     */
    public static final String AGREE_FRIEND = XMPP_HOST_URL + "/api/im/friend/v1/agreeFriend";
    /**
     * 修改好友备注
     */

    public static final String ALTER_FRIEND_NAME = XMPP_HOST_URL + "/api/im/friend/v1/updateFriend";

    /**
     * 修改分组昵称
     */

    public static final String ALTER_FENZU_NAME = XMPP_HOST_URL + "/api/im/friend/v1/updateFriendGroup";

    /**
     * 移动好友至XX分组/恢复黑名单，（从黑名单里移除）
     */

    public static final String REMOVE_FIREND_TO_ZU = XMPP_HOST_URL + "/api/im/friend/v1/moveFriend";


    /**
     * 加入黑名单
     */
    public static final String LAHEI_FIREND = XMPP_HOST_URL + "/api/im/friend/v1/blackFriend";


    /**
     * 删除好友分组
     */

    public static final String DELETE_FIREND_FENZU = XMPP_HOST_URL + "/api/im/friend/v1/deleteFriendGroup";

    /**
     * 删除好友
     */

    public static final String DELETE_FRIEND = XMPP_HOST_URL + "/api/im/friend/v1/deleteFriend";


    /**
     * 添加好友
     */

    public static final String ADD_FRIEND = XMPP_HOST_URL + "/api/im/friend/v1/addFriend";


    /**
     * 添加好友分组
     */
    public static final String ADD_FRIEND_FENZU = XMPP_HOST_URL + "/api/im/friend/v1/addFriendGroup";

    /**
     * 屏蔽群消息
     */
    public static final String SHIELD_GROUP = XMPP_HOST_URL + "/api/im/group/v1/updateImGroupMemberMaskMessage";

    /**
     * 移交群成员
     */
    public static final String GROUP_NOTICE = XMPP_HOST_URL + "/api/im/group/v1/updateImGroupByCondition";

    /**
     * 群昵称
     */
    public static final String GROUP_Nick_NAME = XMPP_HOST_URL + "/api/im/group/v1/updateImGroupMemberRemarkName";
    /**
     * 删除群成员
     */
    public static final String DELETE_GROUP = XMPP_HOST_URL + "/api/im/group/v1/quitGroupBatch";
    /**
     * 全部群成员
     */
    public static final String WHOLE_GROUP = XMPP_HOST_URL + "/api/im/group/v1/queryGroupUserListByPage";

    /**
     * 添加群成员
     */
    public static final String ADD_SET_GROUP = XMPP_HOST_URL + "/api/im/group/v1/joinGroupBatch";
    /**
     * 获取七牛token
     */
    public static final String QI_NIU_TOKEN = XMPP_HOST_URL + "/api/im/qiNiuCloud/v1/getUpToken";
    /**
     * 修改群头像
     */
    public static final String GROUP_HEAD = XMPP_HOST_URL + "/api/im/group/v1/updateImGroupByCondition";
    /**
     * 新的获取用户信息
     */
    public static final String NEW_GET_PEOPLE_INFO = XMPP_HOST_URL + "/api/im/user/v1/getPeopleInfo";

    /**
     * 搜索群成员
     */
    public static final String GROUP_MEMBER = XMPP_HOST_URL + "/api/im/group/v1/queryGroupUserListByPage";
    /**
     * 搜索群
     */
    public static final String GROUP = XMPP_HOST_URL + "/api/im/group/v1/selectAllImGroupsByUserIdForPage";
    /**
     * 删除好友申请
     */
    public static final String DELETE_FRIEND_ASK = XMPP_HOST_URL + "/api/im/friend/v1/deleteFriendAsk";
    /**
     * 添加好友
     */
    public static final String ADD_GROUP_CHENGYUAN = XMPP_HOST_URL + "/api/im/friend/v1/findMyFriendsBySeek";

    /**
     * 新的获取验证码的接口
     */
    public static final String GET_AUTHCODE = XMPP_HOST_URL + "/api/account/user/v1/sendValidateCode";
    /**
     * 新的注册接口
     */
    public static final String USER_REGISTE = XMPP_HOST_URL + "/api/account/user/v1/registerByApi";
    /**
     * 验证验证码的接口
     */
    public static final String DO_GET_AUTHCODE = XMPP_HOST_URL + "/api/account/user/v1/validateCode";

    /**
     * 群组解散群
     */
    public static final String DISSOLUTION_GROUP = XMPP_HOST_URL + "/api/im/group/v1/dismissGroup";
    /**
     * 添加群成员
     */
    public static final String ADD_GROUP_MEMBE = XMPP_HOST_URL + "/api/im/friend/v1/friendGroupWithFilter";


    /**
     * 刷新用户
     */
    public static final String REFRESH_USER = XMPP_HOST_URL + "/api/im/user/v1/refreshUser";
    /**
     * 七牛上传图片基地址
     */
    public static final String QI_NIU_BASE_URL = "http://img.cddang.com/";
    /**
     * 百度短地址生成
     */
    public static final String GET_SHORT_URL = "http://api.cddang.com/api/common/dwz/v340/getShortUrl";

}
