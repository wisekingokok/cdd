package com.chewuwuyou.app.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.Ordertime;
import com.chewuwuyou.app.utils.OrderStateUtil;

public class TimelineAdapter extends BaseAdapter {

	private Context mContext;
	private List<Ordertime> mList;
	private LayoutInflater inflater;

	public TimelineAdapter(Context context, List<Ordertime> list) {
		super();
		this.mContext = context;
		this.mList = list;
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {

		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_time_shaft_item, null);
			viewHolder = new ViewHolder();
			viewHolder.time = (TextView) convertView
					.findViewById(R.id.text_time);
			viewHolder.timetype = (TextView) convertView
					.findViewById(R.id.text_time_type);
			viewHolder.ImageTimeLogo = (ImageView) convertView
					.findViewById(R.id.image_time_logo);
			viewHolder.mView = (TextView) convertView.findViewById(R.id.view);
			viewHolder.relative = (RelativeLayout) convertView
					.findViewById(R.id.relative);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String time = mList.get(position).getTime();
		viewHolder.time.setText(time.substring(0, 19));
		if (!TextUtils.isEmpty(mList.get(position).getOrderresult())) {
			viewHolder.timetype.setText(OrderStateUtil.orderStateTime(
					Integer.parseInt(mList.get(position).getOrderresulttype()),
					Integer.parseInt(mList.get(position).getOrderresult())));
		} else {
			viewHolder.timetype.setText(OrderStateUtil.orderStateTime(
					Integer.parseInt(mList.get(position).getOrderresulttype()),
					0));
		}

		if (mList.size() - 1 == position) {
			viewHolder.ImageTimeLogo
					.setImageResource(R.drawable.order_state_display);
			viewHolder.mView.setVisibility(View.GONE);
		} else {
			viewHolder.ImageTimeLogo
					.setImageResource(R.drawable.order_round_white);
			viewHolder.mView.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	class ViewHolder {
		TextView timetype;
		TextView time;
		TextView date;
		TextView resulttype;
		TextView result;
		RelativeLayout relative;
		ImageView ImageTimeLogo;// 订单状态
		TextView mView;// 横线
	}

}