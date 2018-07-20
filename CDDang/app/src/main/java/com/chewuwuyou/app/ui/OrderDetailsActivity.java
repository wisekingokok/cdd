package com.chewuwuyou.app.ui;

import android.content.Intent;
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
import com.chewuwuyou.app.fragment.OrderAlreadCompletedFragment;
import com.chewuwuyou.app.fragment.OrderAlreadyPaidFragment;
import com.chewuwuyou.app.fragment.OrderInServiceFragment;
import com.chewuwuyou.app.fragment.OrderNotyPaidFragment;
import com.chewuwuyou.app.fragment.OrderRefundOrderFragment;
import com.chewuwuyou.app.fragment.OrderRevokeOrderFragment;

/**
 * @describe:订单详情列表
 * @author:liuchun
 * @version 1.1.0
 * @created:2014-12-29下午6:26:04
 */
public class OrderDetailsActivity extends BaseFragmentActivity implements OnClickListener{

	private TextView mSubHeaderBarTv;// 标题
	private PagerSlidingTabStrip mOrderDetails;//导航
	private ViewPager mViewPager;
	private OrderFragmentAdapter mOrderAdapter;//适配器
	private RelativeLayout mTitleHeight;//标题布局高度
	private ImageButton mSubHeaderBarLeftIbtn;
	private Fragment[] fragments = new Fragment[] { new OrderAlreadyPaidFragment(), new OrderNotyPaidFragment(),new OrderInServiceFragment(),new OrderAlreadCompletedFragment(),new OrderRevokeOrderFragment(),new OrderRefundOrderFragment() };
	private String[] TITLES = { "未付款", "已付款", "服务中","已完成","撤销订单","退款订单" };
	public static int orderId; 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_details_ac);
		
		initView();//初始化控件
		initData();//逻辑处理
		initEvent();//事件监听

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
	
	/**
	 * 初始化控件
	 */
	private void initView() {
		mOrderDetails = (PagerSlidingTabStrip) findViewById(R.id.order_details);
		mViewPager = (ViewPager) findViewById(R.id.order_details_pager);
		mSubHeaderBarTv = (TextView) findViewById(R.id.sub_header_bar_tv);
		mSubHeaderBarTv.setText("订单详情列表");
		mSubHeaderBarLeftIbtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
	}

	/**
	 * 逻辑处理
	 */
	private void initData() {
		mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
		Intent intent = getIntent();
		
		mViewPager.setOffscreenPageLimit(3); // 设置预加载页数
		mOrderAdapter = new OrderFragmentAdapter(getSupportFragmentManager(), TITLES, fragments);
		mViewPager.setAdapter(mOrderAdapter);
		orderId = Integer.parseInt(intent.getStringExtra("storePage"));//设置显示的页数
		mViewPager.setCurrentItem(Integer.parseInt(intent.getStringExtra("storePage")));//设置显示的页数
		int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
						.getDisplayMetrics());
		mViewPager.setPageMargin(pageMargin);
		mOrderDetails.setViewPager(mViewPager);
	}

	/**
	 * 事件监听
	 */
	private void initEvent() {
		mSubHeaderBarLeftIbtn.setOnClickListener(this);
	}

	
}
