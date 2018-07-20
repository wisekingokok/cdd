package com.chewuwuyou.app.utils;

import java.math.BigDecimal;
import java.util.Calendar;

import android.widget.TextView;

import com.chewuwuyou.app.R;

/**
 * 关于钱包明细的工具类 1、选择时间 2、选择类型
 *
 * @author yuyong
 */

public class WalletUtil {
    // 1、选择时间 全部 今天 昨天 七天 一个月 三个月
    public static void getStartTime(String des) {

        if (des.equals("全部")) {

        } else if (des.equals("今天")) {

        } else if (des.equals("昨天")) {

        } else if (des.equals("七天")) {

        } else if (des.equals("一个月")) {

        } else if (des.equals("三个月")) {

        }

    }

    /**
     * 获得钱包明细的类型
     */
    public static void getWalletDesTypeNum(String type) {
        if (type.equals("全部")) {

        } else if (type.equals("充值")) {

        } else if (type.equals("提现")) {

        } else if (type.equals("订单收入")) {

        } else if (type.equals("订单付款")) {

        } else if (type.equals("订单退款")) {

        }
    }

    // 订单支付类型加支付方式 订单退款加入退款方式

    // 1，充值；2，提现成功；3，提现失败；
    // 4，订单入账；5，余额支付；6，支付方式支付；
    // 7，余额支付退回余额；8，支付方式支付退回支付方式；
    // 9，支付方式支付退回到余额

    /**
     * 获取文字类型
     *
     * @param typeNum
     * @return
     */
    public static String getWalletDesType(String typeNum) {
        int typeNo = Integer.parseInt(typeNum);
        String type = null;
        switch (typeNo) {
            case Constant.BALANCE_TYPE.RECHARGE:
                type = "充值";
                break;
            case Constant.BALANCE_TYPE.WITH_DRAW_SUCCESS:
                type = "提现成功";
                break;
            case Constant.BALANCE_TYPE.WITH_DRAW_FAILURE:
                type = "提现退款";
                break;
            case Constant.BALANCE_TYPE.ORDER_ENTER:
                type = "订单收入";
                break;
            case Constant.BALANCE_TYPE.BALANCE_PAY:
                type = "订单付款";
                break;
            case Constant.BALANCE_TYPE.PAYMENT_PAY:
                type = "订单付款";
                break;
            case Constant.BALANCE_TYPE.BALANCE_PAY_TUI:
                type = "订单退款";
                break;
            case Constant.BALANCE_TYPE.PAYMENT_PAY_TUI:
                type = "订单退款";
                break;
            case Constant.BALANCE_TYPE.PAYMENT_PAY_TUI_BALANCE:
                type = "订单退款";
                break;
            case Constant.BALANCE_TYPE.WITH_DRAW_APPLY:
                type = "提现";
                break;
            case Constant.BALANCE_TYPE.EXCEPTION_REFUND_RECHARGE:
                type = "异常退款";
                break;
            case Constant.BALANCE_TYPE.REDPACHKETOUT:
                type = "支付红包";
                break;
            case Constant.BALANCE_TYPE.REDPACKETIN:
                type = "领取红包";
                break;
            case Constant.BALANCE_TYPE.REDPACKETTUIKUAI:
                type = "红包退款";
                break;
            default:
                break;
        }
        return type;
    }

    /**
     * 获取金额类型 ，判断"+"或者"-"
     *
     * @param typeNum
     * @return
     */
    public static String getWalletBalanceType(String typeNum) {
        int typeNo = Integer.parseInt(typeNum);
        String balanceType = null;
        switch (typeNo) {
            case Constant.BALANCE_TYPE.RECHARGE:
                balanceType = "+";
                break;
            case Constant.BALANCE_TYPE.WITH_DRAW_SUCCESS:
                balanceType = "-";
                break;
            case Constant.BALANCE_TYPE.WITH_DRAW_FAILURE:
                balanceType = "+";
                break;
            case Constant.BALANCE_TYPE.ORDER_ENTER:
                balanceType = "+";
                break;
            case Constant.BALANCE_TYPE.BALANCE_PAY:
                balanceType = "-";
                break;
            case Constant.BALANCE_TYPE.PAYMENT_PAY:
                balanceType = "-";
                break;
            case Constant.BALANCE_TYPE.BALANCE_PAY_TUI:
                balanceType = "+";
                break;
            case Constant.BALANCE_TYPE.PAYMENT_PAY_TUI:
                balanceType = "+";
                break;
            case Constant.BALANCE_TYPE.PAYMENT_PAY_TUI_BALANCE:
                balanceType = "+";
                break;
            case Constant.BALANCE_TYPE.WITH_DRAW_APPLY:
                balanceType = "-";
                break;
            case Constant.BALANCE_TYPE.EXCEPTION_REFUND_RECHARGE:
                balanceType = "+";
                break;

            default:
                break;
        }
        return balanceType;

    }

