package com.chewuwuyou.app.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.chewuwuyou.app.R;

import java.util.List;

public class KeyboardUtil {
	private Context ctx;
	private KeyboardView mKeyboardView;
	private Keyboard k2;// 数字键盘
	public boolean isnun = true;// 是否数据键盘

	// private TextView ed;
	InputFinishListener inputOver;
	TextView[] textViews = new TextView[6];
	LinearLayout layout_parent;

	public KeyboardUtil(Activity act, Context ctx, KeyboardView keyboardView,
						LinearLayout layout_parent, InputFinishListener inputOver) {
		this.ctx = ctx;
		this.layout_parent = layout_parent;
		initTextViews();

		k2 = new Keyboard(ctx, R.xml.symbols_num);
		mKeyboardView = keyboardView;
		keyboardView.setEnabled(true);
		keyboardView.setKeyboard(k2);
		keyboardView.setPreviewEnabled(true);
		keyboardView.setOnKeyboardActionListener(listener);
		this.inputOver = inputOver;

		List<Key> keys = keyboardView.getKeyboard().getKeys();
		for (Key key : keys) {
			if (key.codes[0] == Keyboard.KEYCODE_MODE_CHANGE
					|| key.codes[0] == Keyboard.KEYCODE_DELETE) {
				key.onPressed();
			}
		}
	}

	/**
	 * @description:初始化输入框
	 * @user wzl email:wzl200711402@163.com
	 * @date 2014-3-19
	 */
	private void initTextViews() {
		for (int i = 0; i < textViews.length; i++) {
			textViews[i] = new TextView(ctx);
			LinearLayout.LayoutParams params = new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			params.weight = 1;
			textViews[i].setLayoutParams(params);
			layout_parent.addView(textViews[i]);
			textViews[i].setTransformationMethod(PasswordTransformationMethod
					.getInstance());
			textViews[i].setGravity(Gravity.CENTER);
			textViews[i].setTextSize(30);
			if (i < textViews.length - 1) {
				View view = new View(ctx);
				LinearLayout.LayoutParams viewParams = new LayoutParams(
						(int) ctx.getResources().getDimension(
								R.dimen.width_input_tv_cutline),
						LayoutParams.MATCH_PARENT);
				view.setLayoutParams(viewParams);
				view.setBackgroundColor(Color.parseColor("#C8C8C8"));
				layout_parent.addView(view);
			}

		}
	}

	private OnKeyboardActionListener listener = new OnKeyboardActionListener() {
		@Override
		public void swipeUp() {
		}

		@Override
		public void swipeRight() {
		}

		@Override
		public void swipeLeft() {
		}

		@Override
		public void swipeDown() {
		}

		@Override
		public void onText(CharSequence text) {
		}

		@Override
		public void onRelease(int primaryCode) {
		}

		@Override
		public void onPress(int primaryCode) {
		}

		@Override
		public void onKey(int primaryCode, int[] keyCodes) {
			if (primaryCode == Keyboard.KEYCODE_DELETE) {
				deleteTextView();
			} else if (primaryCode != Keyboard.KEYCODE_MODE_CHANGE) {
				inputTextView(Character.toString((char) primaryCode));
			}

		}

	};

	public void showKeyboard() {
		int visibility = mKeyboardView.getVisibility();
		if (visibility == View.GONE || visibility == View.INVISIBLE) {
			mKeyboardView.setVisibility(View.VISIBLE);
		}
	}

	public void hideKeyboard() {
		int visibility = mKeyboardView.getVisibility();
		if (visibility == View.VISIBLE) {
			mKeyboardView.setVisibility(View.INVISIBLE);
		}
	}

	// private boolean isword(String str) {
	// String wordstr = "abcdefghijklmnopqrstuvwxyz";
	// if (wordstr.indexOf(str.toLowerCase()) > -1) {
	// return true;
	// }
	// return false;
	// }

	private void inputTextView(String code) {
		for (int i = 0; i < textViews.length; i++) {
			TextView tv = textViews[i];
			if (tv.getText().toString().equals("")) {
				tv.setText(code);
				if (i == textViews.length - 1) {
					inputOver.inputHasOver(getInputText());
				}
				return;
			}
		}
	}

	private void deleteTextView() {
		for (int i = textViews.length - 1; i >= 0; i--) {
			TextView tv = textViews[i];
			if (!tv.getText().toString().equals("")) {
				tv.setText("");
				return;
			}
		}
	}

	private String getInputText() {
		StringBuffer sb = new StringBuffer();
		for (TextView tv : textViews) {
			sb.append(tv.getText().toString());
		}
		return sb.toString();
	}

	/**
	 * 输入监听
	 * 
	 * @author yuyong
	 * 
	 */
	public interface InputFinishListener {
		public void inputHasOver(String text);
	}

	/**
	 * 清空文本的方法
	 */
	public void clearText() {
		for (int i = textViews.length - 1; i >= 0; i--) {
			TextView tv = textViews[i];
			tv.setText("");
		}
	}

}