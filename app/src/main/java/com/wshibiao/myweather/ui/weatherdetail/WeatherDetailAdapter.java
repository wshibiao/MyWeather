package com.wshibiao.myweather.ui.weatherdetail;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wshibiao.myweather.R;
import com.wshibiao.myweather.data.bean.WeatherInfo;
import com.wshibiao.myweather.util.WeatherIcon;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wsb on 2016/4/28.
 */
public class WeatherDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "WeatherDetailAdapter";
    private static final int SK_WEATHER = 0;
    private static final int TODAY_DETAIL=1;
    private static final int FUTURE_WEATHER = 2;
    private Context mContext;
    private WeatherInfo weather;
    public enum ITEM_TYPE{
        SK_WEATHER,
        TODAY_WEATHER,
        FUTURE_WEATHER
        }

    public WeatherDetailAdapter(Context mContext, WeatherInfo weather) {
        this.mContext = mContext;
        this.weather = weather;
    }


//    public void updateWithClear(WeatherInfo weatherInfo){
//        weather=weatherInfo;
//        weather.result = weatherInfo.result;
//        weather.result.sk = weatherInfo.result.sk;
//        weather.result.future = weatherInfo.result.future;
//        weather.result.today = weatherInfo.result.today;
//        notifyDataSetChanged();
//    }
    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.SK_WEATHER.ordinal()) {
            return new SkViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_sk, parent, false));
        }

        if (viewType == ITEM_TYPE.TODAY_WEATHER.ordinal()) {
            return new TodayViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_today_detail, parent, false));
        }

        if (viewType==ITEM_TYPE.FUTURE_WEATHER.ordinal()){
            return new ForecastViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_forecast7_weather,parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SkViewHolder) {
            try {
                ((SkViewHolder) holder).skCityName.setText(weather.result.today.city);
                Log.d("onBindViewHolder", weather.result.sk.temp + "hahah");
                ((SkViewHolder) holder).skTemperature.setText(weather.result.sk.temp);
                ((SkViewHolder) holder).time.setText(weather.result.sk.time + "更新");
                ((SkViewHolder) holder).skWeather.setText(weather.result.sk.windStrength);
//                ((SkViewHolder) holder).skCityName.setText(weather.result.today.city);
//                Log.d("onBindViewHolder", weather.result.sk.temp + "hahah");
//                ((SkViewHolder) holder).skTemperature.setText(weather.result.sk.temp);
//                ((SkViewHolder) holder).time.setText(weather.result.sk.time + "更新");
//                ((SkViewHolder) holder).skWeather.setText(weather.result.today.weather);
            } catch (Exception e) {

            }
        }
        if (holder instanceof TodayViewHolder){
            try{

             ((TodayViewHolder) holder).tvHumidity.setText(weather.result.sk.humidity);
                ((TodayViewHolder) holder).tvDryingIndex.setText(weather.result.today.dryingIndex);
                ((TodayViewHolder) holder).tvWind.setText(weather.result.sk.windDirection+weather.result.sk.windStrength);
                ((TodayViewHolder) holder).tvUvIndex.setText(weather.result.today.uvIndex);
                ((TodayViewHolder) holder).comfortable.setText(weather.result.today.comfortIndex);
               ((TodayViewHolder) holder).tvDressingIndex.setText(weather.result.today.dressingIndex);
                ((TodayViewHolder) holder).tvDressingAdviceIndex.setText(weather.result.today.dressingAdvice);
                ((TodayViewHolder) holder).tvWashIndex.setText(weather.result.today.washIndex);
                ((TodayViewHolder) holder).tvTravelIndex.setText(weather.result.today.travelIndex);
               ((TodayViewHolder) holder).tvExerciseIndex.setText(weather.result.today.exerciseIndex);

            }catch (Exception e){

            }
        }
        if (holder instanceof ForecastViewHolder) {
            try {
                Log.d(TAG, "onBindViewHolder: future"+weather.result.future.get(1).week);
                for (int i = 0; i < weather.result.future.size(); i++) {
                    ((ForecastViewHolder) holder).forecastDate[i].setText(weather.result.future.get(i).week);

                    int id = WeatherIcon.weatherIcon(weather.result.future.get(i).weatherId.fa);
                    ((ForecastViewHolder) holder).forecastIcon[i].setImageResource(id);
                    ((ForecastViewHolder) holder).forecastTemp[i].setText(weather.result.future.get(i).temperature);

                }
            } catch (Exception e) {

            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == SK_WEATHER) {
            return SK_WEATHER;
        }

        if (position == TODAY_DETAIL) {
            return TODAY_DETAIL;
        }
        if (position == FUTURE_WEATHER) {
            return FUTURE_WEATHER;
        }

        return 0;
    }

    public class SkViewHolder extends RecyclerView.ViewHolder {
        @Bind(com.wshibiao.myweather.R.id.sk_city_name)
        TextView skCityName;
        @Bind(com.wshibiao.myweather.R.id.sk_weather)
        TextView skWeather;
        @Bind(com.wshibiao.myweather.R.id.sk_temperature)
        TextView skTemperature;
        @Bind(com.wshibiao.myweather.R.id.time)
        TextView time;
        @Bind(com.wshibiao.myweather.R.id.sk_card_View)
        CardView skCardView;

        public SkViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }

    }



    public class TodayViewHolder  extends RecyclerView.ViewHolder{
        @Bind(com.wshibiao.myweather.R.id.tv_humidity)
        TextView tvHumidity;
        @Bind(com.wshibiao.myweather.R.id.tv_drying_index)
        TextView tvDryingIndex;
        @Bind(com.wshibiao.myweather.R.id.tv_wind)
        TextView tvWind;
        @Bind(com.wshibiao.myweather.R.id.tv_uv_index)
        TextView tvUvIndex;
        @Bind(com.wshibiao.myweather.R.id.comfortable)
        TextView comfortable;
        @Bind(com.wshibiao.myweather.R.id.tv_dressing_index)
        TextView tvDressingIndex;
        @Bind(com.wshibiao.myweather.R.id.tv_dressing_advice_index)
        TextView tvDressingAdviceIndex;
        @Bind(com.wshibiao.myweather.R.id.tv_wash_index)
        TextView tvWashIndex;
        @Bind(com.wshibiao.myweather.R.id.tv_travel_index)
        TextView tvTravelIndex;
        @Bind(com.wshibiao.myweather.R.id.tv_exercise_index)
        TextView tvExerciseIndex;

        TodayViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class ForecastViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout forecastLinear;
        private TextView[] forecastDate = new TextView[weather.result.future.size()];
        private TextView[] forecastTemp = new TextView[weather.result.future.size()];

        private ImageView[] forecastIcon = new ImageView[weather.result.future.size()];

        public ForecastViewHolder(View itemView) {
            super(itemView);
            forecastLinear = (LinearLayout) itemView.findViewById(com.wshibiao.myweather.R.id.forecast_linear);
            for (int i = 0; i <weather.result.future.size(); i++) {
                View view = View.inflate(mContext, com.wshibiao.myweather.R.layout.item_forecast_line, null);
                forecastDate[i] = (TextView) view.findViewById(R.id.index_future_week);
                forecastTemp[i] = (TextView) view.findViewById(com.wshibiao.myweather.R.id.index_future_temperature);
                forecastIcon[i] = (ImageView) view.findViewById(com.wshibiao.myweather.R.id.index_future_weather);
                forecastLinear.addView(view);
            }
        }
    }
}


