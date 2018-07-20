package com.chewuwuyou.app.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.InOutInfo;
import com.chewuwuyou.app.ui.InOutListActivity;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import net.tsz.afinal.http.AjaxParams;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/1 0001.
 */
public class DataReportFragment extends BaseFragment {
    private int typeKey;
    private int typeDateKey;

    private View rootView;
    private PieChart mChart;
    private TextView turnover;
    private TextView income;
    private TextView inCount;
    private TextView outcome;
    private TextView outCount;
    private LineChart chartLine;
    private InOutInfo inOutInfo;
    private DecimalFormat decimalFormat = new DecimalFormat("###,##0.00");

    /**
     * @param type     (TYPE_ROOT,TYPE_IN,TYPE_OUT)
     * @param typeDate (TYPE_7,TYPE_15,TYPE_30)
     * @return
     */
    public static DataReportFragment getEntity(int type, int typeDate) {
        DataReportFragment fragment = new DataReportFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(DataReportRootFragment.TYPE_KEY, type);
        bundle.putInt(DataReportRootFragment.TYPE_DATE_KEY, typeDate);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        typeKey = bundle.getInt(DataReportRootFragment.TYPE_KEY);
        typeDateKey = bundle.getInt(DataReportRootFragment.TYPE_DATE_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_data_report, null);
        initView();
        initData();
        initEvent();
        return rootView;
    }

    @Override
    protected void initView() {
        mChart = (PieChart) rootView.findViewById(R.id.chart1);
        turnover = (TextView) rootView.findViewById(R.id.turnover);
        income = (TextView) rootView.findViewById(R.id.income);
        inCount = (TextView) rootView.findViewById(R.id.inCount);
        outcome = (TextView) rootView.findViewById(R.id.outcome);
        outCount = (TextView) rootView.findViewById(R.id.outCount);
        chartLine = (LineChart) rootView.findViewById(R.id.chartLine);

        mChart.setUsePercentValues(true);
        mChart.setDescription("");
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColorTransparent(true);
        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);
        mChart.setDrawCenterText(true);
        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(false);
//        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawHoleEnabled(false);
        mChart.setDrawCenterText(false);
        mChart.setNoDataText("");
        mChart.setNoDataTextDescription("没有统计记录!");
        mChart.animateY(1500, Easing.EasingOption.EaseInOutQuad);
        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);

