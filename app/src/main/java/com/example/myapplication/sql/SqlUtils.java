package com.example.myapplication.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myapplication.utils.SpUtils;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/*
 * Package    :com.example.myapplication.sql
 * ClassName  :SqlUtils
 * Description:music library
 * Data       :2020/5/25 14:16
 */
public class SqlUtils extends SQLiteOpenHelper {
  private String sql_name = "create table music(id integer primary key autoincrement,"
    + "_id int,"
    + "name text,"
    + "path text" + " )";

  public SqlUtils(Context context) {
    super(context, "music.db", null, 1);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    //path type name
    db.execSQL("create table if not exists m(_id integer primary key autoincrement , path text , type integer , name text)");
    db.execSQL("create table user(_id integer primary key autoincrement,num text,pass  text,level text)");
    db.execSQL(sql_name);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  }

  //add data
  public boolean add(String path, int type, String name) {
    if (isExists(name)) {
      return false;
    }
    SQLiteDatabase writableDatabase = getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put("path", path);
    values.put("type", type);
    values.put("name", name);
    writableDatabase.insert("m", null, values);
    writableDatabase.close();
    return true;
  }

  //get all data
  public List<MusicBean> getAll() {
    List<MusicBean> list = new ArrayList<>();
    SQLiteDatabase readableDatabase = getReadableDatabase();
    Cursor cursor = readableDatabase.rawQuery("select * from m", new String[]{});
    while (cursor.moveToNext()) {
      int id = cursor.getInt(cursor.getColumnIndex("_id"));
      int type = cursor.getInt(cursor.getColumnIndex("type"));
      String path = cursor.getString(cursor.getColumnIndex("path"));
      String name = cursor.getString(cursor.getColumnIndex("name"));
      list.add(new MusicBean(id, type, path, name));
    }
    cursor.close();
    readableDatabase.close();
    return list;
  }

  //judge if a data resource is exist
  private boolean isExists(String name) {
    SQLiteDatabase readableDatabase = getReadableDatabase();
    Cursor cursor = readableDatabase.rawQuery("select * from m where name=?", new String[]{name});
    boolean b = cursor.moveToFirst();
    cursor.close();
    readableDatabase.close();
    return b;
  }

  //delete data
  public void delete(int id) {
    SQLiteDatabase writableDatabase = getWritableDatabase();
    writableDatabase.delete("m", "_id=?", new String[]{id + ""});
    writableDatabase.close();
  }


  // add new user
  public boolean AddUser(String num, String pass) {
    SQLiteDatabase writableDatabase = getWritableDatabase();
    Cursor cursor = writableDatabase.rawQuery("select*from user where num=?", new String[]{num});
    boolean b = cursor.moveToFirst();
    if (b) {
      if (!num.equals("0"))
        Utils.showToast("用户已存在");
      cursor.close();
      writableDatabase.close();
      return false;
    }
    writableDatabase.execSQL("insert into user(num,pass,level) values(?,?,?)", new String[]{num, pass, "1"});
    writableDatabase.close();
    return true;
  }

  //get password
  public String findPass(String num) {
    SQLiteDatabase writableDatabase = getWritableDatabase();
    Cursor cursor = writableDatabase.rawQuery("select*from user where num=?", new String[]{num});
    boolean b = cursor.moveToFirst();

    if (b) {
      String pass = cursor.getString(cursor.getColumnIndex("pass"));
      cursor.close();
      writableDatabase.close();
      return pass;
    }
    cursor.close();
    writableDatabase.close();
    return "";
  }

  //get password
  public String findLevel() {
    SQLiteDatabase writableDatabase = getWritableDatabase();
    Cursor cursor = writableDatabase.rawQuery("select*from user where num=?", new String[]{SpUtils.getString(StringUtils.PHONE, "")});
    boolean b = cursor.moveToFirst();

    if (b) {
      String pass = cursor.getString(cursor.getColumnIndex("level"));
      cursor.close();
      writableDatabase.close();
      return pass;
    }
    cursor.close();
    writableDatabase.close();
    return "";
  }

  public void updateLevel(int level) {
    SQLiteDatabase writableDatabase = getWritableDatabase();
    writableDatabase.execSQL("update user set level = ? where num = ?", new String[]{level + "", SpUtils.getString(StringUtils.PHONE, "")});
  }
}
