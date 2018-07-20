package com.chewuwuyou.app.ui;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

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
import com.chewuwuyou.app.adapter.YueAdapter;
import com.chewuwuyou.app.bean.YueItem;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.widget.HackyViewPager;
import com.chewuwuyou.app.widget.PullToRefreshBase;
import com.chewuwuyou.app.widget.PullToRefreshBase.OnRefreshListener2;
import com.chewuwuyou.app.widget.PullToRefreshListView;

public final class OtherYueActivity extends CDDBaseActivity implements OnRefreshListener2<ListView> {
	// header bar
	@ViewInject(id = R.id.sub_header_bar_left_ibtn, click = "onAction")
	private ImageButton mBackBtn;
	@ViewInject(id = R.id.sub_header_bar_tv)
	private TextView mHeaderTV;
	private List<YueItem> mData;
	private YueAdapter mAdapter;
	private PullToRefreshListView mPullToRefreshListView;
	private boolean mIsRefreshing = false;// 上拉下拉要用
	private int mCurcor;// 分页要用
	private RelativeLayout mTitleHeight;//标题布局高度
	private RelativeLayout mContainer;
	private HackyViewPager mExpandedImageViewPager;

	// 数据展示
	private String mOtherId = null;
	private String mOtherName = null;
	private boolean mIsSetEmptyTV = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_yue_ac);
		initView();
		initData();
		initEvent();
		getAllYue(true);
	}

	private void getAllYue(final boolean refresh) {
		if (refresh) {
			mCurcor = 0;
		}
		mPullToRefreshListView.setRefreshing();
		AjaxParams params = new AjaxParams();
		params.put("otherId", mOtherId);
		params.put("start", String.valueOf(mCurcor));
		requestNet(new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				
				if (!mIsSetEmptyTV) {
					TextView tv = new TextView(OtherYueActivity.this);
					tv.setGravity(Gravity.CENTER);
					tv.setText("没有活动");
					tv.setTextColor(getResources().getColor(R.color.empty_text_color));
					mPullToRefreshListView.setEmptyView(tv);
					mIsSetEmptyTV = true;
				}
				
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:
					mIsRefreshing = false;
					mPullToRefreshListView.onRefreshComplete();

					List<YueItem> response = null;
					try {
						JSONObject data = new JSONObject(msg.obj.toString());
						response = YueItem.parseList(data.getString("yue").toString());
					} catch (JSONException e) {
						e.printStackTrace();
					}

					if (response != null) {
						if (refresh) {
							mPullToRefreshListView.onLoadMore();
							mData.clear();
						}
						mData.addAll(response);
						mAdapter.notifyDataSetChanged();
						mCurcor = mData.size();
						if (response.size() < Constant.QUAN_PAGE_SIZE) {
							mPullToRefreshListView.onLoadComplete();
						}
					}
					break;

				default:
					mPullToRefreshListView.onRefreshComplete();
					mIsRefreshing = false;
					break;
				}
			}

		}, params, NetworkUtil.GET_MY_YUE, false, 1);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void initView() {
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.yue_list);
		mExpandedImageViewPager = (HackyViewPager) findViewById(R.id.yue_expanded_image_viewpager);
		mContainer = (RelativeLayout) findViewById(R.id.yue_container);
		mHeaderTV.setText(new StringBuilder().append(mOtherName).append("的活动"));

	}

	public void onAction(View v) {
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
	protected void initData() {
		mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
		mOtherId = getIntent().getStringExtra("other_id");
		mOtherName = getIntent().getStringExtra("other_name");
		mHeaderTV.setText(new StringBuilder().append(mOtherName).append("活动"));
		mData = new ArrayList<YueItem>();
		mAdapter = new YueAdapter(OtherYueActivity.this, mData, mExpandedImageViewPager, mContainer);
		mPullToRefreshListView.setAdapter(mAdapter);
	}

	@Override
	protected void initEvent() {
		mPullToRefreshListView.setOnRefreshListener(this);
		mPullToRefreshListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
				Intent intent = new Intent(OtherYueActivity.this, YueDetailActivity.class);
				intent.putExtra("id", mData.get(arg2 - 1).getId());
				intent.putExtra("ziji", mData.get(arg2 - 1).getZiji());
				startActivity(intent);
			}
		});
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
				| DateUtils.FORMAT_ABBREV_ALL);

		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

		if (!mIsRefreshing) {
			mIsRefreshing = true;
			getAllYue(true);
		} else {
			mPullToRefreshListView.onRefreshComplete();
		}
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!mIsRefreshing) {
			mIsRefreshing = true;
			getAllYue(false);
		} else {
			mPullToRefreshListView.onRefreshComplete();
		}
	}

	@Override
	public void onResume() {
		MyLog.d("xuhan", "--------------------------------------onResume");
		super.onResume();
		getAllYue(true);
	}
	
}
