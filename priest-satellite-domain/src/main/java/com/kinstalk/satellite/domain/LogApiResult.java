package com.kinstalk.satellite.domain;

import java.io.Serializable;

/**
 * User: liuling
 * Date: 16/4/20
 * Time: 上午10:10
 */
public class LogApiResult implements Serializable {

    private static final long serialVersionUID = -9210136633534585524L;

    private Long apiId;

    private Double okRate;

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    public Double getOkRate() {
        return okRate;
    }

    public void setOkRate(Double okRate) {
        this.okRate = okRate;
    }

    @Override
    public String toString() {
        return "LogApiResultDTO{" +
                "apiId=" + apiId +
                ", okRate=" + okRate +
                '}';
    }
}
