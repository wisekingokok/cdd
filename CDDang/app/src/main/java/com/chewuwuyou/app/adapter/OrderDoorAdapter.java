package com.chewuwuyou.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.DoorOrderAdapter;

/**
 * @describe:板块Adapter
 * @author:liuchun
 */
public class OrderDoorAdapter extends BaseAdapter {

	private List<DoorOrderAdapter> mList;
	private LayoutInflater mInflater;
	private Context mContext;
	public OrderDoorAdapter(Context context,List<DoorOrderAdapter> mList) {
		this.mList = mList;
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		BanKuaiViewCache viewCache;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.order_already_ft_item, null);
			viewCache = new BanKuaiViewCache();
			viewCache.orderType = (TextView) convertView.findViewById(R.id.order_type);
			viewCache.orderMondel = (TextView) convertView.findViewById(R.id.order_mondel);
			viewCache.orderName = (TextView) convertView.findViewById(R.id.order_name);
			viewCache.orderTime = (TextView) convertView.findViewById(R.id.order_time);
			convertView.setTag(viewCache);
		} else {
			viewCache = (BanKuaiViewCache) convertView.getTag();
		}
		viewCache.orderType.setText(mList.get(position).getOrderType());
		viewCache.orderMondel.setText(mList.get(position).getOrderMoney());
		viewCache.orderName.setText(mList.get(position).getOrderName());
		viewCache.orderTime.setText(mList.get(position).getOrderDate() +""+mList.get(position).getOrderTime());
		return convertView;
	}

	class BanKuaiViewCache {
		TextView orderType,orderMondel,orderName,orderTime;
	}
}
