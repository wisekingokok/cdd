package com.chewuwuyou.app.widget;

import android.content.Context;
import android.widget.Toast;

/**
 * 解决Toast重复显示问题
 */
public class MyToast {
	private static Context context = null;

	private static Toast toast = null;

	/**
	 * 若context相同,则关闭上次的
	 */

	public static Toast getToast(Context context, String text, int duration) {

		if (MyToast.context == context) {
			toast.cancel();
			toast = Toast.makeText(context, text, duration);
		} else {
			MyToast.context = context;
			toast = Toast.makeText(context, text, duration);
		}

		return toast;

	}

	public static void showToast(Context mContext, String text, int duration) {
		getToast(mContext, text, duration).show();
	}

	public static void showToast(Context mContext, int resId, int duration) {
		showToast(mContext, mContext.getResources().getString(resId), duration);
	}

	public static void cancelToast(Context mContext) {
		if(MyToast.context == mContext){
			if(toast != null)
			toast.cancel();
		}
	}
}
