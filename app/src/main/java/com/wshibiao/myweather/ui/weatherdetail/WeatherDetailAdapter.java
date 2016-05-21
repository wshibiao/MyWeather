package com.wshibiao.myweather.ui.weatherdetail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wshibiao.myweather.R;
import com.wshibiao.myweather.data.bean.WeatherInfo;
import com.wshibiao.myweather.util.WeatherIcon;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wsb on 2016/4/28.
 * 弃用RecyclerView
 */
public class WeatherDetailAdapter extends RecyclerView.Adapter<WeatherDetailAdapter.ForecastViewHolder> {
    private static final String TAG = "WeatherDetailAdapter";
    private OnRecyclerViewItemClickListener mOnItemClickListener;
    private Context mContext;
    private WeatherInfo weather;
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
        return 7;
    }

    @Override
    public ForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_forecast_line, parent, false);
        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ForecastViewHolder holder,final int position) {
        try {
            for (int i = 0; i < weather.result.future.size(); i++) {
                holder.indexFutureWeek.setText(weather.result.future.get(i).week);
                int id = WeatherIcon.weatherIcon(weather.result.future.get(i).weatherId.fa);
                holder.indexFutureWeather.setImageResource(id);
                holder.indexFutureTemperature.setText(weather.result.future.get(i).temperature);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                mOnItemClickListener.onItemClick(v, position);
            }
        });
//        if (holder instanceof SkViewHolder) {
//            try {
//                ((SkViewHolder) holder).skCityName.setText(weather.result.today.city);
//                Log.d("onBindViewHolder", weather.result.sk.temp + "hahah");
//                ((SkViewHolder) holder).skTemperature.setText(weather.result.sk.temp);
//                ((SkViewHolder) holder).time.setText(weather.result.sk.time + "更新");
//                ((SkViewHolder) holder).skWeather.setText(weather.result.sk.windStrength);
////                ((SkViewHolder) holder).skCityName.setText(weather.result.today.city);
////                Log.d("onBindViewHolder", weather.result.sk.temp + "hahah");
////                ((SkViewHolder) holder).skTemperature.setText(weather.result.sk.temp);
////                ((SkViewHolder) holder).time.setText(weather.result.sk.time + "更新");
////                ((SkViewHolder) holder).skWeather.setText(weather.result.today.weather);
//            } catch (Exception e) {
//
//            }
//        }
//        if (holder instanceof TodayViewHolder){
//            try{
//
//             ((TodayViewHolder) holder).tvHumidity.setText(weather.result.sk.humidity);
//                ((TodayViewHolder) holder).tvDryingIndex.setText(weather.result.today.dryingIndex);
//                ((TodayViewHolder) holder).tvWind.setText(weather.result.sk.windDirection+weather.result.sk.windStrength);
//                ((TodayViewHolder) holder).tvUvIndex.setText(weather.result.today.uvIndex);
//                ((TodayViewHolder) holder).comfortable.setText(weather.result.today.comfortIndex);
//               ((TodayViewHolder) holder).tvDressingIndex.setText(weather.result.today.dressingIndex);
//                ((TodayViewHolder) holder).tvDressingAdviceIndex.setText(weather.result.today.dressingAdvice);
//                ((TodayViewHolder) holder).tvWashIndex.setText(weather.result.today.washIndex);
//                ((TodayViewHolder) holder).tvTravelIndex.setText(weather.result.today.travelIndex);
//               ((TodayViewHolder) holder).tvExerciseIndex.setText(weather.result.today.exerciseIndex);
//
//            }catch (Exception e){
//
//            }
//        }


    }


//
//    public class SkViewHolder extends RecyclerView.ViewHolder {
//        @Bind(com.wshibiao.myweather.R.id.sk_city_name)
//        TextView skCityName;
//        @Bind(com.wshibiao.myweather.R.id.sk_weather)
//        TextView skWeather;
//        @Bind(com.wshibiao.myweather.R.id.sk_temperature)
//        TextView skTemperature;
//        @Bind(com.wshibiao.myweather.R.id.time)
//        TextView time;
//        @Bind(com.wshibiao.myweather.R.id.sk_card_View)
//        CardView skCardView;
//
//        public SkViewHolder(View itemView) {
//            super(itemView);
//            ButterKnife.bind(this, itemView);
//
//
//        }
//
//    }


//    public class TodayViewHolder  extends RecyclerView.ViewHolder{
//        @Bind(com.wshibiao.myweather.R.id.tv_humidity)
//        TextView tvHumidity;
//        @Bind(com.wshibiao.myweather.R.id.tv_drying_index)
//        TextView tvDryingIndex;
//        @Bind(com.wshibiao.myweather.R.id.tv_wind)
//        TextView tvWind;
//        @Bind(com.wshibiao.myweather.R.id.tv_uv_index)
//        TextView tvUvIndex;
//        @Bind(com.wshibiao.myweather.R.id.comfortable)
//        TextView comfortable;
//        @Bind(com.wshibiao.myweather.R.id.tv_dressing_index)
//        TextView tvDressingIndex;
//        @Bind(com.wshibiao.myweather.R.id.tv_dressing_advice_index)
//        TextView tvDressingAdviceIndex;
//        @Bind(com.wshibiao.myweather.R.id.tv_wash_index)
//        TextView tvWashIndex;
//        @Bind(com.wshibiao.myweather.R.id.tv_travel_index)
//        TextView tvTravelIndex;
//        @Bind(com.wshibiao.myweather.R.id.tv_exercise_index)
//        TextView tvExerciseIndex;
//
//        TodayViewHolder(View view) {
//            super(view);
//            ButterKnife.bind(this, view);
//        }
//    }

    class ForecastViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.index_future_week)
        TextView indexFutureWeek;
        @Bind(R.id.index_future_weather)
        ImageView indexFutureWeather;
        @Bind(R.id.index_future_temperature)
        TextView indexFutureTemperature;
        public ForecastViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);

        }
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int pos);
    }
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}



