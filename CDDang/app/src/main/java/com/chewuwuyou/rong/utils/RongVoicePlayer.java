package com.chewuwuyou.rong.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.File;

public class RongVoicePlayer {
    private MediaPlayer mediaPlayer = null;
    private String filePath = "";
    private static boolean isPlaying = false;
    private VoiceCallBack voiceCallBack;

    private RongVoicePlayer() {

    }

    private static class SingletonHolder {
        private static final RongVoicePlayer INSTANCE = new RongVoicePlayer();
    }

    public static RongVoicePlayer getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void stopPlayVoice() {
        if (voiceCallBack != null)
            voiceCallBack.onCompletion();
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
            isPlaying = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean playVoice(Context context, String filePath, final VoiceCallBack voiceCallBack) {
        if (isPlaying) {
            stopPlayVoice();
            if (this.filePath.equals(filePath)) {
                return false;
            }
        }
        this.filePath = filePath;
        this.voiceCallBack = voiceCallBack;
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) return false;
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mediaPlayer = new MediaPlayer();
        // 设定为扬声器播放
        audioManager.setMode(AudioManager.MODE_NORMAL);
        audioManager.setSpeakerphoneOn(true);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                    stopPlayVoice();
                }
            });
            mediaPlayer.start();
            isPlaying = true;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public interface VoiceCallBack {
        void onCompletion();
    }
}
