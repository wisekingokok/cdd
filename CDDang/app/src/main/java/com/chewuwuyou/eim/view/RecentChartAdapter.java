package com.chewuwuyou.eim.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.UserChatHistory;
import com.chewuwuyou.app.utils.CarFriendQuanUtils;
import com.chewuwuyou.app.utils.ChatUtils;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.JsonUtil;
import com.chewuwuyou.app.widget.BadgeView;
import com.chewuwuyou.eim.activity.im.ChatActivity;
import com.chewuwuyou.eim.manager.ContacterManager;
import com.chewuwuyou.eim.manager.XmppConnectionManager;
import com.chewuwuyou.eim.model.User;

import java.util.List;
import java.util.Map;

@SuppressLint("InflateParams")
public class RecentChartAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private Map<String, String> mFaceCharacterMap;
    private List<UserChatHistory> mUserChatHistories;
    private Handler mHandler;

    public RecentChartAdapter(Context context,
                              List<UserChatHistory> userChatHistories, Handler mHandler) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mUserChatHistories = userChatHistories;
        this.mFaceCharacterMap = JsonUtil.getFaceStrMap(mContext);
        this.mHandler = mHandler;
    }

    @Override
    public int getCount() {
        return mUserChatHistories.size();
    }

    @Override
    public Object getItem(int position) {
        return mUserChatHistories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final UserChatHistory notice = mUserChatHistories.get(position);
        Integer ppCount = notice.getNoticeSum();
        ViewHolderx holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.recent_chart_item, null);
            holder = new ViewHolderx();
            holder.newTitle = (TextView) convertView
                    .findViewById(R.id.new_title);
            holder.itemIcon = (ImageView) convertView
                    .findViewById(R.id.new_icon);
            holder.newContent = (TextView) convertView
                    .findViewById(R.id.new_content);
            holder.delete = (TextView) convertView.findViewById(R.id.delete);

            holder.newDate = (TextView) convertView.findViewById(R.id.new_date);
            holder.mNewMessageView = new BadgeView(mContext, holder.newContent);
            holder.mNewMessageView
                    .setBadgePosition(BadgeView.POSITION_BOTTOM_RIGHT);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderx) convertView.getTag();
        }
        String jid = notice.getFrom();
        User u = ContacterManager.getNickname(jid, XmppConnectionManager
                .getInstance().getConnection());
        if (null == u) {
            u = new User();
            // 普通聊天
            if (jid.contains("@")) {
                u.setJID(jid);
                // 群聊
            } else if (!TextUtils.isEmpty(notice.getRoom())) {
                u.setJID(notice.getRoom());
            } else {
                u.setJID(jid);
            }
            u.setName(jid);
        }
        final String userName;
        if (!TextUtils.isEmpty(notice.getRoom())) {
            holder.itemIcon.setImageResource(R.drawable.user_fang_icon);
            userName = notice.getRoom().split("@")[0];
            ChatUtils.showChatMsg(mContext,
                    notice.getFrom() + ":" + notice.getMessage(),
                    mFaceCharacterMap, holder.newContent);
        } else if (u.getJID().equals("xiaoding@iz232jtyxeoz")) {
            holder.itemIcon.setImageResource(R.drawable.xiaodangdi);
            userName = u.getName();
            ChatUtils.showChatMsg(mContext, notice.getMessage(),
                    mFaceCharacterMap, holder.newContent);
        } else if (u.getJID().equals("xiaodang@iz232jtyxeoz")) {
            holder.itemIcon.setImageResource(R.drawable.user_fang_icon);
            userName = u.getName();
            ChatUtils.showChatMsg(mContext, notice.getMessage(),
                    mFaceCharacterMap, holder.newContent);
        } else {
            // modify start by yuyong 2016/04/20 修改偶尔图片错位现象
            holder.itemIcon.setTag(notice.getUrl());
            // modify start by yuyong 2016/04/20 修改偶尔图片错位现象
            ImageUtils.displayImage(notice.getUrl(), holder.itemIcon, 10,
                    R.drawable.user_fang_icon, R.drawable.user_fang_icon);
            userName = CarFriendQuanUtils.showCarFriendName(
                    notice.getNoteName(), notice.getNickName(), u.getName());
            ChatUtils.showChatMsg(mContext, notice.getMessage(),
                    mFaceCharacterMap, holder.newContent);
        }
        holder.newTitle.setText(userName);

        holder.newContent.setTag(u);
        holder.newDate.setText(notice.getTime().substring(5, 16));
//        if (u.isAvailable()) {// 根据在线情况给头像设置透明度
//            holder.itemIcon.setAlpha(1.0f);
//        } else {
//            holder.itemIcon.setAlpha(0.5f);
//        }
        if (ppCount != null) {
            if (ppCount > 99) {
                holder.mNewMessageView.setText("99+");
                holder.mNewMessageView.show();
            } else if (ppCount > 0) {
                holder.mNewMessageView.setText(ppCount + "");
                holder.mNewMessageView.show();
            } else {
                holder.mNewMessageView.hide();
            }
        }

        holder.delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.obj = position;
                mHandler.sendMessage(message);
            }
        });
//        convertView.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                String jid = mUserChatHistories.get(position).getFrom();
//                String room = mUserChatHistories.get(position).getRoom();// 获得群聊房间ID
//                User user = ContacterManager.getNickname(jid,
//                        XmppConnectionManager.getInstance().getConnection());
//                Intent intent = new Intent(mContext, ChatActivity.class);
//                if (!TextUtils.isEmpty(room)) {// 判断群聊情况
//                    intent.putExtra("to", room);
//                } else if (user == null) {
//                    intent.putExtra("to", jid);
//                } else {
//                    intent.putExtra("to", user.getJID());
//                }
//                intent.putExtra("title", userName);
//                mContext.startActivity(intent);
//
//            }
//        });
        return convertView;
    }

    public class ViewHolderx {
        ImageView itemIcon;
        TextView newTitle;
        TextView newContent;
        TextView newDate;
        TextView paopao, delete;
        BadgeView mNewMessageView;

    }
}