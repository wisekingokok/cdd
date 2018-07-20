package com.chewuwuyou.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mobstat.StatService;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.ServiceViewPagerAdapter;
import com.chewuwuyou.app.adapter.YueGroupAdapter;
import com.chewuwuyou.app.bean.Banner;
import com.chewuwuyou.app.bean.YueTabItem;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.CurrentViewPager;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.TabPageIndicator;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

public class YueActivity extends BaseFragmentActivity implements
        View.OnClickListener {

    // header bar
    @ViewInject(id = R.id.sub_header_bar_left_ibtn, click = "onAction")
    private ImageButton mBackBtn;
    @ViewInject(id = R.id.sub_header_bar_tv)
    private TextView mHeaderTV;
    @ViewInject(id = R.id.sub_header_bar_right_tv, click = "onAction")
    private TextView mHeaderBarRightTV;

    // 页标
    private TabPageIndicator mTabPageIndicator;
    // private ProgressBar mTabGravityRightPB;
    // private ProgressBar mTabGravityLeftPB;
    // 页卡
    private CurrentViewPager mPager;
    private YueGroupAdapter mAdapter;
    // private TextView mNewYueTV;// 发布一个活动
    private LinearLayout new_yue_ll;
    private List<YueTabItem> mTitleData;// 驾证信息集合
    public LocationClient mLocationClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private boolean mIfLocSuccess = false;
    private RelativeLayout mTitleHeight;// 标题布局高度
    private List<Banner> mBanners;// 运营位集合
    private AutoScrollViewPager mServiceViewPager;// 服务页viewPager循环
    private CirclePageIndicator mCirclePageIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏标题栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yue_layout);

        mTitleHeight = (RelativeLayout) findViewById(R.id.yue_head_rl);
        isTitle(mTitleHeight);// 根据不同手机判断
        mBackBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
        mBackBtn.setOnClickListener(this);

        mHeaderTV = (TextView) findViewById(R.id.sub_header_bar_tv);
        mHeaderTV.setText(new StringBuilder().append("车友活动"));
        mServiceViewPager = (AutoScrollViewPager) findViewById(R.id.serviceviewpager);// viewPager初始化
        mCirclePageIndicator = (CirclePageIndicator) findViewById(R.id.circle_page_indicator);// 画圆点
        mHeaderBarRightTV = (TextView) findViewById(R.id.sub_header_bar_right_tv);
        mHeaderBarRightTV.setVisibility(View.VISIBLE);
        mHeaderBarRightTV.setText("定位中");
        new_yue_ll = (LinearLayout) findViewById(R.id.new_yue_ll);
        mPager = (CurrentViewPager) findViewById(R.id.yue_pager);
        mPager.setmNotScroll(true);
        mTabPageIndicator = (TabPageIndicator) findViewById(R.id.yue_pager_tab);
        // mTabGravityRightPB = (ProgressBar)
        // findViewById(R.id.yue_tab_bottom_gravity_right_pb);
        // mTabGravityLeftPB = (ProgressBar)
        // findViewById(R.id.yue_tab_bottom_gravity_left_pb);
        initTitleData();
        ArrayList<Fragment> mFragments = new ArrayList<>();
        for (YueTabItem item : mTitleData) {
            mFragments.add(new YueFragment(this, item.getTitle(), item.getType(), mHeaderBarRightTV.getText().toString()));
        }
        mAdapter = new YueGroupAdapter(this, mTitleData, mFragments);
        mPager.setAdapter(mAdapter);
        mTabPageIndicator.setViewPager(mPager);
        getMyLocation();
        new_yue_ll.setOnClickListener(this);
        mHeaderBarRightTV.setOnClickListener(this);

    }

    private void initTitleData() {
        mTitleData = new ArrayList<YueTabItem>();
        YueTabItem item = new YueTabItem();
        item.setTitle("全部");
        item.setType("");
        item.setIconResId(R.drawable.all_xbg);
        mTitleData.add(item);

        item = new YueTabItem();
        item.setTitle("旅游");
        item.setType("TRAVEL");
        item.setIconResId(R.drawable.travell_xbg);
        mTitleData.add(item);

        item = new YueTabItem();
        item.setTitle("聚餐");
        item.setIconResId(R.drawable.dinner_xbg);
        item.setType("DINNER");
        mTitleData.add(item);

        item = new YueTabItem();
        item.setTitle("观影");
        item.setIconResId(R.drawable.movie_xbg);
        item.setType("FILM");
        mTitleData.add(item);

        item = new YueTabItem();
        item.setTitle("购物");
        item.setIconResId(R.drawable.shopping_xbg);
        item.setType("SHOPPING");
        mTitleData.add(item);

        item = new YueTabItem();
        item.setTitle("泡吧");
        item.setType("BAR");
        item.setIconResId(R.drawable.bar_xbg);
        mTitleData.add(item);
        //
        item = new YueTabItem();
        item.setTitle("麻将");
        item.setIconResId(R.drawable.majiang_xbg);
        item.setType("MAHJONG");
        mTitleData.add(item);

        item = new YueTabItem();
        item.setTitle("运动");
        item.setIconResId(R.drawable.sports_xbg);
        item.setType("SPORTS");
        mTitleData.add(item);
        getBanner();
    }

    /**
     * 获取广告运营资料
     */
    private void getBanner() {
        mBanners = new ArrayList<Banner>();
        AjaxParams params = new AjaxParams();
        params.put("type", Constant.BANNER_TYPE.ACTIVITY_BANNER);
        requestNet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.NET_DATA_SUCCESS:
                        MyLog.i("YUY", "banner = " + msg.obj.toString());
                        try {
                            JSONObject jo = new JSONObject(msg.obj.toString());
                            mBanners = Banner.parseList(jo.getJSONArray("banners")
                                    .toString());
                            if (mBanners.size() == 0) {
                                Banner banner = new Banner();
                                banner.setImageUrl("");
                                banner.setTiaoType(1);
                                banner.setTiaoUrl("http://www.cddang.com");
                                mBanners.add(banner);
                            }
                            mServiceViewPager.setAdapter(new ServiceViewPagerAdapter(
                                    YueActivity.this, mBanners));
                            mServiceViewPager.startAutoScroll();
                            mServiceViewPager.setInterval(2000);
                            mCirclePageIndicator.setViewPager(mServiceViewPager);
                            mServiceViewPager.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % mBanners.size());
                            mServiceViewPager.setOffscreenPageLimit(10);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;

                    default:
                        break;
                }
            }
        }, params, NetworkUtil.GET_BANNER, false, 1);
    }

    private String city;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constant.Yue.RESULT_FROM_CHOOSE_CITY:
                if (resultCode == RESULT_OK) {
                    String region = (!TextUtils
                            .isEmpty(data.getStringExtra("city"))) ? data
                            .getStringExtra("city") : data
                            .getStringExtra("province");
                    mHeaderBarRightTV.setText(region);
                    mIfLocSuccess = true;
                    // mNewYueTV.setText(data.getStringExtra("city")); 删除是因为以前写错了
                    // getAllYue(true); 在fragment做一个监听吧
                    ArrayList<Fragment> fragments = mAdapter.getmFragments();
                    for (Fragment fragment : fragments) {
                        ((YueFragment) fragment).setRegion(region);
                    }
                    Intent intent = new Intent("region_changed");
                    intent.putExtra("region", region);
                    city = region;
                    sendBroadcast(intent);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            // 退出
            case R.id.sub_header_bar_left_ibtn:
                finishActivity();
                break;
            case R.id.sub_header_bar_right_tv:
                // 选择地区
                StatService.onEvent(YueActivity.this, "clickChooseLicenseCity",
                        "点击选择所在城市");
                intent = new Intent(YueActivity.this, AreaSelectActivity.class);
                startActivityForResult(intent, Constant.Yue.RESULT_FROM_CHOOSE_CITY);
                break;
            case R.id.new_yue_ll:
                if (mIfLocSuccess) {
                    if (CacheTools.getUserData("userId") != null
                            && Integer.parseInt(CacheTools.getUserData("userId")) > 0) {
                        intent = new Intent(YueActivity.this, AddYueActivity.class);
                        intent.putExtra("add_yue_region", mHeaderBarRightTV
                                .getText().toString());
                        intent.putExtra("city", city);
                        startActivity(intent);
                    } else {
                        // 跳到登录界面
                        intent = new Intent(YueActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finishActivity();
                    }
                } else {
                    ToastUtil.toastShow(YueActivity.this, "还没定位成功，别着急喔!");
                }
                break;
            default:
                break;
        }
    }

    // private class ProgressBarAsync extends AsyncTask<Void, Integer, Void> {
    // int current = 0;
    //
    // @Override
    // protected Void doInBackground(Void... params) {
    // do {
    // current += 2;
    // try {
    // publishProgress(current); // 这里的参数类型是 AsyncTask<Void,
    // Thread.sleep(1);
    // if (mTabGravityLeftPB.getProgress() >= 100) {
    // break;
    // }
    // } catch (Exception e) {
    // }
    // } while (mTabGravityLeftPB.getProgress() <= 100);
    // return null;
    // }
    //
    // @Override
    // protected void onProgressUpdate(Integer... values) {
    // super.onProgressUpdate(values);
    // mTabGravityLeftPB.setProgress(values[0]);
    // mTabGravityRightPB.setProgress(values[0]);
    // }
    // }

    /**
     * 定位自己的位置
     */
    public void getMyLocation() {
        mLocationClient = new LocationClient(YueActivity.this);
        mLocationClient.registerLocationListener(myListener);
        setLocationOption();// 设定定位参数
        mLocationClient.start();// 开始定位
    }

    // 定位
    // 设置相关参数
    private void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setAddrType("all");// 返回的定位结果包含地址信息
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.disableCache(false);// 禁止启用缓存定位
        option.setPoiNumber(5); // 最多返回POI个数
        option.setScanSpan(5000); // 设置发起定位请求的间隔时间为5000ms
        option.setPoiDistance(1000); // poi查询距离
        option.setPoiExtraInfo(true); // 是否需要POI的电话和地址等详细信息
        mLocationClient.setLocOption(option);

    }

    /**
     * 监听函数，更新位置
     */
    private class MyLocationListenner implements BDLocationListener {

        @Override
        // 接收位置信息
        public void onReceiveLocation(BDLocation location) {
            if (location == null || mIfLocSuccess)
                return;
            if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                mIfLocSuccess = true;
                CacheTools.setUserData("province", location.getProvince());
                CacheTools.setUserData("city", location.getCity());
                CacheTools.setUserData("district", location.getDistrict());
                CacheTools.setUserData("Lat", location.getLatitude() + "");
                CacheTools.setUserData("Lng", location.getLongitude() + "");
                CacheTools.setUserData("addrStr", location.getAddrStr());
                mHeaderBarRightTV.setText(location.getCity());
                ArrayList<Fragment> fragments = mAdapter.getmFragments();
                for (Fragment fragment : fragments) {
                    ((YueFragment) fragment).setRegion(location.getCity());
                }
                Intent intent = new Intent("region_changed");
                intent.putExtra("region", location.getCity());
                city = location.getCity();
                sendBroadcast(intent);
                mTabPageIndicator.setViewPager(mPager);
                // 退出时销毁定位
                mLocationClient.stop();
            }
        }

        // 接收POI信息函数
        @Override
        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null) {
                return;
            }
        }

    }
}
