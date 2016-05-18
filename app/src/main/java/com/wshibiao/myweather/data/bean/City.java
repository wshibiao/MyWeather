package com.wshibiao.myweather.data.bean;

/**
 * Created by wsb on 2016/4/26.
 */
public class City {
    private int id;
    private String province;
    private String city;
    private String district;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
