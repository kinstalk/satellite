package com.kinstalk.satellite.domain;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 模块对象
 * User: liuling
 * Date: 16/4/12
 * Time: 上午11:14
 */
public class Module implements Serializable {

    private static final long serialVersionUID = 7494459472340933481L;

    /**
     * 模块id
     */
    private Long moduleId;

    /**
     * 模块路径
     */
    private String moduleUrl;


    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 模块当前调用状态
     */
    private Integer status;

    /**
     * 模块最近24小时成功率
     */
    private Float lastHoursRate;

    /**
     * 成功率的百分比格式
     */
    private String sucRate;

    /**
     * 接口当前调用最大时间
     */
    private Long costTime;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 等待时间
     */
    private long nextRuntime;

    /**
     * Agent 名称
     */

    private String agentArea;

    /**
     * appId
     */

    private Integer appId;

    /**
     * 统计图 数据
     */

    private  String countString ;



    public Module() {
    }

    public Module(String moduleUrl, Long createTime,Integer appId) {
        this.moduleUrl = moduleUrl;
        this.createTime = createTime;
        this.appId=appId;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleUrl() {
        return moduleUrl;
    }


    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getSucRate() {
        return sucRate;
    }

    public void setSucRate(String sucRate) {
        this.sucRate = sucRate;
    }

    public void setModuleUrl(String moduleUrl) {
        this.moduleUrl = moduleUrl;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Float getLastHoursRate() {
        return lastHoursRate;
    }

    public void setLastHoursRate(Float lastHoursRate) {
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMaximumFractionDigits(1);
        String sucRate = nf.format(lastHoursRate);
        setSucRate(sucRate);
        this.lastHoursRate = lastHoursRate;
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


    public long getNextRuntime() {
        return nextRuntime;
    }

    public void setNextRuntime(long nextRuntime) {
        this.nextRuntime = nextRuntime;
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

    public String getCountString() {
        return countString;
    }

    public void setCountString(String countString) {
        this.countString = countString;
    }

    @Override
    public String toString() {
        return "Module{" +
                "moduleId=" + moduleId +
                ", moduleUrl='" + moduleUrl + '\'' +
                ", moduleName='" + moduleName + '\'' +
                ", status=" + status +
                ", lastHoursRate=" + lastHoursRate +
                ", sucRate='" + sucRate + '\'' +
                ", costTime=" + costTime +
                ", createTime=" + createTime +
                ", nextRuntime=" + nextRuntime +
                ", agentArea='" + agentArea + '\'' +
                '}';
    }
}
