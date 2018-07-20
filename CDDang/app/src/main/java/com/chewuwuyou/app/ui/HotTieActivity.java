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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.HotTieAdapter;
import com.chewuwuyou.app.bean.HotTieItem;
import com.chewuwuyou.app.bean.ZanItem;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.widget.ActionSheet;
import com.chewuwuyou.app.widget.ActionSheet.MenuItemClickListener;
import com.chewuwuyou.app.widget.PullToRefreshBase;
import com.chewuwuyou.app.widget.PullToRefreshBase.OnRefreshListener2;
import com.chewuwuyou.app.widget.PullToRefreshListView;

public class HotTieActivity extends CDDBaseActivity implements OnRefreshListener2<ListView> {

	// header bar
	@ViewInject(id = R.id.sub_header_bar_left_ibtn, click = "onAction")
	private ImageButton mBackIBtn;// 左键,返回按钮
	@ViewInject(id = R.id.sub_header_bar_tv)
	private TextView mHeaderTV;// 标题
	@ViewInject(id = R.id.sub_header_bar_right_ibtn, click = "onAction")
	private ImageButton mHeaderRightBtn;
	private HotTieAdapter mAdapter;
	private PullToRefreshListView mPullToRefreshListView;

	@ViewInject(id = R.id.hot_ties_header_iv, click = "onAction")
	private ImageView mHeaderIV;// 没有数据时提示用户

	private List<HotTieItem> mTieData;

