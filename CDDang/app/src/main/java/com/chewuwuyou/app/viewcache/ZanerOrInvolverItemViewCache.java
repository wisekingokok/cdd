package com.chewuwuyou.app.viewcache;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chewuwuyou.app.R;

public class ZanerOrInvolverItemViewCache {
	private View mView;

	private ImageView mZanerItemAvatarIV;// zaner_item_avatar_iv
	private TextView mZanerItemNameTV;// zaner_item_name_tv
	private ImageView mZanerItemSexIV;// zaner_item_sex_iv
	private TextView mZanerItemMoodTV;//zaner_item_mood_tv

	public View getmView() {
		return mView;
	}

	public ZanerOrInvolverItemViewCache(View view) {
		this.mView = view;
	}
	
	public ImageView getmZanerItemAvatarIV() {
		if (mZanerItemAvatarIV == null) {
			mZanerItemAvatarIV = (ImageView) mView.findViewById(R.id.zaner_item_avatar_iv);
		}
		return mZanerItemAvatarIV;
	}

	public TextView getmZanerItemNameTV() {
		if (mZanerItemNameTV == null) {
			mZanerItemNameTV = (TextView) mView.findViewById(R.id.zaner_item_name_tv);
		}
		return mZanerItemNameTV;
	}

	public ImageView getmZanerItemSexIV() {
		if (mZanerItemSexIV == null) {
			mZanerItemSexIV = (ImageView) mView.findViewById(R.id.zaner_item_sex_iv);
		}
		return mZanerItemSexIV;
	}

	public TextView getmZanerItemMoodTV() {
		if (mZanerItemMoodTV == null) {
			mZanerItemMoodTV = (TextView) mView.findViewById(R.id.zaner_item_mood_tv);
		}
		return mZanerItemMoodTV;
	}
	
}
