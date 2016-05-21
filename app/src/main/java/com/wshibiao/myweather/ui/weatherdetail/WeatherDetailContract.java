package com.wshibiao.myweather.ui.weatherdetail;

import android.content.Context;

import com.wshibiao.myweather.base.BasePresenter;
import com.wshibiao.myweather.base.BaseView;
import com.wshibiao.myweather.data.bean.Forecast3thWeather;
import com.wshibiao.myweather.data.bean.WeatherInfo;

/**
 * Created by wsb on 2016/5/1.
 */
public interface WeatherDetailContract {
    interface View extends BaseView<Presenter> {
        void showWeather(WeatherInfo weatherInfo);
        void showForecast3th(Forecast3thWeather forecast3thWeather);
        void show7Day(WeatherInfo weatherInfo) ;
        void progressBarVisible();
        void progressBarGone();
        void errorNetSnackbar();
        void errorLocSnackbar();
    }


    interface Presenter extends BasePresenter {
        void getForecast3h(String cityName);
        void getCache();
        void location(Context context);
        void getWeatherByCityName(String cityName);
        void getWeatherByGPS(double[] location);
    }
}
