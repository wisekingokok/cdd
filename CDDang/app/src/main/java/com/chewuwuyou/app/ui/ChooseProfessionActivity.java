package com.chewuwuyou.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.ChooseProfessionAdapter;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:选择职业
 * @author:yuyong
 * @date:2015-7-14下午3:41:18
 * @version:1.2.1
 */
public class ChooseProfessionActivity extends CDDBaseActivity implements
		OnClickListener {

	private TextView mTitleTV;
	private ImageButton mBackIBtn;
	private ListView mProfessionList;
	private String[] mProfessions;// 职业数组
	private String[] mIndustrys;// 行业数组
	private Integer[] mIndustryBgColors = { R.color.it_color, R.color.it_color,
			R.color.it_color, R.color.jr_color, R.color.jr_color,
			R.color.wh_color, R.color.wh_color, R.color.fl_color,
			R.color.fl_color, R.color.fl_color, R.color.mt_color,
			R.color.mt_color, R.color.xs_color, R.color.qt_color };// 行业文本背景
	private ChooseProfessionAdapter mAdapter;
	private RelativeLayout mTitleHeight;//标题布局高度
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_profession_ac);
		initView();
		initData();
		initEvent();
	}

	@Override
	protected void initView() {
		mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
		mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mProfessionList = (ListView) findViewById(R.id.profession_list);
	}

	@Override
	protected void initData() {
		mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
		mTitleTV.setText("选择职业");
		mProfessions = getResources().getStringArray(R.array.profession_arr);
		mIndustrys = getResources().getStringArray(R.array.industry_arr);

		mAdapter = new ChooseProfessionAdapter(ChooseProfessionActivity.this,
				mProfessions, mIndustrys,mIndustryBgColors);
		mProfessionList.setAdapter(mAdapter);
	}

	@Override
	protected void initEvent() {
		mBackIBtn.setOnClickListener(this);
		mProfessionList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				intent.putExtra("profession", mProfessions[arg2]);
				intent.putExtra("industry", mIndustrys[arg2]);
				intent.putExtra("industryBgColor", mIndustryBgColors[arg2]);
				setResult(55, intent);
				finishActivity();
			}
		});
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
