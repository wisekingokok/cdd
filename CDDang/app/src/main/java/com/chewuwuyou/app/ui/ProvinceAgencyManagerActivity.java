package com.chewuwuyou.app.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.barcode.view.PagerSlidingTabStrip;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.OrderFragmentAdapter;
import com.chewuwuyou.app.fragment.BusiTJFragment;
import com.chewuwuyou.app.fragment.TradingOrderFragment;
import com.chewuwuyou.app.fragment.TradingOrderPriceTJFragment;
import com.chewuwuyou.app.fragment.TradingOrderTJFragment;
import com.chewuwuyou.app.utils.CacheTools;

/**
 * 省代理管理界面
 * 
 * @author yuyong 包含成交订单、订单金额、订单总量、商家统计的统计
 * 
 * 
 */
public class ProvinceAgencyManagerActivity extends BaseFragmentActivity
		implements OnClickListener {

	private TextView mTitleTV;// 标题
	private PagerSlidingTabStrip mOrderDetails;// 导航
	private ViewPager mViewPager;
	private OrderFragmentAdapter mOrderAdapter;// 适配器
	private ImageButton mBackIbtn;
	private Fragment[] mFragments = new Fragment[] {
			new TradingOrderFragment(), new TradingOrderPriceTJFragment(),
			new TradingOrderTJFragment(), new BusiTJFragment() };
	private String[] mTitles = { "成交订单", "订单金额", "订单总量", "商家统计" };
	public static int orderId;
	private RelativeLayout mTitleHeight;// 标题布局高度

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.province_agency_manager_ac);
		initView();
		initData();
		initEvent();
	}

	private void initView() {
		mOrderDetails = (PagerSlidingTabStrip) findViewById(R.id.order_details);
		mViewPager = (ViewPager) findViewById(R.id.order_details_pager);
		mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
		mBackIbtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
	}

	private void initData() {
		if (CacheTools.getUserData("daiLitype").equals("1")) {// 省代
			mTitleTV.setText("省代管理");
		} else if (CacheTools.getUserData("daiLitype").equals("2")) {
			mTitleTV.setText("市代管理");
		} else {
			mTitleTV.setText("区代管理");
		}
		mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);// 根据不同手机判断
		// Intent intent = getIntent();

		mViewPager.setOffscreenPageLimit(3); // 设置预加载页数
		mOrderAdapter = new OrderFragmentAdapter(getSupportFragmentManager(),
				mTitles, mFragments);
		mViewPager.setAdapter(mOrderAdapter);
		// orderId = Integer.parseInt(intent.getStringExtra("storePage"));//
		// 设置显示的页数
		mViewPager.setCurrentItem(0);// 设置显示的页数
		int pageMargin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
						.getDisplayMetrics());
		mViewPager.setPageMargin(pageMargin);
		mOrderDetails.setViewPager(mViewPager);
	}

	private void initEvent() {
		mBackIbtn.setOnClickListener(this);
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
