
package com.chewuwuyou.app.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.utils.MyLog;

/**
 * @describe:停车场导航
 * @author:yuyong
 * @version 1.1.0
 * @created:2015-1-8下午9:08:43
 */
public class ParkbudNavigationActivity extends BaseActivity {

   // Map<String, List<Shop>> mMap;
    private MapView mMapView;
    BaiduMap mBaiduMap;
    // 定位相关
    LocationClient mLocClient;
    private RelativeLayout mTitleHeight;//标题布局高度
    public MyLocationListenner myListener = new MyLocationListenner();
    boolean isFirstLoc = true;// 是否首次定位
    private Double mLng;
    private Double mLat;
    private double tolat = 0;
    private double tolng = 0;
    // 路径规划
    private RoutePlanSearch mSearch;
    OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult result) {
            // 获取步行线路规划结果
        }

        @Override
        public void onGetTransitRouteResult(TransitRouteResult result) {
            // 获取公交换乘路径规划结果
        }

        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult result) {
            // 获取驾车线路规划结果
            if (result == null) {
                return;
            }
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(ParkbudNavigationActivity.this, "抱歉，未找到结果",
                        Toast.LENGTH_SHORT).show();
            }
            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                // result.getSuggestAddrInfo()
                return;
            }
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                DrivingRouteOverlay drivingRouteOvelray = new DrivingRouteOverlay(
                        mBaiduMap);
                mBaiduMap.setOnMarkerClickListener(drivingRouteOvelray);
                drivingRouteOvelray.setData(result.getRouteLines().get(0));
                drivingRouteOvelray.addToMap();
                drivingRouteOvelray.zoomToSpan();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.native_service_ac);
        initView();

    }

    private void initView() {
    	mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
        ((TextView) findViewById(R.id.sub_header_bar_tv)).setText("路径规划");
        findViewById(R.id.sub_header_bar_left_ibtn).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        finishActivity();
                    }
                });
        try {
            tolat = getIntent().getDoubleExtra("lat", 0);
            tolng = getIntent().getDoubleExtra("lng", 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        MyLog.i("YUY", "====经纬度===" + tolat + "====" + tolng);
        mMapView = (MapView) findViewById(R.id.bmapView);
        // 地图初始化
        mBaiduMap = mMapView.getMap();
        // 普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(18).build()));
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        // option.setScanSpan(1000);
        option.setAddrType("all");
        mLocClient.setLocOption(option);
        mLocClient.start();

        // 路径规划
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(listener);
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            mLng = location.getLongitude();
            mLat = location.getLatitude();
            MyLog.i("YUY", "===定位经纬度===" + mLng + "====" + mLat);
            if (tolat != 0 && tolng != 0) {
                System.out.println("tolat=" + tolat + "   tolng=" + tolng);
                PlanNode stNode = PlanNode.withLocation(new LatLng(mLat, mLng));
                PlanNode enNode = PlanNode
                        .withLocation(new LatLng(tolat, tolng));
                mSearch.drivingSearch((new DrivingRoutePlanOption()).from(
                        stNode).to(enNode));
            }
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
            }
        }

        @Override
        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    public void clearOverlay(View view) {
        mBaiduMap.clear();
    }

    @Override
    protected void onPause() {
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mMapView.onResume();
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        mSearch.destroy();
        mSearch = null;
        super.onDestroy();
    }
}
