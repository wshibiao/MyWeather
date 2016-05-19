package com.wshibiao.myweather.ui.weatherdetail;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wshibiao.myweather.R;
import com.wshibiao.myweather.base.BaseActivity;
import com.wshibiao.myweather.base.BaseFragment;
import com.wshibiao.myweather.base.RxBus;
import com.wshibiao.myweather.data.AutoUpdateWeatherService;
import com.wshibiao.myweather.data.bean.WeatherInfo;
import com.wshibiao.myweather.ui.choosecity.ChooseCityFragment;
import com.wshibiao.myweather.util.Utility;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by wsb on 2016/5/18.
 */
public class WeatherDetailFragment extends BaseFragment implements WeatherDetailContract.View,SwipeRefreshLayout.OnRefreshListener{
    private static final String TAG = "WeatherDetailFragment";
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.cityName)
    TextView cityName;
    @Bind(R.id.weather)
    TextView weather;
    @Bind(R.id.temperature)
    TextView temperature;
    @Bind(R.id.week)
    TextView week;
    @Bind(R.id.today)
    TextView today;
    @Bind(R.id.temp_range)
    TextView tempRange;
    @Bind(R.id.id_gallery)
    LinearLayout idGallery;
    @Bind(R.id.future_listview)
    ListView futureListview;
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

    private SwipeRefreshLayout refresh;
    private CompositeSubscription _subscriptions;
    private WeatherDetailContract.Presenter presenter;
    private RxBus _rxBus;

    public static WeatherDetailFragment newInstance() {
        WeatherDetailFragment fragment = new WeatherDetailFragment();

        return fragment;
    }


    public WeatherDetailFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weather_detail_fragment, container, false);
        refresh=(SwipeRefreshLayout) view.findViewById(R.id.my_refresh);
        refresh.setSize(SwipeRefreshLayout.LARGE);
        refresh.setOnRefreshListener(this);
      presenter.start();
        if (Utility.isNetworkConnected(mActivity)) {
            if (BaseActivity.preferences.getBoolean("GPS_switch", true)) {
                Log.d(TAG, "onCreateView: by gps");
                presenter.location(mActivity);
            } else {
                Log.d(TAG, "onCreateView: by cityname");
                presenter.getWeatherByCityName(BaseActivity.settings.getCity());
            }
//            ProgressBar progressBar=new ProgressBar(getActivity());
//            progressBar.setVisibility(View.VISIBLE);
        } else {
            Log.d(TAG, "onCreateView: by cache");
            presenter.getCache();
        }
        ButterKnife.bind(this, view);
        return view;


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
        _rxBus=((BaseActivity)mActivity).getRxBusSingleton();
    }
    @Override
    public void onStart() {
        super.onStart();
        _subscriptions = new CompositeSubscription();

        Observable<Object> updateEvent = _rxBus.toObserverable().share();
        _subscriptions//
                .add(updateEvent.subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object event) {
                        if (event instanceof AutoUpdateWeatherService.Update) {
                            presenter.getWeatherByCityName(BaseActivity.settings.getCity());
                        }
                    }
                }));

        Observable<Object> cityEvent = _rxBus.toObserverable().share();
        _subscriptions.add(cityEvent.subscribe(new Action1<Object>() {
            @Override
            public void call(final Object o) {
                if (o instanceof ChooseCityFragment.CityEvent) {
                    if (Utility.isNetworkConnected(mActivity)) {
                        Log.d(TAG, "call: rxbus" + ((ChooseCityFragment.CityEvent) o).msg);
                        presenter.getWeatherByCityName(((ChooseCityFragment.CityEvent) o).msg);
                    } else {
                        Snackbar.make(refresh, "无网络", Snackbar.LENGTH_INDEFINITE).setAction("重试", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                presenter.getWeatherByCityName(((ChooseCityFragment.CityEvent) o).msg);
                            }
                        }).show();
                    }
                }
            }
        }));
    }

    @Override
    public void onRefresh() {
        refresh.setRefreshing(false);
        Observable.timer(10, TimeUnit.MILLISECONDS).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                presenter.getWeatherByCityName(BaseActivity.settings.getCity());
                if (_rxBus.hasObservers()) {
                    _rxBus.send(new ReplaceBg());
                }
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

    public synchronized  void handleTodayWeatherResponse(WeatherInfo weather) {
        BaseActivity.settings.setCity(weather.result.today.city);
        cityName.setText(weather.result.today.city);
        Log.d("onBindViewHolder", weather.result.sk.temp + "hahah");
        temperature.setText(weather.result.sk.temp);
        time.setText(weather.result.sk.time + "更新");


    }


    @Override
    public void showWeather(WeatherInfo weatherInfo) {
        Log.d(TAG, "showWeather: ");
        handleTodayWeatherResponse(weatherInfo);
    }

    @Override
    public void progressBarVisible() {

    }

    @Override
    public void progressBarGone() {

    }

    @Override
    public void setPresenter(WeatherDetailContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    public static class ReplaceBg{

    }
}
