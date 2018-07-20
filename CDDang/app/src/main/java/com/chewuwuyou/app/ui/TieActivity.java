package com.chewuwuyou.app.ui;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.TieAdapter;
import com.chewuwuyou.app.bean.TieItem;
import com.chewuwuyou.app.bean.ZanItem;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.widget.ActionSheet;
import com.chewuwuyou.app.widget.ActionSheet.MenuItemClickListener;
import com.chewuwuyou.app.widget.HackyViewPager;
import com.chewuwuyou.app.widget.PullToRefreshBase;
import com.chewuwuyou.app.widget.PullToRefreshBase.OnRefreshListener2;
import com.chewuwuyou.app.widget.PullToRefreshListView;

/**
 * @describe:帖的AC
 * @author:XH
 * @version
 * @created:
 */
public class TieActivity extends CDDBaseActivity implements OnRefreshListener2<ListView> {

	// header bar
	@ViewInject(id = R.id.sub_header_bar_left_ibtn, click = "onAction")
	private ImageButton mBackIBtn;// 左键,返回按钮
	@ViewInject(id = R.id.sub_header_bar_tv)
	private TextView mHeaderTV;// 标题
	@ViewInject(id = R.id.sub_header_bar_right_ibtn, click = "onAction")
	private ImageButton mHeaderBarRightIBtn;// 右键,新增一个帖

	private AlertDialog mDeleteTieDialog;
	private RelativeLayout mTitleHeight;//标题布局高度
	// 展示列表
	// 数据展示
	private String mOtherId = null;
	private String mOtherName = null;

	private int mBanId;// 所在板块ID
	private String mBanTitle;// 所在板块标题
	private String mBanUrl;// 所在板块图
	private int mTodayNum;// 今日话题
	private List<TieItem> mData;// 数据
	private TieAdapter mAdapter;
	private PullToRefreshListView mPullToRefreshListView;
	private int mCurcor;// 翻页要用
	private boolean mIsRefreshing = false;// 翻页要用

	// 显示大图时需要这俩成员变量
	@ViewInject(id = R.id.tie_container)
	private RelativeLayout mContainer;// 点击显示大图需要这个
	private HackyViewPager mExpandedImageViewPager;// 点击显示大图时可以滑动看其它的图

	// 处理消息
	public static final int TOGGLE_ZAN = 111;// 赞或者取消赞一个帖
//	public static final int PING_TIE = 112;// 评价一个帖

	// 在我的帖子中要用
	public static final int EDIT_TIE = 114;
	private boolean mIsSetEmptyTV = false;
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
				setTheme(R.style.ActionSheetStyleIOS7);
				ActionSheet menuView = new ActionSheet(TieActivity.this);
				menuView.setCancelButtonTitle("取 消");// before add items
				if (tie.getZiji().equals("1")) {// 自己发的帖子
					menuView.addItems("删除");
				} else {
					menuView.addItems("举报");
				}

				menuView.setItemClickListener(new MenuItemClickListener() {

					@Override
					public void onItemClick(int itemPosition) {
						switch (itemPosition) {
						case 0:
							if (tie.getZiji().equals("1")) {// 自己发的帖子
								showDeleteTieDialog(tie);
							} else {
								final EditText editText = new EditText(TieActivity.this);
								new AlertDialog.Builder(TieActivity.this).setTitle("请输入举报原因").setView(editText).setPositiveButton("确定", new OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										AjaxParams params = new AjaxParams();
										// 传入帖子id和uid
										params.put("type", "2");
										params.put("relateId", String.valueOf(tie.getId()));
										params.put("reason", editText.getText().toString());

										requestNet(new Handler() {

											@Override
											public void handleMessage(Message msg) {
												// stub
												super.handleMessage(msg);
												switch (msg.what) {
												case Constant.NET_DATA_SUCCESS:
													Toast.makeText(TieActivity.this, "已提交", Toast.LENGTH_SHORT).show();

													break;
												case Constant.NET_DATA_FAIL:
													Toast.makeText(TieActivity.this, "举报失败，再试试", Toast.LENGTH_SHORT).show();
													break;
												default:
													break;
												}

											}
										}, params, NetworkUtil.MAKE_ACCU, false, 0);
									}
								}).setNegativeButton("取消", null).show();
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tie_layout);
		mOtherId = getIntent().getStringExtra("other_id");
		mOtherName = getIntent().getStringExtra("other_name");
		initView();
		initData();
		initEvent();
		getAllTie(true);
	}

	@Override
	protected void initView() {
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.tie_lv);
		mExpandedImageViewPager = (HackyViewPager) findViewById(R.id.tie_expanded_image_viewpager);
	}

