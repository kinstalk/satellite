package com.kinstalk.satellite.socket.manager;


import com.kinstalk.satellite.domain.AgentModel;
import com.kinstalk.satellite.domain.packet.SocketPacket;
import com.kinstalk.satellite.domain.packet.TimerRunEndPacket;
import com.kinstalk.satellite.domain.packet.TimerRunStartPacket;
import com.kinstalk.satellite.service.api.AgentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

/**
 * Created by digitZhang on 16/6/22.
 */

public class TimerRunEndHandler {
	private Logger logger = LoggerFactory.getLogger(TimerRunEndHandler.class);

	public void handle(SocketPacket packet, int agentId) {

		try {
			if (!(packet instanceof TimerRunEndPacket)) {
				logger.error("invalid packet type!" + packet);
			}

			TimerRunEndPacket pkt = (TimerRunEndPacket) packet;

			if(!pkt.isSucc()) {
				TimerAnalysis.sendMessage(pkt.getAppId(), "run jmeter fail!");
			}

			ApplicationContext act = ContextLoader.getCurrentWebApplicationContext();
			AgentService agentService = (AgentService) act.getBean("agentService");
			agentService.updateRunStatus(pkt.getTimerId(), agentId, AgentModel.STATUS_WAIT);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
