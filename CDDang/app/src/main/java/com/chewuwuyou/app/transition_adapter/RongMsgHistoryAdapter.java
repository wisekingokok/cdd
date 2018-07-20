package com.chewuwuyou.app.transition_adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.barcode.view.SweepLayout;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.ImageLoaderBuilder;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.ServerUtils;
import com.chewuwuyou.app.utils.ServiceUtils;
import com.chewuwuyou.rong.bean.CDDTaskMsg;
import com.chewuwuyou.rong.bean.CDDYWZMsg;
import com.chewuwuyou.rong.bean.ChangeGroupBean;
import com.chewuwuyou.rong.bean.ClearMessagesBean;
import com.chewuwuyou.rong.bean.GroupBean;
import com.chewuwuyou.rong.bean.GroupNTFMsgDataBean;
import com.chewuwuyou.rong.bean.InformationNotificationMessage;
import com.chewuwuyou.rong.bean.MsgDataBean;
import com.chewuwuyou.rong.bean.RongUserBean;
import com.chewuwuyou.rong.bean.TaskMsgBean;
import com.chewuwuyou.rong.utils.CDDRongApi;
import com.chewuwuyou.rong.utils.Constant;
import com.chewuwuyou.rong.utils.RongApi;
import com.chewuwuyou.rong.utils.RongMsgType;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.GroupNotificationMessage;
import io.rong.message.TextMessage;

/**
 * Created by xxy on 2016/9/12 0012.
 */
