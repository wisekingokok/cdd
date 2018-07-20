package com.chewuwuyou.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.ServicePro;

public class MyGridAdapter extends BaseAdapter {
	// private List<String> mList;
	private List<ServicePro> mServicePros;
	private Context mContext;
	// 状态标志位
	private int clickTemp = -1;

	// 标识选择的Item
	public void setSeclection(int position) {
		clickTemp = position;
	}

	public MyGridAdapter(List<ServicePro> servicePros, Context mContext) {
		super();
		this.mServicePros = servicePros;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mServicePros.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {

		LinearLayout view = (LinearLayout) LinearLayout.inflate(mContext,
				R.layout.release_order_item, null);
		TextView tv = (TextView) view.findViewById(R.id.tv);
		tv.setText(mServicePros.get(position).getProjectName());
		// 选中时，改变背景色
		if (clickTemp == position) {
			tv.setBackgroundDrawable(mContext.getResources().getDrawable(
					R.drawable.message_proname_z));
			tv.setTextColor(mContext.getResources().getColor(
					R.color.login_text_bg));
		} else {
			// layout.setBackgroundResource(R.drawable.gradient_box);
			tv.setBackgroundDrawable(mContext.getResources().getDrawable(
					R.drawable.message_proname_x));
			tv.setTextColor(mContext.getResources().getColor(
					R.color.common_input_text_color));
		}
		return view;
	}
}
