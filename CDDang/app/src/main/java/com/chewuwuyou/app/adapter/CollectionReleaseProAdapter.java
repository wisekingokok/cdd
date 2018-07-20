package com.chewuwuyou.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.ServicePro;

public class CollectionReleaseProAdapter extends BaseAdapter {
	// private List<String> mList;
	private List<ServicePro> mServicePros;
	private Context mContext;
	// 状态标志位
	private int clickTemp = -1;

	// 标识选择的Item
	public void setSeclection(int position) {
		clickTemp = position;
	}

	public CollectionReleaseProAdapter(List<ServicePro> servicePros, Context mContext) {
		super();
		this.mServicePros = servicePros;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		return mServicePros.size();
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
				R.layout.release_pro_item, null);
		TextView tv = (TextView) view.findViewById(R.id.text_release);
		tv.setText(mServicePros.get(position).getProjectName());
		ImageView checkIV = (ImageView) view.findViewById(R.id.image_right);
		// 选中时，改变背景色
		if (clickTemp == position) {
			// tv.setLayoutParams(btnLayout);
			tv.setBackgroundDrawable(mContext.getResources().getDrawable(
					R.drawable.service_pro_pressed));
			checkIV.setVisibility(View.VISIBLE);
		} else {
			// layout.setBackgroundResource(R.drawable.gradient_box);
			tv.setBackgroundDrawable(mContext.getResources().getDrawable(
					R.drawable.service_pro_normal));
			checkIV.setVisibility(View.GONE);
		}
		return view;
	}
}