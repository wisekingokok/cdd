
package com.chewuwuyou.app.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.chewuwuyou.app.R;

/**
 * 车标大全
 * 
 * @author Administrator
 */
public class CarLogofCountryActivity extends BaseActivity {

    private GridView mCountryGird;
    private RelativeLayout mTitleHeight;//标题布局高度
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carservice_logof_first_ac);
        initView();
    }

    public void initView() {
    	mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
        ((TextView) findViewById(R.id.sub_header_bar_tv)).setText(R.string.car_logof_title);

        findViewById(R.id.sub_header_bar_left_ibtn).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finishActivity();
            }
        });
        mCountryGird = (GridView) findViewById(R.id.contry_grid);
        final String[] country = getResources().getStringArray(R.array.country);
        Integer[] countryLog = {
                R.drawable.china, R.drawable.riben,

                R.drawable.deguo, R.drawable.faguo,

                R.drawable.yidali, R.drawable.yinguo, R.drawable.meiguo,

                R.drawable.hanguo, R.drawable.other
        };

        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < country.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImage", countryLog[i]);
            map.put("ItemText", country[i]);
            lstImageItem.add(map);
        }

        SimpleAdapter saImageItems = new SimpleAdapter(this, lstImageItem,
                R.layout.carservice_logof_item,

                new String[] {
                        "ItemImage", "ItemText"
                },

                new int[] {
                        R.id.carLogoItemImage, R.id.carLogoItemText
                });

        mCountryGird.setAdapter(saImageItems);
        mCountryGird.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                Intent intent = new Intent(CarLogofCountryActivity.this,
                        CarLogofActivity.class);
                intent.putExtra("countryId", arg2 + "");
                intent.putExtra("countryName", country[arg2]);
                startActivity(intent);
            }
        });
    }

}
