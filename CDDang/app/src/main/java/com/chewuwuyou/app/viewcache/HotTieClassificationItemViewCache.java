package com.chewuwuyou.app.viewcache;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;

public class HotTieClassificationItemViewCache {
	private View mView;
	private RelativeLayout mClassificationBgTuRL;// classification_bg_tu_rl
	private ImageView mClassificationBgTuIV;// classification_bg_tu_iv
	private ImageView mClassificationTuIV;// classification_tu_iv
	private TextView mClassificationNameTV;// classification_name_tv
	private TextView mClassificationTodayCntTV;// classification_today_cnt_tv
	private TextView classificationTodayCntTitleTv;

	public HotTieClassificationItemViewCache(View view) {
		this.mView = view;
	}

	public View getmView() {
		return mView;
	}

	public TextView getClassificationTodayCntTitleTv() {
		if (classificationTodayCntTitleTv == null) {
			classificationTodayCntTitleTv = (TextView) mView.findViewById(R.id.classification_today_cnt_title_tv);
		}
		return classificationTodayCntTitleTv;
	}

	public RelativeLayout getmClassificationBgTuRL() {
		if (mClassificationBgTuRL == null) {
			mClassificationBgTuRL = (RelativeLayout) mView.findViewById(R.id.classification_bg_tu_rl);
		}
		return mClassificationBgTuRL;
	}

	public ImageView getmClassificationBgTuIV() {
		if (mClassificationBgTuIV == null) {
			mClassificationBgTuIV = (ImageView) mView.findViewById(R.id.classification_bg_tu_iv);
		}
		return mClassificationBgTuIV;
	}

	public ImageView getmClassificationTuIV() {
		if (mClassificationTuIV == null) {
			mClassificationTuIV = (ImageView) mView.findViewById(R.id.classification_tu_iv);
		}
		return mClassificationTuIV;
	}

	public TextView getmClassificationNameTV() {
		if (mClassificationNameTV == null) {
			mClassificationNameTV = (TextView) mView.findViewById(R.id.classification_name_tv);
		}
		return mClassificationNameTV;
	}

	public TextView getmClassificationTodayCntTV() {
		if (mClassificationTodayCntTV == null) {
			mClassificationTodayCntTV = (TextView) mView.findViewById(R.id.classification_today_cnt_tv);
		}
		return mClassificationTodayCntTV;
	}

}
