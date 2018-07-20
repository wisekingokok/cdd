package com.chewuwuyou.app.adapter;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.CityYI;
import com.chewuwuyou.app.utils.Constant;

public class CityAdaptSan extends BaseAdapter {

	private Context mContext;
	private List<CityYI> mCities;
	private Handler mHandler;
	private boolean isSelected = false;// 是否选中
	private boolean isDisplay = false;// 判断是否显示隐藏区
	private int selectIndex;// 判断点钟的行数
	private LinearLayout mDistrictLinear;

	public CityAdaptSan(Context mContext, List<CityYI> mCities,
			Handler mHandler, LinearLayout mDistrictLinear) {
		this.mContext = mContext;
		this.mCities = mCities;
		this.mHandler = mHandler;
		this.mDistrictLinear = mDistrictLinear;

	}

	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * 
	 * @param list
	 */
	public void updateListView(List<CityYI> mCities) {
		this.mCities = mCities;
		this.isSelected = false;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mCities.size();
	}

	@Override
	public CityYI getItem(int position) {
		return mCities.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		CityViewHolder holder = null;
		if (view == null) {
			view = LayoutInflater.from(mContext).inflate(
					R.layout.item_city_listview, null);
			holder = new CityViewHolder();
			holder.letter = (TextView) view
					.findViewById(R.id.tv_item_city_listview_letter);// 显示字母
			holder.name = (TextView) view
					.findViewById(R.id.tv_item_city_listview_name);// 显示城市名称
			view.setTag(holder);
		} else {
			holder = (CityViewHolder) view.getTag();
		}

		// 根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);

		// 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if (position == getPositionForSection(section)) {
			holder.letter.setVisibility(View.VISIBLE);
			holder.letter.setText(mCities.get(position).getCityPinYin());
		} else {
			holder.letter.setVisibility(View.GONE);
		}

		if (isSelected == true) {
			if (selectIndex == position) {
				holder.name.setBackgroundResource(R.color.choice_city);
			} else {
				holder.name.setBackgroundResource(R.color.white);
			}
		} else {
			if (selectIndex == position) {
				holder.name.setBackgroundResource(R.color.white);
			}
		}
		holder.name.setText(mCities.get(position).getCityName());

		holder.name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (selectIndex == position) {
					if (isDisplay == false) {
						mDistrictLinear.setVisibility(View.VISIBLE);
						selectIndex = position;
						isDisplay = true;
						isSelected = true;
						Message message = new Message();
						message.what = Constant.SEND_ADAPTER;
						Bundle bundle = new Bundle();
						bundle.putString("cityId", String.valueOf(mCities.get(position).getCityId()));
						bundle.putString("city", mCities.get(position)
								.getCityName());
						bundle.putString("province", mCities.get(position)
								.getProvinceName());
						bundle.putString("provinceId", String.valueOf(mCities
								.get(position).getProvinceId()));
						message.setData(bundle);// bundle传值，耗时，效率低
						mHandler.sendMessage(message);
					} else if (isDisplay == true) {
						mDistrictLinear.setVisibility(View.GONE);
						isDisplay = false;
						isSelected = false;
					}
				} else {
					mDistrictLinear.setVisibility(View.VISIBLE);
					selectIndex = position;
					isDisplay = true;
					isSelected = true;
					Message message = new Message();
					message.what = Constant.SEND_ADAPTER;
					Bundle bundle = new Bundle();
					bundle.putString("cityId",
							String.valueOf(mCities.get(position).getCityId()));
					bundle.putString("city", mCities.get(position)
							.getCityName());
					bundle.putString("province", mCities.get(position)
							.getProvinceName());
					bundle.putString("provinceId", String.valueOf(mCities.get(
							position).getProvinceId()));
					message.setData(bundle);// bundle传值，耗时，效率低
					mHandler.sendMessage(message);
				}
				notifyDataSetChanged();
			}
		});
		return view;
	}

	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return mCities.get(position).getCityPinYin().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = mCities.get(i).getCityPinYin();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if(firstChar!=section){
				
			}else{
				return i;
			}
		}
		return -1;
	}

	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 * 
	 * @param str
	 * @return
	 */
	// private String getAlpha(String str) {
	// String sortStr = str.trim().substring(0, 1).toUpperCase();
	// // 正则表达式，判断首字母是否是英文字母
	// if (sortStr.matches("[A-Z]")) {
	// return sortStr;
	// } else {
	// return "#";
	// }
	// }

	public class CityViewHolder {
		TextView letter;
		TextView name;
	}
}
