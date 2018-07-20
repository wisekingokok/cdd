package com.chewuwuyou.rong.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.eventbus.EventBusAdapter;
import com.chewuwuyou.app.ui.CDDBaseActivity;
import com.chewuwuyou.rong.adapter.RongImgViewPagerAdapter;

import java.util.ArrayList;

import cn.trinea.android.view.autoscrollviewpager.ViewPagerFixed;
import de.greenrobot.event.EventBus;

/**
 * Created by xxy on 2016/9/22 0022.
 */
public class RongImgViewPager extends CDDBaseActivity implements View.OnClickListener {
    private ViewPagerFixed viewPager;
    private ArrayList<String> list;// 接收传递过来的url图片路劲
    private String suBscript;// 传递过来的下标
    private ArrayList<View> listView;// 定义集合存放view
    private RongImgViewPagerAdapter vehicleAdapter;// 创建适配器
    private TextView current, pageCount;// 当前页数，总页数
    private LinearLayout mVehicleQuanId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehicle_quan_view_pager);
        initView();// 初始化控件
        initData();
        initEvent();// 逻辑处理
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        viewPager = (ViewPagerFixed) findViewById(R.id.vehivleViewPager);
        current = (TextView) findViewById(R.id.current); // 当前页数
        pageCount = (TextView) findViewById(R.id.pagecount); // 总页数
        mVehicleQuanId = (LinearLayout) findViewById(R.id.vehicle_quan_id);
    }


    @Override
    protected void initData() {
        Intent intent = getIntent();
        list = intent.getStringArrayListExtra("url");// 接收传递过来的url
        suBscript = intent.getStringExtra("viewPagerPosition");// 接收传递过来的图片下标
        vehicleAdapter = new RongImgViewPagerAdapter(this, list);
        viewPager.setAdapter(vehicleAdapter);
        viewPager.setCurrentItem(Integer.parseInt(suBscript));// 设置显示的当前图片
        current.setText(Integer.parseInt(suBscript) + 1 + "/");
        pageCount.setText(list.size() + "");
    }

    /**
     * 事件监听
     */
    @Override
    protected void initEvent() {
        mVehicleQuanId.setOnClickListener(this);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                current.setText(position + 1 + "/");
                pageCount.setText(list.size() + "");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(EventBusAdapter busAdapter) {//接收过来业务大厅订单消息显示小红点
        if(busAdapter.getEventimg().equals("0")){
                   finishActivity();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.vehicle_quan_id:
                finishActivity();
                break;
            default:
                break;
        }

    }
}
