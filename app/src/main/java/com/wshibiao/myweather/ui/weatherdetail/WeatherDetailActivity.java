package com.wshibiao.myweather.ui.weatherdetail;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.bumptech.glide.Glide;
import com.wshibiao.myweather.R;
import com.wshibiao.myweather.base.BaseActivity;
import com.wshibiao.myweather.base.RxBus;
import com.wshibiao.myweather.data.AutoUpdateWeatherService;
import com.wshibiao.myweather.data.bean.BusProvider;
import com.wshibiao.myweather.ui.choosecity.ChooseCityActivity;
import com.wshibiao.myweather.ui.choosecity.ChooseCityFragment;
import com.wshibiao.myweather.ui.preferences.settings.SettingsActivity;
import com.wshibiao.myweather.util.ActivityUtils;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class WeatherDetailActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "WeatherDetailActivity";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    @Bind(R.id.nav_view)
    NavigationView navView;

    private WeatherDetailContract.Presenter presenter;
    private CompositeSubscription _subscriptions;
    private RxBus _rxBus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.weather_detail_activity);
        ButterKnife.bind(this);
        BusProvider.getBusInstance().register(this);
        setSupportActionBar(toolbar);
        Log.d(TAG, "WeatherActivity onCreate: " + getCacheDir() + "/ACache");
        Log.d(TAG, "WeatherActivity onCreate: " + getExternalCacheDir() + "/ACache");
        Glide.with(this).load(R.drawable.sun0);
        collapsingToolbarLayout.setTitle(" ");
        coordinatorLayout.setBackground(getResources().getDrawable(R.drawable.bg_fine_night));
        Log.d(TAG, "onCreate: " + System.currentTimeMillis());
        Observable.timer(3000, TimeUnit.MILLISECONDS).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                collapsingToolbarLayout.setTitle(BaseActivity.settings.getCity());
            }
        });
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);//设置还没收缩时状态下字体颜色
//        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.GREEN);//设置收缩后Toolbar上字体的颜色

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                startActivity(new Intent(WeatherDetailActivity.this, ChooseCityActivity.class));
                startService(new Intent(WeatherDetailActivity.this, AutoUpdateWeatherService.class));
            }
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        navView.setNavigationItemSelectedListener(this);

        WeatherDetailFragment weatherDetailFragment = (WeatherDetailFragment) getSupportFragmentManager().findFragmentById(R.id.frame);
        if (weatherDetailFragment == null) {
            // Create the fragment
            weatherDetailFragment = weatherDetailFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), weatherDetailFragment, R.id.frame);
        }

        presenter = new WeatherDetailPresenter(weatherDetailFragment, this);

        _rxBus = BaseActivity.getRxBusSingleton();
//        processExtraData();

        if (BaseActivity.preferences.getBoolean("auto_update_weather", true)) {
            startService(new Intent(WeatherDetailActivity.this, AutoUpdateWeatherService.class));
        } else {
            stopService(new Intent(WeatherDetailActivity.this, AutoUpdateWeatherService.class));
        }


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.sun3);
        // 创建Palette对象
        Palette.generateAsync(bitmap,
                new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        // 通过Palette来获取对应的色调
                        Palette.Swatch vibrant =
                                palette.getLightVibrantSwatch();
                        // 将颜色设置给相应的组件


                        Window window = getWindow();
                        window.setStatusBarColor(vibrant.getRgb());
                    }
                });

        getWindow().setEnterTransition(new Explode());
        getWindow().setExitTransition(new Explode());

    }

    @Override
    protected void onResume() {
        super.onResume();
        _subscriptions = new CompositeSubscription();

        Observable<Object> updateEvent = _rxBus.toObserverable().share();
        _subscriptions//
                .add(updateEvent.subscribe(new Action1<Object>() {
                    @Override
                    public void call(final Object event) {
                        if (event instanceof ChooseCityFragment.CityEvent) {
                            Observable.timer(3000, TimeUnit.MILLISECONDS).subscribe(new Action1<Long>() {
                                @Override
                                public void call(Long aLong) {
                                    collapsingToolbarLayout.setTitle(((ChooseCityFragment.CityEvent) event).msg);
                                }
                            });

                        }
                    }
                }));

//        Observable<Object> replaceBackground=_rxBus.toObserverable().share();
//        _subscriptions.add(replaceBackground.observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<Object>() {
//                    @Override
//                    public void call(Object o) {
//                        if (o instanceof WeatherDetailFragment.ReplaceBg) {
//                            coordinatorLayout.setBackground(getResources().getDrawable(R.drawable.rain_day));
//                        }
//                    }
//                }));

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        setIntent(intent);//must store the new intent unless getIntent() will return the old one
//        processExtraData();
//    }
//
//    private void processExtraData() {
//
//        Intent intent = getIntent();
//
//        //use the data received here
//
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.weather_deatil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        _subscriptions.unsubscribe();
        ButterKnife.unbind(this);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_choose_city) {
            startActivity(new Intent(WeatherDetailActivity.this, ChooseCityActivity.class),
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else if (id == R.id.nav_settings
                ) {
            startActivity(new Intent(WeatherDetailActivity.this, SettingsActivity.class));
        } else if (id == R.id.nav_share) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
