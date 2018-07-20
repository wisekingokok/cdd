package com.chewuwuyou.app.adapter;

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
import com.chewuwuyou.app.bean.Task;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.DateTimeUtil;
import com.chewuwuyou.app.utils.ServiceUtils;
import com.chewuwuyou.app.utils.WalletUtil;

import java.util.List;

/**
 * @describe:业务大厅适配器
 * @author:liuchun
 */
public class BusinessHallAdapter extends BaseAdapter {

	private List<Task> mAccountList;
	private Context mContext;
	private LayoutInflater mInflater;

	public BusinessHallAdapter(Context context,List<Task> mAccountList) {
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
			convertView = mInflater.inflate(R.layout.business_hall_item, null);
			viewCache = new BanKuaiViewCache();
			viewCache.TextTypeZong = (TextView) convertView.findViewById(R.id.text_type_zong);
			viewCache.TextOfferLogo = (TextView) convertView.findViewById(R.id.text_offer_logo);
			viewCache.TextOffer = (TextView) convertView.findViewById(R.id.text_offer);
			viewCache.TextSignLogo = (TextView) convertView.findViewById(R.id.text_sign_logo);
			viewCache.TextSign = (TextView) convertView.findViewById(R.id.text_sign);
			viewCache.TextType = (TextView) convertView.findViewById(R.id.text_type);
			viewCache.MakeOfferPeoplesManTV = (TextView) convertView.findViewById(R.id.make_offer_peoples_man_tv);
			viewCache.TextMerchant = (TextView) convertView.findViewById(R.id.text_merchant);
			convertView.setTag(viewCache);
		} else {
			viewCache = (BanKuaiViewCache) convertView.getTag();
		}
		viewCache.TextTypeZong.setText(mAccountList.get(position).getType());
		viewCache.TextType.setText(ServiceUtils.getProjectName(mAccountList.get(position).getProjectName()));
		viewCache.TextOffer.setText(String.valueOf(mAccountList.get(position).getOfferCnt()));
		viewCache.TextSign.setText(String.valueOf(mAccountList.get(position).getOrderCnt()));

		if (Integer.parseInt(mAccountList.get(position).getOrderCnt()) >= 10) {
			viewCache.MakeOfferPeoplesManTV.setVisibility(View.VISIBLE);
		} else {
			viewCache.MakeOfferPeoplesManTV.setVisibility(View.GONE);
		}
		if (mAccountList.get(position).getUserId().equals(CacheTools.getUserData("userId"))) {
			viewCache.TextMerchant.setTextColor(mContext.getResources().getColor(R.color.blue_normal));
			viewCache.TextMerchant.setText(DateTimeUtil.getDescriptionTimeFromTimestamp(mAccountList.get(position).getPubishTime()) + "   由我发布");
		} else {
			viewCache.TextMerchant.setTextColor(mContext.getResources().getColor(R.color.common_text_color));
			viewCache.TextMerchant.setText(DateTimeUtil.getDescriptionTimeFromTimestamp(mAccountList.get(position).getPubishTime())+ "  由服务商: "+ mAccountList.get(position).getName() + " 发布");
		}
		return convertView;
	}
	private class BanKuaiViewCache {
		private TextView TextTypeZong,TextOfferLogo,TextOffer,TextSignLogo,TextSign,TextType,MakeOfferPeoplesManTV,TextMerchant;
	}
}
