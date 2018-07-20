package com.chewuwuyou.app.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.JYTask;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.Tools;
import com.chewuwuyou.app.widget.SegmentControl;
import com.chewuwuyou.app.widget.SegmentControl.OnSegmentControlClickListener;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:商家数据统计
 * @author:yuyong
 * @date:2015-10-12下午2:39:22
 * @version:1.2.1
 */
public class DataStatisticsActivity extends CDDBaseActivity implements
		OnClickListener {

	private TextView mTitleTV, mDataSettlement;
	private ImageButton mBackIBtn;
	private SegmentControl mSeControl;
	private PieChart mPieChart;// 饼状图分析
	private LineChart mLineChart;// 曲线图

	private List<JYTask> mJYTasks;
	private LineData data;
	private LineDataSet dataSet;
	private RelativeLayout mTitleHeight;// 标题布局高度

	private List<JYTask> mIllegalTasks;// 驾证
	private List<JYTask> mDrivingTasks;// 违章
	private List<JYTask> mvehicleTasks;// 车辆

	private LinearLayout mSpreadPieChart, mLintChartLayout;

	private ArrayList<String> mXValues = new ArrayList<String>();
	private ArrayList<Entry> mYValues = new ArrayList<Entry>();

	private List<String> xValues;// 折线图日期
	private List<Entry> yValues;// 折线图金额

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏标题栏
		super.onCreate(savedInstanceState);
		setContentView(R.layout.data_statistics_ac);
		initView();
		initData();
		initEvent();
	}

	/**
	 * 初始化控件
	 */
	@Override
	protected void initView() {
		mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
		mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mPieChart = (PieChart) findViewById(R.id.spread_pie_chart);// 饼状图
		mLineChart = (LineChart) findViewById(R.id.lint_chart);
		mSeControl = (SegmentControl) findViewById(R.id.segment_control);
		mSpreadPieChart = (LinearLayout) findViewById(R.id.spread_pie_chart_layout);
		mLintChartLayout = (LinearLayout) findViewById(R.id.lint_chart_layout);
		mDataSettlement = (TextView) findViewById(R.id.datasettlement);

	}

	/**
	 * 逻辑处理
	 */
	@Override
	protected void initData() {
		mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);// 根据不同手机判断
		mTitleTV.setText("数据报表");
		mJYTasks = new ArrayList<JYTask>();
		mIllegalTasks = new ArrayList<JYTask>();
		mDrivingTasks = new ArrayList<JYTask>();
		mvehicleTasks = new ArrayList<JYTask>();
		xValues = new ArrayList<String>();
		yValues = new ArrayList<Entry>();
		pieChAttribute();// 饼状图属性设置
		networkChart();// 请求网络获得图标数据
	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			finishActivity();
			break;

		default:
			break;
		}
	}

	/**
	 * 事件监听
	 */
	@Override
	protected void initEvent() {
		mBackIBtn.setOnClickListener(this);
		mSeControl
				.setOnSegmentControlClickListener(new OnSegmentControlClickListener() {
					@Override
					public void onSegmentControlClick(int index) {
						if (index == 0) {
							mSpreadPieChart.setVisibility(View.VISIBLE);
							mLintChartLayout.setVisibility(View.GONE);
						} else {
							mSpreadPieChart.setVisibility(View.GONE);
							mLintChartLayout.setVisibility(View.VISIBLE);
						}
					}
				});
	}

	/**
	 * 网络请求图标数据
	 */
	public void networkChart() {
		AjaxParams params = new AjaxParams();
		params.put("busId", CacheTools.getUserData("userId"));
		requestNet(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case Constant.NET_DATA_SUCCESS:

					try {
						JSONObject jo = new JSONObject(msg.obj.toString());
						mJYTasks = JYTask.parseList(jo.getJSONArray("tasks")
								.toString());
						for (int i = 0; i < mJYTasks.size(); i++) {
							if (mJYTasks
									.get(i)
									.getMe()
									.equals(getIntent().getIntExtra("issue", 0)
											+ "")) {
								xValues.add(mJYTasks.get(i).getPubishTime()
										.substring(0, 7));
								JYTask task = new JYTask();

								if (mJYTasks.get(i).getType().equals("1")
										&& mJYTasks.get(i).getStatus()
												.equals("29")
										|| mJYTasks.get(i).getStatus()
												.equals("10")) {// 违章数据
									task.setId(mJYTasks.get(i).getId());
									task.setPaymentAmount(mJYTasks.get(i)
											.getPaymentAmount());
									task.setStatus(mJYTasks.get(i).getStatus());
									task.setType(mJYTasks.get(i).getType());
									task.setProjectName(mJYTasks.get(i)
											.getProjectName());
									task.setPubishTime(mJYTasks.get(i)
											.getPubishTime());
									task.setId(mJYTasks.get(i).getId());
									task.setPaymentAmount(mJYTasks.get(i)
											.getPaymentAmount());
									task.setStatus(mJYTasks.get(i).getStatus());
									task.setType(mJYTasks.get(i).getType());
									task.setProjectName(mJYTasks.get(i)
											.getProjectName());
									task.setPubishTime(mJYTasks.get(i)
											.getPubishTime());
									task.setMe(mJYTasks.get(i).getMe());
									mIllegalTasks.add(task);
								} else if (mJYTasks.get(i).getType()
										.equals("3")
										&& mJYTasks.get(i).getStatus()
												.equals("29")
										|| mJYTasks.get(i).getStatus()
												.equals("10")) {// 车辆数据
									task.setId(mJYTasks.get(i).getId());
									task.setPaymentAmount(mJYTasks.get(i)
											.getPaymentAmount());
									task.setStatus(mJYTasks.get(i).getStatus());
									task.setType(mJYTasks.get(i).getType());
									task.setProjectName(mJYTasks.get(i)
											.getProjectName());
									task.setPubishTime(mJYTasks.get(i)
											.getPubishTime());
									task.setMe(mJYTasks.get(i).getMe());
									mvehicleTasks.add(task);
								} else if (mJYTasks.get(i).getType()
										.equals("2")
										&& mJYTasks.get(i).getStatus()
												.equals("29")
										|| mJYTasks.get(i).getStatus()
												.equals("10")) {
									task.setId(mJYTasks.get(i).getId());
									task.setPaymentAmount(mJYTasks.get(i)
											.getPaymentAmount());
									task.setStatus(mJYTasks.get(i).getStatus());
									task.setType(mJYTasks.get(i).getType());
									task.setProjectName(mJYTasks.get(i)
											.getProjectName());
									task.setPubishTime(mJYTasks.get(i)
											.getPubishTime());
									task.setId(mJYTasks.get(i).getId());
									task.setPaymentAmount(mJYTasks.get(i)
											.getPaymentAmount());
									task.setStatus(mJYTasks.get(i).getStatus());
									task.setType(mJYTasks.get(i).getType());
									task.setProjectName(mJYTasks.get(i)
											.getProjectName());
									task.setPubishTime(mJYTasks.get(i)
											.getPubishTime());
									task.setMe(mJYTasks.get(i).getMe());
									mDrivingTasks.add(task);
								}
							}
						}
						if (mIllegalTasks.size() != 0) {
							float typeA = mIllegalTasks.size();
							mXValues.add("违章服务 " + typeA);
							mYValues.add(new Entry(typeA, 0));

						} else if (mDrivingTasks.size() != 0) {
							float typeB = mDrivingTasks.size();
							mXValues.add("驾证服务 " + typeB);
							mYValues.add(new Entry(typeB, 0));
						} else if (mvehicleTasks.size() != 0) {
							float typeC = mDrivingTasks.size();
							mXValues.add("车辆服务 " + typeC);
							mYValues.add(new Entry(typeC, 0));
						}
						mDataSettlement.setText(mIllegalTasks.size()
								+ mDrivingTasks.size() + mvehicleTasks.size()
								+ "单");
						setData(mXValues, mYValues);
						curveAttribute(xValues);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
				default:
					break;
				}
			}

		}, params, NetworkUtil.BUS_INCOME_ANALYSIS, false, 0);
	}

	/**
	 * 饼状图属性设置
	 */
	public void pieChAttribute() {
		mPieChart.setUsePercentValues(true);
		mPieChart.setDescription("");
		// mPieChart.setCenterTextTypeface(Typeface.createFromAsset(getActivity()
		// .getAssets(), "OpenSans-Light.ttf"));
		mPieChart.setDrawHoleEnabled(false);// 中间是否空出空白处
		mPieChart.setHoleColor(Color.WHITE);

		mPieChart.setHoleRadius(58f);
		mPieChart.setTransparentCircleRadius(61f);

		mPieChart.setDrawCenterText(true);

		mPieChart.setRotationAngle(0);
		// enable rotation of the chart by touch
		mPieChart.setRotationEnabled(true);
		mPieChart.animateXY(1400, 1400);
		// mPieChart.setDrawSliceText(false);
		// mPieChart.setUsePercentValues(false);
		// setEvent(2, 100);

		// mPieChart.spin(2000, 0, 360);

		/*
		 * Legend l = mPieChart.getLegend();
		 * l.setPosition(LegendPosition.BELOW_CHART_CENTER);//显示在最下面
		 * l.setXEntrySpace(0f); l.setYEntrySpace(7f); l.setYOffset(0f);
		 */
	}

	/**
	 * 饼图数据填充
	 * 
	 * @param xValues
	 * @param yValues
	 */
	private void setData(ArrayList<String> xValues, List<Entry> yValues) {

		PieDataSet dataSet = new PieDataSet(yValues, "");
		dataSet.setSliceSpace(0f);// 设置个饼状图之间的距离
		dataSet.setSelectionShift(5f);

		ArrayList<Integer> colors = new ArrayList<Integer>();

		int[] LIBERT_COLORS = { Color.rgb(255, 188, 8), Color.rgb(4, 151, 217),
				Color.rgb(114, 188, 223) };
		for (int c : LIBERT_COLORS) {
			colors.add(c);

		}
		dataSet.setColors(colors);
		dataSet.setSelectionShift(1f);

		PieData data = new PieData(xValues, dataSet);
		data.setValueFormatter(new PercentFormatter());
		data.setValueTextSize(11f);
		data.setValueTextColor(Color.BLACK);
		mPieChart.setData(data);
		mPieChart.highlightValues(null);

		mPieChart.invalidate();
	}

	/**
	 * 曲线图属性设置及绑定是数据
	 */
	public void curveAttribute(List<String> xValues) {

		List<String> mXValues = Tools.removeDuplicate(xValues);

		int asd = 0;
		Map<String, Integer> mDataMap = new HashMap<String, Integer>();
		for (int i = 0; i < mJYTasks.size(); i++) {
			if (mJYTasks.get(i).getMe()
					.equals(getIntent().getIntExtra("issue", 0))) {
				for (int j = 0; j < mXValues.size(); j++) {
					if (mJYTasks.get(i).getPubishTime()
							.contains(mXValues.get(j))
							&& mJYTasks.get(i).getStatus().equals("29")
							|| mJYTasks.get(i).getStatus().equals("10")) {
						asd = (int) Float.parseFloat(mJYTasks.get(i)
								.getPaymentAmount());
						mDataMap.put(mXValues.get(j), asd);
					}
				}
			} else {
				for (int j = 0; j < mXValues.size(); j++) {
					if (mJYTasks.get(i).getPubishTime()
							.contains(mXValues.get(j))
							&& mJYTasks.get(i).getStatus().equals("29")
							|| mJYTasks.get(i).getStatus().equals("10")) {
						asd = (int) Float.parseFloat(mJYTasks.get(i)
								.getPaymentAmount());
						mDataMap.put(mXValues.get(j), asd);
					}
				}
			}
		}
		for (int i = 0; i < mDataMap.size(); i++) {
			
			if(String.valueOf(mDataMap.get(mXValues.get(i))).equals("null")){
				
			}else{
			   yValues.add(new Entry(mDataMap.get(mXValues.get(i)), i));
			}
		}
		dataSet = new LineDataSet(yValues, "每月收入分析");
		dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
		dataSet.setDrawCubic(true); // 设置曲线为圆滑的线
		dataSet.setCubicIntensity(0.2f);
		dataSet.setDrawFilled(true); // 设置包括的范围区域填充颜色

		if (xValues.size() <= 0) {
			data = new LineData();
			mLineChart.getXAxis().setPosition(XAxisPosition.BOTTOM); // 让x轴在下面
			mLineChart.setData(data);
			mLineChart.setDrawGridBackground(false); // 是否显示表格颜色
			mLineChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度
			mLineChart.setDescription("");
			mLineChart.animateY(3000);

		} else {
			data = new LineData(mXValues, dataSet);
			mLineChart.getXAxis().setPosition(XAxisPosition.BOTTOM); // 让x轴在下面
			mLineChart.setData(data);
			mLineChart.setDrawGridBackground(false); // 是否显示表格颜色
			mLineChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度
			mLineChart.setDescription("");
			mLineChart.animateY(3000);
		}
	}
}
