package com.apress.proandroidmedia.ch06.simpleservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SimpleServiceActivity extends Activity implements OnClickListener {

	Button startServiceButton;
	Button stopServiceButton;

	Intent serviceIntent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		startServiceButton = (Button) this
				.findViewById(R.id.StartServiceButton);
		stopServiceButton = (Button) this.findViewById(R.id.StopServiceButton);

		startServiceButton.setOnClickListener(this);
		stopServiceButton.setOnClickListener(this);

		serviceIntent = new Intent(this, SimpleServiceService.class);
	}

	public void onClick(View v) {
		if (v == startServiceButton) {
			startService(serviceIntent);
		} else if (v == stopServiceButton) {
			stopService(serviceIntent);
		}
	}
}
