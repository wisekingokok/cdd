package com.chewuwuyou.app.tools;

/**
 * 
 * 防止Button 快速点击 ， 懂得 应付测试手速快的问题
 * 
 * @author zengys
 * 
 */
public class ZysUtilsBtn {
	private static long lastClickTime;

	public synchronized static boolean isFastClick() {
		long time = System.currentTimeMillis();
		if (time - lastClickTime < 1000) {
			return true;
		}
		lastClickTime = time;
		return false;
	}
}
