package com.chewuwuyou.app.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
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
import com.chewuwuyou.app.bean.YueDetailHeaderEntity;
import com.chewuwuyou.app.bean.YuePingItem;
import com.chewuwuyou.app.bean.YuePingReplyItem;
import com.chewuwuyou.app.bean.YueTuItem;
import com.chewuwuyou.app.ui.PersonalHomeActivity2;
import com.chewuwuyou.app.ui.VehicleQuanVewPager;
import com.chewuwuyou.app.ui.YueDetailActivity;
import com.chewuwuyou.app.ui.ZanOrInvolverActivity;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.CarFriendQuanUtils;
import com.chewuwuyou.app.utils.ChatInputUtils;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.ImageUtils.ScalingLogic;
import com.chewuwuyou.app.utils.JsonUtil;
import com.chewuwuyou.app.viewcache.CaptureItemViewCache;
import com.chewuwuyou.app.viewcache.YueHeaderEntityViewCache;
import com.chewuwuyou.app.viewcache.YuePingItemViewCache;
import com.chewuwuyou.app.widget.HackyViewPager;
import com.chewuwuyou.app.widget.MyGridView;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class YueDetailAdapter extends SNSAdapter implements OnClickListener {
	private List<Object> mData;
	private Handler mHandler;
	private Map<String, String> mFaceCharacterMap;

	public YueDetailAdapter(Activity context, Handler handler, List<Object> data, HackyViewPager viewPager,
			View container) {
		super(context, viewPager, container);
		this.mData = data;
		this.mHandler = handler;
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
			return (YueDetailHeaderEntity) mData.get(position);
		} else {
			return (YuePingItem) mData.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Integer position_integer = Integer.valueOf(position);
		if (position == 0) {

			YueHeaderEntityViewCache viewCache = null;
			convertView = mInflater.inflate(R.layout.yue_detail_headerview, null);
			viewCache = new YueHeaderEntityViewCache(convertView);
			convertView.setTag(viewCache);

			YueDetailHeaderEntity item = (YueDetailHeaderEntity) mData.get(position);
			ImageUtils.displayImage(item.getUrl(), viewCache.getmDetailAvatarIV(), 360, R.drawable.user_yuan_icon,
					R.drawable.user_yuan_icon);
			viewCache.getmDetailAvatarIV().setOnClickListener(this);
			viewCache.getmDetailAvatarIV().setTag(position_integer);

			viewCache.getmDetailNameTV().setText(CarFriendQuanUtils.showCarFriendName(item));
			viewCache.getmDetailTitleTV().setText(item.getTitle());
			viewCache.getmDetailChargeTypeTV().setText(item.getChargeType() == 0 ? "请客" : "AA");
			viewCache.getmDetailChargeTypeInfoTV().setText(item.getChargeType() == 0 ? "发起人买单" : "AA制");

			// if(){
			// viewCache.getDeleteTv().setVisibility(View.VISIBLE);//yue_detail_tv
			// viewCache.getmDetailEditIV().setVisibility(View.GONE);
			// viewCache.getDeleteTv().setOnClickListener(new OnClickListener()
			// {
			//
			// @Override
			// public void onClick(View v) {
			//
			// }
			// });
			// }else{
			// viewCache.getDeleteTv().setVisibility(View.GONE);//yue_detail_tv
			// viewCache.getmDetailEditIV().setVisibility(View.GONE);
			// }

			viewCache.getmDetailSexTypeIV().setImageResource(item.getSex() == 1 ? R.drawable.female : R.drawable.male);

			if (item.getSex() == 0) {
				viewCache.getmDetailSexIV().setImageResource(R.drawable.man);
			} else if (item.getSex() == 1) {
				viewCache.getmDetailSexIV().setImageResource(R.drawable.woman);
			} else {
				viewCache.getmDetailSexIV().setVisibility(View.GONE);
			}

			// viewCache.getmDetailSexIV().setImageDrawable(
			// item.getSex() == 1 ?
			// mContext.getResources().getDrawable(R.drawable.woman) :
			// mContext.getResources().getDrawable(R.drawable.man));//
			// 0表示男，1表示女，9表示不限
			viewCache.getmDetailSexInfoTV().setText(item.getSex() == 0 ? "男" : item.getSex() == 1 ? "女" : "不限");
			Date date = null;
			try {
				date = new SimpleDateFormat("yyyyMMddHHmmss").parse(item.getPublishTime());
				viewCache.getmDetailDateTV().setText(new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			// 显示表情及文本
			viewCache.getmDetailContentTV()
					.setText(ChatInputUtils.displayBigFacePic(mContext, item.getContent(), mFaceCharacterMap));
			viewCache.getmDetailPingTV().setOnClickListener(this);
			viewCache.getmDetailPingTV().setTag(position_integer);

			viewCache.getmDetailZanTV().setOnClickListener(this);
			viewCache.getmDetailZanTV().setTag(position_integer);

			viewCache.getmDetailZanTV()
					.setText(mContext.getResources().getString(R.string.item_zan_ping_cnt, item.getYuezancnt()));
			viewCache.getmDetailPingTV()
					.setText(mContext.getResources().getString(R.string.item_zan_ping_cnt, item.getYuepingcnt()));

			if (item.getInvolve() != null && !item.getInvolve().isEmpty()) {
				viewCache.getmDetailInvolverLL().setVisibility(View.VISIBLE);
				int columWidth = mContext.getResources().getDimensionPixelSize(R.dimen.yue_zan_avatar_width);
				int numColumns = item.getInvolve().size() > 6 ? 6 : item.getInvolve().size();
				int totalWidth = numColumns * columWidth + (numColumns - 1) * 2
						* mContext.getResources().getDimensionPixelSize(R.dimen.quan_zan_avatar_padding);

				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(totalWidth,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				viewCache.getmDetailInvolverGV().setLayoutParams(params);
				viewCache.getmDetailInvolverGV().setNumColumns(numColumns);
				viewCache.getmDetailInvolverGV().setColumnWidth(columWidth);
				viewCache.getmDetailInvolverGV().setStretchMode(GridView.NO_STRETCH);
				YueInvolverAdapter yueInvolverAdapter = null;
				if (item.getInvolve().size() < 6) {
					yueInvolverAdapter = new YueInvolverAdapter(mContext, item.getInvolve());
					// viewCache.getmDetailInvolverMoreTV().setVisibility(View.GONE);
				} else {
					yueInvolverAdapter = new YueInvolverAdapter(mContext, item.getInvolve().subList(0, 6));
					// viewCache.getmDetailInvolverMoreTV().setVisibility(View.VISIBLE);
				}
				viewCache.getmDetailInvolverGV().setAdapter(yueInvolverAdapter);

			} else {
				viewCache.getmDetailInvolverLL().setVisibility(View.GONE);
			}
			viewCache.getmDetailInvolverLL().setOnClickListener(this);

			// if (item.getTus().size() > 0) {
			// viewCache.getmYueDetailTusSV().setVisibility(View.VISIBLE);
			// viewCache.getmYueDetailTusGV().setVisibility(View.VISIBLE);
			//
			// List<YueTuItem> tus = item.getTus();
			// final List<String> tuUrls = new ArrayList<String>();
			// for (YueTuItem tu : tus) {
			// tuUrls.add(tu.getUrl());
			// }
			// viewCache.getmYueDetailTusGV().setAdapter(new
			// YueTuGridAdapter(mContext, tuUrls, mViewPager, mContainer,
			// viewCache.getmYueDetailTusGV()));
			//
			// int totalWidth = tuUrls.size() * mTuWidth +
			// mContext.getResources().getDimensionPixelSize(R.dimen.quan_tu_interval)
			// * (tuUrls.size() - 1);
			// LayoutParams params = new LayoutParams(totalWidth, mTuHeight);
			// viewCache.getmYueDetailTusGV().setLayoutParams(params);
			// viewCache.getmYueDetailTusGV().setNumColumns(tuUrls.size());
			// viewCache.getmYueDetailTusGV().setColumnWidth(mTuWidth);
			// viewCache.getmYueDetailTusGV().setStretchMode(GridView.NO_STRETCH);
			// } else {
			// viewCache.getmYueDetailTusSV().setVisibility(View.GONE);
			// viewCache.getmYueDetailTusGV().setVisibility(View.GONE);
			// }

			List<YueTuItem> tus = item.getTus();
			if (tus.size() > 0) {
				viewCache.getmYueDetailTusLL().setVisibility(View.VISIBLE);
				final ArrayList<String> tuUrls = new ArrayList<String>();
				for (YueTuItem tu : tus) {
					tuUrls.add(tu.getUrl());
				}
				int totalWidth = item.getTus().size() * mTuWidth
						+ mContext.getResources().getDimensionPixelSize(R.dimen.quan_tu_interval) * (tuUrls.size() - 1);
				FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(totalWidth, mTuHeight);
				viewCache.getmYueDetailTusInnerLL().setLayoutParams(params);

				viewCache.getmYueDetailTusInnerLL().removeAllViews();
				final LinearLayout yueDetailTusInnerLL = viewCache.getmYueDetailTusInnerLL();
				for (int i = 0; i < tus.size(); i++) {
					final ImageView tuIV = new ImageView(mContext);
					tuIV.setAdjustViewBounds(true);
					tuIV.setScaleType(ScaleType.CENTER_INSIDE);
					LinearLayout.LayoutParams iv_params;
					if (tus.size() == 1) {
						iv_params = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
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
//							zoomImageFromThumb(arg0, tuUrls, yueDetailTusInnerLL, viewPagerPosition);

							Intent intent = new Intent(mContext,VehicleQuanVewPager.class);
							intent.putExtra("viewPagerPosition",viewPagerPosition+"");
							intent.putStringArrayListExtra("url",tuUrls);
							mContext.startActivity(intent);
						}
					});
					yueDetailTusInnerLL.addView(tuIV);
				}

			} else {
				viewCache.getmYueDetailTusLL().setVisibility(View.GONE);
			}

			// 显示赞列表
			if (item.getYuezancnt() > 0) {
				viewCache.getmDetailZanerLL().setVisibility(View.VISIBLE);
				int columWidth = mContext.getResources().getDimensionPixelSize(R.dimen.yue_zan_avatar_width);
				int numColumns = item.getYuezancnt();
				int totalWidth = numColumns * (columWidth
						+ 2 * mContext.getResources().getDimensionPixelSize(R.dimen.quan_zan_avatar_padding));
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(totalWidth,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				viewCache.getmDetailZanerGV().setLayoutParams(params);
				viewCache.getmDetailZanerGV().setNumColumns(numColumns);
				viewCache.getmDetailZanerGV().setColumnWidth(columWidth);

				viewCache.getmDetailZanerGV().setStretchMode(GridView.NO_STRETCH);
				YueZanAdapter yueZanAdapter = null;
				if (item.getYuezancnt() < 6) {
					yueZanAdapter = new YueZanAdapter(mContext, item.getYuezan());
					// viewCache.getmDetailZanerMoreTV().setVisibility(View.GONE);
				} else {
					yueZanAdapter = new YueZanAdapter(mContext, item.getYuezan().subList(0, 6));
					// viewCache.getmDetailZanerMoreTV().setVisibility(View.VISIBLE);
				}
				viewCache.getmDetailZanerGV().setAdapter(yueZanAdapter);
				viewCache.getmDetailZanerGV().setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						Toast.makeText(mContext, "没做处理", Toast.LENGTH_SHORT).show();
					}

				});
			} else {
				viewCache.getmDetailZanerLL().setVisibility(View.GONE);
			}
			if (item.getYuepingcnt() > 0) {
				viewCache.getmDetailPingTitleTV().setVisibility(View.VISIBLE);
				// viewCache.getmDetailPingTitleTV().setText(mContext.getString(R.string.tie_ping_title,
				// item.getTiepingcnt()));
				viewCache.getmDetailPingTitleTV().setText(mContext.getString(R.string.tie_ping_title));
			} else {
				viewCache.getmDetailPingTitleTV().setVisibility(View.GONE);
			}
			viewCache.getmDetailZanerLL().setOnClickListener(this);

			if (Integer.parseInt(item.getZaned()) == 1) {
				viewCache.getmDetailZanTV().setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconheart, 0, 0, 0);
			} else {
				viewCache.getmDetailZanTV().setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconhear, 0, 0, 0);
			}

			viewCache.getmDetailEditIV().setOnClickListener(this);
			viewCache.getmDetailEditIV().setTag(position_integer);

		} else {

			YuePingItemViewCache viewCache = null;
			if (convertView == null || (convertView.getTag()) instanceof YueHeaderEntityViewCache) {
				convertView = mInflater.inflate(R.layout.yue_ping_item, null);
				viewCache = new YuePingItemViewCache(convertView);
				convertView.setTag(viewCache);
			} else {
				viewCache = (YuePingItemViewCache) convertView.getTag();
			}

			YuePingItem item = (YuePingItem) mData.get(position);
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

			viewCache.getmPingContentTV()
					.setText(ChatInputUtils.displayBigFacePic(mContext, item.getContent(), mFaceCharacterMap));
			try {
				Date date = new SimpleDateFormat("yyyyMMddHHmmss").parse(item.getPublishTime());
				viewCache.getmPingDateTV().setText(new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return convertView;
	}

	@Override
	public void onClick(View v) {
		Message msg;
		Object tag = v.getTag();
		Integer position_integer = null;
		if (tag instanceof Integer) {
			position_integer = (Integer) tag;
		}
		switch (v.getId()) {
		case R.id.yue_detail_avatar_iv:
			// 进入到个人详情中
			YueDetailHeaderEntity header = (YueDetailHeaderEntity) mData.get(position_integer.intValue());
			Intent header_intent = new Intent(mContext, PersonalHomeActivity2.class);
			header_intent.putExtra("userId", header.getChiefId());
			mContext.startActivity(header_intent);
			break;
		// case R.id.yue_detail_content_more_tv:
		// // 文本展开
		// break;
		case R.id.yue_detail_ping_tv:
			// 发消息给Activity去弹出评价框和键盘
			msg = new Message();
			msg.what = YueDetailActivity.PING_YUE;
			mHandler.sendMessage(msg);
			break;
		case R.id.yue_detail_zan_tv:
			// 切换赞状态
			msg = new Message();
			msg.what = YueDetailActivity.TOGGLE_ZAN;
			mHandler.sendMessage(msg);
			break;
		case R.id.hot_tie_detail_edit_iv:
			// 切换赞状态
			msg = new Message();
			msg.what = YueDetailActivity.EDIT_TIE;
			mHandler.sendMessage(msg);
			break;
		case R.id.yue_detail_edit_iv:// 三个点
			msg = new Message();
			msg.what = YueDetailActivity.EDIT_TIE;
			mHandler.sendMessage(msg);
			break;
		case R.id.ping_avatar_iv:
			// 进入到个人详情中
			YuePingItem ping = (YuePingItem) mData.get(position_integer.intValue());
			Intent ping_intent = new Intent(mContext, PersonalHomeActivity2.class);
			ping_intent.putExtra("userId", Integer.parseInt(ping.getFriendId()));
			mContext.startActivity(ping_intent);
			break;
		case R.id.ping_reply_tv:
			msg = new Message();
			YuePingItem item = (YuePingItem) mData.get(position_integer.intValue());
			if (item.getFriendId().equals(CacheTools.getUserData("userId"))) {
				msg.what = YueDetailActivity.DELETE_PING;
			} else {
				msg.what = YueDetailActivity.REPLY_PING;
			}
			msg.obj = item;
			mHandler.sendMessage(msg);
			break;
		case R.id.yue_detail_involver_ll:
			Intent intent = new Intent(mContext, ZanOrInvolverActivity.class);
			intent.setAction("com.chewuwuyou.app.show_yue_involver");
			Bundle bundle = new Bundle();
			bundle.putSerializable("yue_detail_header", (YueDetailHeaderEntity) mData.get(0));
			intent.putExtras(bundle);
			mContext.startActivity(intent);
			break;
		case R.id.yue_detail_zaner_ll:
			Intent intent2 = new Intent(mContext, ZanOrInvolverActivity.class);
			intent2.setAction("com.chewuwuyou.app.show_yue_zaner");
			Bundle bundle2 = new Bundle();
			bundle2.putSerializable("yue_detail_header", (YueDetailHeaderEntity) mData.get(0));
			intent2.putExtras(bundle2);
			mContext.startActivity(intent2);
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
					Intent intent = new Intent(mContext, VehicleQuanVewPager.class);
					intent.putStringArrayListExtra("url",mTuData);
					intent.putExtra("viewPagerPosition",position+"");

					mContext.startActivity(intent);
					//zoomImageFromThumb(view, mTuData, mTusGV, position);
				}
			});

			return convertView;
		}
	}

	private SpannableString displayPingName(YuePingItem ping) {
		StringBuilder sb = new StringBuilder();
		SpannableString ssb = null;
		String name = CarFriendQuanUtils.showCarFriendName(ping);
		YuePingReplyItem toWho = ping.getToWho();

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
				ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#5b6e82")), 0, name.length(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				ssb.setSpan(new ClickSpan(Integer.parseInt(ping.getFriendId())), 0, name.length(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (NotFoundException e) {
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
