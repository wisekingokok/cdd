package com.chewuwuyou.app.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.widget.EditText;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:监听短信验证码的接收并自动填充
 * @author:yuyong
 * @date:2015-12-23上午10:22:34
 * @version:1.2.1
 */
public class SmsContent extends ContentObserver {

	public static final String SMS_URI_INBOX = "content://sms/inbox";
	private Activity activity = null;
	private String smsContent = "";
	private EditText verifyText = null;

	public SmsContent(Activity activity, Handler handler, EditText verifyText) {
		super(handler);
		this.activity = activity;
		this.verifyText = verifyText;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		Cursor cursor = null;// 光标
		// 读取收件箱中指定号码的短信
//		 cursor = activity.managedQuery(Uri.parse(SMS_URI_INBOX), new String[]
//		 {"_id", "address", "body", "read" }, "address=? and read=?",
//		 new String[] { "10690570574123", "0" }, "date desc");
		 cursor = activity.managedQuery(Uri.parse(SMS_URI_INBOX), new String[] {
				"_id", "address", "body", "read" }, "read=?",
				new String[] { "0" }, "date desc");
		if (cursor != null) {// 如果短信为未读模式
			cursor.moveToFirst();
			if (cursor.moveToFirst()) {
				String smsbody = cursor
						.getString(cursor.getColumnIndex("body"));
				MyLog.i("YUY", "短信内容= "+smsbody);
				System.out.println("smsbody=======================" + smsbody);
				if (smsbody.contains("车当当") || smsbody.contains("车务无忧")) {
					String regEx = "[^0-9]";
					Pattern p = Pattern.compile(regEx);
					Matcher m = p.matcher(smsbody.toString());
					smsContent = m.replaceAll("").trim().toString();
					verifyText.setText(smsContent);
				}

			}
		}
	}
}