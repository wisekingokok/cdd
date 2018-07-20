package com.chewuwuyou.app.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.ServicePro;
import com.chewuwuyou.app.utils.ImageUtils;

public class ServiceTypeGridAdapter extends BaseAdapter {

	private List<ServicePro> mServiceTypeItems;
	private Context mContext;

	public ServiceTypeGridAdapter(List<ServicePro> mServiceTypeItems,
			Context mContext) {

		super();
		this.mServiceTypeItems = mServiceTypeItems;
		this.mContext = mContext;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mServiceTypeItems.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mServiceTypeItems.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		LinearLayout view = (LinearLayout) LinearLayout.inflate(mContext,
				R.layout.service_type_items, null);
		TextView tv = (TextView) view.findViewById(R.id.service_item_text);
		tv.setText((String) mServiceTypeItems.get(arg0).getProjectName());
		ImageView image = (ImageView) view
				.findViewById(R.id.service_item_image);
		ImageUtils.displayImage(mServiceTypeItems.get(arg0).getProjectImg(),
				image, 0);
		return view;
	}

}
