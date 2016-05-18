package com.wshibiao.myweather.ui.choosecity;


import com.wshibiao.myweather.base.BasePresenter;
import com.wshibiao.myweather.base.BaseView;
import com.wshibiao.myweather.data.bean.City;

import java.util.List;

import rx.Subscription;

/**
 * Created by wsb on 2016/4/26.
 */
 public interface  ChooseCityContract  {
    interface View extends BaseView<Presenter> {
        void showNoCity();
        void showCity(List<City> cities);

    }

    interface Presenter extends BasePresenter {
        Subscription searchCity() ;
        void onEditChanged(String s);

    }
}
