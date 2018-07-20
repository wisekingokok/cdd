package com.chewuwuyou.app.ui;

import net.tsz.afinal.http.AjaxParams;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.SegmentControl;
import com.chewuwuyou.app.widget.SegmentControl.OnSegmentControlClickListener;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:记账订单
 * @author:yuyong
 * @date:2015-10-23上午11:26:47
 * @version:1.2.1
 */
public class RecordOrderActivity extends CDDBaseActivity implements
		OnClickListener {

	private TextView mTitleTV;
	private ImageButton mBackIBtn;

	private SegmentControl mSegmentControl;// 确定几张订单类型
	private EditText mOrderPriceET;
	private EditText mOrderDescriptionET;// 订单描述
	private Button mRecordBtn;// 确认记录
	private String mUrl = NetworkUtil.VIOLATION_SERVICE_URL;
	private RelativeLayout mTitleHeight;//标题布局高度
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_order_ac);
		initView();
		initData();
		initEvent();
	}

	@Override
	protected void initView() {
		mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
		mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mSegmentControl = (SegmentControl) findViewById(R.id.segment_control);
		mOrderPriceET = (EditText) findViewById(R.id.order_price_et);
		mOrderDescriptionET = (EditText) findViewById(R.id.order_description_et);
		mRecordBtn = (Button) findViewById(R.id.record_btn);
	}

	@Override
	protected void initData() {
		mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
		mTitleTV.setText("订单录入");
	}

	@Override
	protected void initEvent() {
		mBackIBtn.setOnClickListener(this);
		mRecordBtn.setOnClickListener(this);
		mSegmentControl
				.setOnSegmentControlClickListener(new OnSegmentControlClickListener() {

					@Override
					public void onSegmentControlClick(int index) {
						if (index == 0) {
							mUrl = NetworkUtil.VIOLATION_SERVICE_URL;
						} else if (index == 1) {
							mUrl = NetworkUtil.VEHICLE_SERVICE_URL;
						} else {
							mUrl = NetworkUtil.LICENCE_SERVICE_URL;
						}
					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			finishActivity();
			break;
		case R.id.record_btn:
			String orderPrice = mOrderPriceET.getText().toString();
			String orderDescription = mOrderDescriptionET.getText().toString();
			if (TextUtils.isEmpty(orderPrice)) {
				ToastUtil.toastShow(RecordOrderActivity.this, "请输入订单价格");
			} else if (orderPrice.equals(".")
					&& Double.valueOf(orderPrice) <= 0) {
				ToastUtil.toastShow(RecordOrderActivity.this, "请输入正确的价格");
			} else if (TextUtils.isEmpty(orderDescription)) {
				ToastUtil.toastShow(RecordOrderActivity.this, "请输入订单描述");
			} else {
				AjaxParams params = new AjaxParams();
				params.put("userDescription", orderDescription);
				params.put("paymentAmount", orderPrice);
				params.put("spSta", "3");
				requestNet(new Handler() {
					@Override
					public void handleMessage(Message msg) {
						super.handleMessage(msg);
						switch (msg.what) {
						case Constant.NET_DATA_SUCCESS:
							AlertDialog.Builder builder = new AlertDialog.Builder(
									RecordOrderActivity.this);
							builder.setTitle("录入结果");
							builder.setMessage("录入成功！");
							builder.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface arg0, int arg1) {
											arg0.dismiss();
											finishActivity();
										}
									});
							builder.setNegativeButton("继续录入",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface arg0, int arg1) {
											arg0.dismiss();
											mOrderPriceET.setText("");
											mOrderPriceET.setText("");
										}
									});
							builder.create().show();
							break;

						default:
							break;
						}
					}
				}, params, mUrl, false, 0);
			}

			break;
		default:
			break;
		}
	}
}
