package com.chewuwuyou.app.ui;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.CarPlateCityAdapter;
import com.chewuwuyou.app.bean.CarCityItem;
import com.chewuwuyou.app.bean.CarProvinceItem;

public class CarPlateCityActivity extends BaseActivity {
	@ViewInject(id = R.id.sub_header_bar_left_ibtn, click = "onAction")
	private ImageButton mBackBtn;
	@ViewInject(id = R.id.sub_header_bar_tv)
	private TextView mHeaderTV;

	@ViewInject(id = R.id.car_plate_city_list)
	private ListView mListView;
	private List<CarCityItem> mData = new ArrayList<CarCityItem>();
	private CarPlateCityAdapter mAdapter;
	private CarProvinceItem province = null;
	private RelativeLayout mTitleHeight;//标题布局高度
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.car_plate_city_ac);
		
		mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
		
		mHeaderTV.setText("选择车牌地-城市");
		province = (CarProvinceItem) getIntent().getSerializableExtra(
				"province");
		mData.addAll(province.getCitys());

		mAdapter = new CarPlateCityAdapter(CarPlateCityActivity.this, mData);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("city_id", mData.get(arg2).getCity_id());
				intent.putExtra("city_name", mData.get(arg2).getCity_name());
				setResult(RESULT_OK, intent);
				finish();
			}
		});

	}

	public void onAction(View v) {
		// Intent intent = new Intent();
		switch (v.getId()) {
		// 退出
		case R.id.sub_header_bar_left_ibtn:
			finish();
			break;
		// 新建一个心情
		case R.id.sub_header_bar_right_ibtn:
			// if (CacheTools.getUserData("userId") != null &&
			// Integer.parseInt(CacheTools.getUserData("userId")) > 0) {
			// intent.setClass(TieActivity.this, AddTieActivity.class);
			// intent.putExtra("banId", mBanId);
			// startActivity(intent);
			// } else {
			// // 跳到登录界面
			// intent = new Intent(TieActivity.this, LoginActivity.class);
			// startActivity(intent);
			// finishActivity();
			// }
			break;
		default:
			break;
		}
	}
}
