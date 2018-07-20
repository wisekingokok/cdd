package com.chewuwuyou.app.transition_view.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.barcode.view.ProgressWebView;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.transition_view.activity.base.BaseTitleActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xxy on 2016/10/11 0011.
 */

public class ExplainActivity extends BaseTitleActivity {

    @BindView(R.id.web_view)
    ProgressWebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explain);
        ButterKnife.bind(this);
        setBarTitle("转账说明");
        setLeftBarBtnImage(R.drawable.backbutton);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl("http://www.baidu.com");
    }

    @Override
    protected void onTitleBarClick(View view, int title_tag) {
        switch (title_tag) {
            case TITLE_TAG_LEFT_BTN:
                finish();
                break;
        }
    }
}
