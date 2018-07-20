package com.chewuwuyou.app.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.CityName;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.widget.PinnedHeaderListView;
import com.chewuwuyou.app.widget.PinnedHeaderListView.PinnedHeaderAdapter;

public class CityAdapter extends BaseAdapter implements SectionIndexer,
		PinnedHeaderAdapter, OnScrollListener {

	private int mLocationPosition = -1;
	private List<CityName> mDatas;
	// 首字母集
	private List<String> mLetterSections;
	// 首字母位置集
	private List<Integer> mLetterPositions;
	private Context mContext;
	// private List<CityName> mSubcribeList;
	private List<CityName> mOriginalValues;
	private final Object mLock = new Object();
	private Handler mHandler;
	public int selectIndex;
	public boolean isSelected;
	private EditText mEditTEXT;
	// private PinnedHeaderListView mListView;
	private LinearLayout mSubLayout;

	private boolean isDisplay = false;// 判断是否显示

	public CityAdapter(Context context, List<CityName> datas,
			List<String> letterSections, List<Integer> letterpositions,
			Handler handler, EditText mEditTEXT,
			PinnedHeaderListView mListView, LinearLayout mSubLayout) {
		mContext = context;
		mDatas = datas;

		mLetterSections = letterSections;
		mLetterPositions = letterpositions;

		mHandler = handler;
		this.mEditTEXT = mEditTEXT;
		// this.mListView = mListView;
		this.mSubLayout = mSubLayout;
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		int section = getSectionForPosition(position);
		Controlview controlView = null;
		if (convertView == null) {
			controlView = new Controlview();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.select_province_item, null);
			controlView.brandText = (TextView) convertView
					.findViewById(R.id.provinceText);
			controlView.headerText = (TextView) convertView
					.findViewById(R.id.title);
			convertView.setTag(controlView);
		} else {
			controlView = (Controlview) convertView.getTag();
		}
		if (getPositionForSection(section) == position) {
			controlView.headerText.setVisibility(View.VISIBLE);

		} else {
			controlView.headerText.setVisibility(View.GONE);
		}
		if (isSelected == true) {
			if (position == selectIndex) {
				controlView.brandText
						.setBackgroundResource(R.color.choice_city);
			} else {
				controlView.brandText.setBackgroundResource(R.color.white);
			}
		} else {
			if (position == selectIndex) {
				controlView.brandText.setBackgroundResource(R.color.white);
			} else {
				controlView.brandText.setBackgroundResource(R.color.white);
			}
		}
		controlView.headerText.setText(mLetterSections.get(section));
		controlView.brandText.setText(mDatas.get(position).getCityName());
		// 设置点击事件,传递选中的搜索城市
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (selectIndex == position) {
					if (isDisplay == false) {
						mSubLayout.setVisibility(View.VISIBLE);
						selectIndex = position;
						isDisplay = true;
						isSelected = true;
						Message message = new Message();
						message.what = Constant.SEND_ADAPTER;
						message.obj = mDatas.get(position);
						mHandler.sendMessage(message);
					} else if (isDisplay == true) {
						mSubLayout.setVisibility(View.GONE);
						isDisplay = false;
						isSelected = false;
					}
				} else {
					mSubLayout.setVisibility(View.VISIBLE);
					selectIndex = position;
					isDisplay = true;
					isSelected = true;
					Message message = new Message();
					message.what = Constant.SEND_ADAPTER;
					message.obj = mDatas.get(position);
					mHandler.sendMessage(message);
				}
				notifyDataSetChanged();
			}
		});

		mEditTEXT.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

				mSubLayout.setVisibility(View.GONE);
				isSelected = false;

				ArrayList<CityName> newValues = null;
				if (mOriginalValues == null) {
					synchronized (mLock) {
						mOriginalValues = new ArrayList<CityName>(mDatas);
					}
				}
				if (mEditTEXT.getText().toString().equals("")) {
					synchronized (mLock) {
						newValues = new ArrayList<CityName>(mOriginalValues);
					}
				} else {
					String prefixString = mEditTEXT.getText().toString().trim();

					List<CityName> values = mOriginalValues;

					int count = values.size();

					newValues = new ArrayList<CityName>(count);

					for (CityName value : values) {
						String title = value.getCityName();
						if (title.indexOf(prefixString) != -1) {
							newValues.add(value);
						}
					}
					if (newValues.size() == 0) {
						newValues = new ArrayList<CityName>(mOriginalValues);
					}
				}
				mDatas = newValues;
				if (newValues.size() > 0) {
					notifyDataSetChanged();
				} else {
					notifyDataSetInvalidated();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});

		return convertView;
	}

	public class Controlview {
		TextView headerText, brandText;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
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

	/**
	 * 向上滑动改变字母
	 */
	@Override
	public void configurePinnedHeader(View header, int position, int alpha) {
		int section = getSectionForPosition(position);
		if (section == -1) {
		} else {
			String title = (String) getSections()[section];

			System.out.println("++显示的字母+++++++++++++++++++++" + title);

			/* Toast.makeText(mContext, title, Toast.LENGTH_SHORT).show(); */
			((TextView) header.findViewById(R.id.title)).setText(title);
		}
	}

	@Override
	public Object[] getSections() {
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
		if (position < 0 || position >= getCount()) {
			return -1;
		}
		int index = Arrays.binarySearch(mLetterPositions.toArray(), position);
		return index >= 0 ? index : -index - 2;
	}
}
