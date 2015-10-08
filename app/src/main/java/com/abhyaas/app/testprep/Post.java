package com.abhyaas.app.testprep;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by root on 8/10/15.
 */
public class Post  extends AsyncTask<String, Void, String> {


    @Override
    protected String doInBackground(String... params) {
        String msg = "empty";
        try {
            msg = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8");
            msg += "&" + URLEncoder.encode("data", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8");
            URL url = new URL("http://abhyaas.co.in/android.php");

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( msg );
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            while((line = reader.readLine()) != null)
            {
                // Append server response in string
                sb.append(line + "\n");
            }
            msg = sb.toString();

        }
        catch (UnsupportedEncodingException e) {
            Log.d("post",e.toString());
        }
        catch (MalformedURLException e) {
            Log.d("post", e.toString());
        } catch (IOException e) {
            Log.d("post",e.toString());
        }


        return msg;
    }


}