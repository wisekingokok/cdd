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
import com.chewuwuyou.app.bean.LogisticsCompany;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.WalletUtil;

import java.util.List;

/**
 * @describe:物流公司名称
 * @author:liuchun
 * @version
 * @created:
 */
public class LogisticsCompanyAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private List<LogisticsCompany> mLogisticsCompanies;

	public LogisticsCompanyAdapter(Context context,List<LogisticsCompany> mLogisticsCompanies) {
		this.mContext = context;
		this.mLogisticsCompanies = mLogisticsCompanies;
		this.mInflater = LayoutInflater.from(mContext);
	}

	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * @param list
	 */
	public void updateListView(List<LogisticsCompany> list){
		this.mLogisticsCompanies = mLogisticsCompanies;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mLogisticsCompanies.size();
	}

	@Override
	public Object getItem(int position) {
		return mLogisticsCompanies.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final BanKuaiViewCache viewCache;
		final LogisticsCompany mContent = mLogisticsCompanies.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.logistics_name, null);
			viewCache = new BanKuaiViewCache();
			viewCache.tvTitle = (TextView) convertView.findViewById(R.id.title);
			viewCache.tvLetter = (TextView) convertView.findViewById(R.id.catalog);
			convertView.setTag(viewCache);
		} else {
			viewCache = (BanKuaiViewCache) convertView.getTag();
		}
       //根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);
		//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if(position == getPositionForSection(section)){
			viewCache.tvLetter.setVisibility(View.VISIBLE);
			viewCache.tvLetter.setText(mContent.getSortLetters());
		}else{
			viewCache.tvLetter.setVisibility(View.GONE);
		}
		viewCache.tvTitle.setText(this.mLogisticsCompanies.get(position).getCommpanyName());
		return convertView;
	}

	class BanKuaiViewCache {
		TextView tvLetter;
		TextView tvTitle;
	}
	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return mLogisticsCompanies.get(position).getSortLetters().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = mLogisticsCompanies.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 *
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String  sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}
	public Object[] getSections() {
		return null;
	}
}
