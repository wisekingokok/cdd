package com.chewuwuyou.app.ui;

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
import com.chewuwuyou.app.adapter.CarPlateProvinceAdapter;
import com.chewuwuyou.app.bean.CarProvinceItem;

public class CarPlateProvinceActivity extends BaseActivity {
	@ViewInject(id = R.id.sub_header_bar_left_ibtn, click = "onAction")
	private ImageButton mBackBtn;
	@ViewInject(id = R.id.sub_header_bar_tv)
	private TextView mHeaderTV;

	@ViewInject(id = R.id.car_plate_province_list)
	private ListView mListView;
	private List<CarProvinceItem> mData;
	private CarPlateProvinceAdapter mAdapter;
	public static final int RESULT_FROM_CHOOSE_CITY = 1;
	private RelativeLayout mTitleHeight;//标题布局高度
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.car_plate_province_ac);
		
		mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
		
		mHeaderTV.setText("选择车牌地-省份");
		mData = CarProvinceItem.parseList(getFromAssets("get_all_configs.json"));
		
		mAdapter = new CarPlateProvinceAdapter(CarPlateProvinceActivity.this, mData);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				if(mData.get(arg2).getCitys() != null && !mData.get(arg2).getCitys().isEmpty()) {
					Intent intent = new Intent(CarPlateProvinceActivity.this, CarPlateCityActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("province", mData.get(arg2));
					intent.putExtras(bundle);
					startActivityForResult(intent, RESULT_FROM_CHOOSE_CITY);
				} else {
//                    setResult(RESULT_OK, data);
//                    finishActivity();
				}
			}
		});
	}

	public void onAction(View v) {
		switch (v.getId()) {
		// 退出
		case R.id.sub_header_bar_left_ibtn:
			finishActivity();
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case RESULT_FROM_CHOOSE_CITY:
			if (resultCode == RESULT_OK) {
				if (data != null) {
                    setResult(RESULT_OK, data);
                    finishActivity();
				}
			}
			break;
		default:
			break;
		}
	}
}
