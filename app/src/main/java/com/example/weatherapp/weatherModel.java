package com.example.weatherapp;

import android.util.Log;

public class weatherModel {
    private String time,temp,icon,wind;

    public weatherModel(String time, String temp, String icon, String wind) {
        this.time = time;
        this.temp = temp;
        this.icon = icon;
        this.wind = wind;
        Log.d("myapp","in weatherapp  constructor");
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }
}
