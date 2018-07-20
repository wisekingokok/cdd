package com.chewuwuyou.app.transition_adapter;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.bean.GroupSetUpMemberInformationEr;
import com.chewuwuyou.app.transition_view.activity.TransferAccountsDetailActivity;
import com.chewuwuyou.app.ui.NewRobOrderDetailsActivity;
import com.chewuwuyou.app.ui.OrderActivity;
import com.chewuwuyou.app.ui.PersonalHomeActivity2;
import com.chewuwuyou.app.ui.ToquoteAcitivity;
import com.chewuwuyou.app.utils.AlertDialog;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.ChatInputUtils;
import com.chewuwuyou.app.utils.ChatUtils;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.ImageLoaderBuilder;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.JsonUtil;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.OrderStateUtil;
import com.chewuwuyou.app.utils.ScreenUtils;
import com.chewuwuyou.app.utils.ServerUtils;
import com.chewuwuyou.app.utils.ServiceUtils;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.rong.bean.CDDGifMsg;
import com.chewuwuyou.rong.bean.CDDHBMsg;
import com.chewuwuyou.rong.bean.CDDLBSMsg;
import com.chewuwuyou.rong.bean.CDDTaskMsg;
import com.chewuwuyou.rong.bean.CDDYWZMsg;
import com.chewuwuyou.rong.bean.CDDZZMsg;
import com.chewuwuyou.rong.bean.ClearMessagesBean;
import com.chewuwuyou.rong.bean.GroupBean;
import com.chewuwuyou.rong.bean.GroupNTFMsgDataBean;
import com.chewuwuyou.rong.bean.InformationNotificationMessage;
import com.chewuwuyou.rong.bean.MsgDataBean;
import com.chewuwuyou.rong.bean.RongUserBean;
import com.chewuwuyou.rong.bean.TaskMsgBean;
import com.chewuwuyou.rong.listener.RongLocClickListener;
import com.chewuwuyou.rong.utils.CDDRongApi;
import com.chewuwuyou.rong.utils.RongApi;
import com.chewuwuyou.rong.utils.RongMsgType;
import com.chewuwuyou.rong.utils.RongVoicePlayer;
import com.chewuwuyou.rong.view.EstablishGroupFirstStepActivtiy;
import com.chewuwuyou.rong.view.RongImgViewPager;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.GroupNotificationMessage;
import io.rong.message.ImageMessage;
import io.rong.message.RecallNotificationMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * 天消息适配器
 *
 * @author xxy
 */
public class RongMsgAdapter extends BaseAdapter {
    private static final int MESSAGE_TYPE_UNKONW = -1;//位置类型消息
    private static final int MESSAGE_TYPE_RECV_TXT = 0;// 收到的文本
    private static final int MESSAGE_TYPE_SEND_TXT = 1;// 发出的文本
    private static final int MESSAGE_TYPE_RECV_IMG = 2;// 收到的图片
    private static final int MESSAGE_TYPE_SEND_IMG = 3;// 发出的图片
    private static final int MESSAGE_TYPE_RECV_VOICE = 4;// 收到的语音
    private static final int MESSAGE_TYPE_SEND_VOICE = 5;// 发出的语音
    private static final int MESSAGE_TYPE_RECV_LOC = 6;// 收到的位置信息
    private static final int MESSAGE_TYPE_SEND_LOC = 7;// 发出的位置信息
    private static final int MESSAGE_TYPE_SEND_GIF = 8;// 发出的GIF
    private static final int MESSAGE_TYPE_RECV_GIF = 9;// 收到的GIF
    private static final int MESSAGE_TYPE_CHE = 10;//撤回了一个消息
    private static final int MESSAGE_TYPE_RECV_YWZ = 11;//收到带颜文字的消息
    private static final int MESSAGE_TYPE_SEND_YWZ = 12;//发出带颜文字的消息
    private static final int MESSAGE_TYPE_ORDER = 13;//收到的订单提醒
    private static final int SERVER_NOTIFICATION = 14;//客服通知消息
    private static final int CONTACT_NOTIFICATION = 15;//好友相关消息
    private static final int GROUP_NTF_MSG = 16;//群通知消息
    private static final int MESSAGE_TYPE_RECV_HB = 17;//收到的红包
    private static final int MESSAGE_TYPE_SEND_HB = 18;//发送的红包
    private static final int MESSAGE_TYPE_RECV_ZZ = 19;//收到的转账
    private static final int MESSAGE_TYPE_SEND_ZZ = 20;//发送的转账
    private final int mItemSize = 22;// 不同Item的项数

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Message> mData;
    private AnimationDrawable voiceAnimation = null;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");//yyyy-MM-dd H:m:s
    private Map<String, String> mFaceCharacterMap;// 表情的Map
    private GroupBean groupBean;
    /**
     * 自己的ID
     * 这 暂时用来判断是否是自己发出的消息
     */
    private String sendId;
    private String myIcon;
    private ScrollTo scrollTo;
    private RongUserBean rongUserBean;
    private GetReSendCallback getReSendCallback;

    /**
     * 用于存储用户信息
     */
    private Map<String, RongUserBean> userBeanMap = new HashMap<>();

    /**
     * 正在请求中的用户Id
     */
    private List<String> postList = new ArrayList<>();
    private Conversation.ConversationType conversationType;
    private String targetId;

    /**
     * 要滚动到底部的回调
     */
    public interface ScrollTo {
        void scroll(int positon);
    }

    public interface GetReSendCallback {
        IRongCallback.ISendMessageCallback get();
    }

    public void setGroupBean(GroupBean groupBean) {
        this.groupBean = groupBean;
        notifyDataSetChanged();
    }

    public void setRongUserBean(RongUserBean rongUserBean) {
        this.rongUserBean = rongUserBean;
        notifyDataSetChanged();
    }


    public RongMsgAdapter(Context context, List<Message> data, String sendId, Conversation.ConversationType conversationType, String targetId) {
        this.mContext = context;
        this.conversationType = conversationType;
        this.targetId = targetId;
        mInflater = LayoutInflater.from(mContext);
        this.mData = data == null ? new ArrayList<Message>() : data;
        this.mFaceCharacterMap = JsonUtil.getFaceStrMap(context);
        this.sendId = sendId;
        this.myIcon = CacheTools.getUserData("rongPortraitUri");
        //加入发送订单提醒用户信及系统提醒用户信息
        RongUserBean rongUserBean = new RongUserBean();
        rongUserBean.setNoteName("订单提醒");
        rongUserBean.setUserId(com.chewuwuyou.rong.utils.Constant.USER_ID_TYPE.ORDER_MSG);
        userBeanMap.put(com.chewuwuyou.rong.utils.Constant.USER_ID_TYPE.ORDER_MSG, rongUserBean);
        RongUserBean rongUserBean1 = new RongUserBean();
        rongUserBean1.setNoteName("车当当");
        rongUserBean1.setUserId(com.chewuwuyou.rong.utils.Constant.USER_ID_TYPE.SYSTEM_MSG);
        userBeanMap.put(com.chewuwuyou.rong.utils.Constant.USER_ID_TYPE.SYSTEM_MSG, rongUserBean1);
        RongUserBean rongUserBean2 = new RongUserBean();
        rongUserBean2.setNoteName("客服");
        rongUserBean2.setUserId(com.chewuwuyou.rong.utils.Constant.SERVER_ID);
        userBeanMap.put(com.chewuwuyou.rong.utils.Constant.SERVER_ID, rongUserBean2);
    }

    /**
     * 根据UserID去的一个用户,本地不存在 会请求
     *
     * @param id
     */
    public RongUserBean getUser(String id) {
        RongUserBean userBean = userBeanMap.get(id);
        if (userBean == null) {
            if (conversationType == Conversation.ConversationType.PRIVATE)
                postUser(id);
            else if (conversationType == Conversation.ConversationType.GROUP)
                postGroupUser(targetId);
        }
        return userBean;
    }

    public void setMessage(boolean isScroll, List<Message> data) {
        this.mData = data == null ? new ArrayList<Message>() : data;
        notifyDataSetChanged();
        if (isScroll && scrollTo != null) scrollTo.scroll(mData.size());
    }

    public void setMessage(List<Message> data) {
        this.mData = data == null ? new ArrayList<Message>() : data;
        notifyDataSetChanged();
    }

    public void setScrollTo(ScrollTo scrollTo) {
        this.scrollTo = scrollTo;
    }

    public void setGetReSendCallback(GetReSendCallback getReSendCallback) {
        this.getReSendCallback = getReSendCallback;
    }

    public void addMessage(Message msg) {
        if (mData == null)
            mData = new ArrayList<>();
        mData.add(msg);
        notifyDataSetChanged();
        //if (scrollTo != null) scrollTo.scroll(mData.size());
    }

