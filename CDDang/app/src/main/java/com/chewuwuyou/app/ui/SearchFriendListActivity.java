package com.chewuwuyou.app.ui;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.SearchFriendAdapter;
import com.chewuwuyou.app.bean.DataError;
import com.chewuwuyou.app.bean.SearchFriend;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.PullToRefreshBase;
import com.chewuwuyou.app.widget.PullToRefreshBase.OnRefreshListener2;
import com.chewuwuyou.app.widget.PullToRefreshListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:
 * @author:yuyong
 * @date:2015-8-18下午8:09:36
 * @version:1.2.1
 */
public class SearchFriendListActivity extends CDDBaseActivity implements
		OnRefreshListener2<ListView> {

	private TextView mTitleTV;
	private ImageButton mBackIBtn;
	private PullToRefreshListView mPullToRefreshListView;
	private List<SearchFriend> mData = new ArrayList<SearchFriend>();
	private String mSearchStr = "";
	private SearchFriendAdapter mAdapter;
	private boolean mIsRefreshing = false;
	private RelativeLayout mTitleHeight;//标题布局高度
	private List<String> mIdData;// 第一次从服务器获取
	private int mCurcor = 0;// 第几次请求
	private boolean mIsSetEmptyTV = false;

	private JSONObject jo;
	private List<SearchFriend> mRefreshData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_friend_list_ac);
		initView();
		initData();
		initEvent();
	}

	@Override
	protected void initView() {
		mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
		mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.search_friend_list);
	}

	@Override
	protected void initData() {
		mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
		mTitleTV.setText("添加车友");
		if (mAdapter == null) {
			mAdapter = new SearchFriendAdapter(SearchFriendListActivity.this,
					mData);
		}
		mPullToRefreshListView.setAdapter(mAdapter);
		mSearchStr = getIntent().getStringExtra("searchStr");
		getData(true);
	}

	@Override
	protected void initEvent() {
		mPullToRefreshListView.setOnRefreshListener(this);
		mBackIBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finishActivity();
			}
		});
	}

	private void getData(final boolean isRefresh) {
		if (isRefresh) {
			mCurcor = 0;
		}
		AjaxParams params = new AjaxParams();
		params.put("nick", mSearchStr);
		if (!isRefresh) {
			StringBuffer sbf = new StringBuffer();
			if (mIdData.size() >= mCurcor + 20) {
				// 超过20个
				int size = mCurcor + 20;
				for (int i = mCurcor; i < size; i++) {
					sbf.append(mIdData.get(i)).append("-");
				}
			} else {
				int size = mIdData.size();
				if (size > mCurcor) {
					for (int i = mCurcor; i < size; i++) {
						sbf.append(mIdData.get(i)).append("-");
					}
				}
			}
			if (sbf.length() == 0) {
				mPullToRefreshListView.onLoadComplete();
				ToastUtil.toastShow(SearchFriendListActivity.this, "没有更多数据了。。");
				return;
			}
			sbf.deleteCharAt(sbf.length() - 1);
			params.put("ids", sbf.toString());
		}
		mPullToRefreshListView.setRefreshing();
		requestNet(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg != null && msg.obj != null) {
					MyLog.i("YUY", "查找好友信息 = " + msg.obj.toString());
				}
				if (!mIsSetEmptyTV) {
					TextView tv = new TextView(SearchFriendListActivity.this);
					tv.setGravity(Gravity.CENTER);
					tv.setText("没有好友");
					tv.setTextColor(getResources().getColor(
							R.color.empty_text_color));
					mPullToRefreshListView.setEmptyView(tv);
					mIsSetEmptyTV = true;
				}
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:
					mIsRefreshing = false;
					mPullToRefreshListView.onRefreshComplete();
					MyLog.i("YUY", "加载更多查询的车友 = " + msg.obj.toString());
					try {
						if (isRefresh) {
							jo = new JSONObject(msg.obj.toString());
							mRefreshData = SearchFriend.parseList(jo
									.getJSONArray("info").toString());
							mIdData = parseAllIds(jo.getJSONArray("ids")
									.toString());
						} else {
							mRefreshData = SearchFriend.parseList(jo
									.getJSONArray("info").toString());
						}

					} catch (JSONException e) {
						mRefreshData.clear();
						e.printStackTrace();

					}
					if (isRefresh) {
						mPullToRefreshListView.onLoadMore();
						mData.clear();
					} else {
						// 是加载后面的数据
						if (mRefreshData == null) {
							ToastUtil.toastShow(SearchFriendListActivity.this,
									"没有更多数据了");
							return;
						}
					}
					mData.addAll(mRefreshData);
					mAdapter.updateData(mData);
					mAdapter.notifyDataSetChanged();
					mCurcor = mData.size();
					if (mRefreshData.size() < 20 || mCurcor == mIdData.size()) {
						mPullToRefreshListView.onLoadComplete();
					}
					break;
				case Constant.NET_DATA_FAIL:
					ToastUtil.toastShow(SearchFriendListActivity.this,
							((DataError) msg.obj).getErrorMessage());
					break;
				default:
					mPullToRefreshListView.onRefreshComplete();
					mIsRefreshing = false;
					break;
				}
			}
		}, params, NetworkUtil.GET_CONTACTS_FRIEND, false, 1);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		String label = DateUtils.formatDateTime(getApplicationContext(),
				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);

		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
		if (!mIsRefreshing) {
			mIsRefreshing = true;
			getData(true);
		} else {
			mPullToRefreshListView.onRefreshComplete();
		}
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!mIsRefreshing) {
			mIsRefreshing = true;
			getData(false);
		} else {
			mPullToRefreshListView.onRefreshComplete();
		}
	}

	private List<String> parseAllIds(String idsJson) {
		Gson g = new Gson();
		List<String> nearByFriendAllIds = g.fromJson(idsJson,
				new TypeToken<List<String>>() {
				}.getType());
		return nearByFriendAllIds;
	}
}
