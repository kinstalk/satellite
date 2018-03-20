package com.kinstalk.satellite.socket.agent;

import com.kinstalk.satellite.common.constant.ConstantSocket;
import com.kinstalk.satellite.domain.SocketUtil;
import com.kinstalk.satellite.domain.packet.SocketPacket;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by digitZhang on 16/5/12.
 */
public class AgentHandler extends ChannelHandlerAdapter {
	private Logger logger = LoggerFactory.getLogger(AgentHandler.class);

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		super.channelRegistered(ctx);
		SocketAgent.channelRegist(ctx);
		logger.debug("manager[{}] register! ", ctx.channel().remoteAddress());
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		super.channelUnregistered(ctx);
		logger.debug("manager[{}] unregister!", ctx.channel().remoteAddress());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		SocketPacket recvPacket = (SocketPacket) msg;
		int packetType = recvPacket.getType();

		long startMilli = System.currentTimeMillis();

		// ack ping包不输出日志
		if (needLog(packetType)) {
			logger.info("recv:" + recvPacket.toString());
		}

		// 自动处理ack
		SocketUtil.responseAck(ctx, recvPacket);

		SocketPacket sendPacket = null;
		switch (packetType) {

			case ConstantSocket.ACK:

				break;

			case ConstantSocket.CONNECT:
				sendPacket = new ConnectHandler().handle(recvPacket);
				break;

			case ConstantSocket.PING:
				sendPacket = new PingHandler().handle(recvPacket);
				break;

			case ConstantSocket.TIMER_UPDATE:
				new TimerUpdateHandler().handle(recvPacket);
				break;
			case ConstantSocket.TIMER_STOP:
				new TimerStopHandler().handle(recvPacket);
				break;
			default://无法处理
				logger.error("unknown packet!");
				break;
		}

		if (sendPacket != null) {
			//ctx.writeAndFlush(sendPacket);
			SocketAgent.sendPacket(sendPacket);
			if (needLog(packetType)) {
				logger.info("send:" + sendPacket);
			}
		}
		logger.debug("handler spend " + (System.currentTimeMillis() - startMilli) + "ms");
	}

	private boolean needLog(int type) {
		return type != ConstantSocket.ACK
				&& type != ConstantSocket.PING;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}

