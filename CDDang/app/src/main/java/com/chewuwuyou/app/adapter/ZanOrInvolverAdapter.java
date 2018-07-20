package com.chewuwuyou.app.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.ZanerOrInvolverItem;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.viewcache.ZanerOrInvolverItemViewCache;

public class ZanOrInvolverAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private Context mContext;
	private List<ZanerOrInvolverItem> mData;

	public ZanOrInvolverAdapter(Activity context, List<ZanerOrInvolverItem> data) {
		this.mContext = context;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		this.mData = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ZanerOrInvolverItemViewCache viewCache = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.zaner_or_involver_item, null);
			viewCache = new ZanerOrInvolverItemViewCache(convertView);
			convertView.setTag(viewCache);
		} else {
			viewCache = (ZanerOrInvolverItemViewCache) convertView.getTag();
		}
		final ZanerOrInvolverItem item = mData.get(position);
		if (item != null) {
			ImageUtils.displayImage(item.getUrl(), viewCache.getmZanerItemAvatarIV(), 360, R.drawable.user_yuan_icon, R.drawable.user_yuan_icon);
			viewCache.getmZanerItemNameTV().setText(item.getNick());

			if (item.getSex().equals("0")) {
				viewCache.getmZanerItemSexIV().setImageResource(R.drawable.man);
			} else if (item.getSex().equals("1")) {
				viewCache.getmZanerItemSexIV().setImageResource(R.drawable.woman);
			} else {
				viewCache.getmZanerItemSexIV().setImageResource(R.drawable.icon_nosex);
			}

//			viewCache.getmZanerItemSexIV().setImageResource(item.getSex().equals("1") ? R.drawable.woman : R.drawable.man);
			viewCache.getmZanerItemMoodTV().setText(item.getSignature());
		}
		return convertView;
	}

}
