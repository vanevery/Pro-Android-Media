package com.apress.proandroidmedia.ch08.audiosynthesis;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AudioSynthesis extends Activity implements OnClickListener {

	Button startSound;
	Button endSound;

	AudioSynthesisTask audioSynth;

	boolean keepGoing = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		startSound = (Button) this.findViewById(R.id.StartSound);
		startSound.setOnClickListener(this);

		endSound = (Button) this.findViewById(R.id.EndSound);
		endSound.setOnClickListener(this);

		endSound.setEnabled(false);
	}

	@Override
	public void onPause() {
		super.onPause();
		keepGoing = false;

		endSound.setEnabled(false);
		startSound.setEnabled(true);
	}

	public void onClick(View v) {
		if (v == startSound) {
			keepGoing = true;

			audioSynth = new AudioSynthesisTask();
			audioSynth.execute();

			endSound.setEnabled(true);
			startSound.setEnabled(false);
		} else if (v == endSound) {
			keepGoing = false;

			endSound.setEnabled(false);
			startSound.setEnabled(true);
		}
	}

	private class AudioSynthesisTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			final int SAMPLE_RATE = 11025;

			int minSize = AudioTrack.getMinBufferSize(SAMPLE_RATE,
					AudioFormat.CHANNEL_CONFIGURATION_MONO,
					AudioFormat.ENCODING_PCM_16BIT);

			AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
					SAMPLE_RATE, AudioFormat.CHANNEL_CONFIGURATION_MONO,
					AudioFormat.ENCODING_PCM_16BIT, minSize,
					AudioTrack.MODE_STREAM);

			audioTrack.play();

			short[] buffer = { 8130, 15752, 22389, 27625, 31134, 32695, 32210,
					29711, 25354, 19410, 12253, 4329, -3865, -11818, -19032,
					-25055, -29511, -32121, -32722, -31276, -27874, -22728,
					-16160, -8582, -466 };

			while (keepGoing) {
				audioTrack.write(buffer, 0, buffer.length);
			}

			return null;
		}
	}
}
