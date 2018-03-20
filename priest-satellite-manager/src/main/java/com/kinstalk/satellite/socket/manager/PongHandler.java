package com.kinstalk.satellite.socket.manager;


import com.kinstalk.satellite.domain.packet.LoginPacket;
import com.kinstalk.satellite.domain.packet.LoginRespPacket;
import com.kinstalk.satellite.domain.packet.PongPacket;
import com.kinstalk.satellite.domain.packet.SocketPacket;
import com.kinstalk.satellite.socket.Agent;
import com.kinstalk.satellite.socket.AgentManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by digitZhang on 16/6/8.
 */
public class PongHandler {

	private Logger logger= LoggerFactory.getLogger(PongHandler.class);

	public void handle(SocketPacket packet,Agent agent) {
		SocketPacket result = null;

		if(! (packet instanceof PongPacket)) {
			logger.error("invalid packet type!" + packet);
			return;
		}
	
		if (agent == null) {
			return;
		}
		
		agent.setLastPongTime(System.currentTimeMillis());




	}
}
