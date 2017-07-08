package com.exam.sai.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class Send extends AppCompatActivity {
      ListView list;
    Aadapter adapter;
    Button button;
   public HashMap<String, Boolean> map= null;
    ArrayList<String> abscent= null;
    String sec=null;
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        Bundle bun=getIntent().getExtras();
        sec=bun.getString("sec");
        Database bd=new Database(this);
        Cursor result=bd.get();
        ArrayList<String> num= new ArrayList<String>();
        if(result==null) {
            Toast.makeText(Send.this,"NO DATA FOUND IN DATA-BASE, PLEASE FETCH THE DATA.",Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(result.getCount()!=0) {
                while (result.moveToNext()) {
                    num.add(result.getString(0));
                }
            }
        list= (ListView) findViewById(R.id.listView);
      adapter=new Aadapter(this,num);
        list.setAdapter(adapter);
      button= (Button) findViewById(R.id.button7);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map=new HashMap<>();
                abscent=new ArrayList<String>();
                map=adapter.abscent;
                Set set = map.entrySet();
                Iterator iterator = set.iterator();
                while(iterator.hasNext()) {
                    Map.Entry mentry2 = (Map.Entry) iterator.next();
                    abscent.add(mentry2.getKey().toString());
                }
                for (int j=0; j<abscent.size();j++)
                {
                    System.out.println(abscent.get(j)+"        thurrr");
                }
                if(abscent.isEmpty())
                {
                    Toast.makeText(Send.this,"Mark the abscent",Toast.LENGTH_SHORT).show();
                }
                else{
                    String[] arr = new String[abscent.size()];
                    arr = abscent.toArray(arr);
                    Intent in = new Intent(Send.this,Submit.class);
                    Bundle b=new Bundle();
                    b.putStringArray("arr",arr);
                    b.putString("sec",sec);
                    in.putExtras(b);
                    startActivity(in);
                    abscent.clear();
                    arr=null;
                }
            }
        });
        }
    }


class Aadapter extends BaseAdapter
{
    Context context;
    ArrayList<String> list;
    private boolean[] checkBoxState = null;
    public HashMap<String, Boolean> abscent= new HashMap<>(); ;

    Aadapter(Context context,ArrayList<String> list)
    {
        this.context=context;
        this.list=list;

    }
    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Viewholder holder=null;
        if (convertView == null) {
            LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.checkboxs, parent, false);
            holder=new Viewholder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder= (Viewholder) convertView.getTag();
        }
        holder.ch.setText(list.get(position));
        checkBoxState = new boolean[list.size()];
        /** checkBoxState has the value of checkBox ie true or false,
         * The position is used so that on scroll your selected checkBox maintain its state **/
        if(checkBoxState != null)
        {  holder.ch.setChecked(checkBoxState[position]);   }
            final String rollnumber=list.get(position);
        holder.ch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked())
                {
                    checkBoxState[position] = true;
                    abscent.put(rollnumber,true);
                }
                else {
                    checkBoxState[position] = false;
                    abscent.remove(rollnumber);
                }
            }
        });
        /**if country is in checkedForNumber then set the checkBox to true **/
        if (abscent.get(rollnumber) != null) {
            holder.ch.setChecked(abscent.get(rollnumber));
        }
        return convertView;
    }
    class Viewholder
    {
        CheckBox ch;
        Viewholder(View v)
        {
            ch= (CheckBox) v.findViewById(R.id.checkBox1);
        }
    }

}
}