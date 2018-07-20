
package com.chewuwuyou.app.ui;

import java.io.IOException;
import java.io.InputStream;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;

/**
 * 车标介绍
 * 
 * @author Administrator
 */
public class CarLogofDetailsActivity extends BaseActivity {

    private WebView mBrandContent;
    private TextView mCarLogNmae;
    private ImageView mCarLogImg;
    private RelativeLayout mTitleHeight;//标题布局高度
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carservice_logof_details_ac);
        initView();
    }

    public void initView() {
    	mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
        ((TextView) findViewById(R.id.sub_header_bar_tv))
                .setText(R.string.car_logof_introduce_title);
        findViewById(R.id.sub_header_bar_left_ibtn).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finishActivity();
            }
        });
        mCarLogImg = (ImageView) findViewById(R.id.carlog_img);
        mCarLogNmae = (TextView) findViewById(R.id.carlog_name);
        mBrandContent = (WebView) findViewById(R.id.brand_content);
        Intent intent = getIntent();
        String carLogo = intent.getStringExtra("carLogo");
        String carName = intent.getStringExtra("logoName");
        String carLogoDetailsUrl = intent.getStringExtra("logoDetails");
        try {
            InputStream is = getAssets().open("logo/" + carLogo);
            mCarLogImg.setImageBitmap(BitmapFactory.decodeStream(is));
            mCarLogNmae.setText(carName);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        WebSettings webSettings = mBrandContent.getSettings();
        webSettings.setDefaultTextEncodingName("utf-8");

        webSettings.setDefaultFontSize(16);
        mBrandContent.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        mBrandContent.setBackgroundColor(Color.TRANSPARENT); // WebView 背景透明效果
        mBrandContent.setVerticalScrollBarEnabled(false);
        mBrandContent.setHorizontalScrollBarEnabled(false);
        mBrandContent
                .loadUrl("file:///android_asset/html/" + carLogoDetailsUrl);
    }
}
