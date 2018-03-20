package com.kinstalk.satellite.domain.packet;


import com.kinstalk.satellite.common.constant.ConstantSocket;
import com.kinstalk.satellite.domain.agnentmodel.TimerModel;

/**
 * Created by zhangchuanqi on 16/6/21.
 */
public class TimerResultPacket extends SocketPacket{


    public TimerResultPacket(byte[] treeLogFile, byte[] csvLogFile, Long startTime, Long endTime, TimerModel timerModel, Integer status, boolean isUpdate, Integer appId){
        type= ConstantSocket.TIMER_RESULT;
        this.treeLogFile = treeLogFile;
        this.csvLogFile = csvLogFile;
        this.startTime=startTime;
        this.endTime=endTime;
        this.timerModel=timerModel;
        this.status=status;
        this.appId=appId;
    }

    private byte[] treeLogFile;//结果树文件;
    private byte[] csvLogFile;//csv 文件;
    private Long startTime;
    private Long endTime;
    private TimerModel timerModel;
    private Integer status;
    private boolean isUpdate;
    private Integer appId;


    public byte[] getTreeLogFile() {
        return treeLogFile;
    }

    public TimerResultPacket setTreeLogFile(byte[] treeLogFile) {
        this.treeLogFile = treeLogFile;
		return this;
    }

    public byte[] getCsvLogFile() {
        return csvLogFile;
    }

    public TimerResultPacket setCsvLogFile(byte[] csvLogFile) {
        this.csvLogFile = csvLogFile;
		return this;
    }

    public Long getStartTime() {
        return startTime;
    }

    public TimerResultPacket setStartTime(Long startTime) {
        this.startTime = startTime;
		return this;
    }

    public Long getEndTime() {
        return endTime;
    }

    public TimerResultPacket setEndTime(Long endTime) {
        this.endTime = endTime;
		return this;
    }

    public TimerModel getTimerModel() {
        return timerModel;
    }

    public TimerResultPacket setTimerModel(TimerModel timerModel) {
        this.timerModel = timerModel;
		return this;
    }

    public Integer getStatus() {
        return status;
    }

    public TimerResultPacket setStatus(Integer status) {
        this.status = status;
		return this;
    }

    public boolean getUpdate() {
        return isUpdate;
    }

    public TimerResultPacket setUpdate(boolean update) {
        this.isUpdate = update;
		return this;
    }

    public Integer getAppId() {
        return appId;
    }

    public TimerResultPacket setAppId(Integer appId) {
        this.appId = appId;
		return this;
    }
}
