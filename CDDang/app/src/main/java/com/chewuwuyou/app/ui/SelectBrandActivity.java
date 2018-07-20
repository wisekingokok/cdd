package com.chewuwuyou.app.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.CarBrandAdapter;
import com.chewuwuyou.app.bean.CarBrand;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.widget.BladeView;
import com.chewuwuyou.app.widget.BladeView.OnItemClickListener;
import com.chewuwuyou.app.widget.PinnedHeaderListView;

public class SelectBrandActivity extends BaseActivity {

	private static final String FORMAT = "^[a-z,A-Z].*$";
	private PinnedHeaderListView mListView;
	private BladeView mLetter;
	private CarBrandAdapter mAdapter;
	private List<CarBrand> mBrands;
	// 首字母集
	private List<String> mSections;
	// 根据首字母存放数据
	private Map<String, List<CarBrand>> mMap;
	// 首字母位置集
	private List<Integer> mPositions;
	private RelativeLayout mTitleHeight;//标题布局高度
	// 首字母对应的位置
	private Map<String, Integer> mIndexer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_brand_ac);

		initData();
		initView();

		((TextView) findViewById(R.id.sub_header_bar_tv))
				.setText(R.string.select_brand);

		findViewById(R.id.sub_header_bar_left_ibtn).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						finishActivity();
					}
				});
	}

	private void initData() {
		mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
		mBrands = CarBrand.parseBrands(getFromAssets("mobile_brands"));
		mSections = new ArrayList<String>();
		mMap = new HashMap<String, List<CarBrand>>();
		mPositions = new ArrayList<Integer>();
		mIndexer = new HashMap<String, Integer>();
		for (CarBrand brand : mBrands) {
			String firstLetter = brand.getFirst_letter();
			if (firstLetter.matches(FORMAT)) {
				if (mSections.contains(firstLetter)) {
					mMap.get(firstLetter).add(brand);
				} else {
					mSections.add(firstLetter);
					List<CarBrand> list = new ArrayList<CarBrand>();
					list.add(brand);
					mMap.put(firstLetter, list);
				}
			} else {
				// 热门车型
				if (mSections.contains("#")) {
					mMap.get("#").add(brand);
				} else {
					mSections.add("#");
					List<CarBrand> list = new ArrayList<CarBrand>();
					list.add(brand);
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
		mAdapter = new CarBrandAdapter(this, mBrands, mSections, mPositions);
		mListView.setAdapter(mAdapter);
		mListView.setOnScrollListener(mAdapter);
		mListView.setPinnedHeaderView(LayoutInflater.from(this)
				.inflate(R.layout.select_brand_item, mListView, false)
				.findViewById(R.id.title));
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String slug = mBrands.get(arg2).getSlug();
				System.out.println("slug==========" + slug);
				try {
					Intent intent = new Intent();
					intent.putExtra("parent", slug);
					intent.putExtra("brandlogo", mBrands.get(arg2)
							.getLogo_img());
					intent.putExtra("brandname", mBrands.get(arg2).getName());

					CacheTools.setUserData("brandname", mBrands.get(arg2)
							.getName());
					setResult(50, intent);
					// mContext.startActivity(intent);
					// Intent intent = new Intent(mContext,
					// SelectCarModelActivity.class);
					// intent.putExtra("parent", slug);
					// intent.putExtra("brandlogo", mBrands.get(arg2)
					// .getLogo_img());
					// intent.putExtra("brandname", mBrands.get(arg2)
					// .getName());
					// CacheTools.setUserData("brandname", mBrands.get(arg2)
					// .getName());
					// mContext.startActivity(intent);
					finishActivity();

				} catch (Exception e) {
				}

			}

		});
	}

}
