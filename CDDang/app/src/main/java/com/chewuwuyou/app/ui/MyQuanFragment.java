package com.chewuwuyou.app.ui;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.QuanThumbnailAdapter;
import com.chewuwuyou.app.bean.QuanItem;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.widget.PullToRefreshBase;
import com.chewuwuyou.app.widget.PullToRefreshBase.OnRefreshListener2;
import com.chewuwuyou.app.widget.PullToRefreshListView;

@SuppressLint("ValidFragment")
public final class MyQuanFragment extends BaseFragment implements OnRefreshListener2<ListView> {
	private Activity mActivity;
	private View mContentView;
	private List<QuanItem> mData;
	private QuanThumbnailAdapter mAdapter;
	private PullToRefreshListView mPullToRefreshListView;
	private boolean mIsRefreshing = false;// 上拉下拉要用
	private int mCurcor;// 分页要用

	private int mOtherId = 0;
	private String mBackImg;
	private String mHeadImg;
	private boolean mIsSetEmptyTV = false;

	// 处理消息
	public static final int SHOW_DEL_QUAN_DIALOG = 114;// 确定是否删除一个活动的dialog
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			QuanItem quan;
			switch (msg.what) {
			case SHOW_DEL_QUAN_DIALOG:
				quan = (QuanItem) msg.obj;
				showDeleteQuanDialog(quan);
				break;
			default:
				break;
			}
		}
	};

	public MyQuanFragment() {
		super();
	}

	public static MyQuanFragment newInstance(Activity activity) {
		MyQuanFragment fragment = new MyQuanFragment(activity);
		return fragment;
	}

	public MyQuanFragment(Activity activity) {
		MyLog.d("xuhan", "--------------------------------------QuanFragment(Activity activity,  String type, String region)");
		this.mActivity = activity;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		MyLog.d("xuhan", "--------------------------------------onAttach");
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		MyLog.d("xuhan", "--------------------------------------onCreate");
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		MyLog.d("xuhan", "--------------------------------------onCreateView");
		this.mContentView = LayoutInflater.from(mActivity).inflate(R.layout.quans_page_layout, null);

		initView();
		initData();
		initEvent();
		getAllQuan(true);
		return mContentView;
	}

	// 获得所有圈文
	private void getAllQuan(final boolean refresh) {
		if (refresh) {
			mCurcor = 0;
		}
		AjaxParams params = new AjaxParams();
		params.put("start", String.valueOf(mCurcor));
		if (mOtherId != 0) {
			params.put("otherId", String.valueOf(mOtherId));
		}
		mPullToRefreshListView.setRefreshing();

		requestNet(new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);

				
				if (!mIsSetEmptyTV) {
					TextView tv = new TextView(mActivity);
					tv.setGravity(Gravity.CENTER);
					tv.setText("没有动态");
					tv.setTextColor(getResources().getColor(R.color.empty_text_color));
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
						List<QuanItem> response = QuanItem.parseList(data.getString("quan").toString());
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
						// TODO Auto-generated catch block
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

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mPullToRefreshListView = (PullToRefreshListView) mContentView.findViewById(R.id.quan_list);

	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		mData = new ArrayList<QuanItem>();
		// String backImg, String personImg, int otherId) {
		mAdapter = new QuanThumbnailAdapter(mActivity, mHandler, mData, mBackImg, mHeadImg, mOtherId, "com.chewuwuyou.app.my_quan");
		mPullToRefreshListView.setAdapter(mAdapter);
	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub
		mPullToRefreshListView.setOnRefreshListener(this);
		mPullToRefreshListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
				Intent intent = new Intent(mActivity, QuanDetailActivity.class);
//				intent.putExtra("id", mData.get(arg2 - 1).getId());
//				intent.putExtra("ziji", mData.get(arg2 - 1).getZiji());
				Bundle b = new Bundle();
				b.putSerializable(Constant.QUAN_MSG_BUNDLE.KEY_QUAN, mData.get(arg2 - 1));
//				intent.putExtra("id", mData.get(arg2 - 1).getId());
				intent.putExtras(b);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		String label = DateUtils.formatDateTime(mActivity.getApplicationContext(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
				| DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

		if (!mIsRefreshing) {
			mIsRefreshing = true;
			getAllQuan(true);
		} else {
			mPullToRefreshListView.onRefreshComplete();
		}
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		if (!mIsRefreshing) {
			mIsRefreshing = true;
			getAllQuan(false);
		} else {
			mPullToRefreshListView.onRefreshComplete();
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		MyLog.d("xuhan", "--------------------------------------onDestroy");
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		MyLog.d("xuhan", "--------------------------------------onDestroyView");
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		MyLog.d("xuhan", "--------------------------------------onDetach");
		super.onDetach();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		MyLog.d("xuhan", "--------------------------------------onPause");
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		MyLog.d("xuhan", "--------------------------------------onResume");
		super.onResume();
		getAllQuan(true);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		MyLog.d("xuhan", "--------------------------------------onStart");
		super.onStart();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		MyLog.d("xuhan", "--------------------------------------onStop");
		super.onStop();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		MyLog.d("xuhan", "--------------------------------------onViewCreated");
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		MyLog.d("xuhan", "--------------------------------------onViewStateRestored");
		// TODO Auto-generated method stub
		super.onViewStateRestored(savedInstanceState);
	}

	private AlertDialog mDeleteQuanDialog;// 删圈提示

	private void showDeleteQuanDialog(final QuanItem quan) {
		mDeleteQuanDialog = new AlertDialog.Builder(mActivity).setTitle("确定删除").setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				delQuan(quan);
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).create();
		mDeleteQuanDialog.setCanceledOnTouchOutside(true);
		mDeleteQuanDialog.show();
	}

	private void delQuan(final QuanItem quan) {
		// TODO Auto-generated method stub
		AjaxParams params = new AjaxParams();
		params.put("quanWenId", String.valueOf(quan.getId()));
		requestNet(new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:
					Toast.makeText(mActivity, "删除成功", Toast.LENGTH_SHORT).show();
					mData.remove(quan);
					mAdapter.notifyDataSetChanged();
					break;
				case Constant.NET_DATA_FAIL:
					Toast.makeText(mActivity, "删除失败", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
			}

		}, params, NetworkUtil.DEL_QUAN, false, 0);

	}
}
