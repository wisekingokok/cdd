package com.chewuwuyou.app.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.utils.DialogUtil;

public class BalanceHelpActivity extends CDDBaseActivity implements
		OnClickListener {

	private TextView mTextPhone;
	private TextView mTextTitle;
	private ImageButton mImageBack;
	private RelativeLayout mTitleHeight;//标题布局高度
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.balance_help_layout);
		initView();
		initData();
		initEvent();
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

		mTextPhone = (TextView) findViewById(R.id.text_phone);
		mTextTitle = (TextView) findViewById(R.id.sub_header_bar_tv);
		mImageBack = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);

	}

	@Override
	protected void initData() {
		mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub
		mTextTitle.setText("帮助");
		mImageBack.setOnClickListener(this);
		mTextPhone.setOnClickListener(this);

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

		switch (arg0.getId()) {

		case R.id.sub_header_bar_left_ibtn:
			finish();
			break;

		case R.id.text_phone:
			String tag = arg0.getTag().toString();
			DialogUtil.call(tag, BalanceHelpActivity.this);
			break;

		default:
			break;
		}

	}

}
