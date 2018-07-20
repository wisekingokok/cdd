package com.chewuwuyou.eim.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class BaseService extends Service {

	protected String ACTION_USE = null;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	//发送通知提示Service的启动项启动完成
	private void sendBroadCast() {
		Intent intent = new Intent();
		intent.setAction(ACTION_USE);
		if (ACTION_USE != null)
		sendBroadcast(intent);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		sendBroadCast();
		return super.onStartCommand(intent, flags, startId);
	}
}