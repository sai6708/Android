package com.exam.sai.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sai on 15-03-2017.
 */
public class Database extends SQLiteOpenHelper {
    public static final String database = "attdendance";
    public static final String tablename = "cr";
    public static final String tablename1 = "students";
    public static final String tablename2 = "subjects";
     Context cntx;
    public Database(Context context) {
        super(context, database, null, 1);
        cntx=context;
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + tablename + "(name char PRIMARY KEY,sec varchar,password varchar)");
        db.execSQL("create table " + tablename1 + "(name char PRIMARY KEY)");
        db.execSQL("create table " + tablename2 + "(subject char PRIMARY KEY)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + tablename);
        db.execSQL("DROP TABLE IF EXISTS" + tablename1);
        db.execSQL("DROP TABLE IF EXISTS" + tablename2);
        onCreate(db);
    }

    public Cursor login(String username, String pass) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select sec from cr where name='" + username + "' and password='" + pass + "'", null);
        return res;
    }
    public Cursor subject() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select *from "+tablename2, null);
        if(res.getCount()==0)
        {
            Toast.makeText(cntx,"Subjects:DATA NOT INSERTED",Toast.LENGTH_SHORT).show();
            return null;
        }
        else
        {
            Toast.makeText(cntx,"Subjects:DATA INSERTED",Toast.LENGTH_SHORT).show();

            return res;
        }
    }
    public void inserts(String result) throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        JSONObject jo=new JSONObject(result);
        JSONArray js = jo.getJSONArray("Subjects");
        for (int i = 0; i < js.length(); i++) {
            String k=js.getString(i).toString();
            cv.put("subject",k);
            db.insert(tablename2, null, cv);
        }
        subject();
    }

    public boolean insert(String username, String password, String sec) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", username);
        cv.put("sec", sec);
        cv.put("password", password);
        long res = db.insert(tablename, null, cv);
        if (res == -1) {
            return false;
        } else {
            return true;
        }
    }


    public void insert1(String result) throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        JSONObject jo=new JSONObject(result);
        JSONArray ja = jo.getJSONArray("Array");
        for (int i = 0; i < ja.length(); i++) {
            String k=ja.getString(i).toString();
            cv.put("name",k);
            db.insert(tablename1, null, cv);
        }
        inserts(result);
         get();
        }

        public Cursor get()
        {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select *from "+tablename1, null);
            if(res.getCount()==0)
            {
                Toast.makeText(cntx,"ROLL NUMBERS:DATA NOT INSERTED",Toast.LENGTH_SHORT).show();
                return null;
            }
            else
            {
                Toast.makeText(cntx,"ROLL NUMBERS:DATA INSERTED",Toast.LENGTH_SHORT).show();
                return res;
            }
        }
    }

