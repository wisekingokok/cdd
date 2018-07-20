package com.chewuwuyou.app.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * 
 * @author zengys 过滤表情
 */

public class EditInputBiaoQing implements InputFilter {

	Pattern emoji;

	public EditInputBiaoQing() {
		emoji = Pattern
				.compile(
						"[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
						Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
	}

	@Override
	public CharSequence filter(CharSequence arg0, int arg1, int arg2,
			Spanned arg3, int arg4, int arg5) {
		// TODO Auto-generated method stub

		Matcher emojiMatcher = emoji.matcher(arg0);

		if (emojiMatcher.find()) {

			return "";

		}
		return null;
	}

}
