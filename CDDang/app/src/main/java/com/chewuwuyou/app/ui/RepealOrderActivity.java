package com.chewuwuyou.app.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;

/**
 * 撤销订单
 * 
 * @author zengys
 * 
 */
public class RepealOrderActivity extends CDDBaseActivity implements
		OnClickListener {

	private TextView mTextTitle;
	private EditText mEditRemark;
	private TextView mTextLength;
	private ImageButton mImageBack;
	private RelativeLayout mTitleHeight;//标题布局高度
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.repeal_order_layout);
		initView();
		initEvent();
		initData();

	}

	@Override
	protected void initView() {

		mTextTitle = (TextView) findViewById(R.id.sub_header_bar_tv);
		mEditRemark = (EditText) findViewById(R.id.edit_remark);
		mTextLength = (TextView) findViewById(R.id.text_length);
		mImageBack = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
	}

	@Override
	protected void initData() {
		mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断

	}

	@Override
	protected void initEvent() {
		mTextTitle.setText("撤销订单");

		// 监听输入的内容
		mEditRemark.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				mTextLength.setText(mEditRemark.getText().length() + "/" + 100);
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				mTextLength.setText(mEditRemark.getText().length() + "/" + 100);
			}
		});

		mImageBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()) {
		case R.id.sub_header_bar_left_ibtn:

			finish();
			break;

		default:
			break;
		}
	}
}
