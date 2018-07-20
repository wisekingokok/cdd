package com.chewuwuyou.app.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.chewuwuyou.app.R;

/**
 * @describe:充值完成
 * @author:liuchun
 */
public class ChoiceCompleteActivity extends CDDBaseActivity implements
		OnClickListener {

	private Button mChoiceContinuechoice;
	private ImageView mChoiceImg;// 成功失败图片
	private TextView mTextTV, mChoiceSuccessful, mChoiceDescribe;// 标题，成功或失败标志，，成功或失败描述
	private ImageButton mSubHeaderLeft;// 左边返回

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choice_complete);

		initView();
		initData();
		initEvent();
	}

	/**
	 * 初始化控件
	 */
	@Override
	protected void initView() {
		mChoiceContinuechoice = (Button) findViewById(R.id.choice_continuechoice);
		mTextTV = (TextView) findViewById(R.id.sub_header_bar_tv);
		mSubHeaderLeft = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mChoiceImg = (ImageView) findViewById(R.id.choice_img);
		mChoiceSuccessful = (TextView) findViewById(R.id.choice_successful);
		mChoiceDescribe = (TextView) findViewById(R.id.choice_describe);
	}

	/**
	 * 逻辑处理
	 */
	@Override
	protected void initData() {
		mTextTV.setText("钱包充值");
		if (getIntent().getIntExtra("czResult", 0) == 1) {// 充值成功
			mChoiceImg.setImageResource(R.drawable.top_successful);
			mChoiceSuccessful.setText("充值成功");
			mChoiceDescribe.setText("本次充值金额为："
					+ getIntent().getStringExtra("payMoney"));
		} else {// 充值失败
			mChoiceImg.setImageResource(R.drawable.top_failure);
			mChoiceSuccessful.setText("充值失败");
			mChoiceDescribe.setText("本次充值失败，充值金额将如数返还到您支付账号上");
		}
	}

	/**
	 * 事件监听
	 */
	@Override
	protected void initEvent() {
		mChoiceContinuechoice.setOnClickListener(this);
		mSubHeaderLeft.setOnClickListener(this);
	}

	/**
	 * 金额填写
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.choice_continuechoice:// 返回到填写金额
			ChoicePresentMannerActivity.mChoicePresentMannerActivity
					.finishActivity();
			finishActivity();
			break;
		case R.id.sub_header_bar_left_ibtn:// 返回到我的钱包
			WalletRechargeActivtiy.mRechargeActivtiy.finishActivity();
			ChoicePresentMannerActivity.mChoicePresentMannerActivity
					.finishActivity();
			finishActivity();

			break;

		default:
			break;
		}

	}

}
