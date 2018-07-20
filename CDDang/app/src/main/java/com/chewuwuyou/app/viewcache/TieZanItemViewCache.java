package com.chewuwuyou.app.viewcache;

import android.view.View;
import android.widget.ImageView;

import com.chewuwuyou.app.R;

public class TieZanItemViewCache {

	private View mView;
	private ImageView avatarIV;
	
	public TieZanItemViewCache(View view) {
		this.mView = view;
	}
	public View getmView() {
		return mView;
	}
	public ImageView getAvatarIV() {
		if (avatarIV == null) {
			avatarIV = (ImageView) mView.findViewById(R.id.tie_zan_avatar_iv);
		}
		return avatarIV;
	}
	
}
