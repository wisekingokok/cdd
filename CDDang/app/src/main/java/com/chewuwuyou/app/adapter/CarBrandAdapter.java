package com.chewuwuyou.app.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.CarBrand;
import com.chewuwuyou.app.widget.PinnedHeaderListView;
import com.chewuwuyou.app.widget.PinnedHeaderListView.PinnedHeaderAdapter;

public class CarBrandAdapter extends BaseAdapter implements SectionIndexer,
		PinnedHeaderAdapter, OnScrollListener {
	private int mLocationPosition = -1;
	private List<CarBrand> mDatas;
	// 首字母集
	private List<String> mLetterSections;
	// 首字母位置集
	private List<Integer> mLetterPositions;
	private Context mContext;

	public CarBrandAdapter(Context context, List<CarBrand> datas,
			List<String> letterSections, List<Integer> letterpositions) {
		mContext = context;
		mDatas = datas;
		mLetterSections = letterSections;
		mLetterPositions = letterpositions;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		int section = getSectionForPosition(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.select_brand_item, null);
		}
		TextView headerText = (TextView) convertView.findViewById(R.id.title);
		if (getPositionForSection(section) == position) {
			headerText.setVisibility(View.VISIBLE);
			headerText.setText(mLetterSections.get(section));
		} else {
			headerText.setVisibility(View.GONE);
		}
		ImageView brandLogo = (ImageView) convertView
				.findViewById(R.id.brandLogo);
		TextView brandText = (TextView) convertView
				.findViewById(R.id.brandText);
		brandText.setText(mDatas.get(position).getName());
		try {
			InputStream is = mContext.getAssets().open(
					"logo/" + mDatas.get(position).getLogo_img());
			brandLogo.setImageBitmap(BitmapFactory.decodeStream(is));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return convertView;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		if (view instanceof PinnedHeaderListView) {
			((PinnedHeaderListView) view).configureHeaderView(firstVisibleItem);
		}
	}

	@Override
	public int getPinnedHeaderState(int position) {
		int realPosition = position;
		if (realPosition < 0
				|| (mLocationPosition != -1 && mLocationPosition == realPosition)) {
			return PINNED_HEADER_GONE;
		}
		mLocationPosition = -1;
		int section = getSectionForPosition(realPosition);
		int nextSectionPosition = getPositionForSection(section + 1);
		if (nextSectionPosition != -1
				&& realPosition == nextSectionPosition - 1) {
			return PINNED_HEADER_PUSHED_UP;
		}
		return PINNED_HEADER_VISIBLE;
	}

	@Override
	public void configurePinnedHeader(View header, int position, int alpha) {
		// TODO Auto-generated method stub
		int realPosition = position;
		int section = getSectionForPosition(realPosition);
		String title = (String) getSections()[section];
		((TextView) header.findViewById(R.id.title)).setText(title);
	}

	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		return mLetterSections.toArray();
	}

	@Override
	public int getPositionForSection(int section) {
		if (section < 0 || section >= mLetterSections.size()) {
			return -1;
		}
		return mLetterPositions.get(section);
	}

	@Override
	public int getSectionForPosition(int position) {
		// TODO Auto-generated method stub
		if (position < 0 || position >= getCount()) {
			return -1;
		}
		int index = Arrays.binarySearch(mLetterPositions.toArray(), position);
		return index >= 0 ? index : -index - 2;
	}

}
