package com.chewuwuyou.app.ui;

import java.util.ArrayList;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.VehicleQuanViewPagerAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import cn.trinea.android.view.autoscrollviewpager.ViewPagerFixed;

public class VehicleQuanVewPager extends CDDBaseActivity implements OnClickListener {

	private ViewPagerFixed viewPager;
	private ArrayList<String> list;// 接收传递过来的url图片路劲
	private String suBscript;// 传递过来的下标
	private ArrayList<View> listView;// 定义集合存放view
	protected ImageLoader mImageLoader;// 访问图片
	private VehicleQuanViewPagerAdapter vehicleAdapter;// 创建适配器
	private TextView current, pageCount;// 当前页数，总页数
	private LinearLayout mVehicleQuanId;
	public static VehicleQuanVewPager mVehicleQuanVewPager;
    private String mType;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vehicle_quan_view_pager);
		mVehicleQuanVewPager = this;
		initView();// 初始化控件
		initData();
		initEvent();// 逻辑处理
	}

	@Override
	protected void initView() {

		viewPager = (ViewPagerFixed) findViewById(R.id.vehivleViewPager);
		current = (TextView) findViewById(R.id.current); // 当前页数
		pageCount = (TextView) findViewById(R.id.pagecount); // 总页数
		mVehicleQuanId = (LinearLayout) findViewById(R.id.vehicle_quan_id);
	}

	@Override
	protected void initData() {
		Intent intent = getIntent();
		if(!TextUtils.isEmpty(getIntent().getStringExtra("type"))){
			mType = getIntent().getStringExtra("type");
		}
		list = intent.getStringArrayListExtra("url");// 接收传递过来的url
		suBscript = intent.getStringExtra("viewPagerPosition");// 接收传递过来的图片下标
		vehicleAdapter = new VehicleQuanViewPagerAdapter(VehicleQuanVewPager.this,list,mType);
		viewPager.setAdapter(vehicleAdapter);
		viewPager.setCurrentItem(Integer.parseInt(suBscript));// 设置显示的当前图片
		current.setText(Integer.parseInt(suBscript) + 1 + "/");
		pageCount.setText(list.size() + "");
	}

	/**
	 * 事件监听
	 */
	@Override
	protected void initEvent() {
		mVehicleQuanId.setOnClickListener(this);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				current.setText(position + 1 + "/");
				pageCount.setText(list.size() + "");
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.vehicle_quan_id:
				finishActivity();
				break;
			default:
				break;
		}

	}
}
