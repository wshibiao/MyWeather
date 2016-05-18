package com.wshibiao.myweather.base;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by wsb on 2016/4/28.
 */
public class BaseApplication extends Application {
    private static final String TAG = "BaseApplication";
    private static Context context;
    public static String cacheDir = "";



    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        //有内存卡则将cache写入内存卡
        if (getApplicationContext().getExternalCacheDir() != null && ExistSDCard()) {
            cacheDir = getApplicationContext().getExternalCacheDir().toString();
            Log.d(TAG, "onCreate have: "+cacheDir);

        } else {
            cacheDir = getApplicationContext().getCacheDir().toString();
            Log.d(TAG, "onCreate: "+cacheDir);
        }

    }
    public File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().toString();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }
    public static Context getContext(){
        return context;
    }


    private boolean ExistSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
}
