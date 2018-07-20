package com.chewuwuyou.app.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.YueInvolverItem;
import com.chewuwuyou.app.ui.PersonalHomeActivity2;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.viewcache.TieZanItemViewCache;

public class YueInvolverAdapter extends BaseAdapter {

	private List<YueInvolverItem> mData;
	private Context mContext;
	private LayoutInflater mInflater;

	public YueInvolverAdapter(Context context, List<YueInvolverItem> data) {
		this.mContext = context;
		this.mData = data;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		TieZanItemViewCache viewCache = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.tie_zan_item, null);
			viewCache = new TieZanItemViewCache(convertView);
			convertView.setTag(viewCache);
		} else {
			viewCache = (TieZanItemViewCache) convertView.getTag();
		}
		final YueInvolverItem item = mData.get(position);

		ImageUtils.displayImage(item.getUrl(), viewCache.getAvatarIV(), 360, R.drawable.user_yuan_icon,R.drawable.user_fang_icon);
		viewCache.getAvatarIV().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, PersonalHomeActivity2.class);
				intent.putExtra("userId", item.getId());
				mContext.startActivity(intent);
			}
		});
		return convertView;
	}
}
