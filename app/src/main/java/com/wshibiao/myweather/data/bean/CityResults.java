package com.wshibiao.myweather.data.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wsb on 2016/4/19.
 */
public class CityResults implements Serializable {
    @SerializedName("result") public List<City> cityLists;

}
