package com.kinstalk.satellite;


import com.kinstalk.satellite.dao.AgentDao;
import com.kinstalk.satellite.domain.AgentModel;

import com.kinstalk.satellite.domain.packet.ConnectPacket;
import com.kinstalk.satellite.domain.packet.SocketPacket;
import com.kinstalk.satellite.socket.Agent;
import com.kinstalk.satellite.socket.AgentManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangchuanqi on 16/7/19.
 */
@Component
public class LoadAllAgent {


	@Resource
	private AgentDao agentDao;


	public void load() {
		new Thread() {
			@Override
			public void run() {
				List<Agent> listAgent = new ArrayList<>();

				List<AgentModel> listmodel = agentDao.selectList();
				for (int i = 0; i < listmodel.size(); i++) {
					Agent agent = new Agent(listmodel.get(i).getAgentId(), listmodel.get(i).getAgentIp(), 9000, "token");
					listAgent.add(agent);
				}

				AgentManager.connectAgent(listAgent);
			}
		}.start();

	}
}