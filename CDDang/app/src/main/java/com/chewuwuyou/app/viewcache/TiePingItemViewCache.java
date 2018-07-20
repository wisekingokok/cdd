package com.chewuwuyou.app.viewcache;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chewuwuyou.app.R;

/**
 * @describe:论坛动态详情中评论的viewcache
 * @author:XH
 * @version 
 * @created:
 */
public class TiePingItemViewCache {
	private View mView;
	private ImageView mPingAvatarIV;
	private TextView mPingNameTV;
	private TextView mPingContentTV;
	private TextView mPingDateTV;
	private TextView mPingReplyTV;
	
	public TiePingItemViewCache(View view) {
		this.mView = view;
	}

	public View getmView() {
		return mView;
	}
	public ImageView getmPingAvatarIV() {
		if (mPingAvatarIV == null) {
			mPingAvatarIV = (ImageView) mView.findViewById(R.id.ping_avatar_iv);
		}
		return mPingAvatarIV;
	}
	public TextView getmPingNameTV() {
		if (mPingNameTV == null) {
			mPingNameTV = (TextView) mView.findViewById(R.id.ping_name_tv);
		}
		return mPingNameTV;
	}
	public TextView getmPingContentTV() {
		if (mPingContentTV == null) {
			mPingContentTV = (TextView) mView.findViewById(R.id.ping_content_tv);
		}
		return mPingContentTV;
	}

	public TextView getmPingDateTV() {
		if (mPingDateTV == null) {
			mPingDateTV = (TextView) mView.findViewById(R.id.ping_date_tv);
		}
		return mPingDateTV;
	}

	public TextView getmPingReplyTV() {
		if (mPingReplyTV == null) {
			mPingReplyTV = (TextView) mView.findViewById(R.id.ping_reply_tv);
		}
		return mPingReplyTV;
	}
	
}
