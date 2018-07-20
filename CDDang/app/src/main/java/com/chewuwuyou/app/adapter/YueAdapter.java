package com.chewuwuyou.app.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.YueItem;
import com.chewuwuyou.app.ui.MyYueFragment;
import com.chewuwuyou.app.ui.PersonalHomeActivity2;
import com.chewuwuyou.app.ui.VehicleQuanVewPager;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.ImageUtils.ScalingLogic;
import com.chewuwuyou.app.viewcache.CaptureItemViewCache;
import com.chewuwuyou.app.viewcache.YueItemViewCache;
import com.chewuwuyou.app.widget.HackyViewPager;
import com.chewuwuyou.app.widget.MyGridView;

/**
 * @describe:活动Adapter
 * @author:XH
 * @version
 * @created:
 */
public class YueAdapter extends SNSAdapter implements OnClickListener {

	private List<YueItem> mYueData;
	private String mAction;
	private Handler mHandler;
	private String mTitle;

	public YueAdapter(Activity context, List<YueItem> data, HackyViewPager viewPager, View container, String title) {
		super(context, viewPager, container);
		this.mYueData = data;
		this.mTitle = title;
	}

	public YueAdapter(Activity context, List<YueItem> data, HackyViewPager viewPager, View container) {
		super(context, viewPager, container);
		this.mYueData = data;
	}

	public YueAdapter(Activity context, Handler handler, List<YueItem> data, HackyViewPager viewPager, View container,
			String action) {
		super(context, viewPager, container);
		this.mYueData = data;
		this.mAction = action;
		this.mHandler = handler;
	}

	@Override
	public int getCount() {
		return mYueData.size();
	}

	@Override
	public Object getItem(int position) {
		return mYueData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		YueItemViewCache viewCache = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.yue_item2, null);
			viewCache = new YueItemViewCache(convertView);
			convertView.setTag(viewCache);
		} else {
			viewCache = (YueItemViewCache) convertView.getTag();
		}
		final Integer position_integer = Integer.valueOf(position);
		final YueItem yue = mYueData.get(position);
		if (yue != null) {
			if (position == 0) {
				if (!TextUtils.isEmpty(mTitle)) {
					viewCache.getmYueItemHeaderNumTV().setVisibility(View.VISIBLE);
					viewCache.getmYueItemHeaderNumTV().setText(mTitle);// 帖子总数
				} else {
					viewCache.getmYueItemHeaderNumTV().setVisibility(View.GONE);
				}
			} else {
				viewCache.getmYueItemHeaderNumTV().setVisibility(View.GONE);
			}
			// ImageUtils.displayImage(yue.getTus().get(0).getUrl(),
			// viewCache.getmYueItemInfoIV(), 0);
			if (yue.getTus().size() > 0) {
				ImageUtils.displayImage(yue.getTus().get(0).getUrl(), viewCache.getmYueItemInfoIV(), 0,
						mContext.getResources().getDimensionPixelSize(R.dimen.yue_item_info_iv_width),
						mContext.getResources().getDimensionPixelSize(R.dimen.yue_item_info_iv_height),
						ScalingLogic.CROP);
			}
			if (!TextUtils.isEmpty(yue.getTitle())) {
				viewCache.getmYueItemTitleTV().setText(yue.getTitle());
			}
			viewCache.getmYueItemDeleteTV().setVisibility(
					mAction != null && mAction.equals("com.chewuwuyou.app.my_yue") ? View.VISIBLE : View.GONE);
			viewCache.getmYueItemDeleteTV().setOnClickListener(this);
			viewCache.getmYueItemChargeTypeTV()
					.setText(yue.getChargeType() == 0 ? "邦主买单" : yue.getChargeType() == 1 ? "大伙AA" : "求请客");
			if (!TextUtils.isEmpty(yue.getLocation())) {
				viewCache.getmYueItemLocationTV().setText(yue.getLocation());
			}
			viewCache.getmYueItemInvolveTV()
					.setText(new StringBuilder().append(String.valueOf(yue.getInvolve())).append("人感兴趣").toString());
			Date avaliableDate = null;
			try {
				avaliableDate = new SimpleDateFormat("yyyyMMddHHmmss").parse(yue.getAvaliableDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			viewCache.getmYueItemAvailableDateTV()
					.setText(new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(avaliableDate));

		}
		viewCache.getmYueItemDeleteTV().setTag(position_integer);
		return convertView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Message msg;
		Object tag = v.getTag();
		Integer position_integer = null;
		if (tag instanceof Integer)
			position_integer = (Integer) tag;
		final YueItem yue = (YueItem) mYueData.get(position_integer.intValue());
		switch (v.getId()) {
		case R.id.yue_item_avatar_iv:
			// 进入到个人详情中
			Intent intent = new Intent(mContext, PersonalHomeActivity2.class);
			intent.putExtra("userId", yue.getChiefId());
			mContext.startActivity(intent);
			break;

		case R.id.yue_item_delete_tv:
			// 进入到个人详情中
			msg = new Message();
			msg.what = MyYueFragment.SHOW_DEL_YUE_DIALOG;
			msg.obj = yue;
			mHandler.sendMessage(msg);
			break;
		default:
			break;
		}
	}

	public class YueTuGridAdapter extends SNSAdapter {
		private ArrayList<String> mTuData;
		private MyGridView mTusGV;

		public YueTuGridAdapter(Activity context, ArrayList<String> data, HackyViewPager viewPager, View container,
				MyGridView tusGV) {
			super(context, viewPager, container);
			this.mTuData = data;
			this.mTusGV = tusGV;
		}

		public int getCount() {
			return mTuData.size();
		}

		public Object getItem(int position) {
			return mTuData.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {

			CaptureItemViewCache viewCache = null;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.capture_item, null);
				viewCache = new CaptureItemViewCache(convertView);
				convertView.setTag(viewCache);
			} else {
				viewCache = (CaptureItemViewCache) convertView.getTag();
			}
			ImageUtils.displayImage(mContext, mTuData.get(position), viewCache.getmIV(), 0, mTuWidth, mTuHeight,
					ScalingLogic.CROP, R.drawable.image_default, R.drawable.image_load_fail);

			viewCache.getmIV().setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
				//	zoomImageFromThumb(view, mTuData, mTusGV, position);
					Intent intent = new Intent(mContext, VehicleQuanVewPager.class);
					intent.putStringArrayListExtra("url",mTuData);
					intent.putExtra("viewPagerPosition",position+"");

					mContext.startActivity(intent);
				}
			});

			return convertView;
		}
	}

	public void updateTitle(String title) {
		mTitle = title;
		notifyDataSetChanged();
	}
}
