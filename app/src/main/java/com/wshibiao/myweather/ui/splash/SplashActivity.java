package com.wshibiao.myweather.ui.splash;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.view.Window;

import com.wshibiao.myweather.util.ActivityUtils;


/**
 * Created by wsb on 2016/4/28.
 */
public class SplashActivity extends AppCompatActivity implements SplashContract.View{
    private SplashContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(com.wshibiao.myweather.R.layout.splash_activity);
        presenter=new SplashPresenter(this);
        SplashFragment splashFragment=(SplashFragment)getSupportFragmentManager().findFragmentById(com.wshibiao.myweather.R.id.splash_frame);
        if(splashFragment==null){
            splashFragment=SplashFragment.getNewInstance().getNewInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), splashFragment, com.wshibiao.myweather.R.id.splash_frame);
        }
        presenter=new SplashPresenter(splashFragment);
        getWindow().setEnterTransition(new Fade());
        getWindow().setExitTransition(new Fade());

    }

    public void setUpTransition(){

    }

    @Override
    public void setPresenter(SplashContract.Presenter presenter) {
        this.presenter=presenter;

    }


    @Override
    public void onResume(){
        super.onResume();

    }
}
