package com.chewuwuyou.app.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.Dqdm;
import com.chewuwuyou.app.ui.ServiceTypeActivity;
import com.chewuwuyou.app.utils.Constant;

public class IllegalCodeQueryAdapter extends BaseAdapter implements Filterable {
	private LayoutInflater inflater;
	private List<Dqdm> mSubcribeList;
	private MyFilter mFilter;
	private List<Dqdm> mOriginalValues;
	private final Object mLock = new Object();
	private ViewHolder viewHolder = null;

	private Context mContext;

	static class ViewHolder {
		TextView wzdmTV;
		TextView wzdmAddressTV;
	}

	public IllegalCodeQueryAdapter(Context context, List<Dqdm> subcribeList) {
		mSubcribeList = subcribeList;
		this.mContext = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mSubcribeList.size();
	}

	@Override
	public Object getItem(int position) {
		return mSubcribeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.dqdm_query_item, null);
			viewHolder.wzdmTV = (TextView) convertView
					.findViewById(R.id.dqdm_tv);
			viewHolder.wzdmAddressTV = (TextView) convertView
					.findViewById(R.id.dqdm_address_tv);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Dqdm wzdm = mSubcribeList.get(position);
		viewHolder.wzdmTV.setText(mSubcribeList.get(position).getIllegalCode());
		if (wzdm.getProvince().equals(wzdm.getCity())
				&& wzdm.getProvince().equals(wzdm.getArea())) {
			viewHolder.wzdmAddressTV.setText(wzdm.getProvince());
		} else if (wzdm.getProvince().equals(wzdm.getCity())
				&& !wzdm.getProvince().equals(wzdm.getArea())) {
			viewHolder.wzdmAddressTV.setText(wzdm.getProvince() + ","
					+ wzdm.getArea());
		} else {
			viewHolder.wzdmAddressTV.setText(wzdm.getProvince() + ","
					+ wzdm.getCity() + "," + wzdm.getArea());
		}
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// String province = mSubcribeList.get(position).getProvince();
				Intent intent = new Intent(mContext, ServiceTypeActivity.class);
				// intent.putExtra("isWzdmActivity", 1);
				// intent.putExtra("province", province);
				// if (!province.equals("重庆市") && !province.equals("北京市")
				// && !province.equals("天津市") && !province.equals("上海市")) {
				// intent.putExtra("city", mSubcribeList.get(position)
				// .getCity());
				// } else {
				// intent.putExtra("city", "");
				// }
				// intent.putExtra("district", mSubcribeList.get(position)
				// .getArea());// 传入地区
				intent.putExtra("serviceType",
						Constant.SERVICE_TYPE.ILLEGAL_SERVICE);
				mContext.startActivity(intent);
			}
		});
		return convertView;
	}

	@Override
	public Filter getFilter() {
		if (mFilter == null) {
			mFilter = new MyFilter();
		}
		return mFilter;
	}

	class MyFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			FilterResults results = new FilterResults();

			if (mOriginalValues == null) {
				synchronized (mLock) {
					mOriginalValues = new ArrayList<Dqdm>(mSubcribeList);
				}
			}
			if (prefix == null || prefix.length() == 0) {
				synchronized (mLock) {
					ArrayList<Dqdm> list = new ArrayList<Dqdm>(mOriginalValues);
					results.values = list;
					results.count = list.size();
				}
			} else {
				String prefixString = prefix.toString();

				final List<Dqdm> values = mOriginalValues;

				final int count = values.size();

				final ArrayList<Dqdm> newValues = new ArrayList<Dqdm>(count);

				for (Dqdm value : values) {
					String title = value.getIllegalCode();

					if (title.indexOf(prefixString) != -1) {
						newValues.add(value);
					}
				}
				results.values = newValues;
				results.count = newValues.size();
			}

			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			mSubcribeList = (ArrayList<Dqdm>) results.values;

			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}

	}
}
