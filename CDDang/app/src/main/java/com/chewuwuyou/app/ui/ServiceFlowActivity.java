package com.chewuwuyou.app.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:查看服务流程
 * @author:yuyong
 * @date:2015-8-9下午5:34:43
 * @version:1.2.1
 */
public class ServiceFlowActivity extends CDDBaseActivity {

	private ImageView mFlowIV;// 流程图片
	private TextView mTitleTV;
	private ImageButton mBackIBtn;
	private RelativeLayout mTitleHeight;//标题布局高度
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_lc_ac);
		initView();
		initData();
		initEvent();
	}

	@Override
	protected void initView() {
		mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
		mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mFlowIV = (ImageView) findViewById(R.id.service_page_iv);

	}

	@Override
	protected void initData() {
		mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
		int serviceType = getIntent().getExtras().getInt("serviceType");
		if (serviceType == 1) {
			mTitleTV.setText("违章代缴流程");
			mFlowIV.setImageResource(R.drawable.service_wzfw_page);
		} else if (serviceType == 2) {
			mTitleTV.setText("车辆服务流程");
			mFlowIV.setImageResource(R.drawable.service_clfw_page);
		} else {
			mTitleTV.setText("驾证服务流程");
			mFlowIV.setImageResource(R.drawable.service_jzfw_page);
		}

	}

	@Override
	protected void initEvent() {
		mBackIBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finishActivity();
			}
		});
	}

}
