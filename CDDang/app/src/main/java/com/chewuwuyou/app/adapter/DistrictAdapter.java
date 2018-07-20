package com.chewuwuyou.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.LatelyCity;

/**
 * @describe:板块Adapter
 * @author:liuchun
 * @version
 * @created:
 */
public class DistrictAdapter extends BaseAdapter {

	private List<LatelyCity> mData;
	private Context mContext;

	public DistrictAdapter(Context context, List<LatelyCity> mData) {
		this.mContext = context;
		this.mData = mData;
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
		district dis = null;
		if (convertView == null) {
			dis = new district();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.select_city_item, null);
			dis.cityValue = (TextView) convertView.findViewById(R.id.textValue);

			convertView.setTag(dis);
		} else {
			dis = (district) convertView.getTag();
		}
		dis.cityValue.setText(mData.get(position).getDistrict());
		return convertView;
	}
	public class district {
		TextView cityValue;
	}
}
