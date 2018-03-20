package com.kinstalk.satellite.domain;

import java.io.Serializable;
import java.text.NumberFormat;

/**
 * User: liuling
 * Date: 16/4/12
 * Time: 上午11:15
 */
public class ReportLastHoursApi implements Serializable {

    private static final long serialVersionUID = -1230039378915133587L;

    /**
     * 接口id
     */
    private Long apiId;

    /**
     * 模块id
     */
    private Long moduleId;

    /**
     * 接口成功率
     */
    private Float successRate;

    /**
     * 成功率的百分比格式,无百分号%
     */
    private Long sucRate;

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
     * 显示时间
     */
    private String displayTime;
    /**
     * AgentID
     */
    private Integer agentId;

    /**
     *appid
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
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMaximumFractionDigits(0);
        String sucRate = nf.format(successRate);
        sucRate = sucRate.replace("%", "");
        setSucRate(Long.valueOf(sucRate));
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

    public Long getSucRate() {
        return sucRate;
    }

    public void setSucRate(Long sucRate) {
        this.sucRate = sucRate;
    }

    public String getDisplayTime() {
        return displayTime;
    }

    public void setDisplayTime(String displayTime) {
        this.displayTime = displayTime;
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

    @Override
    public String toString() {
        return "ReportLastHoursApi{" +
                "apiId=" + apiId +
                ", moduleId=" + moduleId +
                ", successRate=" + successRate +
                ", sucRate=" + sucRate +
                ", totalCount=" + totalCount +
                ", maxCostTime=" + maxCostTime +
                ", minCostTime=" + minCostTime +
                ", aveCostTime=" + aveCostTime +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", displayTime='" + displayTime + '\'' +
                ", agentId=" + agentId +
                '}';
    }
}
