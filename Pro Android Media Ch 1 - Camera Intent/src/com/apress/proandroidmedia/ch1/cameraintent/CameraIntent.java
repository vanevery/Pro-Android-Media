package com.apress.proandroidmedia.ch1.cameraintent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

public class CameraIntent extends Activity {

	final static int CAMERA_RESULT = 0;

	ImageView imv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(i, CAMERA_RESULT);
	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (resultCode == RESULT_OK) {
			Bundle extras = intent.getExtras();
			Bitmap bmp = (Bitmap) extras.get("data");
			imv = (ImageView) findViewById(R.id.ReturnedImageView);
			imv.setImageBitmap(bmp);
		}
	}
}
