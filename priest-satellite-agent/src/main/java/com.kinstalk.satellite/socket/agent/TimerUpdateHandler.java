package com.kinstalk.satellite.socket.agent;

import com.kinstalk.satellite.domain.packet.SocketPacket;
import com.kinstalk.satellite.domain.packet.TimerUpdatePacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhangchuanqi on 16/6/20.
 */
public class TimerUpdateHandler {
    private Logger logger= LoggerFactory.getLogger(TimerUpdateHandler.class);

    public SocketPacket handle(SocketPacket packet) {
        if(! (packet instanceof TimerUpdatePacket)) {
            logger.error("invalid packet type!" + packet);
            return null;
        }

        TimerUpdatePacket timerPacket = (TimerUpdatePacket)packet;

		TimerControl.updateTimer(timerPacket.getTimerModel());

        return timerPacket;
    }
}
