package com.campus.sport.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/5/28.
 */

public class MySQLites extends SQLiteOpenHelper {
    private static SQLiteDatabase database;
    private String path="create table myPath("
            +"id integer primary key autoincrement,"
            +"path text,)";
    public MySQLites(Context context) {
        super(context, "myPath", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(path);
        database=this.getWritableDatabase();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public static void insertSql(String sql){
        ContentValues values=new ContentValues();
        values.put("path",sql);
        database.insert("myPath",null,values);
    }
    public static List<String> getAllSelect(){
        List<String>list=new ArrayList<>();
        Cursor cursor=database.query("myPath",null,null,null,null,null,null);
        while (cursor.moveToNext()){
            list.add(cursor.getString(cursor.getColumnIndex("path")));
        }
        return list;
    }
}
