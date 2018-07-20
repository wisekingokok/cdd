package com.chewuwuyou.eim.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.ui.VehicleQuanVewPager;
import com.chewuwuyou.app.utils.AlertDialog;
import com.chewuwuyou.app.utils.ChatInputUtils;
import com.chewuwuyou.app.utils.ChatUtils;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.JsonUtil;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.ScreenUtils;
import com.chewuwuyou.app.utils.ServerUtils;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.eim.activity.im.BaiduMapActivity;
import com.chewuwuyou.eim.activity.im.ChatActivity;
import com.chewuwuyou.eim.activity.im.MessageTransActivity;
import com.chewuwuyou.eim.manager.MessageManager;
import com.chewuwuyou.eim.model.IMMessage;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * 聊天消息适配器
 *
 * @author yuyong
 */
@SuppressLint("HandlerLeak")
public class MessageAdapter extends BaseAdapter {
    private Context mContext;
    private final int mItemSize = 10;// 不同Item的项数
    private LayoutInflater mInflater;
    private List<IMMessage> mData;

    private ListView mMessageList;// 消息列表
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
    private static final int HANDLER_MESSAGE_REFRESH_LIST = 0;// 刷新页面
    private static final int HANDLER_MESSAGE_SELECT_LAST = 1;// 选择最后一条消息
    private static final int HANDLER_MESSAGE_SEEK_TO = 2;// 刷新并跳转到哪条消息

