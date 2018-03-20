package com.kinstalk.satellite.domain.packet;


import com.kinstalk.satellite.common.constant.ConstantSocket;

/**
 * Created by digitZhang on 16/5/12.
 */
public class AckPacket extends SocketPacket {
	public AckPacket(int ack) {
		type = ConstantSocket.ACK;
		setAck(ack);
	}
}
