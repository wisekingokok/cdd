/**
 * @Title:
 * @Copyright:
 * @Description:
 * @author:
 * @date:2015-3-20上午11:22:18
 * @version:1.2.1
 */
package com.chewuwuyou.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.Shop;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:店铺适配器
 * @author:yuyong
 * @date:2015-3-20上午11:22:18
 * @version:1.2.1
 */
public class CommonShopListAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater layoutInflater;
	private List<Shop> mShops;// 店铺信息集合
	private int shopImgId;

	public final class ListItemView {
		ImageView mShopIcon;// 店铺图标
		TextView mShopName;// 店铺名称
		TextView mShopDistance;// 店铺距离
		TextView mShopAddress;// 店铺地址
	}

	public CommonShopListAdapter(Context context, List<Shop> shops,
			int shopImgId) {
		this.mContext = context;

		layoutInflater = LayoutInflater.from(mContext);

		this.mShops = shops;
		this.shopImgId = shopImgId;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mShops.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mShops.get(position);
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
			convertView = layoutInflater.inflate(R.layout.common_shop_item,
					null);
			listItemView.mShopIcon = (ImageView) convertView
					.findViewById(R.id.shop_icon_iv);
			listItemView.mShopName = (TextView) convertView
					.findViewById(R.id.shop_name_tv);
			listItemView.mShopDistance = (TextView) convertView
					.findViewById(R.id.shop_distance_tv);
			listItemView.mShopAddress = (TextView) convertView
					.findViewById(R.id.shop_address_tv);

			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		listItemView.mShopIcon.setImageResource(shopImgId);
		listItemView.mShopName.setText(mShops.get(position).getTitle());
		listItemView.mShopDistance.setText(""
				+ (Math.round(mShops.get(position).getDistance() / 100) / 10.0)
				+ "公里");
		listItemView.mShopAddress.setText(mShops.get(position).getAddress()
				);
		return convertView;
	}

}