    private static Map<String, String> mFaceCharacterMap;// 表情的Map
    private String mToChat;
    Handler handler = new Handler() {
        private void refreshList() {
            mData = ((ChatActivity) mContext).getMessage();
            notifyDataSetChanged();
            if (mData.size() > 0) {
                mMessageList.setSelection(mData.size() - 1);
            }
        }

        @Override
        public void handleMessage(android.os.Message message) {
            switch (message.what) {
                case HANDLER_MESSAGE_REFRESH_LIST:
                    refreshList();
                    break;
                case HANDLER_MESSAGE_SELECT_LAST:
                    if (mContext instanceof ChatActivity) {
                        if (mData.size() > 0) {
                            mMessageList.setSelection(mData.size() - 1);
                        }
                    }
                    break;
                case HANDLER_MESSAGE_SEEK_TO:
                    int position = message.arg1;
                    if (mContext instanceof ChatActivity) {
                        mMessageList.setSelection(position);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @SuppressWarnings("static-access")
    public MessageAdapter(Context context, List<IMMessage> data, String toChat) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        this.mData = data;
        this.mMessageList = ((ChatActivity) mContext).getListView();
        this.mFaceCharacterMap = JsonUtil.getFaceStrMap(context);
        this.mToChat = toChat;
    }

    public void setMessage(List<IMMessage> data) {
        this.mData = data;
        notifyDataSetChanged();
        if (mData.size() > 0) {
            mMessageList.setSelection(mData.size() - 1);
        }
    }

    @Override
    public int getCount() {
        return mData.size();
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
        IMMessage message = mData.get(position);
        if (message.getType() == Constant.CHAT_MESSAGE_TYPE.TXT) {
            return message.getMsgType() == 0 ? MESSAGE_TYPE_RECV_TXT
                    : MESSAGE_TYPE_SEND_TXT;
        } else if (message.getType() == Constant.CHAT_MESSAGE_TYPE.IMAGE) {
            return message.getMsgType() == 0 ? MESSAGE_TYPE_RECV_IMG
                    : MESSAGE_TYPE_SEND_IMG;
        } else if (message.getType() == Constant.CHAT_MESSAGE_TYPE.LOCATION) {
            return message.getMsgType() == 0 ? MESSAGE_TYPE_RECV_LOC
                    : MESSAGE_TYPE_SEND_LOC;
        } else if (message.getType() == Constant.CHAT_MESSAGE_TYPE.VOICE) {
            return message.getMsgType() == 0 ? MESSAGE_TYPE_RECV_VOICE
                    : MESSAGE_TYPE_SEND_VOICE;
        } else if (message.getType() == Constant.CHAT_MESSAGE_TYPE.GIF_IMG) {
            return message.getMsgType() == 0 ? MESSAGE_TYPE_RECV_GIF
                    : MESSAGE_TYPE_SEND_GIF;
        } else {
            return message.getMsgType() == 0 ? MESSAGE_TYPE_RECV_TXT
                    : MESSAGE_TYPE_SEND_TXT;
        }
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IMMessage message = mData.get(position);
        Holder holder = null;
        if (convertView == null) {
            switch (getItemViewType(position)) {
                case MESSAGE_TYPE_RECV_TXT:// 收到的文本
                    holder = new ItemLeftTXT(convertView = mInflater.inflate(
                            R.layout.row_received_message, null));
                    break;
                case MESSAGE_TYPE_SEND_TXT:// 发出的文本
                    holder = new ItemRightTXT(convertView = mInflater.inflate(
                            R.layout.row_sent_message, null));
                    break;
                case MESSAGE_TYPE_RECV_GIF:// 收到的GIF
                    holder = new ItemLeftGif(convertView = mInflater.inflate(
                            R.layout.row_received_gif, null));
                    break;
                case MESSAGE_TYPE_SEND_GIF:// 发出的GIF
                    holder = new ItemRightGif(convertView = mInflater.inflate(
                            R.layout.row_sent_gif, null));
                    break;
                case MESSAGE_TYPE_RECV_IMG:// 收到的图片
                    holder = new ItemLeftImage(convertView = mInflater.inflate(
                            R.layout.row_received_picture, null));
                    break;
                case MESSAGE_TYPE_SEND_IMG:// 发出的图片
                    holder = new ItemRightImage(convertView = mInflater.inflate(
                            R.layout.row_sent_picture, null));
                    break;
                case MESSAGE_TYPE_RECV_VOICE:// 收到的语音
                    holder = new ItemLeftVoice(convertView = mInflater.inflate(
                            R.layout.row_received_voice, null));
                    break;
                case MESSAGE_TYPE_SEND_VOICE:// 发出的语音
                    holder = new ItemRightVoice(convertView = mInflater.inflate(
                            R.layout.row_sent_voice, null));
                    break;
                case MESSAGE_TYPE_RECV_LOC:// 收到的位置信息
                    holder = new ItemLeftLocal(convertView = mInflater.inflate(
                            R.layout.row_received_location, null));
                    break;
                case MESSAGE_TYPE_SEND_LOC:// 发出的位置信息
                    holder = new ItemRightLocal(convertView = mInflater.inflate(
                            R.layout.row_sent_location, null));
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
     * 左边显示的文本 可复用与文本、gif动态图、表情图
     */
    class ItemLeftTXT extends Holder {
        TextView msgTimeTV;// 信息发送及接收时间
        ImageView leftUserHeadIV;
        TextView contentTV;

        public ItemLeftTXT(View view) {
            contentTV = (TextView) view.findViewById(R.id.tv_chatcontent);
            leftUserHeadIV = (ImageView) view.findViewById(R.id.iv_userhead);
            msgTimeTV = (TextView) view.findViewById(R.id.timestamp_tv);
        }

        @Override
        public void bandData(IMMessage msg, int position) {
            showTime(position, msgTimeTV);
            ChatUtils.disPlayHisImg(mContext, msg, leftUserHeadIV, mToChat);
            setLongClickListener(msg, contentTV);
            switch (msg.getType()) {
                case Constant.CHAT_MESSAGE_TYPE.TXT:
                    contentTV.setText(msg.getContent());
                    break;
                case Constant.CHAT_MESSAGE_TYPE.YWZ_TXT:
                    contentTV.setText(ChatInputUtils.displayBigFacePic(mContext,
                            msg.getContent(), mFaceCharacterMap));
                    break;
                default:
                    contentTV.setText(msg.getContent());
                    break;
            }

        }
    }

    /**
     * 右边显示的文本 可复用与文本、gif动态图、表情图
     */
    class ItemRightTXT extends Holder {
        TextView msgTimeTV;
        ImageView rightUserHeadIV;
        TextView contentTV;

        public ItemRightTXT(View view) {
            contentTV = (TextView) view.findViewById(R.id.tv_chatcontent);
            rightUserHeadIV = (ImageView) view.findViewById(R.id.iv_userhead);
            msgTimeTV = (TextView) view.findViewById(R.id.timestamp_tv);
        }

        @Override
        public void bandData(IMMessage msg, int position) {
            showTime(position, msgTimeTV);
            ChatUtils.disPlayMeImg(mContext, rightUserHeadIV);
            setLongClickListener(msg, contentTV);
            switch (msg.getType()) {
                case Constant.CHAT_MESSAGE_TYPE.TXT:
                    contentTV.setText(msg.getContent());
                    break;
                case Constant.CHAT_MESSAGE_TYPE.YWZ_TXT:
                    contentTV.setText(ChatInputUtils.displayBigFacePic(mContext,
                            msg.getContent(), mFaceCharacterMap));
                    break;
                default:
                    contentTV.setText(msg.getContent());
                    break;
            }
        }
    }

    /**
     * 右边显示的GIF
     */
    class ItemRightGif extends Holder {

        TextView msgTimeTV;
        GifImageView rightGifTextView;
        ImageView rightUserHeadIV;

        public ItemRightGif(View view) {
            rightGifTextView = (GifImageView) view
                    .findViewById(R.id.gifTextView);
            rightUserHeadIV = (ImageView) view.findViewById(R.id.iv_userhead);
            msgTimeTV = (TextView) view.findViewById(R.id.timestamp_tv);
        }

        @Override
        public void bandData(IMMessage msg, int position) {
            setLongClickListener(msg, rightGifTextView);
            showTime(position, msgTimeTV);
            ChatUtils.disPlayMeImg(mContext, rightUserHeadIV);
            int gifEnd = msg.getContent().length() - 4;
            String path = "emoticons/" + msg.getContent().substring(4, gifEnd);
            try {
                rightGifTextView.setImageDrawable(new GifDrawable(mContext
                        .getAssets(), path));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
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

        public ItemLeftGif(View view) {
            leftGifTextView = (GifImageView) view
                    .findViewById(R.id.gifTextView);
            leftUserHeadIV = (ImageView) view.findViewById(R.id.iv_userhead);
            msgTimeTV = (TextView) view.findViewById(R.id.timestamp_tv);
        }

        @Override
        public void bandData(IMMessage msg, int position) {
            setLongClickListener(msg, leftGifTextView);
            showTime(position, msgTimeTV);
            int gifEnd = msg.getContent().length() - 4;
            String path = "emoticons/" + msg.getContent().substring(4, gifEnd);
            try {
                leftGifTextView.setImageDrawable(new GifDrawable(mContext
                        .getAssets(), path));
            } catch (IOException e) {
                e.printStackTrace();
            }
            ChatUtils.disPlayHisImg(mContext, msg, leftUserHeadIV, mToChat);
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

        public ItemLeftImage(View view) {
            leftContentIV = (ImageView) view.findViewById(R.id.iv_sendPicture);
            leftUserHeadIV = (ImageView) view.findViewById(R.id.iv_userhead);
            msgTimeTV = (TextView) view.findViewById(R.id.timestamp_tv);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            progressTV = (TextView) view.findViewById(R.id.percentage_tv);
        }

        @Override
        public void bandData(final IMMessage msg, int position) {
            setLongClickListener(msg, leftContentIV);
            showTime(position, msgTimeTV);
            ChatUtils.disPlayHisImg(mContext, msg, leftUserHeadIV, mToChat);
            ImageUtils.displayChatImage(msg.getContent(), leftContentIV, 10);
            leftContentIV.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    ArrayList<String> list = new ArrayList<String>();
                    list.add(ServerUtils.getChatServerIP(msg.getContent()));
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra("url", (ArrayList<String>) list);
                    intent.putExtra("viewPagerPosition", "0");
                    intent.setClass(mContext, VehicleQuanVewPager.class);
                    mContext.startActivity(intent);


                }
            });
        }
    }

    /**
     * 右边显示的图片
     */
    class ItemRightImage extends Holder {
        TextView msgTimeTV;
        ImageView rightContentIV, rightUserHeadIV;

        public ItemRightImage(View view) {
            rightContentIV = (ImageView) view.findViewById(R.id.iv_sendPicture);
            rightUserHeadIV = (ImageView) view.findViewById(R.id.iv_userhead);
            msgTimeTV = (TextView) view.findViewById(R.id.timestamp_tv);

        }

        @Override
        public void bandData(final IMMessage msg, int position) {
            setLongClickListener(msg, rightContentIV);
            showTime(position, msgTimeTV);
            ChatUtils.disPlayMeImg(mContext, rightUserHeadIV);
            MyLog.e("YUY", "发送图片的地址 = " + msg.getFileName());
            ImageUtils.displayChatImage(msg.getContent(), rightContentIV, 10);
            rightContentIV.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    ArrayList<String> list = new ArrayList<String>();
                    list.add(ServerUtils.getChatServerIP(msg.getContent()));
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra("url", (ArrayList<String>) list);
                    intent.putExtra("viewPagerPosition", "0");
                    intent.setClass(mContext, VehicleQuanVewPager.class);
                    mContext.startActivity(intent);

                }
            });

            setLongClickListener(msg, rightContentIV);
        }

    }

    /**
     * 左边语音显示
     */
    class ItemLeftVoice extends Holder {// 异步下载
        TextView msgTimeTV;
        ImageView leftUserHeadIV, leftVoiceIV;
        TextView leftTimeTV;
        ProgressBar mLeftBar;
        ImageView isReadIV;// 是否已读

        public ItemLeftVoice(View view) {
            isReadIV = (ImageView) view.findViewById(R.id.iv_unread_voice);
            leftTimeTV = (TextView) view.findViewById(R.id.tv_length);
            leftUserHeadIV = (ImageView) view.findViewById(R.id.iv_userhead);
            msgTimeTV = (TextView) view.findViewById(R.id.timestamp_tv);
            mLeftBar = (ProgressBar) view.findViewById(R.id.pb_sending);
            leftVoiceIV = (ImageView) view.findViewById(R.id.iv_voice);
        }

        @Override
        public void bandData(IMMessage msg, int position) {
            setLongClickListener(msg, leftVoiceIV);
            if (!TextUtils.isEmpty(msg.getFileName())) {
                isReadIV.setVisibility(View.GONE);
            } else {
                isReadIV.setVisibility(View.VISIBLE);
            }
            showTime(position, msgTimeTV);
            ChatUtils.disPlayHisImg(mContext, msg, leftUserHeadIV, mToChat);
            int start = msg.getContent().indexOf("{") + 1;
            int end = msg.getContent().indexOf("}");
            double dou = Double.valueOf(msg.getContent().substring(start, end));
            int time = (int) (dou);
            leftTimeTV.setText(time + "\"");
            int width = ScreenUtils.getScreenWidth(mContext) * 6 / 10;
            LayoutParams params = leftVoiceIV.getLayoutParams();
            if (time > 15) {
                params.width = width / 60 * time;
            } else {
                params.width = 200;
            }
            params.height = LayoutParams.WRAP_CONTENT;
            leftVoiceIV.setLayoutParams(params);
            if (msg.getMsgType() == 0) {
                leftVoiceIV.setImageResource(R.drawable.chatfrom_voice_playing);
            } else {
                leftVoiceIV.setImageResource(R.drawable.chatto_voice_playing);
            }
            leftVoiceIV.setOnClickListener(new VoicePlayClickListener(msg,
                    leftVoiceIV, isReadIV, MessageAdapter.this,
                    ((Activity) mContext), mLeftBar));
        }
    }

    /**
     * 右边语音显示
     */
    class ItemRightVoice extends Holder {// 阅读本地
        TextView msgTimeTV;
        ImageView rightUserHeadIV, rightVoiceIV;
        TextView rightTimeTV;

        public ItemRightVoice(View view) {
            rightTimeTV = (TextView) view.findViewById(R.id.tv_length);
            rightUserHeadIV = (ImageView) view.findViewById(R.id.iv_userhead);
            msgTimeTV = (TextView) view.findViewById(R.id.timestamp_tv);
            rightVoiceIV = (ImageView) view.findViewById(R.id.iv_voice);
        }

        @Override
        public void bandData(IMMessage msg, int position) {
            setLongClickListener(msg, rightVoiceIV);
            showTime(position, msgTimeTV);
            ChatUtils.disPlayMeImg(mContext, rightUserHeadIV);
            int start = msg.getContent().indexOf("{") + 1;
            int end = msg.getContent().indexOf("}");
            double dou = Double.valueOf(msg.getContent().substring(start, end));
            int time = (int) (dou);
            rightTimeTV.setText(time + "\"");
            int width = ScreenUtils.getScreenWidth(mContext) * 6 / 10;
            LayoutParams params = rightVoiceIV.getLayoutParams();
            if (time > 15) {
                params.width = width / 60 * time;
            } else {
                params.width = 200;
            }
            params.height = LayoutParams.WRAP_CONTENT;
            rightVoiceIV.setLayoutParams(params);
            if (msg.getMsgType() == 0) {
                rightVoiceIV
                        .setImageResource(R.drawable.chatfrom_voice_playing);
            } else {
                rightVoiceIV.setImageResource(R.drawable.chatto_voice_playing);
            }
            rightVoiceIV.setOnClickListener(new VoicePlayClickListener(msg,
                    rightVoiceIV, MessageAdapter.this, ((Activity) mContext)));

        }
    }

    /**
     * 左边显示的位置信息
     */
    class ItemLeftLocal extends Holder {
        TextView msgTimeTV;
        ImageView leftUserHeadIV;
        TextView leftLocalTV;// 位置信息

        public ItemLeftLocal(View view) {
            msgTimeTV = (TextView) view.findViewById(R.id.timestamp_tv);
            leftLocalTV = (TextView) view.findViewById(R.id.tv_location);
            leftUserHeadIV = (ImageView) view.findViewById(R.id.iv_userhead);
        }

        @Override
        public void bandData(final IMMessage msg, int position) {
            setLongClickListener(msg, leftLocalTV);
            showTime(position, msgTimeTV);
            ChatUtils.disPlayHisImg(mContext, msg, leftUserHeadIV, mToChat);
            Log.e("----", "左边显示的位置信息" + msg.getMsgType());
            ChatUtils.setLocation(msg.getContent(), leftLocalTV);
            leftLocalTV.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent(mContext, BaiduMapActivity.class);
                    intent.putExtra("latitude",
                            ChatUtils.getLocationLat(msg.getContent())[0]);
                    intent.putExtra("longitude",
                            ChatUtils.getLocationLat(msg.getContent())[1]);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    /**
     * 右边显示的位置信息
     */
    class ItemRightLocal extends Holder {
        TextView msgTimeTV;
        ImageView rightUserHeadIV;
        TextView rightLocalTV;// 位置信息

        public ItemRightLocal(View view) {
            msgTimeTV = (TextView) view.findViewById(R.id.timestamp_tv);
            rightLocalTV = (TextView) view.findViewById(R.id.tv_location);
            rightUserHeadIV = (ImageView) view.findViewById(R.id.iv_userhead);
        }

        @Override
        public void bandData(final IMMessage msg, int position) {
            setLongClickListener(msg, rightLocalTV);
            showTime(position, msgTimeTV);
            ChatUtils.disPlayMeImg(mContext, rightUserHeadIV);
            ChatUtils.setLocation(msg.getContent(), rightLocalTV);
            rightLocalTV.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent(mContext, BaiduMapActivity.class);
                    intent.putExtra("latitude",
                            ChatUtils.getLocationLat(msg.getContent())[0]);
                    intent.putExtra("longitude",
                            ChatUtils.getLocationLat(msg.getContent())[1]);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    abstract class Holder {
        public abstract void bandData(IMMessage msg, int position);
    }

    /**
     * 刷新页面
     */
    public void refresh() {
        if (handler.hasMessages(HANDLER_MESSAGE_REFRESH_LIST)) {
            return;
        }
        android.os.Message msg = handler
                .obtainMessage(HANDLER_MESSAGE_REFRESH_LIST);
        handler.sendMessage(msg);
    }

    /**
     * 刷新页面, 选择最后一个
     */
    public void refreshSelectLast() {
        handler.sendMessage(handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST));
        handler.sendMessage(handler.obtainMessage(HANDLER_MESSAGE_SELECT_LAST));
    }

    /**
     * 刷新页面, 选择Position
     */
    public void refreshSeekTo(int position) {
        handler.sendMessage(handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST));
        android.os.Message msg = handler.obtainMessage(HANDLER_MESSAGE_SEEK_TO);
        msg.arg1 = position;
        handler.sendMessage(msg);
    }

    private void showTime(int position, TextView timestamp) {
        // 两条消息时间离得如果稍长，显示时间
        if (position == 0) {
            timestamp.setVisibility(View.VISIBLE);
            timestamp.setText(mData.get(position).getTime().subSequence(5, 16));
            return;
        }
        IMMessage prevMessage = mData.get(position - 1);
        try {
            if (prevMessage != null
                    && ChatUtils.stringDaysBetween(prevMessage.getTime(), mData
                    .get(position).getTime())) {
                timestamp.setVisibility(View.GONE);
            } else {
                timestamp.setVisibility(View.VISIBLE);
                timestamp.setText(mData.get(position).getTime()
                        .subSequence(5, 16));

            }
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 转发消息
     *
     * @param msg  转发的消息
     * @param view 需要监听的view
     */
    private void setLongClickListener(final IMMessage msg, View view) {
        //对传进来的View进行长按监听
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showMsgDoDialog(msg);
                return false;
            }
        });


    }

    /**
     * 显示出对消息的操作
     * 不同的消息有不同的操作
     *
     * @param msg
     */
    private void showMsgDoDialog(final IMMessage msg) {
        final Dialog dialog = new Dialog(mContext, R.style.myDialogTheme);
        LinearLayout layout = (LinearLayout) mInflater.inflate(R.layout.msg_do_menu, null);
        dialog.setContentView(layout);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = ScreenUtils.getScreenWidth(mContext) * 2 / 3;//宽高可设置具体大小
        lp.height = LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        TextView copyTV = (TextView) layout.findViewById(R.id.copy_tv);
        TextView zhuanfaTV = (TextView) layout.findViewById(R.id.zhuanfa_tv);
        TextView deleteTV = (TextView) layout.findViewById(R.id.delete_tv);
        switch (msg.getType()) {
            case Constant.CHAT_MESSAGE_TYPE.LOCATION://不可复制，不可转发
                zhuanfaTV.setVisibility(View.GONE);
                copyTV.setVisibility(View.GONE);
                break;
            case Constant.CHAT_MESSAGE_TYPE.VOICE:
            case Constant.CHAT_MESSAGE_TYPE.GIF_IMG:
                zhuanfaTV.setVisibility(View.GONE);
                copyTV.setVisibility(View.GONE);
                break;
            case Constant.CHAT_MESSAGE_TYPE.IMAGE:
                copyTV.setVisibility(View.GONE);
                break;
        }
        dialog.show();
        copyTV.setOnClickListener(new OnClickListener() {//复制监听
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(msg.getContent());
                ToastUtil.toastShow(mContext, "已复制");
                dialog.dismiss();
            }
        });
        zhuanfaTV.setOnClickListener(new OnClickListener() {//转发
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageTransActivity.class);
                intent.putExtra("skipTo", Constant.SEND_MSG_BY_HISTORY);//标识消息转发
                intent.putExtra("msg", msg.getContent());//转发的消息
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
    }

    /**
     * 删除聊天消息
     */
    private void deleteMsg(final IMMessage msg) {

        final AlertDialog dialog1 = new AlertDialog(mContext).builder();
        dialog1.setTitle("温馨提示");
        dialog1.setMsg("删除后将不会出现在您的聊天记录中");
        dialog1.setPositiveButton("确定", new OnClickListener() {
            @Override
            public void onClick(View v) {
                //操作数据库删除记录
                try {
                    MyLog.e("YUY","--------删除的msgId = "+msg.get_id());
                    MessageManager.getInstance(mContext).delItemChatHisWithSb(mToChat, msg.get_id());
                    mData = MessageManager.getInstance(mContext)
                            .getMessageListByFrom(mToChat, 1, 20);
                    setMessage(mData);
                    ToastUtil.toastShow(mContext, "删除成功");

                } catch (Exception e) {
                    ToastUtil.toastShow(mContext, "删除消息数据异常");
                }

            }
        });
        dialog1.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        dialog1.show();

    }

}
