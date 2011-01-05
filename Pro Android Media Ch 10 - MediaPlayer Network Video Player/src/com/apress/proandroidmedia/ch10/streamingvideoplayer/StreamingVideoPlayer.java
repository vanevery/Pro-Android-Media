package com.apress.proandroidmedia.ch10.streamingvideoplayer;

import java.io.IOException;
import android.app.Activity;
import android.os.Bundle;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.MediaController;

public class StreamingVideoPlayer extends Activity implements
		OnCompletionListener, OnErrorListener, OnInfoListener,
		OnBufferingUpdateListener, OnPreparedListener, OnSeekCompleteListener,
		OnVideoSizeChangedListener, SurfaceHolder.Callback,
		MediaController.MediaPlayerControl {

	MediaController controller;
	Display currentDisplay;
	SurfaceView surfaceView;
	SurfaceHolder surfaceHolder;
	MediaPlayer mediaPlayer;

	View mainView;
	TextView statusView;

	int videoWidth = 0;
	int videoHeight = 0;

	boolean readyToPlay = false;
	public final static String LOGTAG = "STREAMING_VIDEO_PLAYER";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		mainView = this.findViewById(R.id.MainView);

		statusView = (TextView) this.findViewById(R.id.StatusTextView);

		surfaceView = (SurfaceView) this.findViewById(R.id.SurfaceView);
		surfaceHolder = surfaceView.getHolder();

		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		mediaPlayer = new MediaPlayer();

		statusView.setText("MediaPlayer Created");

		mediaPlayer.setOnCompletionListener(this);
		mediaPlayer.setOnErrorListener(this);
		mediaPlayer.setOnInfoListener(this);
		mediaPlayer.setOnPreparedListener(this);
		mediaPlayer.setOnSeekCompleteListener(this);
		mediaPlayer.setOnVideoSizeChangedListener(this);
		mediaPlayer.setOnBufferingUpdateListener(this);

		String filePath = "rtsp://v2.cache2.c.youtube.com/CjgLENy73wIaLwm3JbT_9HqWohMYESARFEIJbXYtZ29vZ2xlSARSB3Jlc3VsdHNg96LUzsK0781MDA==/0/0/0/video.3gp";
		try {
			mediaPlayer.setDataSource(filePath);
		} catch (IllegalArgumentException e) {
			Log.v(LOGTAG, e.getMessage());
			finish();
		} catch (IllegalStateException e) {
			Log.v(LOGTAG, e.getMessage());
			finish();
		} catch (IOException e) {
			Log.v(LOGTAG, e.getMessage());
			finish();
		}

		statusView.setText("MediaPlayer DataSource Set");
		currentDisplay = getWindowManager().getDefaultDisplay();
		controller = new MediaController(this);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		Log.v(LOGTAG, "surfaceCreated Called");

		mediaPlayer.setDisplay(holder);
		statusView.setText("MediaPlayer Display Surface Set");

		try {
			mediaPlayer.prepareAsync();
		} catch (IllegalStateException e) {
			Log.v(LOGTAG, "IllegalStateException " + e.getMessage());
			finish();
		}

		statusView.setText("MediaPlayer Preparing");
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.v(LOGTAG, "surfaceChanged Called");
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.v(LOGTAG, "surfaceDestroyed Called");
	}

	public void onCompletion(MediaPlayer mp) {
		Log.v(LOGTAG, "onCompletion Called");
		statusView.setText("MediaPlayer Playback Completed");
	}

	public boolean onError(MediaPlayer mp, int whatError, int extra) {
		Log.v(LOGTAG, "onError Called");
		statusView.setText("MediaPlayer Error");
		if (whatError == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
			Log.v(LOGTAG, "Media Error, Server Died " + extra);
		} else if (whatError == MediaPlayer.MEDIA_ERROR_UNKNOWN) {
			Log.v(LOGTAG, "Media Error, Error Unknown " + extra);
		}
		return false;
	}

	public boolean onInfo(MediaPlayer mp, int whatInfo, int extra) {
		statusView.setText("MediaPlayer onInfo Called");
		if (whatInfo == MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING) {
			Log.v(LOGTAG, "Media Info, Media Info Bad Interleaving " + extra);
		} else if (whatInfo == MediaPlayer.MEDIA_INFO_NOT_SEEKABLE) {
			Log.v(LOGTAG, "Media Info, Media Info Not Seekable " + extra);
		} else if (whatInfo == MediaPlayer.MEDIA_INFO_UNKNOWN) {
			Log.v(LOGTAG, "Media Info, Media Info Unknown " + extra);
		} else if (whatInfo == MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING) {
			Log.v(LOGTAG, "MediaInfo, Media Info Video Track Lagging " + extra);
		} /*
		 * Android version 2.0 or higher else if (whatInfo ==
		 * MediaPlayer.MEDIA_INFO_METADATA_UPDATE) { Log.v(LOGTAG,
		 * "MediaInfo, Media Info Metadata Update " + extra); }
		 */
		return false;
	}

	public void onPrepared(MediaPlayer mp) {
		Log.v(LOGTAG, "onPrepared Called");
		statusView.setText("MediaPlayer Prepared");

		videoWidth = mp.getVideoWidth();
		videoHeight = mp.getVideoHeight();

		Log.v(LOGTAG, "Width: " + videoWidth);
		Log.v(LOGTAG, "Height: " + videoHeight);

		if (videoWidth > currentDisplay.getWidth()
				|| videoHeight > currentDisplay.getHeight()) {
			float heightRatio = (float) videoHeight
					/ (float) currentDisplay.getHeight();
			float widthRatio = (float) videoWidth
					/ (float) currentDisplay.getWidth();

			if (heightRatio > 1 || widthRatio > 1) {
				if (heightRatio > widthRatio) {
					videoHeight = (int) Math.ceil((float) videoHeight
							/ (float) heightRatio);
					videoWidth = (int) Math.ceil((float) videoWidth
							/ (float) heightRatio);
				} else {
					videoHeight = (int) Math.ceil((float) videoHeight
							/ (float) widthRatio);
					videoWidth = (int) Math.ceil((float) videoWidth
							/ (float) widthRatio);
				}
			}
		}

		surfaceView.setLayoutParams(new LinearLayout.LayoutParams(videoWidth,
				videoHeight));
		controller.setMediaPlayer(this);
		controller.setAnchorView(this.findViewById(R.id.MainView));
		controller.setEnabled(true);
		controller.show();

		mp.start();
		statusView.setText("MediaPlayer Started");
	}

	public void onSeekComplete(MediaPlayer mp) {
		Log.v(LOGTAG, "onSeekComplete Called");
	}

	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
		Log.v(LOGTAG, "onVideoSizeChanged Called");

		videoWidth = mp.getVideoWidth();
		videoHeight = mp.getVideoHeight();

		Log.v(LOGTAG, "Width: " + videoWidth);
		Log.v(LOGTAG, "Height: " + videoHeight);

		if (videoWidth > currentDisplay.getWidth()
				|| videoHeight > currentDisplay.getHeight()) {
			float heightRatio = (float) videoHeight
					/ (float) currentDisplay.getHeight();
			float widthRatio = (float) videoWidth
					/ (float) currentDisplay.getWidth();

			if (heightRatio > 1 || widthRatio > 1) {
				if (heightRatio > widthRatio) {
					videoHeight = (int) Math.ceil((float) videoHeight
							/ (float) heightRatio);
					videoWidth = (int) Math.ceil((float) videoWidth
							/ (float) heightRatio);
				} else {
					videoHeight = (int) Math.ceil((float) videoHeight
							/ (float) widthRatio);
					videoWidth = (int) Math.ceil((float) videoWidth
							/ (float) widthRatio);
				}
			}
		}

		surfaceView.setLayoutParams(new LinearLayout.LayoutParams(videoWidth,
				videoHeight));
	}

	public void onBufferingUpdate(MediaPlayer mp, int bufferedPercent) {
		statusView.setText("MediaPlayer Buffering: " + bufferedPercent + "%");
		Log.v(LOGTAG, "MediaPlayer Buffering: " + bufferedPercent + "%");
	}

	public boolean canPause() {
		return true;
	}

	public boolean canSeekBackward() {
		return true;
	}

	public boolean canSeekForward() {
		return true;
	}

	public int getBufferPercentage() {
		return 0;
	}

	public int getCurrentPosition() {
		return mediaPlayer.getCurrentPosition();
	}

	public int getDuration() {
		return mediaPlayer.getDuration();
	}

	public boolean isPlaying() {
		return mediaPlayer.isPlaying();
	}

	public void pause() {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
		}
	}

	public void seekTo(int pos) {
		mediaPlayer.seekTo(pos);
	}

	public void start() {
		mediaPlayer.start();
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (controller.isShowing()) {
			controller.hide();
		} else {
			controller.show();
		}
		return false;
	}
}
