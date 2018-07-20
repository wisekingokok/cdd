package com.chewuwuyou.app.ui;

import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.chewuwuyou.app.R;

/**
 * @describe:隐私声明
 * @author:yuyong
 * @version 1.1.0
 * @created:2014-12-10上午3:47:50
 */
public class PrivacyPolicyActivity extends CDDBaseActivity {
	@ViewInject(id = R.id.sub_header_bar_left_ibtn)
	private ImageButton mBackBtn;
	@ViewInject(id = R.id.sub_header_bar_tv)
	private TextView mHeaderTV;
	private WebView mWeb;
	private RelativeLayout mTitleHeight;// 标题布局高度

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.privacy_policy_ac);
		initView();
	}

	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPause(PrivacyPolicyActivity.this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(PrivacyPolicyActivity.this);
	}

	@Override
	protected void initView() {
		mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);// 根据不同手机判断
		mBackBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finishActivity();
			}
		});
		mWeb = (WebView) findViewById(R.id.spwebView);
		WebSettings webSettings = mWeb.getSettings();
		webSettings.setDefaultTextEncodingName("utf-8");
		webSettings.setDefaultFontSize(16);
		mWeb.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
		// mWeb.setBackgroundColor(Color.TRANSPARENT); // WebView 背景透明效果
		mWeb.setVerticalScrollBarEnabled(false);
		if (getIntent().getIntExtra("tag", 0) == 1) {
			mHeaderTV.setText("版权&隐私声明");
			mWeb.loadUrl("file:///android_asset/html/" + "yssm.html");
		} else if (getIntent().getIntExtra("tag", 0) == 2) {
			mHeaderTV.setText("免责声明");
			mWeb.loadUrl("file:///android_asset/html/" + "mzsm.html");
		} else {
			mHeaderTV.setText(R.string.setting_yssm_title);
			mWeb.loadUrl("file:///android_asset/html/" + "fwtk.html");
		}

	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initEvent() {

	}

}
