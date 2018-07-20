package com.chewuwuyou.app.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.WeizhangChildItem;
import com.chewuwuyou.app.bean.WeizhangGroupItem;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:违章详情
 * @author:yuyong
 * @date:2015-7-7下午3:19:33
 * @version:1.2.1
 */
@SuppressLint("UseSparseArrays")
public class WeiZhangDetailsAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private List<WeizhangGroupItem> mData;
	public  List<Boolean> mChecked;
	private static HashMap<Integer, View> map = new HashMap<Integer, View>();

	public WeiZhangDetailsAdapter(Context context, List<WeizhangGroupItem> data) {
		this.mData = data;
		this.mContext = context;
		this.mInflater = (LayoutInflater) mContext
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		mChecked = new ArrayList<Boolean>();
		
		
		for (int i = 0; i < mData.get(0).getHistorys().size(); i++) {
			mChecked.add(true);
		}
	}

	@Override
	public int getCount() {
		return mData.get(0).getHistorys().size();
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
		View view;
		WeizhangChildItemViewCache viewCache=null;
		if (map.get(position)  == null) {
			view = mInflater.inflate(R.layout.weizhang_child_item, null);
			viewCache = new WeizhangChildItemViewCache();
			viewCache.mWeizhangDateTV=(TextView) view.findViewById(R.id.weizhang_date_tv);
			viewCache.mWeizhangFakuanNumTV=(TextView) view.findViewById(R.id.weizhang_fakuan_num_tv);
			viewCache.mWeizhangInfoTV=(TextView) view.findViewById(R.id.weizhang_info_tv);
			viewCache.mWeizhangLocationTV=(TextView) view.findViewById(R.id.weizhang_location_tv);
			viewCache.mWeizhangMoreTV=(TextView) view.findViewById(R.id.weizhang_more_tv);
			viewCache.mChooseServiceCB=(CheckBox) view.findViewById(R.id.choose_service_cb);
			viewCache.mWeizhangKoufenNumTV=(TextView) view.findViewById(R.id.weizhang_koufen_num_tv);
			final int p = position;
			map.put(position,view);
			viewCache.mChooseServiceCB.setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							CheckBox cb = (CheckBox) v;
							mChecked.set(p, cb.isChecked());
						}
					});
			view.setTag(viewCache);

		} else {
			view = map.get(position);
			viewCache = (WeizhangChildItemViewCache) view.getTag();
		}
		WeizhangChildItem item = mData.get(0).getHistorys().get(position);
		viewCache.mWeizhangDateTV.setText(item.getOccur_date());
		viewCache.mWeizhangFakuanNumTV.setText(
				"-￥" + String.valueOf(item.getMoney()));
		viewCache.mWeizhangInfoTV.setText(item.getInfo());
		viewCache.mWeizhangKoufenNumTV.setText(
				String.valueOf(item.getFen()));
		viewCache.mWeizhangLocationTV.setText(item.getOccur_area());
		viewCache.mWeizhangMoreTV.setText(
				item.getStatus().equals("N") ? "未处理" : "处理");
		viewCache.mChooseServiceCB.setChecked(mChecked.get(position));
		return view;

	}
	
	class WeizhangChildItemViewCache{
		private TextView mWeizhangDateTV;
		private TextView mWeizhangInfoTV;
		private TextView mWeizhangFakuanNumTV;
		private TextView mWeizhangKoufenNumTV;
		private TextView mWeizhangMoreTV;
		private TextView mWeizhangLocationTV;//
		private CheckBox mChooseServiceCB;
	}
}

