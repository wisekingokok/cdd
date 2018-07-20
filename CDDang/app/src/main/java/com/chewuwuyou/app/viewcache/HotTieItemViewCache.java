package com.chewuwuyou.app.viewcache;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.widget.DrawableCenterTextView;

/**
 * @describe:帖子的viewcache
 * @author:XH
 * @version
 * @created:
 */
public class HotTieItemViewCache {
	private View mView;
	private ImageView mHotTieItemAvatarIV;// 图像
	private TextView mHotTieItemNameTV;// 昵称
	private ImageView mHotTieItemSexIV;// hot_tie_item_sex_iv
	private TextView mHotTieItemContentTV;// 内容
	private TextView mHotTieItemDateTV;
	private DrawableCenterTextView mHotTieItemAreaTV;
	private DrawableCenterTextView mHotTieItemPingTV;
	private DrawableCenterTextView mHotTieItemZanTV;
	private RelativeLayout mHotTieItemTuRL;// hot_tie_item_tu_rl
	private ImageView mHotTieItemTuIV;// hot_tie_item_tu_iv
	private TextView mHotTieItemTuInfoTV;// hot_tie_item_tu_info_tv

	private ImageView mHotTieItemEditIV;// tie_item_edit_iv

	public HotTieItemViewCache(View view) {
		this.mView = view;
	}

	public View getmView() {
		return mView;
	}

	// 圈图像
	public ImageView getmHotTieItemAvatarIV() {
		if (mHotTieItemAvatarIV == null) {
			mHotTieItemAvatarIV = (ImageView) mView.findViewById(R.id.hot_tie_item_avatar_iv);
		}
		return mHotTieItemAvatarIV;
	}

	public ImageView getmHotTieItemSexIV() {
		if (mHotTieItemSexIV == null) {
			mHotTieItemSexIV = (ImageView) mView.findViewById(R.id.hot_tie_item_sex_iv);
		}
		return mHotTieItemSexIV;
	}

	// 圈名字
	public TextView getmHotTieItemNameTV() {
		if (mHotTieItemNameTV == null) {
			mHotTieItemNameTV = (TextView) mView.findViewById(R.id.hot_tie_item_name_tv);
		}
		return mHotTieItemNameTV;
	}

	// 圈文本
	public TextView getmHotTieItemContentTV() {
		if (mHotTieItemContentTV == null) {
			mHotTieItemContentTV = (TextView) mView.findViewById(R.id.hot_tie_item_content_tv);
		}
		return mHotTieItemContentTV;
	}

	public RelativeLayout getmHotTieItemTuRL() {
		if (mHotTieItemTuRL == null) {
			mHotTieItemTuRL = (RelativeLayout) mView.findViewById(R.id.hot_tie_item_tu_rl);
		}
		return mHotTieItemTuRL;
	}

	// 圈发布时间
	public TextView getmHotTieItemDateTV() {
		if (mHotTieItemDateTV == null) {
			mHotTieItemDateTV = (TextView) mView.findViewById(R.id.hot_tie_item_date_tv);
		}
		return mHotTieItemDateTV;
	}

	// 评价数
	public DrawableCenterTextView getmHotTieItemPingTV() {
		if (mHotTieItemPingTV == null) {
			mHotTieItemPingTV = (DrawableCenterTextView) mView.findViewById(R.id.hot_tie_item_ping_tv);
		}
		return mHotTieItemPingTV;
	}

	// 点赞数
	public DrawableCenterTextView getmHotTieItemZanTV() {
		if (mHotTieItemZanTV == null) {
			mHotTieItemZanTV = (DrawableCenterTextView) mView.findViewById(R.id.hot_tie_item_zan_tv);
		}
		return mHotTieItemZanTV;
	}

	public ImageView getmHotTieItemTuIV() {
		if (mHotTieItemTuIV == null) {
			mHotTieItemTuIV = (ImageView) mView.findViewById(R.id.hot_tie_item_tu_iv);
		}
		return mHotTieItemTuIV;
	}

	public TextView getmHotTieItemTuInfoTV() {
		if (mHotTieItemTuInfoTV == null) {
			mHotTieItemTuInfoTV = (TextView) mView.findViewById(R.id.hot_tie_item_tu_info_tv);
		}
		return mHotTieItemTuInfoTV;
	}

	public DrawableCenterTextView getmHotTieItemAreaTV() {
		if (mHotTieItemAreaTV == null) {
			mHotTieItemAreaTV = (DrawableCenterTextView) mView.findViewById(R.id.hot_tie_item_area_tv);
		}
		return mHotTieItemAreaTV;
	}

	public ImageView getmHotTieItemEditIV() {
		if (mHotTieItemEditIV == null) {
			mHotTieItemEditIV = (ImageView) mView.findViewById(R.id.hot_tie_item_edit_iv);
		}
		return mHotTieItemEditIV;
	}

}