//        chartLine.setOnChartValueSelectedListener(this);
        chartLine.setDescription("");
        chartLine.setNoDataText("");
        chartLine.setNoDataTextDescription("没有统计记录!");
        chartLine.setTouchEnabled(true);
        chartLine.setDragEnabled(true);
        chartLine.setScaleEnabled(true);
        chartLine.setHighlightPerDragEnabled(false);
        chartLine.setDrawGridBackground(false);
        chartLine.setPinchZoom(true);
        chartLine.setHighlightPerTapEnabled(false);
        chartLine.animateY(1000);
        Legend legend = chartLine.getLegend();
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setTextSize(11f);
        legend.setTextColor(Color.BLACK);
        legend.setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);
        legend.setStackSpace(10);
        XAxis xAxis = chartLine.getXAxis();
        xAxis.setTextSize(7f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawGridLines(true);
        xAxis.setDrawAxisLine(true);
        xAxis.setSpaceBetweenLabels(2);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    }

    @Override
    protected void initData() {
        getData();
    }

    @Override
    protected void initEvent() {
        chartLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InOutListActivity.class);
                intent.putExtra("inOutInfo", inOutInfo);
                intent.putExtra("typeDateKey", typeDateKey);
                startActivity(intent);
            }
        });
    }

    private void getData() {
        AjaxParams params = new AjaxParams();
        params.put("dayNum", typeDateKey + "");
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        Gson gson = new Gson();
                        try {
                            inOutInfo = gson.fromJson((String) msg.obj, InOutInfo.class);
                            if (inOutInfo == null) return;
                            turnover.setText("营业额:" + decimalFormat.format(inOutInfo.getYye()) + "元");
                            income.setText("收入:" + decimalFormat.format(inOutInfo.getIncomeAll()) + "元");
                            inCount.setText("收到:" + inOutInfo.getSdAll() + "单");
                            outcome.setText("支出:" + decimalFormat.format(inOutInfo.getPayAll()) + "元");
                            outCount.setText("发出:" + inOutInfo.getFdAll() + "单");
                            setData();
                            setDataLine();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case Constant.NET_DATA_FAIL:
                        break;
                }
            }
        }, params, NetworkUtil.IN_OUT_STATISTIC, false, 0);
    }


    private void setData() {
        if (inOutInfo.getSdAll() == 0 && inOutInfo.getFdAll() == 0) return;
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        yVals1.add(new Entry(inOutInfo.getSdAll(), 0));
        yVals1.add(new Entry(inOutInfo.getFdAll(), 1));

        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("收到订单");
        xVals.add("发出订单");

        PieDataSet dataSet = new PieDataSet(yVals1, "收发订单占比");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.rgb(101, 180, 234));
        colors.add(Color.rgb(77, 212, 237));
        dataSet.setColors(colors);
        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);
        // undo all highlights
        mChart.highlightValues(null);
        mChart.invalidate();
    }

    /**
     * #52C2EA   82,194,234
     * #8DB7EC   141,183,236
     */
    private void setDataLine() {
        List<InOutInfo.InOutDataBean> inOutDataBeans = inOutInfo.getInOutData();
        if (inOutDataBeans == null) return;
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<Entry> yVals3 = new ArrayList<Entry>();
        float max = 0;//记录最大值(用于设置图表范围)
        float min = 0;//记录最小值
        for (int i = 0; i < inOutDataBeans.size(); i++) {
            InOutInfo.InOutDataBean inOutDataBean = inOutDataBeans.get(i);
            float pay = inOutDataBean.getPay();
            max = max > pay ? max : pay;
            min = min < pay ? min : pay;
            float income = inOutDataBean.getIncome();
            max = max > income ? max : income;
            min = min < income ? min : income;
            yVals1.add(new Entry(pay, i));
            yVals3.add(new Entry(income, i));
            try {
                if (i == 0)
                    xVals.add(new SimpleDateFormat("MM月dd日").format(new SimpleDateFormat("yyyyMMdd").parse(inOutDataBean.getDate())));
                else
                    xVals.add(new SimpleDateFormat("dd").format(new SimpleDateFormat("yyyyMMdd").parse(inOutDataBean.getDate())));
            } catch (ParseException e) {
                xVals.add(inOutDataBean.getDate());
            }
        }

        YAxis leftAxis = chartLine.getAxisLeft();
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setAxisMaxValue(max > 100 ? max * 1.2f : 100f);
        leftAxis.setDrawGridLines(false);
        leftAxis.setStartAtZero(true);
        leftAxis.setAxisMinValue(min > 0 ? 0 : min * 0.2f);
        YAxis rightAxis = chartLine.getAxisRight();
        rightAxis.setTextColor(Color.RED);
        rightAxis.setAxisMaxValue(max > 100 ? max * 1.2f : 100f);
        rightAxis.setAxisMinValue(min > 0 ? 0 : min * 0.2f);
        rightAxis.setStartAtZero(true);
        rightAxis.setDrawGridLines(false);
        chartLine.getAxisRight().setEnabled(false);

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals1, "支出金额");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(Color.rgb(82, 194, 234));
        set1.setCircleColor(Color.WHITE);
        set1.setLineWidth(2f);
        set1.setCircleSize(3f);
        set1.setFillAlpha(65);
        set1.setFillColor(Color.rgb(82, 194, 234));
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setDrawCircleHole(false);
        set1.setDrawValues(false);
        set1.setDrawFilled(true);
        set1.setDrawCircles(false);
        set1.setDrawCubic(true);
//        set1.setCircleHoleColor(Color.WHITE);

        // create a dataset and give it a type
        LineDataSet set3 = new LineDataSet(yVals3, "收入金额");
        set3.setAxisDependency(YAxis.AxisDependency.RIGHT);
        set3.setColor(Color.rgb(141, 183, 236));
        set3.setCircleColor(Color.WHITE);
        set3.setLineWidth(2f);
        set3.setCircleSize(3f);
        set3.setFillAlpha(65);
        set3.setFillColor(Color.rgb(141, 183, 236));
        set3.setDrawCircleHole(false);
        set3.setDrawValues(false);
        set3.setDrawFilled(true);
        set3.setDrawCircles(false);
        set3.setDrawCubic(true);
        set3.setHighLightColor(Color.rgb(244, 117, 117));

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets
        dataSets.add(set3);

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(9f);

        // set data
        chartLine.setData(data);
    }
}
