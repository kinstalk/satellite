package com.kinstalk.satellite.socket.agent;

import com.kinstalk.satellite.common.constant.ConstantSocket;
import com.kinstalk.satellite.domain.packet.SocketPacket;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;

/**
 * Created by digitZhang on 16/5/12.
 */
@Service
public class SocketAgent implements  ISocketAgent {
	private static Logger logger= LoggerFactory.getLogger(SocketAgent.class);

	private static  ChannelHandlerContext managerChannel = null;

	public void bind(int port) throws Exception{
		//配置NIO线程组
		EventLoopGroup bossGroup=new NioEventLoopGroup();
		EventLoopGroup workerGroup=new NioEventLoopGroup();
		try{
			//服务器辅助启动类配置
			ServerBootstrap b=new ServerBootstrap();
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 100)
					.handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							//添加对象解码器 负责对序列化POJO对象进行解码 设置对象序列化最大长度为1M 防止内存溢出
							//设置线程安全的WeakReferenceMap对类加载器进行缓存 支持多线程并发访问  防止内存溢出
							ch.pipeline().addLast(new ObjectDecoder(ConstantSocket.MAX_SOCKET_PACKET_LENGTH,ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
							//添加对象编码器 在服务器对外发送消息的时候自动将实现序列化的POJO对象编码
							ch.pipeline().addLast(new ObjectEncoder());
							ch.pipeline().addLast(new AgentHandler());
						}
					});
			//绑定端口 同步等待绑定成功
			ChannelFuture channelFuture=b.bind(port).sync();
			logger.info("bind port:"+port+" success!");
			//等到服务端监听端口关闭
			channelFuture.channel().closeFuture().sync();
		}finally{
			//优雅释放线程资源
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
			logger.info("socket close");
		}
	}

	/**
	 * 发数据包给manager
	 * @param packet
	 */
	public static void sendPacket(SocketPacket packet) {
		if(managerChannel == null) {
			logger.error("managerChannel is close!");
			return;
		}
		managerChannel.writeAndFlush(packet);
	}

	public static void channelRegist(ChannelHandlerContext ctx) {
		managerChannel = ctx;
	}
	public static void channelUnRegist(ChannelHandlerContext ctx) {
		managerChannel = null;
	}

	@PostConstruct
	public static void init() throws Exception  {
		new Thread() {

			@Override
			public void run() {
				int port = 9000;
				try {
					new SocketAgent().bind(port);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();

	}
	public static void main(String[] args) throws Exception {
		int port = 9000;
		new SocketAgent().bind(port);

	}
}
