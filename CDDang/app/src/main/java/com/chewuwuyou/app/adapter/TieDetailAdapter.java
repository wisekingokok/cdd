package com.chewuwuyou.app.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.TieDetailHeaderEntity;
import com.chewuwuyou.app.bean.TiePingItem;
import com.chewuwuyou.app.bean.TiePingReplyItem;
import com.chewuwuyou.app.bean.TieTuItem;
import com.chewuwuyou.app.ui.PersonalHomeActivity2;
import com.chewuwuyou.app.ui.TieDetailActivity;
import com.chewuwuyou.app.ui.VehicleQuanVewPager;
import com.chewuwuyou.app.ui.ZanOrInvolverActivity;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.CarFriendQuanUtils;
import com.chewuwuyou.app.utils.ChatInputUtils;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.ImageUtils.ScalingLogic;
import com.chewuwuyou.app.utils.JsonUtil;
import com.chewuwuyou.app.viewcache.CaptureItemViewCache;
import com.chewuwuyou.app.viewcache.TieHeaderEntityViewCache;
import com.chewuwuyou.app.viewcache.TiePingItemViewCache;
import com.chewuwuyou.app.widget.HackyViewPager;
import com.chewuwuyou.app.widget.MyGridView;

/**
 * @describe:论坛动态评论Adapter
 * @author:XH
 * @version
 * @created:
 */
public class TieDetailAdapter extends SNSAdapter implements OnClickListener {

	private List<Object> mData;
	private Handler mHandler;
	private Map<String, String> mFaceCharacterMap;

	public TieDetailAdapter(Activity context, Handler handler, List<Object> data, HackyViewPager viewPager,
			View container) {
		super(context, viewPager, container);
		this.mData = data;
		this.mFaceCharacterMap = JsonUtil.getFaceStrMap(context);
		this.mHandler = handler;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		if (position == 0) {
			return (TieDetailHeaderEntity) mData.get(position);
		} else {
			return (TiePingItem) mData.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Integer position_integer = Integer.valueOf(position);
		if (position == 0) {
			TieHeaderEntityViewCache viewCache = null;
			convertView = mInflater.inflate(R.layout.tie_detail_headerview, null);
			viewCache = new TieHeaderEntityViewCache(convertView);
			convertView.setTag(viewCache);

			TieDetailHeaderEntity item = (TieDetailHeaderEntity) mData.get(position);
			ImageUtils.displayImage(item.getUrl(), viewCache.getmDetailAvatarIV(), 360, R.drawable.user_yuan_icon,
					R.drawable.user_yuan_icon);

			if (item.getSex().equals("0")) {
				viewCache.getmTieDetailSexIV().setImageResource(R.drawable.man);
			} else if (item.getSex().equals("1")) {
				viewCache.getmTieDetailSexIV().setImageResource(R.drawable.woman);
			} else {
				viewCache.getmTieDetailSexIV().setImageResource(R.drawable.icon_nosex);
			}

			// viewCache.getmTieDetailSexIV().setImageResource(item.getSex().equals("1")
			// ? R.drawable.woman : R.drawable.man);
			viewCache.getmDetailAvatarIV().setOnClickListener(this);
			viewCache.getmDetailAvatarIV().setTag(position_integer);
			viewCache.getmDetailNameTV().setText(CarFriendQuanUtils.showCarFriendName(item));
			//显示评论的表情及文本
			viewCache.getmDetailContentTV()
					.setText(ChatInputUtils.displayBigFacePic(mContext, item.getContent(), mFaceCharacterMap));

			List<TieTuItem> tus = item.getTus();
			if (tus.size() > 0) {
				viewCache.getmTieDetailTusLL().setVisibility(View.VISIBLE);
				final ArrayList<String> tuUrls = new ArrayList<String>();
				for (TieTuItem tu : tus) {
					tuUrls.add(tu.getUrl());
				}
				int totalWidth = item.getTus().size() * mTuWidth
						+ mContext.getResources().getDimensionPixelSize(R.dimen.quan_tu_interval) * (tuUrls.size() - 1);
				FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(totalWidth, mTuHeight);
				viewCache.getmTieDetailTusInnerLL().setLayoutParams(params);

				viewCache.getmTieDetailTusInnerLL().removeAllViews();
				final LinearLayout tieDetailTusInnerLL = viewCache.getmTieDetailTusInnerLL();
				for (int i = 0; i < tus.size(); i++) {
					final ImageView tuIV = new ImageView(mContext);
					tuIV.setAdjustViewBounds(true);
					tuIV.setScaleType(ScaleType.FIT_CENTER);
					LinearLayout.LayoutParams iv_params;
					if (tus.size() == 1) {
						iv_params = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
					} else {
						iv_params = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(mTuWidth, mTuHeight));
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
							//zoomImageFromThumb(arg0, tuUrls, tieDetailTusInnerLL, viewPagerPosition);
							Intent intent = new Intent(mContext, VehicleQuanVewPager.class);
							intent.putStringArrayListExtra("url",tuUrls);
							intent.putExtra("viewPagerPosition",viewPagerPosition+"");

							mContext.startActivity(intent);


						}
					});
					tieDetailTusInnerLL.addView(tuIV);
				}

			} else {
				viewCache.getmTieDetailTusLL().setVisibility(View.GONE);
			}

