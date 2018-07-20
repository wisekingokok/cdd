package com.chewuwuyou.eim.activity;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.chewuwuyou.app.AppContext;
import com.chewuwuyou.app.R;
import com.chewuwuyou.app.ui.BaseActivity;
import com.chewuwuyou.app.utils.CacheTools;
import com.chewuwuyou.app.utils.NetworkUtil;
import com.chewuwuyou.eim.comm.Constant;
import com.chewuwuyou.eim.model.LoginConfig;

/**
 * Actity 工具支持类
 * 
 * @author sxk
 * 
 */
public class ActivitySupport extends BaseActivity implements IActivitySupport {

	protected static Context context = null;
	protected SharedPreferences preferences;
	protected AppContext eimApplication;
	protected ProgressDialog pg = null;
	protected NotificationManager notificationManager;
	protected LoginConfig loginConfig;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		preferences = getSharedPreferences(Constant.LOGIN_SET, 0);
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		pg = new ProgressDialog(context);
		eimApplication = (AppContext) getApplication();
		eimApplication.addActivity(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public ProgressDialog getProgressDialog() {
		return pg;
	}

	@Override
	public void startService() {
		// // 好友联系人服务
		// Intent server = new Intent(context, IMContactService.class);
		// context.startService(server);
		// // 聊天服务
		// Intent chatServer = new Intent(context, IMChatService.class);
		// context.startService(chatServer);
		// // 自动恢复连接服务
		// Intent reConnectService = new Intent(context,
		// ReConnectService.class);
		// context.startService(reConnectService);
		// // 系统消息连接服务
		// Intent imSystemMsgService = new Intent(context,
		// IMSystemMsgService.class);
		// context.startService(imSystemMsgService);
	}

	public static void startServiceStatic(Context contextL) {
		// // 好友联系人服务
		// Intent server = new Intent(contextL, IMContactService.class);
		// contextL.startService(server);
		// // 聊天服务
		// Intent chatServer = new Intent(contextL, IMChatService.class);
		// contextL.startService(chatServer);
		// // 自动恢复连接服务
		// Intent reConnectService = new Intent(contextL,
		// ReConnectService.class);
		// contextL.startService(reConnectService);
		// // 系统消息连接服务
		// Intent imSystemMsgService = new Intent(contextL,
		// IMSystemMsgService.class);
		// contextL.startService(imSystemMsgService);
	}

	/**
	 * 
	 * 销毁服务.
	 * 
	 * @author sxk
	 * @update 2012-5-16 下午12:16:08
	 */
	@Override
	public void stopService() {
		// // 好友联系人服务
		// Intent server = new Intent(context, IMContactService.class);
		// context.stopService(server);
		// // 聊天服务
		// Intent chatServer = new Intent(context, IMChatService.class);
		// context.stopService(chatServer);
		//
		// // 自动恢复连接服务
		// Intent reConnectService = new Intent(context,
		// ReConnectService.class);
		// context.stopService(reConnectService);
		//
		// // 系统消息连接服务
		// Intent imSystemMsgService = new Intent(context,
		// IMSystemMsgService.class);
		// context.stopService(imSystemMsgService);
	}

	@Override
	public void isExit() {
		new AlertDialog.Builder(context).setTitle("确定退出吗?")
				.setNeutralButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						stopService();
						eimApplication.exit();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).show();
	}

