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
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.CarAssistantAdapter;
import com.chewuwuyou.app.bean.CarAssistantServiceItem;
import com.chewuwuyou.app.utils.Constant.ShopType;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:行车助手
 * @author:yuyong
 * @date:2015-5-16下午3:51:29
 * @version:1.2.1
 */
public class CarAssistantActivity extends BaseActivity implements
		OnClickListener {

	private TextView mTitleTV;
	private ImageButton mBackIBtn;
	private GridView mCarAssistantGV;
	private List<CarAssistantServiceItem> mCarAssistantItems;
	private int[] imgResIds = { R.drawable.sicons01, R.drawable.sicons011,
			R.drawable.sicons02, R.drawable.sicons03, R.drawable.sicons04,
			R.drawable.sicons05, R.drawable.sicons10 };
	private String[] carAssistantStrs;
	private CarAssistantAdapter mAdapter;
	private RelativeLayout mTitleHeight;//标题布局高度

	// private AutoScrollViewPager mViewPager;
	// private CirclePageIndicator mCirclePageIndicator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.car_assistant_ac);
		initView();
		initDate();
	}

	private void initView() {
		mCarAssistantGV = (GridView) findViewById(R.id.car_assistant_gv);
		mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
		mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		// mViewPager = (AutoScrollViewPager)
		// findViewById(R.id.auto_view_pager);
		// mCirclePageIndicator = (CirclePageIndicator)
		// findViewById(R.id.circle_page_indicator);
		mBackIBtn.setOnClickListener(this);

		// new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		// try {
		// MyLog.i("YUY", "物流信息 = " + WLUtil.getOrderTracesByJson());
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// }).start();

	}

	private void initDate() {
		
		mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
		
		carAssistantStrs = getResources().getStringArray(
				R.array.car_assistant_arr);
		mTitleTV.setText(R.string.driving_assistant_title);
		// List<Integer> data = new ArrayList<Integer>();
		// data.add(R.drawable.cddbg1);
		// data.add(R.drawable.cddbg2);
		// data.add(R.drawable.cddbg3);
		// mViewPager.setAdapter(new
		// ImagePagerAdapter2(CarAssistantActivity.this,
		// data));
		// mViewPager.startAutoScroll();
		// mViewPager.setInterval(2000);
		// mCirclePageIndicator.setViewPager(mViewPager);
		// mViewPager.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE /
		// 2
		// % data.size());
		mCarAssistantItems = new ArrayList<CarAssistantServiceItem>();
		for (int i = 0; i < imgResIds.length; i++) {
			CarAssistantServiceItem carItem = new CarAssistantServiceItem();
			carItem.setImgResId(imgResIds[i]);
			carItem.setNameTV(carAssistantStrs[i]);
			mCarAssistantItems.add(carItem);
		}
		mAdapter = new CarAssistantAdapter(CarAssistantActivity.this,
				mCarAssistantItems);
		mCarAssistantGV.setAdapter(mAdapter);
		mCarAssistantGV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switch (arg2) {
				case 0:// 新车导购
					Intent intent = new Intent(CarAssistantActivity.this,
							NewCarQueryPriceActivity.class);
					startActivity(intent);
					break;
				case 1:// 4s店查询
					Intent shopIntent = new Intent(CarAssistantActivity.this,
							CommonShopListActivity.class);
					shopIntent.putExtra("shopType", ShopType.FOURSSTORES);
					startActivity(shopIntent);
					break;
				case 2:// 洗车美容
					Intent fourshopIntent = new Intent(
							CarAssistantActivity.this,
							CommonShopListActivity.class);
					fourshopIntent.putExtra("shopType", ShopType.CARBEAUTY);
					startActivity(fourshopIntent);
					break;
				case 3:// 维修保养
					Intent maintenanceIntent = new Intent(
							CarAssistantActivity.this,
							CommonShopListActivity.class);
					maintenanceIntent
							.putExtra("shopType", ShopType.MAINTENANCE);
					startActivity(maintenanceIntent);
					break;
				case 4:// 汽车配件
					Intent autoPartsIntent = new Intent(
							CarAssistantActivity.this,
							CommonShopListActivity.class);
					autoPartsIntent.putExtra("shopType", ShopType.AUTOPARTS);
					startActivity(autoPartsIntent);
					break;
				// case 5:// 爱车账本
				// StatService.onEvent(CarAssistantActivity.this,
				// "ClickMainActAczb", "点击主界面爱车账本");
				// Intent recIntent = new Intent(CarAssistantActivity.this,
				// RecordActivity.class);
				// startActivity(recIntent);
				// break;
				case 5:// 保险助手
					StatService.onEvent(CarAssistantActivity.this,
							"ClickMainActYygj", "点击主界面应用工具");
					Intent YyGjIntent = new Intent(CarAssistantActivity.this,
							InsureComputeAcitivity.class);
					startActivity(YyGjIntent);
					break;
				// case 6:// 停车助手
				// Intent pIntent = new Intent(CarAssistantActivity.this,
				// ParkbudGasStationActivity.class);
				// pIntent.putExtra("assistantType", AssistantType.PARK_BUD);
				// startActivity(pIntent);
				// break;
				// case 7:// 加油助手
				// Intent gIntent = new Intent(CarAssistantActivity.this,
				// ParkbudGasStationActivity.class);
				// gIntent.putExtra("assistantType", AssistantType.GAS_STATION);
				// startActivity(gIntent);
				// break;
				case 6:// 驾校服务
					Intent dIntent = new Intent(CarAssistantActivity.this,
							CommonShopListActivity.class);
					dIntent.putExtra("shopType", ShopType.DRIVING_SCHOOL);
					startActivity(dIntent);
					break;
				default:
					break;
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			finishActivity();
			break;

		default:
			break;
		}
	}
}
