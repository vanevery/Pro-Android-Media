package com.apress.proandroidmedia.ch5.intentaudioplayer;

import java.io.File;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class IntentAudioPlayer extends Activity implements OnClickListener {

	Button playButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		playButton = (Button) this.findViewById(R.id.Button01);
		playButton.setOnClickListener(this);
	}

	public void onClick(View v) {
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW);

		File sdcard = Environment.getExternalStorageDirectory();
		File audioFile = new File(sdcard.getPath()
				+ "/Music/goodmorningandroid.mp3");

		intent.setDataAndType(Uri.fromFile(audioFile), "audio/mp3");
		startActivity(intent);
	}
}
