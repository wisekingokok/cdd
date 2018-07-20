package com.chewuwuyou.app.fragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.TradingOrderAdapter;
import com.chewuwuyou.app.bean.BusStatis;
import com.chewuwuyou.app.tools.MyListView;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * 成交订单金额统计
 *
 * @author yuyong
 */
@SuppressLint("NewApi")
public class TradingOrderPriceTJFragment extends BaseFragment {

    private View mView;
    private MyListView mProvinceDataList;
    private TradingOrderAdapter mAdapter;
    private List<BusStatis> mBusStatis;
    private BarChart mBarChart;// 柱状图
    private BarChart mBarChartTwo;

    private TextView mBarOneDescTV;// 图形一描述
    private TextView mBarTwoDescTV;// 图形二描述
    private TextView mBarOneTitleTV;// 图形一标题
    private TextView mBarTwoTitleTV;// 图形二标题

    private TextView mDetailDescOneTV;// 明细描述一
    private TextView mDetailDescTwoTV;// 明细描述二
    private ArrayList<BarEntry> mBarArrayList = new ArrayList<BarEntry>();// 日订单金额集合

    private ArrayList<BarEntry> mMonthArrayList = new ArrayList<BarEntry>();// 月订单金额集合
    private BusStatis mBusStati;// 订单单项统计

    private TextView mTitleDayCompany, mTitleMonthCompany;

