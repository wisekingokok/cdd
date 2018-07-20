package com.chewuwuyou.app.viewcache;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chewuwuyou.app.R;

public class WeizhangGroupItemViewCache {
	private View mView;
	private TextView mWeizhangGroupCityTV; // weizhang_group_city_tv
	private TextView mWeizhangGroupInfoTV;// weizhang_group_info_tv
	private ImageButton mWeizhangGroupRefreshIBtn;// weizhang_group_refresh_ibtn

	public WeizhangGroupItemViewCache(View view) {
		this.mView = view;
	}

	public View getmView() {
		return mView;
	}

	public TextView getmWeizhangGroupCityTV() {

		if (mWeizhangGroupCityTV == null) {
			mWeizhangGroupCityTV = (TextView) mView.findViewById(R.id.weizhang_group_city_tv);
		}
		return mWeizhangGroupCityTV;
	}

	public TextView getmWeizhangGroupInfoTV() {
		if (mWeizhangGroupInfoTV == null) {
			mWeizhangGroupInfoTV = (TextView) mView.findViewById(R.id.weizhang_group_info_tv);
		}
		return mWeizhangGroupInfoTV;
	}

	public ImageButton getmWeizhangGroupRefreshIBtn() {

		if (mWeizhangGroupRefreshIBtn == null) {
			//mWeizhangGroupRefreshIBtn = (ImageButton) mView.findViewById(R.id.weizhang_group_refresh_ibtn);
		}
		return mWeizhangGroupRefreshIBtn;
	}

}
