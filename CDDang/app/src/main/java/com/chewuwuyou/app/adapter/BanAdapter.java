package com.chewuwuyou.app.adapter;

import java.util.ArrayList;
import java.util.List;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.BanKuaiItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BanAdapter extends BaseAdapter {
	private Context context;
	private List<BanKuaiItem> list;

	public BanAdapter(Context context) {
		this.context = context;
		list = new ArrayList<BanKuaiItem>();
	}

	public BanAdapter(Context context, List<BanKuaiItem> list) {
		this.context = context;
		this.list = list;
	}

	public void setList(List<BanKuaiItem> list) {
		if (list == null)
			return;
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.pop_gridview_item, null);
			vh.btn = (TextView) convertView.findViewById(R.id.pet_type_btn);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		BanKuaiItem info = list.get(position);
		vh.btn.setText(info.getTitle());
		int resId = 0;
		switch (position) {
		case 0:
			resId = R.drawable.yue_type_sports_frame;
			break;
		case 1:
			resId = R.drawable.yue_type_majiang_frame;
			break;

		case 2:
			resId = R.drawable.yue_type_movie_frame;
			break;

		case 3:
			resId = R.drawable.yue_type_travell_frame;
			break;

		case 4:
			resId = R.drawable.yue_type_dinner_frame;
			break;
		case 5:
			resId = R.drawable.yue_type_bar_frame;
			break;

		}
		vh.btn.setBackgroundResource(resId);
		return convertView;
	}

	class ViewHolder {
		TextView btn;
	}
	// list.add(new PopItemBean(R.drawable.yue_type_sports_frame, "车展", 0));
	// list.add(new PopItemBean(R.drawable.yue_type_majiang_frame, "自驾", 1));
	// list.add(new PopItemBean(R.drawable.yue_type_movie_frame, "改装", 2));
	// list.add(new PopItemBean(R.drawable.yue_type_travell_frame, "风景", 3));
	// list.add(new PopItemBean(R.drawable.yue_type_dinner_frame, "城市", 4));
	// list.add(new PopItemBean(R.drawable.yue_type_bar_frame, "摩托", 5));
}
