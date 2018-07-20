package com.chewuwuyou.app.utils;

import android.widget.ImageView;

import com.chewuwuyou.app.R;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:录音的工具类
 * @author:yuyong
 * @date:2015-5-8下午1:53:06
 * @version:1.2.1
 */
public class VoiceUtils {

	public static void setVoiceImage(double amp, ImageView mVoiceIV) {
		switch ((int) amp) {
		case 0:
			mVoiceIV.setImageResource(R.drawable.record_animate_01);
			break;
		case 1:
			mVoiceIV.setImageResource(R.drawable.record_animate_02);
			break;
		case 2:
			mVoiceIV.setImageResource(R.drawable.record_animate_03);
			break;
		case 3:
			mVoiceIV.setImageResource(R.drawable.record_animate_04);
			break;
		case 4:
			mVoiceIV.setImageResource(R.drawable.record_animate_05);
			break;
		case 5:
			mVoiceIV.setImageResource(R.drawable.record_animate_06);
			break;
		case 6:
			mVoiceIV.setImageResource(R.drawable.record_animate_07);
			break;
		case 7:
			mVoiceIV.setImageResource(R.drawable.record_animate_08);
			break;
		case 8:
			mVoiceIV.setImageResource(R.drawable.record_animate_09);
			break;
		case 9:
			mVoiceIV.setImageResource(R.drawable.record_animate_10);
			break;
		case 10:
			mVoiceIV.setImageResource(R.drawable.record_animate_11);
			break;
		case 11:
			mVoiceIV.setImageResource(R.drawable.record_animate_12);
			break;
		default:
			mVoiceIV.setImageResource(R.drawable.record_animate_14);
			break;
		}
	}

}
