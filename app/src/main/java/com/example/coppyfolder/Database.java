package com.example.coppyfolder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="secure.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME="TABLE_SECURE";
    public static final String COLUMN_PATH_ENCRYPT="PATH_ENCRYPT";
    public static final String COLUMN_PATH_DECRYPT="PATH_DECRYPT";
    public static final String COLUMN_ID= "COLUMN_ID";
    private static final String SQL_CREATE_TABLE_QUERY="CREATE TABLE " + TABLE_NAME + " ( " + COLUMN_ID + " INT AUTO_INCREMENT NOT NULL, " + COLUMN_PATH_ENCRYPT + " VARCHAR (255) NOT NULL, " + COLUMN_PATH_DECRYPT  + " VARCHAR (255) NOT NULL )";
    private static Database instance;
    private String PASS_DB= "nhung123";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_CREATE_TABLE_QUERY);
            onCreate(db);
    }
    static public synchronized Database getInstance(Context context){
        if (instance==null){
            instance= new Database(context);
        }
        return instance;

    }

    public void insertPath(String pathEncrypt, String pathDecrypt){
        SQLiteDatabase sqLiteDatabase= instance.getWritableDatabase(PASS_DB);

        ContentValues contentValues= new ContentValues();
        contentValues.put(COLUMN_PATH_ENCRYPT,pathEncrypt);
        contentValues.put(COLUMN_PATH_DECRYPT,pathDecrypt);
        sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
        sqLiteDatabase.close();
    }

    public void deletePath(String pathEncrypt, String pathDecrypt){
        SQLiteDatabase sqLiteDatabase= instance.getWritableDatabase(PASS_DB);
        ContentValues contentValues= new ContentValues();
        contentValues.put(COLUMN_PATH_DECRYPT, pathDecrypt);
        contentValues.put(COLUMN_PATH_ENCRYPT, pathEncrypt);
        sqLiteDatabase.delete(TABLE_NAME, COLUMN_PATH_ENCRYPT + "='" + pathEncrypt + "'", null);
        sqLiteDatabase.close();
    }

    public List<String> getList(){
        List<String> list= new ArrayList();
        SQLiteDatabase sqLiteDatabase= instance.getWritableDatabase(PASS_DB);
        Cursor cursor= sqLiteDatabase.rawQuery(String.format("SELECT * FROM '%s';",TABLE_NAME), null);
        while (cursor.moveToFirst()){
            if(cursor.isAfterLast()){
                String path= cursor.getString(cursor.getColumnIndex(COLUMN_PATH_ENCRYPT));
                list.add(path);
                cursor.moveToNext();
            }
        }
        cursor.close();
        sqLiteDatabase.close();

        return list;
    }

    public void delete(Context context){
        context.deleteDatabase(DATABASE_NAME);
    }
}
