package com.chewuwuyou.eim.service;

import java.util.Calendar;
import java.util.HashMap;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Packet;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;

import com.chewuwuyou.app.AppContext;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.eim.activity.notice.MyNoticeActivity;
import com.chewuwuyou.eim.comm.Constant;
import com.chewuwuyou.eim.manager.NoticeManager;
import com.chewuwuyou.eim.manager.XmppConnectionManager;
import com.chewuwuyou.eim.model.Notice;
import com.chewuwuyou.eim.util.DateUtil;

/**
 * 
 * 系统消息服务.
 * 
 * @author sxk
 */
public class IMSystemMsgService extends BaseService {
	private Context context;
	PacketCollector myCollector = null;
	/* 声明对象变量 */
	private NotificationManager myNotiManager;

	SoundPool sp; // 声明SoundPool的引用
	HashMap<Integer, Integer> hm; // 声明一个HashMap来存放声音文件
	int currStreamId;// 当前正播放的streamId

	@Override
	public void onCreate() {
		MyLog.e("SXKTEST", "IMSystemMsgService create!!!!!!!!!!!!!!!!!!!!");
		context = this;
//		ACTION_USE = Constant.SYSTEM_MSG_STARTED;
		super.onCreate();
//		initSysTemMsgManager();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
//		Intent intentL = new Intent(ACTION_USE);
//		context.sendBroadcast(intentL);
		MyLog.e("SXKTEST", "IMSystemMsgService Command Started!!!!!!!!!!!!!!!!!!!!");
		initSysTemMsgManager();
		int returnInt = super.onStartCommand(intent, flags, startId);
		return returnInt;
	}

	//此方法是为了可以在Acitity中获得服务的实例   
	public class ServiceBinder extends Binder {
	        public IMSystemMsgService getService() {
	            return IMSystemMsgService.this;
	        }
	}

	@Override
	public IBinder onBind(Intent intent) {
		MyLog.e("SXKTEST", "IMSystemMsgService onBind!!!!!!!!!!!!!!!!!!!!");
		initSysTemMsgManager();
//		AppContext.getInstance().doSystemMsgStarted();
		return new ServiceBinder();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		MyLog.e("SXKTEST", "IMSystemMsgService onUnbind&&&&&&&&&&&&&&&&&&&&&&&&&");
		XMPPConnection con = XmppConnectionManager.getInstance()
				.getConnection();
		con.removePacketListener(pListener);
		return super.onUnbind(intent);
	}
	
	@Override
	public void onDestroy() {
		XmppConnectionManager.getInstance().getConnection()
				.removePacketListener(pListener);
		super.onDestroy();
	}

	private void initSysTemMsgManager() {
		myNotiManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		initSoundPool();
		XMPPConnection con = XmppConnectionManager.getInstance()
				.getConnection();
		con.addPacketListener(pListener, new MessageTypeFilter(
				Type.normal));
	}

	// 来消息监听
	PacketListener pListener = new PacketListener() {

		@Override
		public void processPacket(Packet packetz) {
			MyLog.i("YUY","收到加好友請求----------------IMSystemService");
			Message message = (Message) packetz;
			MyLog.e("YUY", "系统消息  = "+message.getBody());
			if (message.getType() == Type.normal&&!TextUtils.isEmpty(message.getBody())) {

				NoticeManager noticeManager = NoticeManager
						.getInstance(context);
				Notice notice = new Notice();
				// playSound(1, 0); //播放音效

				notice.setTitle("系统消息");
				notice.setNoticeType(Notice.SYS_MSG);
				notice.setFrom(packetz.getFrom());
				notice.setContent(message.getBody());
				notice.setNoticeTime(DateUtil.date2Str(Calendar.getInstance(),
						Constant.MS_FORMART));
				notice.setFrom(packetz.getFrom());
				notice.setTo(packetz.getTo());
				notice.setStatus(Notice.UNREAD);

				long noticeId = noticeManager.saveNotice(notice);
				if (noticeId != -1) {
					Intent intent = new Intent();
					intent.setAction(Constant.ACTION_SYS_MSG);
					notice.setId(String.valueOf(noticeId));
					intent.putExtra("notice", notice);
					sendBroadcast(intent);
					setNotiType(R.drawable.icon_about, Constant.SYS_MSG_DIS,
							message.getBody(), MyNoticeActivity.class);

				}
			}
		}
	};

	// 初始化声音池的方法
	@SuppressWarnings("deprecation")
	public void initSoundPool() {
		sp = new SoundPool(4, AudioManager.STREAM_MUSIC, 0); // 创建SoundPool对象
		hm = new HashMap<Integer, Integer>(); // 创建HashMap对象
		// hm.put(1, sp.load(this, R.raw.musictest, 1)); //
		// 加载声音文件musictest并且设置为1号声音放入hm中
	}

	// 播放声音的方法
	public void playSound(int sound, int loop) { // 获取AudioManager引用
		AudioManager am = (AudioManager) this
				.getSystemService(Context.AUDIO_SERVICE);
		// 获取当前音量
		float streamVolumeCurrent = am
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		// 获取系统最大音量
		float streamVolumeMax = am
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		// 计算得到播放音量
		float volume = streamVolumeCurrent / streamVolumeMax;
		// 调用SoundPool的play方法来播放声音文件
		currStreamId = sp.play(hm.get(sound), volume, volume, 1, loop, 1.0f);
	}

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
			String contentText, Class activity) {
		/*
		 * 创建新的Intent，作为点击Notification留言条时， 会运行的Activity
		 */
		Intent notifyIntent = new Intent(this, activity);
		notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		/* 创建PendingIntent作为设置递延运行的Activity */
		PendingIntent appIntent = PendingIntent.getActivity(this, 0,
				notifyIntent, 0);

		/* 创建Notication，并设置相关参数 */
		Notification myNoti = new Notification();
		/* 设置statusbar显示的icon */
		myNoti.icon = iconId;
		/* 设置statusbar显示的文字信息 */
		myNoti.tickerText = contentTitle;
		/* 设置notification发生时同时发出默认声音 */
		AudioManager volMgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
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

			if (CacheTools.getUserData("isSound") == null || CacheTools.getUserData("isSound").equals("1")) {
				if (CacheTools.getUserData("isShake") == null || CacheTools.getUserData("isShake").equals("1")) {
					// 11模式
					myNoti.defaults = Notification.DEFAULT_SOUND;
					myNoti.defaults |= Notification.DEFAULT_VIBRATE;
				} else {
					// 10模式
					myNoti.defaults = Notification.DEFAULT_SOUND;
					myNoti.vibrate = null;
				}
			} else {
				if (CacheTools.getUserData("isShake") == null || CacheTools.getUserData("isShake").equals("1")) {
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
		myNotiManager.notify(0, myNoti);
	}

}
