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
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.TieItem;
import com.chewuwuyou.app.bean.TieTuItem;
import com.chewuwuyou.app.ui.MyTieFragment;
import com.chewuwuyou.app.ui.PersonalHomeActivity2;
import com.chewuwuyou.app.ui.TieActivity;
import com.chewuwuyou.app.ui.TieDetailActivity;
import com.chewuwuyou.app.ui.VehicleQuanVewPager;
import com.chewuwuyou.app.utils.CarFriendQuanUtils;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.ImageUtils.ScalingLogic;
import com.chewuwuyou.app.viewcache.DINGTieItemViewCache;
import com.chewuwuyou.app.viewcache.TieItemViewCache;
import com.chewuwuyou.app.widget.HackyViewPager;

public class TieAdapter extends SNSAdapter implements OnClickListener {

	private List<TieItem> mData;
	private Handler mHandler;
	private static final int DING = 0;
	private static final int OTHER = DING + 1;

	private String mAction;// 判断是展示我的帖子还是...
	private String mBanUrl;// 所在板块图
	private int mTodayNum;// 今日话题

	public TieAdapter(Activity context, Handler handler, List<TieItem> data, HackyViewPager viewPager, View container,
			String action) {
		super(context, viewPager, container);
		this.mData = data;
		this.mAction = action;
		this.mHandler = handler;
	}

	public TieAdapter(Activity context, Handler handler, List<TieItem> data, HackyViewPager viewPager, View container,
			String banUrl, int todayNum) {
		super(context, viewPager, container);
		this.mData = data;
		this.mBanUrl = banUrl;
		this.mTodayNum = todayNum;
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
	public int getItemViewType(int position) {
		TieItem item = mData.get(position);
		if (item.getIsPro().equals("1"))
			return DING;
		else
			return OTHER;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int type = getItemViewType(position);

		switch (type) {
		case DING:
			convertView = createDingView(convertView, position);
			break;
		default:
			convertView = createView(convertView, position);
			break;
		}
		return convertView;
	}

	private View createView(View convertView, int position) {
		// TODO Auto-generated method stub
		TieItemViewCache viewCache = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.tie_item, null);
			viewCache = new TieItemViewCache(convertView);
			convertView.setTag(viewCache);
		} else {
			viewCache = (TieItemViewCache) convertView.getTag();
		}
		if (position == 0 && mAction == null) {
			// 显示板块信息
			viewCache.getmTieBanKuaiLL().setVisibility(View.VISIBLE);
			ImageUtils.displayImage(mContext, mBanUrl, viewCache.getmTieBanKuaiIV(), 0, mTuWidth, mTuHeight,
					ScalingLogic.CROP, R.drawable.image_default, R.drawable.image_load_fail);
			viewCache.getmTieBanKuaiNumberTV().setText(String.valueOf(mTodayNum));
		} else {
			viewCache.getmTieBanKuaiLL().setVisibility(View.GONE);
		}
		final Integer position_integer = Integer.valueOf(position);
		final TieItem tie = mData.get(position);
		if (tie != null) {
			ImageUtils.displayImage(tie.getUrl(), viewCache.getmTieItemAvatarIV(), 360, R.drawable.user_fang_icon,
					R.drawable.user_fang_icon);
			viewCache.getmTieItemNameTV().setText(CarFriendQuanUtils.showCarFriendName(tie));
			if (tie.getSex() == 0) {
				viewCache.getmTieItemSexIV().setImageResource(R.drawable.man);
			} else if (tie.getSex() == 1) {
				viewCache.getmTieItemSexIV().setImageResource(R.drawable.woman);
			} else {
				viewCache.getmTieItemSexIV().setImageResource(R.drawable.icon_nosex);
			}
			// viewCache.getmTieItemSexIV().setImageResource(tie.getSex() == 1 ?
			// R.drawable.woman : R.drawable.man);
			viewCache.getmTieItemAreaTV().setText(!TextUtils.isEmpty(tie.getLocation()) ? tie.getLocation() : "不告诉你");
			viewCache.getmTieItemContentTV().setText(tie.getContent());
			List<TieTuItem> tus = tie.getTus();
			if (tus.size() > 0) {
				viewCache.getmTieItemTusLL().setVisibility(View.VISIBLE);
				final ArrayList<String> tuUrls = new ArrayList<String>();
				for (TieTuItem tu : tus) {
					tuUrls.add(tu.getUrl());
				}
				int totalWidth = tie.getTus().size() * mTuWidth
						+ mContext.getResources().getDimensionPixelSize(R.dimen.quan_tu_interval) * (tuUrls.size() - 1);
				LayoutParams params = new LayoutParams(totalWidth, mTuHeight);
				viewCache.getmTieItemTusInnerLL().setLayoutParams(params);

				viewCache.getmTieItemTusInnerLL().removeAllViews();
				final LinearLayout tieItemTusInnerLL = viewCache.getmTieItemTusInnerLL();
				for (int i = 0; i < tus.size(); i++) {
					final ImageView tuIV = new ImageView(mContext);
					tuIV.setAdjustViewBounds(true);
					tuIV.setScaleType(ScaleType.FIT_XY);
					LinearLayout.LayoutParams iv_params;
					if (tus.size() == 1) {
						iv_params = new LinearLayout.LayoutParams(new LayoutParams(
								LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
					} else {
						iv_params = new LinearLayout.LayoutParams(new LayoutParams(mTuWidth, mTuHeight));
					}
					iv_params.setMargins(0, 0, 5, 0);
					tuIV.setLayoutParams(iv_params);
					tuIV.setImageResource(R.drawable.image_default);
					String url = tus.get(i).getUrl();
					ImageUtils.displayImage(mContext, url, tuIV, 0, mTuWidth, mTuHeight, ScalingLogic.CROP,
							R.drawable.image_default, R.drawable.image_default);
					final int viewPagerPosition = i;
					tuIV.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
						//	zoomImageFromThumb(arg0, tuUrls, tieItemTusInnerLL, viewPagerPosition);
							Intent intent = new Intent(mContext, VehicleQuanVewPager.class);
							intent.putStringArrayListExtra("url",tuUrls);
							intent.putExtra("viewPagerPosition",viewPagerPosition+"");

							mContext.startActivity(intent);

						}
					});
					tieItemTusInnerLL.addView(tuIV);
				}

			} else {
				viewCache.getmTieItemTusLL().setVisibility(View.GONE);
			}

