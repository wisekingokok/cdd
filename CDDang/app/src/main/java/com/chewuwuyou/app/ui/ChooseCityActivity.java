
package com.chewuwuyou.app.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.ProvinceAdapter;
import com.chewuwuyou.app.bean.Province;
import com.chewuwuyou.app.widget.BladeView;
import com.chewuwuyou.app.widget.BladeView.OnItemClickListener;
import com.chewuwuyou.app.widget.PinnedHeaderListView;

/**
 * @describe:选择城市
 * @author:xiajy
 * @version 1.1.0
 * @created:2014-11-21上午9:48:34
 */
public class ChooseCityActivity extends BaseActivity {

    @ViewInject(id = R.id.sub_header_bar_left_ibtn)
    private ImageButton mBackBtn;
    @ViewInject(id = R.id.sub_header_bar_tv)
    private TextView mHeaderTV;
    private static final String FORMAT = "^[a-z,A-Z].*$";
    private PinnedHeaderListView mListView;
    private BladeView mLetter;
    private ProvinceAdapter mAdapter;
    private List<Province> mProvinces;
    // 首字母集
    private List<String> mSections;
    // 根据首字母存放数据
    private Map<String, List<Province>> mMap;
    // 首字母位置集
    private List<Integer> mPositions;
    // 首字母对应的位置
    private Map<String, Integer> mIndexer;
    private Context mContext;

    private ListView mSubList;
    private LinearLayout mSubLayout;
    private ImageView mImgHide;
    private RelativeLayout mTitleHeight;//标题布局高度
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_city1_ac);
        mHeaderTV.setText("城市选择");
        mBackBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finishActivity();
            }
        });
        mContext = this;
        initData();
        initView();

    }

    private void initData() {
    	mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
    	
        mProvinces = Province.parses(getFromAssets("city11.json"));
        mSections = new ArrayList<String>();
        mMap = new HashMap<String, List<Province>>();
        mPositions = new ArrayList<Integer>();
        mIndexer = new HashMap<String, Integer>();
        for (Province province : mProvinces) {
            String firstLetter = province.getCode();
            if (firstLetter.matches(FORMAT)) {
                if (mSections.contains(firstLetter)) {
                    mMap.get(firstLetter).add(province);
                } else {
                    mSections.add(firstLetter);
                    List<Province> list = new ArrayList<Province>();
                    list.add(province);
                    mMap.put(firstLetter, list);
                }
            } else {
                // 热门车型
                if (mSections.contains("#")) {
                    mMap.get("#").add(province);
                } else {
                    mSections.add("#");
                    List<Province> list = new ArrayList<Province>();
                    list.add(province);
                    mMap.put("#", list);
                }
            }
        }

        Collections.sort(mSections);
        int position = 0;
        for (int i = 0, size = mSections.size(); i < size; i++) {
            mIndexer.put(mSections.get(i), position);// 存入map中，key为首字母字符串，value为首字母在listview中位置
            mPositions.add(position);// 首字母在listview中位置，存入list中
            position += mMap.get(mSections.get(i)).size();// 计算下一个首字母在listview的位置
        }

    }

    private void initView() {
        mListView = (PinnedHeaderListView) findViewById(R.id.brand_display);
        mLetter = (BladeView) findViewById(R.id.brand_letterlistview);
        mLetter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(String s) {
                if (mIndexer.get(s) != null) {
                    mListView.setSelection(mIndexer.get(s));
                }
            }
        });
        mAdapter = new ProvinceAdapter(this, mProvinces, mSections, mPositions);
        mListView.setAdapter(mAdapter);
        mListView.setOnScrollListener(mAdapter);
        mListView.setPinnedHeaderView(LayoutInflater.from(this)
                .inflate(R.layout.select_brand_item, mListView, false)
                .findViewById(R.id.title));

        mSubList = (ListView) findViewById(R.id.model_list);
//        mSubLayout = (LinearLayout) findViewById(R.id.model_linearlayout);
//        mImgHide = (ImageView) findViewById(R.id.img_hide);
        mImgHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
            	StatService.onEvent(ChooseCityActivity.this, "clicChooseCityHideIV", "点击选择城市隐藏图标");
                mSubLayout.setVisibility(View.GONE);
            }
        });
        TextView tv = new TextView(mContext);
        tv.setText("全部");
        mSubList.addHeaderView(tv);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                final List<String> citys = mProvinces.get(arg2).getCitys();
                final String province = mProvinces.get(arg2).getProvince();
                System.out.println("province==========" + province);
                mSubLayout.setVisibility(View.VISIBLE);
                if (citys != null) {
                    ArrayAdapter<String> cityadapter = new ArrayAdapter<String>(mContext,
                            R.layout.select_city_item, R.id.textValue, citys);

                    mSubList.setAdapter(cityadapter);
                    mSubList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                            mSubLayout.setVisibility(View.GONE);
                            Intent intent = new Intent();
                            if (arg2 == 0) {
                                intent.putExtra("chooseDistrict", province);
                                intent.putExtra("province", province);
                                intent.putExtra("city", "");
                            } else {
                                System.out.println(province + "/" + citys.get(arg2 - 1));
                                intent.putExtra("chooseDistrict", province + citys.get(arg2 - 1));
                                intent.putExtra("province", province);
                                intent.putExtra("city", citys.get(arg2 - 1));
                            }
                            setResult(RESULT_OK, intent);
                            finishActivity();

                        }
                    });
                }
            }

        });
    }
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		StatService.onPause(ChooseCityActivity.this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		StatService.onResume(ChooseCityActivity.this);
	}
}
