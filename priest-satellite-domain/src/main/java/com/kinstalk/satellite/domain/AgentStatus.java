package com.kinstalk.satellite.domain;

import java.io.Serializable;

/**
 * Created by zhangchuanqi on 16/6/24.
 */
public class AgentStatus implements Serializable{

    private  Integer statusId;

    private  Long timerId;

    private  Integer agentId;

    private String timerName;

    private String agentName;

    private  Integer status;

    private  Long lastRuntime;

    private  Long updateTime;

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Long getTimerId() {
        return timerId;
    }

    public void setTimerId(Long timerId) {
        this.timerId = timerId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getLastRuntime() {
        return lastRuntime;
    }

    public void setLastRuntime(Long lastRuntime) {
        this.lastRuntime = lastRuntime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }


    public String getTimerName() {
        return timerName;
    }

    public void setTimerName(String timerName) {
        this.timerName = timerName;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    @Override
    public String toString() {
        return "AgentStatus{" +
                "statusId=" + statusId +
                ", timerId=" + timerId +
                ", agentId=" + agentId +
                ", timerName='" + timerName + '\'' +
                ", agentName='" + agentName + '\'' +
                ", status=" + status +
                ", lastRuntime=" + lastRuntime +
                ", updateTime=" + updateTime +
                '}';
    }
}