public class RongMsgHistoryAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<Conversation> mUserChatHistories;
    private String sendId;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");//yyyy-MM-dd H:m:s
    private SimpleDateFormat dayFormat = new SimpleDateFormat("MM-dd");//yyyy-MM-dd H:m:s
    private SimpleDateFormat todayFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Map<String, RongUserBean> rongUserBeanMap = new HashMap<>();
    private Map<String, GroupBean> groupBeanMap = new HashMap<>();
    private List<String> isGo = new ArrayList<>();
    private String todayFormatStr;

    public RongMsgHistoryAdapter(Context context) {
        if (context == null) {
            return;
        }
        this.mContext = context;
        this.mUserChatHistories = new ArrayList<Conversation>();
        mInflater = LayoutInflater.from(context);

        sendId = CacheTools.getUserData("rongUserId");
        //加入发送订单提醒用户信及系统提醒用户信息
        RongUserBean rongUserBean = new RongUserBean();
        rongUserBean.setNoteName("订单提醒");
        rongUserBean.setUserId(Constant.USER_ID_TYPE.ORDER_MSG + "");
        rongUserBeanMap.put(Constant.USER_ID_TYPE.ORDER_MSG + "", rongUserBean);
        RongUserBean rongUserBean1 = new RongUserBean();
        rongUserBean1.setNoteName("车当当");
        rongUserBean1.setUserId(Constant.USER_ID_TYPE.SYSTEM_MSG + "");
        rongUserBeanMap.put(Constant.USER_ID_TYPE.SYSTEM_MSG + "", rongUserBean1);
        RongUserBean rongUserBean2 = new RongUserBean();
        rongUserBean2.setNoteName("客服");
        rongUserBean2.setUserId(Constant.SERVER_ID);
        todayFormatStr = todayFormat.format(new Date());
    }

    public RongUserBean getRongUserById(String targetId) {
        return rongUserBeanMap.get(targetId);
    }

    public GroupBean getGroupBean(String targetId) {
        return groupBeanMap.get(targetId);
    }

    public void setData(List<Conversation> userChatHistories) {
        this.mUserChatHistories = userChatHistories == null ? new ArrayList<Conversation>() : userChatHistories;
        notifyDataSetChanged();
    }

    public void changeGroup(ChangeGroupBean changeGroupBean) {
        if (!TextUtils.isEmpty(changeGroupBean.targetId)) {
            if (groupBeanMap.get(changeGroupBean.targetId) != null)
                groupBeanMap.remove(changeGroupBean.targetId);
        } else
            groupBeanMap.clear();
        notifyDataSetChanged();
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
        Conversation notice = mUserChatHistories.get(position);
        ViewHolderx holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_rong_msg_history, null);
            holder = new ViewHolderx();
            holder.ll = (RelativeLayout) convertView.findViewById(R.id.ll);
            holder.newTitle = (TextView) convertView.findViewById(R.id.new_title);
            holder.itemIcon = (ImageView) convertView.findViewById(R.id.new_icon);
            holder.newContent = (TextView) convertView.findViewById(R.id.new_content);
            holder.delete = (TextView) convertView.findViewById(R.id.delete);
            holder.newDate = (TextView) convertView.findViewById(R.id.new_date);
            holder.tip = (TextView) convertView.findViewById(R.id.tip);
            holder.more = (TextView) convertView.findViewById(R.id.more);
            holder.sweepLayout = (SweepLayout) convertView.findViewById(R.id.swipeLayout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderx) convertView.getTag();
        }
        holder.BindData(notice, position);
        return convertView;
    }

    public class ViewHolderx {
        TextView more;
        ImageView itemIcon;
        TextView newTitle;
        TextView newContent;
        TextView newDate;
        RelativeLayout ll;
        TextView delete;
        TextView tip;
        SweepLayout sweepLayout;

        public void BindData(final Conversation notice, final int postion) {
            newTitle.setText(notice.getTargetId());
            Date date = new Date(notice.getSentTime());
            if (todayFormatStr.equals(todayFormat.format(date))) {
                newDate.setText(simpleDateFormat.format(date));
            } else {
                newDate.setText(dayFormat.format(date));
            }
            String objectName = notice.getObjectName();
            if (objectName.equals(RongMsgType.CDD_TXT_MSG)) {//颜文字
                CDDYWZMsg msg = (CDDYWZMsg) notice.getLatestMessage();
                newContent.setText(msg.getContent());
            } else if (objectName.equals(RongMsgType.GIF_TXT_MSG)) {
                newContent.setText("[表情]");
            } else if (objectName.equals(RongMsgType.CDD_LBS_MSG)) {
                newContent.setText("[位置]");
            } else if (objectName.equals(RongMsgType.RC_TXT_MSG)) {
                TextMessage msg = (TextMessage) notice.getLatestMessage();
                newContent.setText(msg.getContent());
            } else if (objectName.equals(RongMsgType.RC_VC_MSG)) {
                newContent.setText("[语音]");
            } else if (objectName.equals(RongMsgType.RC_IMG_MSG)) {
                newContent.setText("[图片]");
            } else if (objectName.equals(RongMsgType.RC_IMG_TXT_MSG)) {//图文暂时无用
            } else if (objectName.equals(RongMsgType.RC_CHE_MSG)) {//撤回消息
                newContent.setText("撤回消息");
            } else if (objectName.equals(RongMsgType.CDD_TASK_MSG)) {//订单提醒
                if (TextUtils.isEmpty(notice.getLatestMessage().toString())) {
                    newContent.setText("");
                    return;
                }
                CDDTaskMsg msg = (CDDTaskMsg) notice.getLatestMessage();
                TaskMsgBean taskMsgBean = TaskMsgBean.parse(msg.getContent());
                newContent.setText(ServiceUtils.getProjectName(taskMsgBean.getProjectNum()) + " " + taskMsgBean.getOrderNum());
            } else if (objectName.equals(RongMsgType.RONG_SERVER_NOTIFITION)) {
                InformationNotificationMessage messageContent = (InformationNotificationMessage) notice.getLatestMessage();
                newContent.setText(messageContent.getMessage());
            } else if (objectName.equals(RongMsgType.RONG_GROUP_NOTIFITION)) {//群通知消息
                GroupNotificationMessage groupNotificationMessage = (GroupNotificationMessage) notice.getLatestMessage();
                Log.e("data", "--" + groupNotificationMessage.getData());
                String operation = groupNotificationMessage.getOperation();
                try {
                    GroupNTFMsgDataBean groupNTFMsgDataBean = new Gson().fromJson(groupNotificationMessage.getData(), MsgDataBean.class).getData();
                    if (operation.equals(GroupNotificationMessage.GROUP_OPERATION_ADD)) {//拉人
                        StringBuffer buffer = new StringBuffer();
                        for (int i = 0; i < groupNTFMsgDataBean.getTargetUserDisplayNames().size(); i++) {
                            String name = groupNTFMsgDataBean.getTargetUserDisplayNames().get(i);
                            buffer.append(name);
                            if (i != groupNTFMsgDataBean.getTargetUserDisplayNames().size() - 1)
                                buffer.append(",");
                        }
                        newContent.setText(groupNTFMsgDataBean.getOperatorNickname() + "将" + buffer.toString() + "拉入群");
                    } else if (operation.equals(GroupNotificationMessage.GROUP_OPERATION_QUIT)) {//退群
                        newContent.setText(groupNTFMsgDataBean.getOperatorNickname() + "退出了群");
                    } else if (operation.equals(GroupNotificationMessage.GROUP_OPERATION_KICKED)) {//踢出群
                        StringBuffer buffer = new StringBuffer();
                        for (int i = 0; i < groupNTFMsgDataBean.getTargetUserDisplayNames().size(); i++) {
                            String name = groupNTFMsgDataBean.getTargetUserDisplayNames().get(i);
                            buffer.append(name);
                            if (i != groupNTFMsgDataBean.getTargetUserDisplayNames().size() - 1)
                                buffer.append(",");
                        }
                        newContent.setText(buffer.toString() + "被管理员踢出群");
                    } else if (operation.equals(GroupNotificationMessage.GROUP_OPERATION_RENAME)) {//重命名
                        newContent.setText(groupNTFMsgDataBean.getOperatorNickname() + "将群名称变更为\"" + groupNTFMsgDataBean.getTargetGroupName() + "\"");
                    } else if (operation.equals(GroupNotificationMessage.GROUP_OPERATION_BULLETIN)) {//公告变更
                        newContent.setText(groupNTFMsgDataBean.getOperatorNickname() + "变更了群公告");
                    } else if (operation.equals("Create")) {//创建群
                        newContent.setText(groupNTFMsgDataBean.getOperatorNickname() + "创建了群");
                    } else if (operation.equals("Dismiss")) {//解散群
                        newContent.setText(groupNTFMsgDataBean.getOperatorNickname() + "解散了群");
                    } else if (operation.equals("Transfer")) {//移交管理员
                        newContent.setText(groupNTFMsgDataBean.getOperatorNickname() + "将管理员权限移交给" + groupNTFMsgDataBean.getTargetUserDisplayName());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    newContent.setText(groupNotificationMessage.getOperatorUserId() + "变更了群信息");
                }
            } else if (objectName.equals(RongMsgType.CDD_HB_MSG)) {//红包消息
                newContent.setText("[红包]");
            } else if (objectName.equals(RongMsgType.CDD_ZZ_MSG)) {//转账消息
                newContent.setText("[转账]");
            } else {
                newContent.setText("");
            }
            final int ppCount = notice.getUnreadMessageCount();
            if (ppCount > 99) {
                tip.setText("99+");
                tip.setVisibility(View.VISIBLE);
                more.setText("标记已读");
            } else if (ppCount > 0) {
                tip.setText(ppCount + "");
                tip.setVisibility(View.VISIBLE);
                more.setText("标记已读");
            } else {
                tip.setVisibility(View.GONE);
                more.setText("标记未读");
            }
            more.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (ppCount > 0)
                        RongApi.clearMessagesUnreadStatus(notice.getConversationType(), notice.getTargetId(), new RongIMClient.ResultCallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean aBoolean) {
                                EventBus.getDefault().post(new ClearMessagesBean(notice.getConversationType(), notice.getTargetId()));
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                                Toast.makeText(mContext, "标记失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    else
                        RongApi.setMessageReceivedStatus(notice.getLatestMessageId(), new Message.ReceivedStatus(0), new RongIMClient.ResultCallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean aBoolean) {
                                EventBus.getDefault().post(new ClearMessagesBean());
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                                Toast.makeText(mContext, "标记失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    RongApi.removeConversation(notice.getConversationType(), notice.getTargetId(), new RongIMClient.ResultCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean aBoolean) {
                            EventBus.getDefault().post(new ClearMessagesBean(notice.getConversationType(), notice.getTargetId()));
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {
                            Toast.makeText(mContext, "删除失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            Conversation.ConversationType conversationType = notice.getConversationType();
            final String targetId = notice.getTargetId();
            switch (conversationType) {
                case GROUP:
                    GroupBean groupBean = groupBeanMap.get(targetId);
                    if (groupBean == null) {
                        itemIcon.setImageResource(R.drawable.default_group_portrait);
                        if (isGo.contains(targetId)) return;
                        isGo.add(targetId);
                        CDDRongApi.getGroupById(targetId, new CDDRongApi.NetWorkCallback<GroupBean>() {
                            @Override
                            public void onSuccess(String id, GroupBean groupBean) {
                                if (groupBean == null) return;
                                groupBeanMap.put(id, groupBean);
                                isGo.remove(targetId);
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onFailure(Throwable t, int errorNo, String strMsg) {
                                isGo.remove(targetId);
                            }
                        });
                    } else {
                        if (groupBean.getId() == -1) {
                            newTitle.setText(targetId + "(此群已被解散)");
                            itemIcon.setImageResource(R.drawable.default_group_portrait);
                        } else {
                            newTitle.setText(TextUtils.isEmpty(groupBean.getGroupName()) ? targetId + "(" + groupBean.getImGroupMemberCount() + ")" : groupBean.getGroupName() + "(" + groupBean.getImGroupMemberCount() + ")");
                            ImageLoaderBuilder.Builder().loadFromHttp(groupBean.getGroupImgUrl()).showImageForEmptyUri(R.drawable.default_group_portrait).showImageOnFail(R.drawable.default_group_portrait).showImageOnLoading(R.drawable.default_group_portrait).roundedImage(8).displayImage(itemIcon);
                        }
                    }
                    break;
                case PRIVATE:
                    RongUserBean user = rongUserBeanMap.get(targetId);
                    if (user == null) {
                        itemIcon.setImageResource(R.drawable.user_fang_icon);
                        if (isGo.contains(targetId)) return;
                        isGo.add(targetId);
                        CDDRongApi.getUserById(targetId, sendId, new CDDRongApi.NetWorkCallback<List<RongUserBean>>() {
                            @Override
                            public void onSuccess(String id, List<RongUserBean> o) {
                                if (o == null || o.size() <= 0) return;
                                RongUserBean rongUserBean = o.get(0);
                                rongUserBeanMap.put(id, rongUserBean);
                                isGo.remove(targetId);
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onFailure(Throwable t, int errorNo, String strMsg) {
                                isGo.remove(targetId);
                            }
                        });
                    } else {
                        if (user.getUserId().equals(Constant.USER_ID_TYPE.ORDER_MSG + "")) {//订单提醒
                            newTitle.setText(user.getNoteName());
                            itemIcon.setImageResource(R.drawable.order_remind_icon);
                        } else if (user.getUserId().equals(Constant.USER_ID_TYPE.SYSTEM_MSG + "")) {//系统提醒
                            newTitle.setText(user.getNoteName());
                            itemIcon.setImageResource(R.drawable.system_remind_icon);
                        } else {
                            List<RongUserBean.UrlsBean> urlsBeanns = user.getUrls();
                            if (urlsBeanns == null || urlsBeanns.size() <= 0) return;
                            newTitle.setText(TextUtils.isEmpty(user.getNoteName()) ? TextUtils.isEmpty(user.getNick()) ? targetId : user.getNick() : user.getNoteName());
                            ImageLoaderBuilder.Builder().loadFromHttp(ServerUtils.getImgServer(urlsBeanns.get(0).getUrl())).showImageForEmptyUri(R.drawable.user_fang_icon).showImageOnFail(R.drawable.user_fang_icon).showImageOnLoading(R.drawable.user_fang_icon).roundedImage(8).displayImage(itemIcon);
                        }
                    }
                    break;
                case CUSTOMER_SERVICE:
                    newTitle.setText("客服");
                    itemIcon.setImageResource(R.drawable.contacter_server);
                    break;
            }
        }
    }


}
