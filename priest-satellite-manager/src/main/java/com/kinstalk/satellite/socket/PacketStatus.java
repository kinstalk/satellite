package com.kinstalk.satellite.socket;


import com.kinstalk.satellite.domain.packet.SocketPacket;

/**
 * Created by digitZhang on 16/6/8.
 */
public class PacketStatus {
	private Agent agent;
	private SocketPacket packet;
	private long sendTime;
	private long recvAckTime;
	private long recvResponseTime;

	public PacketStatus(Agent agent, SocketPacket packet) {
		this.agent = agent;
		this.packet = packet;
		sendTime = System.currentTimeMillis();
	}


	public void updateRecvAckTime() {
		this.recvAckTime = System.currentTimeMillis();
	}

	public void updateRecvResponseTime() {
		this.recvResponseTime = System.currentTimeMillis();
	}

	public long getSendMilli() {
		return recvAckTime > 0 && sendTime > 0 ? recvAckTime - sendTime : -1;
	}

	public long getTotalMilli() {
		return recvResponseTime > 0 && sendTime > 0 ? recvResponseTime - sendTime : -1;
	}
}
