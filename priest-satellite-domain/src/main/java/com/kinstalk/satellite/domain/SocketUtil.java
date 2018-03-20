package com.kinstalk.satellite.domain;

import com.kinstalk.satellite.common.constant.ConstantSocket;
import com.kinstalk.satellite.domain.packet.AckPacket;
import com.kinstalk.satellite.domain.packet.SocketPacket;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by digitZhang on 16/6/8.
 */
public class SocketUtil {

	public static void responseAck(ChannelHandlerContext ctx, SocketPacket packet) {
		if(packet.getType() == ConstantSocket.ACK||packet.getType() == ConstantSocket.PING) {
			return;
		}

		if(packet.getAck() > 0) {
			ctx.write(new AckPacket(packet.getAck()));
			ctx.flush();
		}
	}
}

