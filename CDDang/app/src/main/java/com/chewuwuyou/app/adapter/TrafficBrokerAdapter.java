package com.chewuwuyou.app.adapter;

import java.text.DecimalFormat;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.ServicePro;
import com.chewuwuyou.app.bean.TrafficBusinessListBook;
import com.chewuwuyou.app.ui.BusinessDetailActivity;
import com.chewuwuyou.app.ui.BusinessPersonalCenterActivity;
import com.chewuwuyou.app.ui.VehicleServiceActivity;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.ToastUtil;

public class TrafficBrokerAdapter extends BaseAdapter {

	private Context mContext;
	private List<TrafficBusinessListBook> mData;
	private LayoutInflater mInflater;
	private ServicePro servicePro;
    private int type;
    private DecimalFormat df = new DecimalFormat("#0.00");
	public TrafficBrokerAdapter(Context context,
			List<TrafficBusinessListBook> data, ServicePro servicePro,
			String address,int type) {
		this.mData = data;
		this.mContext = context;
		this.mInflater = (LayoutInflater) mContext
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		this.servicePro = servicePro;
		this.type = type;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		view viewCache = null;
		if (convertView == null) {
			viewCache = new view();
			convertView = mInflater.inflate(R.layout.business_list, null);
			viewCache.businessListPortrait = (ImageView) convertView
					.findViewById(R.id.business_list_portrait);
			viewCache.businessListCollection = (ImageView) convertView
					.findViewById(R.id.business_list_collection);
			viewCache.businierssGrade = (RatingBar) convertView
					.findViewById(R.id.businierss_grade);
			viewCache.businessOrder = (Button) convertView
					.findViewById(R.id.business_order);
			viewCache.businessDetails = (Button) convertView
					.findViewById(R.id.business_details);
			viewCache.businessListName = (TextView) convertView
					.findViewById(R.id.business_list_name);
			viewCache.businessListPrice = (TextView) convertView
					.findViewById(R.id.business_list_price);
			viewCache.businessFees = (TextView) convertView
					.findViewById(R.id.business_fees);
			viewCache.businessService = (TextView) convertView
					.findViewById(R.id.business_service);
			viewCache.businessBranch = (TextView) convertView
					.findViewById(R.id.business_branch);
			convertView.setTag(viewCache);
		} else {
			viewCache = (view) convertView.getTag();
		}
		viewCache.businessListName.setText(mData.get(position)
				.getRealName());
		if(type == 1){
			viewCache.businessFees.setVisibility(View.GONE);
			viewCache.businessListPrice.setText("¥ " + df.format(mData.get(position).getServicePrice()));
		}else{
			viewCache.businessFees.setVisibility(View.VISIBLE);
			viewCache.businessFees.setText("  规费"+ df.format(mData.get(position).getFees()));// 规费
			double fees = mData.get(position).getServicePrice()
					+ mData.get(position).getFees();
			viewCache.businessListPrice.setText("¥ " + df.format(fees));// 价格
		}
		
		viewCache.businessService.setText("  服务费"+ df.format(mData.get(position).getServicePrice()));// 服务费
		if (mData.get(position).getIsfavorite() == 1) {
			viewCache.businessListCollection.setVisibility(View.VISIBLE);// 显示
			viewCache.businessListCollection.setImageDrawable(mContext.getResources().getDrawable(R.drawable.business_collection));
		} else {
			viewCache.businessListCollection.setVisibility(View.INVISIBLE);
		}
		ImageUtils.displayImage(mData.get(position).getUrl(),
				viewCache.businessListPortrait, 10);
		viewCache.businierssGrade.setRating(mData.get(position).getStar());
		viewCache.businessBranch.setText(Float.parseFloat((mData.get(position)
				.getStar() + "")) + "分");
		/**
		 * 下单
		 */
		viewCache.businessOrder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (mData.get(position).getUserId()
						.equals(CacheTools.getUserData("userId"))) {
					ToastUtil.toastShow(mContext, "不能给自己下单");
				} else {
					Intent intent = new Intent(mContext,
							BusinessDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable(Constant.TRAFFIC_SER,
							mData.get(position));
					bundle.putSerializable("servicedata", servicePro);
					intent.putExtra("address", VehicleServiceActivity.mAddress);
					intent.putExtras(bundle);
					mContext.startActivity(intent);
				}

			}
		});

		/**
		 * 详情
		 */
		viewCache.businessDetails.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,
						BusinessPersonalCenterActivity.class);
				intent.putExtra("businessId", mData.get(position).getId() + "");
				intent.putExtra("position", position + "");
				mContext.startActivity(intent);
			}
		});
		return convertView;
	}

	public class view {
		ImageView businessListPortrait, businessListCollection;
		RatingBar businierssGrade;
		Button businessOrder, businessDetails;// 下单
		TextView businessListName, businessListPrice, businessFees,
				businessService, businessBranch;
	}
}
