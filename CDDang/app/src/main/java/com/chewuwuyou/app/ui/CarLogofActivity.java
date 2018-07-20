
package com.chewuwuyou.app.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.CarLogOfAdapter;
import com.chewuwuyou.app.bean.CarBrand;

/**
 * 选择国家后显示相应的车标
 * 
 * @author Administrator
 */
public class CarLogofActivity extends BaseActivity {
    private List<CarBrand> mBrands;// 车辆品牌集合
    private GridView mLogofGrid;
    private CarLogOfAdapter adapter;
    private RelativeLayout mTitleHeight;//标题布局高度
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carservice_logof_second_ac);
        initView();
    }

    public void initView() {
    	mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
		
        ((TextView) findViewById(R.id.sub_header_bar_tv)).setText(getIntent().getStringExtra(
                "countryName")
                + "品牌");
        findViewById(R.id.sub_header_bar_left_ibtn).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finishActivity();
            }
        });
        mLogofGrid = (GridView) findViewById(R.id.logof_grid);
        // 获取到车标json
        mBrands = CarBrand.parseBrands(getFromAssets("mobile_brands"));
        Intent intent = getIntent();
        final List<CarBrand> mCountryCarLogoList = new ArrayList<CarBrand>();
        String countryId = intent.getStringExtra("countryId");
        System.out.println("===========国家Id" + countryId);
        mLogofGrid = (GridView) findViewById(R.id.logof_grid);
        if (mBrands != null) {
            for (CarBrand carBrand : mBrands) {
                if (carBrand.getCountry_type().equals(countryId)) {
                    mCountryCarLogoList.add(carBrand);
                }

            }
        }
        adapter = new CarLogOfAdapter(CarLogofActivity.this, mCountryCarLogoList);
        mLogofGrid.setAdapter(adapter);
        // 进入相应国家的车标
        mLogofGrid.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                Intent intent = new Intent(CarLogofActivity.this,
                        CarLogofDetailsActivity.class);
                intent.putExtra("carLogo", mCountryCarLogoList.get(arg2).getLogo_img());
                intent.putExtra("logoName", mCountryCarLogoList.get(arg2).getName());
                intent.putExtra("logoDetails", mCountryCarLogoList.get(arg2).getIntro());
                startActivity(intent);
            }
        });
    }

}
