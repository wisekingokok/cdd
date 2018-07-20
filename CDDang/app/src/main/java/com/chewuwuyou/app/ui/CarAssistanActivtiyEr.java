package com.chewuwuyou.app.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.DrivingAdapter;
import com.chewuwuyou.app.bean.Driving;
import com.chewuwuyou.app.extras.FancyCoverFlow;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.ActionSheet;
import com.chewuwuyou.app.widget.ActionSheet.MenuItemClickListener;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CarAssistanActivtiyEr extends CDDBaseActivity implements OnClickListener, OnGetPoiSearchResultListener {

    private PoiSearch mPoiSearch;
    private BaiduMap mBaiduMap;

    private TextView mTitleTV, msubHeaderBarRightTV;// 标题,地址切换
    private ImageButton mSubHeaderBarLeftIbtn;

    private LinearLayout mLayoutList, mLayoutFragment;// 列表,地址显示

    private TextView mDrivingDistance, mDrivingCity, mAddressName;// 距离，地址，名称
    private double mLat, mLng;// 缓存的经纬度
    private ImageView mImgDrop;// 隐藏商家的基本信息
    private List<Driving> mDrivingsList;
    private FancyCoverFlow mDrivingOption;// 顶部按钮循环滚动
    private DrivingAdapter mDrivingAdapter;
    private TextView mNameOption;
    private int positio = 4;// 检索类型
    // private String businessName, businessAddress, businessService,
    // businessPhone; // 获取用户点击地图标准的基本信息
    // private double latitude, longitude;// //获取用户点击地图标准的经纬度
    private LinearLayout mLinearBg;
    private ImageView mImgLower;
    boolean isStop = true;// 点击判断滑动选择显示隐藏

    // 定位相关
    private LocationClient mLocClient;
    private MyLocationListenner myListener = new MyLocationListenner();

    private MapView mMapView;

    private String mGaoDeUri = "";

    // UI相关
    boolean isFirstLoc = true; // 是否首次定位

    private ImageView mDrivingLocation, mDrivingEnlarge, mDrivingeNnarrow;
    private String mBusinessName;
    private double mLatitude, mLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_assistant_er_ac);
        initView();
        initData();
        initEvent();
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initView() {

        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        // 初始化搜索模块
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);// 标题
        mTitleTV.setText("行车助手");
        mSubHeaderBarLeftIbtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
        msubHeaderBarRightTV = (TextView) findViewById(R.id.sub_header_bar_right_tv);
        msubHeaderBarRightTV.setVisibility(View.VISIBLE);
        msubHeaderBarRightTV.setText("成都");

        mLayoutList = (LinearLayout) findViewById(R.id.layout_list);
        mLayoutFragment = (LinearLayout) findViewById(R.id.layout_fragment);
        mDrivingDistance = (TextView) findViewById(R.id.driving_distance);
        mDrivingCity = (TextView) findViewById(R.id.driving_city);
        mAddressName = (TextView) findViewById(R.id.address_name);
        mImgDrop = (ImageView) findViewById(R.id.img_drop);
        mDrivingOption = (FancyCoverFlow) findViewById(R.id.driving_option);// 循环滚动
        mNameOption = (TextView) findViewById(R.id.textnameoption);
        mLinearBg = (LinearLayout) findViewById(R.id.linear_bg);
        mImgLower = (ImageView) findViewById(R.id.img_lower);

        mDrivingLocation = (ImageView) findViewById(R.id.drivinglocation);
        mDrivingEnlarge = (ImageView) findViewById(R.id.drivingenlarge);
        mDrivingeNnarrow = (ImageView) findViewById(R.id.drivingennarrow);

    }

    /**
     * 逻辑处理
     */
    @SuppressWarnings("deprecation")
    @Override
    protected void initData() {
        mDrivingsList = new ArrayList<Driving>();
        Driving mDriving = new Driving();
        mDriving.setDrivingId(1);
        mDriving.setDrivingName("汽车配件");
        mDrivingsList.add(mDriving);

        Driving mDrivinger = new Driving();
        mDrivinger.setDrivingId(2);
        mDrivinger.setDrivingName("汽车4s店");
        mDrivingsList.add(mDrivinger);

        Driving mDrivingSan = new Driving();
        mDrivingSan.setDrivingId(3);
        mDrivingSan.setDrivingName("加油站");
        mDrivingsList.add(mDrivingSan);

        Driving mDrivingSi = new Driving();
        mDrivingSi.setDrivingId(5);
        mDrivingSi.setDrivingName("维修保养");
        mDrivingsList.add(mDrivingSi);

        Driving mDrivingWu = new Driving();
        mDrivingWu.setDrivingId(4);
        mDrivingWu.setDrivingName("停车助手");
        mDrivingsList.add(mDrivingWu);

        Driving mDrivingLiu = new Driving();
        mDrivingLiu.setDrivingId(6);
        mDrivingLiu.setDrivingName("汽车美容");
        mDrivingsList.add(mDrivingLiu);

        Driving mDrivingQi = new Driving();
        mDrivingQi.setDrivingId(7);
        mDrivingQi.setDrivingName("驾校服务");
        mDrivingsList.add(mDrivingQi);

        mDrivingAdapter = new DrivingAdapter(CarAssistanActivtiyEr.this, mDrivingsList);
        mDrivingOption.setAdapter(mDrivingAdapter);
        mDrivingOption.setCallbackDuringFling(false);
        mDrivingOption.setUnselectedAlpha(0.5f);// 通明度
        mDrivingOption.setUnselectedSaturation(0.5f);// 设置选中的饱和度
        mDrivingOption.setUnselectedScale(0.4f);// 设置选中的规模
        mDrivingOption.setMaxRotation(0);// 设置最大旋转
        mDrivingOption.setScaleDownGravity(0.5f);

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int density = metric.densityDpi;
        int sda = (int) 73 * density / 240;
        mDrivingOption.setSpacing(-(sda));// 设置间距
        mDrivingOption.setActionDistance(FancyCoverFlow.ACTION_DISTANCE_AUTO);
        int arg = Integer.MAX_VALUE / 2 - 3;
        mDrivingOption.setSelection(arg);

        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mMapView.showZoomControls(false);
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    /**
     * 事件监听
     */
    protected void initEvent() {
        mSubHeaderBarLeftIbtn.setOnClickListener(this);
        msubHeaderBarRightTV.setOnClickListener(this);
        mLayoutFragment.setOnClickListener(this);
        mLayoutList.setOnClickListener(this);
        mImgDrop.setOnClickListener(this);
        mImgLower.setOnClickListener(this);
        mDrivingLocation.setOnClickListener(this);
        mDrivingEnlarge.setOnClickListener(this);
        mDrivingeNnarrow.setOnClickListener(this);

        mDrivingOption.setCallbackDuringFling(false);// FancyCoverFlow
        /**
         * FancyCoverFlow 点击滑动事件
         */
        mDrivingOption.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                mNameOption.setText(mDrivingsList.get(position % mDrivingsList.size()).getDrivingName());
                mPoiSearch.searchNearby(new PoiNearbySearchOption().location(new LatLng(mLat, mLng))
                        .keyword(mDrivingsList.get(position % mDrivingsList.size()).getDrivingName()).radius(5000));
                positio = position % mDrivingsList.size() + 1;
                mLayoutFragment.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /**
         * FancyCoverFlow 点击事件
         */
        mDrivingOption.setOnItemClickListener(new FancyCoverFlow.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                positio = position % mDrivingsList.size() + 1;
                mNameOption.setText(mDrivingsList.get(position % mDrivingsList.size()).getDrivingName());
                mPoiSearch.searchNearby(new PoiNearbySearchOption().location(new LatLng(mLat, mLng))
                        .keyword(mDrivingsList.get(position % mDrivingsList.size()).getDrivingName()).radius(5000));
                mLayoutFragment.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 点击事件
     */
    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn:
                finishActivity();
                break;
            case R.id.sub_header_bar_right_tv:// 切换城市

                break;
            case R.id.layout_fragment:// 点击地址
            /*
             * intent = new Intent(this, ShopDetailsActivity.class);
			 * intent.putExtra("shopType", positio);
			 * intent.putExtra("businessName", businessName);
			 * intent.putExtra("businessAddress", businessAddress);
			 * intent.putExtra("businessService", businessService);
			 * intent.putExtra("businessPhone", businessPhone);
			 * intent.putExtra("latitude", latitude + "");
			 * intent.putExtra("longitude", longitude + "");
			 * intent.putExtra("businessStoreName", mNameOption.getText()
			 * .toString()); startActivity(intent);
			 */

                setTheme(R.style.ActionSheetStyleIOS7);
                ActionSheet menuView = new ActionSheet(CarAssistanActivtiyEr.this);
                menuView.setCancelButtonTitle("取 消");
                menuView.addItems("百度地图", "高德地图");
                menuView.setItemClickListener(new MenuItemClickListener() {

                    @Override
                    public void onItemClick(int itemPosition) {
                        switch (itemPosition) {
                            case 0:
                                String loc = null;
                                if (!TextUtils.isEmpty(mBusinessName)) {
                                    loc = mLatitude + "," + mLongitude + "," + mBusinessName;
                                } else {
                                    ToastUtil.toastShow(CarAssistanActivtiyEr.this, "暂无内容");
                                }
                                if (isInstallByread("com.baidu.BaiduMap")) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    Uri uri = Uri.parse("geo:" + loc);
                                    intent.setData(uri);
                                    intent.setPackage("com.baidu.BaiduMap");
                                    startActivity(intent);
                                } else {
                                    ToastUtil.toastShow(CarAssistanActivtiyEr.this, "未安装百度地图应用");
                                }
                                break;
                            case 1:
                                if (isInstallByread("com.autonavi.minimap")) {
                                    Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse(mGaoDeUri));
                                    intent.setPackage("com.autonavi.minimap");
                                    startActivity(intent);
                                } else {
                                    ToastUtil.toastShow(CarAssistanActivtiyEr.this, "未安装高德地图应用");
                                }
                                break;
                            default:
                                break;
                        }

                    }
                });
                menuView.setCancelableOnTouchMenuOutside(true);
                menuView.showMenu();

                break;
            case R.id.layout_list:// 列表
                intent = new Intent(this, ParkbudGasStationActivity.class);
                intent.putExtra("assistantType", positio);
                startActivity(intent);
                break;

            case R.id.img_drop:
                mLayoutFragment.setVisibility(View.GONE);
                break;
            case R.id.img_lower:// 点击判断滑动选择显示隐藏

                if (isStop == true) {
                    mLinearBg.setVisibility(View.GONE);
                    mImgLower.setImageDrawable(getResources().getDrawable(R.drawable.driving_bg_lower));
                    isStop = false;
                } else {
                    mLinearBg.setVisibility(View.VISIBLE);
                    mImgLower.setImageDrawable(getResources().getDrawable(R.drawable.driving_bg_upper));
                    isStop = true;
                }
                break;
            case R.id.drivinglocation:// 回到定位
                // 设定中心点坐标
                LatLng cenpt = new LatLng(mLat, mLng);
                // 定义地图状态
                MapStatus mMapStatus = new MapStatus.Builder().target(cenpt).zoom(18).build();

                // 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化

                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                // 改变地图状态
                mBaiduMap.setMapStatus(mMapStatusUpdate);
                break;
            case R.id.drivingenlarge:// 放大
                float zoomLevel = mBaiduMap.getMapStatus().zoom;
                if (zoomLevel <= 18) {
                    mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomIn());
                    mDrivingEnlarge.setEnabled(true);
                }
                break;
            case R.id.drivingennarrow:// 缩小
                float zoomLeve = mBaiduMap.getMapStatus().zoom;
                if (zoomLeve > 4) {
                    mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomOut());
                    mDrivingeNnarrow.setEnabled(true);
                }
                break;
            default:
                break;
        }

    }

    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishActivity();
        }
        return false;
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                mLat = location.getLatitude();
                mLng = location.getLongitude();
                mPoiSearch.searchNearby(
                        new PoiNearbySearchOption().keyword("停车场").location(new LatLng(mLat, mLng)).radius(5000));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocClient != null && mBaiduMap != null) {
            // 退出时销毁定位
            mLocClient.stop();
            // 关闭定位图层
            mBaiduMap.setMyLocationEnabled(false);
        }

        if (mMapView != null&&mMapView.getMap()!=null) {
            mMapView.onDestroy();
            mMapView = null;
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult result) {

        if (result.error != SearchResult.ERRORNO.NO_ERROR) {

        } else {
            // TODO 没有用到 暂时注释
            // businessName = result.getName();
            // businessAddress = result.getAddress();
            // businessService = result.getTag();
            // businessPhone = result.getTelephone();
            // latitude = result.getLocation().latitude;
            // longitude = result.getLocation().longitude;

            mLatitude = result.getLocation().latitude;
            mLongitude = result.getLocation().longitude;
            mBusinessName = result.getName();
            mAddressName.setText(result.getName());

            mGaoDeUri = "androidamap://navi?sourceApplication=appname&poiname=" + mBusinessName + "&lat=" + mLatitude
                    + "&lon=" + mLongitude + "&dev=1&style=2";

            double distance = getDistance(mLat, mLng, result.getLocation().latitude, result.getLocation().longitude);
            mDrivingDistance.setText((Math.round(distance / 100) / 10.0) + "公里");// 距离
            mDrivingCity.setText(result.getAddress());
            mLayoutFragment.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onGetPoiResult(PoiResult result) {

        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {

            if (mBaiduMap != null) {
                try {
                    mBaiduMap.clear();
                    PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
                    mBaiduMap.setOnMarkerClickListener(overlay);
                    overlay.setData(result);
                    overlay.addToMap();
                    overlay.zoomToSpan();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
            String strInfo = "在";
            for (CityInfo cityInfo : result.getSuggestCityList()) {
                strInfo += cityInfo.city;
                strInfo += ",";
            }
            strInfo += "找到结果";
            Toast.makeText(CarAssistanActivtiyEr.this, strInfo, Toast.LENGTH_LONG).show();
        }

    }

    /**
     * google maps的脚本里代码
     */
    private final static double EARTH_RADIUS = 6378.137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
     * getDistance(29.543636,106.752925,29.543699,106.755045)
     *
     * @param lat1 第一点的纬度坐标
     * @param lng1 第一点的经度坐标
     * @param lat2 第二点的纬度坐标
     * @param lng2 第二点的经度坐标
     * @return
     */
    public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(
                Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;// 公里
        // s = Math.round(s * 10000) / 10000;//转化成米抛一位四舍五入
        return s * 1000;
    }

    private class MyPoiOverlay extends PoiOverlay {

        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int index) {
            super.onPoiClick(index);
            PoiInfo poi = getPoiResult().getAllPoi().get(index);
            mPoiSearch.searchPoiDetail((new PoiDetailSearchOption()).poiUid(poi.uid));
            return true;
        }
    }
}
