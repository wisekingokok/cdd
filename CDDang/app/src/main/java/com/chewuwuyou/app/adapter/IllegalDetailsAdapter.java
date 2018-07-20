package com.chewuwuyou.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.IllegalDetailsData;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:违章详情展示adapter
 * @author:yuyong
 * @date:2015-7-6下午5:35:55
 * @version:1.2.1
 */
public class IllegalDetailsAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater layoutInflater;
	private List<IllegalDetailsData> mData;// 任务集合

	public final class ListItemView {
		TextView mIllegalServiceNumTV;// 违章编号
		TextView mIllegalServiceContentTV;// 违章内容
		TextView mIllegalServiceScoreTV;// 违章扣分
		TextView mIllegalServiceMoneyTV;// 违章金额
	}

	public IllegalDetailsAdapter(Context context, List<IllegalDetailsData> mData) {
		this.mContext = context;

		layoutInflater = LayoutInflater.from(mContext);

		this.mData = mData;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mData.get(position);
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
			convertView = layoutInflater.inflate(R.layout.illegal_service_item,
					null);
			listItemView.mIllegalServiceNumTV = (TextView) convertView
					.findViewById(R.id.illegal_service_num_tv);
			listItemView.mIllegalServiceContentTV = (TextView) convertView
					.findViewById(R.id.illegal_service_content_tv);
			listItemView.mIllegalServiceScoreTV = (TextView) convertView
					.findViewById(R.id.illegal_service_score_tv);
			listItemView.mIllegalServiceMoneyTV = (TextView) convertView
					.findViewById(R.id.illegal_service_money_tv);
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		listItemView.mIllegalServiceNumTV.setText(String.valueOf(position+1)+".");
		listItemView.mIllegalServiceContentTV.setText(mData.get(position).getInfo());
		listItemView.mIllegalServiceScoreTV.setText("-"
				+ mData.get(position).getFen() + "分");
		listItemView.mIllegalServiceMoneyTV.setText("￥"
				+ mData.get(position).getMoney() + "元");
		return convertView;
	}

}
