package com.chewuwuyou.app.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.barcode.view.ProgressWebView;
import com.chewuwuyou.app.R;

/**
 * banner跳转的Activity
 *
 * @author yuyong
 */
public class BannerToActivity extends CDDBaseActivity implements
        OnClickListener {

    private ProgressWebView mWebView;
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
        mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
        mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
        mWebView = (ProgressWebView) findViewById(R.id.web_view);
        WebSettings webSetting = mWebView.getSettings();
        webSetting.setJavaScriptEnabled(true);// 支持JAVA脚本
        webSetting.setAllowFileAccess(true);// 设置可以访问文件
    }

    @Override
    protected void initData() {
        mWebView.setWebViewClient(new webViewClient());// .希望点击链接继续在当前browser中响应，必须覆盖WebViewClient对象。
        mWebView.loadUrl(getIntent().getStringExtra("loadUrl"));
        mWebView.setWebChromeClient(new WebChromeClient() {// 捕获JS的点击事件

            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     final JsResult result) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        BannerToActivity.this);
                builder.setMessage(message)
                        .setNeutralButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0,
                                                        int arg1) {
                                        arg0.dismiss();
                                    }
                                }).show();
                result.cancel();
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url,
                                       String message, final JsResult result) {

                AlertDialog.Builder builder = new AlertDialog.Builder(
                        BannerToActivity.this);
                builder.setMessage(message)
                        .setNegativeButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0,
                                                        int arg1) {
                                        result.confirm();
                                        arg0.dismiss();

                                    }
                                })
                        .setPositiveButton("取消",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        result.cancel();
                                    }
                                }).show();
                return true;
            }
        });
        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                mTitleTV.setText(title);
            }

        };
        // 设置setWebChromeClient对象
        mWebView.setWebChromeClient(wvcc);
    }

    @Override
    protected void initEvent() {
        mBackIBtn.setOnClickListener(this);

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
