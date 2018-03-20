package com.kinstalk.satellite.socket.agent;

import com.kinstalk.satellite.domain.packet.SocketPacket;
import com.kinstalk.satellite.domain.packet.TimerStopPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhangchuanqi on 16/6/20.
 */
public class TimerStopHandler {
	private Logger logger = LoggerFactory.getLogger(TimerStopHandler.class);

	public SocketPacket handle(SocketPacket packet) {
		if (!(packet instanceof TimerStopPacket)) {
			logger.error("invalid packet type!" + packet);
			return null;
		}

		TimerStopPacket timerStopPacket = (TimerStopPacket) packet;

		TimerControl.stop(timerStopPacket.getTimerId());


		return timerStopPacket;
	}


}
