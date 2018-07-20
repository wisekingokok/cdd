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
import com.chewuwuyou.app.bean.CarChildBook;
import com.chewuwuyou.app.utils.ImageUtils;


public class CarBrandActivity extends CDDBaseActivity implements OnClickListener{

	private List<CarChildBook> mCarChildBook;
	private CommonAdapter<CarChildBook> mCarChildBookAdapter;
	private ListView mModelList;
	private TextView mSubHeaderHarTv;
    private ImageButton mSubHeaderBarLeftIbtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_cdd_brand);
		

		initView();
		initData();
		initEvent();

	}

	@Override
	protected void initView() {
		mSubHeaderHarTv= (TextView) findViewById(R.id.sub_header_bar_tv);
		mModelList = (ListView) findViewById(R.id.model_list);
		mSubHeaderBarLeftIbtn = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
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
	
	@Override
	protected void initData() {
		mSubHeaderHarTv.setText("新车查价");
		mCarChildBook = new ArrayList<CarChildBook>();
		try {
			JSONArray jsonArray = new JSONArray(getFromAssets("brands.json"));
			for (int i = 0; i < jsonArray.length(); i++) {
				if(getIntent().getStringExtra("brandname").equals((jsonArray.getJSONObject(i).getString("brandName")))){
					for (int j = 0; j < jsonArray.getJSONObject(i).getJSONArray("cars").length(); j++) {
						mCarChildBook.add(new CarChildBook(jsonArray.getJSONObject(i).getJSONArray("cars").getJSONObject(j).getString("name"), 
								jsonArray.getJSONObject(i).getJSONArray("cars").getJSONObject(j).getString("imgName"),
								jsonArray.getJSONObject(i).getJSONArray("cars").getJSONObject(j).getString("price")));
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		mCarChildBookAdapter = new CommonAdapter<CarChildBook>(CarBrandActivity.this, mCarChildBook,R.layout.car_choosechild) {
				
		    @Override
			public void convert(ViewHolder holder, CarChildBook t,int p) {
				holder.setText(R.id.car_name, t.getName());
				holder.setText(R.id.car_maondel, t.getPrice());
				ImageView carImage = holder.getView(R.id.car_img);
				ImageUtils.displayImage(t.getImgName(), carImage,10, R.drawable.a1_audi, R.drawable.a1_audi);
			}
		};
		mModelList.setAdapter(mCarChildBookAdapter);
	}

	@Override
	protected void initEvent() {
		mSubHeaderBarLeftIbtn.setOnClickListener(this);
		mModelList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.putExtra("name", mCarChildBook.get(position).getName());
				intent.putExtra("imgName", mCarChildBook.get(position).getImgName());
				intent.putExtra("price", mCarChildBook.get(position).getPrice());
				setResult(RESULT_OK, intent);
				finishActivity();
			}
		});
	}

	
}
