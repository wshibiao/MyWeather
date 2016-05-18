package com.wshibiao.myweather.data.bean;

import com.squareup.otto.Bus;

/**
 * Created by wsb on 2016/4/24.
 */
public class BusProvider {
    private static final Bus BUS=new Bus();

    public static Bus getBusInstance(){
        return  BUS;
    }

    private BusProvider() {
    }
}
