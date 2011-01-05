package com.apress.proandroidmedia.ch09.videoview;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.VideoView;

public class ViewTheVideo extends Activity {
	VideoView vv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		vv = (VideoView) this.findViewById(R.id.VideoView);

		// Download "Test_Movie_iPhone.m4v from
		// http://www.mobvcasting.com/android/video/Test_Movie_iPhone.m4v
		// and save to the root of your device's SD card.
		Uri videoUri = Uri.parse(Environment.getExternalStorageDirectory()
				.getPath()
				+ "/Test_Movie_iPhone.m4v");
		// Uri videoUri =
		// Uri.parse(Environment.getExternalStorageDirectory().getPath() +
		// "/Test Movie iPod.m4v");

		vv.setVideoURI(videoUri);
		vv.start();
	}
}
