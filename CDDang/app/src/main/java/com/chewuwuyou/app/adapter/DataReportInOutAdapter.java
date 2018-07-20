package com.chewuwuyou.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.BarEntity;
import com.chewuwuyou.app.fragment.DataReportRootFragment;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2016/8/3 0003.
 */

public class DataReportInOutAdapter extends BaseAdapter {
    private BarEntity barEntity;
    private int typeDate = 7;
    private int type = 1;
    private Context context;
    private DecimalFormat fnum = new DecimalFormat("日平均值:  ##0.00单");

    public DataReportInOutAdapter(Context context, int typeDate, int type) {
        this.typeDate = typeDate;
        this.context = context;
        this.type = type;
    }

    public void setData(BarEntity barEntity) {
        this.barEntity = barEntity;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int position) {
        return barEntity.barDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.list_item_barchart, null);
            holder.chart = (BarChart) convertView.findViewById(R.id.chart);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.count = (TextView) convertView.findViewById(R.id.count);
            holder.dayCount = (TextView) convertView.findViewById(R.id.dayCount);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        switch (position) {
            case 0:
                if (type == DataReportRootFragment.TYPE_OUT)
                    holder.title.setText("发出的订单(已付款)");
                else
                    holder.title.setText("收到的订单(已付款)");
                break;
            case 1:
                holder.title.setText("已完成订单");
                break;
            case 2:
                holder.title.setText("已撤销订单");
                break;
            case 3:
                holder.title.setText("已退款订单");
                break;
        }
        holder.chart.setNoDataText("");
        holder.chart.setNoDataTextDescription("没有统计数据");
        if (barEntity == null) return convertView;
        int count = barEntity.counts.get(position);
        BarData data = barEntity.barDatas.get(position);
        holder.count.setText(count + "单");
        float dayCount = (float) count / (float) typeDate;
        holder.dayCount.setText(fnum.format(dayCount));
        data.setValueTextColor(Color.WHITE);
        data.setDrawValues(false);
        holder.chart.setDescription("");
        holder.chart.setDrawGridBackground(false);
        holder.chart.getLegend().setEnabled(false);
        XAxis xAxis = holder.chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(11);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(7f);
        xAxis.setTextColor(Color.WHITE);
        YAxis leftAxis = holder.chart.getAxisLeft();
        leftAxis.setLabelCount(5, false);
        leftAxis.setSpaceTop(15f);
        leftAxis.setDrawGridLines(false);
        YAxis rightAxis = holder.chart.getAxisRight();
        rightAxis.setLabelCount(5, false);
        rightAxis.setSpaceTop(15f);
        rightAxis.setDrawGridLines(false);
        holder.chart.getAxisRight().setEnabled(false);
        holder.chart.getAxisLeft().setEnabled(false);
        holder.chart.setDrawHighlightArrow(false);
        holder.chart.setHighlightPerTapEnabled(false);
        holder.chart.setHighlightPerDragEnabled(false);
        holder.chart.setData(data);
        // do not forget to refresh the chart
//            holder.chart.invalidate();
        holder.chart.animateY(700, Easing.EasingOption.EaseInCubic);
        holder.chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null)
                    itemClickListener.itemClickListener(position, barEntity);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        BarChart chart;
        TextView count, title, dayCount;
    }

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void itemClickListener(int position, final BarEntity barEntity);
    }
}
