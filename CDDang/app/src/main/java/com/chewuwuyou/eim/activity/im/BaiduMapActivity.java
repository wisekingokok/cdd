package com.chewuwuyou.eim.activity.im;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.CommonAdapter;
import com.chewuwuyou.app.adapter.ViewHolder;
import com.chewuwuyou.app.bean.PoiAddress;
import com.chewuwuyou.app.ui.CDDBaseActivity;
import com.chewuwuyou.app.utils.IsNetworkUtil;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.rong.bean.LocationBean;
import com.chewuwuyou.rong.utils.BaiduMapUtilByRacer;
import com.chewuwuyou.rong.utils.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * 百度地图
 * 1、发送位置信息
 * 2、展示发送的位置信息
 */
public class BaiduMapActivity extends CDDBaseActivity implements OnClickListener, OnGetSuggestionResultListener {

    private MapView mMapView = null;
    private LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private TextView mSendLocationTV;
    private TextView mTitleTV;
    private ImageButton mBackIBtn;
    static BDLocation lastLocation = null;
    private ProgressDialog progressDialog;
    private BaiduMap mBaiduMap;
    private ListView mLocationLV;
    private LocationMode mCurrentMode;
    private RelativeLayout mTitleHeight;// 标题布局高度
    private CommonAdapter<PoiAddress> mAdapter;//地址
    private List<PoiAddress> mPoiAddress;//检索出的地址
    private PoiAddress mPoiAdd;//返回的地址信息
    private GeoCoder mGeoCoder = GeoCoder.newInstance();
    private PoiSearch mPoiSearch;
    private ProgressBar progressBar;
    private AutoCompleteTextView mQueryAutoCompTV;// query_auto_comp_tv;
    private SuggestionSearch mSuggestionSearch;//搜索建议
    private String str = "";
    private String city = "成都";
    private ArrayAdapter<String> mSuggestionAdapter;
    private List<String> citys = new ArrayList<>();
    private int isLocaRefresh = 0;
    private ProgressBar mBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_location_ac);
        initView();
        initData();
        initEvent();
    }

    @Override
    protected void initView() {
        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        isTitle(mTitleHeight);// 根据不同手机判断
        mLocationLV = (ListView) findViewById(R.id.location_lv);
        mMapView = (MapView) findViewById(R.id.location_map);
        mSendLocationTV = (TextView) findViewById(R.id.sub_header_bar_right_tv);
        mSendLocationTV.setVisibility(View.VISIBLE);
        mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
        mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mQueryAutoCompTV = (AutoCompleteTextView) findViewById(R.id.query_auto_comp_tv);
        mBar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    @Override
    protected void initData() {
        mSendLocationTV.setText("发送");
        mTitleTV.setText("位置信息");
        Intent intent = getIntent();
        double latitude = intent.getDoubleExtra("latitude", 0);
        mCurrentMode = LocationMode.NORMAL;
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        mBaiduMap.getUiSettings().setZoomGesturesEnabled(false);
        mBaiduMap.setMapStatus(msu);
        mMapView.setLongClickable(true);
        if (latitude == 0) {
            mMapView = new MapView(BaiduMapActivity.this, new BaiduMapOptions());
            mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                    mCurrentMode, true, null));
            showMapWithLocationClient();
        } else {
            double longtitude = intent.getDoubleExtra("longitude", 0);
            String address = intent.getStringExtra("address");
            LatLng p = new LatLng(latitude, longtitude);
            mMapView = new MapView(BaiduMapActivity.this, new BaiduMapOptions().mapStatus(new MapStatus.Builder().target(p).build()));
            showMap(latitude, longtitude);
        }
        initSendLocation();
        mPoiSearch = PoiSearch.newInstance();
        mSuggestionSearch = SuggestionSearch.newInstance(); // 注册搜索事件监听
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
        mSuggestionAdapter = new ArrayAdapter<String>(this, R.layout.suggest_dropdown_item);
        mQueryAutoCompTV.setAdapter(mSuggestionAdapter);
        mQueryAutoCompTV.setThreshold(1);
        mQueryAutoCompTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (citys.get(arg2).equals("未找到相关结果")) return;
                city = citys.get(arg2);
                mPoiSearch.searchInCity((new PoiCitySearchOption()).city(city).keyword(mSuggestionAdapter.getItem(arg2)));
                LocationBean locationBean = new LocationBean();
                locationBean.setCity(city);
                locationBean.setLocName(mSuggestionAdapter.getItem(arg2));
                isLocaRefresh = 1;
                BaiduMapUtilByRacer.getLocationByGeoCode(locationBean, new BaiduMapUtilByRacer.GeoCodeListener() {
                    @Override
                    public void onGetSucceed(LocationBean locationBean) {
                        MyLog.e("YUY", "经纬度  = " + locationBean.getLatitude() + " " + locationBean.getLongitude());
                        getAddress(locationBean.getLatitude(), locationBean.getLongitude());
                    }

                    @Override
                    public void onGetFailed() {

                    }
                });

            }
        });
    }

    @Override
    protected void initEvent() {
        mSendLocationTV.setOnClickListener(this);
        mBackIBtn.setOnClickListener(this);
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
        mQueryAutoCompTV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String mStr = mQueryAutoCompTV.getText().toString();
                if (!TextUtils.isEmpty(mStr) && !str.equals(mStr) && !TextUtils.isEmpty(city)) {
                    str = mStr;
                    progressBar.setVisibility(View.VISIBLE);
                    mSuggestionSearch.requestSuggestion((new SuggestionSearchOption()).keyword(str).city(city));
                }
            }
        });
    }

    /**
     * 发送位置信息初始化
     */
    private void initSendLocation() {
        mPoiAddress = new ArrayList<>();
        mAdapter = new CommonAdapter<PoiAddress>(BaiduMapActivity.this, mPoiAddress, R.layout.item_location) {
            @Override
            public void convert(ViewHolder holder, PoiAddress poiAddress, int position) {
                holder.setChecked(R.id.check_box, poiAddress.isSelect());
                holder.setText(R.id.poi_address_tv, poiAddress.getPoiAddress());
                holder.setText(R.id.other_poi_address_tv, poiAddress.getPoiAddressDel());
            }
        };
        mLocationLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPoiAddress.get(position).setSelect(true);
                mPoiAdd = mPoiAddress.get(position);
                for (int i = 0; i < mPoiAddress.size(); i++)
                    if (i != position) {
                        mPoiAddress.get(i).setSelect(false);
                    }
                mAdapter.notifyDataSetChanged();
                showMap(mPoiAdd.getLat(), mPoiAdd.getLng());//刷新地图
            }
        });
        mLocationLV.setAdapter(mAdapter);
    }

    private void showMap(double latitude, double longtitude) {
        mBaiduMap.clear();
        LatLng llA = new LatLng(latitude, longtitude); //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder().target(llA).zoom(18).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        OverlayOptions ooA = new MarkerOptions().position(llA).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marka)).zIndex(4).draggable(true);
        mBaiduMap.addOverlay(ooA);
        mBaiduMap.setMapStatus(mMapStatusUpdate);
//        LatLng llA = new LatLng(latitude, longtitude);
//        CoordinateConverter converter = new CoordinateConverter();
//        converter.coord(llA);
//        converter.from(CoordinateConverter.CoordType.COMMON);
//        LatLng convertLatLng = converter.convert();
//        OverlayOptions ooA = new MarkerOptions().position(convertLatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marka)).zIndex(4).draggable(true);
//        mBaiduMap.addOverlay(ooA);
//        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(convertLatLng, 17.0f);
//        mBaiduMap.animateMapStatus(u);
    }

    private void showMapWithLocationClient() {
        String str1 = getResources().getString(
                R.string.Making_sure_your_location);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(str1);
        progressDialog.setOnCancelListener(new OnCancelListener() {

            public void onCancel(DialogInterface arg0) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Log.d("map", "cancel retrieve location");
                finish();
            }
        });
        progressDialog.show();
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); //设置坐标类型
//        option.setCoorType("gcj02");
        option.setScanSpan(30000);
        option.setAddrType("all");
        mLocClient.setLocOption(option);
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        if (mLocClient != null) {
            mLocClient.stop();
        }
        super.onPause();
        lastLocation = null;
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        if (mLocClient != null) {
            mLocClient.start();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (mLocClient != null)
            mLocClient.stop();
        if (mBaiduMap != null) {
            mBaiduMap.setMyLocationEnabled(false);// 关闭定位图层
            mBaiduMap = null;
        }
        if (mMapView != null) {
            mMapView.destroyDrawingCache();
            try {
                mMapView.onDestroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mMapView = null;
        }

        // unregisterReceiver(mBaiduReceiver);
        super.onDestroy();
        System.gc();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sub_header_bar_right_tv:
                if (IsNetworkUtil.isNetworkAvailable(BaiduMapActivity.this) == false) {
                    ToastUtil.toastShow(BaiduMapActivity.this, "发送失败，请检查网络是否连接");
                    return;
                }
                Intent intent = this.getIntent();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.BAIDU_LOCATION, mPoiAdd);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finishActivity();
                overridePendingTransition(R.anim.slide_in_from_left,
                        R.anim.slide_out_to_right);
                break;
            case R.id.sub_header_bar_left_ibtn:
                finishActivity();
                break;
        }
    }

    @Override
    public void onGetSuggestionResult(SuggestionResult res) {
        progressBar.setVisibility(View.GONE);
        citys.clear();
        mSuggestionAdapter.clear();
        if (res == null || res.getAllSuggestions() == null) {// 未找到相关结果
            ToastUtil.toastShow(this, "未找到相关结果");
            return;
        }
        for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
            if (info.key != null) {
                if (TextUtils.isEmpty(info.city)) {
                    mSuggestionAdapter.add(info.key);
                } else {
                    mSuggestionAdapter.add(info.key + "(" + info.city + ")");
                }
                citys.add(info.city);
            }
        }
        mQueryAutoCompTV.setText(str);
        mQueryAutoCompTV.setSelection(str.length());
    }

    /**
     * 监听函数，有新位置的时候，格式化成字符串，输出到屏幕中
     */
    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                return;
            }
            Log.d("map", "On location change received:" + location);
            Log.d("map", "addr:" + location.getAddrStr());
            mSendLocationTV.setEnabled(true);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }

            if (lastLocation != null) {
                if (lastLocation.getLatitude() == location.getLatitude()
                        && lastLocation.getLongitude() == location
                        .getLongitude()) {
                    Log.d("map", "same location, skip refresh");
                    // mMapView.refresh(); //need this refresh?
                    return;
                }
            }
            lastLocation = location;
