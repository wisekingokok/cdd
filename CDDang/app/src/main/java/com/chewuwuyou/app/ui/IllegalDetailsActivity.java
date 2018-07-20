package com.chewuwuyou.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.CommonAdapter;
import com.chewuwuyou.app.adapter.ViewHolder;
import com.chewuwuyou.app.bean.WeizhangChildItem;
import com.chewuwuyou.app.bean.WeizhangGroupItem;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:违章详情
 * @author:yuyong
 * @date:2015-12-31下午5:05:40
 * @version:1.2.1
 */
public class IllegalDetailsActivity extends CDDBaseActivity implements
		OnClickListener {

	private TextView mTitleTV;
	private ImageButton mBackIBtn;
	private RelativeLayout mTitleHeight;//标题布局高度

	private TextView mCityTV;// 违章地址
	private TextView mIllegalGroupInfoTV;// 违章统计
	private TextView mEmptyTV;// 请求数据为空
	private ListView mIllegalLV;// 违章列表
	private WeizhangGroupItem mWeizhangGroupItem;
	private CommonAdapter<WeizhangChildItem> mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.illegal_details_layout);
		initView();
		initData();
		initEvent();
	}

	@Override
	protected void initView() {
		mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
		mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
		mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mCityTV = (TextView) findViewById(R.id.weizhang_group_city_tv);
		mIllegalGroupInfoTV = (TextView) findViewById(R.id.weizhang_group_info_tv);
		mEmptyTV = (TextView) findViewById(R.id.weizhang_empty_tv);
		mIllegalLV = (ListView) findViewById(R.id.weizhang_list);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		mTitleTV.setText("违章列表");
		mCityTV.setText(getIntent().getStringExtra("queryCity"));
		mWeizhangGroupItem = (WeizhangGroupItem) getIntent()
				.getSerializableExtra("weizhangGroupItem");
		if (mWeizhangGroupItem == null||"".equals(mWeizhangGroupItem)) {
			mEmptyTV.setVisibility(View.VISIBLE);
			return;
		}
		String content = new StringBuilder().append("共")
				.append(mWeizhangGroupItem.getCount()).append("条违章,")
				.append("罚款").append(mWeizhangGroupItem.getTotal_money())
				.append("元,").append("扣分")
				.append(mWeizhangGroupItem.getTotal_score()).append("分")
				.toString();
		SpannableStringBuilder ssb = new SpannableStringBuilder(content);
		ssb.setSpan(new AbsoluteSizeSpan(18, true), 1,
				String.valueOf(mWeizhangGroupItem.getCount()).length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mIllegalGroupInfoTV.setText(ssb.toString());
		mAdapter = new CommonAdapter<WeizhangChildItem>(
				IllegalDetailsActivity.this, mWeizhangGroupItem.getHistorys(),
				R.layout.weizhang_child_item) {

			@Override
			public void convert(ViewHolder holder, WeizhangChildItem t,int p) {
				// TODO Auto-generated method stub
				holder.setText(R.id.weizhang_date_tv, t.getOccur_date());
				holder.setText(R.id.weizhang_fakuan_num_tv,
						String.valueOf(t.getMoney()));
				holder.setText(R.id.weizhang_location_tv, t.getOccur_area());
				holder.setText(R.id.weizhang_info_tv, t.getInfo());
				holder.setText(R.id.weizhang_koufen_num_tv,
						String.valueOf(t.getFen()));
				holder.setText(R.id.weizhang_more_tv,
						t.getStatus().equals("N") ? "未处理" : "处理");

			}
		};
		mIllegalLV.setAdapter(mAdapter);

	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub
		mBackIBtn.setOnClickListener(this);
		mIllegalLV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(IllegalDetailsActivity.this,
						WeizhangItemDetailsActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("weizhangChildItem", mWeizhangGroupItem
						.getHistorys().get(arg2));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			finishActivity();
			break;

		default:
			break;
		}
	}

}
