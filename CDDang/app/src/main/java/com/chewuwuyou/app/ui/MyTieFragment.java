package com.chewuwuyou.app.ui;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.http.AjaxParams;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.TieAdapter;
import com.chewuwuyou.app.bean.TieItem;
import com.chewuwuyou.app.bean.ZanItem;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.widget.ActionSheet;
import com.chewuwuyou.app.widget.ActionSheet.MenuItemClickListener;
import com.chewuwuyou.app.widget.HackyViewPager;
import com.chewuwuyou.app.widget.PullToRefreshBase;
import com.chewuwuyou.app.widget.PullToRefreshBase.OnRefreshListener2;
import com.chewuwuyou.app.widget.PullToRefreshListView;

public class MyTieFragment extends BaseFragment implements
		OnRefreshListener2<ListView> {
	private Activity mActivity;
	private View mContentView;
	private List<TieItem> mData;
	private TieAdapter mAdapter;
	private PullToRefreshListView mPullToRefreshListView;
	private boolean mIsRefreshing = false;// 上拉下拉要用
	private int mCurcor;// 分页要用
	private RelativeLayout mContainer;
	private HackyViewPager mExpandedImageViewPager;
	private AlertDialog mDeleteTieDialog;
	private boolean mIsSetEmptyTV = false;
	// 处理消息
	public static final int TOGGLE_ZAN = 111;// 赞或者取消赞一个帖
	// public static final int PING_TIE = 112;// 评价一个帖

	// 在我的帖子中要用
	public static final int EDIT_TIE = 114;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			final TieItem tie;
			switch (msg.what) {
			case TOGGLE_ZAN:// 切换赞一个帖子
				tie = (TieItem) msg.obj;
				toggleZan(tie);
				break;
			case EDIT_TIE:
				tie = (TieItem) msg.obj;
				mActivity.setTheme(R.style.ActionSheetStyleIOS7);
				ActionSheet menuView = new ActionSheet(mActivity);
				menuView.setCancelButtonTitle("取 消");// before add items
				if (tie.getZiji().equals("1")) {// 自己发的帖子
					menuView.addItems("删除");
				} else {
					menuView.addItems("举报");
				}
				menuView.setItemClickListener(new MenuItemClickListener() {

					@Override
					public void onItemClick(int itemPosition) {
						// TODO Auto-generated method stub
						switch (itemPosition) {
						case 0:
							if (tie.getZiji().equals("1")) {// 自己发的帖子
								showDeleteTieDialog(tie);
							} else {
								final EditText editText = new EditText(
										mActivity);
								new AlertDialog.Builder(mActivity)
										.setTitle("请输入举报原因")
										.setView(editText)
										.setPositiveButton("确定",
												new OnClickListener() {

													@Override
													public void onClick(
															DialogInterface arg0,
															int arg1) {
														// TODO Auto-generated
														// method stub
														AjaxParams params = new AjaxParams();
														// 传入帖子id和uid
														params.put("type", "2");
														params.put(
																"relateId",
																String.valueOf(tie
																		.getId()));
														params.put(
																"reason",
																editText.getText()
																		.toString());

														requestNet(
																new Handler() {

																	@Override
																	public void handleMessage(
																			Message msg) {
																		// TODO
																		// Auto-generated
																		// method
																		// stub
																		super.handleMessage(msg);
																		switch (msg.what) {
																		case Constant.NET_DATA_SUCCESS:
																			Toast.makeText(
																					mActivity,
																					"已提交",
																					Toast.LENGTH_SHORT)
																					.show();

																			break;
																		case Constant.NET_DATA_FAIL:
																			Toast.makeText(
																					mActivity,
																					"举报失败，再试试",
																					Toast.LENGTH_SHORT)
																					.show();
																			break;
																		default:
																			break;
																		}

																	}
																},
																params,
																NetworkUtil.MAKE_ACCU,
																false, 0);
													}
												})
										.setNegativeButton("取消", null).show();
							}

							break;

						default:
							break;
						}
					}
				});
				menuView.setCancelableOnTouchMenuOutside(true);
				menuView.showMenu();
				break;
			default:
				break;
			}
		}
	};

	// 切换赞状态
	private void toggleZan(final TieItem tie) {

		AjaxParams params = new AjaxParams();
		// 传入帖子id和uid
		params.put("tieZiId", String.valueOf(tie.getId()));
		requestNet(new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:

					ZanItem tieZan = ZanItem.parse(msg.obj.toString());
					if (tieZan != null) {
						// 如果以前赞过，那现在取消赞
						if (tie.getZaned().equals("0")) {
							tie.setZaned(1 + "");
							tie.setTiezancnt(tie.getTiezancnt() + 1);
							Toast.makeText(mActivity, "点赞成功",
									Toast.LENGTH_SHORT).show();
						} else {
							tie.setZaned(0 + "");
							tie.setTiezancnt(tie.getTiezancnt() - 1);
							Toast.makeText(mActivity, "取消赞", Toast.LENGTH_SHORT)
									.show();
						}
					}
					mAdapter.notifyDataSetChanged();
					break;

				case Constant.NET_DATA_FAIL:
					// 提示切换赞失败
					Toast.makeText(mActivity, "失败，请重新尝试", Toast.LENGTH_SHORT)
							.show();
					break;
				default:
					break;
				}

			}
		}, params, NetworkUtil.TIE_TOGGLE_ZAN, false, 0);
	}

	// 删除一个帖子
	private void delTie(final TieItem tie) {
		// TODO Auto-generated method stub
		AjaxParams params = new AjaxParams();
		params.put("tieZiId", String.valueOf(tie.getId()));
		requestNet(new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:
					Toast.makeText(mActivity, "删除成功", Toast.LENGTH_SHORT)
							.show();
					// mData.remove(position);
					mData.remove(tie);
					mAdapter.notifyDataSetChanged();
					break;
				case Constant.NET_DATA_FAIL:
					Toast.makeText(mActivity, "删除失败", Toast.LENGTH_SHORT)
							.show();
					break;
				default:
					break;
				}
			}

		}, params, NetworkUtil.DEL_TIE, false, 0);

	}

	private void showDeleteTieDialog(final TieItem tie) {
		mDeleteTieDialog = new AlertDialog.Builder(mActivity).setTitle("确定删除")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						delTie(tie);
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).create();
		mDeleteTieDialog.setCanceledOnTouchOutside(true);
		mDeleteTieDialog.show();
	}

	public MyTieFragment() {
		super();
	}

	public static MyTieFragment newInstance(Activity activity) {
		MyTieFragment fragment = new MyTieFragment(activity);
		return fragment;
	}
	@SuppressLint("ValidFragment")
	public MyTieFragment(Activity activity) {
		MyLog.d("--------------------------------------TieFragment(Activity activity,  String type, String region)",
				"xuhan");
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		MyLog.d("xuhan", "--------------------------------------onCreateView");
		this.mContentView = LayoutInflater.from(mActivity).inflate(
				R.layout.my_tie_fragment_layout, null);

		initView();
		initData();
		initEvent();
		getAllTie(true);
		return mContentView;
	}

	private void getAllTie(final boolean refresh) {
		if (refresh) {
			mCurcor = 0;
		}
		AjaxParams params = new AjaxParams();
		mPullToRefreshListView.setRefreshing();

		params.put("start", String.valueOf(mCurcor));
		requestNet(new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);

				if (!mIsSetEmptyTV) {
					TextView tv = new TextView(mActivity);
					tv.setGravity(Gravity.CENTER);
					tv.setText("没有帖子");
					tv.setTextColor(getResources().getColor(
							R.color.empty_text_color));
					mPullToRefreshListView.setEmptyView(tv);
					mIsSetEmptyTV = true;
				}

				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:
					mIsRefreshing = false;
					mPullToRefreshListView.onRefreshComplete();
					List<TieItem> response = TieItem.parseList(msg.obj
							.toString());
					if (response != null) {
						if (refresh) {
							mPullToRefreshListView.onLoadMore();
							mData.clear();
						}
						mData.addAll(response);
						// mEmptyText.setVisibility(View.GONE);
						mAdapter.notifyDataSetChanged();
						mCurcor = mData.size();
						if (response.size() < Constant.TIE_PAGE_SIZE) {
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

		}, params, NetworkUtil.GET_MY_TIE, false, 1);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		String label = DateUtils.formatDateTime(
				mActivity.getApplicationContext(), System.currentTimeMillis(),
				DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);

		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

		if (!mIsRefreshing) {
			mIsRefreshing = true;
			getAllTie(true);
		} else {
			mPullToRefreshListView.onRefreshComplete();
		}
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		if (!mIsRefreshing) {
			mIsRefreshing = true;
			getAllTie(false);
		} else {
			mPullToRefreshListView.onRefreshComplete();
		}
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mPullToRefreshListView = (PullToRefreshListView) mContentView
				.findViewById(R.id.tie_list);
		mExpandedImageViewPager = (HackyViewPager) mContentView
				.findViewById(R.id.tie_expanded_image_viewpager);
		mContainer = (RelativeLayout) mContentView
				.findViewById(R.id.tie_container);

	}

	@Override
	protected void initData() {

		mData = new ArrayList<TieItem>();
		mAdapter = new TieAdapter(mActivity, mHandler, mData,
				mExpandedImageViewPager, mContainer,
				"com.chewuwuyou.app.my_tie");
		mPullToRefreshListView.setAdapter(mAdapter);
	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub
		mPullToRefreshListView.setOnRefreshListener(this);
		mPullToRefreshListView
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							final int arg2, long arg3) {
						Intent intent = new Intent(mActivity,
								TieDetailActivity.class);
						intent.putExtra("id", mData.get(arg2 - 1).getId());
						intent.putExtra("ziji", mData.get(arg2 - 1).getZiji());
						startActivity(intent);
					}
				});
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
		getAllTie(true);
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
		MyLog.d("xuhan",
				"--------------------------------------onViewStateRestored");
		// TODO Auto-generated method stub
		super.onViewStateRestored(savedInstanceState);
	}
}
