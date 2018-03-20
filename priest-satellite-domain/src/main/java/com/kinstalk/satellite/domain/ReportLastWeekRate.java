package com.kinstalk.satellite.domain;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by zhangchuanqi on 16/11/8.
 */
public class ReportLastWeekRate implements Serializable {
    /**
     * 接口id
     */
    private Long apiId;

    /**
     * 模块id
     */
    private Long moduleId;

    /**
     * 接口状态.0:失败 1:成功
     */
    private Integer status;

    /**
     *失败接口数
     */
    private Integer failStatus;
    /**
     * 成功率
     */
    private String sucRate;

    /**
     * 接口当前调用最大时间
     */
    private Long costTime;

    /**
     * 接口总调用次数
     */
    private Integer totalCount;

    /**
     * 接口调用成功的次数
     */
    private Integer okCount;

    /**
     * app_id
     */
    private Integer appId;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 查询开始时间
     */
    private Long startTime;

    /**
     * 查询截止时间
     */
    private Long endTime;
    /**
     * format date
     */

    private String datetime;

    /**
     * 接口url
     */
    private String apiUrl;

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

    public String getSucRate() {
        return sucRate;
    }

    public void setSucRate(String sucRate) {
        this.sucRate = sucRate;
    }

    public Long getCostTime() {
        return costTime;
    }

    public void setCostTime(Long costTime) {
        this.costTime = costTime;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getOkCount() {
        return okCount;
    }

    public void setOkCount(Integer okCount) {
        this.okCount = okCount;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public Integer getFailStatus() {
        return failStatus;
    }

    public void setFailStatus(Integer failStatus) {
        this.failStatus = failStatus;
    }

    public void setCreateTime(Long createTime) {
        DateFormat formatter = new SimpleDateFormat("MM/dd HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(createTime);
        setDatetime(formatter.format(calendar.getTime()));
        this.createTime = createTime;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {


        this.datetime = datetime;

    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    @Override
    public String toString() {
        return "ReportLastWeekRate{" +
                "apiId=" + apiId +
                ", moduleId=" + moduleId +
                ", status=" + status +
                ", sucRate='" + sucRate + '\'' +
                ", costTime=" + costTime +
                ", totalCount=" + totalCount +
                ", okCount=" + okCount +
                ", createTime=" + createTime +
                ", apiUrl='" + apiUrl + '\'' +
                '}';
    }
}
