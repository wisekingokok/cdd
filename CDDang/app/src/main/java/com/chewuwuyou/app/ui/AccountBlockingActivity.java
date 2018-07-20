package com.chewuwuyou.app.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;

/**
 * 账户锁定
 * 
 * @author yuyong
 * 
 */
public class AccountBlockingActivity extends CDDBaseActivity implements
		OnClickListener {

	private TextView mTitleTV;
	private ImageButton mBackIbtn;

	private Button mOkBtn;// 知道了
	private RelativeLayout mTitleHeight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_blocking_ac);
		initView();
		initData();
		initEvent();

	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
		mBackIbtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mTitleTV.setText("输入密码");
		mOkBtn = (Button) findViewById(R.id.ok_btn);
	}

	@Override
	protected void initData() {
		mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断

	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub
		mBackIbtn.setOnClickListener(this);
		mOkBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			finishActivity();
			break;
		case R.id.ok_btn:
			finishActivity();
			break;
		default:
			break;
		}
	}

}
