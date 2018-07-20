package com.chewuwuyou.app.viewcache;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;

/**
 * @describe:帖子的viewcache
 * @author:XH
 * @version
 * @created:
 */
public class DINGTieItemViewCache {
	private View mView;
	private TextView mDINGTieItemTitleTV;// 昵称
	private LinearLayout   mTieBanKuaiLL; //
	private ImageView   mTieBanKuaiIV; //
	private TextView   mTieBanKuaiNumberTV; //

	public DINGTieItemViewCache(View view) {
		this.mView = view;
	}

	public View getmView() {
		return mView;
	}

	// 圈名字
	public TextView getmDINGTieItemTitleTV() {
		if (mDINGTieItemTitleTV == null) {
			mDINGTieItemTitleTV = (TextView) mView.findViewById(R.id.DING_tie_item_title_tv);
		}
		return mDINGTieItemTitleTV;
	}

	public LinearLayout getmTieBanKuaiLL() {
		if (mTieBanKuaiLL == null) {
			mTieBanKuaiLL = (LinearLayout) mView.findViewById(R.id.DING_tie_bankuai_ll);
		}
		return mTieBanKuaiLL;
	}

	public ImageView getmTieBanKuaiIV() {
		if (mTieBanKuaiIV == null) {
			mTieBanKuaiIV = (ImageView) mView.findViewById(R.id.DING_tie_bankuai_iv);
		}
		return mTieBanKuaiIV;
	}

	public TextView getmTieBanKuaiNumberTV() {
		if (mTieBanKuaiNumberTV == null) {
			mTieBanKuaiNumberTV = (TextView) mView.findViewById(R.id.DING_tie_bankuai_number_tv);
		}
		return mTieBanKuaiNumberTV;
	}
}

