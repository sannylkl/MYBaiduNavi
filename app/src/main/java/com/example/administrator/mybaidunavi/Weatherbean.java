package com.example.administrator.mybaidunavi;

/**
 * Created by Administrator on 2017/9/27 0027.
 */

public class Weatherbean {


    /**
     * date : 26日星期二
     * sunrise : 06:06
     * high : 高温 36.0℃
     * low : 低温 27.0℃
     * sunset : 18:09
     * aqi : 44
     * fx : 无持续风向
     * fl : <3级
     * type : 晴
     * notice : 晴空万里，去沐浴阳光吧
     */

    private String date;
    private String sunrise;
    private String high;
    private String low;
    private String sunset;
    private int aqi;
    private String fx;
    private String fl;
    private String type;
    private String notice;

    public Weatherbean(String date, String sunrise, String high, String low, String sunset, int aqi, String fx, String fl, String type, String notice) {
        this.date = date;
        this.sunrise = sunrise;
        this.high = high;
        this.low = low;
        this.sunset = sunset;
        this.aqi = aqi;
        this.fx = fx;
        this.fl = fl;
        this.type = type;
        this.notice = notice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public int getAqi() {
        return aqi;
    }

    public void setAqi(int aqi) {
        this.aqi = aqi;
    }

    public String getFx() {
        return fx;
    }

    public void setFx(String fx) {
        this.fx = fx;
    }

    public String getFl() {
        return fl;
    }

    public void setFl(String fl) {
        this.fl = fl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }
}
