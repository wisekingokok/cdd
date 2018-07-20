package com.chewuwuyou.app.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.OrderStateInfo;
import com.chewuwuyou.app.fragment.DataReportRootFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Administrator on 2016/8/4 0004.
 */
public class DataReportListActivity extends CDDBaseActivity {
    private ListView listView;
    private ImageButton back;
    private TextView title;
    private TextView nullText;
    private int type;
    private int typeKey;
    private OrderStateInfo orderStateInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_report_list);
        orderStateInfo = (OrderStateInfo) getIntent().getSerializableExtra("INFO");
        type = getIntent().getIntExtra("POSITION", 0);
        typeKey = getIntent().getIntExtra("INOUT", 0);
        initView();
        initData();
        initEvent();
    }

    @Override
    protected void initView() {
        listView = (ListView) findViewById(R.id.listView);
        back = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
        title = (TextView) findViewById(R.id.sub_header_bar_tv);
        nullText = (TextView) findViewById(R.id.nullText);
    }

    @Override
    protected void initData() {
        switch (type) {
            case 0:
                if (typeKey == DataReportRootFragment.TYPE_OUT)
                    title.setText("发出的订单");
                else
                    title.setText("收到的订单");
                break;
            case 1:
                title.setText("已完成订单");
                break;
            case 2:
                title.setText("已撤销订单");
                break;
            case 3:
                title.setText("已退款订单");
                break;
        }

        if (orderStateInfo == null || orderStateInfo.getTaskCounts() == null || orderStateInfo.getTaskCounts().size() <= 0) {
            nullText.setVisibility(View.VISIBLE);
            return;
        } else {
            nullText.setVisibility(View.GONE);
        }
        listView.setAdapter(new DataReportListAdapter(this, orderStateInfo, type, typeKey));
    }

    @Override
    protected void initEvent() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });
    }

    class DataReportListAdapter extends BaseAdapter {
        private Context context;
        private List<OrderStateInfo.TaskCountsBean> taskCounts;
        private int type;
        private int typeKey;

        public DataReportListAdapter(Context context, OrderStateInfo orderStateInfo, int type, int typeKey) {
            this.context = context;
            this.type = type;
            this.taskCounts = orderStateInfo.getTaskCounts();
            this.typeKey = typeKey;
        }

        @Override
        public int getCount() {
            return taskCounts.size();
        }

        @Override
        public Object getItem(int position) {
            return taskCounts.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder(convertView = LayoutInflater.from(context).inflate(R.layout.item_data_report_list, null));
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            OrderStateInfo.TaskCountsBean bean = taskCounts.get(position);
            int size = 0;
            switch (type) {
                case 0:
                    if(typeKey == DataReportRootFragment.TYPE_OUT){//判断是收到还是发出的订单
                        size = bean.getFd();
                    }else{
                        size = bean.getSd();
                    }
                    break;
                case 1:
                    size = bean.getWd();
                    break;
                case 2:
                    size = bean.getCd();
                    break;
                case 3:
                    size = bean.getTd();
                    break;
            }

            viewHolder.size.setText(size + "单");
            try {
                viewHolder.time.setText(new SimpleDateFormat("yyyy年MM月dd日").format(new SimpleDateFormat("yyyyMMdd").parse(bean.getDate())));
            } catch (ParseException e) {
                e.printStackTrace();
                viewHolder.time.setText(bean.getDate());
            }
            return convertView;
        }

        class ViewHolder {
            public TextView size;
            public TextView time;

            public ViewHolder(View view) {
                size = (TextView) view.findViewById(R.id.size);
                time = (TextView) view.findViewById(R.id.time);
            }
        }
    }
}
