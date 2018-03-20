package com.kinstalk.satellite.domain;

import java.io.Serializable;

/**
 * Created by zhangchuanqi on 16/7/8.
 */
public class PhonePool implements Serializable {

    private Long id;

    private Long agentId;

    private Long timerId;

    private String phoneStart;

    private String phoneEnd;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public Long getTimerId() {
        return timerId;
    }

    public void setTimerId(Long timerId) {
        this.timerId = timerId;
    }

    public String getPhoneStart() {
        return phoneStart;
    }

    public void setPhoneStart(String phoneStart) {
        this.phoneStart = phoneStart;
    }

    public String getPhoneEnd() {
        return phoneEnd;
    }

    public void setPhoneEnd(String phoneEnd) {
        this.phoneEnd = phoneEnd;
    }

    @Override
    public String toString() {
        return "PhonePool{" +
                "id=" + id +
                ", agentId=" + agentId +
                ", timerId=" + timerId +
                ", phoneStart='" + phoneStart + '\'' +
                ", phoneEnd='" + phoneEnd + '\'' +
                '}';
    }
}
