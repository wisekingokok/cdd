package com.chewuwuyou.app.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.jpush.android.api.JPushInterface;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:消息设置
 * @author:yuyong
 * @date:2015-8-18下午5:39:57
 * @version:1.2.1
 */
public class MessageSettingActivity extends CDDBaseActivity {
	private TextView mTitleTV;
	private ImageButton mBackIBtn;
	private CheckBox mNotificationCB, mSoundSettingCB, mShakeSettingCB;
	private RelativeLayout mTitleHeight;//标题布局高度
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_setting_ac);
		initView();
		initData();
		initEvent();
	}

	@Override
	protected void initView() {
		mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
		mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mNotificationCB = (CheckBox) findViewById(R.id.notification_setting_check);
		mSoundSettingCB = (CheckBox) findViewById(R.id.sound_setting_check);
		mShakeSettingCB = (CheckBox) findViewById(R.id.shake_setting_check);
	}

	@Override
	protected void initData() {
		mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
		mTitleTV.setText("消息设置");
		if (CacheTools.getUserData("isNotification") == null) {
			CacheTools.setUserData("isNotification", Constant.JPUSH_STATUS.JPUSH_CLOSE);
		}
		if (CacheTools.getUserData("isSound") == null) {
			CacheTools.setUserData("isSound", "1");
		}
		if (CacheTools.getUserData("isShake") == null) {
			CacheTools.setUserData("isShake", "1");
		}
		if (CacheTools.getUserData("isNotification").equals(Constant.JPUSH_STATUS.JPUSH_OPEN)) {
			mNotificationCB.setChecked(true);
		} else {
			mNotificationCB.setChecked(false);
		}
		if (CacheTools.getUserData("isSound").equals("1")
				|| CacheTools.getUserData("isSound") == null) {
			mSoundSettingCB.setChecked(true);
		} else {
			mSoundSettingCB.setChecked(false);
		}

		if (CacheTools.getUserData("isShake").equals("1")
				|| CacheTools.getUserData("isShake") == null) {
			mShakeSettingCB.setChecked(true);
		} else {
			mShakeSettingCB.setChecked(false);
		}

	}

	@Override
	protected void initEvent() {
		mBackIBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finishActivity();
			}
		});

		mNotificationCB
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						if (arg1 == false) {
							JPushInterface.stopPush(getApplicationContext());//停止
							CacheTools.setUserData("isNotification", Constant.JPUSH_STATUS.JPUSH_CLOSE);
						} else {
							JPushInterface.resumePush(getApplicationContext());//恢复
							CacheTools.setUserData("isNotification", Constant.JPUSH_STATUS.JPUSH_OPEN);
						}
					}
				});
		mSoundSettingCB
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						// 缓存开关声音
						if (arg1) {
							CacheTools.setUserData("isSound", "1");
						} else {
							CacheTools.setUserData("isSound", "0");
						}
					}
				});
		mShakeSettingCB
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						// 缓存开关震动
						if (arg1) {
							CacheTools.setUserData("isShake", "1");
						} else {
							CacheTools.setUserData("isShake", "0");
						}
					}
				});
	}

}
