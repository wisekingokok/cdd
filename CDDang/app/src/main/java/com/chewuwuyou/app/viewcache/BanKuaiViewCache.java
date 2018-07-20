package com.chewuwuyou.app.viewcache;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;

/**
 * @describe:车友论坛的viewcache
 * @author:XH
 * @version
 * @created:
 * 
 *           bankuai_count_tv
 * 
 */
public class BanKuaiViewCache {

	private View mView;
	private RelativeLayout mBanKuaiRL;
	private ImageView mBanKuaiPhotoIV;
	private TextView mBanKuaiTitleTV;

	public BanKuaiViewCache(View view) {
		this.mView = view;
	}

	public View getmView() {
		return mView;
	}

	public RelativeLayout getmBanKuaiRL() {
		if (mBanKuaiRL == null) {
			mBanKuaiRL = (RelativeLayout) mView.findViewById(R.id.bankuai_rl);
		}
		return mBanKuaiRL;
	}

	public ImageView getmBanKuaiPhotoIV() {
		if (mBanKuaiPhotoIV == null) {
			mBanKuaiPhotoIV = (ImageView) mView
					.findViewById(R.id.bankuai_photo_iv);
		}
		return mBanKuaiPhotoIV;
	}

	public TextView getmBanKuaiTitleTV() {
		if (mBanKuaiTitleTV == null) {
			mBanKuaiTitleTV = (TextView) mView
					.findViewById(R.id.bankuai_title_tv);
		}
		return mBanKuaiTitleTV;
	}

}
