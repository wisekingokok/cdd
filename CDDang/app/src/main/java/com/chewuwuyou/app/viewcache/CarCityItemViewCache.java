package com.chewuwuyou.app.viewcache;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chewuwuyou.app.R;

public class CarCityItemViewCache {
	/*
	 * 	private int  engineno;// 2,
	private int  city_id;// 2,
	private String city_name;// "豫"
	private int classno;// "豫"
	private String car_head;// "豫"
	 */
	private View mView;
	private TextView mCityNameTV;// city_name;
	private TextView mCityHeadTV;// car_head;
	private ImageView mCityChoosedIV;

	public CarCityItemViewCache(View view) {
		this.mView = view;
	}

	public View getmView() {
		return mView;
	}

	public TextView getmCityNameTV() {
		if (mCityNameTV == null) {
			mCityNameTV = (TextView) mView.findViewById(R.id.city_name_tv);
		}
		return mCityNameTV;
	}

	public TextView getmCityHeadTV() {
		if (mCityHeadTV == null) {
			mCityHeadTV = (TextView) mView.findViewById(R.id.city_head_tv);
		}
		return mCityHeadTV;
	}

	public ImageView getmCityChoosedIV() {
		if (mCityChoosedIV == null) {
			mCityChoosedIV = (ImageView) mView.findViewById(R.id.car_choosed_iv);
		}
		return mCityChoosedIV;
	}
	
	
}