    /**
     * 替换某个位 的元
     *
     * @param message
     */
    public void setMessage(Message message) {
        for (int i = 0; i < mData.size(); i++) {
            if (message.getMessageId() == mData.get(i).getMessageId()) {
                mData.set(i, message);
                return;
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = mData.get(position);
        String objectName = message.getObjectName();
        String senderId = message.getSenderUserId();
        if (objectName.equals(RongMsgType.CDD_TXT_MSG)) {//颜文字
            if (senderId.equals(sendId))
                return MESSAGE_TYPE_SEND_YWZ;
            else
                return MESSAGE_TYPE_RECV_YWZ;
        } else if (objectName.equals(RongMsgType.GIF_TXT_MSG)) {
            if (senderId.equals(sendId))
                return MESSAGE_TYPE_SEND_GIF;
            else
                return MESSAGE_TYPE_RECV_GIF;
        } else if (objectName.equals(RongMsgType.CDD_LBS_MSG)) {
            if (senderId.equals(sendId))
                return MESSAGE_TYPE_SEND_LOC;
            else
                return MESSAGE_TYPE_RECV_LOC;
        } else if (objectName.equals(RongMsgType.RC_TXT_MSG)) {
            if (senderId.equals(sendId))
                return MESSAGE_TYPE_SEND_TXT;
            else
                return MESSAGE_TYPE_RECV_TXT;
        } else if (objectName.equals(RongMsgType.RC_VC_MSG)) {
            if (senderId.equals(sendId))
                return MESSAGE_TYPE_SEND_VOICE;
            else
                return MESSAGE_TYPE_RECV_VOICE;
        } else if (objectName.equals(RongMsgType.RC_IMG_MSG)) {
            if (senderId.equals(sendId))
                return MESSAGE_TYPE_SEND_IMG;
            else
                return MESSAGE_TYPE_RECV_IMG;
        } else if (objectName.equals(RongMsgType.RC_IMG_TXT_MSG)) {//图文暂时无用
//            if(senderId.equals(myId))
//                return  ;
//            else
//                return  ;
        } else if (objectName.equals(RongMsgType.RC_CHE_MSG)) {//撤回消息
            return MESSAGE_TYPE_CHE;
        } else if (objectName.equals(RongMsgType.CDD_TASK_MSG)) {//订单提醒
            return MESSAGE_TYPE_ORDER;
        } else if (objectName.equals(RongMsgType.RONG_SERVER_NOTIFITION)) {//通知消息
            return SERVER_NOTIFICATION;
        } else if (objectName.equals(RongMsgType.RONG_CONTACT_NOTIFITION)) {//加好友相关消息
            return CONTACT_NOTIFICATION;
        } else if (objectName.equals(RongMsgType.RONG_GROUP_NOTIFITION)) {
            return GROUP_NTF_MSG;
        } else if (objectName.equals(RongMsgType.CDD_HB_MSG)) {//红包消息
            if (senderId.equals(sendId))
                return MESSAGE_TYPE_SEND_HB;
            else
                return MESSAGE_TYPE_RECV_HB;
        } else if (objectName.equals(RongMsgType.CDD_ZZ_MSG)) {//转账消息
            if (senderId.equals(sendId))
                return MESSAGE_TYPE_SEND_ZZ;
            else
                return MESSAGE_TYPE_RECV_ZZ;
        }
        return MESSAGE_TYPE_UNKONW;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message message = mData.get(position);
        Holder holder = null;
        if (convertView == null) {
            switch (getItemViewType(position)) {
                case MESSAGE_TYPE_RECV_YWZ:// 收到的颜文字
                    holder = new ItemLeftYWZ(convertView = mInflater.inflate(R.layout.row_received_message, null));
                    break;
                case MESSAGE_TYPE_SEND_YWZ:// 发出的颜文字
                    holder = new ItemRightYWZ(convertView = mInflater.inflate(R.layout.row_sent_message, null));
                    break;
                case MESSAGE_TYPE_RECV_TXT:// 收到的文本
                    holder = new ItemLeftTXT(convertView = mInflater.inflate(R.layout.row_received_message, null));
                    break;
                case MESSAGE_TYPE_SEND_TXT:// 发出的文本
                    holder = new ItemRightTXT(convertView = mInflater.inflate(R.layout.row_sent_message, null));
                    break;
                case MESSAGE_TYPE_RECV_GIF:// 收到的GIF
                    holder = new ItemLeftGif(convertView = mInflater.inflate(R.layout.row_received_gif, null));
                    break;
                case MESSAGE_TYPE_SEND_GIF:// 发出的GIF
                    holder = new ItemRightGif(convertView = mInflater.inflate(R.layout.row_sent_gif, null));
                    break;
                case MESSAGE_TYPE_RECV_IMG:// 收到的图片
                    holder = new ItemLeftImage(convertView = mInflater.inflate(R.layout.row_received_picture, null));
                    break;
                case MESSAGE_TYPE_SEND_IMG:// 发出的图片
                    holder = new ItemRightImage(convertView = mInflater.inflate(R.layout.row_sent_picture, null));
                    break;
                case MESSAGE_TYPE_RECV_VOICE:// 收到的语音
                    holder = new ItemLeftVoice(convertView = mInflater.inflate(R.layout.row_received_voice, null));
                    break;
                case MESSAGE_TYPE_SEND_VOICE:// 发出的语音
                    holder = new ItemRightVoice(convertView = mInflater.inflate(R.layout.row_sent_voice, null));
                    break;
                case MESSAGE_TYPE_RECV_LOC:// 收到的位置信息
                    holder = new ItemLeftLocal(convertView = mInflater.inflate(R.layout.row_received_location, null));
                    break;
                case MESSAGE_TYPE_SEND_LOC:// 发出的位置信息
                    holder = new ItemRightLocal(convertView = mInflater.inflate(R.layout.row_sent_location, null));
                    break;
                case MESSAGE_TYPE_CHE:
                    holder = new ItemChe(convertView = mInflater.inflate(R.layout.row_che, null));
                    break;
                case MESSAGE_TYPE_UNKONW:
                    holder = new ItemUnkonw(convertView = mInflater.inflate(R.layout.row_che, null));
                    break;
                case MESSAGE_TYPE_ORDER:
                    holder = new ItemOrder(convertView = mInflater.inflate(R.layout.item_order_remind, null));
                    break;
                case SERVER_NOTIFICATION:
                    holder = new ItemServerNotification(convertView = mInflater.inflate(R.layout.row_che, null));
                    break;
                case CONTACT_NOTIFICATION:
                    holder = new ItemLeftTXT(convertView = mInflater.inflate(R.layout.row_received_message, null));
                    break;
                case GROUP_NTF_MSG:
                    holder = new GroupNtfMsg(convertView = mInflater.inflate(R.layout.row_che, null));
                    break;
                case MESSAGE_TYPE_RECV_ZZ:
                    holder = new ItemLeftZZ(convertView = mInflater.inflate(R.layout.row_received_zz, null));
                    break;
                case MESSAGE_TYPE_SEND_ZZ:
                    holder = new ItemRightZZ(convertView = mInflater.inflate(R.layout.row_sent_zz, null));
                    break;
                case MESSAGE_TYPE_RECV_HB:
                    holder = new ItemLeftHB(convertView = mInflater.inflate(R.layout.row_received_hb, null));
                    break;
                case MESSAGE_TYPE_SEND_HB:
                    holder = new ItemRightHB(convertView = mInflater.inflate(R.layout.row_sent_hb, null));
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.bandData(message, position);
        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return mItemSize;
    }

    /**
     * 左边显示的文本
     */
    class ItemLeftTXT extends Holder {
        TextView msgTimeTV;// 信息发送及接收时间
        ImageView leftUserHeadIV;
        TextView contentTV;
        TextView name;

        public ItemLeftTXT(View view) {
            contentTV = (TextView) view.findViewById(R.id.tv_chatcontent);
            leftUserHeadIV = (ImageView) view.findViewById(R.id.iv_userhead);
            msgTimeTV = (TextView) view.findViewById(R.id.timestamp_tv);
            name = (TextView) view.findViewById(R.id.name);
        }

        @Override
        public void bandData(Message msg, int position) {
            showTime(position, msgTimeTV);
            TextMessage messageContent = (TextMessage) msg.getContent();
            contentTV.setText(messageContent.getContent());
            setLongClickListener(msg, contentTV, false);

            RongUserBean rongUserBean = getUser(msg.getSenderUserId());
            if (rongUserBean != null) {
                List<RongUserBean.UrlsBean> urlsBeanns = rongUserBean.getUrls();
                if (urlsBeanns == null || urlsBeanns.size() <= 0) return;
                ImageLoaderBuilder.Builder().loadFromHttp(ServerUtils.getImgServer(urlsBeanns.get(0).getUrl()))
                        .showImageForEmptyUri(R.drawable.user_fang_icon)
                        .showImageOnFail(R.drawable.user_fang_icon)
                        .showImageOnLoading(R.drawable.user_fang_icon)
                        .roundedImage(8).displayImage(leftUserHeadIV);
                name.setText(TextUtils.isEmpty(rongUserBean.getNoteName()) ? TextUtils.isEmpty(rongUserBean.getNick()) ? rongUserBean.getUserId() : rongUserBean.getNick() : rongUserBean.getNoteName());
            } else {
                name.setText(msg.getSenderUserId());
            }
            if (msg.getConversationType() == Conversation.ConversationType.GROUP) {
                name.setVisibility(View.VISIBLE);
            } else {
                name.setVisibility(View.GONE);
            }
            if (msg.getConversationType().equals(Conversation.ConversationType.CUSTOMER_SERVICE))
                leftUserHeadIV.setImageResource(R.drawable.contacter_server);
            if (msg.getTargetId().equals(com.chewuwuyou.rong.utils.Constant.USER_ID_TYPE.SYSTEM_MSG))
                leftUserHeadIV.setImageResource(R.drawable.system_remind_icon);
            setIconClickListener(msg.getSenderUserId(), leftUserHeadIV);
        }
    }

    /**
     * 右边显示的文本
     */
    class ItemRightTXT extends Holder {
        TextView msgTimeTV;
        ImageView rightUserHeadIV;
        TextView contentTV;
        ImageView msg_status;
        ProgressBar progressBar;

        public ItemRightTXT(View view) {
            contentTV = (TextView) view.findViewById(R.id.tv_chatcontent);
            rightUserHeadIV = (ImageView) view.findViewById(R.id.iv_userhead);
            msgTimeTV = (TextView) view.findViewById(R.id.timestamp_tv);
            msg_status = (ImageView) view.findViewById(R.id.msg_status);
            progressBar = (ProgressBar) view.findViewById(R.id.pb_sending);
        }

        @Override
        public void bandData(final Message msg, int position) {
            ImageLoaderBuilder.Builder().loadFromHttp(myIcon).showImageForEmptyUri(R.drawable.user_fang_icon).showImageOnLoading(R.drawable.user_fang_icon).showImageOnFail(R.drawable.user_fang_icon).roundedImage(8).displayImage(rightUserHeadIV);
            showTime(position, msgTimeTV);
            TextMessage messageContent = (TextMessage) msg.getContent();
            final Message.SentStatus sentStatus = msg.getSentStatus();
            switch (sentStatus) {
                case FAILED:
                    msg_status.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    showError(msg, msgTimeTV);
                    break;
                case SENDING:
                    msg_status.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case SENT:
                    msg_status.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    break;
            }
            msg_status.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (sentStatus == Message.SentStatus.FAILED) {
                        reSendMsg(msg);
                    }
                }
            });
            contentTV.setText(messageContent.getContent());
            setLongClickListener(msg, contentTV, true);
            setIconClickListener(sendId, rightUserHeadIV);
        }
    }

    /**
     * 左边显示的 文字
     */
    class ItemLeftYWZ extends Holder {
        TextView msgTimeTV;// 信息发送及接收时间
        ImageView leftUserHeadIV;
        TextView contentTV;
        TextView name;

        public ItemLeftYWZ(View view) {
            contentTV = (TextView) view.findViewById(R.id.tv_chatcontent);
            leftUserHeadIV = (ImageView) view.findViewById(R.id.iv_userhead);
            msgTimeTV = (TextView) view.findViewById(R.id.timestamp_tv);
            name = (TextView) view.findViewById(R.id.name);
        }

        @Override
        public void bandData(Message msg, int position) {
            showTime(position, msgTimeTV);
            CDDYWZMsg messageContent = (CDDYWZMsg) msg.getContent();
            contentTV.setText(ChatInputUtils.displayBigFacePic(mContext, messageContent.getContent(), mFaceCharacterMap));
            setLongClickListener(msg, contentTV, false);
            RongUserBean rongUserBean = getUser(msg.getSenderUserId());
            if (rongUserBean != null) {
                List<RongUserBean.UrlsBean> urlsBeanns = rongUserBean.getUrls();
                if (urlsBeanns == null || urlsBeanns.size() <= 0) return;
                ImageLoaderBuilder.Builder().loadFromHttp(ServerUtils.getImgServer(urlsBeanns.get(0).getUrl()))
                        .showImageForEmptyUri(R.drawable.user_fang_icon)
                        .showImageOnFail(R.drawable.user_fang_icon)
                        .showImageOnLoading(R.drawable.user_fang_icon)
                        .roundedImage(8).displayImage(leftUserHeadIV);
                name.setText(TextUtils.isEmpty(rongUserBean.getNoteName()) ? TextUtils.isEmpty(rongUserBean.getNick()) ? rongUserBean.getUserId() : rongUserBean.getNick() : rongUserBean.getNoteName());
            } else {
                name.setText(msg.getSenderUserId());
            }
            if (msg.getConversationType().equals(Conversation.ConversationType.CUSTOMER_SERVICE))
                leftUserHeadIV.setImageResource(R.drawable.contacter_server);
            if (msg.getTargetId().equals(com.chewuwuyou.rong.utils.Constant.USER_ID_TYPE.SYSTEM_MSG))
                leftUserHeadIV.setImageResource(R.drawable.system_remind_icon);
            setIconClickListener(msg.getSenderUserId(), leftUserHeadIV);
            if (msg.getConversationType() == Conversation.ConversationType.GROUP) {
                name.setVisibility(View.VISIBLE);
            } else {
                name.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 右边显示的 文字
     */
    class ItemRightYWZ extends Holder {
        TextView msgTimeTV;
        ImageView rightUserHeadIV;
        TextView contentTV;
        ImageView msg_status;
        ProgressBar progressBar;

        public ItemRightYWZ(View view) {
            contentTV = (TextView) view.findViewById(R.id.tv_chatcontent);
            rightUserHeadIV = (ImageView) view.findViewById(R.id.iv_userhead);
            msgTimeTV = (TextView) view.findViewById(R.id.timestamp_tv);
            msg_status = (ImageView) view.findViewById(R.id.msg_status);
            progressBar = (ProgressBar) view.findViewById(R.id.pb_sending);
        }

        @Override
        public void bandData(final Message msg, int position) {
            showTime(position, msgTimeTV);
            CDDYWZMsg messageContent = (CDDYWZMsg) msg.getContent();
            final Message.SentStatus sentStatus = msg.getSentStatus();
            switch (sentStatus) {
                case FAILED:
                    msg_status.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    showError(msg, msgTimeTV);
                    break;
                case SENDING:
                    msg_status.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case SENT:
                    msg_status.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    break;
            }
            msg_status.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (sentStatus == Message.SentStatus.FAILED) {
                        reSendMsg(msg);
                    }
                }
            });
            setIconClickListener(sendId, rightUserHeadIV);
            ImageLoaderBuilder.Builder().loadFromHttp(myIcon).showImageForEmptyUri(R.drawable.user_fang_icon).showImageOnLoading(R.drawable.user_fang_icon).showImageOnFail(R.drawable.user_fang_icon).roundedImage(8).displayImage(rightUserHeadIV);
            contentTV.setText(ChatInputUtils.displayBigFacePic(mContext, messageContent.getContent(), mFaceCharacterMap));
            setLongClickListener(msg, contentTV, true);
        }
    }

    /**
     * 右边显示的GIF
     */
    class ItemRightGif extends Holder {
        TextView msgTimeTV;
        GifImageView rightGifTextView;
        ImageView rightUserHeadIV;
        ImageView msg_status;
        ProgressBar progressBar;

        public ItemRightGif(View view) {
            rightGifTextView = (GifImageView) view.findViewById(R.id.gifTextView);
            rightUserHeadIV = (ImageView) view.findViewById(R.id.iv_userhead);
            msgTimeTV = (TextView) view.findViewById(R.id.timestamp_tv);
            msg_status = (ImageView) view.findViewById(R.id.msg_status);
            progressBar = (ProgressBar) view.findViewById(R.id.pb_sending);
        }

        @Override
        public void bandData(final Message msg, int position) {
            showTime(position, msgTimeTV);
            final Message.SentStatus sentStatus = msg.getSentStatus();
            switch (sentStatus) {
                case FAILED:
                    msg_status.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    showError(msg, msgTimeTV);
                    break;
                case SENDING:
                    msg_status.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case SENT:
                    msg_status.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    break;
            }
            msg_status.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (sentStatus == Message.SentStatus.FAILED) {
                        reSendMsg(msg);
                    }
                }
            });
            String str = ((CDDGifMsg) msg.getContent()).getContent();
            try {
                rightGifTextView.setImageDrawable(new GifDrawable(mContext.getAssets(), "emoticons/" + str.substring(4, str.length() - 4)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            setIconClickListener(sendId, rightUserHeadIV);
            setLongClickListener(msg, rightGifTextView, true);
            ImageLoaderBuilder.Builder().loadFromHttp(myIcon).showImageForEmptyUri(R.drawable.user_fang_icon).showImageOnLoading(R.drawable.user_fang_icon).showImageOnFail(R.drawable.user_fang_icon).roundedImage(8).displayImage(rightUserHeadIV);
        }
    }

    /**
     * 一发送消息
     *
     * @return
     */
    private void reSendMsg(Message message) {
        if (conversationType.equals(Conversation.ConversationType.GROUP) && groupBean == null) {//判断群信息为空时不发送群信息
            ToastUtil.toastShow(mContext, "网络异常，请求群信息失败");
            return;
        }
//        if (!isGroup) {
//            ToastUtil.toastShow(mContext, "您已不是群成员,不能发送消息!");
//            return;
//        }
        if (conversationType.equals(Conversation.ConversationType.GROUP) && groupBean.getId() == -1) {
            ToastUtil.toastShow(mContext, "群已被解散,不能发送消息!");
            return;
        }
        RongApi.sendMessage(message, null, simpleDateFormat.format(new Date()), getReSendCallback == null ? null : getReSendCallback.get());
    }

    /**
     * 左边的gif
     *
     * @author yuyong
     */
    class ItemLeftGif extends Holder {
        TextView msgTimeTV;
        GifImageView leftGifTextView;
        ImageView leftUserHeadIV;
        TextView name;

        public ItemLeftGif(View view) {
            leftGifTextView = (GifImageView) view.findViewById(R.id.gifTextView);
            leftUserHeadIV = (ImageView) view.findViewById(R.id.iv_userhead);
            msgTimeTV = (TextView) view.findViewById(R.id.timestamp_tv);
            name = (TextView) view.findViewById(R.id.name);
        }

        @Override
        public void bandData(Message msg, int position) {
            showTime(position, msgTimeTV);
            String str = ((CDDGifMsg) msg.getContent()).getContent();
            try {
                leftGifTextView.setImageDrawable(new GifDrawable(mContext.getAssets(), "emoticons/" + str.substring(4, str.length() - 4)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            setLongClickListener(msg, leftGifTextView, false);
            RongUserBean rongUserBean = getUser(msg.getSenderUserId());
            if (rongUserBean != null) {
                List<RongUserBean.UrlsBean> urlsBeanns = rongUserBean.getUrls();
                if (urlsBeanns == null || urlsBeanns.size() <= 0) return;
                ImageLoaderBuilder.Builder().loadFromHttp(ServerUtils.getImgServer(urlsBeanns.get(0).getUrl()))
                        .showImageForEmptyUri(R.drawable.user_fang_icon)
                        .showImageOnFail(R.drawable.user_fang_icon)
                        .showImageOnLoading(R.drawable.user_fang_icon)
                        .roundedImage(8).displayImage(leftUserHeadIV);
                name.setText(TextUtils.isEmpty(rongUserBean.getNoteName()) ? TextUtils.isEmpty(rongUserBean.getNick()) ? rongUserBean.getUserId() : rongUserBean.getNick() : rongUserBean.getNoteName());
            } else {
                name.setText(msg.getSenderUserId());
            }
            setIconClickListener(msg.getSenderUserId(), leftUserHeadIV);
            if (msg.getConversationType() == Conversation.ConversationType.GROUP) {
                name.setVisibility(View.VISIBLE);
            } else {
                name.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 左边显示的图片 TODO图片有加载中显示及加载失败显示
     */
    class ItemLeftImage extends Holder {
        TextView msgTimeTV;
        TextView progressTV;// 图片加载进度显示
        ProgressBar progressBar;// 图片加载进度条
        ImageView leftContentIV, leftUserHeadIV;
        TextView name;

        public ItemLeftImage(View view) {
            leftContentIV = (ImageView) view.findViewById(R.id.iv_sendPicture);
            leftUserHeadIV = (ImageView) view.findViewById(R.id.iv_userhead);
            msgTimeTV = (TextView) view.findViewById(R.id.timestamp_tv);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            progressTV = (TextView) view.findViewById(R.id.percentage_tv);
            name = (TextView) view.findViewById(R.id.name);
        }

        @Override
        public void bandData(final Message msg, int position) {
            showTime(position, msgTimeTV);
            final ImageMessage imageMessage = (ImageMessage) msg.getContent();
            if (imageMessage.getThumUri() == null) {
                leftContentIV.setImageResource(R.drawable.chat_img_def);
            } else {
                ImageLoaderBuilder.Builder().loadFromSDCard(imageMessage.getThumUri().getPath())
                        .showImageForEmptyUri(R.drawable.chat_img_def)
                        .showImageOnLoading(R.drawable.chat_img_def)
                        .showImageOnFail(R.drawable.chat_img_def).displayImage(leftContentIV);
            }
            setLongClickListener(msg, leftContentIV, false);
            leftContentIV.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    ArrayList<String> list = new ArrayList<String>();
                    list.add(imageMessage.getRemoteUri().toString());
                    intent.putExtra("viewPagerPosition", "0");
                    intent.putStringArrayListExtra("url", list);
                    intent.setClass(mContext, RongImgViewPager.class);
                    mContext.startActivity(intent);
                }
            });
            RongUserBean rongUserBean = getUser(msg.getSenderUserId());
            if (rongUserBean != null) {
                List<RongUserBean.UrlsBean> urlsBeanns = rongUserBean.getUrls();
                if (urlsBeanns == null || urlsBeanns.size() <= 0) return;
                ImageLoaderBuilder.Builder().loadFromHttp(ServerUtils.getImgServer(urlsBeanns.get(0).getUrl()))
                        .showImageForEmptyUri(R.drawable.user_fang_icon)
                        .showImageOnFail(R.drawable.user_fang_icon)
                        .showImageOnLoading(R.drawable.user_fang_icon)
                        .roundedImage(8).displayImage(leftUserHeadIV);
                name.setText(TextUtils.isEmpty(rongUserBean.getNoteName()) ? TextUtils.isEmpty(rongUserBean.getNick()) ? rongUserBean.getUserId() : rongUserBean.getNick() : rongUserBean.getNoteName());
            } else {
                name.setText(msg.getSenderUserId());
            }
            if (msg.getConversationType().equals(Conversation.ConversationType.CUSTOMER_SERVICE))
                leftUserHeadIV.setImageResource(R.drawable.contacter_server);
            if (msg.getTargetId().equals(com.chewuwuyou.rong.utils.Constant.USER_ID_TYPE.SYSTEM_MSG))
                leftUserHeadIV.setImageResource(R.drawable.system_remind_icon);
            setIconClickListener(msg.getSenderUserId(), leftUserHeadIV);
            if (msg.getConversationType() == Conversation.ConversationType.GROUP) {
                name.setVisibility(View.VISIBLE);
            } else {
                name.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 右边显示的图片
     */
    class ItemRightImage extends Holder {
        TextView msgTimeTV;
        ImageView rightContentIV, rightUserHeadIV;
        ImageView msg_status;
        ProgressBar progressBar;

        public ItemRightImage(View view) {
            rightContentIV = (ImageView) view.findViewById(R.id.iv_sendPicture);
            rightUserHeadIV = (ImageView) view.findViewById(R.id.iv_userhead);
            msgTimeTV = (TextView) view.findViewById(R.id.timestamp_tv);
            msg_status = (ImageView) view.findViewById(R.id.msg_status);
            progressBar = (ProgressBar) view.findViewById(R.id.pb_sending);
        }

        @Override
        public void bandData(final Message msg, int position) {
            showTime(position, msgTimeTV);
            final ImageMessage imageMessage = (ImageMessage) msg.getContent();
            final Message.SentStatus sentStatus = msg.getSentStatus();
            switch (sentStatus) {
                case FAILED:
                    msg_status.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    showError(msg, msgTimeTV);
                    break;
                case SENDING:
                    msg_status.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case SENT:
                    msg_status.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    break;
            }
            msg_status.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (sentStatus == Message.SentStatus.FAILED) {
                        reSendMsg(msg);
                    }
                }
            });
            setLongClickListener(msg, rightContentIV, true);
            ImageLoaderBuilder.Builder().loadFromSDCard(imageMessage.getThumUri().getPath())
                    .showImageForEmptyUri(R.drawable.chat_img_def)
                    .showImageOnLoading(R.drawable.chat_img_def)
                    .showImageOnFail(R.drawable.chat_img_def).displayImage(rightContentIV);
            rightContentIV.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    ArrayList<String> list = new ArrayList<String>();
                    try {
                        list.add(imageMessage != null ? imageMessage.getRemoteUri().toString() : "");
                    } catch (Exception e) {
                        Log.e("YUY", "发送图片异常");
                    }
                    intent.putExtra("viewPagerPosition", "0");
                    intent.putStringArrayListExtra("url", list);
                    intent.setClass(mContext, RongImgViewPager.class);
                    if (imageMessage != null || list != null) {
                        mContext.startActivity(intent);
                    }

                }
            });
            setIconClickListener(sendId, rightUserHeadIV);
            ImageLoaderBuilder.Builder().loadFromHttp(myIcon).showImageForEmptyUri(R.drawable.user_fang_icon).showImageOnLoading(R.drawable.user_fang_icon).showImageOnFail(R.drawable.user_fang_icon).roundedImage(8).displayImage(rightUserHeadIV);
        }

    }

    /**
     * 左边语 显示
     */
    class ItemLeftVoice extends Holder {// 异步下载
        TextView msgTimeTV;
        ImageView leftUserHeadIV, leftVoiceIV;
        TextView leftTimeTV;
        ProgressBar mLeftBar;
        ImageView isReadIV;// 是否已读
        TextView name;

        public ItemLeftVoice(View view) {
            isReadIV = (ImageView) view.findViewById(R.id.iv_unread_voice);
            leftTimeTV = (TextView) view.findViewById(R.id.tv_length);
            leftUserHeadIV = (ImageView) view.findViewById(R.id.iv_userhead);
            msgTimeTV = (TextView) view.findViewById(R.id.timestamp_tv);
            mLeftBar = (ProgressBar) view.findViewById(R.id.pb_sending);
            leftVoiceIV = (ImageView) view.findViewById(R.id.iv_voice);
            name = (TextView) view.findViewById(R.id.name);
        }

        @Override
        public void bandData(final Message msg, int position) {
            showTime(position, msgTimeTV);
            final VoiceMessage voiceMessage = (VoiceMessage) msg.getContent();
            final Message.ReceivedStatus receivedStatus = msg.getReceivedStatus();
            if (receivedStatus.isListened())
                isReadIV.setVisibility(View.GONE);
            else
                isReadIV.setVisibility(View.VISIBLE);
            leftVoiceIV.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (RongVoicePlayer.getInstance().playVoice(mContext, voiceMessage.getUri().getPath(), new RongVoicePlayer.VoiceCallBack() {
                        @Override
                        public void onCompletion() {
                            if (voiceAnimation != null)
                                voiceAnimation.stop();
                            leftVoiceIV.setImageResource(R.drawable.chatfrom_voice_playing);
                            notifyDataSetChanged();
                        }
                    })) {
                        receivedStatus.setListened();
                        notifyDataSetChanged();
                        RongApi.setMessageReceivedStatus(msg.getMessageId(), receivedStatus, new RongIMClient.ResultCallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean aBoolean) {

                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {

                            }
                        });
                        leftVoiceIV.setImageResource(R.drawable.voice_from_icon);
                        voiceAnimation = (AnimationDrawable) leftVoiceIV.getDrawable();
                        voiceAnimation.start();
                    }
                }
            });
            leftTimeTV.setText(voiceMessage.getDuration() + "\"");
            setLongClickListener(msg, leftVoiceIV, false);
            RongUserBean rongUserBean = getUser(msg.getSenderUserId());
            if (rongUserBean != null) {
                List<RongUserBean.UrlsBean> urlsBeanns = rongUserBean.getUrls();
                if (urlsBeanns == null || urlsBeanns.size() <= 0) return;
                ImageLoaderBuilder.Builder().loadFromHttp(ServerUtils.getImgServer(urlsBeanns.get(0).getUrl()))
                        .showImageForEmptyUri(R.drawable.user_fang_icon)
                        .showImageOnFail(R.drawable.user_fang_icon)
                        .showImageOnLoading(R.drawable.user_fang_icon)
                        .roundedImage(8).displayImage(leftUserHeadIV);
                name.setText(TextUtils.isEmpty(rongUserBean.getNoteName()) ? TextUtils.isEmpty(rongUserBean.getNick()) ? rongUserBean.getUserId() : rongUserBean.getNick() : rongUserBean.getNoteName());
            } else {
                name.setText(msg.getSenderUserId());
            }
            if (msg.getConversationType().equals(Conversation.ConversationType.CUSTOMER_SERVICE))
                leftUserHeadIV.setImageResource(R.drawable.contacter_server);
            setIconClickListener(msg.getSenderUserId(), leftUserHeadIV);
            if (msg.getConversationType() == Conversation.ConversationType.GROUP) {
                name.setVisibility(View.VISIBLE);
            } else {
                name.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 右边语 显示
     */
    class ItemRightVoice extends Holder {// 阅读本地
        TextView msgTimeTV;
        ImageView rightUserHeadIV, rightVoiceIV;
        TextView rightTimeTV;
        ImageView msg_status;
        ProgressBar progressBar;

        public ItemRightVoice(View view) {
            rightTimeTV = (TextView) view.findViewById(R.id.tv_length);
            rightUserHeadIV = (ImageView) view.findViewById(R.id.iv_userhead);
            msgTimeTV = (TextView) view.findViewById(R.id.timestamp_tv);
            rightVoiceIV = (ImageView) view.findViewById(R.id.iv_voice);
            msg_status = (ImageView) view.findViewById(R.id.msg_status);
            progressBar = (ProgressBar) view.findViewById(R.id.pb_sending);
        }

        @Override
        public void bandData(final Message msg, int position) {
            showTime(position, msgTimeTV);
            final VoiceMessage voiceMessage = (VoiceMessage) msg.getContent();
            final Message.SentStatus sentStatus = msg.getSentStatus();
            switch (sentStatus) {
                case FAILED:
                    msg_status.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    showError(msg, msgTimeTV);
                    break;
                case SENDING:
                    msg_status.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case SENT:
                    msg_status.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    break;
            }
            msg_status.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (sentStatus == Message.SentStatus.FAILED) {
                        reSendMsg(msg);
                    }
                }
            });
            rightVoiceIV.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (RongVoicePlayer.getInstance().playVoice(mContext, voiceMessage.getUri().getPath(), new RongVoicePlayer.VoiceCallBack() {
                        @Override
                        public void onCompletion() {
                            if (voiceAnimation != null)
                                voiceAnimation.stop();
                            rightVoiceIV.setImageResource(R.drawable.chatto_voice_playing);
                            notifyDataSetChanged();
                        }
                    })) {
                        rightVoiceIV.setImageResource(R.drawable.voice_to_icon);
                        voiceAnimation = (AnimationDrawable) rightVoiceIV.getDrawable();
                        voiceAnimation.start();
                    }
                }
            });
            rightTimeTV.setText(voiceMessage.getDuration() + "\"");
            setLongClickListener(msg, rightVoiceIV, true);
            ImageLoaderBuilder.Builder().loadFromHttp(myIcon).showImageForEmptyUri(R.drawable.user_fang_icon).showImageOnLoading(R.drawable.user_fang_icon).showImageOnFail(R.drawable.user_fang_icon).roundedImage(8).displayImage(rightUserHeadIV);
            setIconClickListener(sendId, rightUserHeadIV);
        }
    }

    /**
     * 左边显示的红包信息
     */
    class ItemLeftHB extends Holder {
        TextView msgTimeTV;
        ImageView leftUserHeadIV;
        TextView leftLocalTV;// 位置信息
        TextView defualtLoc;
        ImageView img;
        View row_rec_location;
        TextView name;
        TextView remark;

        public ItemLeftHB(View view) {
            msgTimeTV = (TextView) view.findViewById(R.id.timestamp_tv);
            leftLocalTV = (TextView) view.findViewById(R.id.tv_location);//恭喜语句
            leftUserHeadIV = (ImageView) view.findViewById(R.id.iv_userhead);
            defualtLoc = (TextView) view.findViewById(R.id.defult_location);//领取语句
            img = (ImageView) view.findViewById(R.id.img);
            row_rec_location = view.findViewById(R.id.row_rec_location);
            name = (TextView) view.findViewById(R.id.name);
            remark = (TextView) view.findViewById(R.id.remark);
        }

        @Override
        public void bandData(final Message msg, int position) {
            setLongClickListener(msg, leftLocalTV, false);
            showTime(position, msgTimeTV);
            final CDDHBMsg cddhbMsg = (CDDHBMsg) msg.getContent();
            defualtLoc.setText("￥" + cddhbMsg.getRedPacketType());
            remark.setText("" + cddhbMsg.getRedPacketRemake());
            row_rec_location.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (redBClickListener != null)
                        redBClickListener.redBClick(cddhbMsg.getRedPacketID(), false);
                }
            });
            RongUserBean rongUserBean = getUser(msg.getSenderUserId());
            if (rongUserBean != null) {
                List<RongUserBean.UrlsBean> urlsBeanns = rongUserBean.getUrls();
                if (urlsBeanns == null || urlsBeanns.size() <= 0) return;
                ImageLoaderBuilder.Builder().loadFromHttp(ServerUtils.getImgServer(urlsBeanns.get(0).getUrl()))
                        .showImageForEmptyUri(R.drawable.user_fang_icon)
                        .showImageOnFail(R.drawable.user_fang_icon)
                        .showImageOnLoading(R.drawable.user_fang_icon)
                        .roundedImage(8).displayImage(leftUserHeadIV);
                name.setText(TextUtils.isEmpty(rongUserBean.getNoteName()) ? TextUtils.isEmpty(rongUserBean.getNick()) ? rongUserBean.getUserId() : rongUserBean.getNick() : rongUserBean.getNoteName());
            } else {
                name.setText(msg.getSenderUserId());
            }
            if (msg.getConversationType().equals(Conversation.ConversationType.CUSTOMER_SERVICE))
                leftUserHeadIV.setImageResource(R.drawable.contacter_server);
            setIconClickListener(msg.getSenderUserId(), leftUserHeadIV);
            if (msg.getConversationType() == Conversation.ConversationType.GROUP) {
                name.setVisibility(View.VISIBLE);
            } else {
                name.setVisibility(View.GONE);
            }
        }
    }

    RedBClickListener redBClickListener;

    public interface RedBClickListener {
        void redBClick(String redBId, boolean isSend);
    }

    public void setRedBClickListener(RedBClickListener redBClickListener) {
        this.redBClickListener = redBClickListener;
    }

    /**
     * 右边显示的红包信息
     */
    class ItemRightHB extends Holder {
        TextView msgTimeTV;
        ImageView rightUserHeadIV;
        TextView rightLocalTV;// 位置信息
        ImageView msg_status;
        ProgressBar progressBar;
        TextView defualtLoc;
        ImageView img;
        View row_rec_location;
        TextView remark;

        public ItemRightHB(View view) {
            msgTimeTV = (TextView) view.findViewById(R.id.timestamp_tv);
            rightLocalTV = (TextView) view.findViewById(R.id.tv_location);
            rightUserHeadIV = (ImageView) view.findViewById(R.id.iv_userhead);
            msg_status = (ImageView) view.findViewById(R.id.msg_status);
            progressBar = (ProgressBar) view.findViewById(R.id.pb_sending);
            defualtLoc = (TextView) view.findViewById(R.id.defult_location);
            img = (ImageView) view.findViewById(R.id.img);
            row_rec_location = view.findViewById(R.id.row_rec_location);
            remark = (TextView) view.findViewById(R.id.remark);
        }

        @Override
        public void bandData(final Message msg, int position) {
            showTime(position, msgTimeTV);
            final CDDHBMsg cddhbMsg = (CDDHBMsg) msg.getContent();
            final Message.SentStatus sentStatus = msg.getSentStatus();
            defualtLoc.setText("￥" + cddhbMsg.getRedPacketType());
            remark.setText("" + cddhbMsg.getRedPacketRemake());
            switch (sentStatus) {
                case FAILED:
                    msg_status.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    showError(msg, msgTimeTV);
                    break;
                case SENDING:
                    msg_status.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case SENT:
                    msg_status.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    break;
            }
            msg_status.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (sentStatus == Message.SentStatus.FAILED) {
                        reSendMsg(msg);
                    }
                }
            });
            row_rec_location.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (redBClickListener != null)
                        redBClickListener.redBClick(cddhbMsg.getRedPacketID(), true);
                }
            });
            setLongClickListener(msg, row_rec_location, true);
            ImageLoaderBuilder.Builder().loadFromHttp(myIcon).showImageForEmptyUri(R.drawable.user_fang_icon).showImageOnLoading(R.drawable.user_fang_icon).showImageOnFail(R.drawable.user_fang_icon).roundedImage(8).displayImage(rightUserHeadIV);
            setIconClickListener(sendId, rightUserHeadIV);
        }
    }

    /**
     * 左边显示的转账信息
     */
    class ItemLeftZZ extends Holder {
        TextView msgTimeTV;
        ImageView leftUserHeadIV;
        TextView leftLocalTV;// 位置信息
        TextView defualtLoc;
        ImageView img;
        View row_rec_location;
        TextView name;
        TextView remark;

        public ItemLeftZZ(View view) {
            msgTimeTV = (TextView) view.findViewById(R.id.timestamp_tv);
            leftLocalTV = (TextView) view.findViewById(R.id.tv_location);//是否收到的语句
            leftUserHeadIV = (ImageView) view.findViewById(R.id.iv_userhead);
            defualtLoc = (TextView) view.findViewById(R.id.defult_location);//转账金额
            img = (ImageView) view.findViewById(R.id.img);
            row_rec_location = view.findViewById(R.id.row_rec_location);
            name = (TextView) view.findViewById(R.id.name);
            remark = (TextView) view.findViewById(R.id.remark);
        }

        @Override
        public void bandData(final Message msg, int position) {
            setLongClickListener(msg, leftLocalTV, false);
            showTime(position, msgTimeTV);
            final CDDZZMsg cddhbMsg = (CDDZZMsg) msg.getContent();
            defualtLoc.setText("￥ " + cddhbMsg.getMoney());
            remark.setText(TextUtils.isEmpty(cddhbMsg.getRemake()) ? "转账" : cddhbMsg.getRemake());
//            leftLocalTV.setText(locationMessage.getPoi());
//            ImageLoaderBuilder.Builder().loadFromSDCard(locationMessage.getImgUri().getPath()).showImageForEmptyUri(R.drawable.location_msg).showImageOnFail(R.drawable.location_msg).showImageOnLoading(R.drawable.location_msg).displayImage(img);
            row_rec_location.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, TransferAccountsDetailActivity.class);
                    intent.putExtra(TransferAccountsDetailActivity.TYPE_KEY, TransferAccountsDetailActivity.PAYEE);
                    intent.putExtra(TransferAccountsDetailActivity.ID_KEY, cddhbMsg.getTransferMoneyID());
                    mContext.startActivity(intent);
                }
            });
            RongUserBean rongUserBean = getUser(msg.getSenderUserId());
            if (rongUserBean != null) {
                List<RongUserBean.UrlsBean> urlsBeanns = rongUserBean.getUrls();
                if (urlsBeanns == null || urlsBeanns.size() <= 0) return;
                ImageLoaderBuilder.Builder().loadFromHttp(ServerUtils.getImgServer(urlsBeanns.get(0).getUrl()))
                        .showImageForEmptyUri(R.drawable.user_fang_icon)
                        .showImageOnFail(R.drawable.user_fang_icon)
                        .showImageOnLoading(R.drawable.user_fang_icon)
                        .roundedImage(8).displayImage(leftUserHeadIV);
                name.setText(TextUtils.isEmpty(rongUserBean.getNoteName()) ? TextUtils.isEmpty(rongUserBean.getNick()) ? rongUserBean.getUserId() : rongUserBean.getNick() : rongUserBean.getNoteName());
            } else {
                name.setText(msg.getSenderUserId());
            }
            if (msg.getConversationType().equals(Conversation.ConversationType.CUSTOMER_SERVICE))
                leftUserHeadIV.setImageResource(R.drawable.contacter_server);
            setIconClickListener(msg.getSenderUserId(), leftUserHeadIV);
            if (msg.getConversationType() == Conversation.ConversationType.GROUP) {
                name.setVisibility(View.VISIBLE);
            } else {
                name.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 右边显示的转账信息
     */
    class ItemRightZZ extends Holder {
        TextView msgTimeTV;
        ImageView rightUserHeadIV;
        TextView rightLocalTV;// 位置信息
        ImageView msg_status;
        ProgressBar progressBar;
        TextView defualtLoc;
        ImageView img;
        View row_rec_location;
        TextView remark;

        public ItemRightZZ(View view) {
            msgTimeTV = (TextView) view.findViewById(R.id.timestamp_tv);
            rightLocalTV = (TextView) view.findViewById(R.id.tv_location);//提示是否收到
            rightUserHeadIV = (ImageView) view.findViewById(R.id.iv_userhead);
            msg_status = (ImageView) view.findViewById(R.id.msg_status);
            progressBar = (ProgressBar) view.findViewById(R.id.pb_sending);
            defualtLoc = (TextView) view.findViewById(R.id.defult_location);//金额
            img = (ImageView) view.findViewById(R.id.img);
            remark = (TextView) view.findViewById(R.id.remark);//备注
            row_rec_location = view.findViewById(R.id.row_rec_location);
        }

        @Override
        public void bandData(final Message msg, int position) {
            showTime(position, msgTimeTV);
            final CDDZZMsg cddhbMsg = (CDDZZMsg) msg.getContent();
            final Message.SentStatus sentStatus = msg.getSentStatus();
            defualtLoc.setText("￥" + cddhbMsg.getMoney());
            remark.setText(TextUtils.isEmpty(cddhbMsg.getRemake()) ? "转账" : cddhbMsg.getRemake());
//            rightLocalTV.setText(locationMessage.getPoi());
//            ImageLoaderBuilder.Builder().loadFromSDCard(locationMessage.getImgUri().getPath()).showImageForEmptyUri(R.drawable.location_msg).showImageOnFail(R.drawable.location_msg).showImageOnLoading(R.drawable.location_msg).displayImage(img);
            switch (sentStatus) {
                case FAILED:
                    msg_status.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    showError(msg, msgTimeTV);
                    break;
                case SENDING:
                    msg_status.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case SENT:
                    msg_status.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    break;
            }
            msg_status.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (sentStatus == Message.SentStatus.FAILED) {
                        reSendMsg(msg);
                    }
                }
            });

            row_rec_location.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, TransferAccountsDetailActivity.class);
                    intent.putExtra(TransferAccountsDetailActivity.TYPE_KEY, TransferAccountsDetailActivity.PAYER);
                    intent.putExtra(TransferAccountsDetailActivity.ID_KEY, cddhbMsg.getTransferMoneyID());
                    mContext.startActivity(intent);
                }
            });
            setLongClickListener(msg, row_rec_location, true);
            ImageLoaderBuilder.Builder().loadFromHttp(myIcon).showImageForEmptyUri(R.drawable.user_fang_icon).showImageOnLoading(R.drawable.user_fang_icon).showImageOnFail(R.drawable.user_fang_icon).roundedImage(8).displayImage(rightUserHeadIV);
            setIconClickListener(sendId, rightUserHeadIV);
        }
    }

    /**
     * 左边显示的位 信息
     */
    class ItemLeftLocal extends Holder {
        TextView msgTimeTV;
        ImageView leftUserHeadIV;
        TextView leftLocalTV;// 位置信息
        TextView defualtLoc;
        ImageView img;
        View row_rec_location;
        TextView name;

        public ItemLeftLocal(View view) {
            msgTimeTV = (TextView) view.findViewById(R.id.timestamp_tv);
            leftLocalTV = (TextView) view.findViewById(R.id.tv_location);
            leftUserHeadIV = (ImageView) view.findViewById(R.id.iv_userhead);
            defualtLoc = (TextView) view.findViewById(R.id.defult_location);
            img = (ImageView) view.findViewById(R.id.img);
            row_rec_location = view.findViewById(R.id.row_rec_location);
            name = (TextView) view.findViewById(R.id.name);
        }

        @Override
        public void bandData(final Message msg, int position) {
            setLongClickListener(msg, leftLocalTV, false);
            showTime(position, msgTimeTV);
            CDDLBSMsg locationMessage = (CDDLBSMsg) msg.getContent();
            defualtLoc.setText(locationMessage.getExtra());
            leftLocalTV.setText(locationMessage.getPoi());
            ImageLoaderBuilder.Builder().loadFromSDCard(locationMessage.getImgUri().getPath()).showImageForEmptyUri(R.drawable.location_msg).showImageOnFail(R.drawable.location_msg).showImageOnLoading(R.drawable.location_msg).displayImage(img);
//            row_rec_location.setOnClickListener(new RongLocClickListener(mContext, locationMessage.getLat(), locationMessage.getLng()));
            row_rec_location.setOnClickListener(new RongLocClickListener(mContext, locationMessage));
            RongUserBean rongUserBean = getUser(msg.getSenderUserId());
            if (rongUserBean != null) {
                List<RongUserBean.UrlsBean> urlsBeanns = rongUserBean.getUrls();
                if (urlsBeanns == null || urlsBeanns.size() <= 0) return;
                ImageLoaderBuilder.Builder().loadFromHttp(ServerUtils.getImgServer(urlsBeanns.get(0).getUrl()))
                        .showImageForEmptyUri(R.drawable.user_fang_icon)
                        .showImageOnFail(R.drawable.user_fang_icon)
                        .showImageOnLoading(R.drawable.user_fang_icon)
                        .roundedImage(8).displayImage(leftUserHeadIV);
                name.setText(TextUtils.isEmpty(rongUserBean.getNoteName()) ? TextUtils.isEmpty(rongUserBean.getNick()) ? rongUserBean.getUserId() : rongUserBean.getNick() : rongUserBean.getNoteName());
            } else {
                name.setText(msg.getSenderUserId());
            }
            if (msg.getConversationType().equals(Conversation.ConversationType.CUSTOMER_SERVICE))
                leftUserHeadIV.setImageResource(R.drawable.contacter_server);
            setIconClickListener(msg.getSenderUserId(), leftUserHeadIV);
            if (msg.getConversationType() == Conversation.ConversationType.GROUP) {
                name.setVisibility(View.VISIBLE);
            } else {
                name.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 右边显示的位 信息
     */
    class ItemRightLocal extends Holder {
        TextView msgTimeTV;
        ImageView rightUserHeadIV;
        TextView rightLocalTV;// 位置信息
        ImageView msg_status;
        ProgressBar progressBar;
        TextView defualtLoc;
        ImageView img;
        View row_rec_location;

        public ItemRightLocal(View view) {
            msgTimeTV = (TextView) view.findViewById(R.id.timestamp_tv);
            rightLocalTV = (TextView) view.findViewById(R.id.tv_location);
            rightUserHeadIV = (ImageView) view.findViewById(R.id.iv_userhead);
            msg_status = (ImageView) view.findViewById(R.id.msg_status);
            progressBar = (ProgressBar) view.findViewById(R.id.pb_sending);
            defualtLoc = (TextView) view.findViewById(R.id.defult_location);
            img = (ImageView) view.findViewById(R.id.img);
            row_rec_location = view.findViewById(R.id.row_rec_location);
        }

        @Override
        public void bandData(final Message msg, int position) {
            showTime(position, msgTimeTV);
            final CDDLBSMsg locationMessage = (CDDLBSMsg) msg.getContent();
            final Message.SentStatus sentStatus = msg.getSentStatus();
            defualtLoc.setText(locationMessage.getExtra());
            rightLocalTV.setText(locationMessage.getPoi());
            ImageLoaderBuilder.Builder().loadFromSDCard(locationMessage.getImgUri().getPath()).showImageForEmptyUri(R.drawable.location_msg).showImageOnFail(R.drawable.location_msg).showImageOnLoading(R.drawable.location_msg).displayImage(img);
            switch (sentStatus) {
                case FAILED:
                    msg_status.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    showError(msg, msgTimeTV);
                    break;
                case SENDING:
                    msg_status.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case SENT:
                    msg_status.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    break;
            }
            msg_status.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (sentStatus == Message.SentStatus.FAILED) {
                        reSendMsg(msg);
                    }
                }
            });
//            row_rec_location.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(mContext, ShowLocationMapActivity.class);
//                    intent.putExtra("latitude", locationMessage.getLat());
//                    intent.putExtra("longitude", locationMessage.getLng());
//                    mContext.startActivity(intent);
//                }
//            });
            row_rec_location.setOnClickListener(new RongLocClickListener(mContext, locationMessage));
            setLongClickListener(msg, row_rec_location, true);
            ImageLoaderBuilder.Builder().loadFromHttp(myIcon).showImageForEmptyUri(R.drawable.user_fang_icon).showImageOnLoading(R.drawable.user_fang_icon).showImageOnFail(R.drawable.user_fang_icon).roundedImage(8).displayImage(rightUserHeadIV);
            setIconClickListener(sendId, rightUserHeadIV);
        }
    }


    /**
     * 撤回消息
     */
    class ItemChe extends Holder {
        TextView msgTimeTV;
        TextView che;

        public ItemChe(View view) {
            msgTimeTV = (TextView) view.findViewById(R.id.timestamp_tv);
            che = (TextView) view.findViewById(R.id.che);
        }

        @Override
        public void bandData(final Message msg, int position) {
            showTime(position, msgTimeTV);
            if (msg.getSenderUserId().equals(sendId)) {
                che.setText("您撤回了1条消息");
            } else {
                if (msg.getConversationType() == Conversation.ConversationType.PRIVATE)
                    che.setText("TA撤回了一条消息");
                else {
                    RongUserBean rongUserBean = getUser(msg.getSenderUserId());
                    if (rongUserBean != null) {
                        che.setText((TextUtils.isEmpty(rongUserBean.getNoteName()) ? TextUtils.isEmpty(rongUserBean.getNick()) ? rongUserBean.getUserId() : rongUserBean.getNick() : rongUserBean.getNoteName()) + "撤回了一条消息");
                    } else {
                        che.setText(msg.getSenderUserId() + "撤回了一条消息");
                    }
                }
            }
        }
    }

    /**
     * 群通知消息
     */
    class GroupNtfMsg extends Holder {
        TextView msgTimeTV;
        TextView che;

        public GroupNtfMsg(View view) {
            msgTimeTV = (TextView) view.findViewById(R.id.timestamp_tv);
            che = (TextView) view.findViewById(R.id.che);
        }

        @Override
        public void bandData(final Message msg, int position) {
            showTime(position, msgTimeTV);
            GroupNotificationMessage groupNotificationMessage = (GroupNotificationMessage) msg.getContent();
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
                    che.setText(groupNTFMsgDataBean.getOperatorNickname() + "将" + buffer.toString() + "拉入群");
                } else if (operation.equals(GroupNotificationMessage.GROUP_OPERATION_QUIT)) {//退群
                    che.setText(groupNTFMsgDataBean.getOperatorNickname() + "退出了群");
                } else if (operation.equals(GroupNotificationMessage.GROUP_OPERATION_KICKED)) {//踢出群
                    StringBuffer buffer = new StringBuffer();
                    for (int i = 0; i < groupNTFMsgDataBean.getTargetUserDisplayNames().size(); i++) {
                        String name = groupNTFMsgDataBean.getTargetUserDisplayNames().get(i);
                        buffer.append(name);
                        if (i != groupNTFMsgDataBean.getTargetUserDisplayNames().size() - 1)
                            buffer.append(",");
                    }
                    che.setText(buffer.toString() + "被管理员踢出群");
                    if (groupNTFMsgDataBean.getTargetUserIds() != null && groupNTFMsgDataBean.getTargetUserIds().contains(sendId)) {
                        CDDRongApi.isInGroup(msg.getTargetId(), sendId, new CDDRongApi.NetWorkCallback<Boolean>() {
                            @Override
                            public void onSuccess(String id, Boolean s) {
                                if (!s) {
                                    if (unGroupCallback != null) unGroupCallback.unGroup();
                                }
                            }

                            @Override
                            public void onFailure(Throwable t, int errorNo, String strMsg) {
                            }
                        });
                    }
                } else if (operation.equals(GroupNotificationMessage.GROUP_OPERATION_RENAME)) {//重命名
                    che.setText(groupNTFMsgDataBean.getOperatorNickname() + "将群名称变更为\"" + groupNTFMsgDataBean.getTargetGroupName() + "\"");
                } else if (operation.equals(GroupNotificationMessage.GROUP_OPERATION_BULLETIN)) {//公告变更
                    che.setText(groupNTFMsgDataBean.getOperatorNickname() + "变更了群公告");
                } else if (operation.equals("Create")) {//创建群
                    che.setText(groupNTFMsgDataBean.getOperatorNickname() + "创建了群");
                } else if (operation.equals("Dismiss")) {//解散群
                    che.setText(groupNTFMsgDataBean.getOperatorNickname() + "解散了群");
                    if (unGroupCallback != null)
                        unGroupCallback.unGroup();
                } else if (operation.equals("Transfer")) {//移交管理员
                    che.setText(groupNTFMsgDataBean.getOperatorNickname() + "将管理员权限移交给" + groupNTFMsgDataBean.getTargetUserDisplayName());
                }
            } catch (Exception e) {
                e.printStackTrace();
                che.setText(groupNotificationMessage.getOperatorUserId() + "变更了群信息");
            }
        }
    }

    private UnGroupCallback unGroupCallback;

    public void setUnGroupCallback(UnGroupCallback unGroupCallback) {
        this.unGroupCallback = unGroupCallback;
    }

    public interface UnGroupCallback {
        void unGroup();
    }

    /**
     * 未知 型消息
     */
    class ItemUnkonw extends Holder {
        TextView msgTimeTV;
        TextView che;

        public ItemUnkonw(View view) {
            msgTimeTV = (TextView) view.findViewById(R.id.timestamp_tv);
            che = (TextView) view.findViewById(R.id.che);
        }

        @Override
        public void bandData(final Message msg, int position) {
            showTime(position, msgTimeTV);
            Log.e("-----", "未知类型消息---:" + msg.getObjectName());
            if (msg.getSenderUserId().equals(sendId)) {
                che.setText("您发出了1条未知类型消息");
            } else {
                if (msg.getConversationType() == Conversation.ConversationType.PRIVATE)
                    che.setText("TA发出了一条未知类型消息");
                else {
                    RongUserBean rongUserBean = getUser(msg.getSenderUserId());
                    if (rongUserBean != null) {
                        che.setText((TextUtils.isEmpty(rongUserBean.getNoteName()) ? TextUtils.isEmpty(rongUserBean.getNick()) ? rongUserBean.getUserId() : rongUserBean.getNick() : rongUserBean.getNoteName()) + "发出了一条未知类型消息");
                    } else {
                        che.setText(msg.getSenderUserId() + "发出了一条未知类型消息");
                    }
                }
            }
        }
    }

    /**
     * 订单提醒
     */
    class ItemOrder extends Holder {
        TextView msgTimeTV;
        ImageView headIV;
        TextView orderStatusTV;//订单状态
        ImageView projectIV;//项目图标
        TextView orderNumTV;//订单号
        TextView projectNameTV;//项目名称
        TextView orderPriceTV;//订单金额
        LinearLayout orderItemLL;//订单选项

        public ItemOrder(View view) {
            msgTimeTV = (TextView) view.findViewById(R.id.timestamp_tv);
            headIV = (ImageView) view.findViewById(R.id.iv_userhead);
            projectIV = (ImageView) view.findViewById(R.id.project_iv);
            orderStatusTV = (TextView) view.findViewById(R.id.order_status_tv);
            projectNameTV = (TextView) view.findViewById(R.id.project_name_tv);
            orderNumTV = (TextView) view.findViewById(R.id.order_number_tv);
            orderPriceTV = (TextView) view.findViewById(R.id.order_price_tv);
            orderItemLL = (LinearLayout) view.findViewById(R.id.item_order_ll);
        }

        @Override
        public void bandData(final Message msg, int position) {
            showTime(position, msgTimeTV);
            final CDDTaskMsg taskMsg = (CDDTaskMsg) msg.getContent();
            if (TextUtils.isEmpty(taskMsg.getContent())) {
                return;
            }
            headIV.setImageResource(R.drawable.order_remind_icon);
            MyLog.e("YUY", "订单提醒信息 =" + taskMsg.getContent());
            final TaskMsgBean taskMsgBean = TaskMsgBean.parse(taskMsg.getContent());
            if (!taskMsgBean.getUserId().equals(CacheTools.getUserData("userId"))) {
                taskMsgBean.setFlag("1");
            } else {
                taskMsgBean.setFlag("3");
            }
            OrderStateUtil.orderStatusShow(Integer.parseInt(taskMsgBean.getStatus()), Integer.parseInt(taskMsgBean.getFlag()), orderStatusTV);
            orderNumTV.setText("订单号 : " + taskMsgBean.getOrderNum());
            projectNameTV.setText(ServiceUtils.getProjectName(taskMsgBean.getProjectNum()));
            ImageUtils.displayImage(taskMsgBean.getProjectImg(), projectIV, 360);
            orderPriceTV.setText("订单金额 : " + taskMsgBean.getPrice() + "元");
            orderItemLL.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if(!CacheTools.getUserData("role").contains("2")){
//                        ToastUtil.toastShow(mContext,"不是商家不能查看");
//                       return;
//                    }
//                    if(!CacheTools.getUserData("role").contains("3")){
//                        ToastUtil.toastShow(mContext,"不是商家不能查看");
//                        return;
//                    }
                    Intent intent;
                    String orderstatus = taskMsgBean.getStatus();
                    if (!taskMsgBean.getUserId().equals(CacheTools.getUserData("userId")) && (orderstatus.equals("27") || orderstatus.equals("28"))) {
                        intent = new Intent(mContext, NewRobOrderDetailsActivity.class);
                    } else if (taskMsgBean.getUserId().equals(CacheTools.getUserData("userId")) && (orderstatus.equals("27") || orderstatus.equals("28"))) {
                        intent = new Intent(mContext, ToquoteAcitivity.class);
                    } else {
                        intent = new Intent(mContext, OrderActivity.class);
                    }
                    intent.putExtra("taskId", taskMsgBean.getTaskId());
                    mContext.startActivity(intent);

                }
            });
        }
    }

    class ItemServerNotification extends Holder {
        TextView msgTimeTV;// 信息发送及接收时间
        TextView che;

        public ItemServerNotification(View view) {
            msgTimeTV = (TextView) view.findViewById(R.id.timestamp_tv);
            che = (TextView) view.findViewById(R.id.che);
        }

        @Override
        public void bandData(Message msg, int position) {
            showTime(position, msgTimeTV);
            InformationNotificationMessage messageContent = (InformationNotificationMessage) msg.getContent();
            if (msg.getConversationType().equals(Conversation.ConversationType.CUSTOMER_SERVICE))
                che.setText(messageContent.getMessage());
//            setLongClickListener(msg, contentTV, true);
        }
    }

    abstract class Holder {
        public abstract void bandData(Message msg, int position);
    }

    private void showTime(int position, TextView timestamp) {
        // 两条消息时间离得如果稍长，显示时间
        if (position == 0) {
            timestamp.setVisibility(View.VISIBLE);
            timestamp.setText(simpleDateFormat.format(new Date(mData.get(position).getSentTime())));
            return;
        }
        Message prevMessage = mData.get(position - 1);
        if (prevMessage != null && ChatUtils.daysBetween(prevMessage.getSentTime(), mData.get(position).getSentTime())) {
            timestamp.setVisibility(View.GONE);
        } else {
            timestamp.setVisibility(View.VISIBLE);
            timestamp.setText(simpleDateFormat.format(new Date(mData.get(position).getSentTime())));
        }
    }

    private boolean isPostGroup = false;
    private boolean isGroup = true;

    /**
     * 应在调用showTime()方法后调用此方法
     * black=4表示：甲乙两人互相不是黑名单用户
     * 1：甲的黑名单只有乙
     * 2：乙的黑名单只有甲,
     * 3：情况很多（甲乙分别的黑名单用户加起来大于等于2）
     *
     * @param message
     * @param textView
     */
    private void showError(Message message, final TextView textView) {
        switch (message.getConversationType()) {
            case PRIVATE:
                RongUserBean rongUserBean = getUser(message.getTargetId());
                if (rongUserBean != null) {
                    if (!rongUserBean.getBlack().equals("2") && !rongUserBean.getBlack().equals("3")) {
                        userBeanMap.remove(message.getTargetId());
                        getUser(message.getTargetId());
                    } else {
                        textView.setText("对方拒绝接受您的消息!");
                        textView.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case GROUP:
                if (groupBean != null && groupBean.getId() == -1) {
                    textView.setText("您当前不是群成员!");
                    textView.setVisibility(View.VISIBLE);
                    return;
                }
                if (!isGroup) {
                    if (unGroupCallback != null) unGroupCallback.unGroup();
                    textView.setText("您当前不是群成员!");
                    textView.setVisibility(View.VISIBLE);
                    return;
                }
                if (!isPostGroup) {
                    isPostGroup = true;
                    CDDRongApi.isInGroup(message.getTargetId(), sendId, new CDDRongApi.NetWorkCallback<Boolean>() {
                        @Override
                        public void onSuccess(String id, Boolean s) {
                            if (!s) {
                                if (unGroupCallback != null) unGroupCallback.unGroup();
                                textView.setText("您当前不是群成员!");
                                textView.setVisibility(View.VISIBLE);
                                isGroup = false;
                            }
                            isPostGroup = false;
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(Throwable t, int errorNo, String strMsg) {
                            isPostGroup = false;
                            notifyDataSetChanged();
                        }
                    });
                }
                break;
        }
    }

    /**
     * 转发消息
     *
     * @param msg    转发的消息
     * @param view   要监听的view
     * @param isSend 是否是自己发的
     */

    private void setLongClickListener(final Message msg, View view, final boolean isSend) {
        //对传进来的View进行长按监听
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showMsgDoDialog(msg, isSend);
                return true;
            }
        });
    }

    private void setIconClickListener(final String id, View view) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PersonalHomeActivity2.class);
                intent.putExtra(PersonalHomeActivity2.NEW_FRIENDID, id);
                mContext.startActivity(intent);
            }
        });
    }

    /**
     * 显示出对消息的操作
     * 不同的消息有不同的操作
     *
     * @param msg
     */
    private void showMsgDoDialog(final Message msg, boolean isSend) {
        final Dialog dialog = new Dialog(mContext, R.style.myDialogTheme);
        View view = mInflater.inflate(R.layout.msg_do_menu, null);
        dialog.setContentView(view);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width = ScreenUtils.getScreenWidth(mContext) * 2 / 3;//宽高可设置具体大小
        layoutParams.height = LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);
        TextView copyTV = (TextView) view.findViewById(R.id.copy_tv);
        TextView zhuanfaTV = (TextView) view.findViewById(R.id.zhuanfa_tv);
        TextView deleteTV = (TextView) view.findViewById(R.id.delete_tv);
        TextView chehuiTV = (TextView) view.findViewById(R.id.chehui_tv);
        final String objectName = msg.getObjectName();
        if (objectName.equals(RongMsgType.RC_VC_MSG))
            zhuanfaTV.setVisibility(View.GONE);
        else zhuanfaTV.setVisibility(View.VISIBLE);
        if (isSend)
            chehuiTV.setVisibility(View.VISIBLE);
        else
            chehuiTV.setVisibility(View.GONE);
        if (objectName.equals(RongMsgType.CDD_TXT_MSG)) {//颜文字
            copyTV.setVisibility(View.VISIBLE);
        } else if (objectName.equals(RongMsgType.GIF_TXT_MSG)) {
            copyTV.setVisibility(View.GONE);
        } else if (objectName.equals(RongMsgType.CDD_LBS_MSG)) {
            copyTV.setVisibility(View.GONE);
        } else if (objectName.equals(RongMsgType.RC_TXT_MSG)) {
            copyTV.setVisibility(View.VISIBLE);
        } else if (objectName.equals(RongMsgType.RC_VC_MSG)) {
            copyTV.setVisibility(View.GONE);
        } else if (objectName.equals(RongMsgType.RC_IMG_MSG)) {
            copyTV.setVisibility(View.GONE);
        } else if (objectName.equals(RongMsgType.RC_IMG_TXT_MSG)) {//图文暂时无用
            copyTV.setVisibility(View.GONE);
        }
