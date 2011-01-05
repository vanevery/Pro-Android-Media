package com.apress.proandroidmedia.ch10.videoview;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class ViewTheVideo extends Activity {
	VideoView vv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		vv = (VideoView) this.findViewById(R.id.VideoView);
		Uri videoUri = Uri
				.parse("rtsp://v2.cache2.c.youtube.com/CjgLENy73wIaLwm3JbT_9HqWohMYESARFEIJbXYtZ29vZ2xlSARSB3Jlc3VsdHNg_vSmsbeSyd5JDA==/0/0/0/video.3gp");
		vv.setMediaController(new MediaController(this));
		vv.setVideoURI(videoUri);
		vv.start();
	}
}
