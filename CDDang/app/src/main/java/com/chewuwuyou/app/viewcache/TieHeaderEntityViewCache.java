package com.chewuwuyou.app.viewcache;

import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.widget.DrawableCenterTextView;

public class TieHeaderEntityViewCache {
	private View mView;
	private ImageView mDetailAvatarIV;
	private TextView mDetailNameTV;
	private TextView mDetailContentTV;
//	private MyGridView mDetailTusGV;
	private TextView mDetailDateTV;
	
	private ImageView mDetailEditIV;//tie_detail_edit_iv
	private TextView mDetailZanTV;
	private TextView mDetailPingTV; 
	
	private LinearLayout mDetailZanerLL;// tie_detail_zaner_rl
	private GridView mDetailZanerGV;// tie_detail_zaner_gv
//	private TextView mDetailZanerMoreTV;// tie_detail_zaner_more_tv 点击进入下一页要用
	
	private LinearLayout mTieDetailTusInnerLL;
	private LinearLayout mTieDetailTusLL;
	private TextView mDetailPingTitleTV;// hot_tie_detail_ping_title_tv 点击进入下一页要用
	private DrawableCenterTextView mTieDetailAreaTV;//tie_detail_area_tv
	
	private ImageView mTieDetailSexIV;//tie_detail_sex_iv
    		
	public TieHeaderEntityViewCache(View view) {
		this.mView = view;
	}
	public View getmView() {
		return mView;
	}
	public ImageView getmDetailAvatarIV() {
		if (mDetailAvatarIV == null) {
			mDetailAvatarIV = (ImageView) mView.findViewById(R.id.tie_detail_avatar_iv);
		}
		return mDetailAvatarIV;
	}
	public TextView getmDetailNameTV() {
		if (mDetailNameTV == null) {
			mDetailNameTV = (TextView) mView.findViewById(R.id.tie_detail_name_tv);
		}
		return mDetailNameTV;
	}
	public TextView getmDetailContentTV() {
		if (mDetailContentTV == null) {
			mDetailContentTV = (TextView) mView.findViewById(R.id.tie_detail_content_tv);
		}
		return mDetailContentTV;
	}
//	public TextView getmDetailContentMoreTV() {
//		if (mDetailContentMoreTV == null) {
//			mDetailContentMoreTV = (TextView) mView.findViewById(R.id.tie_detail_content_more_tv);
//		}
//		return mDetailContentMoreTV;
//	}
//	public FrameLayout getmDetailTusFL() {
//		if (mDetailTusFL == null) {
//			mDetailTusFL = (FrameLayout) mView.findViewById(R.id.tie_detail_tus_fl);
//		}
//		return mDetailTusFL;
//	}
	public TextView getmDetailDateTV() {
		if (mDetailDateTV == null) {
			mDetailDateTV = (TextView) mView.findViewById(R.id.tie_detail_date_tv);
		}
		return mDetailDateTV;
	}
	public TextView getmDetailZanTV() {
		if (mDetailZanTV == null) {
			mDetailZanTV = (TextView) mView.findViewById(R.id.tie_detail_zan_tv);
		}
		return mDetailZanTV;
	}
	public TextView getmDetailPingTV() {
		if (mDetailPingTV == null) {
			mDetailPingTV = (TextView) mView.findViewById(R.id.tie_detail_ping_tv);
		}
		return mDetailPingTV;
	}

	public LinearLayout getmDetailZanerLL() {
		if (mDetailZanerLL == null) {
			mDetailZanerLL = (LinearLayout) mView.findViewById(R.id.tie_detail_zaner_ll);
		}
		return mDetailZanerLL;
	}

//	public TextView getmDetailZanerMoreTV() {
//		if (mDetailZanerMoreTV == null) {
//			mDetailZanerMoreTV = (TextView) mView.findViewById(R.id.tie_detail_zaner_more_tv);
//		}
//		return mDetailZanerMoreTV;
//	}
	
	public GridView getmDetailZanerGV() {
		if (mDetailZanerGV == null) {
			mDetailZanerGV = (GridView) mView.findViewById(R.id.tie_detail_zaner_gv);
		}
		return mDetailZanerGV;
	}
	
	public LinearLayout getmTieDetailTusLL() {
		if (mTieDetailTusLL == null) {
			mTieDetailTusLL = (LinearLayout) mView.findViewById(R.id.tie_detail_tus_ll);
		}
		return mTieDetailTusLL;
	}
	
	public TextView getmDetailPingTitleTV() {
		if (mDetailPingTitleTV == null) {
			mDetailPingTitleTV = (TextView) mView.findViewById(R.id.tie_detail_ping_title_tv);
		}
		return mDetailPingTitleTV;
	}
	
	public LinearLayout getmTieDetailTusInnerLL() {
		if (mTieDetailTusInnerLL == null) {
			mTieDetailTusInnerLL = (LinearLayout) mView.findViewById(R.id.tie_detail_tus_inner_ll);
		}
		return mTieDetailTusInnerLL;
	}
	
	public DrawableCenterTextView getmTieDetailAreaTV() {
		if (mTieDetailAreaTV == null) {
			mTieDetailAreaTV = (DrawableCenterTextView) mView.findViewById(R.id.tie_detail_area_tv);
		}
		return mTieDetailAreaTV;
	}
	
	public ImageView getmTieDetailSexIV() {
		if(mTieDetailSexIV == null) {
			mTieDetailSexIV = (ImageView) mView.findViewById(R.id.tie_detail_sex_iv);
		}
		return mTieDetailSexIV;
	}
	
	public ImageView getmDetailEditIV() {
		if(mDetailEditIV == null) {
			mDetailEditIV = (ImageView) mView.findViewById(R.id.tie_detail_edit_iv);
		}
		return mDetailEditIV;
	}

    
	
}
