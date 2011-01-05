package com.apress.proandroidmedia.ch11.videocaptureintent;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class VideoCaptureIntent extends Activity implements OnClickListener {

	public static int VIDEO_CAPTURED = 1;

	Button captureVideoButton;
	Button playVideoButton;
	Button saveVideoButton;

	EditText titleEditText;

	Uri videoFileUri;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		captureVideoButton = (Button) this
				.findViewById(R.id.CaptureVideoButton);
		playVideoButton = (Button) this.findViewById(R.id.PlayVideoButton);
		saveVideoButton = (Button) this.findViewById(R.id.SaveVideoButton);

		titleEditText = (EditText) this.findViewById(R.id.TitleEditText);

		captureVideoButton.setOnClickListener(this);
		playVideoButton.setOnClickListener(this);
		saveVideoButton.setOnClickListener(this);

		playVideoButton.setEnabled(false);
		saveVideoButton.setEnabled(false);
		titleEditText.setEnabled(false);
	}

	public void onClick(View v) {
		if (v == captureVideoButton) {
			Intent captureVideoIntent = new Intent(
					android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
			startActivityForResult(captureVideoIntent, VIDEO_CAPTURED);
		} else if (v == playVideoButton) {
			Intent playVideoIntent = new Intent(Intent.ACTION_VIEW,
					videoFileUri);
			startActivity(playVideoIntent);
		} else if (v == saveVideoButton) {

			ContentValues values = new ContentValues(1);
			values.put(MediaStore.MediaColumns.TITLE, titleEditText.getText()
					.toString());

			if (getContentResolver().update(videoFileUri, values, null, null) == 1) {

				Toast t = Toast.makeText(this, "Updated "
						+ titleEditText.getText().toString(),
						Toast.LENGTH_SHORT);
				t.show();
			} else {

				Toast t = Toast.makeText(this, "Error", Toast.LENGTH_SHORT);
				t.show();
			}
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == VIDEO_CAPTURED) {
			videoFileUri = data.getData();
			playVideoButton.setEnabled(true);
			saveVideoButton.setEnabled(true);
			titleEditText.setEnabled(true);
		}
	}
}
