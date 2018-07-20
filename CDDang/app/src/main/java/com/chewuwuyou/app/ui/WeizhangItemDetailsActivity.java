package com.chewuwuyou.app.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.WeizhangChildItem;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.utils.Tools;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:违章单项详情可复制违章详情发送给好友
 * @author:yuyong
 * @date:2015-11-11下午5:04:31
 * @version:1.2.1
 */
public class WeizhangItemDetailsActivity extends CDDBaseActivity implements
		OnClickListener {

	private TextView mTitleTV;
	private ImageButton mBackIBtn;
	private WeizhangChildItem mWeizhangChildItem;// 违章单项实体
	private TextView mWeizhangTimeTV, mWeizhangAddressTV,
			mWeizhangDescriptionTV, mWeizhangPriceTV, mWeizhangScoreTV,
			mWeizhangStatusTV;

	private Button mCopyIllegalDetailsBtn;
	private RelativeLayout mTitleHeight;//标题布局高度
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weizhang_item_details_ac);
		initView();
		initData();
		initEvent();

	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
		mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mWeizhangTimeTV = (TextView) findViewById(R.id.weizhang_time_tv);
		mWeizhangAddressTV = (TextView) findViewById(R.id.weizhang_address_tv);
		mWeizhangDescriptionTV = (TextView) findViewById(R.id.weizhang_details_tv);
		mWeizhangPriceTV = (TextView) findViewById(R.id.weizhang_price_tv);
		mWeizhangScoreTV = (TextView) findViewById(R.id.weizhang_score_tv);
		mWeizhangStatusTV = (TextView) findViewById(R.id.weizhang_status_tv);
		mCopyIllegalDetailsBtn = (Button) findViewById(R.id.copy_illegal_details_btn);
	}

	@Override
	protected void initData() {
		mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
		mTitleTV.setText("违章详情");
		mWeizhangChildItem = (WeizhangChildItem) getIntent()
				.getSerializableExtra("weizhangChildItem");
		mWeizhangTimeTV.setText(mWeizhangChildItem.getOccur_date());
		mWeizhangAddressTV.setText(mWeizhangChildItem.getOccur_area());
		mWeizhangDescriptionTV.setText(mWeizhangChildItem.getInfo());
		mWeizhangPriceTV.setText("-￥" + mWeizhangChildItem.getMoney());
		mWeizhangScoreTV.setText(String.valueOf(mWeizhangChildItem.getFen()));
		mWeizhangStatusTV
				.setText(mWeizhangChildItem.getStatus().equals("N") ? "未处理"
						: "处理");
	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub
		mBackIBtn.setOnClickListener(this);
		mCopyIllegalDetailsBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			finishActivity();
			break;
		case R.id.copy_illegal_details_btn:
			String illegailDetail = "时间：" + mWeizhangChildItem.getOccur_date()
					+ "\n" + "违章地点：" + mWeizhangChildItem.getOccur_area()
					+ "\n" + "违章详情：" + mWeizhangChildItem.getInfo() + "\n"
					+ "罚款：" + mWeizhangChildItem.getMoney() + "\n" + "扣分："
					+ mWeizhangChildItem.getFen();
			Tools.copy(illegailDetail, WeizhangItemDetailsActivity.this);
			ToastUtil.toastShow(WeizhangItemDetailsActivity.this, "已复制");
			break;
		default:
			break;
		}
	}
}
