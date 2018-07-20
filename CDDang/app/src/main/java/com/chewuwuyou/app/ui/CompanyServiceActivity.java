package com.chewuwuyou.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.CompanyService;
import com.chewuwuyou.app.fragment.CompanyServiceFragment;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.widget.SegmentControl;

public class CompanyServiceActivity extends BaseFragmentActivity implements View.OnClickListener {

    private static final int ADD_NEW = 10;
    public static final String TYPE = "type";
    private static final int CHOOSE_ADDRESS = 20;
    public static final String DISTRICT = "district";
    public static final String PROVICE = "province";
    public static final String CITY = "city";

    private SegmentControl mSeControl;
    private TextView mTextView_title;
    private TextView mTextView_address;
    private TextView mTextView_search;
    private ViewPager mViewPager;
    private ImageButton mBackIBtn;// 返回
    private TextView mTextView_right;
    private String district;
    private String provice;
    private String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_service);
        initView();
        initData();
        initEvent();

    }


    protected void initView() {
        mSeControl = (SegmentControl) findViewById(R.id.segment_control);
        mTextView_title = (TextView) findViewById(R.id.sub_header_bar_tv);
        mTextView_address = (TextView) findViewById(R.id.company_address);
        mTextView_search = (TextView) findViewById(R.id.company_search);
        mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
        mTextView_right = (TextView) findViewById(R.id.sub_header_bar_right_tv);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        getAddress();
    }


    protected void initData() {
        mTextView_title.setText("企业服务");
        mTextView_right.setText("发布优势");

    }


    protected void initEvent() {

        mTextView_search.setOnClickListener(this);
        mTextView_address.setOnClickListener(this);

        mSeControl.setOnSegmentControlClickListener(new SegmentControl.OnSegmentControlClickListener() {
            @Override
            public void onSegmentControlClick(int index) {
                if (index == 0) {
                    mViewPager.setCurrentItem(0);
                } else {
                    mViewPager.setCurrentItem(1);
                }
            }
        });


        mViewPager.setAdapter(new PageAdapter(getSupportFragmentManager()));


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                mSeControl.setSelectedIndex(position);
                if (position == 0)
                    mTextView_right.setText("发布优势");
                else
                    mTextView_right.setText("发布求助");

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mBackIBtn.setOnClickListener(this);
        mTextView_right.setOnClickListener(this);
        mTextView_right.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.sub_header_bar_left_ibtn:
                finishActivity();
                break;
            case R.id.sub_header_bar_right_tv:
                Intent mIntent = new Intent(this, NewAdvantageActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putInt(NewAdvantageActivity.PUBLISH_TYPE, mViewPager.getCurrentItem());
                mIntent.putExtras(mBundle);
                startActivityForResult(mIntent, ADD_NEW);
                break;
            case R.id.company_address:
                chooseCity();
                break;

            case R.id.company_search:
                Bundle mBundle1 = new Bundle();
                mBundle1.putInt(CompanyServiceActivity.TYPE, mViewPager.getCurrentItem());
                mBundle1.putString(this.CITY, city);
                mBundle1.putString(this.PROVICE, provice);
                mBundle1.putString(this.DISTRICT, district);
                CompanySearchActivity.launch(this, mBundle1);
                break;
            default:
                break;


        }


    }


    private void getAddress() {
        // 获取缓存的地址信息
        provice = CacheTools.getUserData("province");
        city = CacheTools.getUserData("city");
        district = CacheTools.getUserData("district");
        mTextView_address.setText(city);
        mFragment1.setCity(provice, city, district);
        mFragment2.setCity(provice, city, district);
        if (TextUtils.isEmpty(city)) {
            getMyLocation();
        }

    }

    public void chooseCity() {
        Constant.CITY_SHOW_ALL = true;//写上这句话。。。就能在地区里选择全部了。。。
        Intent chooseCityIntent = new Intent(this, AreaSelectActivity.class);
        startActivityForResult(chooseCityIntent, CHOOSE_ADDRESS);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_ADDRESS && resultCode == RESULT_OK) {

            if (data.getStringExtra("province").toString().equals(data.getStringExtra("city").toString())) {
                mTextView_address.setText(data.getStringExtra("city"));
            } else {
                // mTextView_address.setText(data.getStringExtra("province") + data.getStringExtra("city"));
                mTextView_address.setText(data.getStringExtra("city"));
            }

            provice = data.getStringExtra("province");
            city = data.getStringExtra("city");
            if (!"全部".equals(data.getStringExtra("district")))
                district = data.getStringExtra("district");
            else
                district = "";

            mFragment1.setCity(provice, city, district);
            mFragment2.setCity(provice, city, district);
            //     if (mViewPager.getCurrentItem() == 0)
            mFragment1.refreshWithClear();
            //    else
            mFragment2.refreshWithClear();

        } else if (requestCode == ADD_NEW && resultCode == RESULT_OK) {
            if (mViewPager.getCurrentItem() == 0)
                mFragment1.refresh();
            else
                mFragment2.refresh();
        }


    }


    CompanyServiceFragment mFragment1 = CompanyServiceFragment.newInstance(1, null);
    CompanyServiceFragment mFragment2 = CompanyServiceFragment.newInstance(2, null);

    class PageAdapter extends FragmentPagerAdapter {


        public PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return mFragment1;
            else
                return mFragment2;
        }

        @Override
        public int getCount() {
            return 2;
        }


    }


    public MyLocationListenner myListener = new MyLocationListenner();// 定位
    public LocationClient mLocationClient;

    /**
     * 定位自己的位置
     */
    public void getMyLocation() {
        mLocationClient = new LocationClient(this);
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
    public class MyLocationListenner implements BDLocationListener {

        @Override
        // 接收位置信息
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return;
            if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                CacheTools.setUserData("province", location.getProvince());
                CacheTools.setUserData("city", location.getCity());
                CacheTools.setUserData("district", location.getDistrict());
                CacheTools.setUserData("Lat", location.getLatitude() + "");
                CacheTools.setUserData("Lng", location.getLongitude() + "");
                city = CacheTools.getUserData("city");
                provice = CacheTools.getUserData("province");
                district = CacheTools.getUserData("district");
                mTextView_address.setText(city);
                mFragment1.setCity(provice, city, district);
                mFragment2.setCity(provice, city, district);

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


    public interface onAddressSetListener {

        void onAddressset(String pro, String city, String area);

    }


}
