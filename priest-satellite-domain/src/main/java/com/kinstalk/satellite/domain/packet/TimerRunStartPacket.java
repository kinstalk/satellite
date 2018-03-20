package com.kinstalk.satellite.domain.packet;


import com.kinstalk.satellite.common.constant.ConstantSocket;

/**
 * Created by digitZhang on 18/03/01.
 */
public class TimerRunStartPacket extends SocketPacket {

    protected Long timerId;//任务id

    public TimerRunStartPacket(Long timerId){
        type= ConstantSocket.TIMER_RUN_START;
        this.timerId=timerId;
    }

    public Long getTimerId() {
        return timerId;
    }

    public void setTimerId(Long timerId) {
        this.timerId = timerId;
    }
}
