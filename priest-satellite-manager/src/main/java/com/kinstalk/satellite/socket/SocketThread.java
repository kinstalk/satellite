package com.kinstalk.satellite.socket;


import com.kinstalk.satellite.common.constant.ConstantSocket;

import com.kinstalk.satellite.domain.packet.SocketPacket;
import com.kinstalk.satellite.socket.manager.ManagerData;
import com.kinstalk.satellite.socket.manager.ManagerHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionException;


/**
 * Created by digitZhang on 16/5/12.
 */
public class SocketThread {

	private Logger logger = LoggerFactory.getLogger(SocketThread.class);
	private int ack;
	private String ip;
	private int port;
	private Agent agent;

	private ChannelFuture channelFuture;
	private EventLoopGroup group;

	// 管理的数据
	private ManagerData managerData;

	// socket 连接状态
	private int SOCKET_STATUS = ConstantSocket.CONNECT_DEFAULT; // Socket状态 ConstantSocket.CONNECT_*



	public SocketThread(Agent agent) {
		this.agent = agent;
		this.ip = agent.getIp();
		this.port = agent.getPort();
		managerData = new ManagerData();
	}

	public void run() {
		connect();
	}
	
	public void start() {
		new Thread() {
			public void run() {
				try {
					connect();
				} finally {
					logger.debug("Thread run over!");
				}
			}
		}.start();
	}

	public void disconnect() {
		channelFuture.channel().close();
	}

	public void send(SocketPacket packet) {
		try {
			// 更新ack
			packet.setAck(incrAck());


			// 记录发送状态
			managerData.addPacketStatus(agent, packet);

			// 写数据
			channelFuture.channel().write(packet);
			channelFuture.channel().flush();
			logger.debug("send " + packet);
		} catch(RejectedExecutionException e ) {
//			AgentManager.getOnlineAgent().remove(agent.getIp());
//			logger.error("agent is offline! " + agent);
		}
	}

	// 获取连接状态
	public boolean getSocketStatus()  {
		boolean result = false;

		try {
			if(SOCKET_STATUS == ConstantSocket.CONNECT_SUCC) {
				result = true;

			} else if(SOCKET_STATUS == ConstantSocket.CONNECT_DEFAULT
					|| SOCKET_STATUS == ConstantSocket.CONNECT_ON) {
				long startMilli = System.currentTimeMillis();

				while(System.currentTimeMillis() - startMilli < ConstantSocket.MAX_RECONNECT_SECONDS * 1000 ) {
					if(SOCKET_STATUS == ConstantSocket.CONNECT_SUCC) {
						result = true;
						break;
					}
					Thread.sleep(10);
				}
				logger.debug("isConnecting spend " + (System.currentTimeMillis() - startMilli) + "ms");
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result;


	}

	private void connect() {

		//配置客户端线程组
		group=new NioEventLoopGroup();
		try {
			logger.info("socket connect start!");
			// 开始连接
			SOCKET_STATUS = ConstantSocket.CONNECT_ON;

			//配置客户端启动辅助类
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
					.option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							//添加POJO对象解码器 禁止缓存类加载器
							ch.pipeline().addLast(new ObjectDecoder(ConstantSocket.MAX_SOCKET_PACKET_LENGTH, ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));
							//设置发送消息编码器
							ch.pipeline().addLast(new ObjectEncoder());
							//设置网络IO处理器
							ch.pipeline().addLast(new ManagerHandler(managerData));

						}
					});
			//发起异步服务器连接请求 同步等待成功
			channelFuture = b.connect(ip, port).sync();

			logger.info("socket connect success! agent[{}:{}]", ip, port);

			// 连接成功
			SOCKET_STATUS = ConstantSocket.CONNECT_SUCC;

			//等到客户端链路关闭
			channelFuture.channel().closeFuture().sync();

		} catch (Exception e) {
			logger.error("socket offline! agent[{}:{}]", this.ip, this.port, e);
		} finally{
			//优雅释放线程资源
			group.shutdownGracefully();
			SOCKET_STATUS = ConstantSocket.CONNECT_DEFAULT;
			logger.info("socket close!agent[{}:{}]", this.ip, this.port);
		}

	}


	private int incrAck() {
		ack++;
		if(ack > ConstantSocket.MAX_ACK) {
			ack = 1;
		}
		return ack;
	}

}
