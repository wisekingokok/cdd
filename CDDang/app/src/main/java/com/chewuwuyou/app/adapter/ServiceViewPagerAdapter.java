package com.chewuwuyou.app.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.Banner;
import com.chewuwuyou.app.ui.BannerToActivity;
import com.chewuwuyou.app.utils.ImageUtils;
import com.jakewharton.salvage.RecyclingPagerAdapter;

/**
 * 服务viewpager循环
 * 
 * @author liuchun
 * 
 */
public class ServiceViewPagerAdapter extends RecyclingPagerAdapter {

	private Context mContext;
	private List<Banner> mData;
	private LayoutInflater layoutInflater;
	private boolean mIsInfiniteLoop;

	public ServiceViewPagerAdapter(Context context, List<Banner> data) {
		this.mContext = context;
		this.mData = data;
		layoutInflater = LayoutInflater.from(mContext);
		mIsInfiniteLoop = false;
	}

	@Override
	public int getCount() {
		// Infinite loop
		return mIsInfiniteLoop ? Integer.MAX_VALUE : mData.size();
	}

	/**
	 * get really position
	 * 
	 * @param position
	 * @return
	 */
	// private int getPosition(int position) {
	// return mIsInfiniteLoop ? position % mSize : position;
	// }

	@Override
	public View getView(final int position, View convertView, ViewGroup container) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.rim_activtiy_banner, null);
			holder.imageView = (ImageView) convertView.findViewById(R.id.rim_banner_id);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ImageUtils.displayImage(mData.get(position).getImageUrl(),holder.imageView, 0, R.drawable.default_baise,R.drawable.default_baise);
		holder.imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO 通过tiaoType判断跳转网页还是跳转功能页面
				if (mData.get(position).getTiaoType()==1&&!TextUtils.isEmpty(mData.get(position).getTiaoUrl())) {
					Intent intent = new Intent(mContext, BannerToActivity.class);
					intent.putExtra("loadUrl", mData.get(position).getTiaoUrl());
					mContext.startActivity(intent);
				}
			}
		});
	    return convertView;
	}

	private static class ViewHolder {
		ImageView imageView;
	}

	/**
	 * @return the isInfiniteLoop
	 */
	public boolean isInfiniteLoop() {
		return mIsInfiniteLoop;
	}

	/**
	 * @param isInfiniteLoop
	 *            the isInfiniteLoop to set
	 */
	public ServiceViewPagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
		this.mIsInfiniteLoop = isInfiniteLoop;
		return this;
	}
}
