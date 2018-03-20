package com.kinstalk.satellite.domain;

import java.io.Serializable;

/**
 * 接口日报表对象
 * User: liuling
 * Date: 16/4/12
 * Time: 上午11:14
 */
public class ReportDayApi implements Serializable {

    private static final long serialVersionUID = -6350542392405391439L;

    /**
     * 接口id
     */
    private Long apiId;

    /**
     * 接口url
     */
    private String apiUrl;

    /**
     * 模块id
     */
    private Long moduleId;

    /**
     * 接口成功率
     */
    private Float successRate;

    /**
     * 接口总调用次数
     */
    private Integer totalCount;

    /**
     * 接口最大调用时间
     */
    private Long maxCostTime;

    /**
     * 接口最小调用时间
     */
    private Long minCostTime;

    /**
     * 接口平均调用时间
     */
    private Long aveCostTime;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long updateTime;


    /**
     * AgentId
     */
    private Integer agentId;

    /**
     * AgentArea
     */
    private  String agentArea;

    /**
     * APP ID
     */
    private Integer appId;

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

    public Float getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(Float successRate) {
        this.successRate = successRate;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Long getMaxCostTime() {
        return maxCostTime;
    }

    public void setMaxCostTime(Long maxCostTime) {
        this.maxCostTime = maxCostTime;
    }

    public Long getMinCostTime() {
        return minCostTime;
    }

    public void setMinCostTime(Long minCostTime) {
        this.minCostTime = minCostTime;
    }

    public Long getAveCostTime() {
        return aveCostTime;
    }

    public void setAveCostTime(Long aveCostTime) {
        this.aveCostTime = aveCostTime;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getAgentArea() {
        return agentArea;
    }

    public void setAgentArea(String agentArea) {
        this.agentArea = agentArea;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    @Override
    public String toString() {
        return "ReportDayApi{" +
                "apiId=" + apiId +
                ", apiUrl='" + apiUrl + '\'' +
                ", moduleId=" + moduleId +
                ", successRate=" + successRate +
                ", totalCount=" + totalCount +
                ", maxCostTime=" + maxCostTime +
                ", minCostTime=" + minCostTime +
                ", aveCostTime=" + aveCostTime +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", agentId=" + agentId +
                ", agentArea='" + agentArea + '\'' +
                '}';
    }
}
