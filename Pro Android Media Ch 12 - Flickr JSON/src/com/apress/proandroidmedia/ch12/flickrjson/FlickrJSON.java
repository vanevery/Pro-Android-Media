package com.apress.proandroidmedia.ch12.flickrjson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FlickrJSON extends Activity {

	public static final String API_KEY = "YOUR_API_KEY";

	FlickrPhoto[] photos;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(
				"http://api.flickr.com/services/rest/?method=flickr.photos.search&tags=waterfront &format=json&api_key="
						+ API_KEY + "&per_page=5&nojsoncallback=1");

		HttpResponse response;
		try {
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				InputStream inputstream = entity.getContent();

				BufferedReader bufferedreader = new BufferedReader(
						new InputStreamReader(inputstream));
				StringBuilder stringbuilder = new StringBuilder();

				String currentline = null;
				try {
					while ((currentline = bufferedreader.readLine()) != null) {
						stringbuilder.append(currentline + "\n");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				String result = stringbuilder.toString();

				JSONObject thedata = new JSONObject(result);
				JSONObject thephotosdata = thedata.getJSONObject("photos");

				JSONArray thephotodata = thephotosdata.getJSONArray("photo");

				photos = new FlickrPhoto[thephotodata.length()];
				for (int i = 0; i < thephotodata.length(); i++) {
					JSONObject photodata = thephotodata.getJSONObject(i);

					Log.v("JSON", photodata.getString("id"));

					photos[i] = new FlickrPhoto(photodata.getString("id"),
							photodata.getString("owner"), photodata
									.getString("secret"), photodata
									.getString("server"), photodata
									.getString("title"), photodata
									.getString("farm"));
				}
				inputstream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		ListView listView = (ListView) this.findViewById(R.id.ListView);
		listView.setAdapter(new FlickrGalleryAdapter(this, photos));
	}

	class FlickrGalleryAdapter extends BaseAdapter {
		private Context context;
		private FlickrPhoto[] photos;

		LayoutInflater inflater;

		public FlickrGalleryAdapter(Context _context, FlickrPhoto[] _items) {
			context = _context;
			photos = _items;

			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			return photos.length;
		}

		public Object getItem(int position) {
			return photos[position];
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			View photoRow = inflater.inflate(R.layout.list_item, null);

			ImageView image = (ImageView) photoRow.findViewById(R.id.ImageView);
			image.setImageBitmap(imageFromUrl(photos[position].makeURL()));

			TextView imagevideoTitle = (TextView) photoRow
					.findViewById(R.id.TextView);
			imagevideoTitle.setText(photos[position].title);
			return photoRow;
		}

		public Bitmap imageFromUrl(String url) {
			Bitmap bitmapImage;

			URL imageUrl = null;
			try {
				imageUrl = new URL(url);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			try {
				HttpURLConnection httpConnection = (HttpURLConnection) imageUrl
						.openConnection();
				httpConnection.setDoInput(true);
				httpConnection.connect();
				InputStream is = httpConnection.getInputStream();

				bitmapImage = BitmapFactory.decodeStream(is);
			} catch (IOException e) {
				e.printStackTrace();
				bitmapImage = Bitmap.createBitmap(10, 10,
						Bitmap.Config.ARGB_8888);
			}
			return bitmapImage;
		}
	}

	class FlickrPhoto {
		String id;
		String owner;
		String secret;
		String server;
		String title;
		String farm;

		public FlickrPhoto(String _id, String _owner, String _secret,
				String _server, String _title, String _farm) {
			id = _id;
			owner = _owner;
			secret = _secret;
			server = _server;
			title = _title;
			farm = _farm;
		}

		public String makeURL() {
			return "http://farm" + farm + ".static.flickr.com/" + server + "/"
					+ id + "_" + secret + "_m.jpg";
			// http://farm{farm-id}.static.flickr.com/{server-id}/{id}_{secret}_[mstzb].jpg
			// From: http://www.flickr.com/services/api/misc.urls.html
		}
	}
}
