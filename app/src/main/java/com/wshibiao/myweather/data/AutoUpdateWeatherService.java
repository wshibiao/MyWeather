package com.wshibiao.myweather.data;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.wshibiao.myweather.base.BaseActivity;
import com.wshibiao.myweather.R;
import com.wshibiao.myweather.base.RxBus;
import com.wshibiao.myweather.data.bean.WeatherInfo;
import com.wshibiao.myweather.data.local.cache.ACache;
import com.wshibiao.myweather.data.remote.NetWork;
import com.wshibiao.myweather.ui.weatherdetail.WeatherDetailActivity;
import com.wshibiao.myweather.util.WeatherIcon;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by wsb on 2016/5/2.
 */
public class AutoUpdateWeatherService extends Service {
    private static final String TAG = "ForegroundServiceBinder";
    private static final String KEY="6d660d7c817e5b57ab0afd636d336f3b";
    private NotificationCompat.Builder notif;
    private ACache aCache;
    private ForegroundServiceBinder mBinder = new ForegroundServiceBinder();
    private RxBus _rxBus;
    private CompositeSubscription subscription;
    private SharedPreferences preferences;

    public class ForegroundServiceBinder extends Binder {

        public void updateForegroundService() {
            Intent intent=new Intent(getApplicationContext(),WeatherDetailActivity.class);
            PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
            SharedPreferences preferences1=getSharedPreferences("recentlyWeather",MODE_PRIVATE);
            SharedPreferences preferences=getSharedPreferences("today",MODE_PRIVATE);
            notif=new NotificationCompat.Builder(getApplicationContext());
            notif.setContentTitle(preferences1.getString("当前温度","__")+" " +preferences.getString("温度范围","__")
                    +"        "+preferences1.getString("当前风向", "__")+preferences1.getString("当前风力","__"))
                    .setSmallIcon(R.drawable.d01)
                    .setContentText(preferences.getString("天气","__")+"   "+preferences.getString("city","__")+"     "+preferences1.getString("更新时间","__")+"更新")
                    .setContentIntent(pendingIntent);
            startForeground(1,notif.build());
        }

        public int getProgress() {
            Log.d("AutoUpdateWeatherServic","getProgressexecuted");
            return 0;
        }
    }
    public IBinder onBind(Intent intent){

        return mBinder;
    }


    @Override
    public void onCreate(){
        preferences= PreferenceManager.getDefaultSharedPreferences(this);
        aCache= BaseActivity.mCache;
    }



    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        notif=new NotificationCompat.Builder(this);
        subscription=new CompositeSubscription();
        _rxBus= BaseActivity.getRxBusSingleton();
        if(preferences.getBoolean("auto_update_weather",true)) {
            subscription.add(Observable.interval( Long.parseLong(preferences.getString("update_frequency","180")), TimeUnit.MINUTES)
                    .subscribe(new Action1<Long>() {
                @Override
                public void call(Long aLong) {
                    if (_rxBus.hasObservers()) {
                        _rxBus.send(new Update());
                    }
                }
            }));
        }
        ForegroundService();
        startForeground(1, notif.build());
//        Observable.timer(BaseActivity.settings.getAutoUpdate(), TimeUnit.HOURS)
//                .subscribe(new Observer<Long>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//                    @Override
//                    public void onError(Throwable e) {
//                    }
//
//                    @Override
//                    public void onNext(Long aLong) {
//                        if(_rxBus.hasObservers()){
//                            _rxBus.send(new Update());
//                        }
//                    }
//                });

        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
    }

//    public void updateWeather(){
//        SharedPreferences preferences = getSharedPreferences("weather_city", MODE_PRIVATE);
//        String cityName = preferences.getString("cityName", null);
//
//        NetWork.getWeatherApi().getWeatherInfo("2",cityName,KEY)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<WeatherInfo>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                    }
//
//                    @Override
//                    public void onNext(WeatherInfo weatherInfo) {
//                        mCache.put("weather_temp", weatherInfo, );
//                    }
//                });
//    }


    public void ForegroundService(){
        Intent intent=new Intent(this,WeatherDetailActivity.class);
        final PendingIntent pendingIntent=PendingIntent.getActivity(this, 0, intent, 0);
        subscription.add(Observable.interval(30, TimeUnit.MINUTES, Schedulers.io())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        NetWork.getWeatherApi().getWeatherInfo("2", BaseActivity.settings.getCity(), KEY)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(ForegroundServiceObserver(pendingIntent));
                    }
                }));
    }

    //定时任务以分钟为单位
    public Observer ForegroundServiceObserver(final PendingIntent inten){
        return  new Observer<WeatherInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError:ForegroundServiceObserver "+e.toString());
            }

            @Override
            public void onNext(WeatherInfo weatherInfo) {
                Log.d(TAG, "onNext: Foreground"+weatherInfo.result.sk.windDirection);
                int id = WeatherIcon.weatherIcon(weatherInfo.result.today.weatherId.fa);
                notif.setContentTitle(weatherInfo.result.today.temperature+" "+weatherInfo.result.sk.temp
                               + "        "+weatherInfo.result.sk.windDirection+weatherInfo.result.sk.windDirection)
                        .setContentText(weatherInfo.result.today.weather+ "   "+weatherInfo.result.today.city+"     "+weatherInfo.result.sk.time+"更新")
                        .setSmallIcon(id)
                        .setContentIntent(inten);
            }
        };
    }




    public static class Update{

    }


}
