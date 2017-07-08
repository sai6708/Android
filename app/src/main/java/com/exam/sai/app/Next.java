package com.exam.sai.app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.concurrent.ExecutionException;

public class Next extends AppCompatActivity {
    Button b1,b2;
    Database bd;
    String sec;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);




        TextView tv1=(TextView)findViewById(R.id.textView3);
        b1=(Button)findViewById(R.id.button3);
        b2=(Button)findViewById(R.id.button4);
        Bundle bun=getIntent().getExtras();
      sec=bun.getString("sec");
        tv1.setText("your section is \n"+sec);
        submit();
       bd=new Database(this);
    }
    public void submit()
    {
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Next.this,Send.class);
                Bundle b=new Bundle();
                b.putString("sec",sec);
                in.putExtras(b);
                startActivity(in);
            }
        });
    }
  public void fetch(View view) throws ExecutionException {
      Server server = new Server(this);
      server.execute(sec);

  }
  }


