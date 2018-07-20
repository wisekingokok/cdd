package com.chewuwuyou.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.CattleAgentsListAdapter;
import com.chewuwuyou.app.bean.TrafficBroker;
import com.chewuwuyou.app.eventbus.EventBusAdapter;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.PullToRefreshBase;
import com.chewuwuyou.app.widget.PullToRefreshListView;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @version 1.1.0
 * @describe:我关注的商家
 * @author:yuyong
 * @created:2014-12-2下午3:59:22
 */
public class MyAttentionActivity extends CDDBaseActivity implements PullToRefreshBase.OnRefreshListener2<ListView> {
    @ViewInject(id = R.id.sub_header_bar_left_ibtn)
    private ImageButton mBackBtn;
    @ViewInject(id = R.id.sub_header_bar_tv)
    private TextView mHeaderTV;
    @ViewInject(id = R.id.empty_tv)
    private TextView mEmptyText;
    @ViewInject(id = R.id.traffic_brokers_list)
    private PullToRefreshListView mPullToRefreshListView;
    @ViewInject(id = R.id.empty_tv)
    private TextView mEmptyTV;
    private List<TrafficBroker> mTrafficBrokers;// 经纪人集合
    private CattleAgentsListAdapter adapter;
    // public static final String TRAFFIC_SER = "com.chewuwuyou.app.bean.traff";
    private RelativeLayout mTitleHeight;// 标题布局高度
    // private int flag = 0;
    private int mServiceType = 0;
    private boolean mIsRefreshing = false;
    private boolean refresh;
    private int mCurcor;


    /**
     * EventBus接收传递的数据
     * +
     *
     * @param busAdapter
     */
    public void onEventMainThread(EventBusAdapter busAdapter) {
        //requestNet(mHandler, null, NetworkUtil.QUERY_BUSINESS_LIST, false, 0);
        getBusinessCollection();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);// 反注册EventBus
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mServiceType = getIntent().getIntExtra("serviceType", 0);
        setContentView(R.layout.traffic_brokers_ac);
        initView();
        initData();
        initEvent();
    }

    @Override
    protected void initView() {
        // 注册EventBus
        EventBus.getDefault().register(this);
        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        isTitle(mTitleHeight);// 根据不同手机判断
        mHeaderTV.setText("收藏商家");
        findViewById(R.id.choose_district_tv).setVisibility(View.GONE);

    }

    @Override
    protected void initData() {
        mTrafficBrokers = new ArrayList<TrafficBroker>();
        adapter = new CattleAgentsListAdapter(MyAttentionActivity.this,
                mTrafficBrokers, mServiceType);
        mPullToRefreshListView.setAdapter(adapter);
        getBusinessCollection();
    }

    @Override
    protected void initEvent() {
        mPullToRefreshListView.setOnRefreshListener(this);
        mBackBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finishActivity();
            }
        });

        mPullToRefreshListView
                .setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        Intent intent = new Intent(MyAttentionActivity.this, BusinessPersonalCenterActivity.class);
                        intent.putExtra("businessId", mTrafficBrokers.get(arg2 - 1).getId() + "");
                        intent.putExtra("isCollect", 1);// 标识是否是由收藏进入
                        intent.putExtra("position", arg2 - 1 + "");
                        startActivity(intent);
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(MyAttentionActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(MyAttentionActivity.this);
    }

    /**
     * 上啦刷新
     *
     * @param refreshView
     */
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        if (!mIsRefreshing) {
            mIsRefreshing = true;
            getCollection(false);
        } else {
            mPullToRefreshListView.onRefreshComplete();
        }
    }

    /**
     * 下拉加载
     *
     * @param refreshView
     */
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        String label = DateUtils.formatDateTime(getApplicationContext(),
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL);

        refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        if (!mIsRefreshing) {
            mIsRefreshing = true;
            getCollection(true);
        } else {
            mPullToRefreshListView.onRefreshComplete();
        }
    }

    /**
     * 根据城市iD 访问数据
     *
     * @param refresh
     */
    private void getCollection(final boolean refresh) {
        if (refresh) {
            mCurcor = 0;
        }
        AjaxParams params = new AjaxParams();
        params.put("start", String.valueOf(mCurcor));
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        List<TrafficBroker> response = TrafficBroker.parseList(msg.obj
                                .toString());
                        mIsRefreshing = false;
                        mPullToRefreshListView.onRefreshComplete();
                        if (refresh) {
                            mPullToRefreshListView.onLoadMore();
                            mTrafficBrokers.clear();
                            if (response == null) {
                                mEmptyText.setVisibility(View.VISIBLE);
                            }
                        } else {
                            if (response == null) {
                                ToastUtil.toastShow(MyAttentionActivity.this,
                                        "没有更多数据了");
                                return;
                            }
                        }
                        mTrafficBrokers.addAll(response);
                        mCurcor = mTrafficBrokers.size();
                        if (response.size() < 1) {
                            if (msg.obj.toString().equals("[]")) {
                                ToastUtil.toastShow(MyAttentionActivity.this,
                                        "没有更多数据了");
                            }
                            mPullToRefreshListView.onLoadComplete();//加载完成，不再开启刷新
                        }
                        adapter.notifyDataSetChanged();
                        break;
                    default:
                        mPullToRefreshListView.onRefreshComplete();
                        mIsRefreshing = false;
                        break;
                }
            }
        }, params, NetworkUtil.QUERY_BUSINESS_LIST, false, 1);
    }

    /**
     * 收藏商家
     */
    private void getBusinessCollection() {
        mCurcor = 0;
        AjaxParams params = new AjaxParams();
        params.put("start", String.valueOf(mCurcor));
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        mIsRefreshing = false;
                        mPullToRefreshListView.onRefreshComplete();
                        mPullToRefreshListView.onLoadMore();
                        mTrafficBrokers.clear();
                        // 为超哥的锅做兼容
                        JSONArray jsonArray = null;

                        List<TrafficBroker> response = TrafficBroker.parseList(msg.obj.toString());
                        mTrafficBrokers.addAll(response);
                        if (mTrafficBrokers.size() != 0) {
                            mPullToRefreshListView.setVisibility(View.VISIBLE);
                            mEmptyText.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        } else {
                            mPullToRefreshListView.setVisibility(View.GONE);
                            mEmptyText.setVisibility(View.VISIBLE);
                        }
                        mCurcor = mTrafficBrokers.size();
                        adapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        }, params, NetworkUtil.QUERY_BUSINESS_LIST, false, 0);
    }

}
