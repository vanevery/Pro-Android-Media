package com.apress.proandroidmedia.ch06.simpleservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class SimpleServiceService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.v("SIMPLESERVICE", "onCreate");
	}

	/*
	 * @Override public int onStartCommand(Intent intent, int flags, int
	 * startId) { Log.v("SIMPLESERVICE","onStartCommand"); return START_STICKY;
	 * }
	 */

	@Override
	public void onStart(Intent intent, int startid) {
		Log.v("SIMPLESERVICE", "onStart");
	}

	public void onDestroy() {
		Log.v("SIMPLESERVICE", "onDestroy");
	}
}
