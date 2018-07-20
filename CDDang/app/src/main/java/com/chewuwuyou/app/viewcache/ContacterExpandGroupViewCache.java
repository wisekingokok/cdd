package com.chewuwuyou.app.viewcache;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chewuwuyou.app.R;

public class ContacterExpandGroupViewCache {
	private View mView;
	private TextView mOnLineNoTV;
	private TextView mGroupNameTV;
	private ImageView mIsTurnOffIV;

	public ContacterExpandGroupViewCache(View view) {
		this.mView = view;
	}

	public View getmView() {
		return mView;
	}

	public TextView getmOnLineNoTV() {
		if (mOnLineNoTV == null) {
			mOnLineNoTV = (TextView) mView.findViewById(R.id.onlineno);
		}
		return mOnLineNoTV;
	}

	public TextView getmGroupNameTV() {
		if (mGroupNameTV == null) {
			mGroupNameTV = (TextView) mView.findViewById(R.id.groupname);
		}

		return mGroupNameTV;
	}

	public ImageView getmIsTurnOffIV() {
		if (mIsTurnOffIV == null) {
			mIsTurnOffIV = (ImageView) mView.findViewById(R.id.is_turn_off_iv);
		}

		return mIsTurnOffIV;
	}

}
