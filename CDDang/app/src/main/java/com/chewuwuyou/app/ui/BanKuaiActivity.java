package com.chewuwuyou.app.ui;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.BanKuaiGridViewAdapter;
import com.chewuwuyou.app.bean.BanKuaiItem;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.widget.PullToRefreshBase;
import com.chewuwuyou.app.widget.PullToRefreshBase.OnRefreshListener2;
import com.chewuwuyou.app.widget.PullToRefreshGridView;

/**
 * @describe:车友吐槽的AC
 * @author:XH
 * @version
 * @created:
 */
public class BanKuaiActivity extends CDDBaseActivity implements
		OnRefreshListener2<GridView> {
	@ViewInject(id = R.id.sub_header_bar_left_ibtn, click = "onAction")
	private ImageButton mBackBtn;
	@ViewInject(id = R.id.sub_header_bar_tv)
	private TextView mHeaderTV;
	private PullToRefreshGridView mPullRefreshGridView;

	private List<BanKuaiItem> mData;
	private BanKuaiGridViewAdapter mAdapter;
	private RelativeLayout mTitleHeight;// 标题布局高度
	// private AutoScrollViewPager mViewPager;
	// private CirclePageIndicator mCirclePageIndicator;
	private boolean mIsRefreshing = false;// 翻页要用
	private GridView mGridView;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			TextView tv = new TextView(BanKuaiActivity.this);
			tv.setGravity(Gravity.CENTER);
			tv.setText("没有板块");
			tv.setTextColor(getResources().getColor(R.color.empty_text_color));
			mPullRefreshGridView.setEmptyView(tv);

			mIsRefreshing = false;
			switch (msg.what) {
			case Constant.NET_DATA_SUCCESS:
				mPullRefreshGridView.onRefreshComplete();
				mData = BanKuaiItem.parseList(msg.obj.toString());
				if (mData != null && !mData.isEmpty()) {
					mAdapter = new BanKuaiGridViewAdapter(BanKuaiActivity.this,
							mData);
					mGridView.setAdapter(mAdapter);
					mGridView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(BanKuaiActivity.this,
									TieActivity.class);
							intent.putExtra("banId", mData.get(arg2).getId());
							intent.putExtra("banTitle", mData.get(arg2)
									.getTitle());
							intent.putExtra("banUrl", mData.get(arg2)
									.getPhoto());
							startActivity(intent);
						}
					});
				}

				break;

			default:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bankuai_layout);
		initView();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		getAllBankuai();
	}

	protected void initView() {
		mHeaderTV.setText("车友吐槽");
		mPullRefreshGridView = (PullToRefreshGridView) findViewById(R.id.bankuai_gv);
		// mViewPager = (AutoScrollViewPager)
		// findViewById(R.id.auto_view_pager);
		// mCirclePageIndicator = (CirclePageIndicator)
		// findViewById(R.id.circle_page_indicator);
		mPullRefreshGridView.setOnRefreshListener(this);
		mGridView = mPullRefreshGridView.getRefreshableView();

	}

	protected void initData() {
		mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);// 根据不同手机判断
		if (mData == null) {
			mData = new ArrayList<BanKuaiItem>();
		}
		if (mAdapter == null) {
			mAdapter = new BanKuaiGridViewAdapter(BanKuaiActivity.this, mData);
		}
		if (mGridView != null) {
			mGridView.setAdapter(mAdapter);
		}
		getAllBankuai();
	}

	public void onAction(View v) {
		// Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:// 返回
			finish();
			break;
		default:
			break;
		}
	}

	private void getAllBankuai() {
		mPullRefreshGridView.setRefreshing();
		requestNet(mHandler, null, NetworkUtil.GET_ALL_BANKUAI, false, 1);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {

		// TODO Auto-generated method stub
		String label = DateUtils.formatDateTime(getApplicationContext(),
				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);

		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
		if (!mIsRefreshing) {
			mIsRefreshing = true;
			getAllBankuai();
		} else {
			mPullRefreshGridView.onRefreshComplete();
		}
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
		// TODO Auto-generated method stub
		if (!mIsRefreshing) {
			mIsRefreshing = true;
			getAllBankuai();
		} else {
			mPullRefreshGridView.onRefreshComplete();
		}
	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub

	}

}
