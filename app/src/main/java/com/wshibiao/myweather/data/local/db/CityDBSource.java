package com.wshibiao.myweather.data.local.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import com.wshibiao.myweather.data.bean.City;


/**
 * Created by wsb on 2016/4/26.
 */
public class CityDBSource {
    private static CityDBSource INSTANCE;

    private CityDBHelper mDbHelper;

    // Prevent direct instantiation.
    private CityDBSource(@NonNull Context context) {

        mDbHelper = new CityDBHelper(context);
    }

    public static CityDBSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new CityDBSource(context);
        }
        return INSTANCE;
    }




    public List<City> searchFromDB(String name) {
        List<City> cityLists=new ArrayList<City>();
        City city ;
        Cursor cursor;
        SQLiteDatabase db=mDbHelper.getReadableDatabase();
        //查询用户输入的是否是省名
        if ((cursor = db.rawQuery("select * from CityList where province LIKE ?"
                , new String[]{"%"+name+"%"})).moveToFirst())
        {

            do{
                city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setProvince(cursor.getString(cursor.getColumnIndex("province")));
                city.setCity(cursor.getString(cursor.getColumnIndex("city")));
                city.setDistrict(cursor.getString(cursor.getColumnIndex("district")));
                cityLists.add(city);
            }while (cursor.moveToNext());
        }
        //市县名
        else if((cursor=db.rawQuery("select * from CityList where city LIKE ?",
                new String[]{"%"+name+"%"})).moveToFirst()){
            do {
                city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setProvince(cursor.getString(cursor.getColumnIndex("province")));
                city.setCity(cursor.getString(cursor.getColumnIndex("city")));
                city.setDistrict(cursor.getString(cursor.getColumnIndex("district")));
                cityLists.add(city);
            }while (cursor.moveToNext());
        }
        //地区名
        else if((cursor=db.rawQuery("select * from CityList where district LIKE ?",
                new String[]{"%"+name+"%"})).moveToFirst()){
            do {
                city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setProvince(cursor.getString(cursor.getColumnIndex("province")));
                city.setCity(cursor.getString(cursor.getColumnIndex("city")));
                city.setDistrict(cursor.getString(cursor.getColumnIndex("district")));
                cityLists.add(city);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return cityLists;
    }



    public void saveCity(City city) {
        SQLiteDatabase db=mDbHelper.getReadableDatabase();
        if(city!=null) {
            try {
                db.execSQL("insert into CityList (id,province,city,district) values(?,?,?,?)"
                        , new Object[]{city.getId(),city.getProvince()
                        , city.getCity(),city.getDistrict()});
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
