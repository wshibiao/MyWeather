package com.wshibiao.myweather.util;


import com.wshibiao.myweather.R;

/**
 * Created by wsb on 2016/5/14.
 */
public class WeatherIcon {

    private static final int[] mImgID = new int[]{R.drawable.d00, R.drawable.d01, R.drawable.d02, R.drawable.d03,
            R.drawable.d04, R.drawable.d05, R.drawable.d06,
            R.drawable.d07, R.drawable.d08, R.drawable.d09, R.drawable.d10,
            R.drawable.d11, R.drawable.d12, R.drawable.d13, R.drawable.d14,
            R.drawable.d15, R.drawable.d16, R.drawable.d17, R.drawable.d18, R.drawable.d19,
            R.drawable.d20, R.drawable.d21, R.drawable.d22, R.drawable.d23, R.drawable.d24,
            R.drawable.d25, R.drawable.d26, R.drawable.d27, R.drawable.d28, R.drawable.d29,
            R.drawable.d30, R.drawable.d31, R.drawable.d32};


    public static int weatherIcon(String fa){
        if (fa.equals("53")){
            return mImgID[32];
        }
       return  mImgID[Integer.parseInt(fa)];
    }
}
