package com.exam.sai.app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
     Button b1,b2;
     EditText t1,t2,t3;
    CheckBox c1;
    Database db;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        t1=(EditText)findViewById(R.id.editText);
        t2=(EditText)findViewById(R.id.editText4);
        t3=(EditText)findViewById(R.id.editText3);
        b2=(Button)findViewById(R.id.button2);
        t3.setVisibility(View.GONE);
        b2.setVisibility(View.GONE);
        db=new Database(this);
        click();
            check();
            signin();
    }
    public void click()
    {
        b1=(Button)findViewById(R.id.button);
       b1.setOnClickListener(
               new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       String username=t1.getText().toString();
                       String pass=t2.getText().toString();
                       Cursor res=db.login(username,pass);
                       if(res.getCount()==0)
                       {
                           Toast.makeText(MainActivity.this, "Enter your credentials correctlty", Toast.LENGTH_SHORT).show();
                       }
                       else
                       {
                           String sec="";
                           while (res.moveToNext()) {
                                sec = res.getString(0);
                           }
                           Intent intent=new Intent("com.exam.sai.app.Next");
                           Bundle b=new Bundle();
                           b.putString("sec",sec);
                           intent.putExtras(b);
                           startActivity(intent);
                       }
                   }
               }
       );
    }
    public void check()
    {
        c1=(CheckBox)findViewById(R.id.checkBox);
        c1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(((CheckBox)v).isChecked()){
                            t3.setVisibility(View.VISIBLE);
                            b2.setVisibility(View.VISIBLE);
                            b1.setVisibility(View.GONE);

                        }
                        else
                        {
                            t3.setVisibility(View.GONE);
                            b2.setVisibility(View.GONE);
                            b1.setVisibility(View.VISIBLE);

                        }
                    }
                }
        );
    }
    public void signin(){
        b2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        t3.setVisibility(View.GONE);
                        b2.setVisibility(View.GONE);
                        b1.setVisibility(View.VISIBLE);
                        c1.setChecked(false);
                        String username=t1.getText().toString();
                        String pass=t2.getText().toString();
                        String sec=t3.getText().toString();
                        boolean insert=db.insert(username,pass,sec);
                        if (insert==true)
                        {
                            t3.setVisibility(View.GONE);
                            b2.setVisibility(View.GONE);
                            b1.setVisibility(View.VISIBLE);
                            c1.setChecked(false);
                            Toast.makeText(MainActivity.this, "DATA INSERTED", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "DATA NOT INSERTED", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

}
