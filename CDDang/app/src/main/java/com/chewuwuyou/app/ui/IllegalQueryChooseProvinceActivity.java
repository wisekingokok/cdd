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
import com.cheshouye.api.client.json.ProvinceInfoJson;
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
public class IllegalQueryChooseProvinceActivity extends CDDBaseActivity
		implements OnClickListener {

	private TextView mTitleTV;
	private ImageButton mBackIBtn;
	private RelativeLayout mTitleHeight;//标题布局高度
	private ListView mProviceLV;
	private ListAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.csy_activity_citys);
		initView();
		initData();
		initEvent();

	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mTitleTV = (TextView) findViewById(R.id.sub_header_bar_tv);
		mBackIBtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		mProviceLV = (ListView) findViewById(R.id.lv_1ist);
	}

	@Override
	protected void initData() {
		mTitleHeight =  (RelativeLayout) findViewById(R.id.title_height);
		isTitle(mTitleHeight);//根据不同手机判断
		mTitleTV.setText("选择查询地-省份");
		mAdapter = new ListAdapter(IllegalQueryChooseProvinceActivity.this,
				getData2());
		mProviceLV.setAdapter(mAdapter);
	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub
		mBackIBtn.setOnClickListener(this);
		mProviceLV.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				TextView txt_name = (TextView) view.findViewById(R.id.txt_name);

				Intent intent = new Intent();
				intent.putExtra("province_name", txt_name.getText());
				intent.putExtra("province_id", txt_name.getTag().toString());

				intent.setClass(IllegalQueryChooseProvinceActivity.this,
						IllegalQueryChooseCityActivity.class);
				startActivityForResult(intent, 20);
			}
		});
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
	 * title:获取省份信息
	 * 
	 * @return
	 */
	private List<ListModel> getData2() {

		List<ListModel> list = new ArrayList<ListModel>();
		List<ProvinceInfoJson> provinceList = new ArrayList<ProvinceInfoJson>();
		provinceList = WeizhangClient.getAllProvince();

		// 开通数量提示
		TextView txtListTip = (TextView) findViewById(R.id.list_tip);
		txtListTip.setText("全国已开通" + provinceList.size() + "个省份, 其它省将陆续开放");

		for (ProvinceInfoJson provinceInfoJson : provinceList) {
			String provinceName = provinceInfoJson.getProvinceName();
			int provinceId = provinceInfoJson.getProvinceId();

			ListModel model = new ListModel();
			model.setTextName(provinceName);
			model.setNameId(provinceId);
			list.add(model);
		}
		return list;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null)
			return;
		Bundle bundle = data.getExtras();
		// 获取城市name
		String cityName = bundle.getString("city_name");
		String cityId = bundle.getString("city_id");

		Intent intent = new Intent();
		intent.putExtra("city_name", cityName);
		intent.putExtra("city_id", cityId);
		setResult(1, intent);
		finish();
	}

}
