package com.chewuwuyou.app.ui;

import com.chewuwuyou.app.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * 当日时讯
 * 
 * @author zengys
 * 
 */
public class OnDateMessageActivity extends CDDBaseActivity implements
		OnClickListener {

	private WebView mWebView;
	private TextView mTitleTV;
	private ImageButton mBackIBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ondate_message_layout);
		initView();
		initData();
		initEvent();
	}

	@Override
	protected void initView() {
		mWebView = (WebView) findViewById(R.id.web_view);
		mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
		mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);

	}

	@Override
	protected void initData() {
		mTitleTV.setText("当日时讯");
		mWebView.loadUrl("http://wei.cddang.com/mhwcw/news/index.html");
	}

	@Override
	protected void initEvent() {
		mBackIBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			finishActivity();
			break;

		default:
			break;
		}
	}
}
