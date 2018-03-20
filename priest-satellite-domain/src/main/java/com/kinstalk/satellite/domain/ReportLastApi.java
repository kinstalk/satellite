package com.kinstalk.satellite.domain;

import java.io.Serializable;
import java.text.NumberFormat;

/**
 * User: liuling
 * Date: 16/4/12
 * Time: 下午4:15
 */
public class ReportLastApi implements Serializable {

    private static final long serialVersionUID = -5471818180079397082L;

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
     * 接口最近24小时成功率
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
     * 接口总调用次数
     */
    private Integer totalCount;

    /**
     * 接口调用成功的次数
     */
    private Integer okCount;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 统计图 数据
     */

    private  String countString ;


    private String apiUrl;

    public String getSucRate() {
        return sucRate;
    }

    public void setSucRate(String sucRate) {
        this.sucRate = sucRate;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
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

    public Float getLastHoursRate() {
        return lastHoursRate;
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

    public void setLastHoursRate(Float lastHoursRate) {
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMaximumFractionDigits(3);
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

    public String getCountString() {
        return countString;
    }

    public void setCountString(String countString) {
        this.countString = countString;
    }

    @Override
    public String toString() {
        return "ReportLastApi{" +
                "apiId=" + apiId +
                ", moduleId=" + moduleId +
                ", status=" + status +
                ", lastHoursRate=" + lastHoursRate +
                ", sucRate='" + sucRate + '\'' +
                ", costTime=" + costTime +
                ", totalCount=" + totalCount +
                ", okCount=" + okCount +
                ", createTime=" + createTime +
                ", apiUrl='" + apiUrl + '\'' +
                '}';
    }
}
