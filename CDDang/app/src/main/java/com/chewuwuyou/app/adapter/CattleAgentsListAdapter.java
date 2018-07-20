package com.chewuwuyou.app.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.TrafficBroker;
import com.chewuwuyou.app.utils.ImageUtils;

/**
 * @describe:车务经纪人列表
 * @author:yuyong
 * @version 1.1.0
 * @created:2014-11-12下午3:21:25
 */
public class CattleAgentsListAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater layoutInflater;
	private List<TrafficBroker> listItems;// 驾证信息集合
	// private String serviceProject = "";// 服务选项
	// private int mServiceType;
	ListItemView listItemView = null;

	public class ListItemView { // 自定义控件集合
		ImageView businessHeadIV;// 头像
		// ImageView isApproveIV;// 是否认证
		// ImageView isVIPIV;// 是否是VIP
		TextView businessLocationTV;// 服务地区
		TextView businessNameTV;// 商家名称
		RatingBar ratingBar;// 评星等级
		ImageView collectIV;//是否收藏
		TextView gradebranch;
		// LinearLayout ContactServiceLL;// 联系客服
		// LinearLayout DownOrderLL;
	}

	public CattleAgentsListAdapter(Context context,
			List<TrafficBroker> listItems, int serviceType) {
		this.mContext = context;

		layoutInflater = LayoutInflater.from(mContext);

		this.listItems = listItems;
		// this.mServiceType = serviceType;
	}

	public CattleAgentsListAdapter() {
		
	}
	@Override
	public int getCount() {
		return listItems.size();
	}

	@Override
	public Object getItem(int position) {
		return listItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			listItemView = new ListItemView();
			convertView = layoutInflater.inflate(R.layout.business_item_new1,
					null);
			listItemView.businessHeadIV = (ImageView) convertView
					.findViewById(R.id.business_head_iv);
			listItemView.businessNameTV = (TextView) convertView
					.findViewById(R.id.business_name_tv);
			// listItemView.isApproveIV = (ImageView) convertView
			// .findViewById(R.id.is_approve_iv);
			// listItemView.isVIPIV = (ImageView) convertView
			// .findViewById(R.id.is_vip_iv);

			listItemView.businessLocationTV = (TextView) convertView
					.findViewById(R.id.business_address_tv);

			listItemView.ratingBar = (RatingBar) convertView
					.findViewById(R.id.rating_bar);
			listItemView.collectIV=(ImageView) convertView.findViewById(R.id.is_collect_iv);
			// listItemView.ContactServiceLL = (LinearLayout) convertView
			// .findViewById(R.id.contact_service_ll);
			// listItemView.DownOrderLL = (LinearLayout) convertView
			// .findViewById(R.id.down_order_ll);
			
			listItemView.gradebranch = (TextView) convertView.findViewById(R.id.gradebranch);
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		final TrafficBroker traBroker = listItems.get(position);
		if (!TextUtils.isEmpty(traBroker.getUrl())) {
			ImageUtils.displayImage(traBroker.getUrl(),
					listItemView.businessHeadIV, 10);
		} else {
			listItemView.businessHeadIV.setImageResource(R.drawable.user_fang_icon);
		}
		
		listItemView.businessLocationTV.setText(traBroker.getLocation());
		listItemView.ratingBar.setRating(traBroker.getStar());
		listItemView.businessNameTV.setText(traBroker.getRealName());
		listItemView.gradebranch.setText(Float.parseFloat((traBroker.getStar())+"")+"分");
		if(traBroker.getIsfavorite()==0){
			listItemView.collectIV.setVisibility(View.GONE);
		}else{
			listItemView.collectIV.setVisibility(View.VISIBLE);
		}
		// listItemView.ContactServiceLL
		// .setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// Intent intent = new Intent(mContext, ChatActivity.class);
		// if (traBroker.getInteraction().size() > 0) {
		//
		// // List<Service> services = new
		// // ArrayList<Service>();
		// // services.clear();
		// // services.addAll(traBroker
		// // .getInteraction());
		// // Service busiService = new Service();
		// // busiService.setNick(traBroker
		// // .getBusinessName());
		// // busiService.setManagId(traBroker
		// // .getUserId());
		// // busiService.setUserId(traBroker
		// // .getUserId());
		// // busiService.setRole(1);
		// // services.add(busiService);
		//
		// int n = (int) (Math.random() * traBroker
		// .getInteraction().size());
		// intent.putExtra("to", traBroker.getInteraction()
		// .get(n).getUserId()
		// + "@iz232jtyxeoz");
		//
		// intent.putExtra("businessId", traBroker
		// .getInteraction().get(n).getUserId()
		// + "");
		// intent.putExtra("telephone", listItems
		// .get(position).getInteraction().get(n)
		// .getTelephone()
		// + "");
		// } else {
		// intent.putExtra("to", traBroker.getUserId()
		// + "@iz232jtyxeoz");
		// intent.putExtra("businessId", traBroker.getUserId()
		// + "");
		// intent.putExtra("telephone", listItems
		// .get(position).getMobile() + "");
		// }
		// intent.putExtra("nowBusiUserId", listItems
		// .get(position).getUserId() + "");
		// MyLog.i("YUY",
		// "----------商家id--" + traBroker.getUserId());
		// intent.putExtra("isOrderto", 1);// 区分由订单跳入
		// intent.putExtra("serviceType", mServiceType);
		// mContext.startActivity(intent);
		// }
		// });
		// listItemView.DownOrderLL.setOnClickListener(new
		// View.OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// if (!TextUtils.isEmpty(traBroker.getMobile())) {
		// AlertDialog.Builder dialog = new AlertDialog.Builder(
		// mContext);
		// dialog.setTitle("联系商家");
		// dialog.setMessage("确认联系"+traBroker.getBusinessName()+"-"+traBroker.getMobile());
		// dialog.setPositiveButton("确认", new DialogInterface.OnClickListener()
		// {
		//
		// @Override
		// public void onClick(DialogInterface arg0, int arg1) {
		// Intent phoneIntent = new Intent(
		// "android.intent.action.CALL", Uri.parse("tel:"
		// + traBroker.getMobile()));
		// mContext.startActivity(phoneIntent);
		// }
		// });
		// dialog.setNegativeButton("取消", new DialogInterface.OnClickListener()
		// {
		//
		// @Override
		// public void onClick(DialogInterface arg0, int arg1) {
		// arg0.dismiss();
		// }
		// });
		// dialog.show();
		// } else {
		// ToastUtil.toastShow(mContext, "商家暂未提供电话咨询，请与在线客服联系");
		// }
		// delete start by yuyong 由于订单流程的修改 此处不用下单，直接电话咨询
		// if (mServiceType != 0) {
		// Intent intent = new Intent(mContext,
		// BusinessNewOrderActivity.class);
		// intent.putExtra("serviceType", mServiceType);
		// if (traBroker.getInteraction().size() > 0) {
		// int n = (int) (Math.random() * traBroker
		// .getInteraction().size());
		// intent.putExtra("businessId", traBroker
		// .getInteraction().get(n).getManagId()
		// + "");
		// } else {
		// intent.putExtra("businessId", traBroker.getUserId()
		// + "");
		// }
		// intent.putExtra("nowBusiUserId", traBroker.getUserId() + "");
		// mContext.startActivity(intent);
		// } else {
		// Intent intent = new Intent(mContext,
		// BusinessHandlingActivity.class);
		// if (traBroker.getInteraction().size() > 0) {
		// int n = (int) (Math.random() * traBroker
		// .getInteraction().size());
		// intent.putExtra("businessId", traBroker
		// .getInteraction().get(n).getManagId()
		// + "");
		// } else {
		// intent.putExtra("businessId", traBroker.getUserId()
		// + "");
		// }
		// intent.putExtra("nowBusiUserId", traBroker.getUserId() + "");
		// mContext.startActivity(intent);
		// }
		// Intent intent = new Intent(mContext,
		// BusinessNewOrderActivity.class);
		// intent.putExtra("businessId",
		// traBroker.getId());
		// mContext.startActivity(intent);
		// delete end
		// }
		// });
		// int gz = traBroker.getFavoritesize();
		// int isGz = traBroker.getIsfavorite();
		//
		// listItemView.mUserAttentionTV.setText(isGz == 1 ? mContext.getString(
		// R.string.user_attentied, gz) : mContext.getString(
		// R.string.user_attention, gz));
		// listItemView.mUserAttentionTV.setCompoundDrawablesWithIntrinsicBounds(
		// (isGz == 1 ? mContext.getResources().getDrawable(
		// R.drawable.panel_gzz) : mContext.getResources()
		// .getDrawable(R.drawable.icon_gz)), null, null, null);
		// listItemView.mAttentionTV.setOnCheckedChangeListener(new
		// GzCheckListener(position));

		return convertView;
	}
}
