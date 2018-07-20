package com.chewuwuyou.eim.service;

import java.util.Calendar;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;

import com.chewuwuyou.app.AppContext;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.ChatUtils;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.app.utils.Tools;
import com.chewuwuyou.eim.activity.im.ChatActivity;
import com.chewuwuyou.eim.comm.Constant;
import com.chewuwuyou.eim.manager.MessageManager;
import com.chewuwuyou.eim.manager.NoticeManager;
import com.chewuwuyou.eim.manager.XmppConnectionManager;
import com.chewuwuyou.eim.model.IMMessage;
import com.chewuwuyou.eim.model.Notice;
import com.chewuwuyou.eim.util.DateUtil;

/**
 *
 * 聊天服务.
 *
 * @author sxk
 */
public class IMChatService extends BaseService {
	private Context context;
	private NotificationManager notificationManager;

	// private static final String PATH = "/sdcard/MyVoiceForder/Record/";
	// 路径
	// private static String SDPATH = "";

	// public void getPath() {
	// // 得到当前外部存储设备的目录()
	// SDPATH = Environment.getExternalStorageDirectory() + "/";
	//
	// File sdDir = null;
	// boolean sdCardExist = Environment.getExternalStorageState().equals(
	// android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
	// if (sdCardExist) {
	// sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
	// }
	//
	// SDPATH = sdDir.getAbsolutePath() + "/Android/data/"; // 一定要写手机存在的文件夹
	// }

	@Override
	public void onCreate() {
		MyLog.e("SXKTEST", "ChatService create!!!!!!!!!!!!!!!!!!!!");
		context = this;
		// ACTION_USE = Constant.CHAT_STARTED;
		// initChatManager();
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// Intent intentL = new Intent(ACTION_USE);
		// context.sendBroadcast(intentL);
		MyLog.e("SXKTEST", "ChatService Command Started!!!!!!!!!!!!!!!!!!!!");
		initChatManager();
		int returnInt = super.onStartCommand(intent, flags, startId);
		return returnInt;
	}

