package com.chewuwuyou.app.ui;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.barcode.view.ProgressWebView;
import com.chewuwuyou.app.R;

/**
 * 客户帮助
 * 
 * @author zengys
 * 
 */
public class ClientHelpActivity extends BaseActivity implements
		OnClickListener {

	private ProgressWebView mWebView;
	private TextView mTitleTV;
	private ImageButton mBackIBtn;
	private RadioGroup tabs;
	private LinearLayout mlinear;
	private ScrollView mScrollView;
	private RelativeLayout mTitleHeight;//标题布局高度
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.client_help_layout);
		initView();
		initData();
		initEvent();
	}

	@SuppressLint("SetJavaScriptEnabled")

	protected void initView() {
		mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
		mScrollView= (ScrollView) findViewById(R.id.scroll_view);
		mWebView = (ProgressWebView) findViewById(R.id.web_view);
		mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
		mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		tabs = (RadioGroup) findViewById(R.id.tabs);
		mlinear = (LinearLayout) findViewById(R.id.linear);
		
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		final ProgressBar bar = (ProgressBar) findViewById(R.id.myProgressBar);
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					bar.setVisibility(View.INVISIBLE);
				} else {
					if (View.INVISIBLE == bar.getVisibility()) {
						bar.setVisibility(View.VISIBLE);
					}
					bar.setProgress(newProgress);
				}
				super.onProgressChanged(view, newProgress);
			}

		});

		//controlKeyboardLayout(mlinear, mWebView);
	}


	protected void initData() {
		mTitleTV.setText("客服帮助");
		mWebView.loadUrl("http://wei.cddang.com/mhwcw/agreement/preCustomerService");
	}


	protected void initEvent() {
		mBackIBtn.setOnClickListener(this);
		tabs.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.help:
					mWebView.loadUrl("http://wei.cddang.com/mhwcw/agreement/preCustomerService");


					break;
				case R.id.zhaomu:
					mWebView.loadUrl("http://wei.cddang.com/mhwcw/subscription/preIndex");
					break;
				}
			}
		});
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
	/**
	 * @param root
	 *            最外层布局，需要调整的布局
	 * @param scrollToView
	 *            被键盘遮挡的scrollToView，滚动root,使scrollToView在root可视区域的底部
	 */
	private void controlKeyboardLayout(final View root, final View scrollToView) {
		root.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						Rect rect = new Rect();
						// 获取root在窗体的可视区域
						root.getWindowVisibleDisplayFrame(rect);
						// 获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
						int rootInvisibleHeight = root.getRootView()
								.getHeight() - rect.bottom;
						// 若不可视区域高度大于100，则键盘显示
						if (rootInvisibleHeight > 100) {
							int[] location = new int[2];
							// 获取scrollToView在窗体的坐标
							scrollToView.getLocationInWindow(location);
							// 计算root滚动高度，使scrollToView在可见区域
							int srollHeight = (location[1] + scrollToView
									.getHeight()) - rect.bottom;
							root.scrollTo(0, srollHeight);
						} else {
							// 键盘隐藏
							root.scrollTo(0, 0);
						}
					}
				});
	}

}
