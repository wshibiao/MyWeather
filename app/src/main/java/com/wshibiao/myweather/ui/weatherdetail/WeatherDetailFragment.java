package com.wshibiao.myweather.ui.weatherdetail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wshibiao.myweather.R;
import com.wshibiao.myweather.base.BaseActivity;
import com.wshibiao.myweather.base.BaseFragment;
import com.wshibiao.myweather.base.RxBus;
import com.wshibiao.myweather.data.AutoUpdateWeatherService;
import com.wshibiao.myweather.data.bean.Forecast3thWeather;
import com.wshibiao.myweather.data.bean.WeatherInfo;
import com.wshibiao.myweather.ui.choosecity.ChooseCityActivity;
import com.wshibiao.myweather.ui.choosecity.ChooseCityFragment;
import com.wshibiao.myweather.util.Utility;
import com.wshibiao.myweather.util.WeatherIcon;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by wsb on 2016/5/18.
 */
public class WeatherDetailFragment extends BaseFragment implements WeatherDetailContract.View, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "WeatherDetailFragment";
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.weather)
    TextView weatherTv;
    @Bind(R.id.temperature)
    TextView temperature;
    @Bind(R.id.week)
    TextView week;
    @Bind(R.id.today)
    TextView today;
    @Bind(R.id.temp_range)
    TextView tempRange;
    @Bind(R.id.tv_felt_air_temp)
    TextView tvFeltAirTemp;
    @Bind(R.id.tv_humidity)
    TextView tvHumidity;
    @Bind(R.id.tv_drying_index)
    TextView tvDryingIndex;
    @Bind(R.id.tv_wind)
    TextView tvWind;
    @Bind(R.id.tv_uv_index)
    TextView tvUvIndex;
    @Bind(R.id.comfortable)
    TextView comfortable;
    @Bind(R.id.tv_dressing_index)
    TextView tvDressingIndex;
    @Bind(R.id.tv_dressing_advice_index)
    TextView tvDressingAdviceIndex;
    @Bind(R.id.tv_wash_index)
    TextView tvWashIndex;
    @Bind(R.id.tv_travel_index)
    TextView tvTravelIndex;
    @Bind(R.id.tv_exercise_index)
    TextView tv5Index;
    @Bind(R.id.weather_layout)
    LinearLayout weatherLayout;
    @Bind(R.id.id_gallery)
    LinearLayout idGallery;
    @Bind(R.id.forecast7)
    LinearLayout forecast7;
    @Bind(R.id.my_refresh)
    SwipeRefreshLayout refresh;
    private WeatherDetailContract.Presenter presenter;
    private RxBus _rxBus;
    private WeatherInfo weather = new WeatherInfo();

    public static WeatherDetailFragment newInstance() {
        WeatherDetailFragment fragment = new WeatherDetailFragment();
        return fragment;
    }

    public WeatherDetailFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
        _rxBus = ((BaseActivity) mActivity).getRxBusSingleton();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weather_detail_fragment, container, false);
        ButterKnife.bind(this, view);
        refresh.setSize(SwipeRefreshLayout.LARGE);
        refresh.setColorSchemeResources(R.color.blue, R.color.green, R.color.orange);
        refresh.setOnRefreshListener(this);

        if (Utility.isNetworkConnected(mActivity)) {
            if (BaseActivity.preferences.getBoolean("GPS_switch", true)) {
                Log.d(TAG, "onCreateView: by gps");
                presenter.location(mActivity);
            } else {
                refresh.setRefreshing(true);
                Log.d(TAG, "onCreateView: by cityname");
                timer(BaseActivity.settings.getCity());
            }

        } else {
            Log.d(TAG, "onCreateView: by cache");
            presenter.getCache();
        }

        return view;


    }

    /**
     * 后台定时更新任务，以及选择城市事件的观察者， 将它们添加进CompositeSubscription后，保证在fragment生命周期结束后结束订阅
     */
    @Override
    public void onStart() {
        super.onStart();

        subscription=new CompositeSubscription();
        Observable<Object> updateEvent = _rxBus.toObserverable().share();
        subscription//
                .add(updateEvent.subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object event) {
                        if (event instanceof AutoUpdateWeatherService.Update) {
                            presenter.getWeatherByCityName(BaseActivity.settings.getCity());
                            presenter.getForecast3h(BaseActivity.settings.getCity());
                        }
                    }
                }));

        Observable<Object> cityEvent = _rxBus.toObserverable().share();
        subscription.add(cityEvent.subscribe(new Action1<Object>() {
            @Override
            public void call(final Object o) {
                if (o instanceof ChooseCityFragment.CityEvent) {
                    if (Utility.isNetworkConnected(mActivity)) {
                        refresh.setRefreshing(true);
                       timer(((ChooseCityFragment.CityEvent) o).msg);
                        Log.d(TAG, "call: rxbus" + ((ChooseCityFragment.CityEvent) o).msg);

                    } else {
                        Snackbar.make(refresh, "无网络", Snackbar.LENGTH_INDEFINITE).setAction("重试", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                timer(((ChooseCityFragment.CityEvent) o).msg);
                            }
                        }).show();
                    }
                }
            }
        }));
    }

    @Override
    public void onRefresh() {
        if (_rxBus.hasObservers()) {
            _rxBus.send(new ReplaceBg());
        }
       timer(BaseActivity.settings.getCity());
    }

    /**
     * 定时任务，三秒后加载数据，体验更好
     */
    private void timer(final String cityName){
        Observable.timer(3000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        presenter.getWeatherByCityName(cityName);
                        presenter.getForecast3h(cityName);
                        refresh.setRefreshing(false);
                    }
                });
    }

    @Override
    public void errorNetSnackbar() {
        Snackbar.make(refresh, "无网络", Snackbar.LENGTH_INDEFINITE).setAction("重试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getWeatherByCityName(BaseActivity.settings.getCity());
            }
        });
    }

    @Override
    public void errorLocSnackbar(){
        Snackbar.make(refresh, "定位失败", Snackbar.LENGTH_INDEFINITE).setAction("手动选择城市", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.startActivity(new Intent(mActivity, ChooseCityActivity.class));
            }
        });
    }


    public synchronized void handleTodayWeatherResponse(WeatherInfo weather) {
        BaseActivity.settings.setCity(weather.result.today.city);
        Log.d("onBindViewHolder", weather.result.sk.temp + "hahah");
        temperature.setText(weather.result.sk.temp + "℃");
        weatherTv.setText(weather.result.today.weather);
        time.setText(weather.result.sk.time + "更新");


    }

    /**
     * @param weatherInfo 天气信息
     *  动态添加未来七天的天气，并为每个子View设置点击事件
     */
    @Override
    public void show7Day(WeatherInfo weatherInfo) {
        Log.d(TAG, "show7Day: size" + weatherInfo.result.future.size());
        forecast7.removeAllViews();
        for (int i = 0; i < weatherInfo.result.future.size(); i++) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.item_forecast_line, forecast7, false);
            TextView indexFutureWeek = (TextView) view.findViewById(R.id.index_future_week);
            ImageView indexFutureWeather = (ImageView) view.findViewById(R.id.index_future_weather);
            TextView indexFutureTemperature = (TextView) view.findViewById(R.id.index_future_temperature);
            indexFutureWeek.setText(weather.result.future.get(i).week);
            int id = WeatherIcon.weatherIcon(weather.result.future.get(i).weatherId.fa);
            indexFutureWeather.setImageResource(id);
            indexFutureTemperature.setText(weather.result.future.get(i).temperature);
            final WeatherInfo.Result.Sk.FutureList future = weather.result.future.get(i);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(refresh, future.weather+" "+future.wind, Snackbar.LENGTH_LONG).show();
                }
            });
            forecast7.addView(view);
        }
    }


    @Override
    public void showWeather(WeatherInfo weatherInfo) {
        Log.d(TAG, "showWeather: ");
        handleTodayWeatherResponse(weatherInfo);
        weather.result = weatherInfo.result;

    }
    /**
    *   处理获得的今日小时天气信息
     */
    @Override
    public void showForecast3th(Forecast3thWeather forecast3thWeather) {
        Log.d(TAG, "showForecast3th: size" + forecast3thWeather.result.size());
        Log.d(TAG, "showForecast3th: icon" + forecast3thWeather.result.get(0).weatherid);
        idGallery.removeAllViews();
        for (int i = 0; i < forecast3thWeather.result.size(); i++) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.item_forecast3h_weather, idGallery, false);

            TextView indexTime = (TextView) view.findViewById(R.id.index_time);
            TextView indexTemperature = (TextView) view.findViewById(R.id.index_temperature);
            ImageView indexWeather = (ImageView) view.findViewById(R.id.index_weather);

            indexTime.setText(forecast3thWeather.result.get(i).sh);
            indexWeather.setImageResource(WeatherIcon.weatherIcon(forecast3thWeather.result.get(i).weatherid));
            indexTemperature.setText(forecast3thWeather.result.get(i).temp2);
            idGallery.addView(view);
        }

    }

    @Override
    public void progressBarVisible() {

    }

    @Override
    public void progressBarGone() {

    }

    @Override
    public void setPresenter(WeatherDetailContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public static class ReplaceBg {

    }
}
