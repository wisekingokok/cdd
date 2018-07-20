package com.chewuwuyou.app.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.CarProvinceItem;
import com.chewuwuyou.app.viewcache.CarProvinceItemViewCache;

public class CarPlateProvinceAdapter extends BaseAdapter {

	
	private Context mContext;
	private List<CarProvinceItem> mData;
	private LayoutInflater mInflater;

	public CarPlateProvinceAdapter(Context context, List<CarProvinceItem> data) {
		this.mData = data;
		this.mContext = context;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		CarProvinceItemViewCache viewCache = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.car_plate_province_item, null);
			viewCache = new CarProvinceItemViewCache(convertView);
			convertView.setTag(viewCache);
		} else {
			viewCache = (CarProvinceItemViewCache) convertView.getTag();
		}
		final CarProvinceItem item = mData.get(position);
		if (item != null) {
			viewCache.getmProvinceShortNameTV().setText(item.getProvince_short_name());
			viewCache.getmProvinceNameTV().setText(item.getProvince_name());

			return convertView;
		}
		return null;
	}
}