    /**
     * 头像图片类型
     *
     * @param typeNum
     * @return
     */
    public static int getWalletIconType(String typeNum) {
        int typeNo = Integer.parseInt(typeNum);
        int mImageIcon = 0;
        switch (typeNo) {
            case Constant.BALANCE_TYPE.RECHARGE:
                mImageIcon = R.drawable.user_fang_icon;
                break;
            case Constant.BALANCE_TYPE.WITH_DRAW_SUCCESS:
                mImageIcon = R.drawable.zhifubao;
                break;
            case Constant.BALANCE_TYPE.WITH_DRAW_FAILURE:
                mImageIcon = R.drawable.ic_luncher;
                break;
            case Constant.BALANCE_TYPE.ORDER_ENTER:
                mImageIcon = R.drawable.user_fang_icon;
                break;
            case Constant.BALANCE_TYPE.BALANCE_PAY:
                mImageIcon = R.drawable.user_fang_icon;
                break;
            case Constant.BALANCE_TYPE.PAYMENT_PAY:
                mImageIcon = R.drawable.user_fang_icon;
                break;
            case Constant.BALANCE_TYPE.BALANCE_PAY_TUI:
                mImageIcon = R.drawable.user_fang_icon;
                break;
            case Constant.BALANCE_TYPE.PAYMENT_PAY_TUI:
                mImageIcon = R.drawable.user_fang_icon;
                break;
            case Constant.BALANCE_TYPE.PAYMENT_PAY_TUI_BALANCE:
                mImageIcon = R.drawable.user_fang_icon;
                break;
            case Constant.BALANCE_TYPE.WITH_DRAW_APPLY:
                mImageIcon = R.drawable.zhifubao;
            default:
                break;
        }
        return mImageIcon;

    }

    /**
     * 账单详情类型
     *
     * @param typeNum
     * @return
     */
    public static String getWalletDetailsType(String typeNum) {
        int typeNo = Integer.parseInt(typeNum);
        String detailsType = null;
        switch (typeNo) {
            case Constant.BALANCE_TYPE.RECHARGE:
                detailsType = "充值成功";
                break;
            case Constant.BALANCE_TYPE.WITH_DRAW_SUCCESS:
                detailsType = "提现成功";
                break;
            case Constant.BALANCE_TYPE.WITH_DRAW_FAILURE:
                detailsType = "退款成功";
                break;
            case Constant.BALANCE_TYPE.ORDER_ENTER:
                detailsType = "交易成功";
                break;
            case Constant.BALANCE_TYPE.BALANCE_PAY:
                detailsType = "支付成功";
                break;
            case Constant.BALANCE_TYPE.PAYMENT_PAY:
                detailsType = "支付成功";
                break;
            case Constant.BALANCE_TYPE.BALANCE_PAY_TUI:
                detailsType = "退款成功";
                break;
            case Constant.BALANCE_TYPE.PAYMENT_PAY_TUI:
                detailsType = "退款成功";
                break;
            case Constant.BALANCE_TYPE.PAYMENT_PAY_TUI_BALANCE:
                detailsType = "退款成功";
                break;
            case Constant.BALANCE_TYPE.WITH_DRAW_APPLY:
                detailsType = "提现申请成功";
            default:
                break;
        }
        return detailsType;

    }

    /**
     * 获取阶段日期
     *
     * @param dateType 1:一天前 2:1月前3:
     * @author Yangtse
     */
    // 使用方法 char datetype = '7';
    public static String getStartDate(char dateType) {
        Calendar c = Calendar.getInstance(); // 当时的日期和时间
        int hour; // 需要更改的小时
        int day; // 需要更改的天数
        switch (dateType) {
            case '0': // 1小时前
                hour = c.get(Calendar.HOUR_OF_DAY) - 1;
                c.set(Calendar.HOUR_OF_DAY, hour);
                // System.out.println(df.format(c.getTime()));
                break;
            case '1': // 2小时前
                hour = c.get(Calendar.HOUR_OF_DAY) - 2;
                c.set(Calendar.HOUR_OF_DAY, hour);
                // System.out.println(df.format(c.getTime()));
                break;
            case '2': // 3小时前
                hour = c.get(Calendar.HOUR_OF_DAY) - 3;
                c.set(Calendar.HOUR_OF_DAY, hour);
                // System.out.println(df.format(c.getTime()));
                break;
            case '3': // 6小时前
                hour = c.get(Calendar.HOUR_OF_DAY) - 6;
                c.set(Calendar.HOUR_OF_DAY, hour);
                // System.out.println(df.format(c.getTime()));
                break;
            case '4': // 12小时前
                hour = c.get(Calendar.HOUR_OF_DAY) - 12;
                c.set(Calendar.HOUR_OF_DAY, hour);
                // System.out.println(df.format(c.getTime()));
                break;
            case '5': // 一天前
                day = c.get(Calendar.DAY_OF_MONTH) - 1;
                c.set(Calendar.DAY_OF_MONTH, day);
                // System.out.println(df.format(c.getTime()));
                break;
            case '6': // 一星期前
                day = c.get(Calendar.DAY_OF_MONTH) - 7;
                c.set(Calendar.DAY_OF_MONTH, day);
                // System.out.println(df.format(c.getTime()));
                break;
            case '7': // 一个月前
                day = c.get(Calendar.DAY_OF_MONTH) - 30;
                c.set(Calendar.DAY_OF_MONTH, day);
                // System.out.println(df.format(c.getTime()));
                break;
            case '8':// 三个月前
                day = c.get(Calendar.DAY_OF_MONTH) - 90;
                c.set(Calendar.DAY_OF_MONTH, day);
                break;
        }
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        StringBuilder strForwardDate = new StringBuilder("");
        strForwardDate.append(mYear).append("-");
        if (mMonth + 1 < 10) {
            strForwardDate.append("0").append(mMonth + 1);
        } else {
            strForwardDate.append(mMonth + 1);
        }
        strForwardDate.append("-");
        if (mDay < 10) {
            strForwardDate.append("0").append(mDay);
        } else {
            strForwardDate.append(mDay);
        }
        return strForwardDate.toString();
        // return c.getTimeInMillis();
    }

