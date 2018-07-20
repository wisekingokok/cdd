package com.chewuwuyou.app.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxParams;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.BusinessOrderAdapter;
import com.chewuwuyou.app.bean.Task;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.DateTimeUtil;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ScreenUtils;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.PullToRefreshBase;
import com.chewuwuyou.app.widget.PullToRefreshBase.OnRefreshListener2;
import com.chewuwuyou.app.widget.PullToRefreshListView;

public class OrderManagerActivity extends CDDBaseActivity implements
		OnRefreshListener2<ListView>, View.OnClickListener {
	@ViewInject(id = R.id.sub_header_bar_left_ibtn)
	private ImageButton mBackBtn;
	@ViewInject(id = R.id.sub_header_bar_tv)
	private TextView mHeaderTV;
	@ViewInject(id = R.id.sub_header_bar_right_ibtn)
	private ImageButton mHeaderRightBtn;
	@ViewInject(id = R.id.order_empty_tv)
	private TextView mOrderEmptyText;
	@ViewInject(id = R.id.order_role_tv)
	private TextView mOrderRoleTV;// 订单角色
	@ViewInject(id = R.id.order_type_tv)
	private TextView mOrderTypeTV;// 订单类型
	@ViewInject(id = R.id.order_status_tv)
	private TextView mOrderStatusTV;// 订单状态
	private BusinessOrderAdapter mAdapter;
	private PullToRefreshListView mPullToRefreshListView;// 订单列表
	private List<Task> mData;// 订单集合
	// private List<Task> mRefreshData;// 查找后的
	private String mOrderRole = "0", mOrderType = "0";
	private String mOrderStatus;
	private View mOrderRolePopView;
	private PopupWindow mOrderRolePopWindow = null;
	private Button mAllOrderRoleBtn;// all_order_role_btn;
	private Button mUserOrderRoleBtn;// user_order_btn;
	private Button mBussinessOrderRoleBtn;// business_order_btn;
	private Button mAssignOrderRoleBtn;// assign_order_btn;

	private View mOrderTypePopView;
	private PopupWindow mOrderTypePopWindow = null;
	private Button mAllOrderTypeBtn;// all_order_type_btn;
	private Button mIllegalOrderTypeBtn;// illegal_order_type_btn;
	private Button mVehicleOrderTypeBtn;// vehicle_order_type_btn;
	private Button mLicenceOrderTypeBtn;// licence_order_type_btn;

	private View mOrderStatusPopView;
	private PopupWindow mOrderStatusPopWindow = null;
	private Button mAllOrderStatusBtn;// all_order_status_btn;
	private Button mUnfinishedOrderStatusBtn;// unfinished_order_status_btn
	private Button mFinishedOrderStatusBtn;// finished_order_status_btn
	private int width;
	private RelativeLayout mTitleHeight;// 标题布局高度
	private int mCurcor;// 翻页要用
	private boolean mIsRefreshing = false;// 翻页要用
	private TextView mOrderEmptyTV;// 无订单时展示

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_manager_ac);
		initView();
		initData();
		initEvent();
	}

	@Override
	protected void initView() {
		width = ScreenUtils.getScreenWidth(OrderManagerActivity.this) / 3;
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.order_list);
		mOrderEmptyTV = (TextView) findViewById(R.id.order_empty_tv);
		mOrderRolePopView = getLayoutInflater().inflate(
				R.layout.choose_order_role_pop_window, null);
		this.mAllOrderRoleBtn = (Button) mOrderRolePopView
				.findViewById(R.id.all_order_role_btn);
		this.mUserOrderRoleBtn = (Button) mOrderRolePopView
				.findViewById(R.id.user_order_role_btn);
		this.mBussinessOrderRoleBtn = (Button) mOrderRolePopView
				.findViewById(R.id.business_order_role_btn);
		this.mAssignOrderRoleBtn = (Button) mOrderRolePopView
				.findViewById(R.id.assign_order_role_btn);
		this.mAllOrderRoleBtn.setLayoutParams(new LayoutParams(width,
				LayoutParams.WRAP_CONTENT));
		this.mUserOrderRoleBtn.setLayoutParams(new LayoutParams(width,
				LayoutParams.WRAP_CONTENT));
		this.mBussinessOrderRoleBtn.setLayoutParams(new LayoutParams(width,
				LayoutParams.WRAP_CONTENT));
		this.mAssignOrderRoleBtn.setLayoutParams(new LayoutParams(width,
				LayoutParams.WRAP_CONTENT));
		mOrderTypePopView = getLayoutInflater().inflate(
				R.layout.choose_order_type_pop_window, null);
		this.mAllOrderTypeBtn = (Button) mOrderTypePopView
				.findViewById(R.id.all_order_type_btn);
		this.mIllegalOrderTypeBtn = (Button) mOrderTypePopView
				.findViewById(R.id.illegal_order_type_btn);
		this.mVehicleOrderTypeBtn = (Button) mOrderTypePopView
				.findViewById(R.id.vehicle_order_type_btn);
		this.mLicenceOrderTypeBtn = (Button) mOrderTypePopView
				.findViewById(R.id.licence_order_type_btn);
		this.mAllOrderTypeBtn.setLayoutParams(new LayoutParams(width,
				LayoutParams.WRAP_CONTENT));
		this.mIllegalOrderTypeBtn.setLayoutParams(new LayoutParams(width,
				LayoutParams.WRAP_CONTENT));
		this.mVehicleOrderTypeBtn.setLayoutParams(new LayoutParams(width,
				LayoutParams.WRAP_CONTENT));
		this.mLicenceOrderTypeBtn.setLayoutParams(new LayoutParams(width,
				LayoutParams.WRAP_CONTENT));
		mOrderStatusPopView = getLayoutInflater().inflate(
				R.layout.choose_order_status_pop_window, null);
		this.mAllOrderStatusBtn = (Button) mOrderStatusPopView
				.findViewById(R.id.all_order_status_btn);
		this.mUnfinishedOrderStatusBtn = (Button) mOrderStatusPopView
				.findViewById(R.id.unfinished_order_status_btn);
		this.mFinishedOrderStatusBtn = (Button) mOrderStatusPopView
				.findViewById(R.id.finished_order_status_btn);
		this.mAllOrderStatusBtn.setLayoutParams(new LayoutParams(width,
				LayoutParams.WRAP_CONTENT));
		this.mUnfinishedOrderStatusBtn.setLayoutParams(new LayoutParams(width,
				LayoutParams.WRAP_CONTENT));
		this.mFinishedOrderStatusBtn.setLayoutParams(new LayoutParams(width,
				LayoutParams.WRAP_CONTENT));

	}

	@SuppressWarnings("deprecation")
	@Override
	protected void initData() {

		mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);// 根据不同手机判断

		mHeaderTV.setText(R.string.order_manager);
		mHeaderRightBtn.setImageResource(R.drawable.searchbutton);
		mHeaderRightBtn.setVisibility(View.VISIBLE);

		this.mOrderRoleTV.setText(R.string.all_order_role);
		this.mOrderTypeTV.setText(R.string.all_order_type);
		this.mOrderStatusTV.setText(R.string.all_order_type);
		this.mOrderRolePopWindow = new PopupWindow(mOrderRolePopView, width,
				LayoutParams.WRAP_CONTENT);
		this.mOrderRolePopWindow.setBackgroundDrawable(new BitmapDrawable());
		this.mOrderRolePopWindow.setFocusable(true);
		this.mOrderRolePopWindow.setOutsideTouchable(true);

		this.mOrderTypePopWindow = new PopupWindow(mOrderTypePopView, width,
				LayoutParams.WRAP_CONTENT);
		this.mOrderTypePopWindow.setBackgroundDrawable(new BitmapDrawable());
		this.mOrderTypePopWindow.setFocusable(true);
		this.mOrderTypePopWindow.setOutsideTouchable(true);

		this.mOrderStatusPopWindow = new PopupWindow(mOrderStatusPopView,
				width, LayoutParams.WRAP_CONTENT);
		this.mOrderStatusPopWindow.setBackgroundDrawable(new BitmapDrawable());
		this.mOrderStatusPopWindow.setFocusable(true);
		this.mOrderStatusPopWindow.setOutsideTouchable(true);

		if (mData == null) {
			mData = new ArrayList<Task>();
		}
		if (mAdapter == null) {
			mAdapter = new BusinessOrderAdapter(OrderManagerActivity.this,
					mData);
		}
		this.mPullToRefreshListView.setAdapter(mAdapter);
		// getOrderList(mOrderRole, mOrderType, mOrderStatus, true);
		if (CacheTools.getOtherCacheData("taskId") != null
				&& CacheTools.getOtherCacheData("orderType") != null) {
			addOrder(CacheTools.getOtherCacheData("taskId"),
					CacheTools.getOtherCacheData("orderType"));
		}
	}

	@Override
	protected void initEvent() {
		mBackBtn.setOnClickListener(this);
		mHeaderRightBtn.setOnClickListener(this);

		this.mAllOrderRoleBtn.setOnClickListener(this);
		this.mUserOrderRoleBtn.setOnClickListener(this);
		this.mBussinessOrderRoleBtn.setOnClickListener(this);
		this.mAssignOrderRoleBtn.setOnClickListener(this);

		this.mAllOrderTypeBtn.setOnClickListener(this);
		this.mIllegalOrderTypeBtn.setOnClickListener(this);
		this.mVehicleOrderTypeBtn.setOnClickListener(this);
		this.mLicenceOrderTypeBtn.setOnClickListener(this);

		this.mAllOrderStatusBtn.setOnClickListener(this);
		this.mUnfinishedOrderStatusBtn.setOnClickListener(this);
		this.mFinishedOrderStatusBtn.setOnClickListener(this);

		this.mOrderRoleTV.setOnClickListener(this);
		this.mOrderTypeTV.setOnClickListener(this);
		this.mOrderStatusTV.setOnClickListener(this);

		this.mPullToRefreshListView.setOnRefreshListener(this);
		this.mPullToRefreshListView
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						Task task = mData.get(arg2 - 1);
						String flag = task.getFlag();
						String status = task.getStatus();
						Intent intent = null;
						if (status.equals("28")
								&& (flag.equals("1") || flag.equals("2"))) {
							intent = new Intent(OrderManagerActivity.this,
									NewRobOrderDetailsActivity.class);
						} else if (flag.equals("3")
								&& (status.equals("27") || status.equals("28"))) {
							intent = new Intent(OrderManagerActivity.this,
									ToquoteAcitivity.class);
						} else {
							intent = new Intent(OrderManagerActivity.this,
									OrderActivity.class);

						}
						intent.putExtra("taskId", mData.get(arg2 - 1).getId());
						startActivity(intent);

					}
				});
	}

	/**
	 * 根据相应状态获取订单列表
	 */
	private void getOrderList(String OrderRole, String OrderType,
			String OrderStatus, final boolean refresh) {
		if (refresh) {
			mCurcor = 0;
		}
		mPullToRefreshListView.setRefreshing();
		AjaxParams params = new AjaxParams();
		params.put("start", String.valueOf(mCurcor));
		params.put("where", OrderRole.equals("0") ? "" : OrderRole + "");
		params.put("type", OrderType.equals("0") ? "" : OrderType + "");
		params.put("finish", TextUtils.isEmpty(OrderStatus) ? "" : OrderStatus
				+ ""); // 全部 "" 未完成 0 已完成 1
		requestNet(new Handler() {
			@Override
			public void handleMessage(Message msg) {

				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:

					List<Task> mRefreshData = Task.parseList(msg.obj.toString());

					if(mRefreshData.size()==0){
						mOrderEmptyTV.setVisibility(View.VISIBLE);
					}else{
						mOrderEmptyTV.setVisibility(View.GONE);
					}

					mIsRefreshing = false;
					mPullToRefreshListView.onRefreshComplete();

					if (refresh) {
						mPullToRefreshListView.onLoadMore();
						if (mRefreshData == null) {
							mOrderEmptyText.setVisibility(View.VISIBLE);
							return;
						}
						mData.clear();
					} else {
						// 是加载后面的订单
						if (mRefreshData == null) {
							ToastUtil.toastShow(OrderManagerActivity.this,
									"没有更多数据了");
							return;
						}
					}
					mData.addAll(mRefreshData);
					bTBOrderSort(mData);
					mAdapter.notifyDataSetChanged();
					mCurcor = mData.size();
					if (mRefreshData.size() < 10) {
						mPullToRefreshListView.onLoadComplete();
					}
					 if (mRefreshData.size() == 0) {
						   mOrderEmptyText.setVisibility(View.VISIBLE);
						   return;
						 }
					break;

				default:
					mPullToRefreshListView.onRefreshComplete();
					mIsRefreshing = false;
					mData.clear();
					mAdapter.notifyDataSetChanged();
					if (refresh) {
						mOrderEmptyTV.setVisibility(View.VISIBLE);
					}
					break;
				}
			}
		}, params, NetworkUtil.GET_ALL_BUSI_ORDER, false, 1);
	}

	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(OrderManagerActivity.this);
		getOrder(mOrderRole, mOrderType, mOrderStatus, true);
	}



	private void getOrder(String OrderRole, String OrderType,String OrderStatus, final boolean refresh) {
		if (refresh) {
			mCurcor = 0;
		}
//		mPullToRefreshListView.setRefreshing();
		AjaxParams params = new AjaxParams();
		params.put("start", String.valueOf(mCurcor));
		params.put("where", OrderRole.equals("0") ? "" : OrderRole + "");
		params.put("type", OrderType.equals("0") ? "" : OrderType + "");
		params.put("finish", TextUtils.isEmpty(OrderStatus) ? "" : OrderStatus
				+ ""); // 全部 "" 未完成 0 已完成 1
		requestNet(new Handler() {
			@Override
			public void handleMessage(Message msg) {

				switch (msg.what) {
					case Constant.NET_DATA_SUCCESS:

						List<Task> mRefreshData = Task.parseList(msg.obj.toString());

						if(mRefreshData.size()==0){
							mOrderEmptyTV.setVisibility(View.VISIBLE);
						}else{
							mOrderEmptyTV.setVisibility(View.GONE);
						}
						mIsRefreshing = false;
						mPullToRefreshListView.onRefreshComplete();

						if (refresh) {
							mPullToRefreshListView.onLoadMore();
							if (mRefreshData == null) {
								mOrderEmptyText.setVisibility(View.VISIBLE);
								return;
							}
							mData.clear();
						} else {
							// 是加载后面的订单
							if (mRefreshData == null) {
								ToastUtil.toastShow(OrderManagerActivity.this,
										"没有更多数据了");
								return;
							}
						}
						mData.addAll(mRefreshData);
						bTBOrderSort(mData);
						mAdapter.notifyDataSetChanged();
						mCurcor = mData.size();
						if (mRefreshData.size() < 10) {
							mPullToRefreshListView.onLoadComplete();
						}
						if (mRefreshData.size() == 0) {
							mOrderEmptyText.setVisibility(View.VISIBLE);
							return;
						}
						break;

					default:
						mPullToRefreshListView.onRefreshComplete();
						mIsRefreshing = false;
						mData.clear();
						mAdapter.notifyDataSetChanged();
						if (refresh) {
							mOrderEmptyTV.setVisibility(View.VISIBLE);
						}
						break;
				}
			}
		}, params, NetworkUtil.GET_ALL_BUSI_ORDER, false, 0);
	}

	/**
	 * 对商家订单进行排序
	 * 
	 * @param
	 */
	private void bTBOrderSort(List<Task> mTasks) {
		if (mTasks != null) {
			Collections.sort(mTasks, new Comparator<Task>() {

				@Override
				public int compare(Task lhs, Task rhs) {
					Date date1 = DateTimeUtil.stringToDate(lhs.getPubishTime());
					Date date2 = DateTimeUtil.stringToDate(rhs.getPubishTime());
					// 对日期字段进行升序，如果欲降序可采用after方法
					if (date1.before(date2)) {
						return 1;
					}
					return -1;
				}
			});

		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPause(OrderManagerActivity.this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			finishActivity();
			break;
		case R.id.sub_header_bar_right_ibtn:
			StatService.onEvent(OrderManagerActivity.this,
					"clickOrderManagerSearchBtn", "点击订单管理搜索按钮");
			Intent intent = new Intent(OrderManagerActivity.this,
					TaskSearchActivity.class);
			intent.setAction("com.chewuwuyou.app.search_order");
			startActivity(intent);
			break;
		case R.id.order_role_tv:
			mOrderRolePopWindow.showAsDropDown(mOrderRoleTV, 0, 0);
			break;
		case R.id.order_type_tv:
			mOrderTypePopWindow.showAsDropDown(mOrderTypeTV, 0, 0);
			break;
		case R.id.order_status_tv:
			mOrderStatusPopWindow.showAsDropDown(mOrderStatusTV, 0, 0);
			break;
		case R.id.all_order_role_btn:
			mOrderRoleTV.setText(R.string.all_order_role);
			mOrderRole = Constant.ORDERROLE.ALL_ORDER;
			getOrderList(mOrderRole, mOrderType, mOrderStatus, true);
			mOrderRolePopWindow.dismiss();
			break;
		case R.id.user_order_role_btn:
			mOrderRoleTV.setText(R.string.user_order_role);
			mOrderRole = Constant.ORDERROLE.USER_ORDER;
			getOrderList(mOrderRole, mOrderType, mOrderStatus, true);
			mOrderRolePopWindow.dismiss();
			break;
		case R.id.business_order_role_btn:
			mOrderRoleTV.setText(R.string.business_order_role);
			mOrderRole = Constant.ORDERROLE.BUSINESS_ORDER;
			getOrderList(mOrderRole, mOrderType, mOrderStatus, true);
			mOrderRolePopWindow.dismiss();
			break;
		case R.id.assign_order_role_btn:
			mOrderRoleTV.setText(R.string.assign_order_role);
			mOrderRole = Constant.ORDERROLE.ASSIGEMENT_ORDER;
			getOrderList(mOrderRole, mOrderType, mOrderStatus, true);
			mOrderRolePopWindow.dismiss();
			break;
		case R.id.all_order_type_btn:
			mOrderTypeTV.setText(R.string.all_order_type);
			mOrderType = Constant.ORDERTYPE.ALL_ORDER;
			getOrderList(mOrderRole, mOrderType, mOrderStatus, true);
			mOrderTypePopWindow.dismiss();
			break;
		case R.id.illegal_order_type_btn:
			mOrderTypeTV.setText(R.string.illegal_order_type);
			mOrderType = Constant.ORDERTYPE.ILLEGAL_SERVICE;
			getOrderList(mOrderRole, mOrderType, mOrderStatus, true);
			mOrderTypePopWindow.dismiss();
			break;
		case R.id.vehicle_order_type_btn:
			mOrderTypeTV.setText(R.string.vehicle_order_type);
			mOrderType = Constant.ORDERTYPE.VEHILCE_SERVICE;
			getOrderList(mOrderRole, mOrderType, mOrderStatus, true);
			mOrderTypePopWindow.dismiss();
			break;
		case R.id.licence_order_type_btn:
			mOrderTypeTV.setText(R.string.licence_order_type);
			mOrderType = Constant.ORDERTYPE.LICENCE_SERVICE;
			getOrderList(mOrderRole, mOrderType, mOrderStatus, true);
			mOrderTypePopWindow.dismiss();
			break;
		case R.id.all_order_status_btn:
			mOrderStatusTV.setText(R.string.all_order_status);
			mOrderStatus = Constant.ORDERSTATUS.ALL_ORDER;
			getOrderList(mOrderRole, mOrderType, mOrderStatus, true);
			mOrderStatusPopWindow.dismiss();
			break;
		case R.id.unfinished_order_status_btn:
			mOrderStatusTV.setText(R.string.unfinished_order_status);
			mOrderStatus = Constant.ORDERSTATUS.UNFINISH_ORDER;
			getOrderList(mOrderRole, mOrderType, mOrderStatus, true);
			mOrderStatusPopWindow.dismiss();
			break;
		case R.id.finished_order_status_btn:
			mOrderStatusTV.setText(R.string.finished_order_status);
			mOrderStatus = Constant.ORDERSTATUS.FINISH_ORDER;
			getOrderList(mOrderRole, mOrderType, mOrderStatus, true);
			mOrderStatusPopWindow.dismiss();
			break;
		default:
			break;
		}
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
			getOrderList(mOrderRole, mOrderType, mOrderStatus, true);
		} else {
			mPullToRefreshListView.onRefreshComplete();
		}
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!mIsRefreshing) {
			mIsRefreshing = true;
			getOrderList(mOrderRole, mOrderType, mOrderStatus, false);
		} else {
			mPullToRefreshListView.onRefreshComplete();
		}
	}

	public void addOrder(String taskId, String orderType) {
		AjaxParams params = new AjaxParams();
		params.put("taskId", taskId);
		params.put("amount", "0");
		params.put("type", orderType);
		requestNet(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:
					CacheTools.clearUserData("taskId");
					CacheTools.clearUserData("orderType");
					finishActivity();
					break;
				default:
					break;
				}
			}
		}, params, NetworkUtil.UPDATE_ORDER_STATUS, false, 0);
	}
}
