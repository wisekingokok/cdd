package com.chewuwuyou.app.viewcache;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;

public class QuanThumbnailItemViewCache {
	private View mView;
	private LinearLayout mQuanThumbnailTusFL;// quan_thumbnail_tus_fl
	private TextView mQuanThumbnailContentTV;// quan_thumbnail_content_tv
	private TextView mQuanThumbnailTusSizeTV;// quan_thumbnail_tus_size_tv

	private RelativeLayout mQuanThumbnailBgRL;// 图像quan_thumbnail_bg_rl
	private ImageView mQuanThumbnailBgIV;// 图像quan_thumbnail_bg_iv
	private ImageView mQuanThumbnailBgAvatarIV;// 图像quan_thumbnail_bg_avatar_iv
	private TextView mQuanThumbnailDeleteTV;

	public QuanThumbnailItemViewCache(View view) {
		this.mView = view;
	}

	public View getmView() {
		return mView;
	}

	public LinearLayout getmQuanThumbnailTusFL() {
		if (mQuanThumbnailTusFL == null) {
			mQuanThumbnailTusFL = (LinearLayout) mView.findViewById(R.id.quan_thumbnail_tus_fl);
		}
		return mQuanThumbnailTusFL;
	}

	public TextView getmQuanThumbnailContentTV() {
		if (mQuanThumbnailContentTV == null) {
			mQuanThumbnailContentTV = (TextView) mView.findViewById(R.id.quan_thumbnail_content_tv);
		}
		return mQuanThumbnailContentTV;
	}

	public TextView getmQuanThumbnailTusSizeTV() {
		if (mQuanThumbnailTusSizeTV == null) {
			mQuanThumbnailTusSizeTV = (TextView) mView.findViewById(R.id.quan_thumbnail_tus_size_tv);
		}
		return mQuanThumbnailTusSizeTV;
	}

	public RelativeLayout getmQuanThumbnailBgRL() {
		if (mQuanThumbnailBgRL == null) {
			mQuanThumbnailBgRL = (RelativeLayout) mView.findViewById(R.id.quan_thumbnail_bg_rl);
		}
		return mQuanThumbnailBgRL;
	}

	public ImageView getmQuanThumbnailBgIV() {
		if (mQuanThumbnailBgIV == null) {
			mQuanThumbnailBgIV = (ImageView) mView.findViewById(R.id.quan_thumbnail_bg_iv);
		}
		return mQuanThumbnailBgIV;
	}

	public ImageView getmQuanThumbnailBgAvatarIV() {
		if (mQuanThumbnailBgAvatarIV == null) {
			mQuanThumbnailBgAvatarIV = (ImageView) mView.findViewById(R.id.quan_thumbnail_bg_avatar_iv);
		}
		return mQuanThumbnailBgAvatarIV;
	}

	public TextView getmQuanThumbnailDeleteTV() {
		if (mQuanThumbnailDeleteTV == null) {
			mQuanThumbnailDeleteTV = (TextView) mView.findViewById(R.id.quan_thumbnail_delete_tv);
		}
		return mQuanThumbnailDeleteTV;
	}

}
