package com.wshibiao.myweather.data.remote;


import com.wshibiao.myweather.data.remote.api.CityApi;
import com.wshibiao.myweather.data.remote.api.WeatherApi;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wsb on 2016/4/28.
 */
public class NetWork {
    private static String HOST="http://v.juhe.cn/";
    private static CityApi cityApi=null;
    private static WeatherApi weatherApi=null;
    private static OkHttpClient okHttpClient=new OkHttpClient();

    public static CityApi getCityList(){
        Retrofit retrofit=new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        cityApi=retrofit.create(CityApi.class);
        return cityApi;
    }

    public static WeatherApi getWeatherApi(){
        Retrofit retrofit=new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        weatherApi=retrofit.create(WeatherApi.class);
        return weatherApi;
    }



}
