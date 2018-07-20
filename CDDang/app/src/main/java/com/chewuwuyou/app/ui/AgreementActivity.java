package com.chewuwuyou.app.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.utils.AgreementUtil;
import com.chewuwuyou.app.utils.NetworkUtil;

/**
 * 查看协议
 * 
 * @author yuyong
 * 
 */
public class AgreementActivity extends CDDBaseActivity implements
		OnClickListener {

	// http://wei.cddang.com/mhwcw/agreement/preCddRule?type=2
	// type=1 车当当服务收费规则
	// type=2 车当当服务协议
	// type=3 车当当关于第三方服务商违章代办声明
	// type=4 车当当提现协议
	// type=5 车当当退款协议
	// type=6 车当当争议处理协议
	// type=7 成都车当当信息科技有限公司短信校验服务协议
	// type=8 成都车当当信息科技有限公司法律声明
	private WebView mWebView;
	private TextView mTitleTV;
	private ImageButton mBackIBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.agreement_ac);
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
		if (getIntent().getIntExtra("type", 1) == 9) {
			if (getIntent().getStringExtra("linkURL").startsWith("http:")) {
				mWebView.loadUrl(getIntent().getStringExtra("linkURL"));
			} else {
				mWebView.loadUrl(NetworkUtil.BASE_URL
						+ getIntent().getStringExtra("linkURL"));
			}

		} else {
			mWebView.loadUrl("http://wei.cddang.com/mhwcw/agreement/preCddRule?type="
					+ getIntent().getIntExtra("type", 1));
		}

		WebChromeClient wvcc = new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				mTitleTV.setText(title);
			}

		};
		// 设置setWebChromeClient对象
		mWebView.setWebChromeClient(wvcc);
//		mTitleTV.setText(AgreementUtil.getTitle(getIntent().getIntExtra("type",
//				1)));
	}

	@Override
	protected void initEvent() {
		mBackIBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			finishActivity();
			break;

		default:
			break;
		}
	}

}
