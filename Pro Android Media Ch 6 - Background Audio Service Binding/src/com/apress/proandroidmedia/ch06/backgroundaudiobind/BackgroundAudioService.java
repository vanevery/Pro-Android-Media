package com.apress.proandroidmedia.ch06.backgroundaudiobind;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class BackgroundAudioService extends Service implements
		OnCompletionListener {
	MediaPlayer mediaPlayer;

	public class BackgroundAudioServiceBinder extends Binder {
		BackgroundAudioService getService() {
			return BackgroundAudioService.this;
		}
	}

	private final IBinder basBinder = new BackgroundAudioServiceBinder();

	@Override
	public IBinder onBind(Intent intent) {
		// Return the BackgroundAudioServiceBinder object
		return basBinder;
	}

	public void haveFun() {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 2500);
		}
	}

	@Override
	public void onCreate() {
		Log.v("PLAYERSERVICE", "onCreate");

		mediaPlayer = MediaPlayer.create(this, R.raw.test);
		mediaPlayer.setOnCompletionListener(this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.v("PLAYERSERVICE", "onStartCommand");

		if (!mediaPlayer.isPlaying()) {
			mediaPlayer.start();
		}
		return START_STICKY;
	}

	public void onDestroy() {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
		}
		mediaPlayer.release();
		Log.v("SIMPLESERVICE", "onDestroy");
	}

	public void onCompletion(MediaPlayer _mediaPlayer) {
		stopSelf();
	}

}
