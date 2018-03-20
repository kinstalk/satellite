package com.kinstalk.satellite.service.impl;

import com.kinstalk.satellite.dao.AgentDao;
import com.kinstalk.satellite.dao.AgentStatusDao;
import com.kinstalk.satellite.domain.AgentModel;
import com.kinstalk.satellite.domain.AgentStatus;
import com.kinstalk.satellite.domain.packet.TimerResultPacket;
import com.kinstalk.satellite.service.api.AgentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zhangchuanqi on 16/6/24.
 */
@Service ("agentService")
public class AgentServiceImpl implements AgentService {

    private Logger logger= LoggerFactory.getLogger(ScriptServiceImpl.class);
    @Resource
    private AgentDao agentDao;

    @Resource
    private AgentStatusDao agentStatusDao;

	/**
	 * 
	 * @param timerId timerId
	 * @param agentId agentId
	 * @param status  0:离线 1：等待运行 2：运行中
	 */
	@Override
	public void updateRunStatus(Long timerId, Integer agentId, Integer status) {
		//更新agent 状态
		AgentStatus agentStatus = new AgentStatus();
		agentStatus.setTimerId(timerId);
		agentStatus.setAgentId(agentId);
		agentStatus.setLastRuntime(System.currentTimeMillis());
		agentStatus.setStatus(status);
		agentStatusDao.update(agentStatus);
	}
	

    @Override
    public List<AgentModel> selectList() {
        List<AgentModel> list = null;


        try {

            list= agentDao.selectList();

        } catch (Exception e) {
            e.getMessage();

        }
        return list;
    }
    @Override
    public AgentModel selectById(Long id) {
        AgentModel agentModel= null;
        try {

            agentModel = agentDao.selectById(id);

        } catch (Exception e) {
            e.getMessage();

        }
        return agentModel;
    }
    @Override
    public long save(AgentModel agentModel) {
        long rows = 0;
        try {
            if (agentModel != null) {
                Long currentTime = System.currentTimeMillis();
                agentModel.setCreateTime(currentTime);
                if (agentModel.getAgentId() != null && agentModel.getAgentId() != 0) {
                    //更新
                    rows = agentDao.update(agentModel);
                } else {
                    //插入
                    rows = agentDao.save(agentModel);
                }
            }
        } catch (Exception e) {
            e.getMessage();

            logger.error(e.getMessage(),e);

        }
        return rows;
    }
    @Override
    public long delete(Long id) {
        long rows = 0;
        try {

            rows = agentDao.delete(id);

        } catch (Exception e) {

           logger.error( e.getMessage());

        }
        return rows;
    }
    @Override
    public List<AgentStatus> findStatusList(){
        try {


            List<AgentStatus> list= agentStatusDao.selectByAgentid();
            if(list.size()>0){
                return  list;
            }

        } catch (Exception e) {

            logger.error( e.getMessage());

        }
        return  null;
    }
}
