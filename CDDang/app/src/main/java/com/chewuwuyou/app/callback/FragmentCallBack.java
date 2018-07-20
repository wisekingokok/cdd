package com.chewuwuyou.app.callback;

/**
 * Fragment与Activity交互
 * 
 * @author Administrator
 *
 */
public interface FragmentCallBack {
	/**
	 * 
	 * @param pager
	 *            标志位
	 * @param obj
	 *            数据
	 */
	void callback(int pager, Object obj);
}
