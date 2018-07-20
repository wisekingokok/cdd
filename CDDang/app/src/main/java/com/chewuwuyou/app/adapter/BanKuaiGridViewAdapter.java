package com.chewuwuyou.app.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.BanKuaiItem;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.viewcache.BanKuaiViewCache;

/**
 * @describe:板块Adapter
 * @author:XH
 * @version
 * @created:
 */
public class BanKuaiGridViewAdapter extends BaseAdapter {

	private List<BanKuaiItem> mData;
	private Activity mContext;
	private LayoutInflater mInflater;

	public BanKuaiGridViewAdapter(Activity context, List<BanKuaiItem> data) {
		this.mContext = context;
		this.mData = data;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		BanKuaiViewCache viewCache = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.bankuai_item, null);
			viewCache = new BanKuaiViewCache(convertView);
			// 动态高度
			DisplayMetrics dm = new DisplayMetrics();
			mContext.getWindowManager().getDefaultDisplay().getMetrics(dm);

			int screenW = (dm.widthPixels - dip2px(mContext, 5)) / 2;
			int height = (224 * screenW) / 368;
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewCache.getmBanKuaiPhotoIV()
					.getLayoutParams();
			params.height = height;
			viewCache.getmBanKuaiPhotoIV().setLayoutParams(params);
			convertView.setTag(viewCache);
		} else {
			viewCache = (BanKuaiViewCache) convertView.getTag();
		}

		BanKuaiItem item = mData.get(position);

		if (item.getPhoto() != null) {
			ImageUtils.displayImage(item.getPhoto(), viewCache.getmBanKuaiPhotoIV(), 0);
		}

		viewCache.getmBanKuaiTitleTV().setText(item.getTitle());
		return convertView;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
}
