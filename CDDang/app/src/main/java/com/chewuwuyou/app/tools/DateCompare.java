package com.chewuwuyou.app.tools;

import android.annotation.SuppressLint;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.chewuwuyou.app.utils.MyLog;

/**
 * 日期比较
 * 
 * @author zengys
 * 
 */
public class DateCompare {

	public static void main(String args[]) {
		int i = compare_date("1995-11-12 15:21", "1999-12-11 09:59");
		System.out.println("i==" + i);
	}

	/**
	 * 传值两个日期进行比较
	 * 
	 * @param DATE1
	 * @param DATE2
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static int compare_date(String DATE1, String DATE2) {

		DateFormat df = new SimpleDateFormat("yyyy-M-d");
		try {
			Date dt1 = df.parse(DATE1);
			Date dt2 = df.parse(DATE2);
			MyLog.i("yang", "dt1" + dt1);
			MyLog.i("yang", "dt2" + dt2);
			if (dt1.getTime() >= dt2.getTime()) {
				System.out.println("dt1 在dt2前");
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				System.out.println("dt1在dt2后");
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获取当前系统日期
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getPhoneDate() {

		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-M-d");
		String date = sDateFormat.format(new java.util.Date());
		System.out.println(date);
		return date;
	}
}
