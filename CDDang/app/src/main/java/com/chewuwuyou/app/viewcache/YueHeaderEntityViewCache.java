package com.chewuwuyou.app.viewcache;

import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.widget.MyGridView;

public class YueHeaderEntityViewCache {
	private View mView;

	private ImageView mDetailAvatarIV;// android:id="@+id/yue_detail_avatar_iv"
	private TextView mDetailNameTV;// yue_detail_name_tv
	private TextView mDetailDateTV;// yue_detail_date_tv
	private TextView mDetailTitleTV;// yue_detail_title_tv

	private TextView mDetailChargeTypeTV;// yue_detail_charge_type_tv
	private TextView mDetailChargeTypeInfoTV;// yue_detail_charge_type_info_tv
	
	private ImageView mDetailSexTypeIV;//tie_detail_sex_type_iv
	
	private ImageView mDetailSexIV; // yue_detail_sex_iv
	private TextView mDetailSexInfoTV; // yue_detail_sex_info_tv
	private TextView mDetailValidDateTV; // yue_detail_valid_date_tv

	private LinearLayout mDetailInvolverLL;// yue_detail_involver_ll
	private GridView mDetailInvolverGV;// yue_detail_involver_gv
//	private TextView mDetailInvolverMoreTV;// yue_detail_involver_more_tv
											// 点击进入下一页要用

	private TextView mDetailContentTV;// yue_detail_content_tv
//	private TextView mDetailContentMoreTV;// yue_detail_content_more_tv
//	private FrameLayout mDetailTusFL;// yue_detail_tus_fl

	private ImageView mDetailEditIV;//tie_detail_edit_iv
	private TextView mDetailZanTV;
	private TextView mDetailPingTV; 
	private LinearLayout mDetailZanerLL;// yue_detail_zaner_rl
	private GridView mDetailZanerGV;// yue_detail_zaner_gv
	private TextView mDetailZanerMoreTV;// yue_detail_zaner_more_tv 点击进入下一页要用
	
	private MyGridView mYueDetailTusGV;
	private TextView mDetailPingTitleTV;// yue_detail_ping_title_tv 点击进入下一页要用
	private LinearLayout mYueDetailTusInnerLL;
	private LinearLayout mYueDetailTusLL;
	private TextView deleteTv;

	public YueHeaderEntityViewCache(View view) {
		this.mView = view;
	}

	public View getmView() {
		return mView;
	}
	
	public TextView getDeleteTv(){
		if(deleteTv==null)
			deleteTv=(TextView) mView.findViewById(R.id.yue_detail_tv);
		return deleteTv;
	}

	public ImageView getmDetailAvatarIV() {
		if (mDetailAvatarIV == null) {
			mDetailAvatarIV = (ImageView) mView.findViewById(R.id.yue_detail_avatar_iv);
		}
		return mDetailAvatarIV;
	}

	public TextView getmDetailNameTV() {
		if (mDetailNameTV == null) {
			mDetailNameTV = (TextView) mView.findViewById(R.id.yue_detail_name_tv);
		}
		return mDetailNameTV;
	}

	public TextView getmDetailContentTV() {
		if (mDetailContentTV == null) {
			mDetailContentTV = (TextView) mView.findViewById(R.id.yue_detail_content_tv);
		}
		return mDetailContentTV;
	}

//	public TextView getmDetailContentMoreTV() {
//		if (mDetailContentMoreTV == null) {
//			mDetailContentMoreTV = (TextView) mView.findViewById(R.id.yue_detail_content_more_tv);
//		}
//		return mDetailContentMoreTV;
//	}

//	public FrameLayout getmDetailTusFL() {
//		if (mDetailTusFL == null) {
//			mDetailTusFL = (FrameLayout) mView.findViewById(R.id.yue_detail_tus_fl);
//		}
//		return mDetailTusFL;
//	}

	public TextView getmDetailDateTV() {
		if (mDetailDateTV == null) {
			mDetailDateTV = (TextView) mView.findViewById(R.id.yue_detail_date_tv);
		}
		return mDetailDateTV;
	}

	public TextView getmDetailZanTV() {
		if (mDetailZanTV == null) {
			mDetailZanTV = (TextView) mView.findViewById(R.id.yue_detail_zan_tv);
		}
		return mDetailZanTV;
	}

	public TextView getmDetailPingTV() {
		if (mDetailPingTV == null) {
			mDetailPingTV = (TextView) mView.findViewById(R.id.yue_detail_ping_tv);
		}
		return mDetailPingTV;
	}

	public GridView getmDetailZanerGV() {
		if (mDetailZanerGV == null) {
			mDetailZanerGV = (GridView) mView.findViewById(R.id.yue_detail_zaner_gv);
		}
		return mDetailZanerGV;
	}


