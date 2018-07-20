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
import com.chewuwuyou.app.adapter.QuanThumbnailAdapter;
import com.chewuwuyou.app.bean.QuanItem;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.widget.PullToRefreshBase;
import com.chewuwuyou.app.widget.PullToRefreshBase.OnRefreshListener2;
import com.chewuwuyou.app.widget.PullToRefreshListView;

public class QuanThumbnailActivity extends BaseActivity implements
		OnRefreshListener2<ListView> {

	// header bar
	@ViewInject(id = R.id.sub_header_bar_left_ibtn, click = "onAction")
	private ImageButton mBackIBtn;// 左键,返回按钮
	@ViewInject(id = R.id.sub_header_bar_tv)
	private TextView mHeaderTV;// 标题

	private List<QuanItem> mData;// 数据
	private QuanThumbnailAdapter mAdapter;
	private PullToRefreshListView mPullToRefreshListView;

	private RelativeLayout mTitleHeight;// 标题布局高度
	private boolean mIsRefreshing = false;// 翻页要用

	private int mOtherId = 0;
	private String mOtherName = null;
	private int mCurcor;
	private String mBackImg;
	private String mZiji;
	private String mHeadImg;
	private boolean mIsSetEmptyTV = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quan_thumbnail_ac);
		mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);// 根据不同手机判断
		mOtherId = getIntent().getIntExtra("other_id", 0);
		mZiji = getIntent().getStringExtra("ziji");
		if (mZiji.equals("1")) {
			mOtherName = "我";
		} else {
			mOtherName = getIntent().getStringExtra("other_name");
		}
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.quan_thumbnail_list);

		mHeaderTV.setText(new StringBuilder().append(mOtherName).append("的心情"));
		mData = new ArrayList<QuanItem>();
		// String backImg, String personImg, int otherId) {
		mAdapter = new QuanThumbnailAdapter(this, mData,
				mBackImg, mHeadImg, mOtherId);
		mPullToRefreshListView.setAdapter(mAdapter);

		mPullToRefreshListView.setOnRefreshListener(this);
		mPullToRefreshListView
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Intent intent = new Intent(QuanThumbnailActivity.this,
								QuanDetailActivity.class);
						Bundle b = new Bundle();
						b.putSerializable(Constant.QUAN_MSG_BUNDLE.KEY_QUAN,
								mData.get(arg2 - 1));
						// intent.putExtra("id", mData.get(arg2 - 1).getId());
						intent.putExtras(b);
						startActivity(intent);
					}
				});

		getAllQuan(true);
	}

	// 获得所有圈文
	private void getAllQuan(final boolean refresh) {
		if (refresh) {
			mCurcor = 0;
		}
		AjaxParams params = new AjaxParams();
		params.put("start", String.valueOf(mCurcor));
		if (mZiji.equals("0")) {
			params.put("otherId", String.valueOf(mOtherId));
		}

		mPullToRefreshListView.setRefreshing();

		requestNet(new Handler() {

			@Override
			public void handleMessage(Message msg) {

				super.handleMessage(msg);

				if (!mIsSetEmptyTV) {
					TextView tv = new TextView(QuanThumbnailActivity.this);
					tv.setGravity(Gravity.CENTER);
					tv.setText("没有动态");
					tv.setTextColor(getResources().getColor(
							R.color.empty_text_color));
					mPullToRefreshListView.setEmptyView(tv);
					mIsSetEmptyTV = true;
				}

				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:
					mIsRefreshing = false;
					mPullToRefreshListView.onRefreshComplete();
					try {
						JSONObject data = new JSONObject(msg.obj.toString());
						mBackImg = data.getString("imageBack");
						mHeadImg = data.getString("headImg");
						List<QuanItem> response = QuanItem.parseList(data
								.getString("quan").toString());
						if (response != null) {
							if (refresh) {
								mPullToRefreshListView.onLoadMore();
								mData.clear();
							}
							mData.addAll(response);
							mAdapter.setmBackImg(mBackImg);
							mAdapter.setmHeadImg(mHeadImg);
							mAdapter.notifyDataSetChanged();
							mCurcor = mData.size();
							if (response.size() < Constant.QUAN_PAGE_SIZE) {
								mPullToRefreshListView.onLoadComplete();
							}
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}

					break;

				default:
					mPullToRefreshListView.onRefreshComplete();
					mIsRefreshing = false;
					break;
				}
			}

		}, params, NetworkUtil.GET_MY_QUAN, false, 1);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void onPullDownToRefresh(PullToRefreshBase refreshView) {
		String label = DateUtils.formatDateTime(getApplicationContext(),
				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);

		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

		if (!mIsRefreshing) {
			mIsRefreshing = true;
			getAllQuan(true);
		} else {
			mPullToRefreshListView.onRefreshComplete();
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void onPullUpToRefresh(PullToRefreshBase refreshView) {
		if (!mIsRefreshing) {
			mIsRefreshing = true;
			getAllQuan(false);
		} else {
			mPullToRefreshListView.onRefreshComplete();
		}
	}

	public void onAction(View v) {
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			finishActivity();
			break;
		default:
			break;
		}
	}

}
