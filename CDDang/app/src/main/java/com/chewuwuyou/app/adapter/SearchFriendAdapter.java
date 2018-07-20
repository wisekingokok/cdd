package com.chewuwuyou.app.adapter;

import java.util.List;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.XMPPException;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.SearchFriend;
import com.chewuwuyou.app.ui.PersonalHomeActivity2;
import com.chewuwuyou.app.utils.CarFriendQuanUtils;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.IsNetworkUtil;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.eim.manager.XmppConnectionManager;
import com.chewuwuyou.eim.util.StringUtil;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:附近的车友
 * @author:yuyong
 * @date:2015-4-9下午1:59:23
 * @version:1.2.1
 */
public class SearchFriendAdapter extends BaseAdapter implements View.OnClickListener {

	private Context mContext;
	private LayoutInflater layoutInflater;
	private List<SearchFriend> mSearchFriends;

	public SearchFriendAdapter(Context context, List<SearchFriend> searchFriends) {
		this.mContext = context;
		this.mSearchFriends = searchFriends;
		layoutInflater = LayoutInflater.from(mContext);
	}

	public class ListItemView {
		ImageView mCarFriendHeadIV;
		TextView mCarFriendNameTV;
		TextView mCarFriendDistanceTV;
		TextView mCarFriendSingTureTV;// 车友心情
		ImageView mCarFriendSexIV;// 性别图片
		Button mAddFriendBtn;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mSearchFriends.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mSearchFriends.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ListItemView listItemView = null;
		final Integer position_integer = Integer.valueOf(position);
		if (convertView == null) {
			listItemView = new ListItemView();
			convertView = layoutInflater.inflate(R.layout.search_friend_item, null);
			listItemView.mCarFriendHeadIV = (ImageView) convertView.findViewById(R.id.car_friend_head_iv);
			listItemView.mCarFriendNameTV = (TextView) convertView.findViewById(R.id.car_friend_name_tv);
			listItemView.mCarFriendDistanceTV = (TextView) convertView.findViewById(R.id.car_friend_distance_tv);
			listItemView.mCarFriendSingTureTV = (TextView) convertView.findViewById(R.id.car_friend_singture_tv);
			listItemView.mCarFriendSexIV = (ImageView) convertView.findViewById(R.id.car_friend_sex_iv);
			listItemView.mAddFriendBtn = (Button) convertView.findViewById(R.id.add_friend_btn);
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		final SearchFriend searchFriend = mSearchFriends.get(position);
		if (!TextUtils.isEmpty(searchFriend.getUrl())) {
			ImageUtils.displayImage(searchFriend.getUrl(), listItemView.mCarFriendHeadIV, 360);
		} else {
			listItemView.mCarFriendHeadIV.setImageResource(R.drawable.xiaodangmei);
		}
		listItemView.mCarFriendHeadIV.setOnClickListener(this);
		listItemView.mCarFriendHeadIV.setTag(position_integer);
		listItemView.mCarFriendNameTV.setText(CarFriendQuanUtils.showCarFriendName(searchFriend.getNoteName(),
				searchFriend.getNickName(), searchFriend.getUserName()));
		listItemView.mCarFriendSingTureTV.setText(searchFriend.getQianming());

		if (searchFriend.getSex().equals("0")) {
			listItemView.mCarFriendSexIV.setImageResource(R.drawable.man);
		} else if (searchFriend.getSex().equals("1")) {
			listItemView.mCarFriendSexIV.setImageResource(R.drawable.woman);
		} else {
			listItemView.mCarFriendSexIV.setImageResource(R.drawable.icon_nosex);
		}

		if (searchFriend.getFriend().equals("1")) {
			listItemView.mAddFriendBtn.setBackgroundResource(0);
			listItemView.mAddFriendBtn.setTextColor(Color.parseColor("#c0c0c0"));
			listItemView.mAddFriendBtn.setText("好友");
			listItemView.mAddFriendBtn.setOnClickListener(null);
		} else {
			listItemView.mAddFriendBtn.setBackgroundResource(R.drawable.common_red_btn_bg);
			listItemView.mAddFriendBtn.setText("添 加");
			listItemView.mAddFriendBtn.setTextColor(mContext.getResources().getColor(R.color.white));
			final Button addFriendBtn = listItemView.mAddFriendBtn;
			addFriendBtn.setOnClickListener(new OnClickListener() {

				@SuppressWarnings("static-access")
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					AjaxParams params = new AjaxParams();
					params.put("userId", String.valueOf(searchFriend.getId()));
					new NetworkUtil().postMulti(NetworkUtil.ADD_FRIEND_SEND, params, new AjaxCallBack<String>() {

						@Override
						public void onSuccess(String t) {
							// TODO Auto-generated method stub
							super.onSuccess(t);
							try {
								JSONObject jo = new JSONObject(t.toString());
								if (jo.getInt("result") == 1) {
									addFriendBtn.setBackgroundResource(0);
									addFriendBtn.setTextColor(Color.parseColor("#c0c0c0"));
									addFriendBtn.setText("好友");
									MyLog.i("YUY", "加好友发送推送成功");
									try {
										createSubscriber(StringUtil.getJidByName(String.valueOf(searchFriend.getId())),
												null, null);
										Toast.makeText(mContext, R.string.wait_friend_agree, Toast.LENGTH_LONG).show();
									} catch (XMPPException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								} else {
									addFriendBtn.setBackgroundResource(R.drawable.common_red_btn_bg);
									addFriendBtn.setTextColor(mContext.getResources().getColor(R.color.white));
									addFriendBtn.setText("添 加");
									MyLog.i("YUY", "加好友发送推送失败");
									Toast.makeText(mContext, "加好友失败请重试", Toast.LENGTH_LONG).show();
								}

							} catch (JSONException e) {
								// TODO Auto-generated catch
								// block
								e.printStackTrace();
							}
						}

						@Override
						public void onFailure(Throwable t, int errorNo, String strMsg) {
							// TODO Auto-generated method stub
							super.onFailure(t, errorNo, strMsg);
							MyLog.i("YUY", "======fail===" + strMsg);
							Message msg = new Message();
							msg.what = Constant.NET_REQUEST_FAIL;
							if (IsNetworkUtil.isNetworkAvailable(mContext) == false) {
								Toast toast = Toast.makeText(mContext, "网络不可用，请检查您的网络是否连接", Toast.LENGTH_LONG);
								// 可以控制toast显示的位置
								toast.setGravity(Gravity.BOTTOM, 0, 10);
								toast.show();
							} else {
								Toast toast = Toast.makeText(mContext, "网络异常，请稍后再试", Toast.LENGTH_LONG);
								// 可以控制toast显示的位置
								toast.setGravity(Gravity.BOTTOM, 0, 10);
								toast.show();

							}

						}
					});
				}
			});
		}
		return convertView;
	}

	public void updateData(List<SearchFriend> data) {
		this.mSearchFriends = data;
	}

	/**
	 * 添加一个联系人
	 * 
	 * @param userJid
	 *            联系人JID
	 * @param nickname
	 *            联系人昵称
	 * @param groups
	 *            联系人添加到哪些组
	 * @throws XMPPException
	 */
	protected void createSubscriber(String userJid, String nickname, String[] groups) throws XMPPException {
		try {
			XmppConnectionManager.getInstance().getConnection().getRoster().createEntry(userJid, nickname, groups);
		} catch (NotLoggedInException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Object tag = arg0.getTag();
		Integer position_integer = null;
		if (tag instanceof Integer) {
			position_integer = (Integer) tag;
		}
		switch (arg0.getId()) {
		case R.id.car_friend_head_iv:
			Intent intent = new Intent(mContext, PersonalHomeActivity2.class);
			intent.putExtra("userId", mSearchFriends.get(position_integer).getId());
			mContext.startActivity(intent);
			break;
		}
	}

}
