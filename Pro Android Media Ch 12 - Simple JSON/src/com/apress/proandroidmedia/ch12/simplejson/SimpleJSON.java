package com.apress.proandroidmedia.ch12.simplejson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class SimpleJSON extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		String JSONData = ""
				+ "{\"results\":{\"aname\":\"value\", \"anumber\":1234, \"aboolean\":false, "
				+ "\"anarray\":[{\"arrayelement\":\"Array Element 1\"}, {\"arrayelement\":\"Array Element 2\"}]}}";

		try {
			JSONObject overallJSONObject = new JSONObject(JSONData);
			Log.v("SIMPLEJSON", overallJSONObject.toString());

			JSONObject resultsObject = overallJSONObject
					.getJSONObject("results");
			Log.v("SIMPLEJSON", resultsObject.toString());

			String aname = resultsObject.getString("aname");
			Log.v("SIMPLEJSON", aname);

			int anumber = resultsObject.getInt("anumber");
			Log.v("SIMPLEJSON", "" + anumber);

			boolean aboolean = resultsObject.getBoolean("aboolean");
			Log.v("SIMPLEJSON", "" + aboolean);

			JSONArray anarray = resultsObject.getJSONArray("anarray");
			for (int i = 0; i < anarray.length(); i++) {
				JSONObject arrayElementObject = anarray.getJSONObject(i);
				String arrayelement = arrayElementObject
						.getString("arrayelement");
				Log.v("SIMPLEJSON", arrayelement);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