			Date date = null;
			try {
				date = new SimpleDateFormat("yyyyMMddHHmmss").parse(item.getPublishTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			viewCache.getmDetailDateTV().setText(new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(date));

			viewCache.getmDetailPingTV().setOnClickListener(this);
			viewCache.getmDetailPingTV().setTag(position_integer);

			viewCache.getmDetailZanTV().setOnClickListener(this);
			viewCache.getmDetailZanTV().setTag(position_integer);

			viewCache.getmDetailZanTV()
					.setText(mContext.getResources().getString(R.string.item_zan_ping_cnt, item.getTiezancnt()));
			viewCache.getmDetailPingTV()
					.setText(mContext.getResources().getString(R.string.item_zan_ping_cnt, item.getTiepingcnt()));
			// 后面要改的

			if (Integer.parseInt(item.getZaned()) == 1) {
				viewCache.getmDetailZanTV().setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconheart, 0, 0, 0);
			} else {
				viewCache.getmDetailZanTV().setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconhear, 0, 0, 0);
			}
			if (item.getTiezancnt() > 0) {
				viewCache.getmDetailZanerLL().setVisibility(View.VISIBLE);
				int columWidth = mContext.getResources().getDimensionPixelSize(R.dimen.tie_zan_avatar_width);
				int numColumns = item.getTiezancnt();
				int totalWidth = numColumns * columWidth + (numColumns - 1) * 2
						* mContext.getResources().getDimensionPixelSize(R.dimen.quan_zan_avatar_padding);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(totalWidth,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				viewCache.getmDetailZanerGV().setLayoutParams(params);
				viewCache.getmDetailZanerGV().setNumColumns(numColumns);
				viewCache.getmDetailZanerGV().setColumnWidth(columWidth);
				viewCache.getmDetailZanerGV().setStretchMode(GridView.NO_STRETCH);
				TieZanAdapter tieZanAdapter = null;

				if (item.getTiezancnt() <= 6) {
					tieZanAdapter = new TieZanAdapter(mContext, item.getTiezan());
					// viewCache.getmDetailZanerMoreTV().setVisibility(View.GONE);
				} else {
					tieZanAdapter = new TieZanAdapter(mContext, item.getTiezan().subList(0, 6));
					// viewCache.getmDetailZanerMoreTV().setVisibility(View.VISIBLE);
				}
				viewCache.getmDetailZanerGV().setAdapter(tieZanAdapter);
				viewCache.getmDetailZanerGV().setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						Toast.makeText(mContext, "没做处理", Toast.LENGTH_SHORT).show();
					}

				});
			} else {
				viewCache.getmDetailZanerLL().setVisibility(View.GONE);
			}
			if (item.getTiepingcnt() > 0) {
				viewCache.getmDetailPingTitleTV().setVisibility(View.VISIBLE);
				// viewCache.getmDetailPingTitleTV().setText(mContext.getString(R.string.tie_ping_title,
				// item.getTiepingcnt()));
				viewCache.getmDetailPingTitleTV().setText(mContext.getString(R.string.tie_ping_title));
			} else {
				viewCache.getmDetailPingTitleTV().setVisibility(View.GONE);
			}
			viewCache.getmDetailZanerLL().setOnClickListener(this);
			viewCache.getmDetailEditIV().setOnClickListener(this);
			viewCache.getmDetailEditIV().setTag(position_integer);
		} else {

			TiePingItemViewCache viewCache = null;
			if (convertView == null || (convertView.getTag()) instanceof TieHeaderEntityViewCache) {
				convertView = mInflater.inflate(R.layout.tie_ping_item, null);
				viewCache = new TiePingItemViewCache(convertView);
				convertView.setTag(viewCache);
			} else {
				viewCache = (TiePingItemViewCache) convertView.getTag();
			}

			TiePingItem item = (TiePingItem) mData.get(position);
			ImageUtils.displayImage(item.getUrl(), viewCache.getmPingAvatarIV(), 10, R.drawable.user_fang_icon,
					R.drawable.user_fang_icon);
			viewCache.getmPingAvatarIV().setOnClickListener(this);
			viewCache.getmPingAvatarIV().setTag(position_integer);

			if (item.getFriendId().equals(CacheTools.getUserData("userId"))) {
				viewCache.getmPingReplyTV().setText("删除");
			} else {
				viewCache.getmPingReplyTV().setText("回复");
			}

			viewCache.getmPingReplyTV().setOnClickListener(this);
			viewCache.getmPingReplyTV().setTag(position_integer);

			viewCache.getmPingNameTV().setText(displayPingName(item));
			//修改表情显示
			viewCache.getmPingContentTV()
					.setText(ChatInputUtils.displayBigFacePic(mContext, item.getContent(), mFaceCharacterMap));
			try {
				Date date = new SimpleDateFormat("yyyyMMddHHmmss").parse(item.getPublishTime());
				viewCache.getmPingDateTV().setText(new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(date));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return convertView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Message msg;
		Object tag = v.getTag();
		Integer position_integer = null;
		if (tag instanceof Integer) {
			position_integer = (Integer) tag;
		}
		switch (v.getId()) {
		case R.id.tie_detail_avatar_iv:
			// 进入到个人详情中
			TieDetailHeaderEntity header = (TieDetailHeaderEntity) mData.get(position_integer.intValue());
			Intent header_intent = new Intent(mContext, PersonalHomeActivity2.class);
			header_intent.putExtra("userId", header.getUserId());
			mContext.startActivity(header_intent);
			break;
		// case R.id.tie_detail_content_more_tv:
		// // 文本展开
		// break;
		case R.id.tie_detail_ping_tv:
			// 发消息给Activity去弹出评价框和键盘
			msg = new Message();
			msg.what = TieDetailActivity.PING_TIE_ZI;
			mHandler.sendMessage(msg);
			break;
		case R.id.tie_detail_zan_tv:
			// 切换赞状态
			msg = new Message();
			msg.what = TieDetailActivity.TOGGLE_ZAN;
			mHandler.sendMessage(msg);
			break;
		case R.id.ping_avatar_iv:
			// 进入到个人详情中
			TiePingItem ping = (TiePingItem) mData.get(position_integer.intValue());
			Intent ping_intent = new Intent(mContext, PersonalHomeActivity2.class);
			ping_intent.putExtra("userId", Integer.parseInt(ping.getFriendId()));
			mContext.startActivity(ping_intent);
			break;
		case R.id.ping_reply_tv:
			msg = new Message();
			TiePingItem item = (TiePingItem) mData.get(position_integer.intValue());
			if (item.getFriendId().equals(CacheTools.getUserData("userId"))) {
				msg.what = TieDetailActivity.DELETE_PING;
			} else {
				msg.what = TieDetailActivity.REPLY_PING;
			}
			msg.obj = item;
			mHandler.sendMessage(msg);
			break;
		case R.id.tie_detail_edit_iv:
			// 切换赞状态
			msg = new Message();
			msg.what = TieDetailActivity.EDIT_TIE;
			mHandler.sendMessage(msg);
			break;
		case R.id.tie_detail_zaner_ll:
			Intent intent = new Intent(mContext, ZanOrInvolverActivity.class);
			intent.setAction("com.chewuwuyou.app.show_tie_zaner");
			Bundle bundle = new Bundle();
			bundle.putSerializable("tie_detail_header", (TieDetailHeaderEntity) mData.get(0));
			intent.putExtras(bundle);
			mContext.startActivity(intent);
			break;

		default:
			break;
		}
	}

	public class TieTuGridAdapter extends SNSAdapter {
		private ArrayList<String> mTuData;
		private MyGridView mTusGV;

		public TieTuGridAdapter(Activity context, ArrayList<String> data, HackyViewPager viewPager, View container,
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
					//zoomImageFromThumb(view, mTuData, mTusGV, position);
					Intent intent = new Intent(mContext, VehicleQuanVewPager.class);
					intent.putStringArrayListExtra("url",mTuData);
					intent.putExtra("viewPagerPosition",position+"");

					mContext.startActivity(intent);

				}
			});

			return convertView;
		}
	}

	private SpannableString displayPingName(TiePingItem ping) {
		StringBuilder sb = new StringBuilder();
		SpannableString ssb = null;
		String name = CarFriendQuanUtils.showCarFriendName(ping);
		TiePingReplyItem toWho = ping.getToWho();

		try {
			if (toWho != null && toWho.getName() != null && toWho.getId() > 0) {
				String toname = toWho.getName();
				int length = mContext.getResources().getString(R.string.reply_to_who).length();
				sb.append(name).append(mContext.getResources().getString(R.string.reply_to_who)).append(toname);
				ssb = new SpannableString(sb.toString());
				ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#5b6e82")), 0, name.length(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#434343")), name.length(), name.length() + length,
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#5b6e82")), name.length() + length,
						name.length() + length + toname.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

			} else {
				sb.append(name);
				ssb = new SpannableString(sb.toString());
				if(!TextUtils.isEmpty(name)){
					ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#5b6e82")), 0, name.length(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					ssb.setSpan(new ClickSpan(Integer.parseInt(ping.getFriendId())), 0, name.length(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ssb;
	}

	public class ClickSpan extends ClickableSpan implements OnClickListener {
		private int uid;

		public ClickSpan(int uid) {
			this.uid = uid;
		}

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(mContext, PersonalHomeActivity2.class);
			intent.putExtra("userId", uid);
			mContext.startActivity(intent);
		}

		@Override
		public void updateDrawState(TextPaint ds) {
			ds.setUnderlineText(false);
		}

	}
}
