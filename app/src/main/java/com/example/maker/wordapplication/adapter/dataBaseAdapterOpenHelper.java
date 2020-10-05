package com.example.maker.wordapplication.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class dataBaseAdapterOpenHelper extends SQLiteOpenHelper {
    public dataBaseAdapterOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //word 单词，wordMean 单词意思，times 出现的频率
        String sql="create table words (id integer primary key autoincrement,word varchar(20),wordMean varchar(200),times integer)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    //获取所有单词列表
    public List<Map<String,String>> getWordList( SQLiteDatabase database){
        List<Map<String,String>> list = new ArrayList<>();
        Cursor cursor = database.query("words",null,null,null,null,null,null);
        while(cursor.moveToNext()){
            Map<String,String> map = new HashMap<>();
            map.put("word",cursor.getString(cursor.getColumnIndex("word")));
            map.put("wordMean",cursor.getString(cursor.getColumnIndex("wordMean")));
            map.put("times",cursor.getString(cursor.getColumnIndex("times")));
            list.add(map);
        }
        return list;
    }
    public boolean deleteWord(String word,SQLiteDatabase db){
        boolean flag = false;
        int num;
        num = (int) db.delete("words","word=?",new String[]{word});
        if(num>0){
            flag = true;
        }
        return flag;

    }
    //插入一个单词，times默认为1
    public boolean insertWord(String word,String wordMean,SQLiteDatabase db){
        boolean flag = false;
        int num;
        ContentValues values = new ContentValues();
        values.put("word", word);
        values.put("wordMean", wordMean);
        values.put("times", 1);
        num = (int) db.insert("words",null,values);
        if(num>0){
            flag = true;
        }
        return flag;

    }

    public boolean getWordExist(String name,SQLiteDatabase db){
        boolean flag = false;
        Cursor cursor = db.query("words",null,"word=?",new String[]{name},null,null,null);
        if(cursor.getCount()!=0){
            flag = true;
        }
        cursor.moveToNext();

        Log.e("txhLog","times="+cursor.getCount());
        return flag;
    }
    public boolean updateWordTimes(String name,SQLiteDatabase db){

            boolean flag = false;
            int times = 0;
            String wordMean="";
        Cursor cursor = db.query("words",null,"word=?",new String[]{name},null,null,null);
       while (cursor.moveToNext()){
           times = cursor.getInt(cursor.getColumnIndex("times"));
           wordMean = cursor.getString(cursor.getColumnIndex("wordMean"));
        }

       times = times+1;
        Log.e("txhvalue","times"+times+"wordmean"+wordMean);
        ContentValues values = new ContentValues();
        values.put("word",name);
        values.put("wordMean",wordMean);
        values.put("times",times);
        int num=db.update("words",values,"word=?",new String[]{name});
        Log.e("txhLog","update"+num);
        if(num>0){
            flag = true;
        }
            return flag;
    }


}
