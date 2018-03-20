package com.kinstalk.satellite.domain;

import java.io.Serializable;

/**
 * Created by zhangchuanqi on 16/4/14.
 */
public class RunScriptTimer implements Serializable{

    private Long id;

    private Long scriptId;

    private String scriptName;//脚本名称

    private String name;

    private Integer timerType;

    private Integer runType;

    private Integer appId;

    private Long firstTime;

    private Integer sleepTime;

    private Integer timerStatus;

    private Integer runStatus;

    private String noticePhone;

    private String noticeEmail;

    private String duty;

    private Long createTime;

    private String path;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getScriptId() {
        return scriptId;
    }

    public void setScriptId(Long scriptId) {
        this.scriptId = scriptId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTimerType() {
        return timerType;
    }

    public void setTimerType(Integer timerType) {
        this.timerType = timerType;
    }

    public Long getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(Long firstTime) {
        this.firstTime = firstTime;
    }

    public Integer getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(Integer sleepTime) {
        this.sleepTime = sleepTime;
    }

    public Integer getTimerStatus() {
        return timerStatus;
    }

    public void setTimerStatus(Integer timerStatus) {
        this.timerStatus = timerStatus;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(Integer runStatus) {
        this.runStatus = runStatus;
    }

    public String getScriptName() {
        return scriptName;
    }

    public void setScriptName(String scriptName) {
        this.scriptName = scriptName;
    }

    public Integer getRunType() {
        return runType;
    }

    public void setRunType(Integer runType) {
        this.runType = runType;
    }

    public String getNoticePhone() {
        return noticePhone;
    }

    public void setNoticePhone(String noticePhone) {
        this.noticePhone = noticePhone;
    }

    public String getNoticeEmail() {
        return noticeEmail;
    }

    public void setNoticeEmail(String noticeEmail) {
        this.noticeEmail = noticeEmail;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    @Override
    public String toString() {
        return "RunScriptTimer{" +
                "id=" + id +
                ", scriptId=" + scriptId +
                ", scriptName='" + scriptName + '\'' +
                ", name='" + name + '\'' +
                ", timerType=" + timerType +
                ", runType=" + runType +
                ", appId=" + appId +
                ", firstTime=" + firstTime +
                ", sleepTime=" + sleepTime +
                ", timerStatus=" + timerStatus +
                ", runStatus=" + runStatus +
                ", noticePhone='" + noticePhone + '\'' +
                ", noticeEmail='" + noticeEmail + '\'' +
                ", duty='" + duty + '\'' +
                ", createTime=" + createTime +
                ", path='" + path + '\'' +
                '}';
    }
}
