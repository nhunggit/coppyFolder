package com.example.coppyfolder;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.net.PasswordAuthentication;

//public class nhung extends SQLiteOpenHelper implements MainActivity.CallBackPass {
//    public static nhung instance;
//    public static final int DATABASE_VER = 1;
//    public static final String DATABASE_NAME="secure.db";
//    public static final String TABLE_NAME="TABLE_SECURE";
//    public static final String COLUMN_NAME="IMAGE";
//    private static final String SQL_CREATE_TABLE_QUERY="CREATE TABLE " + TABLE_NAME+" (" + COLUMN_NAME + "TEXT PRIMARY KEY)";
//    private static final String SQL_DELETE_TABLE_QUERY="DROP TABLE IF EXIT" + TABLE_NAME;
//    private String PASS="nhung123";
//    MainActivity.CallBackPass callBackPass=new MainActivity.CallBackPass() {
//        @Override
//        public void setPass(String pass) {
//
//        }
//    };
//    public nhung(@Nullable Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VER);
//    }
//
//    static public synchronized nhung getInstance(Context context){
//        if(instance == null)
//            instance = new nhung(context);
//        return instance;
//    }
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(SQL_CREATE_TABLE_QUERY);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//
//    }
//    public void insert(){
//        Log.d("nhungltk", "insert: "+PASS);
//        SQLiteDatabase db =instance.getWritableDatabase(PASS);
//    }
//
//    @Override
//    public void setPass(String pass) {
//        this.PASS=pass;
//    }
//}
