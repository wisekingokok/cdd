package com.chewuwuyou.app.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cheshouye.api.client.WeizhangClient;
import com.cheshouye.api.client.json.CityInfoJson;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.adapter.ListAdapter;
import com.chewuwuyou.app.bean.ListModel;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:违章选择城市
 * @author:yuyong
 * @date:2015-12-31上午11:15:20
 * @version:1.2.1
 */
public class IllegalQueryChooseCityActivity extends CDDBaseActivity implements
		OnClickListener {

	private TextView mTitleTV;
	private ImageButton mBackIBtn;
	private RelativeLayout mTitleHeight;//标题布局高度
	private ListView mCityLV;
	private ListAdapter mAdapter;
	private String mProvinceName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.csy_activity_citys);
		initView();
		initEvent();
		initData();

	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
		mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mCityLV = (ListView) findViewById(R.id.lv_1ist);
	}

	@Override
	protected void initData() {
		mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
		mTitleTV.setText("选择查询地-城市");
		Bundle bundle = getIntent().getExtras();
		mProvinceName = bundle.getString("province_name");
		final String provinceId = bundle.getString("province_id");
		mAdapter = new ListAdapter(IllegalQueryChooseCityActivity.this,
				getData(provinceId));
		mCityLV.setAdapter(mAdapter);

		mCityLV.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				TextView txt_name = (TextView) view.findViewById(R.id.txt_name);

				Intent intent = new Intent();
				// 设置cityName
				intent.putExtra("city_name", txt_name.getText());
				// 设置cityId
				intent.putExtra("city_id", txt_name.getTag().toString());
				setResult(20, intent);
				finish();
			}
		});
	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub
		mBackIBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			finishActivity();
			break;

		default:
			break;
		}
	}

	/**
	 * title:获取数据
	 * 
	 * @param provinceId
	 * @return
	 */
	private List<ListModel> getData(String provinceId) {
		List<ListModel> list = new ArrayList<ListModel>();

		List<CityInfoJson> cityList = WeizhangClient.getCitys(Integer
				.parseInt(provinceId));

		// 开通数量提示
		TextView txtListTip = (TextView) findViewById(R.id.list_tip);
		txtListTip.setText(mProvinceName + "已开通" + cityList.size()
				+ "个城市, 其它城市将陆续开放");

		for (CityInfoJson cityInfoJson : cityList) {
			String cityName = cityInfoJson.getCity_name();
			int cityId = cityInfoJson.getCity_id();

			ListModel model = new ListModel();
			model.setNameId(cityId);
			model.setTextName(cityName);
			list.add(model);
		}

		return list;
	}
}
