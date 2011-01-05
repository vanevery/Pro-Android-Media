package com.apress.proandroidmedia.ch06.audiohttp;

import java.io.IOException;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

public class AudioHTTPPlayer extends Activity {

	MediaPlayer mediaPlayer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mediaPlayer = new MediaPlayer();

		try {
			mediaPlayer
					.setDataSource("http://www.mobvcasting.com/android/audio/goodmorningandroid.mp3");

			mediaPlayer.prepare();
			mediaPlayer.start();
		} catch (IOException e) {
			Log.v("AUDIOHTTPPLAYER", e.getMessage());
		}
	}
}
