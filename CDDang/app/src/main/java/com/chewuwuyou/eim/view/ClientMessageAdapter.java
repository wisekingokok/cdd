package com.chewuwuyou.eim.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.ui.VehicleQuanVewPager;
import com.chewuwuyou.app.utils.ChatInputUtils;
import com.chewuwuyou.app.utils.ChatUtils;
import com.chewuwuyou.app.utils.Constant;
import com.chewuwuyou.app.utils.ImageUtils;
import com.chewuwuyou.app.utils.JsonUtil;
import com.chewuwuyou.app.utils.ScreenUtils;
import com.chewuwuyou.app.utils.ServerUtils;
import com.chewuwuyou.eim.activity.im.BaiduMapActivity;
import com.chewuwuyou.eim.activity.im.ChatActivity;
import com.chewuwuyou.eim.activity.im.ClientManagerChatActivity;
import com.chewuwuyou.eim.model.IMMessage;

/**
 * 聊天消息适配器
 * 
 * @author yuyong
 * 
 */
@SuppressLint("HandlerLeak")
public class ClientMessageAdapter extends BaseAdapter {
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
//	private String mToChat;
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
	public ClientMessageAdapter(Context context, List<IMMessage> data) {
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		this.mData = data;
		this.mMessageList = ((ClientManagerChatActivity) mContext).getListView();
		this.mFaceCharacterMap = JsonUtil.getFaceStrMap(context);
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

	// private View createViewByMessage(IMMessage message, int position) {
	// switch (message.getType()) {
	// case Constant.CHAT_MESSAGE_TYPE.TXT:
	// case Constant.CHAT_MESSAGE_TYPE.YWZ_TXT:
	// return message.getMsgType() == 0 ? mInflater.inflate(
	// R.layout.row_received_message, null) : mInflater.inflate(
	// R.layout.row_sent_message, null);
	// case Constant.CHAT_MESSAGE_TYPE.GIF_IMG:
	// return message.getType() == 0 ? mInflater.inflate(
	// R.layout.row_received_gif, null) : mInflater.inflate(
	// R.layout.row_sent_gif, null);
	// case Constant.CHAT_MESSAGE_TYPE.VOICE:
	// return message.getMsgType() == 0 ? mInflater.inflate(
	// R.layout.row_received_voice, null) : mInflater.inflate(
	// R.layout.row_sent_voice, null);
	// case Constant.CHAT_MESSAGE_TYPE.IMAGE:
	// return message.getMsgType() == 0 ? mInflater.inflate(
	// R.layout.row_received_picture, null) : mInflater.inflate(
	// R.layout.row_sent_picture, null);
	//
	// case Constant.CHAT_MESSAGE_TYPE.LOCATION:
	// return message.getMsgType() == 0 ? mInflater.inflate(
	// R.layout.row_received_location, null) : mInflater.inflate(
	// R.layout.row_sent_location, null);
	// default:
	// return message.getMsgType() == 0 ? mInflater.inflate(
	// R.layout.row_received_message, null) : mInflater.inflate(
	// R.layout.row_sent_message, null);
	//
	// }
	// }

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
	 * 
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
			showTime(position, msgTimeTV);
			int gifEnd = msg.getContent().length() - 4;
			String path = "emoticons/" + msg.getContent().substring(4, gifEnd);
			try {
				leftGifTextView.setImageDrawable(new GifDrawable(mContext
						.getAssets(), path));
			} catch (IOException e) {
				e.printStackTrace();
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

		public ItemLeftImage(View view) {
			leftContentIV = (ImageView) view.findViewById(R.id.iv_sendPicture);
			leftUserHeadIV = (ImageView) view.findViewById(R.id.iv_userhead);
			msgTimeTV = (TextView) view.findViewById(R.id.timestamp_tv);
			progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
			progressTV = (TextView) view.findViewById(R.id.percentage_tv);
		}

		@Override
		public void bandData(final IMMessage msg, int position) {
			showTime(position, msgTimeTV);
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
			showTime(position, msgTimeTV);
			ChatUtils.disPlayMeImg(mContext, rightUserHeadIV);

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
			if(!TextUtils.isEmpty(msg.getFileName())){
				isReadIV.setVisibility(View.GONE);
			}else{
				isReadIV.setVisibility(View.VISIBLE);
			}
			showTime(position, msgTimeTV);
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
					leftVoiceIV, isReadIV, ClientMessageAdapter.this,
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
					rightVoiceIV, ClientMessageAdapter.this, ((Activity) mContext)));

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
			showTime(position, msgTimeTV);
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

}