	// 此方法是为了可以在Acitity中获得服务的实例
	public class ServiceBinder extends Binder {
		public IMChatService getService() {
			return IMChatService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		System.out.println("onBind.....");
		MyLog.e("SXKTEST", "ChatService onBind!!!!!!!!!!!!!!!!!!!!");
		initChatManager();
		// AppContext.getInstance().doChatStarted();
		return new ServiceBinder();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		MyLog.e("SXKTEST", "ChatService onUnbind&&&&&&&&&&&&&&&&&&&&&&&&&");
		XMPPConnection conn = XmppConnectionManager.getInstance()
				.getConnection();
		conn.removePacketListener(pListener);
		return super.onUnbind(intent);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void initChatManager() {
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		XMPPConnection conn = XmppConnectionManager.getInstance()
				.getConnection();
		conn.addPacketListener(pListener, new MessageTypeFilter(
				Message.Type.chat));
	}

	PacketListener pListener = new PacketListener() {

		@Override
		public void processPacket(Packet arg0) {
			final Message message = (Message) arg0;
			if (!TextUtils.isEmpty(message.getBody())) {
				final IMMessage msg = new IMMessage();
				// String time = (String)
				// message.getProperty(IMMessage.KEY_TIME);
				String time = DateUtil.date2Str(Calendar.getInstance(),
						Constant.MS_FORMART);
				msg.setTime(time);
				// if (Message.Type.error == message.getType()) {
				// msg.setType(IMMessage.ERROR);
				// } else {
				// msg.setType(IMMessage.SUCCESS);
				// }
				String from = message.getFrom().split("/")[0];//3857@iz232jtyxeoz/Smack
				msg.setFromSubJid(from);
				msg.setContent(message.getBody());
				msg.setTime(time);
				msg.setMsgType(0);//收到的消息
				msg.setType(ChatUtils.getChatMessageType(msg.getContent()));
				msg.setMsgTo(CacheTools.getUserData("userId"));
				msg.setFromRoom(msg.getFromRoom());
				MessageManager.getInstance(context).saveIMMessage(msg);
				MyLog.i("YUY", "收到的消息   = " + message.getBody());

				// 生成通知
				NoticeManager noticeManager = NoticeManager
						.getInstance(context);
				Notice notice = new Notice();
				notice.setTitle("会话信息");
				notice.setNoticeType(Notice.CHAT_MSG);
				notice.setContent(message.getBody());
				notice.setFrom(from);
				notice.setNoticeTime(time);
				if(Tools.getNowClass(getApplicationContext()).contains("ChatActivity")){//如果当前正在聊天页面 消息就设为已读
					notice.setStatus(Notice.READ);
				}else{
					notice.setStatus(Notice.UNREAD);
				}
				long noticeId = -1;
				noticeId = noticeManager.saveNotice(notice);
				Intent intent = new Intent(Constant.NEW_MESSAGE_ACTION);
				intent.putExtra(IMMessage.IMMESSAGE_KEY, msg);
				Bundle bundle=new Bundle();
				bundle.putSerializable("notice", notice);
				sendBroadcast(intent);
				if (noticeId != -1&&!Tools.getNowClass(getApplicationContext()).contains("ChatActivity")) {//判断当前在聊天时不进行消息提示
//					setNotiType(R.drawable.notification_ic_luncher,
//							getResources().getString(R.string.new_message),
//							ChatUtils.returnMsg(notice.getContent()), ChatActivity.class, from);
				}

			}

		}

	};

	/**
	 *
	 * 发出Notification的method.
	 *
	 * @param iconId
	 *            图标
	 * @param contentTitle
	 *            标题
	 * @param contentText
	 *            你内容
	 * @param activity
	 * @author sxk
	 * @update 2012-5-14 下午12:01:55
	 */
	@SuppressWarnings({ "rawtypes", "deprecation" })
	private void setNotiType(int iconId, String contentTitle,
							 String contentText, Class activity, String from) {

		/*
		 * 创建新的Intent，作为点击Notification留言条时， 会运行的Activity
		 */
		Intent notifyIntent = new Intent(this, activity);
		MyLog.e("YUY","聊天推送--------------------"+from);
		notifyIntent.putExtra("to", from);
		// notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		notifyIntent.putExtra("title","");
		/* 创建PendingIntent作为设置递延运行的Activity */
		PendingIntent appIntent = PendingIntent.getActivity(this, 0,
				notifyIntent, 0);

		/* 创建Notication，并设置相关参数 */
		Notification myNoti = new Notification();
		// 点击自动消失
		myNoti.flags = Notification.FLAG_AUTO_CANCEL;
		/* 设置statusbar显示的icon */
		myNoti.icon = iconId;
		/* 设置statusbar显示的文字信息 */
		myNoti.tickerText = contentTitle;

		AudioManager volMgr = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		switch (volMgr.getRingerMode()) {// 获取系统设置的铃声模式
			case AudioManager.RINGER_MODE_SILENT:// 静音模式，值为0，这时候不震动，不响铃
				myNoti.sound = null;
				myNoti.vibrate = null;
				break;
			case AudioManager.RINGER_MODE_VIBRATE:// 震动模式，值为1，这时候震动，不响铃
				myNoti.sound = null;
				myNoti.defaults |= Notification.DEFAULT_VIBRATE;
				break;

			case AudioManager.RINGER_MODE_NORMAL:// 常规模式，值为2，分两种情况：1_响铃但不震动，2_响铃+震动

				if (CacheTools.getUserData("isSound") == null
						|| CacheTools.getUserData("isSound").equals("1")) {
					if (CacheTools.getUserData("isShake") == null
							|| CacheTools.getUserData("isShake").equals("1")) {
						// 11模式
						myNoti.defaults = Notification.DEFAULT_SOUND;
						myNoti.defaults |= Notification.DEFAULT_VIBRATE;
					} else {
						// 10模式
						myNoti.defaults = Notification.DEFAULT_SOUND;
						myNoti.vibrate = null;
					}
				} else {
					if (CacheTools.getUserData("isShake") == null
							|| CacheTools.getUserData("isShake").equals("1")) {
						// 01模式
						myNoti.sound = null;
						myNoti.defaults |= Notification.DEFAULT_VIBRATE;
					} else {
						// 00模式
						myNoti.sound = null;
						myNoti.vibrate = null;
					}
				}
				// 获取软件的设置
				break;
			default:
				myNoti.defaults = Notification.DEFAULT_SOUND;
				myNoti.defaults |= Notification.DEFAULT_VIBRATE;
				break;

		}

		/* 设置Notification留言条的参数 */
//		myNoti.setLatestEventInfo(this, contentTitle, contentText, appIntent);
		/* 送出Notification */
		notificationManager.notify(0, myNoti);
	}

}
