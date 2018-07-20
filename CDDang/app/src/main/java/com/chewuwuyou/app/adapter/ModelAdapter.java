package com.chewuwuyou.app.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.CarModel;
import com.chewuwuyou.app.bean.Model;
import com.chewuwuyou.app.bean.Serie;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ModelAdapter extends BaseAdapter {
	final Integer TOP_CAP = 1;
	final Integer BOTTOM_CAP = 2;
	final Integer NO_CAP = 3;
	final Integer TOP_AND_BOTTOM = 4;

	List<Model> mDatas;
	Map<Integer, Integer> mItemState;
	List<String> mSelects;
	Map<String, List<CarModel>> mMaps;
	Context mContext;
	boolean isShowCarPrice;
	private ImageLoader mImageLoader;

	public ModelAdapter(Context context, List<CarModel> datas,
			boolean isShowCarPrice) {
		// mDatas = datas;
		mContext = context;
		mMaps = new HashMap<String, List<CarModel>>();
		mSelects = new ArrayList<String>();
		mDatas = new ArrayList<Model>();
		this.isShowCarPrice = isShowCarPrice;
		this.mImageLoader = ImageLoader.getInstance();
		for (CarModel model : datas) {
			if (mSelects.contains(model.getFac())) {
				mMaps.get(model.getFac()).add(model);
			} else {
				mSelects.add(model.getFac());
				List<CarModel> list = new ArrayList<CarModel>();
				list.add(model);
				mMaps.put(model.getFac(), list);
			}
		}
		mItemState = new HashMap<Integer, Integer>();
		int index = 0;
		for (String fac : mSelects) {
			for (CarModel model : mMaps.get(fac)) {
				//
				int position = 0;
				for (Serie name : model.getSeries()) {
					position++;
					if (model.getSeries().size() == 1) {
						mItemState.put(index++, TOP_AND_BOTTOM);
					} else {
						if (position == 1) {
							mItemState.put(index++, TOP_CAP);
						} else if (position == model.getSeries().size()) {
							mItemState.put(index++, BOTTOM_CAP);
							position = 0;
						} else {
							mItemState.put(index++, NO_CAP);
						}
					}
					//
					Model modeltemp = new Model();
					modeltemp.setFac(model.getFac());
					modeltemp.setName(name.getName());
					modeltemp.setIcon(name.getIcon());
					modeltemp.setPrice(name.getPrice());
					mDatas.add(modeltemp);

				}

			}
		}
	}

	@Override
	public int getCount() {
		return mDatas == null ? 0 : mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHoder hoder;
		if (convertView == null) {
			hoder = new ViewHoder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.select_model_item1, null);
			hoder.textValue = (TextView) convertView
					.findViewById(R.id.textValue);
			hoder.textHeader = (TextView) convertView
					.findViewById(R.id.sectionHeader);
			hoder.textImage = (ImageView) convertView
					.findViewById(R.id.textImage);
			hoder.textPrice = (TextView) convertView
					.findViewById(R.id.vehicle_parice_tv);
			convertView.setTag(hoder);
		} else {
			hoder = (ViewHoder) convertView.getTag();
		}
		int state = mItemState.get(position);

		hoder.textValue.setText(mDatas.get(position).getName());
		// Bitmap bitmap = new AsyncBitmapLoader().loadBitmap(hoder.textImage,
		// NetworkUtil.IMAGE_BASE_URL + mDatas.get(position).getIcon(),
		// new ImageCallBack() {
		// @Override
		// public void imageLoad(View imageView, Bitmap bitmap) {
		// System.out
		// .println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx================="
		// + bitmap);
		// ((ImageView) imageView)
		// .setImageDrawable(new BitmapDrawable(bitmap));
		// }
		// });
		// if (bitmap != null) {
		// hoder.textImage.setImageDrawable(new BitmapDrawable(bitmap));
		// }
		mImageLoader.displayImage(
				NetworkUtil.IMAGE_BASE_URL + mDatas.get(position).getIcon(),
				hoder.textImage);
		hoder.textHeader.setText(mDatas.get(position).getFac());
		if (isShowCarPrice) {
			hoder.textPrice.setText(mDatas.get(position).getPrice());
		} else {
			hoder.textPrice.setVisibility(View.GONE);
		}

		switch (state) {
		case 1:
			// hoder.textView.setBackgroundResource(R.drawable.top_round);
			hoder.textHeader.setVisibility(View.VISIBLE);
			// layout.setMargins(0, 0, 0, 0);
			// hoder.textValue.setLayoutParams(layout);
			break;
		case 2:
			// hoder.textView.setBackgroundResource(R.drawable.bottom_round);
			// hoder.textValue.setLayoutParams(layout);
			hoder.textHeader.setVisibility(View.GONE);
			break;
		case 3:
			// hoder.textView.setBackgroundResource(R.drawable.no_round);
			hoder.textHeader.setVisibility(View.GONE);
			// hoder.textValue.setLayoutParams(layout);
			break;
		case 4:
			// hoder.textView.setBackgroundResource(R.drawable.bottom_and_top_round);
			hoder.textHeader.setVisibility(View.VISIBLE);
			// layout.setMargins(0, 0, 0, 0);
			// hoder.textValue.setLayoutParams(layout);
		default:
			break;
		}
		return convertView;
	}

	static class ViewHoder {
		TextView textHeader;
		ImageView textImage;
		TextView textValue;
		TextView textPrice;
	}
}
