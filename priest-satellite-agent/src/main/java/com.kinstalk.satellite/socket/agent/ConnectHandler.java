package com.kinstalk.satellite.socket.agent;

import com.kinstalk.satellite.domain.packet.ConnectPacket;
import com.kinstalk.satellite.domain.packet.LoginPacket;
import com.kinstalk.satellite.domain.packet.SocketPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by digitZhang on 16/6/8.
 */
public class ConnectHandler {

	private Logger logger= LoggerFactory.getLogger(ConnectHandler.class);

	public SocketPacket handle(SocketPacket packet) {
		SocketPacket result = null;

		if(! (packet instanceof ConnectPacket)) {
			logger.error("invalid packet type!" + packet);
			return null;
		}

		ConnectPacket myPacket = (ConnectPacket)packet;


		result = new LoginPacket();

		return result;
	}
}
