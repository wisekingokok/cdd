/**
 * @Title:
 * @Copyright:
 * @Description:
 * @author:
 * @date:2015-3-23上午11:32:16
 * @version:1.2.1
 */
package com.chewuwuyou.app.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.CarBrandAdapter;
import com.chewuwuyou.app.adapter.ModelAdapter;
import com.chewuwuyou.app.bean.CarBrand;
import com.chewuwuyou.app.bean.CarModel;
import com.chewuwuyou.app.bean.Model;
import com.chewuwuyou.app.bean.Serie;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.widget.BladeView;
import com.chewuwuyou.app.widget.BladeView.OnItemClickListener;
import com.chewuwuyou.app.widget.PinnedHeaderListView;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:新车查价
 * @author:yuyong
 * @date:2015-3-23上午11:32:16
 * @version:1.2.1
 */
public class NewCarQueryPriceActivity extends BaseActivity {

	@ViewInject(id = R.id.sub_header_bar_left_ibtn)
	private ImageButton mBackBtn;
	@ViewInject(id = R.id.sub_header_bar_tv)
	private TextView mHeaderTV;

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
	// 首字母对应的位置
	private Map<String, Integer> mIndexer;
	//private Context mContext;
	private ListView mModelList;
	private LinearLayout mModelLLayout;
	private List<CarModel> mCarModels;
	private ImageView mImgHide;
	private Map<String, List<CarModel>> mMap1;
	private List<Model> mModels;
	private List<String> mSections1;
	private String brand = "", carModel = "";// 品牌、车型
	private RelativeLayout mTitleHeight;//标题布局高度
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_carbrand_ac);
		mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
		mHeaderTV.setText(R.string.choose_carbrand_title);
		mBackBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finishActivity();
			}
		});
		//mContext = this;
		initData();
		initView();

	}

	private void initData() {
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
		//
		mCarModels = CarModel.parseBrands(getFromAssets("mobile_models"));
		mMap1 = new HashMap<String, List<CarModel>>();
		mSections1 = new ArrayList<String>();
		for (CarModel carModel : mCarModels) {
			String parentBrand = carModel.getParent();
			if (mSections1.contains(parentBrand)) {
				mMap1.get(parentBrand).add(carModel);
			} else {
				mSections1.add(parentBrand);
				List<CarModel> list = new ArrayList<CarModel>();
				list.add(carModel);
				mMap1.put(parentBrand, list);
			}
		}
	}

	private void initView() {
		mListView = (PinnedHeaderListView) findViewById(R.id.brand_display);
		mModelList = (ListView) findViewById(R.id.model_list);
		mModelLLayout = (LinearLayout) findViewById(R.id.model_linearlayout);
		mImgHide = (ImageView) findViewById(R.id.img_hide);
		mImgHide.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				StatService.onEvent(NewCarQueryPriceActivity.this,
						"ClickChooseCarBrandHideIV", "点击选择汽车品牌隐藏图标");
				mModelLLayout.setVisibility(View.GONE);
			}
		});
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
				brand = mBrands.get(arg2).getName();
				System.out.println("slug==========" + slug);
				mModelLLayout.setVisibility(View.VISIBLE);

				List<CarModel> datas = mMap1.get(slug);
				if (datas != null) {
					mModels = new ArrayList<Model>();
					for (CarModel carModel : datas) {
						for (Serie modelName : carModel.getSeries()) {
							Model model = new Model();
							model.setFac(carModel.getFac());
							model.setName(modelName.getName());
							model.setIcon(modelName.getIcon());
							model.setPrice(modelName.getPrice());
							mModels.add(model);
						}
					}
					ModelAdapter adapter = new ModelAdapter(
							NewCarQueryPriceActivity.this, datas, true);
					mModelList.setAdapter(adapter);
					mModelList
							.setOnItemClickListener(new AdapterView.OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									mModelLLayout.setVisibility(View.GONE);
									carModel = mModels.get(arg2).getName();
									Intent intent = new Intent();
									intent.putExtra("brand", brand);
									intent.putExtra("model", carModel);
									setResult(Constant.Vehicle.CHOICE_MODEL,
											intent);
									//finishActivity();
								}
							});
				} else {
					// finishActivity();
				}
			}

		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPause(NewCarQueryPriceActivity.this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(NewCarQueryPriceActivity.this);
	}
}
