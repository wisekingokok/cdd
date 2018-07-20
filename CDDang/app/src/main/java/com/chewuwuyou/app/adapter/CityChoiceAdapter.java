package com.chewuwuyou.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chewuwuyou.app.R;

/**
 * @describe:板块Adapter
 * @author:XH
 * @version
 * @created:
 */
public class CityChoiceAdapter extends BaseAdapter {

	private List<String> mCityLIst;
	private Context mContext;
	public int index;


	public CityChoiceAdapter(Context mContext, List<String> mCityLIst) {
		this.mContext = mContext;
		this.mCityLIst = mCityLIst;

	}

	@Override
	public int getCount() {
		return mCityLIst.size();
	}

	@Override
	public Object getItem(int position) {
		return mCityLIst.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		City city = null;
		if (convertView == null) {
			city = new City();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.select_city_item, null);
			city.cityValue = (TextView) convertView
					.findViewById(R.id.textValue);

			convertView.setTag(city);
		} else {
			city = (City) convertView.getTag();
		}
		city.cityValue.setText(mCityLIst.get(position));
	
		return convertView;
	}

	public class City {
		TextView cityValue;
	}

}
