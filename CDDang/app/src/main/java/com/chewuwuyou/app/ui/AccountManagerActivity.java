package com.chewuwuyou.app.ui;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.chewuwuyou.app.R;

/**
 * @describe:账户管理
 * @author:yuyong
 * @version 1.1.0
 * @created:2014-11-7下午5:21:24
 */
public class AccountManagerActivity extends BaseActivity {

	@ViewInject(id = R.id.sub_header_bar_left_ibtn, click = "onAction")
	private ImageButton mBackBtn;
	@ViewInject(id = R.id.sub_header_bar_tv)
	private TextView mHeaderTV;
	@ViewInject(id = R.id.password_manager_layout, click = "onAction")
	private LinearLayout mPasswordManagerLayout;// 密码管理
	@ViewInject(id = R.id.phone_bind_layout, click = "onAction")
	private LinearLayout mPhoneBindLayout;// 手机绑定
	@ViewInject(id = R.id.bind_phone)
	TextView mBindPhone;// 已绑定手机

	private RelativeLayout mTitleHeight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_manager_ac);
		initView();// 初始化方法
	}

	/**
	 * 初始化控件
	 */
	public void initView() {
		mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);// 根据不同手机判断
		mHeaderTV.setText(R.string.account_manager_ac);
	}

	public void onAction(View v) {

		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			finishActivity();
			break;
		case R.id.password_manager_layout:// 密码管理
			StatService.onEvent(AccountManagerActivity.this,
					"clickForgetPassword", "点击忘记密码");
			Intent forgetPasswordIntent = new Intent(
					AccountManagerActivity.this, ForgetPasswordActivity.class);
			startActivity(forgetPasswordIntent);
			finishActivity();
			break;

		case R.id.phone_bind_layout:// 找回 密码
			// 不能进行手机绑定的修改 改为找回密码
			StatService.onEvent(AccountManagerActivity.this,
					"clickResetPassword", "点击重置密码");
			Intent intent = new Intent(AccountManagerActivity.this,
					UserPasswordManagerActivity.class);
			startActivity(intent);

			break;
		default:
			break;
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPause(AccountManagerActivity.this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(AccountManagerActivity.this);
	}
}
