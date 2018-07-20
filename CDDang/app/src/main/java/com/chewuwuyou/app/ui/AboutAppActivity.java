package com.chewuwuyou.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;

/**
 * @describe:关于车当当
 * @author:yuyong
 * @version 1.1.0
 * @created:2014-12-29下午6:26:04
 */
public class AboutAppActivity extends CDDBaseActivity implements
		OnClickListener {

	// private AutoScrollViewPager mViewPager;
	// private CirclePageIndicator mCirclePageIndicator;
	private RelativeLayout mTitleHeight;
	private TextView mXieyiTV;// 点击查看服务协议

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_cdd_xml);
		((TextView) findViewById(R.id.sub_header_bar_tv)).setText("关于我们");
		mXieyiTV = (TextView) findViewById(R.id.xieyi_tv);
		findViewById(R.id.sub_header_bar_left_ibtn).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						finishActivity();
					}
				});

		initView();
		initData();
		initEvent();

	}

	@Override
	protected void initView() {
		// mViewPager = (AutoScrollViewPager)
		// findViewById(R.id.auto_view_pager);
		// mCirclePageIndicator = (CirclePageIndicator)
		// findViewById(R.id.circle_page_indicator);
		// List<Integer> data = new ArrayList<Integer>();
		// data.add(R.drawable.cdd_page1);
		// mViewPager
		// .setAdapter(new ImagePagerAdapter2(AboutAppActivity.this, data));
		// mViewPager.startAutoScroll();
		// mViewPager.setInterval(2000);
		// mCirclePageIndicator.setViewPager(mViewPager);
		// mViewPager.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE /
		// 2
		// % data.size());
		// mCirclePageIndicator = (CirclePageIndicator)
		// findViewById(R.id.circle_page_indicator);
	}

	@Override
	protected void initData() {
		mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);// 根据不同手机判断

	}

	@Override
	protected void initEvent() {
		mXieyiTV.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()) {
		case R.id.xieyi_tv:
			Intent intent = new Intent(AboutAppActivity.this,
					AgreementActivity.class);
			intent.putExtra("type", 8);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

}