			Date date = null;
			try {
				date = new SimpleDateFormat("yyyyMMddHHmmss").parse(tie.getPublishTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			viewCache.getmTieItemDateTV().setText(new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(date));
			// viewCache.getmTieItemDeleteLL().setVisibility(mAction != null &&
			// mAction.equals("com.chewuwuyou.app.my_tie") ? View.VISIBLE :
			// View.GONE);
			// 后面要改的
			viewCache.getmTieItemZanTV()
					.setText(mContext.getResources().getString(R.string.item_zan_ping_cnt, tie.getTiezancnt()));
			viewCache.getmTieItemPingTV()
					.setText(mContext.getResources().getString(R.string.item_zan_ping_cnt, tie.getTiepingcnt()));

			if (Integer.parseInt(tie.getZaned()) == 1) {
				viewCache.getmTieItemZanTV().setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconheart, 0, 0, 0);
			} else {
				viewCache.getmTieItemZanTV().setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconhear, 0, 0, 0);
			}

		}

		viewCache.getmTieItemAvatarIV().setOnClickListener(this);
		viewCache.getmTieItemPingTV().setOnClickListener(this);
		viewCache.getmTieItemZanTV().setOnClickListener(this);
		viewCache.getmTieItemEditIV().setOnClickListener(this);

		viewCache.getmTieItemAvatarIV().setTag(position_integer);
		viewCache.getmTieItemPingTV().setTag(position_integer);
		viewCache.getmTieItemZanTV().setTag(position_integer);
		viewCache.getmTieItemEditIV().setTag(position_integer);
		return convertView;
	}

	private View createDingView(View convertView, int position) {
		// TODO Auto-generated method stub
		DINGTieItemViewCache viewCache = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.ding_tie_item, null);
			viewCache = new DINGTieItemViewCache(convertView);
			convertView.setTag(viewCache);
		} else {
			viewCache = (DINGTieItemViewCache) convertView.getTag();
		}
		if (position == 0 && mAction == null) {
			// 显示板块信息
			viewCache.getmTieBanKuaiLL().setVisibility(View.VISIBLE);
			ImageUtils.displayImage(mContext, mBanUrl, viewCache.getmTieBanKuaiIV(), 0, mTuWidth, mTuHeight,
					ScalingLogic.CROP, R.drawable.image_default, R.drawable.image_load_fail);
			viewCache.getmTieBanKuaiNumberTV().setText(String.valueOf(mTodayNum));
		} else {
			viewCache.getmTieBanKuaiLL().setVisibility(View.GONE);
		}
		final TieItem tie = mData.get(position);
		if (tie != null) {
			viewCache.getmDINGTieItemTitleTV().setText(tie.getTitle());
		}

		return convertView;
	}

	public void updateData(List<TieItem> data) {
		this.mData = data;
		this.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Message msg;
		Intent intent;
		Object tag = v.getTag();
		Integer position_integer = null;
		if (tag instanceof Integer)
			position_integer = (Integer) tag;
		final TieItem tie = (TieItem) mData.get(position_integer.intValue());
		switch (v.getId()) {
		case R.id.tie_item_ll:
			// 显示大图
			intent = new Intent(mContext, TieDetailActivity.class);
			intent.putExtra("id", tie.getId());
			intent.putExtra("ziji", tie.getZiji());
			mContext.startActivity(intent);
			break;
		case R.id.tie_item_ping_tv:
			intent = new Intent(mContext, TieDetailActivity.class);
			intent.putExtra("id", tie.getId());
			intent.putExtra("ziji", tie.getZiji());
			intent.putExtra("isShowInputMethod", true);
			mContext.startActivity(intent);
			break;
		case R.id.tie_item_avatar_iv:
			// 进入到个人详情中
			intent = new Intent(mContext, PersonalHomeActivity2.class);
			intent.putExtra("userId", tie.getUserId());
			mContext.startActivity(intent);
			break;

		case R.id.tie_item_zan_tv:
			// 切换赞状态
			msg = new Message();
			if (mAction != null && mAction.equals("com.chewuwuyou.app.my_tie")) {
				msg.what = MyTieFragment.TOGGLE_ZAN;
			} else {
				msg.what = TieActivity.TOGGLE_ZAN;
			}
			msg.obj = tie;
			mHandler.sendMessage(msg);
			break;
		case R.id.tie_item_edit_iv:
			// 切换赞状态
			msg = new Message();
			if (mAction != null && mAction.equals("com.chewuwuyou.app.my_tie")) {
				msg.what = MyTieFragment.EDIT_TIE;
			} else {
				msg.what = TieActivity.EDIT_TIE;
			}
			msg.obj = tie;
			mHandler.sendMessage(msg);
			break;

		default:
			break;
		}
	}

	public void setmTodayNum(int todayNum) {
		this.mTodayNum = todayNum;
	}

}
