package com.apress.proandroidmedia.ch06.backgroundaudiobind;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BackgroundAudioActivity extends Activity implements
		OnClickListener {

	Button startPlaybackButton, stopPlaybackButton;
	Button haveFunButton;
	Intent playbackServiceIntent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		startPlaybackButton = (Button) this
				.findViewById(R.id.StartPlaybackButton);
		stopPlaybackButton = (Button) this
				.findViewById(R.id.StopPlaybackButton);
		haveFunButton = (Button) this.findViewById(R.id.HaveFunButton);

		startPlaybackButton.setOnClickListener(this);
		stopPlaybackButton.setOnClickListener(this);
		haveFunButton.setOnClickListener(this);

		playbackServiceIntent = new Intent(this, BackgroundAudioService.class);
	}

	public void onClick(View v) {
		if (v == startPlaybackButton) {
			startService(playbackServiceIntent);
			bindService(playbackServiceIntent, serviceConnection,
					Context.BIND_AUTO_CREATE);
		} else if (v == stopPlaybackButton) {
			unbindService(serviceConnection);
			stopService(playbackServiceIntent);
		} else if (v == haveFunButton) {
			baService.haveFun();
		}
	}

	private BackgroundAudioService baService;

	private ServiceConnection serviceConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder baBinder) {
			baService = ((BackgroundAudioService.BackgroundAudioServiceBinder) baBinder)
					.getService();
		}

		public void onServiceDisconnected(ComponentName className) {
			baService = null;
		}
	};
}