	private boolean mIsRefreshing = false;// 上拉下拉要用
	private int mCurcor;// 分页要用
	private int mBanId = 0;
	private String mBanTitle;// 所在板块标题
	private int mBanImageId;// 所在板块图
	private boolean mIsSetEmptyTV = false;
	private RelativeLayout mTitleHeight;//标题布局高度
	private AlertDialog mDeleteTieDialog;
	// 消息处理
	public static final int TOGGLE_ZAN = 111;// 赞或者取消赞一个动态
	public static final int EDIT_TIE = 114;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			final HotTieItem tie;
			switch (msg.what) {
			case TOGGLE_ZAN:// 切换赞一个帖子
				tie = (HotTieItem) msg.obj;
				toggleZan(tie);
				break;
			case EDIT_TIE:
				tie = (HotTieItem) msg.obj;
				setTheme(R.style.ActionSheetStyleIOS7);
				ActionSheet menuView = new ActionSheet(HotTieActivity.this);
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
								final EditText editText = new EditText(HotTieActivity.this);
								new AlertDialog.Builder(HotTieActivity.this).setTitle("请输入举报原因").setView(editText)
										.setPositiveButton("确定", new OnClickListener() {

											@Override
											public void onClick(DialogInterface arg0, int arg1) {
												// TODO Auto-generated method
												// stub
												AjaxParams params = new AjaxParams();
												// 传入帖子id和uid
												params.put("type", "2");
												params.put("relateId", String.valueOf(tie.getId()));
												params.put("reason", editText.getText().toString());

												requestNet(new Handler() {

													@Override
													public void handleMessage(Message msg) {
														// TODO Auto-generated
														// method stub
														super.handleMessage(msg);
														switch (msg.what) {
														case Constant.NET_DATA_SUCCESS:
															Toast.makeText(HotTieActivity.this, "已提交", Toast.LENGTH_SHORT).show();

															break;
														case Constant.NET_DATA_FAIL:
															Toast.makeText(HotTieActivity.this, "举报失败，再试试", Toast.LENGTH_SHORT).show();
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hot_ties_layout);
		initView();
		initData();
		initEvent();
		getAllHotTie(true);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.hot_tie_list);
	}

	@Override
	protected void initData() {
		mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
		mTieData = new ArrayList<HotTieItem>();
		mAdapter = new HotTieAdapter(HotTieActivity.this, mHandler, mTieData);
		mPullToRefreshListView.setAdapter(mAdapter);

		mBanId = getIntent().getIntExtra("banId", 0);
		mBanTitle = getIntent().getStringExtra("banTitle");
		mBanImageId = getIntent().getIntExtra("banImageId", 0);
		if (mBanImageId > 0) {
			mHeaderIV.setImageResource(mBanImageId);
			mHeaderIV.setVisibility(View.VISIBLE);
		} else {
			mHeaderIV.setVisibility(View.GONE);
		}
		mHeaderTV.setText(mBanTitle);
		mHeaderRightBtn.setImageResource(R.drawable.cams);
		mHeaderRightBtn.setVisibility(View.VISIBLE);
		// cams.png
	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub
		mPullToRefreshListView.setOnRefreshListener(this);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
				| DateUtils.FORMAT_ABBREV_ALL);

		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

		if (!mIsRefreshing) {
			mIsRefreshing = true;
			getAllHotTie(true);
		} else {
			mPullToRefreshListView.onRefreshComplete();
		}
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		if (!mIsRefreshing) {
			mIsRefreshing = true;
			getAllHotTie(false);
		} else {
			mPullToRefreshListView.onRefreshComplete();
		}
	}

	// 获得所有帖子
	private void getAllHotTie(final boolean refresh) {
		if (refresh) {
			mCurcor = 0;
		}
		mPullToRefreshListView.setRefreshing();
		AjaxParams params = new AjaxParams();
		params.put("banId", String.valueOf(mBanId));
		params.put("start", String.valueOf(mCurcor));
		requestNet(new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if (!mIsSetEmptyTV) {
					TextView tv = new TextView(HotTieActivity.this);
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

					try {
						JSONObject data = new JSONObject(msg.obj.toString());

						List<HotTieItem> response = HotTieItem.parseList(data.getString("tie").toString());

						if (response != null) {
							if (refresh) {
								mPullToRefreshListView.onLoadMore();
								mTieData.clear();
							}
							mTieData.addAll(response);
							// mEmptyText.setVisibility(View.GONE);
							mAdapter.notifyDataSetChanged();
							mCurcor = mTieData.size();
							if (response.size() < Constant.TIE_PAGE_SIZE) {
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

		}, params, NetworkUtil.ALL_TIE, false, 1);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public void onAction(View v) {
		switch (v.getId()) {
		// 退出
		case R.id.sub_header_bar_left_ibtn:
			finish();
			break;
		case R.id.sub_header_bar_right_ibtn:
			Intent intent;
			if (CacheTools.getUserData("userId") != null && Integer.parseInt(CacheTools.getUserData("userId")) > 0) {

				intent = new Intent(HotTieActivity.this, AddHotTieActivity.class);
				intent.putExtra("banId", mBanId);
				startActivity(intent);
			} else {
				// 跳到登录界面
				intent = new Intent(HotTieActivity.this, LoginActivity.class);
				startActivity(intent);
				finishActivity();
			}
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getAllHotTie(true);
	}

	// 切换赞状态
	private void toggleZan(final HotTieItem tie) {

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
							Toast.makeText(HotTieActivity.this, "点赞成功", Toast.LENGTH_SHORT).show();
							tie.setTiezancnt(tie.getTiezancnt() + 1);
						} else {
							tie.setZaned(0 + "");
							Toast.makeText(HotTieActivity.this, "取消赞", Toast.LENGTH_SHORT).show();
							tie.setTiezancnt(tie.getTiezancnt() - 1);
						}
					}
					mAdapter.notifyDataSetChanged();
					break;

				case Constant.NET_DATA_FAIL:
					// 提示切换赞失败
					Toast.makeText(HotTieActivity.this, "失败，请重新尝试", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}

			}
		}, params, NetworkUtil.TIE_TOGGLE_ZAN, false, 0);
	}

	private void showDeleteTieDialog(final HotTieItem tie) {
		mDeleteTieDialog = new AlertDialog.Builder(HotTieActivity.this).setTitle("确定删除").setPositiveButton("确定", new DialogInterface.OnClickListener() {

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

	private void delTie(final HotTieItem tie) {
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
					Toast.makeText(HotTieActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
					// mData.remove(position);
					mTieData.remove(tie);
					mAdapter.notifyDataSetChanged();
					break;
				case Constant.NET_DATA_FAIL:
					Toast.makeText(HotTieActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
			}

		}, params, NetworkUtil.DEL_TIE, false, 0);

	}
}
