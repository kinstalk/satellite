package com.kinstalk.satellite.domain;

/**
 * User: liuling
 * Date: 16/4/13
 * Time: 下午4:00
 */
public class TimeRate {

    private String time;

    private Float rate;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "TimeRate{" +
                "time='" + time + '\'' +
                ", rate=" + rate +
                '}';
    }
}
