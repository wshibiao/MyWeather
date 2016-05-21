package com.wshibiao.myweather.ui.choosecity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import com.wshibiao.myweather.base.BaseActivity;
import com.wshibiao.myweather.R;
import com.wshibiao.myweather.util.ActivityUtils;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by wsb on 2016/4/26.
 */
public class ChooseCityActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.frame_city)
    FrameLayout frame;

    private ChooseCityContract.Presenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        setContentView(R.layout.choose_city_activity);
        ButterKnife.bind(this);
        ChooseCityFragment chooseCityFragment = (ChooseCityFragment) getSupportFragmentManager().findFragmentById(R.id.frame_city);


        if (chooseCityFragment == null) {
            // Create the fragment
            chooseCityFragment = ChooseCityFragment.newInstance();
             ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), chooseCityFragment, R.id.frame_city);
        }

        presenter=new ChooseCityPresenter(chooseCityFragment);


        toolbar.setTitle("选择城市");
        toolbar.setTitleTextColor(Color.BLUE);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_32dpdp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(toolbar);
        ActionBar ab=getSupportActionBar();

        ab.setDisplayHomeAsUpEnabled(true);
        getWindow().setEnterTransition(new Explode());
        getWindow().setExitTransition(new Explode());


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.home:
                Snackbar.make(toolbar,"city clicked",Snackbar.LENGTH_LONG).show();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
