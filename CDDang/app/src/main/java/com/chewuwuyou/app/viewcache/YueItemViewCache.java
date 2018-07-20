package com.chewuwuyou.app.viewcache;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chewuwuyou.app.R;

public class YueItemViewCache {
	private View mView;

	private TextView mYueItemHeaderNumTV;//yue_item_header_num_tv
	private ImageView mYueItemInfoIV;// yue_item_info_iv
	private TextView mYueItemTitleTV;// yue_item_title_tv
	private TextView mYueItemChargeTypeTV;// yue_item_charge_type_tv
	private TextView mYueItemInvolveTV;// yue_item_involve_tv

	private TextView mYueItemLocationTV;// yue_item_location_tv
	private TextView mYueItemAvailableDateTV;// yue_item_available_date_tv
	private TextView mYueItemDeleteTV;//yue_item_delete_tv

	public View getmView() {
		return mView;
	}

	public YueItemViewCache(View view) {
		this.mView = view;
	}

	
	public TextView getmYueItemHeaderNumTV() {
		if (mYueItemHeaderNumTV == null) {
			mYueItemHeaderNumTV = (TextView) mView.findViewById(R.id.yue_item_header_num_tv);
		}

		return mYueItemHeaderNumTV;
	}

	public ImageView getmYueItemInfoIV() {
		if (mYueItemInfoIV == null) {
			mYueItemInfoIV = (ImageView) mView.findViewById(R.id.yue_item_info_iv);
		}

		return mYueItemInfoIV;
	}

	public TextView getmYueItemTitleTV() {
		if (mYueItemTitleTV == null) {
			mYueItemTitleTV = (TextView) mView.findViewById(R.id.yue_item_title_tv);
		}

		return mYueItemTitleTV;
	}

	public TextView getmYueItemChargeTypeTV() {
		if (mYueItemChargeTypeTV == null) {
			mYueItemChargeTypeTV = (TextView) mView.findViewById(R.id.yue_item_charge_type_tv);
		}

		return mYueItemChargeTypeTV;
	}

	public TextView getmYueItemInvolveTV() {
		if (mYueItemInvolveTV == null) {
			mYueItemInvolveTV = (TextView) mView.findViewById(R.id.yue_item_involve_tv);
		}

		return mYueItemInvolveTV;
	}

	public TextView getmYueItemLocationTV() {
		if (mYueItemLocationTV == null) {
			mYueItemLocationTV = (TextView) mView.findViewById(R.id.yue_item_location_tv);
		}
		return mYueItemLocationTV;
	}

	public TextView getmYueItemAvailableDateTV() {
		if (mYueItemAvailableDateTV == null) {
			mYueItemAvailableDateTV = (TextView) mView.findViewById(R.id.yue_item_available_date_tv);
		}
		return mYueItemAvailableDateTV;
	}

	public TextView getmYueItemDeleteTV() {
		if (mYueItemDeleteTV == null) {
			mYueItemDeleteTV = (TextView) mView.findViewById(R.id.yue_item_delete_tv);
		}
		return mYueItemDeleteTV;
	}
	
}
