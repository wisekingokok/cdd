package com.chewuwuyou.app.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.HotTieItem;
import com.chewuwuyou.app.bean.TieTuItem;
import com.chewuwuyou.app.ui.HotTieActivity;
import com.chewuwuyou.app.ui.HotTieDetailActivity;
import com.chewuwuyou.app.ui.PersonalHomeActivity2;
import com.chewuwuyou.app.utils.CarFriendQuanUtils;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.ImageUtils.ScalingLogic;
import com.chewuwuyou.app.viewcache.HotTieItemViewCache;

public class HotTieAdapter extends BaseAdapter implements OnClickListener {

	private Activity mContext;
	private List<HotTieItem> mData;
	private LayoutInflater mInflater;
	// private static final int FLOW = 0;
	// private static final int OTHER = FLOW + 1;
	protected static final DisplayMetrics mOutMetrics = new DisplayMetrics();

	protected int mBgTuWidth = 0;
	protected int mBgTuHeight = 0;
	private Handler mHandler;

	public HotTieAdapter(Activity context, Handler handler, List<HotTieItem> data) {

		this.mContext = context;
		this.mData = data;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		mContext.getWindowManager().getDefaultDisplay().getMetrics(mOutMetrics);

		mBgTuWidth = mOutMetrics.widthPixels;
		mBgTuHeight = (mOutMetrics.widthPixels * 2) / 3;
		this.mHandler = handler;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HotTieItemViewCache viewCache = null;
		if (convertView == null || !(convertView.getTag() instanceof HotTieItemViewCache)) {
			convertView = mInflater.inflate(R.layout.hot_tie_item, null);
			viewCache = new HotTieItemViewCache(convertView);
			convertView.setTag(viewCache);
		} else if (convertView.getTag() instanceof HotTieItemViewCache) {
			viewCache = (HotTieItemViewCache) convertView.getTag();
		}
		final Integer position_integer = Integer.valueOf(position);
		final HotTieItem item = mData.get(position);
		if (item != null && viewCache != null) {
			ImageUtils.displayImage(item.getUrl(), viewCache.getmHotTieItemAvatarIV(), 360, R.drawable.user_fang_icon,
					R.drawable.user_fang_icon);
			if (TextUtils.isEmpty(item.getSex())) {
			} else {
				if (item.getSex().equals("0")) {
					viewCache.getmHotTieItemSexIV().setImageResource(R.drawable.man);
				} else if (item.getSex().equals("1")) {
					viewCache.getmHotTieItemSexIV().setImageResource(R.drawable.woman);
				} else {
					viewCache.getmHotTieItemSexIV().setImageResource(R.drawable.icon_nosex);
				}
			}

			// viewCache.getmHotTieItemSexIV().setImageResource(item.getSex().equals("1")
			// ? R.drawable.woman : R.drawable.man);
			viewCache.getmHotTieItemNameTV().setText(CarFriendQuanUtils.showCarFriendName(item));
			viewCache.getmHotTieItemContentTV().setText(item.getContent());
			if (item.getTucnt() > 0) {
				viewCache.getmHotTieItemTuRL().setVisibility(View.VISIBLE);
			} else {
				viewCache.getmHotTieItemTuRL().setVisibility(View.GONE);
			}
			List<TieTuItem> tus = item.getTus();
			if (tus.size() > 0) {
				viewCache.getmHotTieItemTuRL().setVisibility(View.VISIBLE);
				ImageView tuIV = viewCache.getmHotTieItemTuIV();
				ImageUtils.displayImage(mContext, tus.get(0).getUrl(), tuIV, 0, mBgTuWidth, mBgTuHeight,ScalingLogic.CROP, R.drawable.image_default, R.drawable.image_default);
				
				viewCache.getmHotTieItemTuInfoTV().setText(String.valueOf(tus.size()));
			} else {
				viewCache.getmHotTieItemTuRL().setVisibility(View.GONE);
			}

			Date date = null;
			try {
				date = new SimpleDateFormat("yyyyMMddHHmmss").parse(item.getPublishTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			viewCache.getmHotTieItemDateTV().setText(new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(date));
			viewCache.getmHotTieItemAreaTV()
					.setText(!TextUtils.isEmpty(item.getLocation()) ? item.getLocation() : "不告诉你");

			if (Integer.parseInt(item.getZaned()) == 1) {
				viewCache.getmHotTieItemZanTV().setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconheart, 0, 0, 0);
			} else {
				viewCache.getmHotTieItemZanTV().setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconhear, 0, 0, 0);
			}

			// 后面要改的
			viewCache.getmHotTieItemZanTV()
					.setText(mContext.getResources().getString(R.string.item_zan_ping_cnt, item.getTiezancnt()));

			viewCache.getmHotTieItemPingTV()
					.setText(mContext.getResources().getString(R.string.item_zan_ping_cnt, item.getTiepingcnt()));
			viewCache.getmView().setTag(position_integer);

			viewCache.getmView().setOnClickListener(this);
			viewCache.getmHotTieItemAvatarIV().setOnClickListener(this);
			viewCache.getmHotTieItemPingTV().setOnClickListener(this);
			viewCache.getmHotTieItemEditIV().setOnClickListener(this);
			viewCache.getmHotTieItemZanTV().setOnClickListener(this);

			viewCache.getmHotTieItemZanTV().setTag(position_integer);
			viewCache.getmHotTieItemAvatarIV().setTag(position_integer);
			viewCache.getmHotTieItemPingTV().setTag(position_integer);
			viewCache.getmHotTieItemEditIV().setTag(position_integer);
		}

		return convertView;
	}

