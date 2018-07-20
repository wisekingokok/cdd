package com.chewuwuyou.app.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.NearByCarFriendAdapter;
import com.chewuwuyou.app.bean.NearByFriend;
import com.chewuwuyou.app.bean.PersonHome;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.widget.PullToRefreshBase;
import com.chewuwuyou.app.widget.PullToRefreshBase.OnRefreshListener2;
import com.chewuwuyou.app.widget.PullToRefreshListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
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
public class SameBrandFriendActivity extends CDDBaseActivity implements
        OnRefreshListener2<ListView>, OnClickListener {
    @ViewInject(id = R.id.sub_header_bar_left_ibtn)
    private ImageButton mBackBtn;
    @ViewInject(id = R.id.sub_header_bar_tv)
    private TextView mHeaderTV;
    @ViewInject(id = R.id.sub_header_bar_right_tv)
    private TextView mFriendTypeTV;
    private String sex = "";
    private PullToRefreshListView mPullToRefreshListView;
    private List<NearByFriend> mData;
    private NearByCarFriendAdapter mAdapter;
    private boolean mIsRefreshing = false;
    private StringBuilder sb;
    private String mCarBrand = "";
    private boolean mIsSetEmptyTV = false;
    private RelativeLayout mTitleHeight;// 标题布局高度
    private ImageView mCarLogoImage;
    private TextView mCarLogoTV;
    private String minAge, maxAge;// 筛选-年龄段
    private PersonHome mPersonHome;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.NET_DATA_SUCCESS:
                    try {
                        MyLog.i("yang", msg.obj.toString());
                        JSONArray jArray = new JSONArray(msg.obj.toString());
                        mPersonHome = PersonHome.parse(jArray.get(0).toString());
                        if (mPersonHome.getCarBrand().equals("")) {
                            mCarLogoTV.setText("请前往个人中心设置您的爱车");
                        } else {
                            mCarLogoTV
                                    .setText(mPersonHome.getCarBrand());
                            mCarBrand = mPersonHome.getCarBrand().substring( mPersonHome.getCarBrand().indexOf("/")+1,mPersonHome.getCarBrand().length());

                        }
                        getData(true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carfriend_layout);
        initView();
        initData();
        initEvent();
    }

    @Override
    protected void initView() {
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.nearby_friend_list);
        mCarLogoTV = (TextView) findViewById(R.id.carlogo_text);
        mCarLogoImage = (ImageView) findViewById(R.id.carlogo_image);
        mFriendTypeTV.setText("筛选");
        mFriendTypeTV.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {
        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        isTitle(mTitleHeight);// 根据不同手机判断
        mHeaderTV.setText(R.string.homology_car_friend);
        getCarBrand();
        getMyLocation();
        if (mData == null)
            mData = new ArrayList<NearByFriend>();
        if (mAdapter == null)
            mAdapter = new NearByCarFriendAdapter(SameBrandFriendActivity.this, mData);
        mPullToRefreshListView.setAdapter(mAdapter);
        mPullToRefreshListView.onLoadMore();
        if (TextUtils.isEmpty(com.chewuwuyou.eim.comm.Constant.CAR_NAME)) {
            mCarLogoTV.setText("请筛选汽车");
        } else {
            mCarLogoTV.setText(com.chewuwuyou.eim.comm.Constant.CAR_NAME);
            mCarBrand = com.chewuwuyou.eim.comm.Constant.CAR_NAME;
        }
        if (TextUtils.isEmpty(com.chewuwuyou.eim.comm.Constant.CAR_COORD)) {
        } else {
            setImage(mCarLogoImage, com.chewuwuyou.eim.comm.Constant.CAR_COORD);
        }
    }

    @Override
    protected void initEvent() {
        mBackBtn.setOnClickListener(this);
        mPullToRefreshListView.setOnRefreshListener(this);
        mFriendTypeTV.setOnClickListener(this);
        mPullToRefreshListView
                .setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                        Intent intent = new Intent(
                                SameBrandFriendActivity.this,
                                PersonalHomeActivity2.class);
                        intent.putExtra("userId", Integer.parseInt(mData.get(
                                arg2 - 1).getUserId()) + "");

                        intent.putExtra(PersonalHomeActivity2.NEW_FRIENDID, mData.get(arg2 - 1).getUserId());


                        startActivity(intent);
                    }
                });
        if (!mIsSetEmptyTV) {
            TextView tv = new TextView(this);
            tv.setGravity(Gravity.CENTER);
            tv.setText("没有同系车友");
            tv.setTextColor(getResources().getColor(
                    R.color.empty_text_color));
            mPullToRefreshListView.setEmptyView(tv);
            mIsSetEmptyTV = true;
        }
    }

    /**
     * 获取附近的车友或同品牌车友
     */
    private void getData(final boolean isRefresh) {
        AjaxParams params = new AjaxParams();
        params.put("start", isRefresh ? (0 + "") : (mAdapter.getCount() + ""));
        if (!TextUtils.isEmpty(mCarBrand))
            params.put("brand", mCarBrand);
        params.put("sex", sex);
        if (!TextUtils.isEmpty(minAge) && !TextUtils.isEmpty(maxAge)) {
            params.put("minAge", minAge);
            params.put("maxAge", maxAge);
        }
        mIsRefreshing = true;
        requestNet(new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mIsRefreshing = false;
                mPullToRefreshListView.onRefreshComplete();
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        List<NearByFriend> mRefreshData = null;
                        try {
                            JSONObject jo = new JSONObject(msg.obj.toString());
                            mRefreshData = NearByFriend.parseFriends(jo.getJSONArray("firstPage").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (isRefresh) mData.clear();
                        if (mRefreshData != null) {
                            mData.addAll(mRefreshData);
                            mAdapter.updateData(mData);
                            mAdapter.notifyDataSetChanged();
                        }
                        break;
                }
            }
        }, params, NetworkUtil.SAME_BRAND_CAR_FRIEND, false, 1);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        String label = DateUtils.formatDateTime(getApplicationContext(),
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL);

        refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        if (!mIsRefreshing) {
            getData(true);
        } else {
            mPullToRefreshListView.onRefreshComplete();
        }

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        if (!mIsRefreshing) {
            getData(false);
        } else {
            mPullToRefreshListView.onRefreshComplete();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn:
                finishActivity();
                break;
            case R.id.sub_header_bar_right_tv:
                intent = new Intent(SameBrandFriendActivity.this,
                        SameBrandFilterActivity.class);
                intent.putExtra("nearCar", "2");// 2表示从筛选同系车
                startActivityForResult(intent, 20);

                break;
            default:
                break;
        }
    }

    private List<String> parseAllIds(String idsJson) {
        Gson g = new Gson();
        List<String> nearByFriendAllIds = g.fromJson(idsJson,
                new TypeToken<List<String>>() {
                }.getType());
        return nearByFriendAllIds;
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
                mCarBrand = bundle.getString("brand").toString();
                if (bundle.getString("sex").equals("")) {
                    sex = "";
                } else if (bundle.getString("sex").equals("0")) {
                    sex = "0";

                } else {
                    sex = "1";
                }
                setImage(mCarLogoImage,
                        com.chewuwuyou.eim.comm.Constant.CAR_COORD);
                mCarLogoTV.setText(com.chewuwuyou.eim.comm.Constant.CAR_NAME);
                getData(true);
            }

        }
    }

    /**
     * 获取车友信息数据
     */
    private void getCarBrand() {
        AjaxParams params = new AjaxParams();
        params.put("ids", String.valueOf(CacheTools.getUserData("userId")));
        requestNet(mHandler, params, NetworkUtil.GET_USER_INFO, false, 0);
    }

}
