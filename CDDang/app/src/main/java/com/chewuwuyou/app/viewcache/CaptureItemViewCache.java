package com.chewuwuyou.app.viewcache;



import android.view.View;
import android.widget.ImageView;

import com.chewuwuyou.app.R;

public class CaptureItemViewCache {

	private ImageView mIV;
	private View mView;
	
	public CaptureItemViewCache(View view){
		this.mView = view;
	}

	public ImageView getmIV() {
		if(mIV == null){
			mIV = (ImageView)mView.findViewById(R.id.capture_item_iv);
		}
		return mIV;
	}
	
}