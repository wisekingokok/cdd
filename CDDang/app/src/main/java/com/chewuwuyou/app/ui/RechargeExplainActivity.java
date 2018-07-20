package com.chewuwuyou.app.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chewuwuyou.app.R;

/**
 * @describe:充值说明
 * @author:liuchun
 */
public class RechargeExplainActivity extends CDDBaseActivity implements
		OnClickListener {

	private TextView mTextTV;
	private ImageButton mSubHeaderLeft;// 左边返回
	private WebView mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rechargeexplain);
		initView();
		initData();
		initEvent();

	}

	@Override
	protected void initView() {
		mTextTV = (TextView) findViewById(R.id.sub_header_bar_tv);
		mSubHeaderLeft = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mWebView = (WebView) findViewById(R.id.web_view);
	}

	@Override
	protected void initData() {
		switch (getIntent().getIntExtra("type", 0)) {
		case 0:// 充值说明
			mTextTV.setText("充值说明");
			mWebView.loadUrl("http://wei.cddang.com/mhwcw/"
					+ "news/chongzhishuoming.html");
			break;
		case 1:// 发布说明
			mTextTV.setText("发布说明");
			mWebView.loadUrl("http://wei.cddang.com/mhwcw/"
					+ "news/shuoming.html");
			break;

		default:
			break;
		}
	}

	@Override
	protected void initEvent() {
		mSubHeaderLeft.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:// 返回上一页
			finishActivity();
			break;

		default:
			break;
		}
	}
}
