package com.chewuwuyou.app.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import com.chewuwuyou.app.ui.LoginActivity;

/**
 * ErrorCode定义类，后面会填充
 *
 * @author XH
 * @version 1.0
 * @created 2015-1-17
 */
public class ErrorCodeUtil {
    // 个人中心
    public static class IndividualCenter {
        public static final int LOGINED_IN_OTHER_PHONE = 15;// 您已在另一台手机登陆
        public static final int LOGIN_PAST = 0;//登录过期
        public static final int ROLE_CHANGE = 27;//权限变更
    }

    // 商家
    public static class Business {
        public static final int NOT_BUSINESS = 110;// 不是商家用户
        public static final int BUSINESS_QUERY_FAILED = 112;// 查询商家失败
        public static final int NONE_FAVORITE_BUSINESSES = 113;// 暂无收藏商家信息
    }

    // 违章
    public static class Illegal {
        public static final int QUERY_FAILED = 210;// 查询失败
    }

    // 驾证
    public static class Licence {
        public static final int NONE_SUCH_LICENCE = 310;// 当前用户不存在此驾证
        public static final int NONE_LICENCES = 311;// 当前用户不存在任何驾证
        public static final int NONE_UNVIP_LICENCES = 312;// 当前用户暂无非vip驾证信息
        public static final int PUBLISH_FAILED = 313;// 驾证任务发布失败
        public static final int FILENUM_INVALID = 314;// 档案编号无效
    }

    // 车辆
    public static class Vehicle {
        public static final int NONE_SUCH_VEHICLE = 410;// 当前用户不存在此车辆
        public static final int NONE_VEHICLES = 411;// 当前用户不存在任何车辆
        public static final int NONE_UNVIP_VEHICLES = 412;// 当前用户暂无非vip车辆信息
        public static final int PUBLISH_FAILED = 413;// 车辆任务发布失败
        public static final int ENGINENUM_INVALID = 414;// 发动机号无效
        public static final int PLATENUM_INVALID = 415;// 车牌号无效
    }

    // 订单
    public static class Order {
        public static final int FORBID_DELETE = 510;// 不能删除订单
        public static final int USE_VOUCHER_PAY_EXCEED_LIMIT = 511; // 使用抵用券超限额
        public static final int PAY_EXCEED = 512; // 支付超额
        public static final int PAY_INSUFFICIENT = 513; // 支付不足
    }

    // 任务
    public static class Task {
        public static final int PUBLISH_FAILED = 610;// 发布任务失败
        public static final int IS_EXISTED = 611;// 任务已存在
        public static final int STATUS_NOT_CORRECT_TO_START_SERVICE = 612;// 任务状态不对不能开始服务
        public static final int STATUS_NOT_CORRECT_TO_FULFIL_SERVICE = 613;// 任务状态不对不能完成服务
    }

    // 约约
    public static class YUEYUE {
        public static final int NEED_IMPROVE_PERSONAL_INFO = 710;// 加入活动需要您完善个人信息
        public static final int NOT_SPECIFY_APPLY_OBJECT = 711;// 未指定申请对象
        public static final int NOT_SPECIFY_APPLY_INFORMATION = 712;// 未指定申请信息
        public static final int NOT_PUBLISHER = 713;// 您不是发布者，无权操作
        public static final int LOCAL_NOT_CHOSED = 714;// 未选择地区
        public static final int DETAIL_NOT_SET = 715;// 未设置约约信息
        public static final int NONE_TITLE_OR_CONTENT = 716;// 未包含标题或者内容
        public static final int IS_EXISTED = 717;// 同样的YueYue信息已经存在
        public static final int DELETE_FAILED = 718;// 删除失败
        public static final int ID_MUST_PROVIDED = 719;// 活动ID必须提供
    }

    // 通知8开头
    public static class QUAN {

    }

    // 通知
    public static class Notification {
        public static final int NONE_NOTI = 411;// 判断是否有车辆
        public static final int NONE_REMIND = 910;// 判断是否有提醒

    }

    // 通用
    public static class Common {
        public static final int PARAMETER_ERROR = 1110;// 参数异常
        public static final int EXECUTE_FAILED = 1111;// 执行失败
        public static final int SYSTEM_EXCEPTION = 1112;// 系统异常
        public static final int ANIMUS_ACCESS = 1113;// 恶意访问

    }

    /**
     * 在另一台手机登录
     */
    public static final int LOGIN_IN_OTHER_PHONE = 10031;
    /**
     * 恶意访问
     */
    public static final int NOT_LOGIN = 10032;

    /**
     * 异常统一处理
     *
     * @param context
     * @param code    错误码
     * @param msg     错误消息提示
     */
    public static boolean doErrorCode(Context context, int code, String msg) {
        if (code == LOGIN_IN_OTHER_PHONE) {
            DialogUtil.loginInOtherPhoneDialog((Activity) context);
            return false;
        } else if (code == NOT_LOGIN) {
            ToastUtil.toastShow(context, "未登录,请先登录再访问");
            return false;
        }else{
            return true;
        }
    }


}
