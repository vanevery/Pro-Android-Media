package com.apress.proandroidmedia.ch07.intentaudiorecord;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class IntentAudioRecorder extends Activity implements OnClickListener,
		OnCompletionListener {

	public static int RECORD_REQUEST = 0;

	Button createRecording, playRecording;

	Uri audioFileUri;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		createRecording = (Button) this.findViewById(R.id.RecordButton);
		createRecording.setOnClickListener(this);

		playRecording = (Button) this.findViewById(R.id.PlayButton);
		playRecording.setOnClickListener(this);
		playRecording.setEnabled(false);
	}

	public void onClick(View v) {
		if (v == createRecording) {
			Intent intent = new Intent(
					MediaStore.Audio.Media.RECORD_SOUND_ACTION);
			startActivityForResult(intent, RECORD_REQUEST);
		} else if (v == playRecording) {

			MediaPlayer mediaPlayer = MediaPlayer.create(this, audioFileUri);
			mediaPlayer.setOnCompletionListener(this);
			mediaPlayer.start();
			playRecording.setEnabled(false);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == RECORD_REQUEST) {
			audioFileUri = data.getData();
			playRecording.setEnabled(true);
		}
	}

	public void onCompletion(MediaPlayer mp) {
		playRecording.setEnabled(true);
	}
}
