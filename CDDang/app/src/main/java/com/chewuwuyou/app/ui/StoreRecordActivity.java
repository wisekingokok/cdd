package com.chewuwuyou.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.barcode.view.ProgressWheel;
import com.chewuwuyou.app.R;

/**
 * @describe:门店录单
 * @author:liuchun
 */
public class StoreRecordActivity extends CDDBaseActivity implements
		OnClickListener {

	private TextView mTitel;// 标题
	private ImageButton mSubHeaderBarLeftIbtn;// 返回上一页
	private TextView mSubHeaderBarRight;
	private ProgressWheel mProgressNoPayment, mProgressaLreadyPayment,
			mProgressaInService, mProgressCompleted, mProgressRevokeOrder,
			mProgressRefundOrder;// 未付款,已付款,服务中,已完成,撤销订单,退款订单
	private RelativeLayout mTitleHeight;// 标题布局高度
	private TextView mNoPayment, mProgressAlready, mInService, mCompleted,
			mRevokeOrder, mRefundOrder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_record);

		initView();// 初始化
		initData();// 逻辑处理
		initEvent();// 监听点击事件
	}

	/**
	 * 初始化控件
	 */
	@Override
	protected void initView() {
		mTitel = (TextView) findViewById(R.id.sub_header_bar_tv);
		mTitel.setText("门店录单");
		mSubHeaderBarLeftIbtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);// 返回上一页
		mSubHeaderBarRight = (TextView) findViewById(R.id.sub_header_bar_right_tv);// 录入
		mSubHeaderBarRight.setVisibility(View.VISIBLE);
		mSubHeaderBarRight.setText("录入");

		mNoPayment = (TextView) findViewById(R.id.no_payment_onclic);// 未付款
		mProgressAlready = (TextView) findViewById(R.id.progress_already_onclic);// 已付款
		mInService = (TextView) findViewById(R.id.in_service_onclic);// 服务中
		mCompleted = (TextView) findViewById(R.id.completed_onclick);// 已完成
		mRevokeOrder = (TextView) findViewById(R.id.revoke_order_onclick);// 撤销订单
		mRefundOrder = (TextView) findViewById(R.id.refund_order_onclick);// 退款订单
		mProgressNoPayment = (ProgressWheel) findViewById(R.id.progress_no_payment);// 未付款
		mProgressaLreadyPayment = (ProgressWheel) findViewById(R.id.progress_already_payment);// 已付款
		mProgressaInService = (ProgressWheel) findViewById(R.id.progress_in_service);// 服务中
		mProgressCompleted = (ProgressWheel) findViewById(R.id.progress_completed);// 已完成
		mProgressRevokeOrder = (ProgressWheel) findViewById(R.id.progress_revoke_order);// 撤销订单
		mProgressRefundOrder = (ProgressWheel) findViewById(R.id.progress_refund_order);// 退款订单

	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		Intent intent;

		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:// 返回上一页
			finishActivity();
			break;
		case R.id.sub_header_bar_right_tv:// 录入
			intent = new Intent(this, AddStoreRecordActivity.class);
			startActivity(intent);
			break;
		case R.id.no_payment_onclic:// 未付款
			intent = new Intent(this, OrderDetailsActivity.class);
			intent.putExtra("storePage", "0");
			startActivity(intent);
			break;
		case R.id.progress_already_onclic:// 已付款
			intent = new Intent(this, OrderDetailsActivity.class);
			intent.putExtra("storePage", "1");
			startActivity(intent);
			break;
		case R.id.in_service_onclic:// 服务中
			intent = new Intent(this, OrderDetailsActivity.class);
			intent.putExtra("storePage", "2");
			startActivity(intent);
			break;
		case R.id.completed_onclick:// 已完成
			intent = new Intent(this, OrderDetailsActivity.class);
			intent.putExtra("storePage", "3");
			startActivity(intent);
			break;
		case R.id.revoke_order_onclick:// 撤销订单
			intent = new Intent(this, OrderDetailsActivity.class);
			intent.putExtra("storePage", "4");
			startActivity(intent);
			break;
		case R.id.refund_order_onclick:// 退款订单
			intent = new Intent(this, OrderDetailsActivity.class);
			intent.putExtra("storePage", "5");
			startActivity(intent);
			break;
		default:
			break;
		}

	}

	@Override
	protected void initData() {
		mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);// 根据不同手机判断
		mProgressNoPayment.setCurrentValues(50);
		mProgressaLreadyPayment.setCurrentValues(50);
		mProgressaInService.setCurrentValues(50);
		mProgressCompleted.setCurrentValues(50);
		mProgressRevokeOrder.setCurrentValues(50);
		mProgressRefundOrder.setCurrentValues(50);
	}

	/**
	 * 监听点击事件
	 */
	@Override
	protected void initEvent() {
		mSubHeaderBarLeftIbtn.setOnClickListener(this);
		mSubHeaderBarRight.setOnClickListener(this);
		mNoPayment.setOnClickListener(this);
		mProgressAlready.setOnClickListener(this);
		mInService.setOnClickListener(this);
		mCompleted.setOnClickListener(this);
		mRevokeOrder.setOnClickListener(this);
		mRefundOrder.setOnClickListener(this);
	}
}
