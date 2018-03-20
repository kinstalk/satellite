package com.kinstalk.satellite.socket;


import com.kinstalk.satellite.domain.packet.ConnectPacket;
import com.kinstalk.satellite.domain.packet.SocketPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by digitZhang on 16/6/8.
 */
public class AgentManager {

	private static Logger logger = LoggerFactory.getLogger(AgentManager.class);



	// 保存所有在线Agent对象  key:ip value:Agent
	private static HashMap<String, Agent> onlineAgent = new HashMap<String, Agent>();



	/**
	 * 连接Agent
	 * @param listAgent
	 */
	public static String connectAgent(List<Agent> listAgent) {

		String  strOutLine="";
		for(Agent agent : listAgent) {

			Agent curr = onlineAgent.get(agent.getIp());
			if(curr!=null && curr.isConnecting()){
				continue;
			}
			agent.getSocket().start();
			boolean	 isSucc = agent.isConnecting();
			if(isSucc) {
				agent.setLastPongTime(System.currentTimeMillis());
				onlineAgent.put(agent.getIp(), agent);
			} else {
				logger.error("connect fail!" + agent);
				strOutLine+=agent.getIp()+",";
			}
		}
		return  strOutLine;
	}

	/**
	 * 链接单个agent
	 * @param agent
	 */

	public static  boolean  connectSingleAgent(Agent agent){
		boolean result = false;
		try {
			agent.getSocket().start();
			result = agent.getSocket().getSocketStatus();
			if (result) {
				agent.setLastPongTime(System.currentTimeMillis());
				onlineAgent.put(agent.getIp(), agent);

				logger.info("connect single agent success!"+agent);
			} else {
				logger.error("connect fail!" + agent);

			}
		} catch(Exception e) {
			logger.error("{}", e);
		}
		return result;

	}
	public static void send(String ip, SocketPacket packet) {
		Agent agent = onlineAgent.get(ip);
		if(agent != null) {
			agent.getSocket().send(packet);
		}
	}

	public static Agent getAgentByIp(String ip) {
		return onlineAgent.get(ip);
	}

	public static void send(SocketPacket packet) {
		try{
			Collection<Agent> coll = onlineAgent.values();
			if(coll != null && coll.size() > 0) {
				for(Agent agent : coll) {

					agent.getSocket().send(packet);
				}
			} else {
				logger.error("no online Agent!");
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}

	}

	public static void sendSingle(SocketPacket packet,String ip) {


		if(onlineAgent.get(ip) != null ) {
			onlineAgent.get(ip).getSocket().send(packet);
		} else {
			logger.error("no online Agent!");
		}
	}

	public static void disconnect(String ip) {
		Agent agent = onlineAgent.get(ip);
		if(agent != null) {
			agent.getSocket().disconnect();
			logger.info("ip:{} disconnect!", ip);
		}
	}

	public static void disconnect() {
		Collection<Agent> coll = onlineAgent.values();
		if(coll != null && coll.size() > 0) {
			for(Agent agent : coll) {
				agent.getSocket().disconnect();
				logger.info("ip:{} disconnect!", agent.getIp());
			}
		}
	}

	public static void main(String args[]) {
		AgentManager manager = new AgentManager();

		// init agent
		List<Agent> listAgent = new ArrayList<>();
		Agent agent = new Agent(1, "127.0.0.1", 9000, "token");
		listAgent.add(agent);

		// connect
		manager.connectAgent(listAgent);

		SocketPacket packet = new ConnectPacket();

		send(packet);

	}

	private static void sleep(int milliSeconds) {
		try {
			Thread.sleep(milliSeconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static HashMap<String, Agent> getOnlineAgent() {
		return onlineAgent;
	}

	public static void setOnlineAgent(HashMap<String, Agent> onlineAgent) {
		AgentManager.onlineAgent = onlineAgent;
	}


}
