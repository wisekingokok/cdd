package com.chewuwuyou.app.viewcache;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chewuwuyou.app.R;

public class ContacterExpandChildViewCache {
	private View mView;
	private TextView mMoodTV;
	private TextView mUserNameTV;
	private ImageView mImageIV;

	public ContacterExpandChildViewCache(View view) {
		this.mView = view;
	}

	public View getmView() {
		return mView;
	}

	public TextView getmMoodTV() {
		if (mMoodTV == null) {
			mMoodTV = (TextView) mView.findViewById(R.id.mood);
		}
		return mMoodTV;
	}

	public TextView getmUserNameTV() {
		if (mUserNameTV == null) {
			mUserNameTV = (TextView) mView.findViewById(R.id.username);
		}
		return mUserNameTV;
	}

	public ImageView getmImageIV() {
		if (mImageIV == null) {
			mImageIV = (ImageView) mView.findViewById(R.id.child_item_head);
		}
		return mImageIV;
	}


}
