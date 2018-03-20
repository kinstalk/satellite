package com.kinstalk.satellite.domain;

import java.io.Serializable;

/**
 * User: liuling
 * Date: 16/4/21
 * Time: 上午11:01
 */
public class Api implements Serializable {

    private static final long serialVersionUID = 2253912323232514319L;

    /**
     * 接口id
     */
    private Long apiId;

    /**
     * 接口名称
     */
    private String apiUrl;

    /**
     * 模块id
     */
    private Long moduleId;

    /**
     * app id
     */
    private Integer appId;

    public Api() {
    }

    public Api(String apiUrl, Long moduleId, Long createTime,Integer appId) {
        this.apiUrl = apiUrl;
        this.moduleId = moduleId;
        this.createTime = createTime;
        this.appId=appId;
    }

    /**
     * 创建时间
     */
    private Long createTime;

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    @Override
    public String toString() {
        return "Api{" +
                "apiId=" + apiId +
                ", apiUrl='" + apiUrl + '\'' +
                ", moduleId=" + moduleId +
                ", createTime=" + createTime +
                '}';
    }
}
