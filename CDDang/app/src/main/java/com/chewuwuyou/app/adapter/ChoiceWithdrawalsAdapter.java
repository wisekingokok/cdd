package com.chewuwuyou.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.ChoiceWithdrawal;
import com.chewuwuyou.app.utils.WalletUtil;

/**
 * @describe:板块Adapter
 * @author:liuchun
 * @version
 * @created:
 */
public class ChoiceWithdrawalsAdapter extends BaseAdapter {

	private List<ChoiceWithdrawal> mAccountList;
	private Context mContext;
	private LayoutInflater mInflater;
	public static int posit = 0;

	public ChoiceWithdrawalsAdapter(Context context,
			List<ChoiceWithdrawal> mAccountList) {
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
		BanKuaiViewCache viewCache;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.choice_account_item, null);
			viewCache = new BanKuaiViewCache();
			viewCache.accountName = (TextView) convertView.findViewById(R.id.account_name);
			viewCache.accountPhone = (TextView) convertView.findViewById(R.id.account_phone);
			viewCache.accountImg = (ImageView) convertView.findViewById(R.id.account_img);
			viewCache.ccountRadio= (RadioButton) convertView.findViewById(R.id.ccount_img_radio);
			convertView.setTag(viewCache);
		} else {
			viewCache = (BanKuaiViewCache) convertView.getTag();
		}
		viewCache.ccountRadio.setChecked(mAccountList.get(position).isRadio());
		viewCache.accountName.setText(mAccountList.get(position).getAccountName());
		//viewCache.accountPhone.setText(mAccountList.get(position).getAccountPhone());
		WalletUtil.showAccount(mAccountList.get(position).getAccountPhone(), viewCache.accountPhone);
		return convertView;
	}

	class BanKuaiViewCache {
		RadioButton ccountRadio;
		TextView accountName, accountPhone,AccountDefault;// 名称，电话
		ImageView accountImg;// 图片
	}
}
