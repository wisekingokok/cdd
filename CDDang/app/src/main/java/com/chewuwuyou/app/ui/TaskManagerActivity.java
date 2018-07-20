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

/**
 * @describe:任务管理
 * @author:yuyong
 * @version 1.1.0
 * @created:2014-11-4下午4:44:30
 */
public class TaskManagerActivity extends CDDBaseActivity implements
		OnRefreshListener2<ListView>, View.OnClickListener {
	@ViewInject(id = R.id.sub_header_bar_left_ibtn)
	private ImageButton mBackBtn;
	@ViewInject(id = R.id.sub_header_bar_tv)
	private TextView mHeaderTV;
	@ViewInject(id = R.id.sub_header_bar_right_ibtn)
	private ImageButton mHeaderRightBtn;
	private List<Task> mData;
	private BusinessOrderAdapter mAdapter;
	private PullToRefreshListView mPullToRefreshListView;// 任务列表
	@ViewInject(id = R.id.task_type_tv)
	private TextView mTaskTypeTV;// 服务类型:全部、车辆、驾证、违章服务
	@ViewInject(id = R.id.task_status_tv)
	private TextView mTaskStatusTV;// 服务状态：全部、已完成、未完成
	private View mTaskTypePopView;
	private PopupWindow mTaskTypePopWindow = null;

	private Button mAllTaskTypeBtn;// all_type_type_btn;
	private Button mIllegalTaskTypeBtn;// illegal_type_type_btn;
	private Button mVehicleTaskTypeBtn;// vehicle_type_type_btn;
	private Button mLicenceTaskTypeBtn;// licence_type_type_btn;
	private RelativeLayout mTitleHeight;//标题布局高度
	private View mTaskStatusPopView;
	private PopupWindow mTaskStatusPopWindow = null;
	private Button mAllTaskStatusBtn;// all_type_status_btn
	private Button mUnfinishedTaskStatusBtn;// unfinished_type_status_btn
	private Button mFinishedTaskStatusBtn;// finished_type_status_btn
	// public static final String TASK_SER = "com.cwwy.bean.task.ser";//
	// 传递任务实体的标识
	private List<Task> mRefreshData;// 通过选择的服务类型和服务状态更新任务集合
	private String mTaskType = Constant.ORDERTYPE.ALL_ORDER, 
			    mTaskStatus = Constant.ORDERSTATUS.ALL_ORDER; //所有订单 ,所有订单状态
	
	private int mCurcor = 0;// 翻页要用
	private boolean mIsRefreshing = false;// 翻页要用
	@ViewInject(id = R.id.task_empty_tv)
	private TextView mOrderEmptyTV;// 无订单时展示
	// private Handler mHandler = new Handler() {
	// @Override
	// public void handleMessage(Message msg) {
	// super.handleMessage(msg);
	// switch (msg.what) {
	// case Constant.NET_DATA_SUCCESS:
	//
	// mData = Task.parseList(msg.obj.toString());
	// if (mData != null && !mData.isEmpty()) {
	// mOrderEmptyTV.setText("");
	// } else {
	// mOrderEmptyTV.setText(R.string.no_task_txt);
	// }
	// if (mData != null) {
	// // 按时间顺序降序排列
	// Collections.sort(mData, new Comparator<Task>() {
	// @Override
	// public int compare(Task lhs, Task rhs) {
	// Date date1 = DateTimeUtil.stringToDate(lhs
	// .getPubishTime());
	// Date date2 = DateTimeUtil.stringToDate(rhs
	// .getPubishTime());
	// // 对日期字段进行升序，如果欲降序可采用after方法
	// if (date1.before(date2)) {
	// return 1;
	// }
	// return -1;
	// }
	// });
	// refreshListShow(mTaskType, mTaskStatus, true);
	// }
	//
	// break;
	// case Constant.NET_DATA_FAIL:
	// MyLog.i("YUY", "========订单====" + msg.obj.toString());
	// break;
	// default:
	// break;
	// }
	// }
	//
	// };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_manager_ac);
		initView();
		initData();
		initEvent();
		// requestNet(mHandler, null, NetworkUtil.TASK_MANAGER_URL, false, 0);
	}

	@Override
	protected void initView() {
		
		this.mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.task_list);

		this.mTaskTypePopView = getLayoutInflater().inflate(
				R.layout.choose_task_type_pop_window, null);
		this.mAllTaskTypeBtn = (Button) mTaskTypePopView
				.findViewById(R.id.all_task_type_btn);
		this.mIllegalTaskTypeBtn = (Button) mTaskTypePopView
				.findViewById(R.id.illegal_task_type_btn);
		this.mVehicleTaskTypeBtn = (Button) mTaskTypePopView
				.findViewById(R.id.vehicle_task_type_btn);
		this.mLicenceTaskTypeBtn = (Button) mTaskTypePopView
				.findViewById(R.id.licence_task_type_btn);
		this.mTaskStatusPopView = getLayoutInflater().inflate(
				R.layout.choose_task_status_pop_window, null);
		this.mAllTaskStatusBtn = (Button) mTaskStatusPopView
				.findViewById(R.id.all_task_status_btn);
		this.mUnfinishedTaskStatusBtn = (Button) mTaskStatusPopView
				.findViewById(R.id.unfinished_task_status_btn);
		this.mFinishedTaskStatusBtn = (Button) mTaskStatusPopView
				.findViewById(R.id.finished_task_status_btn);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void initData() {
		mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
		mHeaderTV.setText(R.string.task_manager_title);
		mHeaderRightBtn.setImageResource(R.drawable.searchbutton);
		mHeaderRightBtn.setVisibility(View.VISIBLE);
		this.mTaskTypeTV.setText("全部类型");
		this.mTaskStatusTV.setText("全部");
		int width = ScreenUtils.getScreenWidth(TaskManagerActivity.this) / 2;
		this.mTaskTypePopWindow = new PopupWindow(mTaskTypePopView, width,
				LayoutParams.WRAP_CONTENT);
		this.mTaskTypePopWindow.setBackgroundDrawable(new BitmapDrawable());
		this.mTaskTypePopWindow.setFocusable(true);
		this.mTaskTypePopWindow.setOutsideTouchable(true);
		this.mAllTaskTypeBtn.setLayoutParams(new LayoutParams(width,
				LayoutParams.WRAP_CONTENT));
		this.mIllegalTaskTypeBtn.setLayoutParams(new LayoutParams(width,
				LayoutParams.WRAP_CONTENT));
		this.mVehicleTaskTypeBtn.setLayoutParams(new LayoutParams(width,
				LayoutParams.WRAP_CONTENT));
		this.mLicenceTaskTypeBtn.setLayoutParams(new LayoutParams(width,
				LayoutParams.WRAP_CONTENT));
		this.mTaskStatusPopWindow = new PopupWindow(mTaskStatusPopView, width,
				LayoutParams.WRAP_CONTENT);
		this.mTaskStatusPopWindow.setBackgroundDrawable(new BitmapDrawable());
		this.mTaskStatusPopWindow.setFocusable(true);
		this.mTaskStatusPopWindow.setOutsideTouchable(true);
		this.mAllTaskStatusBtn.setLayoutParams(new LayoutParams(width,
				LayoutParams.WRAP_CONTENT));
		this.mUnfinishedTaskStatusBtn.setLayoutParams(new LayoutParams(width,
				LayoutParams.WRAP_CONTENT));
		this.mFinishedTaskStatusBtn.setLayoutParams(new LayoutParams(width,
				LayoutParams.WRAP_CONTENT));
		if (mData == null) {
			mData = new ArrayList<Task>();
		}
		if (mRefreshData == null) {
			mRefreshData = new ArrayList<Task>();
		}
		if (mAdapter == null) {
			mAdapter = new BusinessOrderAdapter(TaskManagerActivity.this, mData);
		}
		this.mPullToRefreshListView.setAdapter(mAdapter);
		refreshListShow(mTaskType, mTaskStatus, true);
		if (CacheTools.getOtherCacheData("taskId") != null
				&& CacheTools.getOtherCacheData("orderType") != null) {
			addOrder(CacheTools.getOtherCacheData("taskId"),
					CacheTools.getOtherCacheData("orderType"));
		}
	}

	@Override
	protected void initEvent() {
		this.mBackBtn.setOnClickListener(this);
		this.mHeaderRightBtn.setOnClickListener(this);

		this.mTaskTypeTV.setOnClickListener(this);
		this.mTaskStatusTV.setOnClickListener(this);

		this.mAllTaskTypeBtn.setOnClickListener(this);
		this.mIllegalTaskTypeBtn.setOnClickListener(this);
		this.mVehicleTaskTypeBtn.setOnClickListener(this);
		this.mLicenceTaskTypeBtn.setOnClickListener(this);

		this.mAllTaskStatusBtn.setOnClickListener(this);
		this.mUnfinishedTaskStatusBtn.setOnClickListener(this);
		this.mFinishedTaskStatusBtn.setOnClickListener(this);

		this.mPullToRefreshListView.setOnRefreshListener(this);
		this.mPullToRefreshListView
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						
					/*	Intent intent = new Intent(TaskManagerActivity.this,
								TaskDetailsActivity.class);*/
						
						/*Bundle bundle = new Bundle();
						bundle.putSerializable(Constant.ORDER_SER_KEY,
								mData.get(arg2 - 1));
						intent.putExtras(bundle);
						intent.putExtra("className", "TaskManagerActivity");*/
						//TODO 判断业务大厅相关的状态
						Intent intent = new Intent(TaskManagerActivity.this,
								OrderActivity.class);
						intent.putExtra("taskId", mData.get(arg2 - 1).getId());
						startActivity(intent);
					}
				});
	}

	/**
	 * 刷新列表的显示
	 */
	// private void refreshListShow(int serviceType, int serviceStatus) {
	// if (mData != null) {
	// if (serviceType == 0 && serviceStatus == 0) {// 所有订单
	// mRefreshData.clear();
	// mRefreshData = mData;
	// } else if (serviceType == 0 && serviceStatus == 1) {
	// mRefreshData.clear();
	// for (int i = 0, size = mData.size(); i < size; i++) {
	// if (!"7".equals(mData.get(i).getStatus())
	// && !"9".equals(mData.get(i).getStatus())) {// 所有未完成订单
	// mRefreshData.add(mData.get(i));
	// }
	// }
	// } else if (serviceType == 0 && serviceStatus == 2) {// 所有完成订单
	// mRefreshData.clear();
	// for (int i = 0, size = mData.size(); i < size; i++) {
	// if (mData.get(i).getStatus().equals("7")
	// || mData.get(i).getStatus().equals("9")) {
	// mRefreshData.add(mData.get(i));
	// }
	// }
	// } else if (serviceType == 1 && serviceStatus == 0) {// 所有违章服务
	// mRefreshData.clear();
	// for (int i = 0, size = mData.size(); i < size; i++) {
	// if (mData.get(i).getType().trim().equals("违章服务")) {
	// mRefreshData.add(mData.get(i));
	// }
	// }
	//
	// } else if (serviceType == 1 && serviceStatus == 1) {// 违章服务未完成订单
	// mRefreshData.clear();
	// for (int i = 0, size = mData.size(); i < size; i++) {
	// if (mData.get(i).getType().trim().equals("违章服务")
	// && !"7".equals(mData.get(i).getStatus())
	// && !"2".equals(mData.get(i).getStatus())
	// && !"9".equals(mData.get(i).getStatus())) {
	// mRefreshData.add(mData.get(i));
	// }
	// }
	// } else if (serviceType == 1 && serviceStatus == 2) {// 违章服务已完成订单
	// mRefreshData.clear();
	// for (int i = 0, size = mData.size(); i < size; i++) {
	// if (mData.get(i).getType().trim().equals("违章服务")
	// && ("7".equals(mData.get(i).getStatus()) || "9"
	// .equals(mData.get(i).getStatus()))) {
	// mRefreshData.add(mData.get(i));
	// }
	// }
	// } else if (serviceType == 2 && serviceStatus == 0) {// 所有车辆服务
	// mRefreshData.clear();
	// for (int i = 0, size = mData.size(); i < size; i++) {
	// if (mData.get(i).getType().trim().equals("车辆服务")) {
	// mRefreshData.add(mData.get(i));
	// }
	// }
	//
	// } else if (serviceType == 2 && serviceStatus == 1) {// 车辆服务未完成订单
	// mRefreshData.clear();
	// for (int i = 0, size = mData.size(); i < size; i++) {
	// if (mData.get(i).getType().trim().equals("车辆服务")
	// && !"7".equals(mData.get(i).getStatus())
	// && !"2".equals(mData.get(i).getStatus())
	// && !"9".equals(mData.get(i).getStatus())) {
	// mRefreshData.add(mData.get(i));
	// }
	// }
	// } else if (serviceType == 2 && serviceStatus == 2) {// 车辆服务已完成订单
	// mRefreshData.clear();
	// for (int i = 0, size = mData.size(); i < size; i++) {
	// if (mData.get(i).getType().trim().equals("车辆服务")
	// && ("7".equals(mData.get(i).getStatus()) || "9"
	// .equals(mData.get(i).getStatus()))) {
	// mRefreshData.add(mData.get(i));
	// }
	// }
	// } else if (serviceType == 3 && serviceStatus == 0) {// 所有驾证服务
	// mRefreshData.clear();
	// for (int i = 0, size = mData.size(); i < size; i++) {
	// if (mData.get(i).getType().trim().equals("驾证服务")) {
	// mRefreshData.add(mData.get(i));
	// }
	// }
	//
	// } else if (serviceType == 3 && serviceStatus == 1) {// 驾证服务未完成订单
	// mRefreshData.clear();
	// for (int i = 0, size = mData.size(); i < size; i++) {
	// if (mData.get(i).getType().trim().equals("驾证服务")
	// && !"7".equals(mData.get(i).getStatus())
	// && !"2".equals(mData.get(i).getStatus())
	// && !"9".equals(mData.get(i).getStatus())) {
	// mRefreshData.add(mData.get(i));
	// }
	// }
	// } else if (serviceType == 3 && serviceStatus == 2) {// 驾证服务已完成订单
	// mRefreshData.clear();
	// for (int i = 0, size = mData.size(); i < size; i++) {
	// if (mData.get(i).getType().trim().equals("驾证服务")
	// && ("7".equals(mData.get(i).getStatus()) || "9"
	// .equals(mData.get(i).getStatus()))) {
	// mRefreshData.add(mData.get(i));
	// }
	// }
	// }
	// }
	// mAdapter.notifyDataSetChanged();
	// }
	private void refreshListShow(String taskType, String taskStatus,
			final boolean refresh) {
		if (refresh) {
			mCurcor = 0;
		}
		mPullToRefreshListView.setRefreshing();
		AjaxParams params = new AjaxParams();
		params.put("start", String.valueOf(mCurcor));
		params.put("type", taskType.equals("0") ? "" : taskType + "");
		params.put("finish", taskStatus.equals("-1") ? "" : taskStatus + "");
		requestNet(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:
					mOrderEmptyTV.setText("");
					mIsRefreshing = false;
					mPullToRefreshListView.onRefreshComplete();
					// mRefreshData = Task.parseList(msg.obj.toString());
					if (msg.obj.toString().equals("[]")
							|| msg.obj.toString() == null) {
						mOrderEmptyTV.setText(R.string.no_order_txt);
					}
					List<Task> mRefreshData = Task.parseList(msg.obj.toString());
					if (refresh) {
						mPullToRefreshListView.onLoadMore();
						if (mRefreshData == null) {
							mOrderEmptyTV.setText(R.string.no_order_txt);
							return;
						}
						mData.clear();
					} else {
						// 是加载后面的订单
						if (mRefreshData == null) {
							ToastUtil.toastShow(TaskManagerActivity.this,
									"没有更多数据了");
							return;
						}
					}
					if(mRefreshData.size()!=0){
						mOrderEmptyTV.setVisibility(View.GONE);
						
					}else{
						mOrderEmptyTV.setText(R.string.no_order_txt);
						mOrderEmptyTV.setVisibility(View.VISIBLE);
					}
					mData.addAll(mRefreshData);
					taskSort(mData);
					mAdapter.notifyDataSetChanged();
					mCurcor = mData.size();
					if (mRefreshData.size() < 10) {
						mPullToRefreshListView.onLoadComplete();
					}
					break;
				default:
					mData.clear();
					mAdapter.notifyDataSetChanged();
					mPullToRefreshListView.onRefreshComplete();
					mIsRefreshing = false;
					
					if (refresh) {
						mOrderEmptyTV.setText(R.string.no_order_txt);
					}
					break;
				}
			}
		}, params, NetworkUtil.TASK_MANAGER_URL, false, 1);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			finishActivity();
			break;
		case R.id.sub_header_bar_right_ibtn:
			Intent intent = new Intent(TaskManagerActivity.this,
					TaskSearchActivity.class);
			intent.setAction("com.chewuwuyou.app.search_task");
			startActivity(intent);
			break;
		case R.id.task_type_tv:
			mTaskTypePopWindow.showAsDropDown(mTaskTypeTV, 0, 0);
			break;
		case R.id.task_status_tv:
			mTaskStatusPopWindow.showAsDropDown(mTaskStatusTV, 0, 0);
			break;
		case R.id.all_task_type_btn:
			mTaskTypeTV.setText(R.string.all_task_type);
			mTaskType = Constant.ORDERTYPE.ALL_ORDER;
			refreshListShow(mTaskType, mTaskStatus, true);
			mTaskTypePopWindow.dismiss();
			break;
		case R.id.illegal_task_type_btn:
			mTaskTypeTV.setText(R.string.illegal_task_type);
			mTaskType = Constant.ORDERTYPE.ILLEGAL_SERVICE;
			refreshListShow(mTaskType, mTaskStatus, true);
			mTaskTypePopWindow.dismiss();
			break;
		case R.id.vehicle_task_type_btn:
			mTaskTypeTV.setText(R.string.vehicle_task_type);
			mTaskType = Constant.ORDERTYPE.VEHILCE_SERVICE;
			refreshListShow(mTaskType, mTaskStatus, true);
			mTaskTypePopWindow.dismiss();
			break;
		case R.id.licence_task_type_btn:
			mTaskTypeTV.setText(R.string.licence_task_type);
			mTaskType = Constant.ORDERTYPE.LICENCE_SERVICE;
			refreshListShow(mTaskType, mTaskStatus, true);
			mTaskTypePopWindow.dismiss();
			break;
		case R.id.all_task_status_btn:
			mTaskStatusTV.setText(R.string.all_task_status);
			mTaskStatus = Constant.ORDERSTATUS.ALL_ORDER;
			refreshListShow(mTaskType, mTaskStatus, true);
			mTaskStatusPopWindow.dismiss();
			break;
		case R.id.unfinished_task_status_btn:
			mTaskStatusTV.setText(R.string.unfinished_task_status);
			mTaskStatus = Constant.ORDERSTATUS.UNFINISH_ORDER;
			refreshListShow(mTaskType, mTaskStatus, true);
			mTaskStatusPopWindow.dismiss();
			break;
		case R.id.finished_task_status_btn:
			mTaskStatusTV.setText(R.string.finished_task_status);
			mTaskStatus = Constant.ORDERSTATUS.FINISH_ORDER;
			refreshListShow(mTaskType, mTaskStatus, true);
			mTaskStatusPopWindow.dismiss();
			break;
		default:
			break;
		}
	}

	/**
	 * 对商家订单进行排序
	 * 
	 * @param mBtbOrders12
	 */
	private void taskSort(List<Task> mTasks) {
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
	protected void onResume() {
		super.onResume();
		// requestNet(mHandler, null, NetworkUtil.TASK_MANAGER_URL, false, 0);
		refreshListShow(mTaskType, mTaskStatus, true);
		StatService.onResume(TaskManagerActivity.this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPause(TaskManagerActivity.this);
	}

	@Override
	protected void onDestroy() {
		dismissWaitingDialog();
		super.onDestroy();
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
			refreshListShow(mTaskType, mTaskStatus, true);
		} else {
			mPullToRefreshListView.onRefreshComplete();
		}
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!mIsRefreshing) {
			mIsRefreshing = true;
			refreshListShow(mTaskType, mTaskStatus, false);
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
					mOrderEmptyTV.setText(R.string.no_order_txt);
					break;
				}
			}
		}, params, NetworkUtil.UPDATE_ORDER_STATUS, false, 1);
	}
}
