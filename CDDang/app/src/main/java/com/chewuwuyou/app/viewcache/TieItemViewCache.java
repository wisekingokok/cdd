package com.chewuwuyou.app.viewcache;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.widget.DrawableCenterTextView;

/**
 * @describe:帖子的viewcache
 * @author:XH
 * @version
 * @created:
 */
public class TieItemViewCache {
	private View mView;
	private ImageView mTieItemAvatarIV;// 图像
	private TextView mTieItemNameTV;// 昵称
	private TextView mTieItemContentTV;// 内容
	private TextView mTieItemDateTV;
	private DrawableCenterTextView mTieItemPingTV;
	private DrawableCenterTextView mTieItemZanTV;
	
	private LinearLayout   mTieBanKuaiLL; //
	private ImageView   mTieBanKuaiIV; //
	private TextView   mTieBanKuaiNumberTV; //
	
	private LinearLayout mTieItemTusLL;
	private LinearLayout mTieItemTusInnerLL;
	private ImageView mTieItemSexIV;
	private ImageView mTieItemEditIV;//tie_item_edit_iv
    private LinearLayout mTieItemDelShareZanPingLL;//tie_item_del_share_zan_ping_ll
	private DrawableCenterTextView mTieItemAreaTV;//tie_item_area_tv
	public TieItemViewCache(View view) {
		this.mView = view;
	}

	public View getmView() {
		return mView;
	}

	
	public ImageView getmTieItemEditIV() {
		if (mTieItemEditIV == null) {
			mTieItemEditIV = (ImageView) mView.findViewById(R.id.tie_item_edit_iv);
		}
		return mTieItemEditIV;
	}

	// 圈图像
	public ImageView getmTieItemAvatarIV() {
		if (mTieItemAvatarIV == null) {
			mTieItemAvatarIV = (ImageView) mView.findViewById(R.id.tie_item_avatar_iv);
		}
		return mTieItemAvatarIV;
	}

	// 圈名字
	public TextView getmTieItemNameTV() {
		if (mTieItemNameTV == null) {
			mTieItemNameTV = (TextView) mView.findViewById(R.id.tie_item_name_tv);
		}
		return mTieItemNameTV;
	}

	// 圈文本
	public TextView getmTieItemContentTV() {
		if (mTieItemContentTV == null) {
			mTieItemContentTV = (TextView) mView.findViewById(R.id.tie_item_content_tv);
		}
		return mTieItemContentTV;
	}

//	// 圈文本更多
//	public TextView getmTieItemContentMoreTV() {
//		if (mTieItemContentMoreTV == null) {
//			mTieItemContentMoreTV = (TextView) mView.findViewById(R.id.tie_item_content_more_tv);
//		}
//		return mTieItemContentMoreTV;
//	}

	// 圈发布时间
	public TextView getmTieItemDateTV() {
		if (mTieItemDateTV == null) {
			mTieItemDateTV = (TextView) mView.findViewById(R.id.tie_item_date_tv);
		}
		return mTieItemDateTV;
	}
	
	// 评价LinearLayout
	public LinearLayout getmTieItemDelShareZanPingLL() {
		if (mTieItemDelShareZanPingLL == null) {
			mTieItemDelShareZanPingLL = (LinearLayout) mView.findViewById(R.id.tie_item_del_share_zan_ping_ll);
		}
		return mTieItemDelShareZanPingLL;
	}


	// 评价数
	public DrawableCenterTextView getmTieItemPingTV() {
		if (mTieItemPingTV == null) {
			mTieItemPingTV = (DrawableCenterTextView) mView.findViewById(R.id.tie_item_ping_tv);
		}
		return mTieItemPingTV;
	}

	// 点赞数
	public DrawableCenterTextView getmTieItemZanTV() {
		if (mTieItemZanTV == null) {
			mTieItemZanTV = (DrawableCenterTextView) mView.findViewById(R.id.tie_item_zan_tv);
		}
		return mTieItemZanTV;
	}

	public LinearLayout getmTieBanKuaiLL() {
		if (mTieBanKuaiLL == null) {
			mTieBanKuaiLL = (LinearLayout) mView.findViewById(R.id.tie_bankuai_ll);
		}
		return mTieBanKuaiLL;
	}

	public ImageView getmTieBanKuaiIV() {
		if (mTieBanKuaiIV == null) {
			mTieBanKuaiIV = (ImageView) mView.findViewById(R.id.tie_bankuai_iv);
		}
		return mTieBanKuaiIV;
	}

	public TextView getmTieBanKuaiNumberTV() {
		if (mTieBanKuaiNumberTV == null) {
			mTieBanKuaiNumberTV = (TextView) mView.findViewById(R.id.tie_bankuai_number_tv);
		}
		return mTieBanKuaiNumberTV;
	}
	
	public LinearLayout getmTieItemTusLL() {
		if (mTieItemTusLL == null) {
			mTieItemTusLL = (LinearLayout) mView.findViewById(R.id.tie_item_tu_ll);
		}
		return mTieItemTusLL;
	}

	public LinearLayout getmTieItemTusInnerLL() {
		if (mTieItemTusInnerLL == null) {
			mTieItemTusInnerLL = (LinearLayout) mView.findViewById(R.id.tie_item_tus_inner_ll);
		}
		return mTieItemTusInnerLL;
	}

	public ImageView getmTieItemSexIV() {
		if (mTieItemSexIV == null) {
			mTieItemSexIV = (ImageView) mView.findViewById(R.id.tie_item_sex_iv);
		}
		return mTieItemSexIV;
	}

	public DrawableCenterTextView getmTieItemAreaTV() {
		if (mTieItemAreaTV == null) {
			mTieItemAreaTV = (DrawableCenterTextView) mView.findViewById(R.id.tie_item_area_tv);
		}
		return mTieItemAreaTV;
	}
	
	
}
