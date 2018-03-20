package com.kinstalk.satellite.domain.packet;


import com.kinstalk.satellite.common.constant.ConstantSocket;

/**
 * Created by zhangchuanqi on 16/6/16.
 */
public class TimerStopPacket extends SocketPacket {

    protected Long timerId;//任务id

    public TimerStopPacket(Long timerId){
        type= ConstantSocket.TIMER_STOP;
        this.timerId=timerId;
    }

    public Long getTimerId() {
        return timerId;
    }

    public void setTimerId(Long timerId) {
        this.timerId = timerId;
    }
}
