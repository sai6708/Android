package com.exam.sai.app;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Objects;

/**
 * Created by sai on 28-03-2017.
 */


public class Server extends AsyncTask<String,Void,String > {
     Context context;
     Server (Context ctx)
     {
        context=ctx;
     }
     String result="";
    Database bd;
    String  error="";
    @Override
    protected String   doInBackground(String... params) {
        String login_url="http://baskiloman.com/fetch.php";
      try {
          String sec=params[0];
          System.out.println(sec);
          URL url = new URL(login_url);
          HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
          httpURLConnection.setRequestMethod("POST");
          httpURLConnection.setDoOutput(true);
          httpURLConnection.setDoInput(true);
          OutputStream outputStream = httpURLConnection.getOutputStream();
          BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
          String post_data = URLEncoder.encode("sec","UTF-8")+"="+URLEncoder.encode(sec,"UTF-8");
          System.out.println("sent request");
          bufferedWriter.write(post_data);
          bufferedWriter.flush();
          bufferedWriter.close();
          outputStream.close();
          InputStream inputStream = httpURLConnection.getInputStream();
          BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));

          String line = "";
          while ((line = bufferedReader.readLine()) != null) {
              result += line;
          }
          System.out.println(result);
          JSONObject jo=new JSONObject(result);
          error=jo.getString("error");
          System.out.println(error);
          if(error.equals("null"))
          {
              JSONArray ja = jo.getJSONArray("Array");
              JSONArray js=jo.getJSONArray("Subjects");
              for (int i = 0; i < ja.length(); i++) {
                  String k = ja.getString(i).toString();
                  System.out.println(k);
              }
              for (int i = 0; i < js.length(); i++) {
                  String k = js.getString(i).toString();
                  System.out.println(k);
              }


          }

      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      } catch (ProtocolException e) {
          e.printStackTrace();
      } catch (MalformedURLException e) {
          e.printStackTrace();
      } catch (IOException e) {
          e.printStackTrace();
      } catch (JSONException e) {
          e.printStackTrace();
      }
        return null;
    }

    @Override
    protected void onPreExecute() {

        Toast.makeText(context,"Process initiated",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(String s) {
        if(!error.equals("null"))
        Toast.makeText(context,error,Toast.LENGTH_LONG).show();
        else
        {
            bd=new Database(context);
            try {
                bd.insert1(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
