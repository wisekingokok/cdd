package com.chewuwuyou.app.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.JYTask;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ScreenUtils;
import com.chewuwuyou.app.utils.ServiceUtils;
import com.chewuwuyou.app.utils.Tools;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:结算状态订单:收入总额订单、未入账订单、已入账订单
 * @author:yuyong
 * @date:2015-10-12下午4:33:22
 * @version:1.2.1
 */
public class TallyStatusOrderActivity extends CDDBaseActivity implements
		OnClickListener {

	private TextView mTitleTV;
	private ImageButton mBackIBtn;
	private TextView mScreenTV;// 筛选
	private ExpandableListView mTallyStatusOrderLV;
	private int mTallyStatus;
	private List<JYTask> mJyTasks;// 所有订单集合
	private RelativeLayout mTitleHeight;// 标题布局高度
	private List<JYTask> mUpdataJyTasks;// 通过状态更新订单

	// private String[] mAllOrderStatus = { "6", "7", "9", "10", "19", "20" };//
	// 收入总额订单
	//
	// private String[] mEarlyJiesuanStatus = { "10", "20" };// 已结算订单
	//
	// private String[] mNoJiesuanOrderStatus = { "6", "7", "9", "19" };// 未结算订单

	// private CommonAdapter<JYTask> mAdapter;
	private List<String> mDataList = new ArrayList<String>();// 对订单发布时间的统计

	private PopupWindow mPopupWindow;

	private View mPopWindowView;
	private RadioGroup mChooseServiceTypeGroup;// 选择服务类型

	// private RadioButton mVehicleServiceRBtn;// 车辆服务
	// private RadioButton mLicenseServiceRBtn;// 驾证服务
	// private RadioButton mIllegalServiceRBtn;// 违章服务
	// private RadioButton mAllServiceRBtn;// 所有类型

	private Button mYearBtn;// 年份
	private Button mStartMonthBtn;// 选择开始月份
	private Button mEndMonthBtn;// 选择结束月份
	private Button mResetBtn;// 重置
	private Button mComfirmBtn;// 确认

	private String mChooseServiceType = "全部";
	private String mYear;
	private String mStartMonth;
	private String mEndMonth;

	private List<String> mYearList = new ArrayList<String>();
	private List<String> mMonthList = new ArrayList<String>();
	private Map<String, List<JYTask>> mScreenMap;

	private MyAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tallly_status_order_ac);
		initView();
		initData();
		initEvent();
	}

	@Override
	protected void initView() {

		mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
		mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mScreenTV = (TextView) findViewById(R.id.sub_header_bar_right_tv);
		mScreenTV.setVisibility(View.GONE);
		mTallyStatusOrderLV = (ExpandableListView) findViewById(R.id.tally_status_order_lv);
		mTallyStatusOrderLV.setGroupIndicator(null);
		mPopWindowView = getLayoutInflater().inflate(
				R.layout.screen_order_popwindow, null);
		mChooseServiceTypeGroup = (RadioGroup) mPopWindowView
				.findViewById(R.id.choose_service_type_group);

		// mVehicleServiceRBtn = (RadioButton) mPopWindowView
		// .findViewById(R.id.vehicle_service_rbtn);
		// mLicenseServiceRBtn = (RadioButton) mPopWindowView
		// .findViewById(R.id.license_service_rbtn);
		// mIllegalServiceRBtn = (RadioButton) mPopWindowView
		// .findViewById(R.id.illegal_service_rbtn);
		// mAllServiceRBtn = (RadioButton) mPopWindowView
		// .findViewById(R.id.all_type_rbtn);

		mYearBtn = (Button) mPopWindowView.findViewById(R.id.year_btn);
		mStartMonthBtn = (Button) mPopWindowView
				.findViewById(R.id.start_month_btn);
		mEndMonthBtn = (Button) mPopWindowView.findViewById(R.id.end_month_btn);
		mResetBtn = (Button) mPopWindowView.findViewById(R.id.reset_btn);
		mComfirmBtn = (Button) mPopWindowView
				.findViewById(R.id.comfirm_screen_btn);
	}

	@Override
	protected void initData() {
		mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);// 根据不同手机判断
		mScreenTV.setText("筛选");
		mTallyStatus = getIntent().getIntExtra("TallyStatus", 0);
		if (mTallyStatus == 0) {
			mTitleTV.setText("未结算订单");
		} else if (mTallyStatus == 1) {
			mTitleTV.setText("已结算订单");
		} else {
			mTitleTV.setText("收入总额订单");
		}
		mUpdataJyTasks = new ArrayList<JYTask>();
		requestNet(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:
					mScreenTV.setVisibility(View.VISIBLE);

					try {
						JSONObject jo = new JSONObject(msg.obj.toString());

						mJyTasks = JYTask.parseList(jo.getJSONArray("tasks")
								.toString());
						for (int i = 0; i < mJyTasks.size(); i++) {

							JYTask item = new JYTask();
							item.setProjectName(mJyTasks.get(i)
									.getProjectName());
							item.setPubishTime(mJyTasks.get(i).getPubishTime());
							item.setStatus(mJyTasks.get(i).getStatus());
							item.setType(mJyTasks.get(i).getType());
							item.setId(mJyTasks.get(i).getId());
							item.setMe(mJyTasks.get(i).getMe());
							item.setPaymentAmount(mJyTasks.get(i)
									.getPaymentAmount());

							int state = Integer.parseInt(item.getStatus());
							String orderRole = item.getMe();
							if (mTallyStatus == 0) {// 未结算
								if ((orderRole.equals("0") && (state == 4
										|| state == 5 || state == 6
										|| state == 7 || state == 9
										|| state == 17 || state == 18 || state == 19))
										|| state == 23 || state == 24) {
									mUpdataJyTasks.add(item);
								}

							} else if (mTallyStatus == 1) {// 已结算
								if ((orderRole.equals("0") && (state == 10 || state == 29))) {
									mUpdataJyTasks.add(item);
								}
							} else if ((orderRole.equals("0") && (state == 6
									|| state == 7 || state == 9 || state == 19
									|| state == 10 || state == 20))
									|| state == 22) {// 收入总额的订单

								mUpdataJyTasks.add(item);
							}

						}
						for (int i = mUpdataJyTasks.size() - 1; i >= 0; i--) {
							String publishTime = mUpdataJyTasks.get(i)
									.getPubishTime();
							mDataList.add(publishTime.substring(0, 7));
							mYearList.add(publishTime.substring(0, 4));
							mMonthList.add(publishTime.substring(5, 7));
						}
						mDataList = Tools.removeDuplicate(mDataList);// 循环提出年份和月份
						mYearList = Tools.removeDuplicate(mYearList);
						mMonthList = Tools.removeDuplicate(mMonthList);

						if (mYearList.size() > 0) {
							mYear = mYearList.get(0);
							mStartMonth = mMonthList.get(mMonthList.size() - 1);
							mEndMonth = mMonthList.get(0);
						}

						mAdapter = new MyAdapter(getScreenMap(mUpdataJyTasks,
								mChooseServiceType, mYear, mStartMonth,
								mEndMonth));
						mTallyStatusOrderLV.setAdapter(mAdapter);

					} catch (JSONException e) {
						e.printStackTrace();
					}
					int groupCount = mTallyStatusOrderLV.getCount();
					for (int i = 0; i < groupCount; i++) {
						mTallyStatusOrderLV.expandGroup(i);
					}
					break;
				default:
					mScreenTV.setVisibility(View.GONE);
					break;

				}
			}
		}, null, NetworkUtil.BUS_INCOME_ANALYSIS, false, 0);

	}

	@Override
	protected void initEvent() {
		mBackIBtn.setOnClickListener(this);
		mScreenTV.setOnClickListener(this);
		mYearBtn.setOnClickListener(this);
		mStartMonthBtn.setOnClickListener(this);
		mEndMonthBtn.setOnClickListener(this);
		mResetBtn.setOnClickListener(this);
		mComfirmBtn.setOnClickListener(this);
		mChooseServiceTypeGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkId) {
						if (checkId == R.id.vehicle_service_rbtn) {
							mChooseServiceType = "车辆服务";
						} else if (checkId == R.id.license_service_rbtn) {
							mChooseServiceType = "驾证服务";
						} else if (checkId == R.id.illegal_service_rbtn) {
							mChooseServiceType = "违章服务";
						} else {
							mChooseServiceType = "全部";
						}

					}
				});

		mTallyStatusOrderLV.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {

				JYTask jyTask = mScreenMap.get(mDataList.get(groupPosition))
						.get(childPosition);
				Intent intent = new Intent(TallyStatusOrderActivity.this,
						OrderActivity.class);
				intent.putExtra("taskId", jyTask.getId());
				startActivity(intent);
				return false;
			}
		});
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			finishActivity();
			break;

		case R.id.sub_header_bar_right_tv:
			mYearBtn.setText(mYear);
			mStartMonthBtn.setText(mStartMonth);
			mEndMonthBtn.setText(mEndMonth);
			int width = ScreenUtils
					.getScreenWidth(TallyStatusOrderActivity.this) * 3 / 4;
			mPopupWindow = new PopupWindow(mPopWindowView, width,
					LayoutParams.MATCH_PARENT, true);
			mPopupWindow.showAsDropDown(mScreenTV, 40, 20);
			mPopupWindow.setFocusable(true);
			mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
			mPopupWindow.setOutsideTouchable(true);
			mPopWindowView.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (mPopupWindow != null && mPopupWindow.isShowing()) {
						mPopupWindow.dismiss();
						mPopupWindow = null;
					}
					return false;
				}
			});
			break;
		case R.id.year_btn:
			showChooseTimeDialog(mYearList, mYearBtn, 0);
			break;
		case R.id.start_month_btn:
			showChooseTimeDialog(mMonthList, mStartMonthBtn, 1);
			break;
		case R.id.end_month_btn:
			showChooseTimeDialog(mMonthList, mEndMonthBtn, 2);
			break;
		case R.id.reset_btn:
			mChooseServiceType = "全部";
			mYear = mYearList.get(0);
			mStartMonth = mMonthList.get(mMonthList.size() - 1);
			mEndMonth = mMonthList.get(0);// 获取月份
			mYearBtn.setText(mYearList.get(0));// 获取年份

			mStartMonthBtn.setText(mMonthList.get(mMonthList.size() - 1));
			mEndMonthBtn.setText(mMonthList.get(0));
			((RadioButton) mPopWindowView.findViewById(R.id.all_type_rbtn))
					.setChecked(true);
			break;
		case R.id.comfirm_screen_btn:// 确认筛选
			mPopupWindow.dismiss();
			mPopupWindow = null;

			mTallyStatusOrderLV.setAdapter(new MyAdapter(getＷholeMap(
					mUpdataJyTasks, mChooseServiceType, mYear, mStartMonth,
					mEndMonth)));
			break;
		default:
			break;
		}
	}

	/**
	 * 筛选订单
	 * 
	 * @param updateJyTasks
	 * @param year
	 * @param startMonth
	 * @param endMonth
	 * @return
	 */
	private Map<String, List<JYTask>> getＷholeMap(List<JYTask> updateJyTasks,
			String serviceType, String year, String startMonth, String endMonth) {
		mScreenMap = new HashMap<String, List<JYTask>>();

		List<JYTask> screenJyTasks = new ArrayList<JYTask>();
		screenJyTasks.clear();

		if (serviceType.equals("车辆服务")) {
			serviceType = "2";
		} else if (serviceType.equals("违章服务")) {
			serviceType = "1";
		} else if (serviceType.equals("驾证服务")) {
			serviceType = "3";
		} else {
			serviceType = "全部";
		}

		for (int i = 0; i < updateJyTasks.size(); i++) {
			JYTask item = new JYTask();
			item.setProjectName(updateJyTasks.get(i).getProjectName());
			item.setPubishTime(updateJyTasks.get(i).getPubishTime());
			item.setStatus(updateJyTasks.get(i).getStatus());
			item.setType(updateJyTasks.get(i).getType());
			item.setId(updateJyTasks.get(i).getId());
			item.setMe(updateJyTasks.get(i).getMe());
			item.setPaymentAmount(updateJyTasks.get(i).getPaymentAmount());

			String projectType = !TextUtils.isEmpty(updateJyTasks.get(i)
					.getProjectName()) ? updateJyTasks.get(i).getProjectName()
					.substring(0, 1) : "1";// 获取项目类型
			String projectYear = updateJyTasks.get(i).getPubishTime()
					.substring(0, 4);// 年
			String projectMonth = updateJyTasks.get(i).getPubishTime()
					.substring(5, 7);// 月

			if (!serviceType.equals("全部")) {
				if (projectType.equals(serviceType)
						&& projectYear.equals(year)
						&& Integer.valueOf(projectMonth) >= Integer
								.valueOf(startMonth)
						&& Integer.valueOf(projectMonth) <= Integer
								.valueOf(endMonth)) {
					screenJyTasks.add(item);
				}
			}
			/*
			 * if (projectYear.equals(year)&& Integer.valueOf(projectMonth) >=
			 * Integer.valueOf(startMonth)&& Integer.valueOf(projectMonth) <=
			 * Integer.valueOf(endMonth)) { screenJyTasks.add(item); }
			 */if (serviceType.equals("全部")) {
				if (projectYear.equals(year)
						&& Integer.valueOf(projectMonth) >= Integer
								.valueOf(startMonth)
						&& Integer.valueOf(projectMonth) <= Integer
								.valueOf(endMonth)) {
					screenJyTasks.add(item);
				}
			}

		}

		mDataList = new ArrayList<String>();
		for (int i = screenJyTasks.size() - 1; i >= 0; i--) {
			mDataList.add(screenJyTasks.get(i).getPubishTime().substring(0, 7));
		}

		mDataList = Tools.removeDuplicate(mDataList);

		List<JYTask> screenJyTasksByMonth;

		for (int i = 0; i < mDataList.size(); i++) {
			screenJyTasksByMonth = new ArrayList<JYTask>();
			for (int j = 0; j < screenJyTasks.size(); j++) {
				if (screenJyTasks.get(j).getPubishTime()
						.contains(mDataList.get(i))) {
					screenJyTasksByMonth.add(screenJyTasks.get(j));
					mScreenMap.put(mDataList.get(i), screenJyTasksByMonth);
				}
			}
		}

		return mScreenMap;
	}

	/**
	 * 查询全部订单
	 * 
	 * @param updateJyTasks
	 * @param serviceType
	 * @param year
	 * @param startMonth
	 * @param endMonth
	 * @return
	 */
	private Map<String, List<JYTask>> getScreenMap(List<JYTask> updateJyTasks,
			String serviceType, String year, String startMonth, String endMonth) {
		mScreenMap = new HashMap<String, List<JYTask>>();

		List<JYTask> screenJyTasks = new ArrayList<JYTask>();
		screenJyTasks.clear();

		if (serviceType.equals("车辆服务")) {
			serviceType = "2";
		} else if (serviceType.equals("违章服务")) {
			serviceType = "1";
		} else if (serviceType.equals("驾证服务")) {
			serviceType = "3";
		} else {
			serviceType = "全部";
		}

		for (int i = 0; i < updateJyTasks.size(); i++) {
			JYTask item = new JYTask();
			item.setProjectName(updateJyTasks.get(i).getProjectName());
			item.setPubishTime(updateJyTasks.get(i).getPubishTime());
			item.setStatus(updateJyTasks.get(i).getStatus());
			item.setType(updateJyTasks.get(i).getType());
			item.setId(updateJyTasks.get(i).getId());
			item.setMe(updateJyTasks.get(i).getMe());
			item.setPaymentAmount(updateJyTasks.get(i).getPaymentAmount());
			// String projectType = !TextUtils.isEmpty(updateJyTasks.get(i)
			// .getProjectName()) ? updateJyTasks.get(i).getProjectName()
			// .substring(0, 1) : "1";// 获取项目类型
			// String projectYear = updateJyTasks.get(i).getPubishTime()
			// .substring(0, 4);// 年
			// String projectMonth = updateJyTasks.get(i).getPubishTime()
			// .substring(5, 7);// 月
			screenJyTasks.add(item);

		}

		mDataList = new ArrayList<String>();
		for (int i = screenJyTasks.size() - 1; i >= 0; i--) {
			mDataList.add(screenJyTasks.get(i).getPubishTime().substring(0, 7));
		}
		mDataList = Tools.removeDuplicate(mDataList);
		List<JYTask> screenJyTasksByMonth;

		for (int i = 0; i < mDataList.size(); i++) {
			screenJyTasksByMonth = new ArrayList<JYTask>();
			for (int j = 0; j < screenJyTasks.size(); j++) {
				if (screenJyTasks.get(j).getPubishTime()
						.contains(mDataList.get(i))) {
					screenJyTasksByMonth.add(screenJyTasks.get(j));
					mScreenMap.put(mDataList.get(i), screenJyTasksByMonth);
				}
			}
		}
		return mScreenMap;
	}

	class MyAdapter extends BaseExpandableListAdapter {

		private Map<String, List<JYTask>> mJytasksMap;

		public MyAdapter(Map<String, List<JYTask>> jyTaskMap) {
			this.mJytasksMap = jyTaskMap;
		}

		// 得到子item需要关联的数据
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			String key = mDataList.get(groupPosition);
			return (mJytasksMap.get(key).get(childPosition));
		}

		// 得到子item的ID
		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		// 设置子item的组件
		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			String key = mDataList.get(groupPosition);
			JYTask jyTask = mJytasksMap.get(key).get(childPosition);
			SonControl control = null;
			if (convertView == null) {
				control = new SonControl();
				convertView = LayoutInflater
						.from(TallyStatusOrderActivity.this).inflate(
								R.layout.tally_status_order_item, null);
				control.orderNameTV = (TextView) convertView
						.findViewById(R.id.order_name_tv);
				control.orderTimeTV = (TextView) convertView
						.findViewById(R.id.order_time_tv);
				control.orderPriceTV = (TextView) convertView
						.findViewById(R.id.order_price_tv);
				convertView.setTag(control);
			} else {
				control = (SonControl) convertView.getTag();
			}
			control.orderTimeTV
					.setText(jyTask.getPubishTime().substring(0, 10));

			control.orderNameTV.setText(ServiceUtils.getProjectName(jyTask
					.getProjectName()));
			double amount = Double.valueOf(jyTask.getPaymentAmount());
			java.text.DecimalFormat df = new java.text.DecimalFormat(
					"######0.00");
			control.orderPriceTV.setText(String.valueOf(df.format(amount)));
			return convertView;
		}

		// 获取当前父item下的子item的个数
		@Override
		public int getChildrenCount(int groupPosition) {
			String key = mDataList.get(groupPosition);
			int size = mJytasksMap.get(key).size();
			return size;
		}

		// 获取当前父item的数据
		@Override
		public Object getGroup(int groupPosition) {
			return mDataList.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return mDataList.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		// 设置父item组件
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			ExpandView expand = null;
			if (convertView == null) {
				expand = new ExpandView();
				convertView = LayoutInflater
						.from(TallyStatusOrderActivity.this).inflate(
								R.layout.data_statistics_group_item, null);
				expand.tv = (TextView) convertView
						.findViewById(R.id.data_statistics_group_tv);
				convertView.setTag(expand);
			} else {
				expand = (ExpandView) convertView.getTag();
			}

			expand.tv.setText(mDataList.get(groupPosition));
			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		class ExpandView {
			TextView tv;
		}

		class SonControl {
			TextView orderTimeTV;
			TextView orderNameTV;
			TextView orderPriceTV;
		}
	}

	/**
	 * 筛选时间段
	 * 
	 * @param times
	 *            时间集合
	 * @param mView
	 *            显示文本
	 * @param timeType
	 *            选择时间类型:年份、开始时间、结束时间
	 */
	public void showChooseTimeDialog(final List<String> times,
			final Button btn, final int timeType) {
		final Dialog dialog = new Dialog(TallyStatusOrderActivity.this,
				R.style.myDialogTheme);
		LayoutInflater inflater = LayoutInflater
				.from(TallyStatusOrderActivity.this);
		RelativeLayout layout = (RelativeLayout) inflater.inflate(
				R.layout.choose_time_dialog, null);

		layout.setAlpha(100);
		dialog.setContentView(layout);
		dialog.show();
		ListView chooseTimeLV = (ListView) layout
				.findViewById(R.id.choose_time_lv);
		chooseTimeLV.setAdapter(new ArrayAdapter<String>(
				TallyStatusOrderActivity.this, R.layout.time_item, times));
		chooseTimeLV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (timeType == 0) {
					mYear = times.get(arg2);
					btn.setText(mYear);
				} else if (timeType == 1) {
					mStartMonth = times.get(arg2);
					btn.setText(mStartMonth);
				} else {
					mEndMonth = times.get(arg2);
					btn.setText(mEndMonth);
				}
				dialog.dismiss();
			}
		});
	}

}
