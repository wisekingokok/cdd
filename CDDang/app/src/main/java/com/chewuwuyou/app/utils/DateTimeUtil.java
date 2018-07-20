package com.chewuwuyou.app.utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import android.content.Context;
import android.text.TextUtils;

/**
 * @author ：caixuemei
 * @describe：获取时间和日期工具类
 * @created ：2014-5-20下午3:30:59
 */
public class DateTimeUtil {

    private static String mYear;
    private static String mMonth;
    private static String mDay;
    private static String mWay;
    private static final int YEAR = 365 * 24 * 60 * 60;// 年
    private static final int MONTH = 30 * 24 * 60 * 60;// 月
    private static final int DAY = 24 * 60 * 60;// 天
    private static final int HOUR = 60 * 60;// 小时
    private static final int MINUTE = 60;// 分钟

    // 添加大小月月份并将其转换为list,方便之后的判断
    private static String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
    private static String[] months_little = {"4", "6", "9", "11"};

    @SuppressWarnings("rawtypes")
    private static List list_big = Arrays.asList(months_big);
    @SuppressWarnings("rawtypes")
    private static List list_little = Arrays.asList(months_little);

    public static String StringData() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = "天";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return mYear + "年" + mMonth + "月" + mDay + "日" + "/星期" + mWay;
    }

    public static String StringData(Date date) {
        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码

        return mYear + "年" + mMonth + "月" + mDay + "日";
    }

    /**
     * 获取系统当前星期
     *
     * @return
     */
    public static String getWeek() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        int mWay = c.get(Calendar.DAY_OF_WEEK);
        if (mWay == 1) {
            return "星期天";
        } else if (mWay == 2) {
            return "星期一";
        } else if (mWay == 3) {
            return "星期二";
        } else if (mWay == 4) {
            return "星期三";
        } else if (mWay == 5) {
            return "星期四";
        } else if (mWay == 6) {
            return "星期五";
        } else {
            return "星期六";
        }

    }

    public static Date stringToDate(String dateString) {
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateValue = simpleDateFormat.parse(dateString, position);
        return dateValue;
    }

    /**
     * 获取系统当前时间
     *
     * @return
     */
    public static String getUpdateDatetime() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * 获取系统当前年月日
     *
     * @return
     */
    public static String getSystemTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * 获取系统当前月份和日期
     *
     * @return
     */
    public static String getTime() {

        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd ");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * 获取时间差 Date startDate = new Date(publishtime);//publishtime为
     * "E MMMM dd hh:mm:ss z yyyy"这种格式， Date nowDate =
     * Calendar.getInstance().getTime(); 就可以得到与微博一样精细的时间差。
     *
     * @param key
     * @return
     */
    public static String twoDateDistance(String key) {
        Date startDate;
        if (CacheTools.getUserLoginData(key) != null) {
            startDate = stringToDate(CacheTools.getUserLoginData(key));
        } else {
            startDate = new Date(System.currentTimeMillis());
        }
        Date endDate = new Date(System.currentTimeMillis());
        if (startDate == null || endDate == null) {
            return null;
        }
        long timeLong = endDate.getTime() - startDate.getTime();
        if (timeLong < 60 * 1000)
            return "刚刚";
        else if (timeLong < 60 * 60 * 1000) {
            timeLong = timeLong / 1000 / 60;
            return timeLong + "分钟前";
        } else if (timeLong < 60 * 60 * 24 * 1000) {
            timeLong = timeLong / 60 / 60 / 1000;
            return timeLong + "小时前";
        } else if (timeLong < 60 * 60 * 24 * 1000 * 7) {
            timeLong = timeLong / 1000 / 60 / 60 / 24;
            return timeLong + "天前";
        } else if (timeLong < 60 * 60 * 24 * 1000 * 7 * 4) {
            timeLong = timeLong / 1000 / 60 / 60 / 24 / 7;
            return timeLong + "周前";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
            return sdf.format(startDate);
        }
    }

    /**
     * 比较选择时间与系统当前时间的先后
     *
     * @param startDate
     * @param endDate
     * @return true:选择时间大于系统当前时间
     * @return false:选择时间小于当前时间
     */
    // public static boolean compareTime(String choiceDate) {
    // SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    // Date curDate = new Date(System.currentTimeMillis());
    // String str = formatter.format(curDate);
    // // 系统时间
    // Long currentTime = Long.parseLong(str);
    // Date choicDate = new Date(choiceDate);
    // String choiceT = formatter.format(choicDate);
    // // 选择时间
    // Long choiceTime = Long.parseLong(choiceT);
    // if (choiceDate != null) {
    //
    // if (choiceTime >= currentTime) {
    // return true;
    // }
    // }
    //
    // return false;
    // }

    /**
     * 日期相减的方法
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int getDateDays(String date1, String date2) {
        date1 = date1.split(" ")[0];
        date2 = date2.split(" ")[0];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int days = 0;
        try {
            Date date = sdf.parse(date1);
            Date dateBegin = sdf.parse(date2);
            long betweenTime = date.getTime() - dateBegin.getTime();
            days = (int) (betweenTime / 1000 / 60 / 60 / 24);
        } catch (Exception e) {
        }
        return days;
    }

    /**
     * 判断时间先后
     *
     * @param choiceTime
     * @return false 选择时间小于当前时间
     */
    public static boolean compareTime(String choiceTime) {

        choiceTime = choiceTime.split(" ")[0];
        String systemTime = getSystemTime();
        int days = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date choiceT = sdf.parse(choiceTime);
            Date systemdate = sdf.parse(systemTime);
            long betweenTime = choiceT.getTime() - systemdate.getTime();
            days = (int) (betweenTime / 1000 / 60 / 60 / 24);

            if (days > 0) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 判断时间先后
     *
     * @param choiceTime
     * @return false 选择时间小于当前时间
     */
    public static boolean compareTime1(String choiceTime, Context mContext) {

        choiceTime = choiceTime.split(" ")[0];
        String systemTime = getSystemTime();
        int days = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date choiceT = sdf.parse(choiceTime);
            Date systemdate = sdf.parse(systemTime);
            long betweenTime = choiceT.getTime() - systemdate.getTime();
            days = (int) (betweenTime / 1000 / 60 / 60 / 24);
            if (days > 0) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 时间间隔为多少年
     *
     * @param choiceTime
     * @return false选择时间与当前时间间隔大于15年
     */
    public static boolean compareTimeYears(String choiceTime) {
        choiceTime = choiceTime.split(" ")[0];
        String systemTime = getSystemTime();
        // int years = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        try {
            Date choicedate = sdf.parse(choiceTime);
            Date systemdate = sdf.parse(systemTime);
            long betweenYear = systemdate.getTime() - choicedate.getTime();
            int s = Integer.parseInt(String.valueOf(betweenYear / 1000 / 60 / 60 / 24 / 365));
            if (s <= 15) {
                return true;
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return false;

    }

    public static String lastDate(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return null;
        }
        long timeLong = endDate.getTime() - startDate.getTime();
        if (timeLong < 60 * 1000) {// 在一分钟以内
            return "剩余" + timeLong + "秒";
        } else if (timeLong < 60 * 60 * 1000) {// 在一小时以内
            timeLong = timeLong / 1000 / 60;
            return "剩余" + timeLong + "分钟";
        } else if (timeLong < 60 * 60 * 24 * 1000) {// 在一天以内
            timeLong = timeLong / 60 / 60 / 1000;
            return "剩余" + timeLong + "小时";
        } else {
            timeLong = timeLong / 1000 / 60 / 60 / 24;// 其它
            return "剩余" + timeLong + "天";
        }
    }

    public static String beforeDate(Date startDate, Date endDate) {
        long timeLong = endDate.getTime() - startDate.getTime();
        if (timeLong < 60 * 1000)
            return "刚刚";
        else if (timeLong < 60 * 60 * 1000) {
            timeLong = timeLong / 1000 / 60;
            return timeLong + "分钟前";
        } else if (timeLong < 60 * 60 * 24 * 1000) {
            timeLong = timeLong / 60 / 60 / 1000;
            return timeLong + "小时前";
        } else if (timeLong < 60 * 60 * 24 * 1000 * 7) {
            timeLong = timeLong / 1000 / 60 / 60 / 24;
            return timeLong + "天前";
        } else if (timeLong < 60 * 60 * 24 * 1000 * 7 * 4) {
            timeLong = timeLong / 1000 / 60 / 60 / 24 / 7;
            return timeLong + "周前";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
            return sdf.format(startDate);
        }
    }

    /**
     * 将秒转成分秒
     *
     * @return
     */
    public static String getVoiceRecorderTime(int time) {
        int minute = time / 60;
        int second = time % 60;
        if (minute == 0) {
            return String.valueOf(second);
        }
        return minute + ":" + second;

    }

    public static String showChatTime(Date mDate) {
        getUpdateDatetime();

        return "";
    }

    /**
     * 根据时间戳获取描述性时间，如3分钟前，1天前
     *
     * @param timestamp 时间戳 单位为毫秒
     * @return 时间字符串
     */
    public static String getDescriptionTimeFromTimestamp(String timestamp) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
        Date date = null;
        try {
            date = formatter.parse(timestamp);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        long currentTime = System.currentTimeMillis();
        long timeGap = (currentTime - date.getTime()) / 1000;// 与现在时间相差秒数
        System.out.println("timeGap: " + timeGap);
        String timeStr = null;
        if (timeGap > YEAR) {
            timeStr = timeGap / YEAR + "年前";
        } else if (timeGap > MONTH) {
            timeStr = timeGap / MONTH + "个月前";
        } else if (timeGap > DAY) {// 1天以上
            timeStr = timeGap / DAY + "天前";
        } else if (timeGap > HOUR) {// 1小时-24小时
            timeStr = timeGap / HOUR + "小时前";
        } else if (timeGap > MINUTE) {// 1分钟-59分钟
            timeStr = timeGap / MINUTE + "分钟前";
        } else {// 1秒钟-59秒钟
            timeStr = "刚刚";
        }
        return timeStr;
    }

    public static String getTime1(long timestamp) {
        String timestr = null;
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:MM");
        Date curDate = new Date(timestamp);
        timestr = formatter.format(curDate);
        return timestr;
    }

    // 获得明天日期
    public static String getTomoData() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        if (day == 30) {
            if (list_big.contains(String.valueOf(month))) {
                day = 31;
            }
            if (list_little.contains(String.valueOf(month))) {
                day = 1;
                if (month == 12) {
                    year++;
                    month = 1;
                } else {
                    month++;
                }

            }
        } else if (day == 31) {
            day = 1;
            if (month == 12) {
                year++;
                month = 1;
            } else {
                month++;
            }

        } else {
            day++;
        }
        String data = year + "-" + month + "-" + day;
        return data;
    }

    /**
     * 返回转换后的格式
     *
     * @param data
     * @return
     */
    public static String parseDateFormat(String data) {
        String str = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(data);
            str = sdf.format(date);
            System.out.println(str);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 判断时间先后
     *
     * @param choiceTime
     * @return false 选择时间小于当前时间
     */
    public static boolean compareData(String choiceTime) {

        String systemTime = getUpdateDatetime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {

            if(!TextUtils.isEmpty(choiceTime)){
                Date choiceT = sdf.parse(choiceTime);
                Date systemdate = sdf.parse(systemTime);
                System.out.println(choiceTime + " = " + systemTime);
                if (choiceT.getTime() >= systemdate.getTime()) {
                    return true;
                } else {
                    return false;
                }
            }

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 获取昨天的日期
     *
     * @return
     */
    public static String getYesterDayDate() {
        final Calendar c = Calendar.getInstance();
        mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH) - 1);// 获取当前月份的日期号码
        return mYear + "年" + mMonth + "月" + mDay + "日";
    }


    /**
     * 时间字符串格式
     *
     * @param dateString
     * @return
     */
    public static Date stringToDate1(String dateString) {
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateValue = simpleDateFormat.parse(dateString, position);
        return dateValue;
    }
}
