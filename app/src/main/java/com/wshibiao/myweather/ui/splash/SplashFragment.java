package com.wshibiao.myweather.ui.splash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wshibiao.myweather.base.BaseFragment;
import com.wshibiao.myweather.R;
import com.wshibiao.myweather.ui.weatherdetail.WeatherDetailActivity;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;

/**
 * Created by wsb on 2016/4/28.
 */
public class SplashFragment extends BaseFragment implements SplashContract.View{
    private SplashContract.Presenter presenter;
    public static SplashFragment getNewInstance(){
        return new SplashFragment();
    }


    @Override
    public void setPresenter(SplashContract.Presenter presenter) {
        this.presenter=presenter;

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity=(Activity)context;
    }
    public void isFirstRun(){
        SharedPreferences preferences= mActivity.getSharedPreferences("is_first", Context.MODE_PRIVATE);
        if(preferences.getBoolean("is_first",true)){
            presenter.start();
            SharedPreferences.Editor editor = mActivity.getSharedPreferences("is_first",
                    Context.MODE_PRIVATE).edit();
            editor.putBoolean("is_first", false);
            presenter.start();
            editor.apply();
            Observable.timer(8000, TimeUnit.MILLISECONDS)
                    .subscribe(getObserver());

        }else {

            Observable.timer(2000, TimeUnit.MILLISECONDS)
                    .subscribe(getObserver());
        }
    }


    public Observer<Long> getObserver(){
        return new Observer<Long>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onNext(Long aLong) {
                toActivity();
            }
        };
    }

    public void toActivity(){
        Intent intent = new Intent(mActivity, WeatherDetailActivity.class);
        startActivity(intent);
        getActivity().finish();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    super.onCreateView(inflater, container, savedInstanceState);
        View view=inflater.inflate(R.layout.splash_fragment,container,false);
        return view;

    }

    @Override
    public void onResume(){
        super.onResume();
        isFirstRun();
    }
}
