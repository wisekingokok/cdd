package com.chewuwuyou.app.adapter;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.AccountWithdrawal;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.WalletUtil;

/**
 * @describe:提现账户列表adapter
 * @author:liuchun
 * @version
 * @created:
 */
public class AccountWithdrawalsAdapter extends BaseAdapter {

	private List<AccountWithdrawal> mAccountList;
	private Context mContext;
	private LayoutInflater mInflater;
	private Handler mHandler;

	public AccountWithdrawalsAdapter(Context context,
			List<AccountWithdrawal> mAccountList, Handler mHandler) {
		this.mContext = context;
		this.mAccountList = mAccountList;
		this.mInflater = LayoutInflater.from(mContext);
		this.mHandler = mHandler;
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
			convertView = mInflater.inflate(R.layout.slide_view_merge, null);
			viewCache = new BanKuaiViewCache();
			viewCache.accountName = (TextView) convertView
					.findViewById(R.id.account_name);
			viewCache.accountPhone = (TextView) convertView
					.findViewById(R.id.account_phone);
			viewCache.accountImg = (ImageView) convertView
					.findViewById(R.id.account_img);
			viewCache.ccountRadio = (ImageView) convertView
					.findViewById(R.id.ccount_img_radio);
			viewCache.accountDelete = (TextView) convertView
					.findViewById(R.id.delete);
			convertView.setTag(viewCache);
		} else {
			viewCache = (BanKuaiViewCache) convertView.getTag();
		}

		viewCache.accountName.setText(mAccountList.get(position)
				.getAccountName());
		WalletUtil.showAccount(mAccountList.get(position).getAccountPhone(),
				viewCache.accountPhone);
		// viewCache.accountPhone.setText(mAccountList.get(position).getAccountPhone());
		viewCache.accountDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Constant.CCCOUNT_WITHDRAWLS = position;
				Message message = new Message();
				message.what = Constant.SEND_Handler;
				message.obj = String.valueOf(mAccountList.get(position)
						.getAccountPhone());
				mHandler.sendMessage(message);
			}
		});
		return convertView;
	}

	final class BanKuaiViewCache {
		TextView accountName, accountPhone, AccountDefault, accountDelete;// 名称，电话
		ImageView accountImg, ccountRadio;// 图片
	}
}
