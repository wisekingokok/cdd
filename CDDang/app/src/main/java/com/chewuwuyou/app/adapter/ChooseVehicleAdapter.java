package com.chewuwuyou.app.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.VehicleIllegal;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 切換车辆adapter
 * 
 * @author Administrator
 */
public class ChooseVehicleAdapter extends BaseAdapter {

	private Context mContext;
	private List<VehicleIllegal> vehicles;
	private LayoutInflater layoutInflater;
	private ListItemView listItemView = null;
	private ImageLoader mImageLoader;

	public ChooseVehicleAdapter(Context context, List<VehicleIllegal> vehicles) {
		this.mContext = context;
		this.vehicles = vehicles;
		this.mImageLoader = ImageLoader.getInstance();
		layoutInflater = LayoutInflater.from(mContext);
	}

	public final class ListItemView {
		public TextView mPlateNumber;
		public ImageView mVehicleImage;
		public TextView mIllegalNum;
		public TextView mIllegalMoney;
		public TextView mIllegalScore;
		public TextView mIllegalLocationTV;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return vehicles.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return vehicles.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			listItemView = new ListItemView();
			convertView = layoutInflater.inflate(R.layout.choose_vehicle_item,
					null);
			listItemView.mPlateNumber = (TextView) convertView
					.findViewById(R.id.exchange_vehicle_plateNumber);
			listItemView.mVehicleImage = (ImageView) convertView
					.findViewById(R.id.exchange_vehicle_image);
			listItemView.mIllegalNum = (TextView) convertView
					.findViewById(R.id.illegal_num_tv);
			listItemView.mIllegalMoney = (TextView) convertView
					.findViewById(R.id.illegal_money_tv);
			listItemView.mIllegalScore = (TextView) convertView
					.findViewById(R.id.illegal_score_tv);
			listItemView.mIllegalLocationTV = (TextView) convertView
					.findViewById(R.id.vehicle_location_tv);
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		if (TextUtils.isEmpty(vehicles.get(position).getNoteName())) {
			listItemView.mPlateNumber.setText(vehicles.get(position)
					.getPlateNumber());
		} else {
			listItemView.mPlateNumber.setText(vehicles.get(position)
					.getNoteName());
		}

		mImageLoader.displayImage(vehicles.get(position).getVehicleImageUrl(),
				listItemView.mVehicleImage,
				new DisplayImageOptions.Builder().cacheInMemory(true)
						.cacheOnDisc(true).showStubImage(R.drawable.a1_audi)
						.showImageForEmptyUri(R.drawable.a1_audi)
						.showImageOnFail(R.drawable.a1_audi).build());
		// if (!TextUtils.isEmpty(vehicles.get(position).getVehicleImageUrl()))
		// {
		// ImageLoader.getInstance().displayImage(vehicles.get(position).getVehicleImageUrl(),
		// listItemView.mVehicleImage);
		// fb.display(listItemView.mVehicleImage, vehicles.get(position)
		// .getVehicleImageUrl());
		// ImageUtils.displayImage(vehicles.get(position)
		// .getVehicleImageUrl(), listItemView.mVehicleImage, 0);

		// }
		listItemView.mIllegalNum.setText(vehicles.get(position)
				.getVehicleIllegalNum() != null ? vehicles.get(position)
				.getVehicleIllegalNum() : "0");
		listItemView.mIllegalMoney.setText(vehicles.get(position)
				.getVehicleIllegalMoney() != null ? vehicles.get(position)
				.getVehicleIllegalMoney() : "0");
		listItemView.mIllegalScore.setText(vehicles.get(position)
				.getVehicleIllegalScore() != null ? vehicles.get(position)
				.getVehicleIllegalScore() : "0");
		if (!TextUtils.isEmpty(vehicles.get(position).getCityName())) {
			listItemView.mIllegalLocationTV.setText(vehicles.get(position)
					.getCityName());
		}

		return convertView;

	}

}
