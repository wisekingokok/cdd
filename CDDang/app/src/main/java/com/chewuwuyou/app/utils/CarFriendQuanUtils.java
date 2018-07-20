package com.chewuwuyou.app.utils;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;

import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.chewuwuyou.app.bean.ChatUserInfo;
import com.chewuwuyou.app.bean.HotTieItem;
import com.chewuwuyou.app.bean.QuanDetailHeaderEntity;
import com.chewuwuyou.app.bean.QuanItem;
import com.chewuwuyou.app.bean.QuanPingItem;
import com.chewuwuyou.app.bean.TieDetailHeaderEntity;
import com.chewuwuyou.app.bean.TieItem;
import com.chewuwuyou.app.bean.TiePingItem;
import com.chewuwuyou.app.bean.YueDetailHeaderEntity;
import com.chewuwuyou.app.bean.YueItem;
import com.chewuwuyou.app.bean.YuePingItem;
import com.chewuwuyou.app.bean.ZanItem;

/**
 * @Title:chedingdang
 * @Copyright:chengdu chewuwuyou company
 * @Description:车友圈工类
 * @author:yuyong
 * @date:2015-4-9下午4:52:37
 * @version:1.2.1
 */
public class CarFriendQuanUtils {
	/**
	 * 通过三者来区分:层级依次优先
	 * 
	 * @param noteName
	 *            备注名
	 * @param nickName
	 *            昵称
	 * @param userName
	 *            用户名
	 * @return
	 */
	public static String showCarFriendName(String noteName, String nickName, String userName) {

		if (!TextUtils.isEmpty(noteName)) {
			return noteName;
		} else if (!TextUtils.isEmpty(nickName)) {
			return nickName;
		} else {
			return userName;
		}
	}

	/**
	 * 通过三者来区分:层级依次优先
	 *            用户名
	 * @return
	 */
	public static String showCarFriendName(Object obj) {
		String noteName = null, nickName = null, userName = null;
		if (obj instanceof QuanItem) {
			noteName = ((QuanItem) obj).getNoteName();
			nickName = ((QuanItem) obj).getNickName();
			userName = ((QuanItem) obj).getUserName();
		} else if (obj instanceof YueItem) {
			noteName = ((YueItem) obj).getNoteName();
			nickName = ((YueItem) obj).getChief();
			userName = ((YueItem) obj).getUserName();
		} else if (obj instanceof TieItem) {
			noteName = ((TieItem) obj).getNoteName();
			nickName = ((TieItem) obj).getNickName();
			userName = ((TieItem) obj).getUserName();
		} else if (obj instanceof HotTieItem) {
			noteName = ((HotTieItem) obj).getNoteName();
			nickName = ((HotTieItem) obj).getNickName();
			userName = ((HotTieItem) obj).getUserName();
		} else if (obj instanceof QuanPingItem) {
			noteName = ((QuanPingItem) obj).getNoteName();
			nickName = ((QuanPingItem) obj).getNickName();
		} else if (obj instanceof YuePingItem) {
			noteName = ((YuePingItem) obj).getNoteName();
			nickName = ((YuePingItem) obj).getNickName();
		} else if (obj instanceof TiePingItem) {
			noteName = ((TiePingItem) obj).getNoteName();
			nickName = ((TiePingItem) obj).getNickName();
		} else if (obj instanceof QuanDetailHeaderEntity) {
			noteName = ((QuanDetailHeaderEntity) obj).getNoteName();
			nickName = ((QuanDetailHeaderEntity) obj).getNickName();
		} else if (obj instanceof YueDetailHeaderEntity) {
			noteName = ((YueDetailHeaderEntity) obj).getNoteName();
			userName = ((YueDetailHeaderEntity) obj).getUserName();
		} else if (obj instanceof TieDetailHeaderEntity) {
			noteName = ((TieDetailHeaderEntity) obj).getNoteName();
			nickName = ((TieDetailHeaderEntity) obj).getNickName();
		} else if (obj instanceof ZanItem) {
			noteName = ((ZanItem) obj).getNoteName();
			nickName = ((ZanItem) obj).getNickName();
		}
		if (!TextUtils.isEmpty(noteName)) {
			return noteName;
		} else if (!TextUtils.isEmpty(nickName)) {
			return nickName;
		} else {
			return userName;
		}
	}

	@SuppressWarnings("static-access")
	public static void setUserName(String userId, final TextView mUserNameTV, final ImageView mUserHeadIV) {
		AjaxParams params = new AjaxParams();
		params.put("ids", userId);
		new NetworkUtil().postMulti(NetworkUtil.GET_CHAT_USER_INFO, params, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
//				MyLog.i("YUY", "=========聊天界面他人的头像====" + t);
				try {
					JSONArray jArray = new JSONArray(t);
					ChatUserInfo mChatUser = ChatUserInfo.parse(jArray.get(0).toString());

					String noteName = mChatUser.getNoteName();
					String nickName = mChatUser.getNick();
					String userName = mChatUser.getUserName();
					if (!TextUtils.isEmpty(noteName)) {
						mUserNameTV.setText(noteName);
					} else if (!TextUtils.isEmpty(nickName)) {
						mUserNameTV.setText(nickName);
					} else {
						mUserNameTV.setText(userName);
					}
					ImageUtils.displayImage(mChatUser.getUrl(), mUserHeadIV, 360);
							// ImageLoader.getInstance().displayImage(NetworkUtil.IMAGE_BASE_URL
							// + mChatUser.getUrl(), mUserHeadIV,
							// new
							// DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true).displayer(new RoundedBitmapDisplayer(360)).build());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
}
