package com.chewuwuyou.app.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;

public class BalanceTimeGridAdapter extends BaseAdapter {

	private List<Map<String, Object>> listItems;
	private Context mContext;
	// 状态标志位
	private int clickTemp = -1;

	// 标识选择的Item
	public void setSeclection(int position) {
		clickTemp = position;
	}

	public BalanceTimeGridAdapter(List<Map<String, Object>> listItems,
			Context mContext, int clickTemp) {
		super();
		this.listItems = listItems;
		this.mContext = mContext;
		this.clickTemp = clickTemp;
	}

	@Override
	public int getCount() {
		return listItems.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {

		LinearLayout view = (LinearLayout) LinearLayout.inflate(mContext,
				R.layout.balance_grid_item, null);
		TextView tv = (TextView) view.findViewById(R.id.text);

		tv.setText((String) listItems.get(position).get("timetype"));

		// 选中时，改变背景色

		if (clickTemp == position) {
			tv.setBackgroundDrawable(mContext.getResources().getDrawable(
					R.drawable.balance_blue));
			tv.setTextColor(mContext.getResources().getColor(
					R.color.login_text_bg));
		} else {
			// layout.setBackgroundResource(R.drawable.gradient_box);
			tv.setBackgroundDrawable(mContext.getResources().getDrawable(
					R.drawable.balance_gray));
			tv.setTextColor(mContext.getResources().getColor(
					R.color.balance_black));
		}

		return view;
	}
}
