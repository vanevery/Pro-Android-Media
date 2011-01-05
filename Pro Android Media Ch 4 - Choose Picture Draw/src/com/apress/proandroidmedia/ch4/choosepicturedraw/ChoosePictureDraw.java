package com.apress.proandroidmedia.ch4.choosepicturedraw;

import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;

public class ChoosePictureDraw extends Activity implements OnClickListener,
		OnTouchListener {

	ImageView choosenImageView;
	Button choosePicture;

	Bitmap bmp;
	Bitmap alteredBitmap;
	Canvas canvas;
	Paint paint;
	Matrix matrix;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		choosenImageView = (ImageView) this.findViewById(R.id.ChoosenImageView);
		choosePicture = (Button) this.findViewById(R.id.ChoosePictureButton);

		choosePicture.setOnClickListener(this);
		choosenImageView.setOnTouchListener(this);
	}

	public void onClick(View v) {
		Intent choosePictureIntent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(choosePictureIntent, 0);
	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (resultCode == RESULT_OK) {
			Uri imageFileUri = intent.getData();
			Display currentDisplay = getWindowManager().getDefaultDisplay();

			float dw = currentDisplay.getWidth();
			float dh = currentDisplay.getHeight();

			try {
				BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
				bmpFactoryOptions.inJustDecodeBounds = true;
				bmp = BitmapFactory
						.decodeStream(getContentResolver().openInputStream(
								imageFileUri), null, bmpFactoryOptions);

				int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight
						/ dh);
				int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth
						/ dw);
				if (heightRatio > 1 && widthRatio > 1) {
					if (heightRatio > widthRatio) {
						bmpFactoryOptions.inSampleSize = heightRatio;
					} else {
						// Width ratio is larger, scale according to it
						bmpFactoryOptions.inSampleSize = widthRatio;
					}
				}

				bmpFactoryOptions.inJustDecodeBounds = false;
				bmp = BitmapFactory
						.decodeStream(getContentResolver().openInputStream(
								imageFileUri), null, bmpFactoryOptions);

				alteredBitmap = Bitmap.createBitmap(bmp.getWidth(), bmp
						.getHeight(), bmp.getConfig());
				canvas = new Canvas(alteredBitmap);
				paint = new Paint();
				paint.setColor(Color.GREEN);
				paint.setStrokeWidth(5);
				matrix = new Matrix();
				canvas.drawBitmap(bmp, matrix, paint);

				choosenImageView.setImageBitmap(alteredBitmap);
				choosenImageView.setOnTouchListener(this);
			} catch (FileNotFoundException e) {
				Log.v("ERROR", e.toString());
			}
		}
	}

	float downx = 0;
	float downy = 0;
	float upx = 0;
	float upy = 0;

	public boolean onTouch(View v, MotionEvent event) {
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			downx = event.getX();
			downy = event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			upx = event.getX();
			upy = event.getY();
			canvas.drawLine(downx, downy, upx, upy, paint);
			choosenImageView.invalidate();
			downx = upx;
			downy = upy;
			break;
		case MotionEvent.ACTION_UP:
			upx = event.getX();
			upy = event.getY();
			canvas.drawLine(downx, downy, upx, upy, paint);
			choosenImageView.invalidate();
			break;
		case MotionEvent.ACTION_CANCEL:
			break;
		default:
			break;
		}

		return true;
	}

}
