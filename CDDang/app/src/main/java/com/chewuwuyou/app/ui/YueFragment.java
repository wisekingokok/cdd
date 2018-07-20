package com.chewuwuyou.app.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.YueAdapter;
import com.chewuwuyou.app.bean.YueItem;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.widget.HackyViewPager;
import com.chewuwuyou.app.widget.PullToRefreshBase;
import com.chewuwuyou.app.widget.PullToRefreshBase.OnRefreshListener2;
import com.chewuwuyou.app.widget.PullToRefreshListView;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class YueFragment extends BaseFragment implements OnRefreshListener2<ListView> {
    private Activity mActivity;
    private View mContentView;
    private List<YueItem> mData;
    private YueAdapter mAdapter;
    private PullToRefreshListView mPullToRefreshListView;
    private boolean mIsRefreshing = false;// 上拉下拉要用
    private int mCurcor;// 分页要用
    private String mTitle;//分类标题
    private String mYueCnt;
    private String mType = null;
    private String mRegion = null;
    private RelativeLayout mContainer;
    private HackyViewPager mExpandedImageViewPager;
    private boolean mIsSetEmptyTV = false;
    //	private RelativeLayout mTitleHeight;//标题布局高度
    private RegionChangedReceiver mRegionChangedReceiver;

    private class RegionChangedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            mRegion = intent.getStringExtra("region");
            getAllYue(true);
        }
    }

    public YueFragment() {
        super();
    }

    @SuppressLint("ValidFragment")
    public YueFragment(Activity activity, String title, String type, String region) {
        this.mActivity =activity;
        this.mTitle = title;
        this.mType = type;
        this.mRegion = region;
    }

    public void setRegion(String region) {
        mRegion = region;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mContentView = LayoutInflater.from(mActivity).inflate(R.layout.yues_page_layout, null);

        initView();
        initData();
        initEvent();
        getAllYue(true);
        if (mRegionChangedReceiver == null) {
            mRegionChangedReceiver = new RegionChangedReceiver();
        }
        mActivity.registerReceiver(mRegionChangedReceiver, new IntentFilter(
                "region_changed"));
        return mContentView;
    }

    private void getAllYue(final boolean refresh) {
        if (refresh)
            mCurcor = 0;
        AjaxParams params = new AjaxParams();
        mPullToRefreshListView.setRefreshing();
        params.put("region", mRegion);
        params.put("type", mType);
        params.put("start", String.valueOf(mCurcor));
        requestNet(new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (!mIsSetEmptyTV) {
                    TextView tv = new TextView(mActivity);
                    tv.setGravity(Gravity.CENTER);
                    tv.setText("没有活动");
                    tv.setTextColor(mActivity.getResources().getColor(R.color.empty_text_color));
                    mPullToRefreshListView.setEmptyView(tv);
                    mIsSetEmptyTV = true;
                }
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        mIsRefreshing = false;
                        mPullToRefreshListView.onRefreshComplete();
                        List<YueItem> response = null;
                        try {
                            JSONObject data = new JSONObject(msg.obj.toString());
                            mYueCnt = data.getString("yueCnt");
                            response = YueItem.parseList(data.getString("yue").toString());
                            mAdapter.updateTitle(new StringBuilder().append(mTitle).append("(").append(mYueCnt).append(")").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (response != null) {
                            if (refresh) {
                                mPullToRefreshListView.onLoadMore();
                                mData.clear();
                            }
                            mData.addAll(response);
                            mAdapter.notifyDataSetChanged();
                            mCurcor = mData.size();
                            if (response.size() < Constant.QUAN_PAGE_SIZE) {
                                mPullToRefreshListView.onLoadComplete();
                            }
                        }
                        break;
                    default:
                        mPullToRefreshListView.onRefreshComplete();
                        mIsRefreshing = false;
                        break;
                }
            }

        }, params, NetworkUtil.ALL_YUE, false, 1);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void initView() {
        mPullToRefreshListView = (PullToRefreshListView) mContentView.findViewById(R.id.yue_list);
        mExpandedImageViewPager = (HackyViewPager) mContentView.findViewById(R.id.yue_expanded_image_viewpager);
        mContainer = (RelativeLayout) mContentView.findViewById(R.id.yue_container);

    }

    @Override
    protected void initData() {
        mData = new ArrayList<YueItem>();
        mAdapter = new YueAdapter(mActivity, mData, mExpandedImageViewPager, mContainer, new StringBuilder().append(mTitle).append("(").append(mYueCnt).append(")").toString());
        mPullToRefreshListView.setAdapter(mAdapter);
    }

    @Override
    protected void initEvent() {
        mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
                Intent intent = new Intent(mActivity, YueDetailActivity.class);
                intent.putExtra("id", mData.get(arg2 - 1).getId());
                intent.putExtra("ziji", mData.get(arg2 - 1).getZiji());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        String label = DateUtils.formatDateTime(mActivity.getApplicationContext(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

        refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

        if (!mIsRefreshing) {
            mIsRefreshing = true;
            getAllYue(true);
        } else {
            mPullToRefreshListView.onRefreshComplete();
        }
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        if (!mIsRefreshing) {
            mIsRefreshing = true;
            getAllYue(false);
        } else {
            mPullToRefreshListView.onRefreshComplete();
        }
    }

    @Override
    public void onDestroy() {
        MyLog.d("xuhan", "--------------------------------------onDestroy");
        super.onDestroy();
        mActivity.unregisterReceiver(mRegionChangedReceiver);
    }

    @Override
    public void onDestroyView() {
        MyLog.d("xuhan", "--------------------------------------onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        MyLog.d("xuhan", "--------------------------------------onDetach");
        super.onDetach();
    }

    @Override
    public void onPause() {
        MyLog.d("xuhan", "--------------------------------------onPause");
        super.onPause();
    }

    @Override
    public void onResume() {
        MyLog.d("xuhan", "--------------------------------------onResume");
        super.onResume();
        getAllYue(true);
    }

    @Override
    public void onStart() {
        MyLog.d("xuhan", "--------------------------------------onStart");
        super.onStart();
    }

    @Override
    public void onStop() {
        MyLog.d("xuhan", "--------------------------------------onStop");
        super.onStop();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        MyLog.d("xuhan", "--------------------------------------onViewCreated");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        MyLog.d("xuhan", "--------------------------------------onViewStateRestored");
        super.onViewStateRestored(savedInstanceState);
    }
}
