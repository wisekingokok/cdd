
package com.chewuwuyou.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.chewuwuyou.app.R;

/**
 * @describe：选择类别
 * @author ：caixuemei
 * @created ：2014-7-4下午7:35:44
 */
public class RecordSelectCategoryActivity extends BaseActivity {

    private TextView mNaviBarTitle;
    private ImageButton mNaviBarBack;
    private ListView mCategoryList;
    private RelativeLayout mTitleHeight;//标题布局高度
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.record_select_category_ac);

        init();
    }

    /**
     * 初始化布局
     */
    private void init() {
    	
    	mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
		
        mNaviBarTitle = (TextView) findViewById(R.id.sub_header_bar_tv);
        mNaviBarTitle.setText(R.string.select_category);
        mNaviBarBack = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
        mCategoryList = (ListView) findViewById(R.id.categoryList);

        String[] array = getResources().getStringArray(R.array.record_select_category);
        // 需要全部选项的时候
        if (getIntent().getIntExtra("all", 0) == 1) {
            String[] oldArray = array;
            array = new String[array.length + 1];
            array[0] = "所有";
            //changed by xh 20150202 for ArrayIndexOutOfBoundsException start
            //for (int i = 0, len = oldArray.length; i < len; i++) {
            //    array[i] = oldArray[i - 1];
            //}
            for (int i = 1, len = oldArray.length; i <= len; i++) {
                array[i] = oldArray[i - 1];
            }
            //changed by xh 20150202 for ArrayIndexOutOfBoundsException end
        }
        final String[] putArray = array;
        // 实例化ArrayAdapter对象
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.record_select_category_item, R.id.categoryName, putArray);
        
        // 绑定适配器
        mCategoryList.setAdapter(adapter);

        // 返回
        mNaviBarBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                finish();
            }
        });

        // // 广告
        // mCommonMsgTxt.setOnClickListener(new View.OnClickListener() {
        //
        // @Override
        // public void onClick(View arg0) {
        //
        // Intent intent = new Intent(RecordSelectCategoryActivity.this,
        // CommonAdvActivity.class);
        // Bundle bundle = new Bundle();
        // bundle.putInt("locationbgId", R.drawable.common_bg);
        // bundle.putInt("advId", R.string.car_bottom_adv_ac_adv);
        // bundle.putInt("titleId", R.string.select_category);
        // bundle.putInt("bottombg", R.drawable.common_bottom_yellow);
        // intent.putExtras(bundle);
        // startActivity(intent);
        // }
        // });

        // 选择类别
        mCategoryList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                Intent intent = new Intent();
                intent.putExtra("choicecategory", putArray[arg2]);
                setResult(30, intent);
                finishActivity();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(RecordSelectCategoryActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(RecordSelectCategoryActivity.this);
    }
}
