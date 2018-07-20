package com.chewuwuyou.app.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.CarAssistantServiceItem;
import com.chewuwuyou.app.bean.ServiceAdministration;
import com.chewuwuyou.app.bean.ServicePro;

import java.util.List;

public class ServiceProAdapter extends BaseAdapter {
	// private List<String> mList;
	private List<ServiceAdministration> mServicePros;
	private Context mContext;
	private LayoutInflater layoutInflater;

	public ServiceProAdapter(Context context,List<ServiceAdministration> mServicePros) {
		this.mContext = context;
		this.mServicePros = mServicePros;
		layoutInflater = LayoutInflater.from(mContext);
	}

	public class ListItemView {
		ImageView mCarAssistantIV;
		TextView mCarAssistantTV;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mServicePros.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mServicePros.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ListItemView listItemView = null;
		if (convertView == null) {
			listItemView = new ListItemView();
			convertView = layoutInflater.inflate(R.layout.release_pro_item,null);
			listItemView.mCarAssistantIV = (ImageView) convertView
					.findViewById(R.id.image_right);
			listItemView.mCarAssistantTV = (TextView) convertView
					.findViewById(R.id.text_release);
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		listItemView.mCarAssistantTV.setText(mServicePros.get(position).getProjectName());
		if(mServicePros.get(position).ismBoolea() == true){
			listItemView.mCarAssistantIV.setVisibility(View.VISIBLE);
			listItemView.mCarAssistantTV.setBackgroundDrawable(mContext.getResources().getDrawable(
					R.drawable.service_pro_pressed));
		}else{
			listItemView.mCarAssistantIV.setVisibility(View.GONE);
			listItemView.mCarAssistantTV.setBackgroundDrawable(mContext.getResources().getDrawable(
					R.drawable.service_pro_normal));
		}
		return convertView;
	}
}