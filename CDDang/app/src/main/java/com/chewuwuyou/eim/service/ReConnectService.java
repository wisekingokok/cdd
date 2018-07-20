package com.chewuwuyou.eim.service;

import org.jivesoftware.smack.XMPPConnection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import com.chewuwuyou.app.AppContext;
import com.chewuwuyou.app.utils.MyLog;
import com.chewuwuyou.eim.comm.Constant;
import com.chewuwuyou.eim.manager.XmppConnectionManager;

/**
 * 
 * 重连接服务.
 * 
 * @author sxk
 */
public class ReConnectService extends BaseService {
	// private Context context;
	private ConnectivityManager connectivityManager;
	private NetworkInfo info;

	@Override
	public void onCreate() {
		MyLog.e("SXKTEST", "ReConnectService create!!!!!!!!!!!!!!!!!!!!");
		// context = this;
		// ACTION_USE = Constant.RE_CONNECT_STARTED;
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(reConnectionBroadcastReceiver, mFilter);
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// Intent intentL = new Intent(ACTION_USE);
		// context.sendBroadcast(intentL);
		MyLog.e("SXKTEST",
				"ReConnectService Command Started!!!!!!!!!!!!!!!!!!!!");
		int returnInt = super.onStartCommand(intent, flags, startId);
		return returnInt;
	}

	// 此方法是为了可以在Acitity中获得服务的实例
	public class ServiceBinder extends Binder {
		public ReConnectService getService() {
			return ReConnectService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		MyLog.e("SXKTEST", "ReConnectService onBind!!!!!!!!!!!!!!!!!!!!");
		// AppContext.getInstance().doReConnectStarted();
		return new ServiceBinder();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		MyLog.e("SXKTEST", "ReConnectService onUnbind&&&&&&&&&&&&&&&&&&&&&&&&&");
		unregisterReceiver(reConnectionBroadcastReceiver);
		reConnectionBroadcastReceiver = null;
		return super.onUnbind(intent);
	}

	@Override
	public void onDestroy() {
		// unregisterReceiver(reConnectionBroadcastReceiver);
		super.onDestroy();
	}

	BroadcastReceiver reConnectionBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)

			// Ping 心跳包发现无法ping通服务器，也就是已经失联
					|| action.equals(Constant.ACTION_PING_OUT)) {
				MyLog.i("mark", "网络状态已经改变:" + action);
				connectivityManager = (ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				XMPPConnection connection = XmppConnectionManager.getInstance()
						.getConnection();
				info = connectivityManager.getActiveNetworkInfo();
				if (info != null && info.isAvailable()) {
					if (!connection.isConnected()) {
						MyLog.e("SXKTEST",
								"没有连接，要重连啊" + connection.isConnected()
										+ ";aut:"
										+ connection.isAuthenticated());
						reConnect(connection);
					} else {
						sendInentAndPre(Constant.RECONNECT_STATE_SUCCESS);
						// Toast.makeText(context, "用户已上线!", Toast.LENGTH_LONG)
						// .show();
					}
				} else {
					sendInentAndPre(Constant.RECONNECT_STATE_FAIL);
					MyLog.i("YUY","网络断开，用户已离线");
//					Toast.makeText(context, "网络断开,用户已离线!", Toast.LENGTH_LONG)
//							.show();
				}
			}

		}

	};

	/**
	 * 
	 * 递归重连，直连上为止.
	 * 
	 * @author sxk
	 * @update 2012-7-10 下午2:12:25
	 */
	public void reConnect(XMPPConnection connection) {
		// try {
		// MyLog.e("SXKTEST", "开始重连");
		// connection.disconnect()
		// connection.connect();
		// connection.login(getSharedPreferences(Constant.LOGIN_SET,
		// 0).getString(Constant.USERNAME, null),
		// getSharedPreferences(Constant.LOGIN_SET,
		// 0).getString(Constant.PASSWORD, null));
		// if (connection.isConnected()) {
		// Presence presence = new Presence(Presence.Type.available);
		// connection.sendPacket(presence);
		// Toast.makeText(context, "用户已上线!", Toast.LENGTH_LONG).show();
		// }
		// } catch (XMPPException e) {
		// Log.e("ERROR", "XMPP连接失败!", e);
		// reConnect(connection);
		// } catch (SmackException e) {
		// if (e instanceof AlreadyLoggedInException
		// || e instanceof FeatureNotSupportedException) {
		//
		// e.printStackTrace();
		// } else {
		//
		// e.printStackTrace();
		// reConnect(connection);
		// }
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
	}

	private void sendInentAndPre(boolean isSuccess) {
		Intent intent = new Intent();
		SharedPreferences preference = getSharedPreferences(Constant.LOGIN_SET,
				0);
		// 保存在线连接信息
		preference.edit().putBoolean(Constant.IS_ONLINE, isSuccess).commit();
		intent.setAction(Constant.ACTION_RECONNECT_STATE);
		intent.putExtra(Constant.RECONNECT_STATE, isSuccess);
		sendBroadcast(intent);
	}

}
