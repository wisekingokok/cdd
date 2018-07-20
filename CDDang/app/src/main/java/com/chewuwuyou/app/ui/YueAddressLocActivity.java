package com.chewuwuyou.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.utils.ToastUtil;

import net.tsz.afinal.annotation.view.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class YueAddressLocActivity extends BaseActivity implements OnGetPoiSearchResultListener, OnGetSuggestionResultListener, BaiduMap.OnMapClickListener,
        View.OnClickListener {

    @ViewInject(id = R.id.sub_header_bar_left_ibtn)
    private ImageButton mHeaderBackIBtn;
    @ViewInject(id = R.id.sub_header_bar_tv)
    private TextView mHeaderBarTV;
    // @ViewInject(id = R.id.sub_header_bar_right_tv)
    // private TextView mHeaderBarRightTV;
    @ViewInject(id = R.id.progressBar)
    private ProgressBar progressBar;
    private AutoCompleteTextView mQueryAutoCompTV;// query_auto_comp_tv;
    private SuggestionSearch mSuggestionSearch;
    private ArrayAdapter<String> mSuggestionAdapter;
    private LatLng mLatlng;// LatLng地理坐标基本数据结构
    private PoiSearch mPoiSearch;// 类PoiSearch继承poi检索接口
    public LocationClient mLocationClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private String city = "成都";
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private String mLocaAddrStr;
    private RelativeLayout mTitleHeight;//标题布局高度
    private String str = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yue_address_loc_ac);

        mTitleHeight = (RelativeLayout) findViewById(R.id.title_height);
        isTitle(mTitleHeight);//根据不同手机判断
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        getMyLocation();
        mHeaderBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
        mHeaderBarTV = (TextView) findViewById(R.id.sub_header_bar_tv);
        // mHeaderBarRightTV = (TextView)
        // findViewById(R.id.sub_header_bar_right_tv);

        mHeaderBarTV.setText("聚会地点");
        // mHeaderBarRightTV.setText("确认");
        // mHeaderBarRightTV.setVisibility(View.VISIBLE);
        mHeaderBackIBtn.setOnClickListener(this);
        // mHeaderBarRightTV.setOnClickListener(this);

        mQueryAutoCompTV = (AutoCompleteTextView) findViewById(R.id.query_auto_comp_tv);
        mPoiSearch = PoiSearch.newInstance();
        // 注册搜索事件监听
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
        mSuggestionAdapter = new ArrayAdapter<String>(this, R.layout.suggest_dropdown_item);
        mQueryAutoCompTV.setAdapter(mSuggestionAdapter);
        mQueryAutoCompTV.setThreshold(1);
        mQueryAutoCompTV.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (citys.get(arg2).equals("未找到相关结果")) return;
                city = citys.get(arg2);
                mPoiSearch.searchInCity((new PoiCitySearchOption()).city(city).keyword(mSuggestionAdapter.getItem(arg2)));
            }
        });
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
        mBaiduMap.setOnMapClickListener(this);
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {

            @SuppressWarnings("static-access")
            @Override
            public boolean onMarkerClick(final Marker marker) {
                TextView popupText = (TextView) getLayoutInflater().inflate(R.layout.bitmap_pop_up_window, null);
                popupText.setText(mLocaAddrStr);

                // mBaiduMap.showInfoWindow(new InfoWindow(popupText,
                // poi.location, 0));
                mBaiduMap.showInfoWindow(new InfoWindow(new BitmapDescriptorFactory().fromView(popupText), marker.getPosition(), -40,
                        new InfoWindow.OnInfoWindowClickListener() {

                            @Override
                            public void onInfoWindowClick() {
                                Intent intent = new Intent();
                                intent.putExtra("yue_address", mLocaAddrStr);
                                intent.putExtra("yue_city", city);
                                setResult(RESULT_OK, intent);
                                finishActivity();
                            }
                        }));
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient.isStarted())
            mLocationClient.stop();
        mLocationClient = null;
        mSuggestionSearch.destroy();
        mPoiSearch.destroy();
    }

    private List<String> citys = new ArrayList<>();

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

    // 检索查询事件监听实现的方法
    @Override
    public void onGetPoiResult(PoiResult result) {
        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND)
            return;
        // 判断搜索结果状态码result.error是否等于检索结果状态码， SearchResult.ERRORNO值的没问题
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            mBaiduMap.clear();
            PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(result);
            overlay.addToMap();
            overlay.zoomToSpan();
            return;
        }
        // AMBIGUOUS_KEYWORD表示 检索词有岐义
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
            String strInfo = "在";
            for (CityInfo cityInfo : result.getSuggestCityList()) {
                strInfo += cityInfo.city;
                strInfo += ",";
            }
            strInfo += "找到结果";
            Toast.makeText(YueAddressLocActivity.this, strInfo, Toast.LENGTH_LONG).show();
        }

    }

    private class MyPoiOverlay extends PoiOverlay {

        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @SuppressWarnings("static-access")
        @Override
        public boolean onPoiClick(int index) {
            super.onPoiClick(index);
            final PoiInfo poi = getPoiResult().getAllPoi().get(index);

            TextView popupText = (TextView) getLayoutInflater().inflate(R.layout.bitmap_pop_up_window, null);
            // 弹出泡泡
            popupText.setText(poi.name);
            // mBaiduMap.showInfoWindow(new InfoWindow(popupText, poi.location,
            // 0));
            mBaiduMap.showInfoWindow(new InfoWindow(new BitmapDescriptorFactory().fromView(popupText), poi.location, -40,
                    new InfoWindow.OnInfoWindowClickListener() {

                        @Override
                        public void onInfoWindowClick() {

                            Intent intent = new Intent();
                            intent.putExtra("yue_address", poi.name);
                            intent.putExtra("yue_city", poi.city);
                            setResult(RESULT_OK, intent);
                            finishActivity();
                        }
                    }));
            return true;
        }
    }

    /**
     * 定位自己的位置
     */
    public void getMyLocation() {
        mLocationClient = new LocationClient(YueAddressLocActivity.this);
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

        @SuppressWarnings("static-access")
        @Override
        // 接收位置信息
        public void onReceiveLocation(final BDLocation location) {
            if (location == null)
                return;
            if (location.getLocType() == BDLocation.TypeNetWorkLocation) {

                mLatlng = new LatLng(location.getLatitude(), location.getLongitude());
                mLocaAddrStr = location.getAddrStr();
                city = location.getCity();
                BitmapDescriptor bimp = new BitmapDescriptorFactory().fromResource(R.drawable.icon_mappin);
                OverlayOptions option = new MarkerOptions().position(mLatlng).icon(bimp);
                mBaiduMap.addOverlay(option);
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(mLatlng);
                mBaiduMap.animateMapStatus(u);

                TextView popupText = (TextView) getLayoutInflater().inflate(R.layout.bitmap_pop_up_window, null);
                // 弹出泡泡
                popupText.setText(location.getAddrStr());
                mBaiduMap.showInfoWindow(new InfoWindow(new BitmapDescriptorFactory().fromView(popupText), mLatlng, -40,
                        new InfoWindow.OnInfoWindowClickListener() {

                            @Override
                            public void onInfoWindowClick() {
                                Intent intent = new Intent();
                                intent.putExtra("yue_address", mLocaAddrStr);
                                intent.putExtra("yue_city", city);
                                setResult(RESULT_OK, intent);
                                finishActivity();
                            }
                        }));
            }
            mLocationClient.stop();
        }

        // 接收POI信息函数
        @Override
        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null) {
                return;
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn:
                setResult(RESULT_CANCELED, null);
                finishActivity();
                break;
            // case R.id.sub_header_bar_right_tv:
            // Intent intent = new Intent();
            // intent.putExtra("yue_address",
            // mQueryAutoCompTV.getText().toString());
            // setResult(RESULT_OK, intent);
            // break;
//            case R.id.search:
//                /**
//                 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
//                 */
//                String str = mQueryAutoCompTV.getText().toString();
//                if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(city))
//                    mSuggestionSearch.requestSuggestion((new SuggestionSearchOption()).keyword(str).city(city));
//                break;
            default:
                break;
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult result) {
        Intent intent = new Intent();
        intent.putExtra("yue_address", result.getAddress());
        setResult(RESULT_OK, intent);
        finishActivity();
    }

    @Override
    public void onMapClick(LatLng point) {
        mBaiduMap.hideInfoWindow();
    }

    @Override
    public boolean onMapPoiClick(MapPoi arg0) {
        return false;
    }

}
