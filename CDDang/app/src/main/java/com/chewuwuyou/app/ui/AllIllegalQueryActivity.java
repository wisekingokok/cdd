package com.chewuwuyou.app.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chewuwuyou.app.R;

/**
 * 全国违章查询通道
 *
 * @author yuyong
 */
public class AllIllegalQueryActivity extends CDDBaseActivity implements
        OnClickListener {

    private WebView mWebView;
    private TextView mTitleTV;
    private ImageButton mBackIBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_illegal_query_ac);
        initView();
        initData();
        initEvent();

    }

    @Override
    protected void initView() {
        mWebView = (WebView) findViewById(R.id.web_view);
        mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
        mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
        WebSettings webSetting = mWebView.getSettings();
        webSetting.setJavaScriptEnabled(true);// 支持JAVA脚本
        webSetting.setAllowFileAccess(true);// 设置可以访问文件
        // webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.setWebViewClient(new webViewClient());// .希望点击链接继续在当前browser中响应，必须覆盖WebViewClient对象。
    }

    @Override
    protected void initData() {
        mTitleTV.setText("全国违章查询通道");
        mWebView.loadUrl("http://wei.cddang.com/mhwcw/agreement/preCddRule?type=101");

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

    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains("tel:")) {// 支持打电话
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(intent);
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mWebView.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {
            mWebView.goBack(); // goBack()表示返回webView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
