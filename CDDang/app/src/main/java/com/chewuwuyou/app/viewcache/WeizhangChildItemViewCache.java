package com.chewuwuyou.app.viewcache;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.chewuwuyou.app.R;

public class WeizhangChildItemViewCache {
	private View mView;
	private TextView mWeizhangDateTV;
	private TextView mWeizhangInfoTV;
	private TextView mWeizhangFakuanNumTV;
	private TextView mWeizhangKoufenNumTV;
	private TextView mWeizhangMoreTV;
	private TextView mWeizhangLocationTV;//
	private CheckBox mChooseServiceCB;

	public WeizhangChildItemViewCache(View view) {
		this.mView = view;
	}

	public View getmView() {
		return mView;
	}

	/*
	 * public ImageView getmDetailAvatarIV() { if (mDetailAvatarIV == null) {
	 * mDetailAvatarIV = (ImageView)
	 * mView.findViewById(R.id.hot_tie_detail_avatar_iv); } return
	 * mDetailAvatarIV; }
	 */

	public TextView getmWeizhangDateTV() {
		if (mWeizhangDateTV == null) {
			mWeizhangDateTV = (TextView) mView
					.findViewById(R.id.weizhang_date_tv);
		}
		return mWeizhangDateTV;
	}

	public TextView getmWeizhangInfoTV() {
		if (mWeizhangInfoTV == null) {
			mWeizhangInfoTV = (TextView) mView
					.findViewById(R.id.weizhang_info_tv);
		}
		return mWeizhangInfoTV;
	}

	public TextView getmWeizhangFakuanNumTV() {
		if (mWeizhangFakuanNumTV == null) {
			mWeizhangFakuanNumTV = (TextView) mView
					.findViewById(R.id.weizhang_fakuan_num_tv);
		}
		return mWeizhangFakuanNumTV;
	}

	public TextView getmWeizhangKoufenNumTV() {
		if (mWeizhangKoufenNumTV == null) {
			mWeizhangKoufenNumTV = (TextView) mView
					.findViewById(R.id.weizhang_koufen_num_tv);
		}
		return mWeizhangKoufenNumTV;
	}

	public TextView getmWeizhangMoreTV() {
		if (mWeizhangMoreTV == null) {
			mWeizhangMoreTV = (TextView) mView
					.findViewById(R.id.weizhang_more_tv);
		}
		return mWeizhangMoreTV;
	}

	public TextView getmWeizhangLocationTV() {
		if (mWeizhangLocationTV == null) {
			mWeizhangLocationTV = (TextView) mView
					.findViewById(R.id.weizhang_location_tv);
		}
		return mWeizhangLocationTV;
	}

	public CheckBox getmChooseServiceCB() {
		if(mChooseServiceCB==null){
			mChooseServiceCB=(CheckBox) mView.findViewById(R.id.choose_service_cb);
		}
		return mChooseServiceCB;
	}

}
