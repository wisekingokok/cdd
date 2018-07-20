package com.chewuwuyou.eim.view;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.chewuwuyou.app.R;
import com.chewuwuyou.app.utils.ServerUtils;
import com.chewuwuyou.app.utils.ToastUtil;
import com.chewuwuyou.eim.manager.MessageManager;
import com.chewuwuyou.eim.model.IMMessage;
import com.chewuwuyou.eim.util.DownloadVoiceUtil;

public class VoicePlayClickListener implements View.OnClickListener {

	IMMessage message;
	ImageView voiceIconView;
	private AnimationDrawable voiceAnimation = null;
	MediaPlayer mediaPlayer = null;
	ImageView iv_read_status;
	Activity activity;
	private BaseAdapter adapter;
	ProgressBar mBar;
	public static boolean isPlaying = false;
	public static VoicePlayClickListener currentPlayListener = null;

	public VoicePlayClickListener(IMMessage message, ImageView v,
			ImageView iv_read_status, BaseAdapter adapter, Activity activity,
			ProgressBar pb) {
		this.message = message;
		this.iv_read_status = iv_read_status;
		this.adapter = adapter;
		voiceIconView = v;
		this.activity = activity;
		this.mBar = pb;

	}

	public VoicePlayClickListener(IMMessage message, ImageView v,
			BaseAdapter adapter, Activity activity) {
		this.message = message;
		this.adapter = adapter;
		voiceIconView = v;
		this.activity = activity;

	}

	public void stopPlayVoice() {

		try {
			voiceAnimation.stop();
			if (message.getMsgType() == 0) {
				voiceIconView
						.setImageResource(R.drawable.chatfrom_voice_playing);
			} else {
				voiceIconView.setImageResource(R.drawable.chatto_voice_playing);
			}
			// stop play voice
			if (mediaPlayer != null) {
				mediaPlayer.stop();
				mediaPlayer.release();
			}
			isPlaying = false;
			adapter.notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void playVoice(String filePath) {
		if (!(new File(filePath).exists())) {
			return;
		}
		AudioManager audioManager = (AudioManager) activity
				.getSystemService(Context.AUDIO_SERVICE);
		mediaPlayer = new MediaPlayer();
		// 设定为扬声器播放
		// if (HXSDKHelper.getInstance().getModel().getSettingMsgSpeaker()) {
		audioManager.setMode(AudioManager.MODE_NORMAL);
		audioManager.setSpeakerphoneOn(true);
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
		// } else {
		// audioManager.setSpeakerphoneOn(false);// 关闭扬声器
		// 把声音设定成Earpiece（听筒）出来，设定为正在通话中
		// audioManager.setMode(AudioManager.MODE_IN_CALL);
		// mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
		// }
		try {
			mediaPlayer.setDataSource(filePath);
			mediaPlayer.prepare();
			mediaPlayer
					.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

						@Override
						public void onCompletion(MediaPlayer mp) {
							mediaPlayer.release();
							mediaPlayer = null;
							stopPlayVoice(); // stop animation
						}

					});
			isPlaying = true;
			currentPlayListener = this;
			mediaPlayer.start();
			showAnimation();
		} catch (Exception e) {
		}
	}

	// show the voice playing animation
	private void showAnimation() {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (message.getMsgType() == 0) {
					currentPlayListener.voiceIconView
							.setImageResource(R.drawable.voice_from_icon);
				} else {
					currentPlayListener.voiceIconView
							.setImageResource(R.drawable.voice_to_icon);
				}
				voiceAnimation = (AnimationDrawable) currentPlayListener.voiceIconView
						.getDrawable();
				voiceAnimation.start();
			}
		});

	}

	@Override
	public void onClick(View v) {

		if (!TextUtils.isEmpty(message.getFileName())) {// 修改
			if (isPlaying) {
				currentPlayListener.stopPlayVoice();
				if (message.getMsgType() == 1) {
					playVoice(message.getFileName());
				} else {
					iv_read_status.setVisibility(View.GONE);
					File file = new File(message.getFileName());
					if (file.exists() && file.isFile()) {
						playVoice(message.getFileName());
					} else {
						ToastUtil.toastShow(activity, "解析语音失败");
					}
				}
			} else {
				if (message.getMsgType() == 1) {
					playVoice(message.getFileName());
				} else {
					iv_read_status.setVisibility(View.GONE);
					File file = new File(message.getFileName());
					if (file.exists() && file.isFile()) {
						playVoice(message.getFileName());
					} else {
						ToastUtil.toastShow(activity, "解析语音失败");
					}
				}
			}

		} else {
			// ToastUtil.toastShow(activity, "正在下载语音...");
			downloadVoice();

		}

	}

	private void downloadVoice() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		mBar.setVisibility(View.VISIBLE);
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					if (message.getContent().contains("#VOI")
							&& message.getContent().contains("IOV#")) {
						
						String filename = System.currentTimeMillis() + ".m4a";
						final DownloadVoiceUtil dUtil = new DownloadVoiceUtil();
						boolean result = dUtil.downloadVoice(ServerUtils
								.getVoiceServiceIP(message.getContent()),
								filename);
						if (result) {
							message.setFileName(dUtil.getSDPATH() + filename);
							MessageManager.getInstance(activity)
									.updateIMMessage(message.getContent(),
											dUtil.getSDPATH() + filename);
							playVoice(message.getFileName());
							activity.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									mBar.setVisibility(View.GONE);
								}
							});

						} else {
							ToastUtil.toastShow(activity, "解析语音失败");
						}

					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}).start();
	}
}