package com.chewuwuyou.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.BusStatis;

/**
 * 省代数据统计
 * 
 * @author yuyong
 * 
 */
public class TradingOrderAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater layoutInflater;
	private List<BusStatis> mBusStatis;

	public final class ListItemView {
		TextView cityName;
		TextView orderNumOne;
		TextView orderNumTwo;
	}

	public TradingOrderAdapter(Context context, List<BusStatis> busStatis) {
		this.mContext = context;

		layoutInflater = LayoutInflater.from(mContext);

		this.mBusStatis = busStatis;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mBusStatis.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mBusStatis.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ListItemView listItemView = null;
		if (convertView == null) {
			listItemView = new ListItemView();
			convertView = layoutInflater.inflate(
					R.layout.province_atistics_item, null);
			listItemView.cityName = (TextView) convertView
					.findViewById(R.id.city_tv);
			listItemView.orderNumOne = (TextView) convertView
					.findViewById(R.id.number_one_tv);
			listItemView.orderNumTwo = (TextView) convertView
					.findViewById(R.id.number_two_tv);
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		// 设置不同的显示颜色
		if (position % 2 == 0) {
			convertView.setBackgroundColor(mContext.getResources().getColor(
					R.color.trading_color));
		} else {
			convertView.setBackgroundColor(mContext.getResources().getColor(
					R.color.white));
		}

		listItemView.cityName.setText(mBusStatis.get(position).getCityName());
		listItemView.orderNumOne.setText(mBusStatis.get(position)
				.getOrderNumOne());
		listItemView.orderNumTwo.setText(mBusStatis.get(position)
				.getOrderNumTwo());

		return convertView;
	}
}
