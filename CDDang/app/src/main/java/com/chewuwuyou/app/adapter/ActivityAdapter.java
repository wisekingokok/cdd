package com.chewuwuyou.app.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.YueItem;
import com.chewuwuyou.app.ui.MyYueFragment;
import com.chewuwuyou.app.ui.PersonalHomeActivity2;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.ImageUtils.ScalingLogic;
import com.chewuwuyou.app.viewcache.YueItemViewCache;

/**
 * @describe:活动Adapter
 */
public class ActivityAdapter extends BaseAdapter implements OnClickListener {

	private List<YueItem> mYueData;
	private Handler mHandler;
	private Context context;

	public ActivityAdapter(Context context, List<YueItem> data) {
		this.context = context;
		this.mYueData = data;
	}

	public void setData(List<YueItem> data) {
		this.mYueData = data;
		notifyDataSetChanged();
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
			convertView = LayoutInflater.from(context).inflate(
					R.layout.yue_item2, null);
			viewCache = new YueItemViewCache(convertView);
			convertView.setTag(viewCache);
		} else {
			viewCache = (YueItemViewCache) convertView.getTag();
		}
		// final Integer position_integer = Integer.valueOf(position);
		final YueItem yue = mYueData.get(position);
		if (yue != null) {
			// ImageUtils.displayImage(yue.getTus().get(0).getUrl(),
			// viewCache.getmYueItemInfoIV(), 0);
			if (yue.getTus().size() > 0) {
				ImageUtils.displayImage(
						yue.getTus().get(0).getUrl(),
						viewCache.getmYueItemInfoIV(),
						0,
						context.getResources().getDimensionPixelSize(
								R.dimen.yue_item_info_iv_width),
						context.getResources().getDimensionPixelSize(
								R.dimen.yue_item_info_iv_height),
						ScalingLogic.CROP);
			}
			if (!TextUtils.isEmpty(yue.getTitle())) {
				viewCache.getmYueItemTitleTV().setText(yue.getTitle());
			}
			viewCache.getmYueItemChargeTypeTV().setText(
					yue.getChargeType() == 0 ? "邦主买单"
							: yue.getChargeType() == 1 ? "大伙AA" : "求请客");
			if (!TextUtils.isEmpty(yue.getLocation())) {
				viewCache.getmYueItemLocationTV().setText(yue.getLocation());
			}

			viewCache.getmYueItemInvolveTV().setText(
					new StringBuilder()
							.append(String.valueOf(yue.getInvolve()))
							.append("人感兴趣").toString());
			Date avaliableDate = null;
			try {
				avaliableDate = new SimpleDateFormat("yyyyMMddHHmmss")
						.parse(yue.getAvaliableDate());

			} catch (ParseException e) {
				e.printStackTrace();
			}
			viewCache.getmYueItemAvailableDateTV().setText(
					new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss")
							.format(avaliableDate));

		}
		return convertView;
	}

	@Override
	public void onClick(View v) {
		Message msg;
		Object tag = v.getTag();
		Integer position_integer = null;
		if (tag instanceof Integer)
			position_integer = (Integer) tag;
		final YueItem yue = (YueItem) mYueData.get(position_integer.intValue());
		switch (v.getId()) {
		case R.id.yue_item_avatar_iv:
			// 进入到个人详情中
			Intent intent = new Intent(context, PersonalHomeActivity2.class);
			intent.putExtra("userId", yue.getChiefId());
			context.startActivity(intent);
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
}
