package com.apress.proandroidmedia.ch12.simplehttprequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class SimpleHTTPRequest extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet("http://www.apress.com/book/view/9781430232674");

        try {
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                InputStream inputstream = entity.getContent();
                BufferedReader bufferedreader =
                  new BufferedReader(new InputStreamReader(inputstream));
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
                Log.v("HTTP REQUEST",result);
                inputstream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
