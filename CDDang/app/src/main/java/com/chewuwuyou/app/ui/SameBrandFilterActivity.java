package com.chewuwuyou.app.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.CycleWheelView;
import com.chewuwuyou.app.widget.CycleWheelView.CycleWheelViewException;
import com.chewuwuyou.app.widget.CycleWheelView.WheelItemSelectedListener;
import com.chewuwuyou.app.widget.SegmentControl;
import com.chewuwuyou.app.widget.SegmentControl.OnSegmentControlClickListener;

/**
 * @author zengys 同系车 -筛选
 */

public class SameBrandFilterActivity extends CDDBaseActivity implements
		OnClickListener {
	public final static int RESULT_CODE = 1;
	private ImageButton mImBtnBack;
	private TextView mTvTitle;
	private TextView mTvRight;
	private SegmentControl mSeControl;
	private Button mChooseCarBtn;
	private CycleWheelView cycleWheelView_one, cycleWheelView_two;
	private String sex = "";
	private String brand = "";
	private int mAge_one, mAge_two;
	private LinearLayout mLinearCarMode;
	private String nearcar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.samebrand_filter_layout);
		initView();
		initData();
		initEvent();
	}

	@Override
	protected void initView() {
		mImBtnBack = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mTvTitle = (TextView) findViewById(R.id.sub_header_bar_tv);
		mTvRight = (TextView) findViewById(R.id.sub_header_bar_right_tv);
		mSeControl = (SegmentControl) findViewById(R.id.filter_segment);
		mChooseCarBtn = (Button) findViewById(R.id.choose_car_btn);
		cycleWheelView_one = (CycleWheelView) findViewById(R.id.cycleWheelView_one);
		cycleWheelView_two = (CycleWheelView) findViewById(R.id.cycleWheelView_two);
		mLinearCarMode = (LinearLayout) findViewById(R.id.linear_CarMode);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		List<String> labels = new ArrayList<String>();
		for (int i = 0; i < 100; i++) {
			labels.add(String.valueOf(i) + "岁");
		}

		cycleWheelView_one.setLabels(labels);
		cycleWheelView_two.setLabels(labels);

		/**
		 * 设置第一个cycleWheelView属性
		 */

		try {
			cycleWheelView_one.setWheelSize(5); // 设置显示多少个数据
		} catch (CycleWheelViewException e) {
			e.printStackTrace();
		}

		cycleWheelView_one.setSelection(18);// 设置中间显示的数据是数据源中的第几个
		cycleWheelView_one.setAlphaGradual(0.5f);
		cycleWheelView_one.setDivider(Color.WHITE, 1);
		cycleWheelView_one.setSolid(Color.WHITE, Color.WHITE);
		cycleWheelView_one.setLabelColor(Color.BLACK);
		cycleWheelView_one.setLabelSelectColor(Color.BLACK);
		cycleWheelView_one
				.setOnWheelItemSelectedListener(new WheelItemSelectedListener() {

					@Override
					public void onItemSelected(int position, String label) {
						// TODO Auto-generated method stub
						mAge_one = position;
					}
				});

		/**
		 * 设置第二个cycleWheelView属性
		 */

		try {
			cycleWheelView_two.setWheelSize(5); // 设置显示多少个数据
		} catch (CycleWheelViewException e) {
			e.printStackTrace();
		}
		cycleWheelView_two.setSelection(22);// 设置中间显示的数据是数据源中的第几个
		cycleWheelView_two.setAlphaGradual(0.5f);
		cycleWheelView_two.setDivider(Color.WHITE, 1);
		cycleWheelView_two.setSolid(Color.WHITE, Color.WHITE);
		cycleWheelView_two.setLabelColor(Color.BLACK);
		cycleWheelView_two.setLabelSelectColor(Color.BLACK);
		cycleWheelView_two
				.setOnWheelItemSelectedListener(new WheelItemSelectedListener() {

					@Override
					public void onItemSelected(int position, String label) {
						// TODO Auto-generated method stub
						mAge_two = position;
					}
				});
	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub
		mTvTitle.setText("筛选");
		mTvRight.setText("确定");
		mTvRight.setVisibility(View.VISIBLE);

		nearcar = getIntent().getStringExtra("nearCar");
		if (nearcar.equals("1")) {
			mLinearCarMode.setVisibility(View.GONE);
		} else {
			mLinearCarMode.setVisibility(View.VISIBLE);
		}
		mImBtnBack.setOnClickListener(this);
		mTvRight.setOnClickListener(this);
		mChooseCarBtn.setOnClickListener(this);

		mSeControl
				.setOnSegmentControlClickListener(new OnSegmentControlClickListener() {

					@Override
					public void onSegmentControlClick(int index) {
						// TODO Auto-generated method stub
						if (index == 0) {
							sex = "";
						} else if (index == 1) {
							sex = "0";
						} else {
							sex = "1";
						}

					}
				});
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (arg0.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			finish();
			break;

		case R.id.sub_header_bar_right_tv:

			if (mChooseCarBtn.getText().equals("选择车系品牌")&&!getIntent().getStringExtra("nearCar").equals("1")) {
				ToastUtil.toastShow(this, "请选择车系品牌");
			} else {
				intent = new Intent();
				intent.putExtra("sex", sex);
				if (nearcar.equals("2")) {
					intent.putExtra("brand", brand);
				}

				if (mAge_one < mAge_two) {
					intent.putExtra("minAge", String.valueOf(mAge_one));
					intent.putExtra("maxAge", String.valueOf(mAge_two));
				} else if (mAge_one > mAge_two) {
					intent.putExtra("minAge", String.valueOf(mAge_two));
					intent.putExtra("maxAge", String.valueOf(mAge_one));
				} else {
					intent.putExtra("minAge", String.valueOf(mAge_one));
					intent.putExtra("maxAge", String.valueOf(mAge_two));
				}
				setResult(RESULT_CODE, intent);
				finish();
			}
			break;

		case R.id.choose_car_btn:
			intent = new Intent(SameBrandFilterActivity.this,
					ChooseCarBrandActivity.class);
			startActivityForResult(intent, 20);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 20) {
			if (data != null) {
				brand = data.getStringExtra("name");
				mChooseCarBtn.setText(data.getStringExtra("brandName") + "/"
						+ data.getStringExtra("name"));
			}
		}
	}
}
