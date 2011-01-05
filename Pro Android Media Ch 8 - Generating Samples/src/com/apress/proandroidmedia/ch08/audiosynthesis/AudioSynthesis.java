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

	float synth_frequency = 440; // 440 Hz, Middle A

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

			short[] buffer = new short[minSize];

			float angular_frequency = (float) (2 * Math.PI) * synth_frequency
					/ SAMPLE_RATE;
			float angle = 0;

			while (keepGoing) {
				for (int i = 0; i < buffer.length; i++) {
					buffer[i] = (short) (Short.MAX_VALUE * ((float) Math
							.sin(angle)));
					angle += angular_frequency;
				}
				audioTrack.write(buffer, 0, buffer.length);
			}

			return null;
		}
	}
}
