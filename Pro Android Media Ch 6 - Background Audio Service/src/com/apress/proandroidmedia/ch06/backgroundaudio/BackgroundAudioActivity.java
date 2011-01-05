package com.apress.proandroidmedia.ch06.backgroundaudio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BackgroundAudioActivity extends Activity implements
		OnClickListener {

	Button startPlaybackButton, stopPlaybackButton;
	Intent playbackServiceIntent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		startPlaybackButton = (Button) this
				.findViewById(R.id.StartPlaybackButton);
		stopPlaybackButton = (Button) this
				.findViewById(R.id.StopPlaybackButton);

		startPlaybackButton.setOnClickListener(this);
		stopPlaybackButton.setOnClickListener(this);

		playbackServiceIntent = new Intent(this, BackgroundAudioService.class);
	}

	public void onClick(View v) {
		if (v == startPlaybackButton) {
			startService(playbackServiceIntent);
			finish();
		} else if (v == stopPlaybackButton) {
			stopService(playbackServiceIntent);
			finish();
		}
	}
}
