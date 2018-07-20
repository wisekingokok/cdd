package com.chewuwuyou.app.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.CarCityItem;
import com.chewuwuyou.app.viewcache.CarCityItemViewCache;

public class CarPlateCityAdapter extends BaseAdapter {

	
	private Context mContext;
	private List<CarCityItem> mData;
	private LayoutInflater mInflater;

	public CarPlateCityAdapter(Context context, List<CarCityItem> data) {
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
		CarCityItemViewCache viewCache = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.car_plate_city_item, null);
			viewCache = new CarCityItemViewCache(convertView);
			convertView.setTag(viewCache);
		} else {
			viewCache = (CarCityItemViewCache) convertView.getTag();
		}
		CarCityItem item = mData.get(position);
		if (item != null) {
			viewCache.getmCityHeadTV().setText(item.getCar_head());
			viewCache.getmCityNameTV().setText(item.getCity_name());
			return convertView;
		}
		return null;
	}

}
