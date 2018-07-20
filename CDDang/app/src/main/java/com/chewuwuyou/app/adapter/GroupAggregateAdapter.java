package com.chewuwuyou.app.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.Userfriend;
import com.chewuwuyou.app.utils.ImageUtils;

import java.util.List;

/**
 * @describe:提现账户列表adapter
 * @author:liuchun
 * @version
 * @created:
 */
public class GroupAggregateAdapter extends BaseAdapter {

	private List<Userfriend> mAccountList;
	private Context mContext;
	private LayoutInflater mInflater;


	public GroupAggregateAdapter(Context context, List<Userfriend> mAccountList) {
		this.mContext = context;
		this.mAccountList = mAccountList;
		this.mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mAccountList.size();
	}

	@Override
	public Object getItem(int position) {
		return mAccountList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		BanKuaiViewCache viewCache;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_group_list, null);
			viewCache = new BanKuaiViewCache();
			viewCache.mGroupHeadPortrait = (ImageView) convertView.findViewById(R.id.group_head_portrait);
			viewCache.mGroupHeadName = (TextView) convertView.findViewById(R.id.group_head_name);
			viewCache.mGroupNumber = (TextView) convertView.findViewById(R.id.group_number);
			convertView.setTag(viewCache);
		} else {
			viewCache = (BanKuaiViewCache) convertView.getTag();
		}
		viewCache.mGroupNumber.setVisibility(View.GONE);
		if(!TextUtils.isEmpty(mAccountList.get(position).getNickname())){
			viewCache.mGroupHeadName.setText(mAccountList.get(position).getNickname());
		}else{
			viewCache.mGroupHeadName.setText(mAccountList.get(position).getName());
		}
		if(!TextUtils.isEmpty(mAccountList.get(position).getPortraitUri())){
			ImageUtils.displayImage(mAccountList.get(position).getPortraitUri(), viewCache.mGroupHeadPortrait, 0);
		}
		return convertView;
	}

	class BanKuaiViewCache {
		private ImageView mGroupHeadPortrait;
		private TextView mGroupHeadName,mGroupNumber;
	}
}
