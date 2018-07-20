package com.chewuwuyou.app.ui;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxParams;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.ZanOrInvolverAdapter;
import com.chewuwuyou.app.bean.TieDetailHeaderEntity;
import com.chewuwuyou.app.bean.YueDetailHeaderEntity;
import com.chewuwuyou.app.bean.YueInvolverItem;
import com.chewuwuyou.app.bean.ZanItem;
import com.chewuwuyou.app.bean.ZanerOrInvolverItem;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.widget.PullToRefreshBase;
import com.chewuwuyou.app.widget.PullToRefreshBase.OnRefreshListener2;
import com.chewuwuyou.app.widget.PullToRefreshListView;

public class ZanOrInvolverActivity extends BaseActivity implements OnRefreshListener2<ListView> {

	// header bar
	@ViewInject(id = R.id.sub_header_bar_left_ibtn, click = "onAction")
	private ImageButton mBackIBtn;// 左键,返回按钮
	@ViewInject(id = R.id.sub_header_bar_tv)
	private TextView mHeaderTV;// 标题 点赞的车友
	private List<ZanItem> mInitialZanData = new ArrayList<ZanItem>();
	private List<YueInvolverItem> mInitialInvolverData = new ArrayList<YueInvolverItem>();
	private List<ZanerOrInvolverItem> mData;
	private ZanOrInvolverAdapter mAdapter;
	private PullToRefreshListView mPullToRefreshListView;
	private int mCurcor;// 翻页要用
	private boolean mIsRefreshing = false;// 翻页要用
	private String mAction;
	private boolean mIsSetEmptyTV = false;
	private RelativeLayout mTitleHeight;// 标题布局高度

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zan_list_ac);
		mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);// 根据不同手机判断
		mAction = getIntent().getAction();
		if (mAction != null && mAction.equals("com.chewuwuyou.app.show_yue_involver")) {
			YueDetailHeaderEntity yueDetailHeader = (YueDetailHeaderEntity) getIntent()
					.getSerializableExtra("yue_detail_header");
			mHeaderTV.setText("感兴趣的车友");
			mInitialInvolverData = yueDetailHeader.getInvolve();
		} else if (mAction != null && mAction.equals("com.chewuwuyou.app.show_yue_zaner")) {
			YueDetailHeaderEntity yueDetailHeader = (YueDetailHeaderEntity) getIntent()
					.getSerializableExtra("yue_detail_header");
			mHeaderTV.setText("点赞的车友");
			mInitialZanData = yueDetailHeader.getYuezan();
		} else if (mAction != null && mAction.equals("com.chewuwuyou.app.show_tie_zaner")) {
			TieDetailHeaderEntity tieDetailHeader = (TieDetailHeaderEntity) getIntent()
					.getSerializableExtra("tie_detail_header");
			mHeaderTV.setText("点赞的车友");
			mInitialZanData = tieDetailHeader.getTiezan();
		} else if (mAction != null && mAction.equals("com.chewuwuyou.app.show_hot_tie_zaner")) {
			TieDetailHeaderEntity tieDetailHeader = (TieDetailHeaderEntity) getIntent()
					.getSerializableExtra("tie_detail_header");
			mHeaderTV.setText("点赞的车友");
			mInitialZanData = tieDetailHeader.getTiezan();
		}
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.zan_lv);
		mData = new ArrayList<ZanerOrInvolverItem>();
		if (mAdapter == null) {
			mAdapter = new ZanOrInvolverAdapter(ZanOrInvolverActivity.this, mData);
		}
		mPullToRefreshListView.setAdapter(mAdapter);
		if (!mInitialInvolverData.isEmpty()) {
			getInvolverList(true);
		} else {
			getZanerList(true);
		}
		mPullToRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ZanOrInvolverActivity.this, PersonalHomeActivity2.class);
				intent.putExtra("userId", Integer.parseInt(mData.get(position - 1).getId()));
				startActivity(intent);
			}
		});
	}

	private void getZanerList(final boolean refresh) {
		// TODO Auto-generated method stub
		if (refresh) {
			mCurcor = 0;
		}
		mPullToRefreshListView.setRefreshing();
		AjaxParams params = new AjaxParams();

		StringBuffer sbf = new StringBuffer();
		if (mInitialZanData.size() > mCurcor + 20) {
			// 超过20个
			for (int i = mCurcor; i < 20; i++) {
				sbf.append(mInitialZanData.get(i).getFriendId()).append("-");
			}
		} else {
			int size = mInitialZanData.size();
			if (size > mCurcor) {
				for (int i = mCurcor; i < size; i++) {
					sbf.append(mInitialZanData.get(i).getFriendId()).append("-");
				}
			}

		}
		sbf.deleteCharAt(sbf.length() - 1);
		params.put("ids", sbf.toString());
		requestNet(new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);

				if (!mIsSetEmptyTV) {
					TextView tv = new TextView(ZanOrInvolverActivity.this);
					tv.setGravity(Gravity.CENTER);
					tv.setText("没有帖子");
					tv.setTextColor(getResources().getColor(R.color.empty_text_color));
					mPullToRefreshListView.setEmptyView(tv);
					mIsSetEmptyTV = true;
				}

				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:
					mIsRefreshing = false;
					mPullToRefreshListView.onRefreshComplete();

					List<ZanerOrInvolverItem> response = ZanerOrInvolverItem.parseList(msg.obj.toString());
					if (response != null) {
						// mEmptyTV.setVisibility(View.GONE);
						if (refresh) {
							mPullToRefreshListView.onLoadMore();
							mData.clear();
						}
						mData.addAll(response);
						// mEmptyText.setVisibility(View.GONE);
						mAdapter.notifyDataSetChanged();
						mCurcor = mData.size();
						if (response.size() < 20) {
							mPullToRefreshListView.onLoadComplete();
						}
					}
					break;

				default:
					mIsRefreshing = false;
					break;
				}
			}

		}, params, NetworkUtil.GET_CHAT_USER_INFO, false, 1);

	}

	private void getInvolverList(final boolean refresh) {
		// TODO Auto-generated method stub
		if (refresh) {
			mCurcor = 0;
		}
		mPullToRefreshListView.setRefreshing();
		AjaxParams params = new AjaxParams();

		StringBuffer sbf = new StringBuffer();
		if (mInitialInvolverData.size() > mCurcor + 20) {
			// 超过20个
			for (int i = mCurcor; i < 20; i++) {
				sbf.append(mInitialInvolverData.get(i).getId()).append("-");
			}
		} else {
			int size = mInitialInvolverData.size();
			if (size > mCurcor) {
				for (int i = mCurcor; i < size; i++) {
					sbf.append(mInitialInvolverData.get(i).getId()).append("-");
				}
			}

		}
		sbf.deleteCharAt(sbf.length() - 1);
		params.put("ids", sbf.toString());
		requestNet(new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);

				if (!mIsSetEmptyTV) {
					TextView tv = new TextView(ZanOrInvolverActivity.this);
					tv.setGravity(Gravity.CENTER);
					tv.setText("没有帖子");
					tv.setTextColor(getResources().getColor(R.color.empty_text_color));
					mPullToRefreshListView.setEmptyView(tv);
					mIsSetEmptyTV = true;
				}

				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:
					mIsRefreshing = false;
					mPullToRefreshListView.onRefreshComplete();

					List<ZanerOrInvolverItem> response = ZanerOrInvolverItem.parseList(msg.obj.toString());
					if (response != null) {
						// mEmptyTV.setVisibility(View.GONE);
						if (refresh) {
							mPullToRefreshListView.onLoadMore();
							mData.clear();
						}
						mData.addAll(response);
						// mEmptyText.setVisibility(View.GONE);
						mAdapter.notifyDataSetChanged();
						mCurcor = mData.size();
						if (response.size() < 20) {
							mPullToRefreshListView.onLoadComplete();
						}
					}
					break;

				default:
					mIsRefreshing = false;
					break;
				}
			}

		}, params, NetworkUtil.GET_CHAT_USER_INFO, false, 1);

	}

	public void onAction(View v) {
		// Intent intent = new Intent();
		switch (v.getId()) {
		// 退出
		case R.id.sub_header_bar_left_ibtn:
			finish();
			break;
		default:
			break;

		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
				DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

		if (!mIsRefreshing) {
			mIsRefreshing = true;

			if (!mInitialInvolverData.isEmpty()) {
				getInvolverList(true);
			} else {
				getZanerList(true);
			}
		} else {
			mPullToRefreshListView.onRefreshComplete();
		}
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		if (!mIsRefreshing) {
			mIsRefreshing = true;
			if (!mInitialInvolverData.isEmpty()) {
				getInvolverList(false);
			} else {
				getZanerList(false);
			}
		} else {
			mPullToRefreshListView.onRefreshComplete();
		}
	}
}
