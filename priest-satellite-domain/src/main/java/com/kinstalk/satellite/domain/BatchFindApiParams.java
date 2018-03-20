package com.kinstalk.satellite.domain;

import java.io.Serializable;

/**
 * User: liuling
 * Date: 16/4/18
 * Time: 下午11:05
 */
public class BatchFindApiParams implements Serializable {

    private static final long serialVersionUID = -4189234033386106437L;

    private Long apiId;

    private Long createTime;

    public BatchFindApiParams() {
    }

    public BatchFindApiParams(Long apiId, Long createTime) {
        this.apiId = apiId;
        this.createTime = createTime;
    }

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "BatchFindApiParams{" +
                "apiId=" + apiId +
                ", createTime=" + createTime +
                '}';
    }
}
