package com.chewuwuyou.app.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.AccountWithdrawal;
import com.chewuwuyou.app.bean.GroupSetUpMember;
import com.chewuwuyou.app.bean.GroupSetUpMemberInformationEr;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.WalletUtil;

import java.util.List;

/**
 * @describe:提现账户列表adapter
 * @author:liuchun
 * @version
 * @created:
 */
public class GroupSetUpMemberAdapter extends BaseAdapter {

	private List<GroupSetUpMemberInformationEr> mAccountList;
	private Context mContext;
	private LayoutInflater mInflater;

	public GroupSetUpMemberAdapter(Context context,List<GroupSetUpMemberInformationEr> mAccountList) {
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
			convertView = mInflater.inflate(R.layout.item_group_setup_member, null);
			viewCache = new BanKuaiViewCache();
			viewCache.mGroupSetupMemberName = (TextView) convertView.findViewById(R.id.group_setup_member_name);
			viewCache.mGroupSetupMemberImg = (ImageView) convertView.findViewById(R.id.group_setup_member_img);
			convertView.setTag(viewCache);
		} else {
			viewCache = (BanKuaiViewCache) convertView.getTag();
		}
		if(!TextUtils.isEmpty(mAccountList.get(position).getUser_friend_name())&&!mAccountList.get(position).getUser_friend_name().equals("null")){
			viewCache.mGroupSetupMemberName.setText(mAccountList.get(position).getUser_friend_name());
		}else if(!TextUtils.isEmpty(mAccountList.get(position).getUser_group_name())&&!mAccountList.get(position).getUser_group_name().equals("null")){
			viewCache.mGroupSetupMemberName.setText(mAccountList.get(position).getUser_group_name());
		}else{
			viewCache.mGroupSetupMemberName.setText(mAccountList.get(position).getUser_own_name());
		}

		if(mAccountList.get(position).getAccid().equals("gengduo")){
			viewCache.mGroupSetupMemberImg.setImageResource(R.drawable.group_more);
		}else if(mAccountList.get(position).getAccid().equals("jia")){
			viewCache.mGroupSetupMemberImg.setImageResource(R.drawable.group_portrait_img);
		}else if(mAccountList.get(position).getAccid().equals("jian")){
			viewCache.mGroupSetupMemberImg.setImageResource(R.drawable.group_delete);
		}else{
			Glide.with(mContext).load(mAccountList.get(position).getHead_image_url()).crossFade().placeholder(R.drawable.user_fang_icon).error(R.drawable.user_fang_icon).into(viewCache.mGroupSetupMemberImg);
		}
		return convertView;
	}

	final class BanKuaiViewCache {
		TextView mGroupSetupMemberName;// 名称，电话
		ImageView mGroupSetupMemberImg;// 图片
	}
}
