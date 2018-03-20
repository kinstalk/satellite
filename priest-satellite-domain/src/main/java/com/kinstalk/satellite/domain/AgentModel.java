package com.kinstalk.satellite.domain;

import java.io.Serializable;

/**
 * Created by zhangchuanqi on 16/6/24.
 */
public class AgentModel implements Serializable {
	public static final int STATUS_OFFLINE = 0; // 离线 
	public static final int STATUS_RUN = 1; 	// 运行
	public static final int STATUS_WAIT = 2;    // 等待运行 
    private Integer agentId;

    private String agentIp;

    private String agentPort;

    private String agentArea;

    private Long createTime;

    private Integer status=0;//0 离线 1 在线

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getAgentIp() {
        return agentIp;
    }

    public void setAgentIp(String agentIp) {
        this.agentIp = agentIp;
    }

    public String getAgentArea() {
        return agentArea;
    }

    public void setAgentArea(String agentArea) {
        this.agentArea = agentArea;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getAgentPort() {
        return agentPort;
    }

    public void setAgentPort(String agentPort) {
        this.agentPort = agentPort;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AgentModel{" +
                "agentId=" + agentId +
                ", agentIp='" + agentIp + '\'' +
                ", agentPort='" + agentPort + '\'' +
                ", agentArea='" + agentArea + '\'' +
                ", createTime=" + createTime +
                ", status=" + status +
                '}';
    }
}
