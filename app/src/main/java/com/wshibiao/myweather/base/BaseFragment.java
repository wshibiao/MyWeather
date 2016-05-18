package com.wshibiao.myweather.base;

import android.app.Activity;
import android.support.v4.app.Fragment;

import rx.Subscription;

/**
 * Created by wsb on 2016/5/15.
 */
public class BaseFragment extends Fragment {
    protected static Activity mActivity;
    protected Subscription subscription;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unSubscribe();
    }

    protected void unSubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

}
