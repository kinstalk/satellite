package com.kinstalk.satellite.domain;

/**
 * User: liuling
 * Date: 16/4/13
 * Time: 下午4:00
 */
public class DayRate {

    private String day;

    private Float rate;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "DayRate{" +
                "day='" + day + '\'' +
                ", rate=" + rate +
                '}';
    }
}
