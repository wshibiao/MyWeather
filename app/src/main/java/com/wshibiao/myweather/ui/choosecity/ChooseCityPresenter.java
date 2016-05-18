package com.wshibiao.myweather.ui.choosecity;


import android.util.Log;

import com.wshibiao.myweather.base.BaseApplication;
import com.wshibiao.myweather.data.bean.City;
import com.wshibiao.myweather.data.local.db.CityDBSource;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by wsb on 2016/4/26.
 */
public class ChooseCityPresenter implements ChooseCityContract.Presenter {
    private static final String TAG = "ChooseCityPresenter";
    private PublishSubject<String> mSearchResultsSubject ;
    private ChooseCityContract.View mView;
    private CityDBSource db= CityDBSource.getInstance(BaseApplication.getContext());

    public ChooseCityPresenter(ChooseCityContract.View mView) {
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public Subscription searchCity() {

        mSearchResultsSubject = PublishSubject.create();
        return mSearchResultsSubject
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, List<City>>() {
                    public List<City> call(String city) {
                        return db.searchFromDB(city);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<City>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: searchCity"+e);

                    }

                    @Override
                    public void onNext(List<City> cities) {
                        if (cities.isEmpty()) {
                            mView.showNoCity();
                        } else {
                            mView.showCity(cities);
                        }
                    }
                });
            }



    @Override
    public void onEditChanged(String s){
        mSearchResultsSubject.onNext(s);
    }

    @Override
    public void start() {

    }
}
