package com.chewuwuyou.app.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.fragment.DataReportRootFragment;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/1 0001.
 */
public class DataReportActivity extends BaseFragmentActivity {
    private CirclePageIndicator indicator;
    private ViewPager viewPager;
    private ImageButton back;
    private TextView title;
    private List<Fragment> list = new ArrayList<Fragment>();
    private DataPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_report);
        initView();
        initData();
        initEvent();
    }

    protected void initView() {
        indicator = (CirclePageIndicator) findViewById(R.id.circle_page_indicator);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        back = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
        title = (TextView) findViewById(R.id.sub_header_bar_tv);
    }


    protected void initData() {
        title.setText("收支统计");
        list.add(DataReportRootFragment.getEntity(DataReportRootFragment.TYPE_ROOT));
        list.add(DataReportRootFragment.getEntity(DataReportRootFragment.TYPE_OUT));
        list.add(DataReportRootFragment.getEntity(DataReportRootFragment.TYPE_IN));
        adapter = new DataPagerAdapter(getSupportFragmentManager(), list);
        viewPager.setOffscreenPageLimit(3);// 设置缓存view 的个数（实际有3个，缓存2个+正在显示的1个）
        viewPager.setAdapter(adapter);
    }


    protected void initEvent() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });
        indicator.setViewPager(viewPager);
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                switch (arg0) {
                    case 0:
                        title.setText("收支统计");
                        break;
                    case 1:
                        title.setText("发出订单统计");
                        break;
                    case 2:
                        title.setText("收到订单统计");
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }


    class DataPagerAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> list;

        public DataPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public DataPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int arg0) {
            if (list.get(arg0) == null) {

            }
            return list.get(arg0);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }
}
