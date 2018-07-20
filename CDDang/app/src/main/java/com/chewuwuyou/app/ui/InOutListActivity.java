package com.chewuwuyou.app.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.InOutInfo;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.google.gson.Gson;

import net.tsz.afinal.http.AjaxParams;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/8/4 0004.
 */
public class InOutListActivity extends CDDBaseActivity {
    private ListView listView;
    private ImageButton back;
    private TextView title;
    private TextView nullText;
    private InOutInfo inOutInfo;
    private int typeDateKey;
    private InOutInfo mInOutInfo;
    private InOutListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_out_list);
        inOutInfo = (InOutInfo) getIntent().getSerializableExtra("inOutInfo");
        typeDateKey = getIntent().getIntExtra("typeDateKey", 0);
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
        title.setText("收支记录数据");
        listView.setAdapter(adapter = new InOutListAdapter(this));
        if (inOutInfo == null || inOutInfo.getInOutData() == null || inOutInfo.getInOutData().size() <= 0) {
            nullText.setVisibility(View.VISIBLE);
            return;
        } else {
            nullText.setVisibility(View.GONE);
            adapter.setData(inOutInfo);
        }
//        getData();
    }

    /**
     * 前几天的年月日（请求数据参数）
     *
     * @param day
     * @return
     */
    private String getDate(int day) {
        return new SimpleDateFormat("yyyyMMdd").format(new Date(new Date().getTime() - day * 24 * 60 * 60 * 1000));
    }

    private void getData() {
        AjaxParams params = new AjaxParams();
        params.put("statrtDate", getDate(1));
        params.put("endDate", getDate(typeDateKey + 1));
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        Gson gson = new Gson();
                        try {
                            mInOutInfo = gson.fromJson((String) msg.obj, InOutInfo.class);
                            if (mInOutInfo == null || mInOutInfo.getInOutData() == null || mInOutInfo.getInOutData().size() <= 0) {
                                nullText.setVisibility(View.VISIBLE);
                                return;
                            } else {
                                nullText.setVisibility(View.GONE);
                                adapter.setData(mInOutInfo);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            nullText.setVisibility(View.VISIBLE);
                        }
                        break;
                    case Constant.NET_DATA_FAIL:
                        nullText.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }, params, NetworkUtil.IN_OUT_RECORD_DATA, false, 0);
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

    class InOutListAdapter extends BaseAdapter {
        private Context context;
        private List<InOutInfo.InOutDataBean> inOutDataBeen;
        private DecimalFormat decimalFormat = new DecimalFormat("###,##0.00元");

        public InOutListAdapter(Context context, InOutInfo inOutInfo) {
            this.context = context;
            this.inOutDataBeen = mInOutInfo.getInOutData();
        }

        public InOutListAdapter(Context context) {
            this.context = context;
            this.inOutDataBeen = new ArrayList<>();
        }

        public void setData(InOutInfo inOutInfo) {
            this.inOutDataBeen = inOutInfo.getInOutData();
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return inOutDataBeen.size();
        }

        @Override
        public Object getItem(int position) {
            return inOutDataBeen.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder(convertView = LayoutInflater.from(context).inflate(R.layout.item_in_out_list, null));
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            InOutInfo.InOutDataBean bean = inOutDataBeen.get(position);
            viewHolder.in.setText("收入金额:" + decimalFormat.format(bean.getIncome()));
            viewHolder.out.setText("支出金额:" + decimalFormat.format(bean.getPay()));
            try {
                viewHolder.time.setText(new SimpleDateFormat("yyyy年MM月dd日").format(new SimpleDateFormat("yyyyMMdd").parse(bean.getDate())));
            } catch (ParseException e) {
                e.printStackTrace();
                viewHolder.time.setText(bean.getDate());
            }
            return convertView;
        }

        class ViewHolder {
            public TextView in;
            public TextView out;
            public TextView time;

            public ViewHolder(View view) {
                in = (TextView) view.findViewById(R.id.in);
                out = (TextView) view.findViewById(R.id.out);
                time = (TextView) view.findViewById(R.id.time);
            }
        }
    }
}
