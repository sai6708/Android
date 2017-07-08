package com.exam.sai.app;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Submit extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    Spinner spinner,spinner2;
     EditText sign;
   String sec=null;
     String[] abscent=null;
    Button upload;
     String  hour,subject,fsign=null;
    ArrayList<String> sub=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        CheckBox ch= (CheckBox) findViewById(R.id.checkBox2);
        final TextView tx1= (TextView) findViewById(R.id.textView4);
        final TextView tx2= (TextView) findViewById(R.id.textView5);
        spinner= (Spinner) findViewById(R.id.spinner);
        spinner2=(Spinner)findViewById(R.id.spinner2);
        sign= (EditText) findViewById(R.id.editText5);
        upload= (Button) findViewById(R.id.button5);
        tx1.setMovementMethod(new ScrollingMovementMethod());
        ArrayAdapter adapter=ArrayAdapter.createFromResource(Submit.this,R.array.hours,android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setVisibility(View.INVISIBLE);
        Database bd=new Database(this);
        Cursor result=bd.subject();
        if(result==null) {
            Toast.makeText(Submit.this,"NO DATA FOUND IN DATA-BASE, PLEASE FETCH THE DATA.",Toast.LENGTH_SHORT).show();
        }
        else {
            if (result.getCount() != 0) {
                while (result.moveToNext()) {
                    sub.add(result.getString(0));
                }
            }
        }
        for (int i=0;i<sub.size();i++)
        {
            System.out.println(sub.get(i));
        }
        ArrayAdapter<String> adapter2=new ArrayAdapter<String>(Submit.this,R.layout.support_simple_spinner_dropdown_item,sub);
        adapter2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(this);
        upload.setVisibility(View.INVISIBLE);
        sign.setVisibility(View.INVISIBLE);
        spinner2.setVisibility(View.INVISIBLE);
        Bundle bun=getIntent().getExtras();
        abscent= bun.getStringArray("arr");
        sec=bun.getString("sec");
        for(String s:abscent)
        {
            System.out.println(s);
            tx1.append(s);
            tx1.append("\n");
        }
          ch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
              @Override
              public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                  if(isChecked)
                  {
                      tx1.setVisibility(View.GONE);
                      tx2.setVisibility(View.GONE);

                      spinner.setVisibility(View.VISIBLE);
                      upload.setVisibility(View.VISIBLE);
                      sign.setVisibility(View.VISIBLE);
                      spinner2.setVisibility(View.VISIBLE);
                  }
                  else
                  {
                      tx1.setVisibility(View.VISIBLE);
                      tx2.setVisibility(View.VISIBLE);

                      spinner.setVisibility(View.INVISIBLE);
                      upload.setVisibility(View.INVISIBLE);
                      sign.setVisibility(View.INVISIBLE);
                      spinner2.setVisibility(View.INVISIBLE);
                  }

              }
          });

set();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView mytext= (TextView) view;

        switch (parent.getId())
        {
            case R.id.spinner : {hour=mytext.getText().toString();
                System.out.println(hour);
            break;
            }
            case R.id.spinner2 : {subject=mytext.getText().toString();
                System.out.println(subject);
                break;
        }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

        public void set()
        {

            upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fsign=sign.getText().toString();

                    if(fsign.isEmpty() || hour.equals("Select the hour") || subject.equals("a"))
                    {

                        Toast.makeText(Submit.this,"Enter All The details",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        System.out.println("here"+fsign);
                        Upload up=new Upload(Submit.this,abscent,hour,subject,fsign,sec);
                        up.execute();
                    }
                }
            });

        }

}




 class  Upload extends AsyncTask<String ,Void,Void>
{
    Context context;
    String[] abscent=null;
    String hour,subject,fsign,result="",sec="";

    public Upload(Context ctx,String[] abscent,String hour,String subject,String fsign,String sec)
    {
        context=ctx;
        this.abscent=abscent;
        this.hour=hour;
        this.subject=subject;
        this.fsign=fsign;
        this.sec=sec;
    }



    @Override
    protected Void doInBackground(String... params) {

        try
        {
            URL url = new URL("http://baskiloman.com/upload.php");
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);
        OutputStream outputStream = httpURLConnection.getOutputStream();
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("hour","UTF-8")+"="+URLEncoder.encode(hour,"UTF-8")+"&"+URLEncoder.encode("subject","UTF-8")+"="+URLEncoder.encode(subject,"UTF-8")+"&"+URLEncoder.encode("fsign","UTF-8")+"="+URLEncoder.encode(fsign,"UTF-8")+"&"+URLEncoder.encode("abscent","UTF-8")+"="+URLEncoder.encode(java.util.Arrays.toString(abscent) ,"UTF-8")+"&"+URLEncoder.encode("sec","UTF-8")+"="+URLEncoder.encode(sec,"UTF-8");;
            System.out.println(post_data);
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

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        Toast.makeText(context,"started uploading",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        System.out.println("result="+result);
        Toast.makeText(context,result,Toast.LENGTH_SHORT).show();
    }


}
