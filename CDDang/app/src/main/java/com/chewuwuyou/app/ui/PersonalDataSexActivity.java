
package com.chewuwuyou.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;

/**
 * @author Administrator
 */
public class PersonalDataSexActivity extends BaseActivity {

    private ListView mSexList;// 性别列表
    private RelativeLayout mTitleHeight;//标题布局高度
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_data_sex_ac);
        init();
    }

    /**
     * 初始化布局
     */
    private void init() {
    	mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
        ((TextView) findViewById(R.id.sub_header_bar_tv)).setText("修改性别");
        findViewById(R.id.sub_header_bar_left_ibtn).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finishActivity();
            }
        });
        mSexList = (ListView) findViewById(R.id.sexList);

        final String[] array = getResources().getStringArray(R.array.personal_data_sex);
        // 实例化ArrayAdapter对象
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.personal_data_sex_item, R.id.sex, array);
        // 绑定适配器
        mSexList.setAdapter(adapter);

        // 性别
        mSexList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                Intent intent = new Intent();
                intent.putExtra("choicesex", arg2);
                setResult(30, intent);
                finishActivity();
            }
        });
    }

}