    private float mJinRiChengJiao;
    private float mZuoRiChengJiao;
    private float mBenYueChengJiao;
    private float mShangYueChengJiao;
    private JSONArray mJsonArray;
    private ScrollView mScorllView;
    private LinearLayout mProvinceTitleLL;//标题区代隐藏
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.trading_oder_ft, null);
        initView();
        initData();
        initEvent();
        return mView;
    }

    @Override
    protected void initView() {

        mTitleDayCompany = (TextView) mView.findViewById(R.id.image_title_day_company);
        mTitleMonthCompany = (TextView) mView.findViewById(R.id.image_title_month_company);
        mBarOneDescTV = (TextView) mView.findViewById(R.id.image_one_desc_tv);
        mBarTwoDescTV = (TextView) mView.findViewById(R.id.image_two_desc_tv);
        mBarOneTitleTV = (TextView) mView.findViewById(R.id.image_title_one_tv);
        mBarTwoTitleTV = (TextView) mView.findViewById(R.id.image_title_two_tv);
        mProvinceDataList = (MyListView) mView.findViewById(R.id.statistics_list);
        mBarChart = (BarChart) mView.findViewById(R.id.bar_chart);
        mBarChartTwo = (BarChart) mView.findViewById(R.id.bar_chart_two);
        mDetailDescOneTV = (TextView) mView.findViewById(R.id.detail_desc_one_tv);
        mScorllView = (ScrollView) mView.findViewById(R.id.scorll_view);
        mDetailDescTwoTV = (TextView) mView.findViewById(R.id.detail_desc_two_tv);
        mProvinceTitleLL= (LinearLayout) mView.findViewById(R.id.province_title_ll);
    }

    @Override
    protected void initData() {
        if(CacheTools.getUserData("daiLitype").equals("3")){//当商家角色为区代理时不显示列表信息
            mBarTwoDescTV.setVisibility(View.GONE);
            mProvinceTitleLL.setVisibility(View.GONE);
        }
        mTitleDayCompany.setText("单位(万元)");
        mTitleMonthCompany.setText("单位(万元)");
        mBarOneTitleTV.setText("日订单金额");
        mBarTwoTitleTV.setText("月订单金额");
        mDetailDescOneTV.setText("本月成交金额");
        mDetailDescTwoTV.setText("上月成交金额");
        mBusStatis = new ArrayList<BusStatis>();
        // BusStatis busStatis;
        // for (int i = 0; i < 10; i++) {
        // busStatis = new BusStatis();
        // busStatis.setCityName("成都市");
        // busStatis.setOrderNumOne("2029");
        // busStatis.setOrderNumTwo("2039");
        // mBusStatis.add(busStatis);
        // }
        if (mAdapter == null) {
            mAdapter = new TradingOrderAdapter(getActivity(), mBusStatis);
            mProvinceDataList.setAdapter(mAdapter);
        }
        mProvinceDataList.setFocusable(false);// 让listview失去焦点
        mProvinceDataList.smoothScrollToPosition(0, 20);// 设置显示的位置

        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        try {
                            mScorllView.setVisibility(View.VISIBLE);
                            JSONObject jo = new JSONObject(msg.obj.toString());

                            mBarOneDescTV.setText(jo.getString("myZone") + "成交订单金额统计");
                            mBarTwoDescTV.setText(jo.getString("myZone") + "代理各区域成交订单金额统计明细");

                            mJinRiChengJiao = jo.getInt("jinRiChengJiaoMount");
                            mZuoRiChengJiao = jo.getInt("zuoRiChengJiaoMount");
                            mBarArrayList.add(new BarEntry(mJinRiChengJiao / 10000, 0));
                            mBarArrayList.add(new BarEntry(mZuoRiChengJiao / 10000, 1));
                            mBarChart.setData(getOneBarData(2, mBarArrayList));

                            mBenYueChengJiao = jo.getInt("benYueChengJiaoMount");
                            mShangYueChengJiao = jo.getInt("shangYueChengJiaoMount");
                            mMonthArrayList.add(new BarEntry(Float.valueOf(Math.round(mBenYueChengJiao / 100) / 100f), 0));
                            mMonthArrayList
                                    .add(new BarEntry(Float.valueOf(Math.round(mShangYueChengJiao / 100) / 100f), 1));
                            mBarChartTwo.setData(getTwoBarData(2, mMonthArrayList));

                            mJsonArray = jo.getJSONArray("places");
                            for (int i = 0; i < jo.getJSONArray("zones").length(); i++) {// 区域订单统计
                                JSONObject zone = jo.getJSONArray("zones").getJSONObject(i);
                                mBusStati = new BusStatis();
                                mBusStati.setCityName(zone.getString("zoneName"));
                                mBusStati.setOrderNumOne(zone.getString("thisZoneBenYueChengJiaoAmount"));
                                mBusStati.setOrderNumTwo(zone.getString("thisZoneShangYueChengJiaoAmount"));
                                mBusStatis.add(mBusStati);
                                for (int j = 0; j < jo.getJSONArray("places").length(); j++) {
                                    String zoneName = jo.getJSONArray("zones").getJSONObject(i).getString("zoneName");
                                    String cityName = jo.getJSONArray("places").getJSONObject(j).getString("cityName");
                                    if (zoneName.equals(cityName)) {
                                        if (zoneName.equals(cityName)) {
                                            if (Build.VERSION.SDK_INT < 19) {
                                                remove(mJsonArray, j);
                                            } else {
                                                mJsonArray.remove(i);
                                            }
                                        }
                                    }

                                }

                            }
                        //没有成交金额的地区显示为0
						for (int j = 0; j < mJsonArray.length(); j++) {
							String cityName = jo.getJSONArray("places").getJSONObject(j).getString("cityName");
							mBusStati = new BusStatis();
							mBusStati.setCityName(cityName);
							mBusStati.setOrderNumOne("0");
							mBusStati.setOrderNumTwo("0");
							mBusStatis.add(mBusStati);

						}

                            dayBarStyle(mBarChart);
                            monthBarStyle(mBarChartTwo);
                            mAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;

                    default:
                        break;
                }
            }

        }, null, NetworkUtil.CHECK_AGENCY_FOR_ORDER, false, 0);
    }

    public JSONArray remove(JSONArray jsonArray, int index) {
        JSONArray mJsonArray = new JSONArray();

        if (index < 0)
            return mJsonArray;
        if (index > jsonArray.length())
            return mJsonArray;

        for (int i = 0; i < index; i++) {
            try {
                mJsonArray.put(jsonArray.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        for (int i = index + 1; i < jsonArray.length(); i++) {
            try {
                mJsonArray.put(jsonArray.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return mJsonArray;
    }

    /**
     * 设置柱状图的显示样式
     *
     * @param mBarChart
     */
    private void dayBarStyle(BarChart mBarChart) {

        float max;
        if (mJinRiChengJiao / 10000 > mZuoRiChengJiao / 10000) {
            if (mJinRiChengJiao / 10000 <= 6) {
                max = 8;
            } else {
                max = mJinRiChengJiao / 10000 + 10;
            }
        } else {
            if (mZuoRiChengJiao / 10000 <= 6) {
                max = 8;
            } else {
                max = mZuoRiChengJiao / 10000 + 10;
            }
        }

        mBarChart.setDrawBorders(false); // //是否在折线图上添加边框

        // barChart.setDescription("");// 数据描述

        // 如果没有数据的时候，会显示这个，类似ListView的EmptyView
        mBarChart.setNoDataTextDescription("暂无数据");

        mBarChart.setDrawGridBackground(false); // 是否显示表格颜色
        mBarChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度

        mBarChart.setTouchEnabled(false); // 设置是否可以触摸

        mBarChart.setDragEnabled(false);// 是否可以拖拽
        mBarChart.setScaleEnabled(false);// 是否可以缩放
        mBarChart.setPinchZoom(false);//

        // barChart.setBackgroundColor();// 设置背景

        mBarChart.setDrawBarShadow(false);
        mBarChart.setDrawValueAboveBar(true);

        mBarChart.setDescription("");

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mBarChart.setMaxVisibleValueCount(60);
        // scaling can now only be done on x- and y-axis separately
        mBarChart.setPinchZoom(false);

        mBarChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);

        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(2);

        YAxis leftAxis = mBarChart.getAxisLeft();
        leftAxis.setLabelCount(6, true);
        leftAxis.setPosition(YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.resetAxisMaxValue();
        leftAxis.setAxisMaxValue(max);
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mBarChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(6, true);
        rightAxis.resetAxisMaxValue();
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMaxValue(max);
        rightAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

        Legend l = mBarChart.getLegend();
        l.setPosition(LegendPosition.BELOW_CHART_LEFT);
        l.setForm(LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        // X轴设定
        // XAxis xAxis = barChart.getXAxis();
        // xAxis.setPosition(XAxisPosition.BOTTOM);

        // barChart.animateX(2500); // 立即执行的动画,x轴
        mBarChart.animateY(3000);
        // barChart.animateXY(3000, 3000);

        Legend mLegend = mBarChart.getLegend(); // 设置比例图标示

        mLegend.setForm(LegendForm.CIRCLE);// 样式
        mLegend.setFormSize(12f);// 字体
        mLegend.setTextColor(Color.BLACK);// 颜色
    }

    /**
     * 设置柱状图的显示样式
     *
     * @param mBarChart
     */
    private void monthBarStyle(BarChart mBarChart) {

        float max;
        if (mBenYueChengJiao / 10000 > mShangYueChengJiao / 10000) {
            if (mBenYueChengJiao / 10000 <= 6) {
                max = 8;
            } else {
                max = mBenYueChengJiao / 10000 + 10;
            }
        } else {
            if (mShangYueChengJiao / 10000 <= 6) {
                max = 8;
            } else {
                max = mShangYueChengJiao / 10000 + 10;
            }
        }

        mBarChart.setDrawBorders(false); // //是否在折线图上添加边框

        // barChart.setDescription("");// 数据描述

        // 如果没有数据的时候，会显示这个，类似ListView的EmptyView
        mBarChart.setNoDataTextDescription("暂无数据");

        mBarChart.setDrawGridBackground(false); // 是否显示表格颜色
        mBarChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度

        mBarChart.setTouchEnabled(false); // 设置是否可以触摸

        mBarChart.setDragEnabled(false);// 是否可以拖拽
        mBarChart.setScaleEnabled(false);// 是否可以缩放
        mBarChart.setPinchZoom(false);//

        // barChart.setBackgroundColor();// 设置背景

        mBarChart.setDrawBarShadow(false);
        mBarChart.setDrawValueAboveBar(true);

        mBarChart.setDescription("");

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mBarChart.setMaxVisibleValueCount(60);
        // scaling can now only be done on x- and y-axis separately
        mBarChart.setPinchZoom(false);

        mBarChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);

        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(2);

        YAxis leftAxis = mBarChart.getAxisLeft();
        leftAxis.setLabelCount(6, true);
        leftAxis.setPosition(YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.resetAxisMaxValue();
        leftAxis.setAxisMaxValue(max);
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mBarChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(6, true);
        rightAxis.resetAxisMaxValue();
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMaxValue(max);
        rightAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

        Legend l = mBarChart.getLegend();
        l.setPosition(LegendPosition.BELOW_CHART_LEFT);
        l.setForm(LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        // X轴设定
        // XAxis xAxis = barChart.getXAxis();
        // xAxis.setPosition(XAxisPosition.BOTTOM);

        // barChart.animateX(2500); // 立即执行的动画,x轴
        mBarChart.animateY(3000);
        // barChart.animateXY(3000, 3000);

        Legend mLegend = mBarChart.getLegend(); // 设置比例图标示

        mLegend.setForm(LegendForm.CIRCLE);// 样式
        mLegend.setFormSize(12f);// 字体
        mLegend.setTextColor(Color.BLACK);// 颜色
    }

    @Override
    protected void initEvent() {

    }

    private BarData getOneBarData(int count, ArrayList<BarEntry> yValues) {
        ArrayList<String> xValues = new ArrayList<String>();
        xValues.add("今日");
        xValues.add("昨日");
        // ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();
        //
        // for (int i = 0; i < count; i++) {
        // float value = (float) (Math.random() * range/* 100以内的随机数 */) + 3;
        // yValues.add(new BarEntry(value, i));
        // }

        // y轴的数据集合
        BarDataSet barDataSet = new BarDataSet(yValues, "");

        barDataSet.setColor(getResources().getColor(R.color.canvas_blue2));
        barDataSet.setBarSpacePercent(70f);// 设置空白,缩小柱状图的宽度
        ArrayList<BarDataSet> barDataSets = new ArrayList<BarDataSet>();
        barDataSet.setValueFormatter(new ValueFormatter() {//设置单位

            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex,
                                            ViewPortHandler viewPortHandler) {
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                String s = decimalFormat.format(value);
                return s;
            }
        });
        barDataSets.add(barDataSet); // add the datasets
        BarData barData = new BarData(xValues, barDataSets);

        return barData;
    }

    private BarData getTwoBarData(int count, ArrayList<BarEntry> yValues) {
        ArrayList<String> xValues = new ArrayList<String>();
        // for (int i = 0; i < count; i++) {
        // xValues.add("第" + (i + 1) + "季度");
        // }
        xValues.add("本月");
        xValues.add("上月");
        // ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();
        //
        // for (int i = 0; i < count; i++) {
        // float value = (float) (Math.random() * range/* 100以内的随机数 */) + 3;
        // yValues.add(new BarEntry(value, i));
        // }

        // y轴的数据集合
        BarDataSet barDataSet = new BarDataSet(yValues, "");

        barDataSet.setColor(getResources().getColor(R.color.canvas_red));
        barDataSet.setBarSpacePercent(70f);// 设置空白,缩小柱状图的宽度
        ArrayList<BarDataSet> barDataSets = new ArrayList<BarDataSet>();

        barDataSet.setValueFormatter(new ValueFormatter() {

            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex,
                                            ViewPortHandler viewPortHandler) {
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                String s = decimalFormat.format(value);
                return s;
            }
        });
        barDataSets.add(barDataSet); // add the datasets

        BarData barData = new BarData(xValues, barDataSets);

        return barData;
    }
}
