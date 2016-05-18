package com.wshibiao.myweather.ui.splash;

import com.wshibiao.myweather.base.BaseApplication;
import com.wshibiao.myweather.data.remote.NetWork;
import com.wshibiao.myweather.data.bean.City;
import com.wshibiao.myweather.data.bean.CityResults;
import com.wshibiao.myweather.data.local.db.CityDBSource;

import rx.Observable;
import rx.Observer;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wsb on 2016/4/28.
 */
public class SplashPresenter implements SplashContract.Presenter {
    private final String KEY="6d660d7c817e5b57ab0afd636d336f3b";
    private CityDBSource db= CityDBSource.getInstance(BaseApplication.getContext());

    private SplashContract.View mView;

    public SplashPresenter(SplashContract.View mView) {
        this.mView = mView;
        mView.setPresenter(this);
    }

    //加载城市
    @Override
    public void start() {
        NetWork.getCityList()
                .mCity(KEY)
                .subscribeOn(Schedulers.newThread())
                .flatMap(new Func1<CityResults, Observable<City>>() {
                    public Observable<City> call(CityResults result) {
                        return Observable.from(result.cityLists);
                    }
                }).subscribe(new Observer<City>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(City city) {
                db.saveCity(city);
            }
        });
    }
}
