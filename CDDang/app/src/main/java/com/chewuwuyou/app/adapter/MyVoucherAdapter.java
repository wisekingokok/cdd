package com.chewuwuyou.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.Voucher;

/**
 * @describe:抵用券详情
 * @author:yuyong
 * @version 1.1.0
 * @created:2015-1-26下午2:39:48
 */

public class MyVoucherAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater layoutInflater;
	private List<Voucher> mVouchers;

	public class ListItemView {
		TextView voucherMoney; // 抵用券金额
		TextView voucherStatus;// 抵用券使用状态
		TextView voucherTime;// 抵用券使用时间
	}

	public MyVoucherAdapter(Context context, List<Voucher> mVouchers) {
		this.mContext = context;
		layoutInflater = LayoutInflater.from(context);
		this.mVouchers = mVouchers;
	}

	@Override
	public int getCount() {
		return mVouchers.size();
	}

	@Override
	public Object getItem(int position) {
		return mVouchers.get(position);

	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ListItemView listItemView = null;
		if (convertView == null) {
			listItemView = new ListItemView();
			convertView = layoutInflater.inflate(R.layout.use_voucher_item,
					null);
			listItemView.voucherMoney = (TextView) convertView
					.findViewById(R.id.voucher_money_tv);
			listItemView.voucherStatus = (TextView) convertView
					.findViewById(R.id.use_voucher_status_tv);
			listItemView.voucherTime = (TextView) convertView
					.findViewById(R.id.use_voucher_time_tv);
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		listItemView.voucherMoney.setText("￥"
				+ mVouchers.get(position).getAmount() + "");
		if (mVouchers.get(position).getAmount() < 0) {
			listItemView.voucherStatus.setTextColor(mContext.getResources().getColor(R.color.common_text_color));
			listItemView.voucherStatus.setText("已使用");
		} else {
			listItemView.voucherStatus
					.setTextColor(mContext.getResources().getColor(R.color.common_input_text_color));
			listItemView.voucherStatus.setText(mVouchers.get(position)
					.getType());
		}

		listItemView.voucherTime.setText(mVouchers.get(position)
				.getCreateTime());

		return convertView;
	}

}
