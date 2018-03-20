package com.kinstalk.satellite.service.api;

import com.kinstalk.satellite.domain.AgentModel;
import com.kinstalk.satellite.domain.AgentStatus;

import java.util.List;

/**
 * Created by zhangchuanqi on 16/6/24.
 */
public interface AgentService {

	List<AgentModel> selectList();

	AgentModel selectById(Long id);

	long delete(Long id);

	long save(AgentModel agentModel);

	List<AgentStatus> findStatusList();

	void updateRunStatus(Long timerId, Integer agentId, Integer status);
}
