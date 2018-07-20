package com.chewuwuyou.app.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.PopItemBean;

public class PopGridAdapter extends BaseAdapter {
	private Context context;
	private List<PopItemBean> list;

	public PopGridAdapter(Context context) {
		this.context = context;
		list = new ArrayList<PopItemBean>();
	}

	public PopGridAdapter(Context context, List<PopItemBean> list) {
		this.context = context;
		this.list = list;
	}

	public void setList(List<PopItemBean> list) {
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
		PopItemBean info = list.get(position);
		vh.btn.setText(info.name);
		vh.btn.setBackgroundResource(info.resId);
		return convertView;
	}

	class ViewHolder {
		TextView btn;
	}

}
