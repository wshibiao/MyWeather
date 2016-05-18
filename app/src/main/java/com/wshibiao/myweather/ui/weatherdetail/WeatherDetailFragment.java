package com.wshibiao.myweather.ui.weatherdetail;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wshibiao.myweather.base.BaseActivity;
import com.wshibiao.myweather.base.BaseFragment;
import com.wshibiao.myweather.base.RxBus;
import com.wshibiao.myweather.data.AutoUpdateWeatherService;
import com.wshibiao.myweather.data.bean.WeatherInfo;
import com.wshibiao.myweather.ui.choosecity.ChooseCityFragment;
import com.wshibiao.myweather.ui.preferences.Settings;
import com.wshibiao.myweather.util.Utility;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;


public class WeatherDetailFragment extends BaseFragment implements WeatherDetailContract.View,SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "WeatherDetailFragment";
    //    @Bind(R.id.my_refresh)
//    SwipeRefreshLayout myRefresh;
    @Bind(com.wshibiao.myweather.R.id.list)
    RecyclerView recycler;
    @Bind(com.wshibiao.myweather.R.id.progressBar)
    ProgressBar progressBar;
    @Bind(com.wshibiao.myweather.R.id.iv_erro)
    TextView ivErro;
    @Bind(com.wshibiao.myweather.R.id.refresh)
    SwipeRefreshLayout refresh;
    private SharedPreferences mPrefs;
    private WeatherDetailContract.Presenter presenter;
    private WeatherInfo weatherInfo = new WeatherInfo();
    private WeatherDetailAdapter mAdapter;
    private RxBus _rxBus;
    private CompositeSubscription _subscriptions;

    public WeatherDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void setPresenter(WeatherDetailContract.Presenter presenter) {
        this.presenter = presenter;
    }


    public static WeatherDetailFragment newInstance() {
        WeatherDetailFragment fragment = new WeatherDetailFragment();

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity=(Activity)context;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new WeatherDetailAdapter(mActivity, weatherInfo);
//        _rxBus=((BaseActivity)getActivity()).getRxBusSingleton();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(com.wshibiao.myweather.R.layout.fragment_act1, container, false);
        ButterKnife.bind(this, view);
        _rxBus = BaseActivity.getRxBusSingleton();
        mPrefs = mActivity.getSharedPreferences("weather_city", Context.MODE_PRIVATE);
        weatherInfo = new WeatherInfo();
        refresh.setOnRefreshListener(this);
        refresh.setSize(SwipeRefreshLayout.LARGE);
        presenter.start();
        mAdapter = new WeatherDetailAdapter(mActivity, weatherInfo);
        initRecyclerView();

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

        return view;
    }

    @Override
    public void errorNetSnackbar() {
        Snackbar.make(recycler, "网络不好,~( ´•︵•` )~", Snackbar.LENGTH_INDEFINITE).setAction("重试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getWeatherByCityName(mPrefs.getString(Settings.CITY, "海口"));
            }
        }).show();

    }


    public void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setHasFixedSize(true);
        recycler.setAdapter(mAdapter);
    }



    @Override
    public void onRefresh() {
        refresh.setRefreshing(false);
        Observable.timer(10, TimeUnit.MILLISECONDS).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                presenter.getWeatherByCityName(BaseActivity.settings.getCity());
            }
        });

    }




    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: weatherFragment");


    }

    @Override
    public void showWeather(WeatherInfo weather) {
        weatherInfo.result = weather.result;
        weatherInfo.result.sk = weather.result.sk;
        weatherInfo.result.future = weather.result.future;
        weatherInfo.result.today = weather.result.today;
        mAdapter.notifyDataSetChanged();
        BaseActivity.settings.setCity(weatherInfo.result.today.city);
    }

    @Override
    public void progressBarVisible() {
        Log.d(TAG, "progressBarVisible: called");
        progressBar.setVisibility(View.VISIBLE);
        progressBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBarGone();
            }
        }, 5000);

    }
    @Override
    public void progressBarGone() {
        progressBar.setVisibility(View.GONE);
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
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _subscriptions.clear();
        ButterKnife.unbind(this);
    }


}
