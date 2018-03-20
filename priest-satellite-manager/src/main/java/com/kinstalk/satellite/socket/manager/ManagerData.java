package com.kinstalk.satellite.socket.manager;


import com.kinstalk.satellite.common.constant.ConstantSocket;
import com.kinstalk.satellite.socket.Agent;
import com.kinstalk.satellite.socket.PacketStatus;
import com.kinstalk.satellite.domain.packet.SocketPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by digitZhang on 16/6/8.
 */
public class ManagerData {
	Logger logger = LoggerFactory.getLogger(ManagerData.class);

	private HashMap<Integer, PacketStatus> sendPacketStatus = new LinkedHashMap<Integer, PacketStatus>(500);

	public void addPacketStatus(Agent agent, SocketPacket packet) {
		PacketStatus obj = new PacketStatus(agent, packet);
		sendPacketStatus.put(packet.getAck(), obj);
	}

	public void updPacketStatusAckTime(SocketPacket packet) {
		if(packet == null || packet.getAck() <= 0) {
			return;
		}

		int ack = packet.getAck();

		PacketStatus obj = sendPacketStatus.get(ack);
		if(obj == null) {
			//logger.error("update ack time fail! packetState find by ack[{}] is empty!", ack);
			return;
		}

		// 包类型为ACK
		if(packet.getType() == ConstantSocket.ACK) {
			obj.updateRecvAckTime();
			logger.debug("packet ack={} receive ack. spend {} ms", ack, obj.getSendMilli());
		} else {
			obj.updateRecvResponseTime();
			logger.debug("packet ack={} receive response. spend {} ms", ack, obj.getTotalMilli());
		}
	}

	public Collection<PacketStatus> getAllPacketStatus() {
		return sendPacketStatus.values();
	}

}