//        else if (objectName.equals(RongMsgType.RC_CHE_MSG)) {//撤回消息
//            copyTV.setVisibility(View.GONE);
//        }
        else if (objectName.equals(RongMsgType.CDD_HB_MSG)) {
            copyTV.setVisibility(View.GONE);
            zhuanfaTV.setVisibility(View.GONE);
            chehuiTV.setVisibility(View.GONE);
            if (isSend)
                return;
            else
                deleteTV.setVisibility(View.VISIBLE);
        } else if (objectName.equals(RongMsgType.CDD_ZZ_MSG)) {
            copyTV.setVisibility(View.GONE);
            zhuanfaTV.setVisibility(View.GONE);
            chehuiTV.setVisibility(View.GONE);
            if (isSend)
                return;
            else
                deleteTV.setVisibility(View.VISIBLE);
        } else {
            return;
        }
        copyTV.setOnClickListener(new OnClickListener() {//复制监听
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                if (objectName.equals(RongMsgType.CDD_TXT_MSG)) {//颜文字
                    cm.setText(((CDDYWZMsg) msg.getContent()).getContent());
                } else if (objectName.equals(RongMsgType.RC_TXT_MSG)) {
                    cm.setText(((TextMessage) msg.getContent()).getContent());
                }
                ToastUtil.toastShow(mContext, "已复制");
                dialog.dismiss();
            }
        });
        zhuanfaTV.setOnClickListener(new OnClickListener() {//转发
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EstablishGroupFirstStepActivtiy.class);
                intent.putExtra("type", Constant.FORWARD_GROUP);//标识消息转发
                intent.putExtra("msg", msg);//转发的消息
                mContext.startActivity(intent);
                dialog.dismiss();
            }
        });
        deleteTV.setOnClickListener(new OnClickListener() {//删除
            @Override
            public void onClick(View v) {
                deleteMsg(msg);
                dialog.dismiss();
            }
        });
        chehuiTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                recallMsg(msg);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 撤回消息
     *
     * @param msg
     */
    private void recallMsg(Message msg) {
        Long sendTime = msg.getSentTime();
        Long thisTime = new Date().getTime();
        if (thisTime - sendTime > 120000) {
            Toast.makeText(mContext, "发送太久，已不能撤回!", Toast.LENGTH_SHORT).show();
            return;
        }
        RongApi.recallMessage(msg, new RongIMClient.ResultCallback<RecallNotificationMessage>() {
                    @Override
                    public void onSuccess(RecallNotificationMessage recallNotificationMessage) {
                        if (getReSendCallback != null)
                            getReSendCallback.get().onSuccess(null);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        Toast.makeText(mContext, "撤回失败", Toast.LENGTH_SHORT).show();
                    }
                }

        );
    }

    private void postGroupUser(final String targetId) {
        if (postList.contains(targetId)) return;
        postList.add(targetId);
        CDDRongApi.getGroupMeber(targetId, sendId, new CDDRongApi.NetWorkCallback<List<GroupSetUpMemberInformationEr>>() {
            @Override
            public void onSuccess(String id, List<GroupSetUpMemberInformationEr> groupBean) {
                if (groupBean != null && groupBean.size() > 0) {
                    for (GroupSetUpMemberInformationEr u : groupBean) {
                        userBeanMap.put(u.getAccid(), new RongUserBean(u.getAccid(), u.getUser_group_name(), u.getHead_image_url()));
                    }
                }
                postList.remove(targetId);
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                postList.remove(targetId);
                t.printStackTrace();
            }
        });
    }

    private void postUser(final String id) {
        RongUserBean user = userBeanMap.get(id);
        if (user == null) {
            if (postList.contains(id)) return;
            postList.add(id);
            CDDRongApi.getUserById(id, sendId, new CDDRongApi.NetWorkCallback<List<RongUserBean>>() {
                @Override
                public void onSuccess(String id, List<RongUserBean> o) {
                    if (o == null || o.size() <= 0) return;
                    RongUserBean rongUserBean = o.get(0);
                    userBeanMap.put(id, rongUserBean);
                    postList.remove(id);
                    notifyDataSetChanged();
                }

                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    postList.remove(id);
                    t.printStackTrace();
                }
            });
        } else notifyDataSetChanged();
    }

    /**
     * 删  天消息
     */
    private void deleteMsg(Message msg) {
        final AlertDialog dialog1 = new AlertDialog(mContext).builder();
        dialog1.setTitle("温馨提示");
        dialog1.setMsg("删除后将不会出现在您的聊天记录中");
        final int[] ids = {msg.getMessageId()};
        dialog1.setPositiveButton("确定", new OnClickListener() {
            @Override
            public void onClick(View v) {
                RongApi.deleteMessages(ids, new RongIMClient.ResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        if (deleteMsgCallBack != null)
                            deleteMsgCallBack.deleteMsgCallBack();
                        ToastUtil.toastShow(mContext, "删除成功");
                        EventBus.getDefault().post(new ClearMessagesBean());//发送一个空消息 去刷新通讯记录界面
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        ToastUtil.toastShow(mContext, "删除失败");
                    }
                });
            }
        });
        dialog1.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        dialog1.show();
    }

    private DeleteMsgCallBack deleteMsgCallBack;

    public void setDeleteMsgCallBack(DeleteMsgCallBack deleteMsgCallBack) {
        this.deleteMsgCallBack = deleteMsgCallBack;
    }

    public interface DeleteMsgCallBack {
        void deleteMsgCallBack();
    }

    private RefUserCallBack refUserCallBack;

    public void setRefUserCallBack(RefUserCallBack refUserCallBack) {
        this.refUserCallBack = refUserCallBack;
    }

    public interface RefUserCallBack {
        void ref();
    }
}
