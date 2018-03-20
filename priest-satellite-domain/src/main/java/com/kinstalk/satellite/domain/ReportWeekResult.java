package com.kinstalk.satellite.domain;

import java.io.Serializable;
import java.util.List;

/**
 * User: liuling
 * Date: 16/4/13
 * Time: 下午3:14
 */
public class ReportWeekResult implements Serializable {

    private static final long serialVersionUID = 6488489931112962638L;

    /**
     * 接口id
     */
    private Long apiId;

    /**
     * 接口名称
     */
    private String apiUrl;

    /**
     * agent Area
     */

    private String agentArea;

    /**
     * 显示的日期
     */
    private List<Integer> statusList;

    public ReportWeekResult() {
    }

    public ReportWeekResult(Long apiId, String apiUrl, List<Integer> statusList,String agentArea) {
        this.apiId = apiId;
        this.apiUrl = apiUrl;
        this.statusList = statusList;
        this.agentArea=agentArea;
    }

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

    public List<Integer> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<Integer> statusList) {
        this.statusList = statusList;
    }


    public String getAgentArea() {
        return agentArea;
    }

    public void setAgentArea(String agentArea) {
        this.agentArea = agentArea;
    }

    @Override
    public String toString() {
        return "ReportWeekResult{" +
                "apiId=" + apiId +
                ", apiUrl='" + apiUrl + '\'' +
                ", agentArea='" + agentArea + '\'' +
                ", statusList=" + statusList +
                '}';
    }
}
