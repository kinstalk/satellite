package com.kinstalk.satellite.domain.agnentmodel;



import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by zhangchuanqi on 16/6/17.
 */
public class TimerModel implements Serializable {


    private byte[] file;//执行脚本;
    private Long id;//任务id
    private Integer sleepTime;//执行时间间隔
    private Integer timerType;//是否循环执行
    private Integer runType;
    private Integer appId;




    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(Integer sleepTime) {
        this.sleepTime = sleepTime;
    }

    public Integer getTimerType() {
        return timerType;
    }

    public void setTimerType(Integer timerType) {
        this.timerType = timerType;
    }

    public Integer getRunType() {
        return runType;
    }

    public void setRunType(Integer runType) {
        this.runType = runType;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    @Override
    public String toString() {
        return "TimerModel{" +
                "file=" + Arrays.toString(file) +
                ", id=" + id +
                ", sleepTime=" + sleepTime +
                ", timerType=" + timerType +
                '}';
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null){
            return false;
        }else {
            if(this.getClass() == obj.getClass()){
                TimerModel u = (TimerModel) obj;
                if(this.getSleepTime()==u.getSleepTime()&&this.getFile().length==u.getFile().length){
                    return true;
                }else{
                    return false;
                }

            }else{
                return false;
            }
        }
    }
}