    /**
     * 显示账户 包含@符号 邮箱显示开头2个字符+@邮箱地址，如ss******@foxmail.com
     * 手机显示开头3个数字+末尾4个数字，如：132****1234
     */
    public static void showAccount(String accountNo, TextView showTV) {
        if (accountNo.contains("@")) {
            if (accountNo.split("@")[0].length() <= 2) {
                showTV.setText(accountNo.split("@")[0] + "******@"
                        + accountNo.split("@")[1]);
            } else {
                showTV.setText(accountNo.split("@")[0].substring(0, 2)
                        + "******@" + accountNo.split("@")[1]);
            }
        } else if (accountNo.length() >= 7) {
            showTV.setText(accountNo.substring(0, 3)
                    + "*****"
                    + accountNo.substring(accountNo.length() - 4,
                    accountNo.length()));
        } else {
            showTV.setText(accountNo);
        }

    }

    /**
     * 获取手续费
     *
     * @param sxf
     * @return BigDecimal.ROUND_DOWN;//直接删除多余的小数位
     * BigDecimal.ROUND_UP;//临近位非零，则直接进位；临近位为零，不进位。 这里的费率 要求如下：
     * 只截取到小数点后三位， 第四位以后小数直接舍去。 而第三位小数的要求是临近位非零，则直接进位；临近位为零，不进位。
     */
    public static Double getSxf(Double sxf) {
        // double f1 = (sxf * 1000) / 1000;
        // BigDecimal a = new BigDecimal(f1+"");
        // Double f2 = a.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
        // return f2;
        float f1 = Float.parseFloat(sxf + "");
        int num = (int) (f1 * 1000);
        if (num % 10 > 0)
            f1 = (num - num % 10 + 10 * 1.0f) / 1000.0f;
        else
            f1 = num * 1.0f / 1000.0f;
        return Double.parseDouble(f1 + "");

    }

    public static Double SXF(Double sxf) {
        BigDecimal b = new BigDecimal(sxf);
        Double f1 = b.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
        return f1;
    }

    /**
     * 将每三个数字加上逗号处理（通常使用金额方面的编辑）
     *
     * @param str 无逗号的数字
     * @return 加上逗号的数字
     */
    public static String addComma(String str) {
        if (str.contains(".")) {
            String[] strs = str.split("\\.");

            // 将传进数字反转
            String reverseStr = new StringBuilder(strs[0]).reverse().toString();
            String strTemp = "";

            for (int i = 0; i < reverseStr.length(); i++) {
                if (i * 3 + 3 > reverseStr.length()) {
                    strTemp += reverseStr.substring(i * 3, reverseStr.length());
                    break;
                }
                strTemp += reverseStr.substring(i * 3, i * 3 + 3) + ",";
            }
            // 将 【789,456,】 中最后一个【,】去除
            if (strTemp.endsWith(",")) {
                strTemp = strTemp.substring(0, strTemp.length() - 1);
            }

            // 将数字重新反转
            String resultStr = new StringBuilder(strTemp).reverse().toString();
            return resultStr + "." + strs[1];

        } else {
            // 将传进数字反转
            String reverseStr = new StringBuilder(str).reverse().toString();
            String strTemp = "";

            for (int i = 0; i < reverseStr.length(); i++) {
                if (i * 3 + 3 > reverseStr.length()) {
                    strTemp += reverseStr.substring(i * 3, reverseStr.length());
                    break;
                }
                strTemp += reverseStr.substring(i * 3, i * 3 + 3) + ",";
            }
            // 将 【789,456,】 中最后一个【,】去除
            if (strTemp.endsWith(",")) {
                strTemp = strTemp.substring(0, strTemp.length() - 1);
            }
            // 将数字重新反转
            String resultStr = new StringBuilder(strTemp).reverse().toString();
            return resultStr;

        }

    }

}
