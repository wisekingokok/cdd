package com.chewuwuyou.rong.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.ui.CDDBaseActivity;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.app.widget.ActionSheet;
import com.chewuwuyou.rong.bean.CDDLBSMsg;

import net.tsz.afinal.annotation.view.ViewInject;

import java.io.File;

/**
 * Created by yuyong on 2016/9/24.
 * 查看位置信息并导航
 */
public class ShowLocationMapActivity extends CDDBaseActivity implements View.OnClickListener {
    @ViewInject(id = R.id.sub_header_bar_left_ibtn)
    ImageButton mBackIBtn;
    @ViewInject(id = R.id.sub_header_bar_tv)
    TextView mTitleTV;
    @ViewInject(id = R.id.title_height)
    RelativeLayout mTitleRL;
    @ViewInject(id = R.id.bmapView)
    MapView mMapView;
    @ViewInject(id = R.id.address_tv)
    TextView mAddressTV;
    @ViewInject(id = R.id.nevation_iv)
    ImageView mNevationIV;//导航
    private BaiduMap mBaiduMap = null;
    private String mGaoDeUri;
    private CDDLBSMsg cddlbsMsg;//位置消息实体

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_map_location);
        initView();
        initData();
        initEvent();
    }

    @Override
    protected void initView() {
        isTitle(mTitleRL);
        mBaiduMap = mMapView.getMap();
    }

    @Override
    protected void initData() {
        mTitleTV.setText("位置信息");
        cddlbsMsg = getIntent().getParcelableExtra(com.chewuwuyou.rong.utils.Constant.LOC_MSG);
        Log.e("YUY", "------lat = " + cddlbsMsg.getLat() + " lng = " + cddlbsMsg.getLng());
        mAddressTV.setText(cddlbsMsg.getPoi());
        showMap(cddlbsMsg.getLat(), cddlbsMsg.getLng());
    }

    @Override
    protected void initEvent() {
        mBackIBtn.setOnClickListener(this);
        mNevationIV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sub_header_bar_left_ibtn:
                finishActivity();
                break;
            case R.id.nevation_iv:
                navigation();//导航
                break;
        }
    }

    private void navigation() {

        setTheme(R.style.ActionSheetStyleIOS7);
        ActionSheet menuView = new ActionSheet(ShowLocationMapActivity.this);
        menuView.setCancelButtonTitle("取 消");
        menuView.addItems("百度地图", "高德地图");
        menuView.setItemClickListener(new ActionSheet.MenuItemClickListener() {
            @Override
            public void onItemClick(int itemPosition) {
                switch (itemPosition) {
                    case 0:
                        String loc = null;
                        if (!TextUtils.isEmpty(cddlbsMsg.getPoi())) {
                            loc = cddlbsMsg.getLat() + "," + cddlbsMsg.getLng() + "," + cddlbsMsg.getPoi();
                        } else {
                            ToastUtil.toastShow(ShowLocationMapActivity.this, "暂无内容");
                        }
                        if (isInstallByread("com.baidu.BaiduMap")) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            Uri uri = Uri.parse("geo:" + loc);
                            intent.setData(uri);
                            intent.setPackage("com.baidu.BaiduMap");
                            startActivity(intent);
                        } else {
                            ToastUtil.toastShow(ShowLocationMapActivity.this, "未安装百度地图应用");
                        }
                        break;
                    case 1:
                        mGaoDeUri = "androidamap://navi?sourceApplication=appname&poiname=" + cddlbsMsg.getPoi() + "&lat=" + cddlbsMsg.getLat()
                                + "&lon=" + cddlbsMsg.getLng() + "&dev=1&style=2";
                        if (isInstallByread("com.autonavi.minimap")) {
                            Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse(mGaoDeUri));
                            intent.setPackage("com.autonavi.minimap");
                            startActivity(intent);
                        } else {
                            ToastUtil.toastShow(ShowLocationMapActivity.this, "未安装高德地图应用");
                        }
                        break;
                    default:
                        break;
                }

            }
        });
        menuView.setCancelableOnTouchMenuOutside(true);
        menuView.showMenu();
    }

    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    private void showMap(double latitude, double longtitude) {
        LatLng llA = new LatLng(latitude, longtitude); //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder().target(llA).zoom(18).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        OverlayOptions ooA = new MarkerOptions().position(llA).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marka)).zIndex(4).draggable(true);
        mBaiduMap.addOverlay(ooA);
        mBaiduMap.setMapStatus(mMapStatusUpdate); //改变地图状态
//        CoordinateConverter converter = new CoordinateConverter();
//        converter.coord(llA);
//        converter.from(CoordinateConverter.CoordType.COMMON);
//        LatLng convertLatLng = converter.convert();
//        OverlayOptions ooA = new MarkerOptions().position(convertLatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marka)).zIndex(4).draggable(true);
//        mBaiduMap.addOverlay(ooA);
//        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(convertLatLng, 17.0f);
//        mBaiduMap.animateMapStatus(u);

    }
}
