package com.wshibiao.myweather.data.remote.api;


import com.wshibiao.myweather.data.bean.Forecast3thWeather;
import com.wshibiao.myweather.data.bean.WeatherInfo;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wsb on 2016/5/7.
 */
public interface WeatherApi {
    @GET("/weather/index?/")
    Observable<WeatherInfo> getWeatherInfo(@Query("format") String format
            , @Query("cityname") String cityname, @Query("key") String key);
    @GET("/weather/geo?/")
    Observable<WeatherInfo> getWeatherByLoc(@Query("format") String format, @Query("key") String key
            , @Query("lon") String lon, @Query("lat") String lat);
    @GET("/weather/forecast3h.php?")
    Observable<Forecast3thWeather> getWeatherForecast3h( @Query("cityname") String cityname, @Query("key") String key);
}
