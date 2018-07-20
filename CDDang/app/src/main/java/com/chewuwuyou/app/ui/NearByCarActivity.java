package com.chewuwuyou.app.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.NearByCarFriendAdapter;
import com.chewuwuyou.app.bean.NearByFriend;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.widget.PullToRefreshBase;
import com.chewuwuyou.app.widget.PullToRefreshBase.OnRefreshListener2;
import com.chewuwuyou.app.widget.PullToRefreshListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:
 * @author:yuyong
 * @date:2015-8-17下午5:29:36
 * @version:1.2.1
 */
@SuppressLint("HandlerLeak")
public class NearByCarActivity extends CDDBaseActivity implements
        OnRefreshListener2<ListView>, OnClickListener {
    @ViewInject(id = R.id.sub_header_bar_left_ibtn)
    private ImageButton mBackBtn;
    @ViewInject(id = R.id.sub_header_bar_tv)
    private TextView mHeaderTV;
    @ViewInject(id = R.id.sub_header_bar_right_tv)
    private TextView mFriendTypeTV;
    @ViewInject(id = R.id.nullText)
    private TextView nullText;
    private String sex = "";
    private PullToRefreshListView mPullToRefreshListView;
    private List<NearByFriend> mData;

    private int mCurcor = 0;// 第几次请求
    private NearByCarFriendAdapter mAdapter;
    private boolean mIsRefreshing = false;

    private JSONObject jo;
    private RelativeLayout mTitleHeight;// 标题布局高度

    private String minAge, maxAge;// 筛选-年龄段

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearby_car_ac);
        initView();
        initData();
        initEvent();
    }

    @Override
    protected void initView() {
        // TODO Auto-generated method stub
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.nearby_friend_list);
        mFriendTypeTV.setText("筛选");
        mFriendTypeTV.setVisibility(View.VISIBLE);

    }

    @Override
    protected void initData() {
        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        isTitle(mTitleHeight);// 根据不同手机判断
        mHeaderTV.setText(R.string.nearby_car);
        getMyLocation();
        if (mData == null) {
            mData = new ArrayList<NearByFriend>();
        }
        if (mAdapter == null) {
            mAdapter = new NearByCarFriendAdapter(NearByCarActivity.this, mData);
        }
        mPullToRefreshListView.setAdapter(mAdapter);
        mPullToRefreshListView.onLoadMore();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(true);
    }

    @Override
    protected void initEvent() {
        // TODO Auto-generated method stub
        mBackBtn.setOnClickListener(this);
        mPullToRefreshListView.setOnRefreshListener(this);
        mFriendTypeTV.setOnClickListener(this);
        mPullToRefreshListView
                .setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(NearByCarActivity.this,
                                PersonalHomeActivity2.class);
                        intent.putExtra("userId", Integer.parseInt(mData.get(arg2 - 1).getUserId()) + "");
                        intent.putExtra(PersonalHomeActivity2.NEW_FRIENDID, mData.get(arg2 - 1).getUserId());

                        startActivity(intent);
                    }
                });
    }

    /**
     * 获取附近的车友
     */
    private void getData(final boolean isRefresh) {
        AjaxParams params = new AjaxParams();
        params.put("lat", CacheTools.getUserData("Lat"));
        params.put("lng", CacheTools.getUserData("Lng"));
        params.put("sex", sex);
        params.put("start", isRefresh ? (0 + "") : (mAdapter.getCount() + ""));
        if (!TextUtils.isEmpty(minAge) && !TextUtils.isEmpty(maxAge)) {
            params.put("minAge", minAge);
            params.put("maxAge", maxAge);
        }
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                mPullToRefreshListView.onRefreshComplete();
                mIsRefreshing = false;
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        mIsRefreshing = false;
                        mPullToRefreshListView.onRefreshComplete();
                        List<NearByFriend> nearByFriends = null;
                        try {
                            Gson gson = new Gson();
                            nearByFriends = gson.fromJson(msg.obj.toString(), new TypeToken<List<NearByFriend>>() {
                            }.getType());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (isRefresh)
                            mData.clear();
                        if (nearByFriends != null) {
                            mData.addAll(nearByFriends);
                            mAdapter.updateData(mData);
                            mAdapter.notifyDataSetChanged();
                            mCurcor = mData.size();
                        }
                        break;
                }
                if (mAdapter.getCount() <= 0)
                    nullText.setVisibility(View.VISIBLE);
                else
                    nullText.setVisibility(View.GONE);
            }
        }, params, NetworkUtil.NEARBY_CAR_FRIEND, false, 0);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        String label = DateUtils.formatDateTime(getApplicationContext(),
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL);
        refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        if (!mIsRefreshing) {
            mIsRefreshing = true;
            getData(true);
        } else {
            mPullToRefreshListView.onRefreshComplete();
        }

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        // TODO Auto-generated method stub
        if (!mIsRefreshing) {
            mIsRefreshing = true;
//            url = NetworkUtil.MORE_NEARBY_CAR_FRIEND;
            getData(false);
        } else {
            mPullToRefreshListView.onRefreshComplete();
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn:
                finishActivity();
                break;
            case R.id.sub_header_bar_right_tv:
                Intent intent = new Intent(NearByCarActivity.this,
                        SameBrandFilterActivity.class);
                intent.putExtra("nearCar", "1");// 1表示从筛选附近人
                startActivityForResult(intent, 20);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 20) {

            if (resultCode == SameBrandFilterActivity.RESULT_CODE) {
                Bundle bundle = data.getExtras();
                minAge = bundle.getString("minAge");
                maxAge = bundle.getString("maxAge");
                if (bundle.getString("sex").equals("")) {
                    sex = "";
                } else if (bundle.getString("sex").equals("0")) {
                    sex = "0";
                } else {
                    sex = "1";
                }
                mData.clear();
                mAdapter.updateData(mData);
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
