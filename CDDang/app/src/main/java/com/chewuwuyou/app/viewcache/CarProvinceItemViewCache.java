package com.chewuwuyou.app.viewcache;

import android.view.View;
import android.widget.TextView;

import com.chewuwuyou.app.R;

public class CarProvinceItemViewCache {
	private View mView;
	private TextView mProvinceShortNameTV;// province_short_name_tv;
	private TextView mProvinceNameTV;// province_name_tv;

	public CarProvinceItemViewCache(View view) {
		this.mView = view;
	}

	public View getmView() {
		return mView;
	}

	public TextView getmProvinceShortNameTV() {
		if (mProvinceShortNameTV == null) {
			mProvinceShortNameTV = (TextView) mView.findViewById(R.id.province_short_name_tv);
		}
		return mProvinceShortNameTV;
	}

	public TextView getmProvinceNameTV() {
		if (mProvinceNameTV == null) {
			mProvinceNameTV = (TextView) mView.findViewById(R.id.province_name_tv);
		}
		return mProvinceNameTV;
	}
}