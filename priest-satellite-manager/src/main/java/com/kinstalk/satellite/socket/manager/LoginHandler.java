package com.kinstalk.satellite.socket.manager;


import com.kinstalk.satellite.domain.packet.LoginPacket;
import com.kinstalk.satellite.domain.packet.LoginRespPacket;
import com.kinstalk.satellite.domain.packet.SocketPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by digitZhang on 16/6/8.
 */
public class LoginHandler {

	private Logger logger= LoggerFactory.getLogger(LoginHandler.class);

	public SocketPacket handle(SocketPacket packet) {
		SocketPacket result = null;

		if(! (packet instanceof LoginPacket)) {
			logger.error("invalid packet type!" + packet);
			return null;
		}

		result = new LoginRespPacket();

		return result;
	}
}