//            mBaiduMap.clear();
            LatLng llA = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            mBar.setVisibility(View.VISIBLE);
            getAddress(lastLocation.getLatitude(), lastLocation.getLongitude());
            CoordinateConverter converter = new CoordinateConverter();
            converter.coord(llA);
            converter.from(CoordinateConverter.CoordType.COMMON);
            LatLng convertLatLng = converter.convert();
            OverlayOptions ooA = new MarkerOptions().position(convertLatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marka)).zIndex(4).draggable(true);
            mBaiduMap.addOverlay(ooA);
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(
                    convertLatLng, 17.0f);
            mBaiduMap.animateMapStatus(u);
        }

        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null) {
                return;
            }
        }
    }

    /**
     * 反地理编码检索
     */
    public void getAddress(double lat, double lng) {
        LatLng mLatLng = new LatLng(lat, lng);
        ReverseGeoCodeOption mReverseGeoCodeOption = new ReverseGeoCodeOption(); // 反地理编码请求参数对象
        mReverseGeoCodeOption.location(mLatLng);// 设置请求参数
        mGeoCoder.reverseGeoCode(mReverseGeoCodeOption);// 发起反地理编码请求(经纬度->地址信息)
        mGeoCoder.setOnGetGeoCodeResultListener(geoCoderResultListener); // 设置查询结果监听者
    }

    OnGetGeoCoderResultListener geoCoderResultListener = new OnGetGeoCoderResultListener() {
        @Override
        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
            if (geoCodeResult == null
                    || geoCodeResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// 没有找到检索结果
                Toast.makeText(BaiduMapActivity.this, "未找到结果",
                        Toast.LENGTH_LONG).show();
                mBar.setVisibility(View.GONE);
                return;
            }


        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
            if (reverseGeoCodeResult.error == SearchResult.ERRORNO.NO_ERROR) {// 检索结果正常返回
//                mBaiduMap.clear();
                mBar.setVisibility(View.GONE);
                if (lastLocation == null) {
                    return;
                }
                String imgUrl = "http://api.map.baidu.com/staticimage?width=300&height=150&zoom=16&markers=" + lastLocation.getLongitude() + "," + lastLocation.getLatitude();
                MyLog.i("YUY", "静态地图 = " + imgUrl);
                mPoiAddress.clear();
                if (isLocaRefresh == 0) {
                    mPoiAddress.add(new PoiAddress(imgUrl, lastLocation.getAddrStr(), lastLocation.getPoi(), true, lastLocation.getLatitude(), lastLocation.getLongitude()));
                    for (int i = 0; i < reverseGeoCodeResult.getPoiList().size(); i++) {
                        PoiInfo poiInfo = reverseGeoCodeResult.getPoiList().get(i);
                        String poiImgUrl = "http://api.map.baidu.com/staticimage?width=300&height=150&zoom=16&markers=" + poiInfo.location.longitude + "," + poiInfo.location.latitude;
                        mPoiAddress.add(new PoiAddress(poiImgUrl, poiInfo.name, poiInfo.address, false, poiInfo.location.latitude, poiInfo.location.longitude));
                    }
                } else {
                    for (int i = 0; i < reverseGeoCodeResult.getPoiList().size(); i++) {
                        PoiInfo poiInfo = reverseGeoCodeResult.getPoiList().get(i);
                        String poiImgUrl = "http://api.map.baidu.com/staticimage?width=300&height=150&zoom=16&markers=" + poiInfo.location.longitude + "," + poiInfo.location.latitude;
                        mPoiAddress.add(new PoiAddress(poiImgUrl, poiInfo.name, poiInfo.address, i == 0 ? true : false, poiInfo.location.latitude, poiInfo.location.longitude));
                    }
                }

                mPoiAdd = mPoiAddress.get(0);
                mAdapter.notifyDataSetChanged();
                if (mPoiAddress != null) {
                    isLocaRefresh = 1;
                    mBaiduMap.clear();
                    showMap(mPoiAddress.get(0).getLat(), mPoiAddress.get(0).getLng());
                }

            }
        }
    };

}
