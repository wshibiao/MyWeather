package com.wshibiao.myweather.data.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wsb on 2016/4/19.
 */
public class WeatherInfo implements Serializable {

    /**
     * resultcode : 200
     * reason : 查询成功!
     * result : {"sk":{"temp":"21","wind_direction":"西风","wind_strength":"2级","humidity":"4%","time":"14:25"},"today":{"city":"天津","date_y":"2014年03月21日","week":"星期五","temperature":"8℃~20℃","weather":"晴转霾","weather_id":{"fa":"00","fb":"53"},"wind":"西南风微风","dressing_index":"较冷","dressing_advice":"建议着大衣、呢外套加毛衣、卫衣等服装。","uv_index":"中等","comfort_index":"","wash_index":"较适宜","travel_index":"适宜","exercise_index":"较适宜","drying_index":""},"future":[{"temperature":"28℃~36℃","weather":"晴转多云","weather_id":{"fa":"00","fb":"01"},"wind":"南风3-4级","week":"星期一","date":"20140804"},{"temperature":"28℃~36℃","weather":"晴转多云","weather_id":{"fa":"00","fb":"01"},"wind":"东南风3-4级","week":"星期二","date":"20140805"},{"temperature":"27℃~35℃","weather":"晴转多云","weather_id":{"fa":"00","fb":"01"},"wind":"东南风3-4级","week":"星期三","date":"20140806"},{"temperature":"27℃~34℃","weather":"多云","weather_id":{"fa":"01","fb":"01"},"wind":"东南风3-4级","week":"星期四","date":"20140807"},{"temperature":"27℃~33℃","weather":"多云","weather_id":{"fa":"01","fb":"01"},"wind":"东北风4-5级","week":"星期五","date":"20140808"},{"temperature":"26℃~33℃","weather":"多云","weather_id":{"fa":"01","fb":"01"},"wind":"北风4-5级","week":"星期六","date":"20140809"},{"temperature":"26℃~33℃","weather":"多云","weather_id":{"fa":"01","fb":"01"},"wind":"北风4-5级","week":"星期日","date":"20140810"}]}
     * error_code : 0
     */


    /**
     * sk : {"temp":"21","wind_direction":"西风","wind_strength":"2级","humidity":"4%","time":"14:25"}
     * today : {"city":"天津","date_y":"2014年03月21日","week":"星期五","temperature":"8℃~20℃","weather":"晴转霾","weather_id":{"fa":"00","fb":"53"},"wind":"西南风微风","dressing_index":"较冷","dressing_advice":"建议着大衣、呢外套加毛衣、卫衣等服装。","uv_index":"中等","comfort_index":"","wash_index":"较适宜","travel_index":"适宜","exercise_index":"较适宜","drying_index":""}
     * future : [{"temperature":"28℃~36℃","weather":"晴转多云","weather_id":{"fa":"00","fb":"01"},"wind":"南风3-4级","week":"星期一","date":"20140804"},{"temperature":"28℃~36℃","weather":"晴转多云","weather_id":{"fa":"00","fb":"01"},"wind":"东南风3-4级","week":"星期二","date":"20140805"},{"temperature":"27℃~35℃","weather":"晴转多云","weather_id":{"fa":"00","fb":"01"},"wind":"东南风3-4级","week":"星期三","date":"20140806"},{"temperature":"27℃~34℃","weather":"多云","weather_id":{"fa":"01","fb":"01"},"wind":"东南风3-4级","week":"星期四","date":"20140807"},{"temperature":"27℃~33℃","weather":"多云","weather_id":{"fa":"01","fb":"01"},"wind":"东北风4-5级","week":"星期五","date":"20140808"},{"temperature":"26℃~33℃","weather":"多云","weather_id":{"fa":"01","fb":"01"},"wind":"北风4-5级","week":"星期六","date":"20140809"},{"temperature":"26℃~33℃","weather":"多云","weather_id":{"fa":"01","fb":"01"},"wind":"北风4-5级","week":"星期日","date":"20140810"}]
     */
    @SerializedName("resultcode")
    public String resultCode;
    @SerializedName("error_code")
    public String errorCode;
    @SerializedName("result")
    public Result result;


    public static class Result implements Serializable {
        /**
         * temp : 21
         * wind_direction : 西风
         * wind_strength : 2级
         * humidity : 4%
         * time : 14:25
         */

        @SerializedName("sk")
        public Sk sk;
        /**
         * city : 天津
         * date_y : 2014年03月21日
         * week : 星期五
         * temperature : 8℃~20℃
         * weather : 晴转霾
         * weather_id : {"fa":"00","fb":"53"}
         * wind : 西南风微风
         * dressing_index : 较冷
         * dressing_advice : 建议着大衣、呢外套加毛衣、卫衣等服装。
         * uv_index : 中等
         * comfort_index :
         * wash_index : 较适宜
         * travel_index : 适宜
         * exercise_index : 较适宜
         * drying_index :
         */

        @SerializedName("today")
        public Sk.Today today;
        /**
         * temperature : 28℃~36℃
         * weather : 晴转多云
         * weather_id : {"fa":"00","fb":"01"}
         * wind : 南风3-4级
         * week : 星期一
         * date : 20140804
         */

        @SerializedName("future")
        public List<Sk.FutureList> future;


        public static class Sk  implements Serializable {
            @SerializedName("temp")
            public String temp;
            @SerializedName("wind_direction")
            public String windDirection;
            @SerializedName("wind_strength")
            public String windStrength;
            @SerializedName("humidity")
            public String humidity;
            @SerializedName("time")
            public String time;




            public static class Today  implements Serializable {
                @SerializedName("city")
                public String city;
                @SerializedName("date_y")
                public String dateY;
                @SerializedName("week")
                public String week;
                @SerializedName("temperature")
                public String temperature;
                @SerializedName("weather")
                public String weather;
                /**
                 * fa : 00
                 * fb : 53
                 */

                @SerializedName("weather_id")
                public WeatherIdBean weatherId;
                @SerializedName("wind")
                public String wind;
                @SerializedName("dressing_index")
                public String dressingIndex;
                @SerializedName("dressing_advice")
                public String dressingAdvice;
                @SerializedName("uv_index")
                public String uvIndex;
                @SerializedName("comfort_index")
                public String comfortIndex;
                @SerializedName("wash_index")
                public String washIndex;
                @SerializedName("travel_index")
                public String travelIndex;
                @SerializedName("exercise_index")
                public String exerciseIndex;
                @SerializedName("drying_index")
                public String dryingIndex;


                public static class WeatherIdBean implements Serializable{
                    @SerializedName("fa")
                    public String fa;
                    @SerializedName("fb")
                    public String fb;
                    
                }
            }

            public static class FutureList  implements Serializable {
                @SerializedName("temperature")
                public String temperature;
                @SerializedName("weather")
                public String weather;
                /**
                 * fa : 00
                 * fb : 01
                 */

                @SerializedName("weather_id")
                public WeatherId weatherId;
                @SerializedName("wind")
                public String wind;
                @SerializedName("week")
                public String week;
                @SerializedName("date")
                public String date;


                public static class WeatherId  implements Serializable {
                    @SerializedName("fa")
                    public String fa;
                    @SerializedName("fb")
                    public String fb;

                  
                }
            }
        }
    }
}