	public TextView getmDetailTitleTV() {
		if (mDetailTitleTV == null) {
			mDetailTitleTV = (TextView) mView.findViewById(R.id.yue_detail_title_tv);
		}
		return mDetailTitleTV;
	}

	public TextView getmDetailChargeTypeTV() {
		if (mDetailChargeTypeTV == null) {
			mDetailChargeTypeTV = (TextView) mView.findViewById(R.id.yue_detail_charge_type_tv);
		}
		return mDetailChargeTypeTV;
	}

	public TextView getmDetailChargeTypeInfoTV() {
		if (mDetailChargeTypeInfoTV == null) {
			mDetailChargeTypeInfoTV = (TextView) mView.findViewById(R.id.yue_detail_charge_type_info_tv);
		}
		return mDetailChargeTypeInfoTV;
	}

	public ImageView getmDetailSexIV() {
		if (mDetailSexIV == null) {
			mDetailSexIV = (ImageView) mView.findViewById(R.id.yue_detail_sex_iv);
		}
		return mDetailSexIV;
	}

	public TextView getmDetailSexInfoTV() {
		if (mDetailSexInfoTV == null) {
			mDetailSexInfoTV = (TextView) mView.findViewById(R.id.yue_detail_sex_info_tv);
		}
		return mDetailSexInfoTV;
	}

	public TextView getmDetailValidDateTV() {
		if (mDetailValidDateTV == null) {
			mDetailValidDateTV = (TextView) mView.findViewById(R.id.yue_detail_valid_date_tv);
		}
		return mDetailValidDateTV;
	}

	public LinearLayout getmDetailInvolverLL() {
		if (mDetailInvolverLL == null) {
			mDetailInvolverLL = (LinearLayout) mView.findViewById(R.id.yue_detail_involver_ll);
		}
		return mDetailInvolverLL;
	}

	public GridView getmDetailInvolverGV() {
		if (mDetailInvolverGV == null) {
			mDetailInvolverGV = (GridView) mView.findViewById(R.id.yue_detail_involver_gv);
		}
		return mDetailInvolverGV;
	}

//	public TextView getmDetailInvolverMoreTV() {
//		if (mDetailInvolverMoreTV == null) {
//			mDetailInvolverMoreTV = (TextView) mView.findViewById(R.id.yue_detail_involver_more_tv);
//		}
//		return mDetailInvolverMoreTV;
//	}

	public LinearLayout getmDetailZanerLL() {
		if (mDetailZanerLL == null) {
			mDetailZanerLL = (LinearLayout) mView.findViewById(R.id.yue_detail_zaner_ll);
		}
		return mDetailZanerLL;
	}

	public TextView getmDetailZanerMoreTV() {
		if (mDetailZanerMoreTV == null) {
			mDetailZanerMoreTV = (TextView) mView.findViewById(R.id.yue_detail_zaner_more_tv);
		}
		return mDetailZanerMoreTV;
	}
	

	public MyGridView getmYueDetailTusGV() {
		if (mYueDetailTusGV == null) {
			mYueDetailTusGV = (MyGridView) mView.findViewById(R.id.yue_detail_tus_gv);
		}
		return mYueDetailTusGV;
	}

	public TextView getmDetailPingTitleTV() {
		if (mDetailPingTitleTV == null) {
			mDetailPingTitleTV = (TextView) mView.findViewById(R.id.yue_detail_ping_title_tv);
		}
		return mDetailPingTitleTV;
	}
	
	public ImageView getmDetailSexTypeIV() {
		if (mDetailSexTypeIV == null) {
			mDetailSexTypeIV = (ImageView) mView.findViewById(R.id.yue_detail_sex_type_iv);
		}
		return mDetailSexTypeIV;
	}

	public LinearLayout getmYueDetailTusInnerLL() {
		if (mYueDetailTusInnerLL == null) {
			mYueDetailTusInnerLL = (LinearLayout) mView.findViewById(R.id.yue_detail_tus_inner_ll);
		}
		return mYueDetailTusInnerLL;
	}

	public LinearLayout getmYueDetailTusLL() {
		if (mYueDetailTusLL == null) {
			mYueDetailTusLL = (LinearLayout) mView.findViewById(R.id.yue_detail_tu_ll);
		}
		return mYueDetailTusLL;
	}

	public ImageView getmDetailEditIV() {
		if (mDetailEditIV == null) {
			mDetailEditIV = (ImageView) mView.findViewById(R.id.yue_detail_edit_iv);
		}
		return mDetailEditIV;
	}
	
	
	
}
