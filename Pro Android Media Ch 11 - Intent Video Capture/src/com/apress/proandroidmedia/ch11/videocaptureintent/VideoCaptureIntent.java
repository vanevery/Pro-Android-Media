package com.apress.proandroidmedia.ch11.videocaptureintent;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.VideoView;

public class VideoCaptureIntent extends Activity implements OnClickListener {
	public static int VIDEO_CAPTURED = 1;

	Button captureVideoButton;
	Button playVideoButton;

	VideoView videoView;
	Uri videoFileUri;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		captureVideoButton = (Button) this
				.findViewById(R.id.CaptureVideoButton);
		playVideoButton = (Button) this.findViewById(R.id.PlayVideoButton);

		captureVideoButton.setOnClickListener(this);
		playVideoButton.setOnClickListener(this);

		playVideoButton.setEnabled(false);

		videoView = (VideoView) this.findViewById(R.id.VideoView);
	}

	public void onClick(View v) {
		if (v == captureVideoButton) {
			Intent captureVideoIntent = new Intent(
					android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
			startActivityForResult(captureVideoIntent, VIDEO_CAPTURED);
		} else if (v == playVideoButton) {
			videoView.setVideoURI(videoFileUri);
			videoView.start();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == VIDEO_CAPTURED) {
			videoFileUri = data.getData();
			playVideoButton.setEnabled(true);
		}
	}

}
