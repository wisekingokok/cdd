package com.chewuwuyou.app.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.QuanItem;
import com.chewuwuyou.app.bean.QuanTuItem;
import com.chewuwuyou.app.ui.MyQuanFragment;
import com.chewuwuyou.app.ui.PersonalHomeActivity2;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.ImageUtils.ScalingLogic;
import com.chewuwuyou.app.viewcache.QuanThumbnailItemViewCache;

@SuppressLint("NewApi")
public class QuanThumbnailAdapter extends SNSAdapter implements OnClickListener {
	private List<QuanItem> mData;
	// private int mFLWidth = 0;
	// private int mFLHeight = 0;
	// private int mWidth = 0;
	// private int mHeight = 0;
	private String mBackImg;
	private String mHeadImg;
	private String mAction;
	private int mOtherId = 0;
	private Handler mHandler;

	public QuanThumbnailAdapter(Activity context, List<QuanItem> data, String backImg, String headImg, int otherId) {
		super(context);
		this.mData = data;
		this.mBackImg = backImg;
		this.mHeadImg = headImg;
		this.mOtherId = otherId;
		DisplayMetrics dm = new DisplayMetrics();
		// 获取屏幕信息
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		mTuWidth = dm.widthPixels / 5;
	}

	public QuanThumbnailAdapter(Activity context, Handler handler, List<QuanItem> data, String backImg, String headImg,
			int otherId, String action) {
		super(context);
		this.mData = data;
		this.mHandler = handler;
		this.mAction = action;
		this.mBackImg = backImg;
		this.mHeadImg = headImg;
		this.mOtherId = otherId;
		DisplayMetrics dm = new DisplayMetrics();
		// 获取屏幕信息
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		mTuWidth = dm.widthPixels / 5;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		QuanThumbnailItemViewCache viewCache = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.quan_thumbnail_item, null);
			viewCache = new QuanThumbnailItemViewCache(convertView);
			convertView.setTag(viewCache);
		} else {
			viewCache = (QuanThumbnailItemViewCache) convertView.getTag();
		}
		final Integer position_integer = Integer.valueOf(position);
		if (position == 0) {
			viewCache.getmQuanThumbnailBgRL().setVisibility(View.VISIBLE);
			viewCache.getmQuanThumbnailBgRL().setOnClickListener(this);

			if (mBackImg != null) {
				ImageUtils.displayImage(mContext, mBackImg, viewCache.getmQuanThumbnailBgIV(), 0,
						mOutMetrics.widthPixels, mOutMetrics.widthPixels, ScalingLogic.CROP, R.drawable.bg_defaultbg,
						R.drawable.bg_defaultbg);
			}
			if (!TextUtils.isEmpty(mHeadImg)) {
				// ImageUtils.displayImage(mHeadImg,
				// viewCache.getmQuanThumbnailBgAvatarIV(), 360,
				// mContext.getResources().getDimensionPixelSize(R.dimen.quan_big_avatar_width),
				// mContext.getResources().getDimensionPixelSize(R.dimen.quan_big_avatar_height),
				// ScalingLogic.CROP);
				ImageUtils.displayImage(mContext, mHeadImg, viewCache.getmQuanThumbnailBgAvatarIV(), 0,
						mContext.getResources().getDimensionPixelSize(R.dimen.quan_big_avatar_width),
						mContext.getResources().getDimensionPixelSize(R.dimen.quan_big_avatar_height),
						ScalingLogic.CROP, R.drawable.image_default, R.drawable.image_default);
			} else {
				viewCache.getmQuanThumbnailBgAvatarIV().setImageResource(R.drawable.user_fang_icon);
			}
			viewCache.getmQuanThumbnailBgAvatarIV().setOnClickListener(this);

		} else {
			viewCache.getmQuanThumbnailBgRL().setVisibility(View.GONE);
		}
		final QuanItem item = mData.get(position);

		viewCache.getmQuanThumbnailTusFL().removeAllViews();

		List<QuanTuItem> tus = item.getTus();
		viewCache.getmQuanThumbnailTusFL().setVisibility(tus.size() > 0 ? View.VISIBLE : View.GONE);
		int tuSize = tus.size() > 4 ? 4 : tus.size();
		for (int i = 0; i < tuSize; i++) {
			ImageView tuIV = new ImageView(mContext);
			tuIV.setAdjustViewBounds(true);
			tuIV.setScaleType(ScaleType.FIT_XY);
			LinearLayout.LayoutParams iv_params = new LinearLayout.LayoutParams(new LayoutParams(mTuWidth, mTuWidth));
			iv_params.setMargins(5, 0, 5, 0);
			tuIV.setLayoutParams(iv_params);
			tuIV.setImageResource(R.drawable.image_default);
			String url = tus.get(i).getUrl();

			ImageUtils.displayImage(url, tuIV, 0, R.drawable.image_default, R.drawable.image_default);

			viewCache.getmQuanThumbnailTusFL().addView(tuIV);
		}

		viewCache.getmQuanThumbnailContentTV().setText(item.getContent());
		if (item.getTucnt() > 4) {
			viewCache.getmQuanThumbnailTusSizeTV().setVisibility(View.VISIBLE);
			viewCache.getmQuanThumbnailTusSizeTV().setText("共" + item.getTucnt() + "张");
		} else {
			viewCache.getmQuanThumbnailTusSizeTV().setVisibility(View.GONE);
		}
		viewCache.getmQuanThumbnailDeleteTV().setOnClickListener(this);
		viewCache.getmQuanThumbnailDeleteTV().setVisibility(
				mAction != null && mAction.equals("com.chewuwuyou.app.my_quan") ? View.VISIBLE : View.GONE);
		viewCache.getmQuanThumbnailDeleteTV().setTag(position_integer);
		return convertView;
	}

	@Override
	public void onClick(View v) {
		Message msg;
		Object tag = v.getTag();
		Integer position_integer = null;
		QuanItem quan = null;
		if (tag instanceof Integer)
			position_integer = (Integer) tag;
		if (position_integer != null) {
			quan = (QuanItem) mData.get(position_integer.intValue());
		}
		switch (v.getId()) {
		case R.id.quan_thumbnail_bg_avatar_iv:
			// 删除一个评论
			// 进入到个人详情中
			Intent intent2 = new Intent(mContext, PersonalHomeActivity2.class);
			intent2.putExtra("userId", mOtherId);
			mContext.startActivity(intent2);
			break;
		case R.id.quan_thumbnail_delete_tv:
			// 进入到个人详情中
			msg = new Message();
			msg.what = MyQuanFragment.SHOW_DEL_QUAN_DIALOG;
			msg.obj = quan;
			mHandler.sendMessage(msg);
			break;
		default:
			break;
		}
	}

	public void setmBackImg(String mBackImg) {
		this.mBackImg = mBackImg;
	}

	public void setmHeadImg(String mHeadImg) {
		this.mHeadImg = mHeadImg;
	}

}