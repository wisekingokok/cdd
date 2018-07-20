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
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:我的秘书
 * @author:yuyong
 * @date:2015-9-25下午4:23:53
 * @version:1.2.1
 */
public class MyWorkAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater layoutInflater;
	private List<String> mWorks;
	private List<String> mWorkNums;

	public MyWorkAdapter(Context context, List<String> works,
			List<String> workNums) {
		this.mContext = context;
		this.mWorks = works;
		this.mWorkNums = workNums;
		layoutInflater = LayoutInflater.from(mContext);
	}

	public class ListItemView {
		TextView mWorkContentTV;
		TextView mWorkNumberTV;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mWorks.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mWorks.get(position);
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
			convertView = layoutInflater.inflate(R.layout.work_item, null);
			listItemView.mWorkContentTV = (TextView) convertView
					.findViewById(R.id.work_content_tv);
			listItemView.mWorkNumberTV = (TextView) convertView
					.findViewById(R.id.work_number_tv);
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		if (mWorks != null && !mWorks.isEmpty()) {
			listItemView.mWorkContentTV.setText(mWorks.get(position));
		}

		if (mWorkNums != null && !mWorkNums.isEmpty()) {
			listItemView.mWorkNumberTV.setText(mWorkNums.get(position));
		}
		return convertView;
	}

}
