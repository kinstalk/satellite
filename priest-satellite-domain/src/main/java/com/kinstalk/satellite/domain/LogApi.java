package com.kinstalk.satellite.domain;

import java.io.Serializable;

/**
 * 结果流水对象
 * User: liuling
 * Date: 16/4/12
 * Time: 上午11:14
 */
public class LogApi implements Serializable {

    private static final long serialVersionUID = 7217150000725143150L;

    /**
     * 接口id
     */
    private Long apiId;

    /**
     * 模块id
     */
    private Long moduleId;
    /**
     * AgentID
     */
    private Integer agentId;

    /**
     * 接口状态.0:失败 1:成功
     */
    private Integer status;

    /**
     * 接口调用时间
     */
    private Long costTime;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * app ID
     */
    private Integer appId;

    /**
     * timer ID
     */
    private Long timerId;



    public LogApi() {
    }

    public LogApi(Long apiId, Integer status, Long costTime, Long createTime,Integer agentId,Integer appId,Long timerId) {
        this.apiId = apiId;
        this.status = status;
        this.costTime = costTime;
        this.createTime = createTime;
        this.agentId=agentId;
        this.appId=appId;
        this.timerId=timerId;
    }

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCostTime() {
        return costTime;
    }

    public void setCostTime(Long costTime) {
        this.costTime = costTime;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Long getTimerId() {
        return timerId;
    }

    public void setTimerId(Long timerId) {
        this.timerId = timerId;
    }

    @Override
    public String toString() {
        return "LogApi{" +
                "apiId=" + apiId +
                ", moduleId=" + moduleId +
                ", agentId=" + agentId +
                ", status=" + status +
                ", costTime=" + costTime +
                ", createTime=" + createTime +
                ", appId=" + appId +
                '}';
    }
}
