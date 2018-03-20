package com.kinstalk.satellite.domain.packet;


import com.kinstalk.satellite.common.constant.ConstantSocket;
import com.kinstalk.satellite.domain.agnentmodel.TimerModel;

/**
 * Created by zhangchuanqi on 16/6/16.
 */
public class TimerUpdatePacket extends SocketPacket {

    protected TimerModel timerModel;//任务包

    public TimerUpdatePacket(TimerModel timerModel){
        type= ConstantSocket.TIMER_UPDATE;
        this.timerModel=timerModel;
    }
    public TimerModel getTimerModel() {
        return timerModel;
    }

    public void setTimerModel(TimerModel timerModel) {
        this.timerModel = timerModel;
    }

}
