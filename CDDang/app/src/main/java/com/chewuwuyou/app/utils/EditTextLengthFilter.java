package com.chewuwuyou.app.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;

import com.chewuwuyou.app.R;

public class EditTextLengthFilter implements InputFilter {
	int MAX_EN = 0;
	int MIN_EN = 0;
	String regEx = "[\\u4e00-\\u9fa5]";
	Context context;

	public EditTextLengthFilter(Context context, int mAX_EN, int mIN_EN) {
		super();
		this.context = context;
		MAX_EN = mAX_EN;
		MIN_EN = mIN_EN;
	}
	
	public EditTextLengthFilter(Context context, int mAX_EN) {
		super();
		this.context = context;
		MAX_EN = mAX_EN;
	}

	@Override
	public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
		int destCount = dest.toString().length() + getChineseCount(dest.toString());
		int sourceCount = source.toString().length() + getChineseCount(source.toString());
		if (destCount + sourceCount > MAX_EN) {
			ToastUtil.toastShow(context, context.getResources().getString(R.string.warning_of_max_count, String.valueOf(MAX_EN)));
			return "";

		} else if (destCount + sourceCount < MIN_EN) {
			ToastUtil.toastShow(context, context.getResources().getString(R.string.warning_of_min_count, String.valueOf(MIN_EN)));
			return "";
		} else {
			return source;
		}
	}

	private int getChineseCount(String str) {
		int count = 0;
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		while (m.find()) {
			for (int i = 0; i <= m.groupCount(); i++) {
				count = count + 1;
			}
		}
		return count;
	}
}