	@Override
	protected void initData() {
		mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
		if (mOtherName != null) {
			mHeaderTV.setText(new StringBuilder().append(mOtherName).append("的话题"));
		} else {
			mBanId = getIntent().getIntExtra("banId", 0);
			mBanTitle = getIntent().getStringExtra("banTitle");
			mBanUrl = getIntent().getStringExtra("banUrl");
			mHeaderTV.setText(mBanTitle);
			mHeaderBarRightIBtn.setVisibility(View.VISIBLE);
			mHeaderBarRightIBtn.setImageResource(R.drawable.btn_add_quan_or_tie);
		}


		if (mData == null) {
			mData = new ArrayList<TieItem>();
		}
		if (mAdapter == null) {
			if (mOtherId != null) {
				mAdapter = new TieAdapter(TieActivity.this, mHandler, mData, mExpandedImageViewPager, mContainer, "com.chewuwuyou.app.other_tie");
			} else {
				mAdapter = new TieAdapter(TieActivity.this, mHandler, mData, mExpandedImageViewPager, mContainer, mBanUrl, mTodayNum);
			}
		}
		if (mPullToRefreshListView != null) {
			mPullToRefreshListView.setAdapter(mAdapter);
		}

	}

	@Override
	protected void initEvent() {
		mPullToRefreshListView.setOnRefreshListener(this);
		mPullToRefreshListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
				Intent intent = null;
				if (mData.get(arg2 - 1).getReuse() != null && mData.get(arg2 - 1).getReuse().equals("1")) {
					intent = new Intent(TieActivity.this, HotTieDetailActivity.class);
				} else {
					intent = new Intent(TieActivity.this, TieDetailActivity.class);
				}

				intent.putExtra("id", mData.get(arg2 - 1).getId());
				intent.putExtra("ziji", mData.get(arg2 - 1).getZiji());
				startActivity(intent);
			}

		});

	}

	public void onAction(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		// 退出
		case R.id.sub_header_bar_left_ibtn:
			finish();
			break;
		// 新建一个心情
		case R.id.sub_header_bar_right_ibtn:
			if (CacheTools.getUserData("userId") != null && Integer.parseInt(CacheTools.getUserData("userId")) > 0) {
				intent.setClass(TieActivity.this, AddTieActivity.class);
				intent.putExtra("banId", mBanId);
				startActivity(intent);
			} else {
				// 跳到登录界面
				intent = new Intent(TieActivity.this, LoginActivity.class);
				startActivity(intent);
				finishActivity();
			}
			break;

		default:
			break;
		}
	}

	// 切换赞状态
	private void toggleZan(final TieItem tie) {
		AjaxParams params = new AjaxParams();
		// 传入帖子id和uid
		params.put("tieZiId", String.valueOf(tie.getId()));
		requestNet(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:

					ZanItem tieZan = ZanItem.parse(msg.obj.toString());
					if (tieZan != null) {
						// 如果以前赞过，那现在取消赞
						if (tie.getZaned().equals("0")) {
							tie.setZaned(1 + "");
							tie.setTiezancnt(tie.getTiezancnt() + 1);
							Toast.makeText(TieActivity.this, "点赞成功", Toast.LENGTH_SHORT).show();
						} else {
							tie.setZaned(0 + "");
							tie.setTiezancnt(tie.getTiezancnt() - 1);
							Toast.makeText(TieActivity.this, "取消赞", Toast.LENGTH_SHORT).show();
						}
					}
					mAdapter.notifyDataSetChanged();
					break;

				case Constant.NET_DATA_FAIL:
					// 提示切换赞失败
					Toast.makeText(TieActivity.this, "失败，请重新尝试", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
			}
		}, params, NetworkUtil.TIE_TOGGLE_ZAN, false, 0);
	}
	// 获得所有帖子
	private void getAllTie(final boolean refresh) {
		String request_url;
		if (refresh) {
			mCurcor = 0;
		}
		mPullToRefreshListView.setRefreshing();
		AjaxParams params = new AjaxParams();
		params.put("start", String.valueOf(mCurcor));

		if (mOtherId != null) {
			params.put("otherId", mOtherId);
			request_url = NetworkUtil.GET_MY_TIE;
		} else {
			params.put("banId", String.valueOf(mBanId));
			request_url = NetworkUtil.ALL_TIE;
		}

		requestNet(new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				if (!mIsSetEmptyTV) {
					TextView tv = new TextView(TieActivity.this);
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

					if (mOtherId != null) {
						List<TieItem> response = TieItem.parseList(msg.obj.toString());
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
							if (response.size() < Constant.TIE_PAGE_SIZE) {
								mPullToRefreshListView.onLoadComplete();
							}
						} else {
							// if (refresh) {
							// mEmptyTV.setVisibility(View.VISIBLE);
							// }
						}
					} else {
						try {
							JSONObject data = new JSONObject(msg.obj.toString());
							mTodayNum = data.getInt("today");
							mAdapter.setmTodayNum(mTodayNum);
							List<TieItem> response = TieItem.parseList(data.getString("tie").toString());
							if (response != null) {
								// mEmptyTV.setVisibility(View.GONE);
								if (refresh) {
									mPullToRefreshListView.onLoadMore();
									mData.clear();
								}
								mData.addAll(response);
								// mEmptyText.setVisibility(View.GONE);
								mCurcor = mData.size();
								if (response.size() < Constant.TIE_PAGE_SIZE) {
									mPullToRefreshListView.onLoadComplete();
								}
							} else {
								// if (refresh) {
								// mEmptyTV.setVisibility(View.VISIBLE);
								// }
							}
							mAdapter.notifyDataSetChanged();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					break;

				default:
					mIsRefreshing = false;
					// if (refresh) {
					// mEmptyTV.setVisibility(View.VISIBLE);
					// }
					break;
				}
			}

		}, params, request_url, false, 1);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
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
		if (!mIsRefreshing) {
			mIsRefreshing = true;
			getAllTie(false);
		} else {
			mPullToRefreshListView.onRefreshComplete();
		}
	}

	/**
	 * @describe:帖子Adapter
	 * @author:XH
	 * @version
	 * @created:
	 */

	@Override
	protected void onResume() {
		super.onResume();
		getAllTie(true);
	}

	private void showDeleteTieDialog(final TieItem tie) {
		mDeleteTieDialog = new AlertDialog.Builder(TieActivity.this).setTitle("确定删除").setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				delTie(tie);
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).create();
		mDeleteTieDialog.setCanceledOnTouchOutside(true);
		mDeleteTieDialog.show();
	}

	private void delTie(final TieItem tie) {
		AjaxParams params = new AjaxParams();
		params.put("tieZiId", String.valueOf(tie.getId()));
		requestNet(new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:
					Toast.makeText(TieActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
					// mData.remove(position);
					mData.remove(tie);
					mAdapter.notifyDataSetChanged();
					break;
				case Constant.NET_DATA_FAIL:
					Toast.makeText(TieActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
			}

		}, params, NetworkUtil.DEL_TIE, false, 0);

	}
}
