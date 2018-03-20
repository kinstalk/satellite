package com.kinstalk.satellite.domain;

import java.io.Serializable;

/**
 * 批量保存至流水表的参数类
 * User: liuling
 * Date: 16/4/18
 * Time: 下午5:24
 */
public class BatchInsertParam implements Serializable {

    private static final long serialVersionUID = -8838743132598251857L;

    /**
     * 接口id
     */
    private String apiUrl;

    /**
     * 接口状态.0:失败 1:成功
     */
    private Integer status;

    /**
     * 接口调用时间
     */
    private Long costTime;

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
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

    @Override
    public String toString() {
        return "BatchInsertParam{" +
                "apiUrl='" + apiUrl + '\'' +
                ", status=" + status +
                ", costTime=" + costTime +
                '}';
    }
}
