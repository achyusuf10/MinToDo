package com.example.minitodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String TODO_TABLE = "TODO_TABLE";
    public static final String ROW_TODO_TITLE = "TODO_TITLE";
    public static final String ROW_TODO_DESC = "TODO_DESC";
    public static final String ROW_ID = "_id";
    public static final String ROW_TIME = "TODO_TIME";
    public static final String ROW_DATE = "TODO_DATE";

    private SQLiteDatabase db;

    public DBHelper(@Nullable Context context) {

        super(context, "todo.db", null,1);
        db = getWritableDatabase();
    }

    // this is called the first time a database is accessed
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TODO_TABLE + "(" + ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ROW_TODO_TITLE + " TEXT, " + ROW_TODO_DESC + " TEXT, " + ROW_DATE + " TEXT, " + ROW_TIME + " TEXT)";
        db.execSQL(query);
    }

    // this is called if database version number changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
    //Insert
    public void addOne(ContentValues cv){
        db.insert(TODO_TABLE, null, cv);
    }

    public Cursor getAll(){
        Cursor cur = db.rawQuery("SELECT * FROM " + TODO_TABLE + " ORDER BY " + ROW_ID + " DESC ",null);
        return cur;
    }

    public Cursor oneData(long id){
        Cursor cur = db.rawQuery("SELECT * FROM " + TODO_TABLE + " WHERE " + ROW_ID + "=" + id,null);
        return cur;
    }
    //Update
    public void updateData(ContentValues values,long id){
        db.update(TODO_TABLE,values, ROW_ID + "=" + id,null);
    }
    //Delete Data
    public void deleteData(long id){
        db.delete(TODO_TABLE, ROW_ID + "=" + id,null);
    }
}
