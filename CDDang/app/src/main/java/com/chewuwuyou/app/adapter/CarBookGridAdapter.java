package com.chewuwuyou.app.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chewuwuyou.app.R;

public class CarBookGridAdapter extends BaseAdapter {

	private List<Map<String, Object>> mCarAssistantItems;
	private Context mContext;
	// 状态标志位
	private int clickTemp = -1;

	// 标识选择的Item
	public void setSeclection(int position) {
		clickTemp = position;
	}

	public CarBookGridAdapter(List<Map<String, Object>> mCarAssistantItems,
			Context mContext, int clickTemp) {

		super();
		this.mCarAssistantItems = mCarAssistantItems;
		this.mContext = mContext;
		this.clickTemp = clickTemp;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mCarAssistantItems.size();
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

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		LinearLayout view = (LinearLayout) LinearLayout.inflate(mContext,
				R.layout.car_book_item, null);
		TextView tv = (TextView) view.findViewById(R.id.car_book_item_tv);
		tv.setText((String) mCarAssistantItems.get(arg0).get("carBookStrs"));
		ImageView image = (ImageView) view
				.findViewById(R.id.car_book_item_image);

		image.setImageResource((Integer) mCarAssistantItems.get(arg0).get(
				"carBookImgBlueResIds"));
		// 选中时，改变背景色

		if (clickTemp == arg0) {
			view.setBackgroundColor(mContext.getResources().getColor(
					R.color.top_blue));
			tv.setTextColor(mContext.getResources().getColor(
					R.color.login_text_bg));
			image.setImageResource((Integer) mCarAssistantItems.get(clickTemp)
					.get("carBookImgWhiteResIds"));
		} else {
			view.setBackgroundColor(mContext.getResources().getColor(
					R.color.white));
			tv.setTextColor(mContext.getResources().getColor(
					R.color.car_book_gray));
			image.setImageResource((Integer) mCarAssistantItems.get(arg0).get(
					"carBookImgBlueResIds"));
		}

		return view;
	}

}
