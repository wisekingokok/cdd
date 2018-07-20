package com.chewuwuyou.app.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.CarBrand;

public class CarLogOfAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater layoutInflater;
	// 任务信息集合
	private List<CarBrand> listItems;

	public final class ListItemView { // 自定义控件集合
		ImageView mLogoImg;
		TextView mLogoText;

	}

	public CarLogOfAdapter(Context context, List<CarBrand> listItems) {
		this.mContext = context;

		layoutInflater = LayoutInflater.from(context);

		this.listItems = listItems;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listItems.get(position);
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
			convertView = layoutInflater.inflate(
					R.layout.carservice_logof_item, null);

			listItemView.mLogoImg = (ImageView) convertView
					.findViewById(R.id.carLogoItemImage);
			listItemView.mLogoText = (TextView) convertView
					.findViewById(R.id.carLogoItemText);
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		try {
			InputStream is = mContext.getAssets().open(
					"logo/" + listItems.get(position).getLogo_img());
			listItemView.mLogoImg.setImageBitmap(BitmapFactory.decodeStream(is));
			listItemView.mLogoText.setText(listItems.get(position).getName());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return convertView;
	}
}
