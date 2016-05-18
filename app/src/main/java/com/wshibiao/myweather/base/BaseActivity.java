package com.wshibiao.myweather.base;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.wshibiao.myweather.data.local.cache.ACache;
import com.wshibiao.myweather.ui.preferences.Settings;

public class BaseActivity extends AppCompatActivity {
    public static SharedPreferences preferences;
    public static Settings settings;
    public static ACache mCache;
    private static RxBus _rxBus=null;
  public static RxBus getRxBusSingleton() {
        if (_rxBus == null) {
            _rxBus = new RxBus();
        }

        return _rxBus;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences= PreferenceManager.getDefaultSharedPreferences(this);
        settings= Settings.getInstance();
        mCache=ACache.get(this);
    }
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    private boolean canMakeSmores(){
        return (Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1);
    }
}
