package com.apress.proandroidmedia.ch5.audioplayer;

import java.io.File;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.provider.MediaStore;

public class AudioPlayer extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		String[] columns = { MediaStore.Audio.Media.DATA,
				MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,
				MediaStore.Audio.Media.DISPLAY_NAME,
				MediaStore.Audio.Media.MIME_TYPE,
				MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM,
				MediaStore.Audio.Media.IS_RINGTONE,
				MediaStore.Audio.Media.IS_ALARM,
				MediaStore.Audio.Media.IS_MUSIC,
				MediaStore.Audio.Media.IS_NOTIFICATION };

		Cursor cursor = managedQuery(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, columns, null,
				null, null);

		int fileColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
		int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
		int displayColumn = cursor
				.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);
		int mimeTypeColumn = cursor
				.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE);

		if (cursor.moveToFirst()) {

			String audioFilePath = cursor.getString(fileColumn);
			String mimeType = cursor.getString(mimeTypeColumn);

			Log.v("AUDIOPLAYER", audioFilePath);
			Log.v("AUDIOPLAYER", mimeType);

			Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
			File newFile = new File(audioFilePath);
			intent.setDataAndType(Uri.fromFile(newFile), mimeType);
			startActivity(intent);
		}
	}
}