	// public View createViewPagerView(View convertView, int position) {
	// HotTieViewPagerViewCache viewCache = null;
	// if (convertView == null) {
	// convertView = mInflater.inflate(R.layout.hot_tie_view_flow, null);
	// viewCache = new HotTieViewPagerViewCache(convertView);
	// convertView.setTag(viewCache);
	// } else if (convertView.getTag() instanceof HotTieViewPagerViewCache) {
	// viewCache = (HotTieViewPagerViewCache) convertView.getTag();
	// }
	// final Integer position_integer = Integer.valueOf(position);
	// final HotTieItem item = mData.get(position);
	// if (item != null) {
	// // List<TieTuItem> tus = item.getTus();
	// //
	// // final List<String> tuUrls = new ArrayList<String>();
	// //
	// // for (TieTuItem tu : tus) {
	// // tuUrls.add(tu.getUrl());
	// // }
	// // viewCache.getmAutoScrollViewPager().setAdapter(new
	// // ImagePagerAdapter(mContext, tuUrls));
	// List<Integer> tuIds = new ArrayList<Integer>();
	// tuIds.add(R.drawable.cddbg1);
	// tuIds.add(R.drawable.cddbg2);
	// tuIds.add(R.drawable.cddbg3);
	// viewCache.getmAutoScrollViewPager().setAdapter(new
	// ImagePagerAdapter2(mContext, tuIds));
	// viewCache.getmCirclePageIndicator().setViewPager(viewCache.getmAutoScrollViewPager());
	// // viewCache.getmAutoScrollViewPager().setOnPageChangeListener(new
	// // MyPageChangeListener());
	// // viewCache.getmAutoScrollViewPager().setInterval(2000);
	// // viewCache.getmAutoScrollViewPager().startAutoScroll();
	// viewCache.getmAutoScrollViewPager().setCurrentItem(Integer.MAX_VALUE / 2
	// - Integer.MAX_VALUE / 2 % tuIds.size());
	// }
	// return convertView;
	// }

	// public View createView(View convertView, int position) {
	//
	// }

	public void updateData(List<HotTieItem> data) {
		this.mData = data;
		this.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Message msg;
		Object tag = v.getTag();
		Integer position_integer = null;
		Intent intent = null;
		if (tag instanceof Integer)
			position_integer = (Integer) tag;
		final HotTieItem item = mData.get(position_integer.intValue());
		switch (v.getId()) {
		case R.id.hot_tie_item_avatar_iv:
			// 进入到个人详情中
			intent = new Intent(mContext, PersonalHomeActivity2.class);
			intent.putExtra("userId", item.getUserId());
			mContext.startActivity(intent);
			break;
		case R.id.hot_tie_item_ping_tv:
			intent = new Intent(mContext, HotTieDetailActivity.class);
			intent.putExtra("id", item.getId());
			intent.putExtra("ziji", item.getZiji());
			intent.putExtra("isShowInputMethod", true);
			mContext.startActivity(intent);
			break;
		case R.id.hot_tie_item_ll:
			// 显示大图
			intent = new Intent(mContext, HotTieDetailActivity.class);
			intent.putExtra("id", item.getId());
			intent.putExtra("ziji", item.getZiji());
			mContext.startActivity(intent);
			break;
		case R.id.hot_tie_item_zan_tv:
			// 切换赞状态
			msg = new Message();
			msg.what = HotTieActivity.TOGGLE_ZAN;
			msg.obj = item;
			mHandler.sendMessage(msg);
			break;
		case R.id.hot_tie_item_edit_iv:
			// 切换赞状态
			msg = new Message();
			msg.what = HotTieActivity.EDIT_TIE;
			msg.obj = item;
			mHandler.sendMessage(msg);
			break;
		default:
			break;
		}
	}
}
