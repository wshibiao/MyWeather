package com.wshibiao.myweather.ui.weatherdetail;

import android.content.Context;

import com.wshibiao.myweather.base.BasePresenter;
import com.wshibiao.myweather.base.BaseView;
import com.wshibiao.myweather.data.bean.WeatherInfo;

/**
 * Created by wsb on 2016/5/1.
 */
public interface WeatherDetailContract {
    interface View extends BaseView<Presenter> {
        void showWeather(WeatherInfo weatherInfo);
        void progressBarVisible();
        void progressBarGone();
        void errorNetSnackbar();
    }


    interface Presenter extends BasePresenter {
//        public Observer<WeatherInfo> setUpObserver();
        void getCache();
        void location(Context context);
        void getWeatherByCityName(String string);
        void getWeatherByGPS(double[] location);
    }
}
