package com.chewuwuyou.app.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chewuwuyou.app.R;

/**
 * 门店订单退款
 * 
 * @author zengys
 * 
 */
public class RefundOrderActivity extends CDDBaseActivity implements
		OnClickListener {

	private TextView mTextTitle;
	private ImageButton mImageBack;
	private EditText mEditRemark;
	private TextView mTextLength;
	private ScrollView mUiScroll;
	private RelativeLayout mTitleHeight;// 标题布局高度

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.refund_order_layout);
		initView();
		initData();
		initEvent();

	}

	@Override
	protected void initView() {
		mTextTitle = (TextView) findViewById(R.id.sub_header_bar_tv);
		mImageBack = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mEditRemark = (EditText) findViewById(R.id.edit_remark);
		mTextLength = (TextView) findViewById(R.id.text_length);
		mUiScroll = (ScrollView) findViewById(R.id.uiscroll);
	}

	@Override
	protected void initData() {
		mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);// 根据不同手机判断

	}

	@Override
	protected void initEvent() {

		mUiScroll.setVerticalScrollBarEnabled(false);
		mTextTitle.setText("退款订单");
		mImageBack.setOnClickListener(this);

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
