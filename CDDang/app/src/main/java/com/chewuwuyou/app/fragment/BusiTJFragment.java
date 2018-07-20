package com.chewuwuyou.app.fragment;

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
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.TradingOrderAdapter;
import com.chewuwuyou.app.bean.BusStatis;
import com.chewuwuyou.app.tools.MyListView;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.JsonUtil;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;

/**
 * 省代理商家统计
 *
 * @author yuyong
 */
@SuppressLint("NewApi")
public class BusiTJFragment extends BaseFragment {

    private View mView;
    private MyListView mProvinceDataList;
    private TradingOrderAdapter mAdapter;
    private List<BusStatis> mBusStatis;
    private PieChart mPieChart;// 饼图对省代商家进行统计

    private TextView mBarOneDescTV;// 图形一描述
    private TextView mBarTwoDescTV;// 图形二描述
    private TextView mBarOneTitleTV;// 图形一标题
    private ArrayList<String> mXValues = new ArrayList<String>();
    private ArrayList<Entry> mYValues = new ArrayList<Entry>();
    private BusStatis mBusStati;// 订单单项统计
    private JSONArray mJsonArray;
    private LinearLayout mProvinceTitleLL;//标题区代隐藏

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.business_tj_ft, null);

        initView();
        initData();
        initEvent();
        return mView;
    }

    @Override
    protected void initView() {
        mBarOneDescTV = (TextView) mView.findViewById(R.id.image_one_desc_tv);
        mBarTwoDescTV = (TextView) mView.findViewById(R.id.image_two_desc_tv);
        mBarOneTitleTV = (TextView) mView.findViewById(R.id.image_title_one_tv);
        mProvinceDataList = (MyListView) mView
                .findViewById(R.id.statistics_list);
        mPieChart = (PieChart) mView.findViewById(R.id.pie_chart);
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

        Legend l = mPieChart.getLegend();
        l.setPosition(LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(0f);
        l.setYEntrySpace(7f);
        l.setYOffset(0f);
        mProvinceTitleLL = (LinearLayout) mView.findViewById(R.id.province_title_ll);

    }

    @Override
    protected void initData() {
        if (CacheTools.getUserData("daiLitype").equals("3")) {//当商家角色为区代理时不显示列表信息
            mBarTwoDescTV.setVisibility(View.GONE);
            mProvinceTitleLL.setVisibility(View.GONE);
        }
        mBarOneDescTV.setText("四川省商家统计");
        mBarTwoDescTV.setText("四川省代理各区域商家总量统计");
        mBarOneTitleTV.setText("商家总数");
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
                            MyLog.i("YUY", "省代数据返回 = " + msg.obj);
                            JSONObject jo = new JSONObject(msg.obj.toString());
                            mBarOneTitleTV.setText("商家总数 " + jo.getString("total"));
                            mBarOneDescTV.setText(jo.getString("myZone")
                                    + "代理区域商家统计");
                            mBarTwoDescTV.setText(jo.getString("myZone")
                                    + "代理各区域商家统计");

                            float typeA = jo.getInt("typeA");
                            float typeB = jo.getInt("typeB");

                            if (typeA != 0) {
                                mXValues.add("品牌商家 " + jo.getInt("typeA"));
                                mYValues.add(new Entry(typeA, 0));
                            }
                            if (typeB != 0) {
                                mXValues.add("会员商家 " + jo.getInt("typeB"));
                                mYValues.add(new Entry(typeB, 1));
                            }
                            setData(2, mXValues, mYValues);
                            for (PieDataSet set : mPieChart.getData().getDataSets()) {
                                set.setDrawValues(!set.isDrawValuesEnabled());
                            }

                            mJsonArray = jo.getJSONArray("places");
                            MyLog.i("YUY", "前数据长度 = " + mJsonArray.length());
                            for (int i = 0; i < jo.getJSONArray("zones").length(); i++) {// 区域订单统计
                                JSONObject zone = jo.getJSONArray("zones")
                                        .getJSONObject(i);
                                mBusStati = new BusStatis();
                                mBusStati.setCityName(zone.getString("zoneName"));
                                mBusStati.setOrderNumOne(String.valueOf(zone
                                        .getInt("thisCityA")));
                                mBusStati.setOrderNumTwo(String.valueOf(zone
                                        .getInt("thisCityB")));
                                mBusStatis.add(mBusStati);
                                for (int j = 0; j < jo.getJSONArray("places")
                                        .length(); j++) {
                                    String zoneName = jo.getJSONArray("zones")
                                            .getJSONObject(i).getString("zoneName");
                                    String cityName = jo.getJSONArray("places")
                                            .getJSONObject(j).getString("cityName");
                                    if (zoneName.equals(cityName)) {
                                        if (Build.VERSION.SDK_INT < 19) {
                                            try {
                                                JsonUtil.JSONArrayRemove(i, mJsonArray);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            mJsonArray.remove(j);
                                        }
                                    }
                                }
                            }
                            MyLog.i("YUY", "后数据长度 = " + mJsonArray.length());
                            for (int j = 0; j < mJsonArray.length(); j++) {
                                String cityName = jo.getJSONArray("places")
                                        .getJSONObject(j).getString("cityName");
                                mBusStati = new BusStatis();
                                mBusStati.setCityName(cityName);
                                mBusStati.setOrderNumOne("0");
                                mBusStati.setOrderNumTwo("0");
                                mBusStatis.add(mBusStati);

                            }
                            mAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;

                    default:
                        break;
                }
            }

        }, null, NetworkUtil.CHECK_AGENCY_FOR_BUS, false, 0);
    }

    @Override
    protected void initEvent() {

    }

    private void setData(int count, ArrayList<String> xValues,
                         ArrayList<Entry> yValues) {

        PieDataSet dataSet = new PieDataSet(yValues, "");
        dataSet.setSliceSpace(0f);// 设置个饼状图之间的距离
        dataSet.setSelectionShift(5f);
        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        int[] LIBERT_COLORS = {Color.rgb(255, 188, 8), Color.rgb(4, 151, 217)};
        for (int c : LIBERT_COLORS) {
            colors.add(c);

        }

        // colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        dataSet.setSelectionShift(1f);

        PieData data = new PieData(xValues, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        mPieChart.setData(data);

        // undo all highlights
        mPieChart.highlightValues(null);

        mPieChart.invalidate();
    }
}
