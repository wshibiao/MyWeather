package com.wshibiao.myweather.data.local.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wsb on 2016/4/26.
 */
public class CityDBHelper extends SQLiteOpenHelper {


    private static final String CREATE_CITY="create table CityList("
            +"id integer primary key unique,"
            +"province text,"
            +"city text,"
            +"district text)";

    public CityDBHelper(Context context) {
        super(context,CREATE_CITY,null,1);

    }

    public CityDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CITY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
