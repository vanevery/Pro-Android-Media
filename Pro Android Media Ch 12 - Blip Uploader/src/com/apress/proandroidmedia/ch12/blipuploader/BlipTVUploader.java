package com.apress.proandroidmedia.ch12.blipuploader;

import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class BlipTVUploader extends Activity {

	final static int VIDEO_CAPTURED = 0;
	final static int VIDEO_PLAYED = 1;

	File videoFile;
	String title = "A Video";
	String username = "BLIPTV_USERNAME";
	String password = "BLIPTV_PASSWORD";

	String postingResult = "";

	long fileLength = 0;

	TextView textview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		textview = (TextView) findViewById(R.id.textview);

		Intent captureVideoIntent = new Intent(
				android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
		startActivityForResult(captureVideoIntent, VIDEO_CAPTURED);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK && requestCode == VIDEO_CAPTURED) {
			Uri videoFileUri = data.getData();

			String[] columns = { android.provider.MediaStore.Video.Media.DATA };
			Cursor cursor = managedQuery(videoFileUri, columns, null, null,
					null);
			int fileColumn = cursor
					.getColumnIndexOrThrow(android.provider.MediaStore.Video.Media.DATA);
			if (cursor.moveToFirst()) {
				String videoFilePath = cursor.getString(fileColumn);
				Log.v("VIDEO FILE PATH", videoFilePath);

				videoFile = new File(videoFilePath);
				fileLength = videoFile.length();
				BlipTVFilePoster btvfp = new BlipTVFilePoster();
				btvfp.execute();
			}

		} else if (requestCode == VIDEO_PLAYED) {
			finish();
		}
	}

	class BlipTVFilePoster extends AsyncTask<Void, String, Void> implements
			ProgressListener, BlipXMLParserListener {

		String videoUrl;

		@Override
		protected Void doInBackground(Void... params) {

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://blip.tv/file/post");

			ProgressMultipartEntity multipartentity = new ProgressMultipartEntity(
					this);

			try {
				multipartentity.addPart("file", new FileBody(videoFile));

				multipartentity.addPart("userlogin", new StringBody(username));
				multipartentity.addPart("password", new StringBody(password));
				multipartentity.addPart("title", new StringBody(title));
				multipartentity.addPart("post", new StringBody("1"));
				multipartentity.addPart("skin", new StringBody("api"));

				httppost.setEntity(multipartentity);
				HttpResponse httpresponse = httpclient.execute(httppost);

				HttpEntity responseentity = httpresponse.getEntity();
				if (responseentity != null) {

					InputStream inputstream = responseentity.getContent();

					SAXParserFactory aSAXParserFactory = SAXParserFactory
							.newInstance();
					try {

						SAXParser aSAXParser = aSAXParserFactory.newSAXParser();
						XMLReader anXMLReader = aSAXParser.getXMLReader();

						BlipResponseXMLHandler xmlHandler = new BlipResponseXMLHandler(
								this);
						anXMLReader.setContentHandler(xmlHandler);
						anXMLReader.parse(new InputSource(inputstream));

					} catch (ParserConfigurationException e) {
						e.printStackTrace();
					} catch (SAXException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

					inputstream.close();

				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onProgressUpdate(String... textToDisplay) {
			textview.setText(textToDisplay[0]);
		}

		protected void onPostExecute(Void result) {
			if (videoUrl != null) {
				Intent viewVideoIntent = new Intent(Intent.ACTION_VIEW);
				Uri uri = Uri.parse("http://blip.tv/file/get/" + videoUrl);
				viewVideoIntent.setDataAndType(uri, "video/3gpp");
				startActivityForResult(viewVideoIntent, VIDEO_PLAYED);
			}
		}

		public void transferred(long num) {
			double percent = (double) num / (double) fileLength;
			int percentInt = (int) (percent * 100);

			publishProgress("" + percentInt + "% Transferred");
		}

		public void parseResult(String result) {
			publishProgress(result);
		}

		public void setVideoUrl(String url) {
			videoUrl = url;
		}
	}

	class ProgressMultipartEntity extends MultipartEntity {
		ProgressListener progressListener;

		public ProgressMultipartEntity(ProgressListener pListener) {
			super();
			this.progressListener = pListener;
		}

		@Override
		public void writeTo(OutputStream outstream) throws IOException {
			super.writeTo(new ProgressOutputStream(outstream,
					this.progressListener));
		}
	}

	interface ProgressListener {
		void transferred(long num);
	}

	static class ProgressOutputStream extends FilterOutputStream {

		ProgressListener listener;
		int transferred;

		public ProgressOutputStream(final OutputStream out,
				ProgressListener listener) {
			super(out);
			this.listener = listener;
			this.transferred = 0;
		}

		public void write(byte[] b, int off, int len) throws IOException {
			out.write(b, off, len);
			this.transferred += len;
			this.listener.transferred(this.transferred);
		}

		public void write(int b) throws IOException {
			out.write(b);
			this.transferred++;
			this.listener.transferred(this.transferred);
		}
	}

	interface BlipXMLParserListener {
		void parseResult(String result);

		void setVideoUrl(String url);
	}

	class BlipResponseXMLHandler extends DefaultHandler {

		int NONE = 0;
		int ONSTATUS = 1;
		int ONFILE = 2;
		int ONERRORMESSAGE = 3;

		int state = NONE;

		int STATUS_UNKNOWN = 0;
		int STATUS_OK = 1;
		int STATUS_ERROR = 2;

		int status = STATUS_UNKNOWN;

		String message = "";

		BlipXMLParserListener listener;

		public BlipResponseXMLHandler(BlipXMLParserListener bxpl) {
			super();
			listener = bxpl;
		}

		@Override
		public void startDocument() throws SAXException {
		}

		@Override
		public void endDocument() throws SAXException {
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if (localName.equalsIgnoreCase("status")) {
				state = ONSTATUS;
			} else if (localName.equalsIgnoreCase("file")) {
				state = ONFILE;

				listener.parseResult("onFile");
				message = attributes.getValue("src");
				listener.parseResult("filemessage:" + message);

				listener.setVideoUrl(message);
			} else if (localName.equalsIgnoreCase("message")) {
				state = ONERRORMESSAGE;
				listener.parseResult("onErrorMessage");
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if (localName.equalsIgnoreCase("status")) {
				state = NONE;
			} else if (localName.equalsIgnoreCase("file")) {
				state = NONE;
			} else if (localName.equalsIgnoreCase("message")) {
				state = NONE;
			}
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			String stringChars = new String(ch, start, length);
			if (state == ONSTATUS) {
				if (stringChars.equalsIgnoreCase("OK")) {
					status = STATUS_OK;
				} else if (stringChars.equalsIgnoreCase("ERROR")) {
					status = STATUS_ERROR;
				} else {
					status = STATUS_UNKNOWN;
				}
			} else if (state == ONERRORMESSAGE) {
				message += stringChars.trim();
				listener.parseResult(message);
			}
		}
	}
}