	@Override
	public boolean hasInternetConnected() {
		@SuppressWarnings("static-access")
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(context.CONNECTIVITY_SERVICE);
		if (manager != null) {
			NetworkInfo network = manager.getActiveNetworkInfo();
			if (network != null && network.isConnectedOrConnecting()) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("static-access")
	@Override
	public boolean validateInternet() {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(context.CONNECTIVITY_SERVICE);
		if (manager == null) {
			openWirelessSet();
			return false;
		} else {
			NetworkInfo[] info = manager.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		openWirelessSet();
		return false;
	}

	@SuppressWarnings("static-access")
	@Override
	public boolean hasLocationGPS() {
		LocationManager manager = (LocationManager) context
				.getSystemService(context.LOCATION_SERVICE);
		if (manager
				.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("static-access")
	@Override
	public boolean hasLocationNetWork() {
		LocationManager manager = (LocationManager) context
				.getSystemService(context.LOCATION_SERVICE);
		if (manager
				.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void checkMemoryCard() {
		if (!Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			new AlertDialog.Builder(context)
					.setTitle(R.string.prompt)
					.setMessage("请检查内存卡")
					.setPositiveButton(R.string.menu_settings,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
									Intent intent = new Intent(
											Settings.ACTION_SETTINGS);
									context.startActivity(intent);
								}
							})
					.setNegativeButton("退出",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
									eimApplication.exit();
								}
							}).create().show();
		}
	}

	public void openWirelessSet() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
		dialogBuilder
				.setTitle(R.string.prompt)
				.setMessage(context.getString(R.string.check_connection))
				.setPositiveButton(R.string.menu_settings,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
								Intent intent = new Intent(
										Settings.ACTION_WIRELESS_SETTINGS);
								context.startActivity(intent);
							}
						})
				.setNegativeButton(R.string.close,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.cancel();
							}
						});
		dialogBuilder.show();
	}

	/**
	 * 
	 * 显示toast
	 * 
	 * @param text
	 * @param longint
	 * @author sxk
	 * @update 2012-6-28 下午3:46:18
	 */
	public void showToast(String text, int longint) {
		Toast.makeText(context, text, longint).show();
	}

	@Override
	public void showToast(String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 
	 * 关闭键盘事件
	 * 
	 * @author sxk
	 * @update 2012-7-4 下午2:34:34
	 */
	public void closeInput() {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (inputMethodManager != null && this.getCurrentFocus() != null) {
			inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus()
					.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
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
	public void setNotiType(int iconId, String contentTitle,
			String contentText, Class activity, String from) {
		/*
		 * 创建新的Intent，作为点击Notification留言条时， 会运行的Activity
		 */
		Intent notifyIntent = new Intent(this, activity);
		notifyIntent.putExtra("to", from);
		// notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

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
		/* 设置notification发生时同时发出默认声音 */
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

	@Override
	public Context getContext() {
		return context;
	}

	@Override
	public SharedPreferences getLoginUserSharedPre() {
		return preferences;
	}

	@Override
	public void saveLoginConfig(LoginConfig loginConfig) {
		preferences.edit()
				.putString(Constant.XMPP_HOST, loginConfig.getXmppHost())
				.commit();
		preferences.edit()
				.putInt(Constant.XMPP_PORT, loginConfig.getXmppPort()).commit();
		preferences
				.edit()
				.putString(Constant.XMPP_SEIVICE_NAME,
						loginConfig.getXmppServiceName()).commit();
		preferences.edit()
				.putString(Constant.USERNAME, loginConfig.getUsername())
				.commit();
		preferences.edit()
				.putString(Constant.PASSWORD, loginConfig.getPassword())
				.commit();
		preferences.edit()
				.putBoolean(Constant.IS_AUTOLOGIN, loginConfig.isAutoLogin())
				.commit();
		preferences.edit()
				.putBoolean(Constant.IS_NOVISIBLE, loginConfig.isNovisible())
				.commit();
		preferences.edit()
				.putBoolean(Constant.IS_REMEMBER, loginConfig.isRemember())
				.commit();
		preferences.edit()
				.putBoolean(Constant.IS_ONLINE, loginConfig.isOnline())
				.commit();
		preferences.edit()
				.putBoolean(Constant.IS_FIRSTSTART, loginConfig.isFirstStart())
				.commit();
	}

	@Override
	public LoginConfig getLoginConfig() {
		LoginConfig loginConfig = new LoginConfig();
		// String a = preferences.getString(Constant.XMPP_HOST, null);
		// String b = getResources().getString(R.string.xmpp_host);
		// loginConfig.setXmppHost(preferences.getString(Constant.XMPP_HOST,
		// getResources().getString(R.string.xmpp_host)));
		// loginConfig.setXmppPort(preferences.getInt(Constant.XMPP_PORT,
		// getResources().getInteger(R.integer.xmpp_port)));
		// loginConfig.setUsername(preferences.getString(Constant.USERNAME,
		// null));
		// loginConfig.setPassword(preferences.getString(Constant.PASSWORD,
		// null));
		// loginConfig.setXmppServiceName(preferences.getString(
		// Constant.XMPP_SEIVICE_NAME,
		// getResources().getString(R.string.xmpp_service_name)));
		// loginConfig.setAutoLogin(preferences.getBoolean(Constant.IS_AUTOLOGIN,
		// getResources().getBoolean(R.bool.is_autologin)));
		// loginConfig.setNovisible(preferences.getBoolean(Constant.IS_NOVISIBLE,
		// getResources().getBoolean(R.bool.is_novisible)));
		// loginConfig.setRemember(preferences.getBoolean(Constant.IS_REMEMBER,
		// getResources().getBoolean(R.bool.is_remember)));
		// loginConfig.setFirstStart(preferences.getBoolean(
		// Constant.IS_FIRSTSTART, true));

		loginConfig.setXmppHost(NetworkUtil.XMPP_HOST_URL);
		 loginConfig.setXmppPort(getResources().getInteger(R.integer.xmpp_port));
		loginConfig.setXmppServiceName(getResources().getString(
				R.string.xmpp_service_name));

		return loginConfig;
	}

	public static LoginConfig getLoginConfigStatic(Context context) {
		LoginConfig loginConfig = new LoginConfig();

		loginConfig.setXmppHost(NetworkUtil.XMPP_HOST_URL);
		loginConfig.setXmppPort(context.getResources().getInteger(
				R.integer.xmpp_port));
		loginConfig.setXmppServiceName(context.getResources().getString(
				R.string.xmpp_service_name));

		return loginConfig;
	}

	@Override
	public boolean getUserOnlineState() {
		// preferences = getSharedPreferences(Constant.LOGIN_SET,0);
		return preferences.getBoolean(Constant.IS_ONLINE, true);
	}

	@Override
	public void setUserOnlineState(boolean isOnline) {
		// preferences = getSharedPreferences(Constant.LOGIN_SET,0);
		preferences.edit().putBoolean(Constant.IS_ONLINE, isOnline).commit();

	}

	@Override
	public AppContext getEimApplication() {
		return eimApplication;
	}

	@Override
	public void statDoLogin() {
	}

	public static void saveLoginConfigStatic(Context context,
			LoginConfig loginConfig2) {

		SharedPreferences preferences = context.getSharedPreferences(
				Constant.LOGIN_SET, 0);
		preferences.edit()
				.putString(Constant.XMPP_HOST, loginConfig2.getXmppHost())
				.commit();
		preferences.edit()
				.putInt(Constant.XMPP_PORT, loginConfig2.getXmppPort())
				.commit();
		preferences
				.edit()
				.putString(Constant.XMPP_SEIVICE_NAME,
						loginConfig2.getXmppServiceName()).commit();
		preferences.edit()
				.putString(Constant.USERNAME, loginConfig2.getUsername())
				.commit();
		preferences.edit()
				.putString(Constant.PASSWORD, loginConfig2.getPassword())
				.commit();
		preferences.edit()
				.putBoolean(Constant.IS_AUTOLOGIN, loginConfig2.isAutoLogin())
				.commit();
		preferences.edit()
				.putBoolean(Constant.IS_NOVISIBLE, loginConfig2.isNovisible())
				.commit();
		preferences.edit()
				.putBoolean(Constant.IS_REMEMBER, loginConfig2.isRemember())
				.commit();
		preferences.edit()
				.putBoolean(Constant.IS_ONLINE, loginConfig2.isOnline())
				.commit();
		preferences
				.edit()
				.putBoolean(Constant.IS_FIRSTSTART, loginConfig2.isFirstStart())
				.commit();
	}
}
