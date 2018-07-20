package com.chewuwuyou.eim.view;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.ChatUserInfo;
import com.chewuwuyou.app.ui.PersonalHomeActivity2;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.eim.activity.im.ChatActivity;
import com.chewuwuyou.eim.manager.NoticeManager;
import com.chewuwuyou.eim.manager.XmppConnectionManager;
import com.chewuwuyou.eim.model.Notice;
import com.chewuwuyou.eim.util.StringUtil;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

public class NoticeAdapter extends BaseAdapter implements View.OnClickListener {
    private LayoutInflater mInflater;
    private List<Notice> inviteNotices;
    private Context context;
    //  private List<ChatUserInfo> mChatUsers = new ArrayList<ChatUserInfo>();

    public NoticeAdapter(Context context, List<Notice> inviteUsers) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.inviteNotices = inviteUsers;
    }

    public void setNoticeList(List<Notice> inviteUsers) {
        this.inviteNotices = inviteUsers;
    }

    @Override
    public int getCount() {
        return inviteNotices == null ? 0 : inviteNotices.size();
    }

    @Override
    public Object getItem(int position) {
        return inviteNotices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Integer position_integer = Integer.valueOf(position);
        final Notice notice = inviteNotices.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.add_friend_item, null);
            holder = new ViewHolder();
            holder.headIV = (ImageView) convertView.findViewById(R.id.user_head_iv);
            holder.userNickNameTV = (TextView) convertView.findViewById(R.id.user_nick_name_tv);
            // holder.addFriendDescriptionTV = (TextView) convertView
            // .findViewById(R.id.user_add_friend_descrption_tv);
            holder.addFriendBtn = (Button) convertView.findViewById(R.id.add_friend_agree_btn);
            //     holder.refuseFriendBtn = (Button) convertView.findViewById(R.id.add_friend_refuse_btn);
            holder.resultTV = (TextView) convertView.findViewById(R.id.friend_get_result_tv);
            holder.mTextView_delete = (TextView) convertView.findViewById(R.id.delete);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // if (notice.getNoticeType() == Notice.ADD_FRIEND) {// 添加好友未处理
        // 加气泡，处理的就消失了整体

        holder.userNickNameTV.setText(inviteNotices.get(position).getName());
        // 描述：后面描述加好友请求说明
        // holder.addFriendDescriptionTV.setText(notice.getContent());

        if (notice.getAddState() == Notice.AGREE || notice.getAddState() == Notice.REFUSE) {

            holder.addFriendBtn.setVisibility(View.GONE);
            holder.resultTV.setVisibility(View.VISIBLE);
            if (notice.getAddState() == Notice.AGREE) {
                holder.resultTV.setText("已同意");
            } else if (notice.getAddState() == Notice.REFUSE) {
                holder.resultTV.setText("已拒绝");
            }

        } else {
            holder.addFriendBtn.setVisibility(View.VISIBLE);
            holder.resultTV.setVisibility(View.GONE);
        }
        final Button addFriendBtn = holder.addFriendBtn;

        final TextView friendGetResuleTV = holder.resultTV;
        holder.addFriendBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // 接受请求

              //  ProgressDialog m = ProgressDialog.show(context);

                AjaxParams params = new AjaxParams();
                params.put("userId", CacheTools.getUserData("rongUserId"));
                params.put("friendId", notice.getUserId());
                NetworkUtil.postMulti(NetworkUtil.AGREE_FRIEND, params, new AjaxCallBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        super.onSuccess(s);


                        friendGetResuleTV.setText("已通过");
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo, String strMsg) {
                        super.onFailure(t, errorNo, strMsg);
                    }
                });


            }
        });

        // }
        // else
        // if (Notice.SYS_MSG == notice.getNoticeType())
        // {// 如果系统消息未读，加气泡
        // holder.itemIcon.setBackgroundResource(R.drawable.icon_message);
        // holder.newContent.setText(notice.getContent());
        // }
        // holder.newTitle.setText(notice.getTitle());
        // holder.newDate.setText(notice.getNoticeTime().substring(5, 19));
        // holder.newContent.setTag(notice);
        // if (Notice.UNREAD == notice.getStatus()) {
        // holder.paopao.setText("");
        // holder.paopao.setVisibility(View.VISIBLE);
        // } else {
        // holder.paopao.setVisibility(View.GONE);
        // }
        holder.headIV.setOnClickListener(this);

        holder.headIV.setTag(position_integer);

        holder.mTextView_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inviteNotices.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    private class ViewHolder {
        public ImageView headIV;
        public TextView userNickNameTV;
        // public TextView addFriendDescriptionTV;
        public Button addFriendBtn;
        public TextView resultTV;
        TextView mTextView_delete;
    }

    /**
     * 回复一个presence信息给用户
     *
     * @param type
     * @param to
     */
    protected void sendSubscribe(Presence.Type type, String to) {
        Presence presence = new Presence(type);
        presence.setTo(to);
        try {
            XmppConnectionManager.getInstance().getConnection().sendPacket(presence);
        } catch (NotConnectedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加一个联系人
     *
     * @param userJid  联系人JID
     * @param nickname 联系人昵称
     * @param groups   联系人添加到哪些组
     * @throws XMPPException
     */
    protected void createSubscriber(String userJid, String nickname, String[] groups) throws XMPPException {
        try {
            XmppConnectionManager.getInstance().getConnection().getRoster().createEntry(userJid, nickname, groups);
        } catch (NotLoggedInException e) {
            e.printStackTrace();
        } catch (NoResponseException e) {
            e.printStackTrace();
        } catch (NotConnectedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View arg0) {
        Object tag = arg0.getTag();
        Integer position_integer = null;
        if (tag instanceof Integer) {
            position_integer = (Integer) tag;
        }
        switch (arg0.getId()) {
            case R.id.user_head_iv:

                Intent intent = new Intent(context, PersonalHomeActivity2.class);
                intent.putExtra("userId", Integer.valueOf(inviteNotices.get(position_integer).getFrom().split("@")[0]));
                context.startActivity(intent);
                break;
        }
    }
}
