package com.wshibiao.myweather.ui.weatherdetail;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.wshibiao.myweather.base.BaseActivity;
import com.wshibiao.myweather.data.bean.Forecast3thWeather;
import com.wshibiao.myweather.data.bean.WeatherInfo;
import com.wshibiao.myweather.data.local.cache.ACache;
import com.wshibiao.myweather.data.remote.NetWork;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wsb on 2016/5/1.
 */
public class WeatherDetailPresenter implements WeatherDetailContract.Presenter,AMapLocationListener {
    private static final String TAG = "WeatherDetailPresenter";
    private WeatherDetailContract.View mView;
    private static final String KEY = "6d660d7c817e5b57ab0afd636d336f3b";
    private double[] location=new double[2];
    private ACache aCache;
    SharedPreferences preferences;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;
    public WeatherDetailPresenter(WeatherDetailContract.View mView, Context context) {
        this.mView = mView;
        mView.setPresenter(this);
        aCache= BaseActivity.mCache;
        preferences=PreferenceManager.getDefaultSharedPreferences(context);
    }

    //创建并返回Observer对象

    public  Observer<WeatherInfo> setUpObserver(){
        return new Observer<WeatherInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(WeatherInfo weatherInfo) {
                mView.progressBarVisible();
                Log.d(TAG, "onNext: weather" + weatherInfo.result.sk.windDirection);
                aCache.put("weather_temp", weatherInfo, Integer.parseInt(preferences.getString("update_frequency", "180")) * 60 * 1000);
                mView.showWeather(weatherInfo);
                mView.show7Day(weatherInfo);
            }
        };
    }

    //今日小时天气
    @Override
    public void getForecast3h(String cityName){
        NetWork.getWeatherApi().getWeatherForecast3h(cityName, KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Forecast3thWeather>() {
                    @Override
                    public void onCompleted() {

                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: forecast3th",e );
                    }

                    @Override
                    public void onNext(Forecast3thWeather forecast3thWeather) {
                        mView.showForecast3th(forecast3thWeather);
                    }
                });
  }


    @Override
    public void getWeatherByCityName(String cityname) {

        NetWork.getWeatherApi().getWeatherInfo("2",cityname,KEY)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(setUpObserver());
    }

    @Override
    public void getCache() {
        WeatherInfo weatherInfo = null;
        try {
            weatherInfo = (WeatherInfo) aCache.getAsObject("weather_temp");
            Log.d(TAG, "getCache: weatherInfo"+weatherInfo.result.sk.windDirection);
        } catch (Exception e) {
            Log.e("CacheError aa", e.toString());
        }

        if (weatherInfo != null) {
            Log.d(TAG, "getCache: not null");
            mView.showWeather(weatherInfo);

        } else {
            mView.errorNetSnackbar();
        }
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //经度
                location[0] = aMapLocation.getLongitude();
                //纬度
                location[1] = aMapLocation.getLatitude();
                NetWork.getWeatherApi().getWeatherByLoc("2", KEY, String.valueOf(location[0]), String.valueOf(location[1]))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<WeatherInfo>() {
                            @Override
                            public void onCompleted() {
                                Log.d(TAG, "onCompleted: location");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "onError: location" + e.toString());
                                mView.errorLocSnackbar();
                            }

                            @Override
                            public void onNext(WeatherInfo weatherInfo) {
                                if (weatherInfo != null) {
                                    Log.d(TAG, "getCache: not null");
                                }
                                Log.d(TAG, "onNext:location " + weatherInfo.result.sk.windDirection);
                                mView.showWeather(weatherInfo);
                                aCache.put("weather_temp", weatherInfo, Integer.parseInt(preferences.getString("update_frequency", "180")) * 60 * 1000);
                            }
                        });
                Log.d(TAG, "onLocationChanged: location"+aMapLocation.getLongitude());
            }else{
                Log.e(TAG, "onLocationChanged:ErrorCode " + aMapLocation.getErrorCode());
                mView.errorNetSnackbar();
            }
        }
    }

    @Override
    public void getWeatherByGPS(final double[] location) {
        Log.d(TAG, "getWeatherByGPS:  11");
        NetWork.getWeatherApi().getWeatherByLoc("2", KEY, String.valueOf(location[0]), String.valueOf(location[1]))
                .subscribe(setUpObserver());
    }

    @Override
    public void location(Context context) {
        //初始化定位
        Log.d(TAG, "location: call with location");
        mLocationClient = new AMapLocationClient(context);
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        long frequency=Long.parseLong(BaseActivity.preferences.getString("update_frequency","60"));
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔 单位毫秒
        mLocationOption.setInterval(frequency*60*1000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();

    }



    @Override
    public void start( ){
        mView.progressBarVisible();
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               mView.progressBarGone();
            }
        },6000);

    }
}
