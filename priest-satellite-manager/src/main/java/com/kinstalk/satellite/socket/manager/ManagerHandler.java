package com.kinstalk.satellite.socket.manager;
import com.kinstalk.satellite.common.constant.ConstantSocket;
import com.kinstalk.satellite.domain.SocketUtil;
import com.kinstalk.satellite.domain.packet.SocketPacket;
import com.kinstalk.satellite.socket.Agent;
import com.kinstalk.satellite.socket.AgentManager;



import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.TooLongFrameException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Created by digitZhang on 16/5/12.
 */
public class ManagerHandler extends ChannelHandlerAdapter {
	private Logger logger= LoggerFactory.getLogger(ManagerHandler.class);

	private ManagerData managerData;

	public ManagerHandler(ManagerData managerData) {
		this.managerData = managerData;
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		String remoteIP = getIP(ctx);
		AgentManager.disconnect(remoteIP);
	}

	/**
	 * 链路链接成功
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {

		try{
			SocketPacket recvPacket = (SocketPacket) msg;

			logger.debug("recv:" + recvPacket.toString());
			
			// 自动应答ack
			SocketUtil.responseAck(ctx, recvPacket);
			
			String remoteIP = getIP(ctx);

			// 更新packet处理结果
			managerData.updPacketStatusAckTime(recvPacket);

			Agent agent = AgentManager.getAgentByIp(remoteIP);
			SocketPacket sendPacket = null;
			switch (recvPacket.getType()) {
				case ConstantSocket.ACK:
					break;

				case ConstantSocket.PONG:
					if(agent == null) {
						logger.error("receive pong package, agent is offline! ip:{}", remoteIP);
					}

				    new PongHandler().handle(recvPacket, agent) ;
					break;
				case ConstantSocket.LOGIN:
					//sendPacket = new LoginHandler().handle(recvPacket);
					break;
				case ConstantSocket.TIMER_RUN_START:
					new TimerRunStartHandler().handle(recvPacket, agent.getId());
					break;
				case ConstantSocket.TIMER_RUN_END:
					new TimerRunEndHandler().handle(recvPacket, agent.getId());
					break;
				case ConstantSocket.TIMER_RESULT:
					new TimerResultHandler().handle(recvPacket, AgentManager.getAgentByIp(remoteIP).getId());
					break;
				default://无法处理
					logger.error( "unknown packet! {}", recvPacket);
					break;
			}


		}
		catch (Exception e){
			e.printStackTrace();
		}

	}
	
	private String getIP(ChannelHandlerContext ctx) {
		InetSocketAddress address = (InetSocketAddress)ctx.channel().remoteAddress();

		InetAddress inetAddress = address.getAddress();
		//System.out.println(inetAddress.getHostName() + ", " + inetAddress + ", " + inetAddress.getHostAddress());
		return inetAddress.getHostAddress();
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		if (cause instanceof TooLongFrameException) {
			logger.error("netty error, too long data frame! please resize channel maxObjectSize");
		}
		cause.printStackTrace();
		
	}


}
