package com.abhyaas.app.testprep;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by root on 22/9/15.
 */
public class DownloadTask  extends AsyncTask<String, Void, BufferedReader> {


    @Override
    protected BufferedReader doInBackground(String... params) {
        BufferedReader in = null;
        try {
            URL url = new URL("http://abhyaas.co.in/images/a.txt");
            in = new BufferedReader(new InputStreamReader(url.openStream()));
        } catch (MalformedURLException e) {
            Log.d("text-url1", e.getMessage());
        } catch (IOException e) {
            Log.d("text-urle2", e.getMessage());
        }
        return in;
    }
}