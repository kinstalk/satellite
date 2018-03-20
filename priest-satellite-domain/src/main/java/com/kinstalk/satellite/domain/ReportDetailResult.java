package com.kinstalk.satellite.domain;

import java.io.Serializable;
import java.util.List;

/**
 * User: liuling
 * Date: 16/4/13
 * Time: 下午4:01
 */
public class ReportDetailResult implements Serializable{

    private static final long serialVersionUID = 949660097100134679L;

    private List<DayRate> dayRateList;

    private List<TimeRate> timeRateList;

    public List<DayRate> getDayRateList() {
        return dayRateList;
    }

    public void setDayRateList(List<DayRate> dayRateList) {
        this.dayRateList = dayRateList;
    }

    public List<TimeRate> getTimeRateList() {
        return timeRateList;
    }

    public void setTimeRateList(List<TimeRate> timeRateList) {
        this.timeRateList = timeRateList;
    }

    @Override
    public String toString() {
        return "ReportDetailResult{" +
                "dayRateList=" + dayRateList +
                ", timeRateList=" + timeRateList +
                '}';
    }
}
