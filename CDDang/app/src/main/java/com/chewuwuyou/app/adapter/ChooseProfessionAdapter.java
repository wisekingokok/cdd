package com.chewuwuyou.app.adapter;

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
 * @Description:选择职业
 * @author:yuyong
 * @date:2015-7-14下午3:56:16
 * @version:1.2.1
 */
public class ChooseProfessionAdapter extends BaseAdapter {

	private String[] mProfessions;
	private String[] mIndustrys;
	private Integer[] mIndustryBgColors;
	private LayoutInflater mLayoutInflater;
	private Context mContext;

	public ChooseProfessionAdapter(Context context, String[] professions,
			String[] industrys, Integer[] industryBgColors) {
		// TODO Auto-generated constructor stub
		this.mProfessions = professions;
		this.mIndustrys = industrys;
		this.mIndustryBgColors = industryBgColors;
		this.mContext = context;
		mLayoutInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mProfessions.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mProfessions[position];
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
			convertView = mLayoutInflater.inflate(
					R.layout.choose_profession_item, null);
			listItemView.mProfessionTV = (TextView) convertView
					.findViewById(R.id.profession_tv);
			listItemView.mIndustryTV = (TextView) convertView
					.findViewById(R.id.industry_tv);
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		listItemView.mProfessionTV.setText(mProfessions[position]);
		listItemView.mIndustryTV.setText(mIndustrys[position]);
		listItemView.mIndustryTV.setBackgroundResource(mIndustryBgColors[position]);

		return convertView;
	}

	public class ListItemView {
		public TextView mProfessionTV;
		public TextView mIndustryTV;
	}

}
