package com.chewuwuyou.app.adapter;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.InputOrderType;

/**
 * @describe:板块Adapter
 * @author:liuchun
 * @version
 * @created:
 */
public class StoreRecordAdapter extends BaseAdapter {

	private List<InputOrderType> mAccountList;
	private Context mContext;
	private LayoutInflater mInflater;
	public StoreRecordAdapter(Context context,List<InputOrderType> mAccountList) {
		this.mContext = context;
		this.mAccountList = mAccountList;
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mAccountList.size();
	}

	@Override
	public Object getItem(int position) {
		return mAccountList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		BanKuaiViewCache viewCache;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.store_record_item, null);
			viewCache = new BanKuaiViewCache();
			viewCache.storeTypeItem = (TextView) convertView.findViewById(R.id.store_type_item);
			convertView.setTag(viewCache);
		} else {
			viewCache = (BanKuaiViewCache) convertView.getTag();
		}

		if(mAccountList.get(position).isSelected() == true){
			viewCache.storeTypeItem.setTextColor(mContext.getResources().getColor(R.color.white));
			Resources res = mContext.getResources(); //resource handle
			Drawable drawable = res.getDrawable(R.drawable.common_store_type); 
			viewCache.storeTypeItem.setBackgroundDrawable(drawable);
			viewCache.storeTypeItem.setText(mAccountList.get(position).getType());
		}else{
			viewCache.storeTypeItem.setTextColor(mContext.getResources().getColor(R.color.black));
			Resources res = mContext.getResources(); //resource handle
			Drawable drawable = res.getDrawable(R.drawable.common_item_drawable); 
			viewCache.storeTypeItem.setBackgroundDrawable(drawable);
			viewCache.storeTypeItem.setText(mAccountList.get(position).getType());
		}
		return convertView;
	}

	class BanKuaiViewCache {
		TextView storeTypeItem;
	}
}
