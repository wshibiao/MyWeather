package com.wshibiao.myweather.ui.weatherdetail;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.wshibiao.myweather.R;
import com.wshibiao.myweather.data.bean.WeatherInfo;

import java.util.List;

import butterknife.Bind;

/**
 * Created by wsb on 2016/5/17.
 */
public class TestAdapter extends BaseMultiItemQuickAdapter {
    private static final int SK_WEATHER = 0;
    private static final int TODAY_DETAIL = 1;
    private static final int FUTURE_WEATHER = 2;
    @Bind(R.id.sk_city_name)
    TextView skCityName;
    @Bind(R.id.sk_weather)
    TextView skWeather;
    @Bind(R.id.sk_temperature)
    TextView skTemperature;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.sk_card_View)
    CardView skCardView;
    private Context mContext;
    private WeatherInfo weatherInfo;

    public TestAdapter(Context context, List<WeatherInfo> data, Context mContext, WeatherInfo weatherInfo) {
        super(context, data);
        this.mContext = mContext;
        this.weatherInfo = weatherInfo;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, MultiItemEntity multiItemEntity) {

        addItmeType(SK_WEATHER, R.layout.item_sk);
        addItmeType(TODAY_DETAIL, R.layout.item_today_detail);
        addItmeType(FUTURE_WEATHER, R.layout.item_forecast7_weather);
    }
}
