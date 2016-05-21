package com.wshibiao.myweather.data.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wsb on 2016/5/20.
 */
public class Forecast3thWeather implements Serializable{


    /**
     * resultcode : 200
     * reason : successed
     * result : [{"weatherid":"00","weather":"晴","temp1":"27","temp2":"31","sh":"08","eh":"11","date":"20140530","sfdate":"20140530080000","efdate":"20140530110000"}]
     */

    @SerializedName("resultcode")
    public String resultcode;
    @SerializedName("reason")
    public String reason;
    /**
     * weatherid : 00
     * weather : 晴
     * temp1 : 27
     * temp2 : 31
     * sh : 08
     * eh : 11
     * date : 20140530
     * sfdate : 20140530080000
     * efdate : 20140530110000
     */

    @SerializedName("result")
    public List<ResultBean> result;

    public static class ResultBean implements Serializable{
        @SerializedName("weatherid")
        public String weatherid;
        @SerializedName("weather")
        public String weather;
        @SerializedName("temp1")
        public String temp1;
        @SerializedName("temp2")
        public String temp2;
        @SerializedName("sh")
        public String sh;
        @SerializedName("eh")
        public String eh;
        @SerializedName("date")
        public String date;
        @SerializedName("sfdate")
        public String sfdate;
        @SerializedName("efdate")
        public String efdate;

    }
}
