package com.wshibiao.myweather.ui.choosecity;


import com.wshibiao.myweather.base.BasePresenter;
import com.wshibiao.myweather.base.BaseView;
import com.wshibiao.myweather.data.bean.City;

import java.util.List;

/**
 * Created by wsb on 2016/4/26.
 */
 public interface  ChooseCityContract  {
    interface View extends BaseView<Presenter> {
        void showNoCity();
        void showCity(List<City> cities);
        void listenToSearchInput();
        void toActivity(String cityName);
        void hideHotCity();

    }

    interface Presenter extends BasePresenter {
        void searchCity() ;
        void onEditChanged(String s);

    }
}
