package com.chewuwuyou.app.adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.utils.CarFriendQuanUtils;
import com.chewuwuyou.app.utils.ImageUtils;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:附近的车友
 * @author:yuyong
 * @date:2015-4-9下午1:59:23
 * @version:1.2.1
 */
public class CollectMeUserAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater layoutInflater;
	private JSONArray mJsonArray;

	public CollectMeUserAdapter(Context context, JSONArray jsonArray) {
		this.mContext = context;
		this.mJsonArray = jsonArray;
		layoutInflater = LayoutInflater.from(mContext);
	}

	public class ListItemView {
		ImageView mCarFriendHeadIV;
		TextView mCarFriendNameTV;
		TextView mCarFriendDistanceTV;
		TextView mCarFriendSingTureTV;// 车友心情
		ImageView mCarFriendSexIV;// 性别图片
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mJsonArray.length();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		JSONObject jo = null;
		try {
			jo = mJsonArray.getJSONObject(position);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jo;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ListItemView listItemView = null;
		if (convertView == null) {
			listItemView = new ListItemView();
			convertView = layoutInflater.inflate(
					R.layout.nearby_car_friend_item, null);
			listItemView.mCarFriendHeadIV = (ImageView) convertView
					.findViewById(R.id.car_friend_head_iv);
			listItemView.mCarFriendNameTV = (TextView) convertView
					.findViewById(R.id.car_friend_name_tv);
			listItemView.mCarFriendDistanceTV = (TextView) convertView
					.findViewById(R.id.car_friend_distance_tv);
			listItemView.mCarFriendSingTureTV = (TextView) convertView
					.findViewById(R.id.car_friend_singture_tv);
			listItemView.mCarFriendSexIV = (ImageView) convertView
					.findViewById(R.id.car_friend_sex_iv);
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		try {
			JSONObject jo = mJsonArray.getJSONObject(position);
			if (!TextUtils.isEmpty(jo.getString("url"))) {
				ImageUtils.displayImage(jo.getString("url"),
						listItemView.mCarFriendHeadIV, 360);
			} else {
				listItemView.mCarFriendHeadIV
						.setImageResource(R.drawable.user_fang_icon);
			}

			listItemView.mCarFriendNameTV.setText(CarFriendQuanUtils
					.showCarFriendName(jo.getString("noteName"),
							jo.getString("nick"), jo.getString("userName")));
			listItemView.mCarFriendSingTureTV
					.setText(jo.getString("signature"));

			if (jo.getString("sex").equals("0")) {
				listItemView.mCarFriendSexIV.setImageResource(R.drawable.man);
			} else {
				listItemView.mCarFriendSexIV.setImageResource(R.drawable.woman);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return convertView;
	}

}
