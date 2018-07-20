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
import android.widget.AdapterView;
import android.widget.ListView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.DataReportInOutAdapter;
import com.chewuwuyou.app.bean.BarEntity;
import com.chewuwuyou.app.bean.OrderStateInfo;
import com.chewuwuyou.app.ui.DataReportListActivity;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.gson.Gson;

import net.tsz.afinal.http.AjaxParams;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/1 0001.
 */
public class DataReportInOutFragment extends BaseFragment {
    private int typeKey;
    private int typeDateKey;
    private OrderStateInfo orderStateInfo;
    private DataReportInOutAdapter adapter;
    private ListView lv;

    /**
     * @param type     (TYPE_ROOT,TYPE_IN,TYPE_OUT)
     * @param typeDate (TYPE_7,TYPE_15,TYPE_30)
     * @return
     */
    public static DataReportInOutFragment getEntity(int type, int typeDate) {
        DataReportInOutFragment fragment = new DataReportInOutFragment();
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
        View view = inflater.inflate(R.layout.fragment_data_report_in_out, null);
        lv = (ListView) view.findViewById(R.id.listView1);
        adapter = new DataReportInOutAdapter(getActivity(), typeDateKey, typeKey);
        lv.setAdapter(adapter);
        initView();
        initData();
        initEvent();
        return view;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        getData();
    }

    @Override
    protected void initEvent() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toDetail(position);
            }
        });
        adapter.setItemClickListener(new DataReportInOutAdapter.ItemClickListener() {
            @Override
            public void itemClickListener(int position, BarEntity barEntity) {
                toDetail(position);
            }
        });
    }

    private void toDetail(int position) {
        Intent intent = new Intent(getActivity(), DataReportListActivity.class);
        intent.putExtra("INFO", orderStateInfo);
        intent.putExtra("POSITION", position);
        intent.putExtra("INOUT", typeKey);
        startActivity(intent);
    }

    /**
     * 获取数据
     */
    private void getData() {
        AjaxParams params = new AjaxParams();
        params.put("dayNum", typeDateKey + "");
        params.put("type", (typeKey == 1 ? 2 : 1) + "");
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        Gson gson = new Gson();
                        try {
                            orderStateInfo = gson.fromJson((String) msg.obj, OrderStateInfo.class);
                            adapter.setData(generateDatas());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case Constant.NET_DATA_FAIL:
                        break;
                }
            }
        }, params, NetworkUtil.ORDER_STATISTIC, false, 0);
    }

    /**
     * 构造数据
     *
     * @return
     */
    private BarEntity generateDatas() {
        ArrayList<BarEntry> entriesFD = new ArrayList<BarEntry>();
        ArrayList<BarEntry> entriesWD = new ArrayList<BarEntry>();
        ArrayList<BarEntry> entriesCD = new ArrayList<BarEntry>();
        ArrayList<BarEntry> entriesTD = new ArrayList<BarEntry>();
        List<String> time = new ArrayList<>();
        List<OrderStateInfo.TaskCountsBean> taskCountsBeanList = orderStateInfo.getTaskCounts();
        int fdCount = 0;
        int wdCount = 0;
        int cdCount = 0;
        int tdCount = 0;
        for (int i = 0; i < taskCountsBeanList.size(); i++) {
            OrderStateInfo.TaskCountsBean taskCountsBean = taskCountsBeanList.get(i);
            int fd = typeKey == 2 ? taskCountsBean.getFd() : taskCountsBean.getSd();
            int wd = taskCountsBean.getWd();
            int cd = taskCountsBean.getCd();
            int td = taskCountsBean.getTd();
            fdCount += fd;
            wdCount += wd;
            cdCount += cd;
            tdCount += td;
            entriesFD.add(new BarEntry(fd, i));
            entriesWD.add(new BarEntry(wd, i));
            entriesCD.add(new BarEntry(cd, i));
            entriesTD.add(new BarEntry(td, i));
            try {
                if (i == 0)
                    time.add(new SimpleDateFormat("MM月dd日").format(new SimpleDateFormat("yyyyMMdd").parse(taskCountsBean.getDate())));
                else
                    time.add(new SimpleDateFormat("dd").format(new SimpleDateFormat("yyyyMMdd").parse(taskCountsBean.getDate())));
            } catch (ParseException e) {
                time.add(taskCountsBean.getDate());
            }
        }
        List<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.white));

        List<BarData> barDatas = new ArrayList<>();
        barDatas.add(new BarData(time, getBarDataSet(entriesFD, typeKey == 2 ? "发出的订单" : "收到的订单", colors)));
        barDatas.add(new BarData(time, getBarDataSet(entriesWD, "已完成的订单", colors)));
        barDatas.add(new BarData(time, getBarDataSet(entriesCD, "已撤销的订单", colors)));
        barDatas.add(new BarData(time, getBarDataSet(entriesTD, "已退款的订单", colors)));

        List<Integer> counts = new ArrayList<>();
        counts.add(fdCount);
        counts.add(wdCount);
        counts.add(cdCount);
        counts.add(tdCount);

        BarEntity barEntity = new BarEntity();
        barEntity.barDatas = barDatas;
        barEntity.counts = counts;
        return barEntity;
    }

    /**
     * 构造数据
     *
     * @param entries
     * @param title
     * @param colors
     * @return
     */
    private ArrayList<BarDataSet> getBarDataSet(ArrayList<BarEntry> entries, String title, List<Integer> colors) {
        BarDataSet fdBarDataSet = new BarDataSet(entries, title);
        fdBarDataSet.setBarSpacePercent(30f);
        fdBarDataSet.setColors(colors);
        fdBarDataSet.setValueTextColor(Color.WHITE);
        fdBarDataSet.setBarShadowColor(Color.rgb(203, 203, 203));
        ArrayList<BarDataSet> sets = new ArrayList<BarDataSet>();
        sets.add(fdBarDataSet);
        return sets;
    }
}
