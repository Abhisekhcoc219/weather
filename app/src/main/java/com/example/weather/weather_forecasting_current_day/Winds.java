package com.example.weather.weather_forecasting_current_day;

import com.google.gson.annotations.SerializedName;

public class Winds {
    @SerializedName("speed")
    private double speed;
    @SerializedName("deg")
    private int deg;
    @SerializedName("gust")
    private double gust;

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getDeg() {
        return deg;
    }

    public void setDeg(int deg) {
        this.deg = deg;
    }

    public double getGust() {
        return gust;
    }

    public void setGust(double gust) {
        this.gust = gust;
    }
}
