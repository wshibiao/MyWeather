package com.wshibiao.myweather.data.remote.api;


import com.wshibiao.myweather.data.bean.CityResults;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wsb on 2016/4/19.
 */
public interface CityApi {
    @GET("http://v.juhe.cn/weather/citys")
    Observable<CityResults> mCity(@Query("key") String key);
}
