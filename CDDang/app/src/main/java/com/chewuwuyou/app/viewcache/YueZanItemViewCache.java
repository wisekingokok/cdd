package com.chewuwuyou.app.viewcache;

import android.view.View;
import android.widget.ImageView;

import com.chewuwuyou.app.R;

public class YueZanItemViewCache {
	private View mView;
	private ImageView avatarIV;
	public YueZanItemViewCache(View view) {
		this.mView = view;
	}
	public View getmView() {
		return mView;
	}
	public ImageView getAvatarIV() {
		if (avatarIV == null) {
			avatarIV = (ImageView) mView.findViewById(R.id.yue_zan_avatar_iv);
		}
		return avatarIV;
	}
	
}
