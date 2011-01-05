package com.apress.proandroidmedia.ch09.videointent;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class VideoPlayerIntent extends Activity implements OnClickListener {
	Button playButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		playButton = (Button) this.findViewById(R.id.PlayButton);
		playButton.setOnClickListener(this);
	}

	public void onClick(View v) {
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
		// Download "Test_Movie_iPhone.m4v from
		// http://www.mobvcasting.com/android/video/Test_Movie_iPhone.m4v
		// and save to the root of your device's SD card.
		Uri data = Uri.parse(Environment.getExternalStorageDirectory()
				.getPath()
				+ "/Test_Movie_iPhone.m4v");
		intent.setDataAndType(data, "video/mp4");
		startActivity(intent);
	}
}