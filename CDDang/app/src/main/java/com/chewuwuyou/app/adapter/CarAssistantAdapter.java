package com.chewuwuyou.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.CarAssistantServiceItem;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:行车助手adapter
 * @author:yuyong
 * @date:2015-5-16下午3:46:51
 * @version:1.2.1
 */
public class CarAssistantAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater layoutInflater;
	private List<CarAssistantServiceItem> mCaItems;

	public CarAssistantAdapter(Context context,
			List<CarAssistantServiceItem> mCaItems) {
		this.mContext = context;
		this.mCaItems = mCaItems;
		layoutInflater = LayoutInflater.from(mContext);
	}

	public class ListItemView {
		ImageView mCarAssistantIV;
		TextView mCarAssistantTV;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mCaItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mCaItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ListItemView listItemView = null;
		if (convertView == null) {
			listItemView = new ListItemView();
			convertView = layoutInflater.inflate(R.layout.car_assistant_item,null);
			listItemView.mCarAssistantIV = (ImageView) convertView
					.findViewById(R.id.car_service_iv);
			listItemView.mCarAssistantTV = (TextView) convertView
					.findViewById(R.id.car_service_tv);
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		listItemView.mCarAssistantIV.setImageResource(mCaItems.get(position)
				.getImgResId());
		listItemView.mCarAssistantTV
				.setText(mCaItems.get(position).getNameTV());
		return convertView;
	}
}
