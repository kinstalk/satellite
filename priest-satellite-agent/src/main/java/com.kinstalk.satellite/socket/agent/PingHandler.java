package com.kinstalk.satellite.socket.agent;

import com.kinstalk.satellite.domain.packet.PingPacket;
import com.kinstalk.satellite.domain.packet.PongPacket;
import com.kinstalk.satellite.domain.packet.SocketPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhangchuanqi on 16/8/8.
 */
public class PingHandler {

    private Logger logger= LoggerFactory.getLogger(TimerStopHandler.class);

    public SocketPacket handle(SocketPacket packet) {
        if(! (packet instanceof PingPacket)) {
            logger.error("invalid packet type!" + packet);
            return null;
        }

        return new PongPacket();
    }
}
