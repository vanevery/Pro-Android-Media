package com.apress.proandroidmedia.ch12.flickrjsonlocation;

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
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FlickrJSONLocation extends Activity implements LocationListener {

	public static final String API_KEY = "YOUR_API_KEY";

	FlickrPhoto[] photos;
	TextView tv;
	LocationManager lm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		tv = (TextView) findViewById(R.id.TextView);
		tv.setText("Looking Up Location");

		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000l,
				500.0f, this);
	}

	public void onPause() {
		super.onPause();
		lm.removeUpdates(this);
	}

	public void onLocationChanged(Location location) {
		tv.setText(location.getLatitude() + " " + location.getLongitude());
		Log.v("LOCATION", "onLocationChanged: lat=" + location.getLatitude()
				+ ", lon=" + location.getLongitude());

		HttpClient httpclient = new DefaultHttpClient();

		String url = "http://api.flickr.com/services/rest/?method=flickr.photos.search&tags= dog,halloween&format=json&api_key="
				+ API_KEY
				+ "&per_page=5&nojsoncallback=1&accuracy=6&lat="
				+ location.getLatitude() + "&lon=" + location.getLongitude();
		HttpGet httpget = new HttpGet(url);

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
					photos[i] = new FlickrPhoto(photodata.getString("id"),
							photodata.getString("owner"), photodata
									.getString("secret"), photodata
									.getString("server"), photodata
									.getString("title"), photodata
									.getString("farm"));
					Log.v("URL", photos[i].makeURL());
				}

				inputstream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		ListView listView = (ListView) this.findViewById(R.id.ListView);
		listView.setAdapter(new FlickrGalleryAdapter(this, photos));
	}

	public void onProviderDisabled(String provider) {
	}

	public void onProviderEnabled(String provider) {
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
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
			View videoRow = inflater.inflate(R.layout.list_item, null);

			ImageView image = (ImageView) videoRow.findViewById(R.id.ImageView);
			image.setImageBitmap(imageFromUrl(photos[position].makeURL()));

			TextView videoTitle = (TextView) videoRow
					.findViewById(R.id.TextView);
			videoTitle.setText(photos[position].title);
			return videoRow;
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
				int length = httpConnection.getContentLength();
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
		}
	}
}
