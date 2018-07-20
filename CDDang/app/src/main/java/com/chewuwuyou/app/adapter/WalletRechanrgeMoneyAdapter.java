package com.chewuwuyou.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.WalletRechargeMoney;

/**
 * @describe:提现账户列表adapter
 * @author:liuchun
 */
public class WalletRechanrgeMoneyAdapter extends BaseAdapter {

	private List<WalletRechargeMoney> mAccountList;
	private Context mContext;
	private LayoutInflater mInflater;

	public WalletRechanrgeMoneyAdapter(Context context,
			List<WalletRechargeMoney> mAccountList) {
		this.mContext = context;
		this.mAccountList = mAccountList;
		this.mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mAccountList.size();
	}

	@Override
	public Object getItem(int position) {
		return mAccountList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final BanKuaiViewCache viewCache;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.wallet_rechanrge_item, null);
			viewCache = new BanKuaiViewCache();
			viewCache.money = (TextView) convertView.findViewById(R.id.money);
			viewCache.moneySelected = (ImageView) convertView.findViewById(R.id.money_selected);
			
			convertView.setTag(viewCache);
		} else {
			viewCache = (BanKuaiViewCache) convertView.getTag();
		}
		viewCache.money.setText(mAccountList.get(position).getMoney()+"元");
		if(mAccountList.get(position).isSelected() == true){
			viewCache.money.setBackground(mContext.getResources().getDrawable(R.drawable.business_blue));
			viewCache.moneySelected.setVisibility(View.VISIBLE);
			viewCache.moneySelected.setImageDrawable(mContext.getResources().getDrawable(R.drawable.rechang_money));
		}else{
			viewCache.moneySelected.setVisibility(View.GONE);
			viewCache.money.setBackground(mContext.getResources().getDrawable(R.drawable.business_white));
		}
		
		return convertView;
	}

	class BanKuaiViewCache {
		TextView money;//选择金额
		ImageView moneySelected;// 选中图片
	}
}
