package com.chewuwuyou.app.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.CommonAdapter;
import com.chewuwuyou.app.adapter.ViewHolder;
import com.chewuwuyou.app.bean.CarBrandBook;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.eim.comm.Constant;

/**
 * @describe:选择品牌和车型
 * @author:yuyong
 * @version 1.1.0
 * @created:2014-10-28上午9:48:34
 */
public class ChooseCarBrandActivity extends CDDBaseActivity implements
		OnClickListener {

	private List<CarBrandBook> mCarBrandBook;// 尺品牌几何
	private ListView mBrandDisplay;// 车品牌列表
	private CommonAdapter<CarBrandBook> mAdapter;// 品牌适配器
	private TextView mSubHeaderHarTv;// 标题
	private String name, imgName, price, brandName, brandImage;
	private ImageButton mSubHeaderBarLeftIbtn;// 返回上一页
	private int type;// 接收传递过来的值

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_brand_ac);

		initView();
		initData();
		initEvent();

	}

	/**
	 * 初始化控件
	 */
	@Override
	protected void initView() {
		mBrandDisplay = (ListView) findViewById(R.id.brand_display);
		mSubHeaderHarTv = (TextView) findViewById(R.id.sub_header_bar_tv);
		mSubHeaderHarTv.setText("新车查价");
		mSubHeaderBarLeftIbtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		type = getIntent().getIntExtra("type", 0);
	}

	/**
	 * 逻辑处理
	 */
	@Override
	protected void initData() {
		try {
			mCarBrandBook = new ArrayList<CarBrandBook>();
			JSONArray jsonArray = new JSONArray(getFromAssets("brands.json"));
			for (int i = 0; i < jsonArray.length(); i++) {
				mCarBrandBook.add(new CarBrandBook(jsonArray.getJSONObject(i)
						.getString("brandName"), jsonArray.getJSONObject(i)
						.getString("brandImage")));
			}
			mAdapter = new CommonAdapter<CarBrandBook>(
					ChooseCarBrandActivity.this, mCarBrandBook,
					R.layout.car_choose) {

				@Override
				public void convert(ViewHolder holder, CarBrandBook t,int p) {
					holder.setText(R.id.car_name, t.getBrandName());
					ImageView carImage = holder.getView(R.id.car_img);
					ImageUtils.displayImage(t.getBrandImage(), carImage, 10,
							R.drawable.a1_audi, R.drawable.a1_audi);
				}
			};
			mBrandDisplay.setAdapter(mAdapter);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 事件监听
	 */
	@Override
	protected void initEvent() {
		mSubHeaderBarLeftIbtn.setOnClickListener(this);
		mBrandDisplay.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				brandName = mCarBrandBook.get(position).getBrandName();
				brandImage = mCarBrandBook.get(position).getBrandImage();
				Intent intent = new Intent(ChooseCarBrandActivity.this,
						CarBrandActivity.class);
				intent.putExtra("brandname", mCarBrandBook.get(position)
						.getBrandName());
				startActivityForResult(intent, 20);

			}

		});

	}

	/**
	 * 点击事件
	 */
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

	/**
	 * 回调方法
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 20) {
			if (data != null) {

				name = data.getStringExtra("name");
				imgName = data.getStringExtra("imgName");
				price = data.getStringExtra("price");
				Constant.CAR_NAME = name;
				Constant.CAR_COORD = imgName;
				Intent intent = new Intent();
				intent.putExtra("name", name);
				intent.putExtra("imgName", imgName);
				intent.putExtra("price", price);
				intent.putExtra("brandName", brandName);
				intent.putExtra("brandImage", brandImage);
				setResult(RESULT_OK, intent);
				if (type != 1) {
					finishActivity();
				}
			}

		}
	}
}
