package com.chewuwuyou.app.ui;

import com.chewuwuyou.app.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * 提现说明
 * 
 * @author zengys
 * 
 */
public class DepositExplainActivity extends CDDBaseActivity implements OnClickListener {

	private ImageButton mImageBack;
	private TextView mTextTitle;
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deposit_explain_layout);

		initView();
		initData();
	}

	@Override
	protected void initView() {
		webView = (WebView) findViewById(R.id.webView);
		mImageBack = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mTextTitle = (TextView) findViewById(R.id.sub_header_bar_tv);
		mTextTitle.setText("提现说明");
		mImageBack.setOnClickListener(this);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
	}

	@Override
	protected void initData() {
		webView.loadUrl("http://wei.cddang.com/mhwcw/news/tixianshuoming.html");
	}

	@Override
	protected void initEvent() {

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
