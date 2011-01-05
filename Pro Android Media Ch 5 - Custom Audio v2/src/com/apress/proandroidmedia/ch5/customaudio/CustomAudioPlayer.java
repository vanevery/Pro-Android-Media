package com.apress.proandroidmedia.ch5.customaudio;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class CustomAudioPlayer extends Activity implements
		OnCompletionListener, OnTouchListener, OnClickListener {

	MediaPlayer mediaPlayer;
	View theView;
	Button stopButton, startButton;

	int position = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		stopButton = (Button) this.findViewById(R.id.StopButton);
		startButton = (Button) this.findViewById(R.id.StartButton);

		startButton.setOnClickListener(this);
		stopButton.setOnClickListener(this);

		theView = this.findViewById(R.id.theview);

		theView.setOnTouchListener(this);

		mediaPlayer = MediaPlayer.create(this, R.raw.goodmorningandroid);
		mediaPlayer.setOnCompletionListener(this);
		mediaPlayer.start();
	}

	public void onCompletion(MediaPlayer mp) {
		mediaPlayer.start();
		mediaPlayer.seekTo(position);
	}

	public boolean onTouch(View v, MotionEvent me) {

		if (me.getAction() == MotionEvent.ACTION_MOVE) {
			if (mediaPlayer.isPlaying()) {
				position = (int) (me.getX() * mediaPlayer.getDuration() / theView
						.getWidth());
				Log.v("SEEK", "" + position);
				mediaPlayer.seekTo(position);
			}
		}

		return true;
	}

	public void onClick(View v) {
		if (v == stopButton) {
			mediaPlayer.pause();
		} else if (v == startButton) {
			mediaPlayer.start();
		}

	}
}
