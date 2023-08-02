package com.websarva.wings.android.jankensample2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    //データベース名
    private static final String DATABASE_NAME = "jankenmemo.db";
    //データベースのバージョン
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //一番最初に1度だけ実行される
        //Log.i("DataBaseSample","onCreate 開始");
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE jankenmemos(");
        sb.append(" _id INTEGER PRIMARY KEY,");
        sb.append(" seiseki TEXT");
        sb.append(" );");
        String sql = sb.toString();
        db.execSQL(sql);
        //Log.i("DataBaseSample","onCreate 終了");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
