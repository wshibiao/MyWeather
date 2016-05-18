package com.wshibiao.myweather.ui.preferences;

import android.content.Context;
import android.content.SharedPreferences;


import com.wshibiao.myweather.base.BaseApplication;

/**
 * Created by wsb on 2016/5/7.
 */
public class Settings {
    private SharedPreferences sharedPreferences;
    public static final String UPDATE_FREQUENCE="syn_frequency";
    public static final String AllOW_UPDATE="auto_update_weather";
    public static final String AllOW_GPS="GPS_switch";
    public static final String CITY="city";

    private static Settings sInstance;
    public static final String NOTIFICATION_MODEL = "notification_model";

    public static Settings getInstance() {
        if (sInstance == null) {
            sInstance = new Settings(BaseApplication.getContext());
        }
        return sInstance;
    }


    private Settings(Context context) {

        sharedPreferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE);


        //mPrefs.edit().putInt(CHANGE_ICONS, 1).apply();
    }

    public void setAutoUpdate(int t) {
        sharedPreferences.edit().putInt(UPDATE_FREQUENCE, t).apply();
    }

    public int getAutoUpdate() {
        return sharedPreferences.getInt(UPDATE_FREQUENCE,3);
    }
    public void setCity(String name) {
        sharedPreferences.edit().putString(CITY, name).apply();
    }

    public String getCity() {
        return sharedPreferences.getString(CITY, "北京");
    }



}
