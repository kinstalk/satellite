package com.kinstalk.satellite.service.impl;

import com.kinstalk.satellite.dao.*;
import com.kinstalk.satellite.domain.*;
import com.kinstalk.satellite.domain.agnentmodel.TimerModel;
import com.kinstalk.satellite.domain.packet.TimerStopPacket;
import com.kinstalk.satellite.domain.packet.TimerUpdatePacket;
import com.kinstalk.satellite.service.api.RunService;
import com.kinstalk.satellite.socket.AgentManager;
import com.kinstalk.satellite.utils.FileUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zhangchuanqi on 16/4/14.
 */
@Service
public class RunServiceImpl implements RunService {
	private Logger logger = LoggerFactory.getLogger(RunServiceImpl.class);

	@Resource
	private ScriptMapper scriptMapper;
	@Resource
	private RunDao runDao;
	@Resource
	private AgentDao agentDao;
	@Resource
	private PhonePoolDao phonePoolDao;
	@Resource
	private AgentStatusDao agentStatusDao;
	@Value("${jmeter.run.path}")
	private String jmeterPath;

	/**
	 * 初始化脚本下拉框
	 */
	@Override
	public List<Script> queryScriptList() {
		List<Script> list = null;

		try {
			list = scriptMapper.selectList();
		} catch (Exception e) {
			e.getMessage();
		}
		return list;
	}

	/**
	 * 查询脚本
	 */
	@Override
	public Script queryScript(Long id) {
		Script script = null;
		try {
			script = scriptMapper.selectById(id);
		} catch (Exception e) {
			e.getMessage();
		}
		return script;
	}

	/**
	 * 查询单条任务
	 */
	@Override
	public RunScriptTimer queryTimerById(Long id) {
		RunScriptTimer runScriptTimer = null;

		try {
			runScriptTimer = runDao.selectById(id);
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return runScriptTimer;
	}

	/**
	 * 查询任务列表
	 *
	 */
	public List<RunScriptTimer> queryTimerList() {
		List<RunScriptTimer> list = null;
		try {
			list = runDao.selectList();
		} catch (Exception e) {
			e.getMessage();
		}
		return list;
	}

	/**
	 * 查询任务详细
	 *
	 */
	public List<RunScriptTimerDetail> queryDetailList(Long id) {
		List<RunScriptTimerDetail> list = null;
		try {
			list = runDao.selectDetailList(id);
		} catch (Exception e) {
			e.getMessage();
		}
		return list;
	}

	/**
	 * 查询一条任务详细
	 *
	 */
	public RunScriptTimerDetail queryDetail(Long id) {
		RunScriptTimerDetail runScriptTimerDetail = null;
		try {
			runScriptTimerDetail = runDao.selectDetail(id);
		} catch (Exception e) {
			e.getMessage();
		}
		return runScriptTimerDetail;
	}

	/**
	 * 运行任务保存
	 *
	 */
	@Override
	public long saveTimer(RunScriptTimer runScriptTimer) {
		long id = 0;
		try {
			if (runScriptTimer != null) {
				if (runScriptTimer.getId() != null && runScriptTimer.getId() != 0) {
					if (runScriptTimer.getTimerType() == 1) {
						runScriptTimer.setSleepTime(0);
					}
					id = runDao.updateTimer(runScriptTimer);
				} else {
					Long currentTime = System.currentTimeMillis();
					runScriptTimer.setCreateTime(currentTime);
					runScriptTimer.setRunStatus(0);
					runScriptTimer.setTimerStatus(3);
					runScriptTimer.setTimerStatus(0);
					id = runDao.save(runScriptTimer);
					runScriptTimer.setId(id);
				}
			}
		} catch (Exception e) {
			e.getMessage();
			logger.error(e.getMessage(), e);
		}
		return id;
	}

	/**
	 * 分发
	 *
	 * @param id timerId
	 */
	@Override
	public String run(Long id) {

		if (AgentManager.getOnlineAgent().size() <= 0) {
			return "NotOnline";
		}
		RunScriptTimer runScriptTimer = queryTimerById(id);

		runScriptTimer.setRunStatus(1);
		runDao.update(runScriptTimer);
		try {
			// connect
			TimerModel timerModel = new TimerModel();
			timerModel.setTimerType(runScriptTimer.getTimerType());
			timerModel.setSleepTime(runScriptTimer.getSleepTime());
			timerModel.setId(id);
			timerModel.setRunType(runScriptTimer.getRunType());
			timerModel.setAppId(runScriptTimer.getAppId());

			List<AgentModel> listModel = agentDao.selectList();

			for (AgentModel agentModel : listModel) {
				if (AgentManager.getOnlineAgent().get(agentModel.getAgentIp()) == null) {
					continue;
				}
				AgentStatus agentStatus = new AgentStatus();
				agentStatus.setAgentId(agentModel.getAgentId());
				agentStatus.setTimerId(timerModel.getId());
				agentStatus.setLastRuntime(null);
				agentStatus.setUpdateTime(0L);
				agentStatus.setStatus(1);
				if (agentStatusDao.selectById(agentStatus) == null) {
					agentStatusDao.save(agentStatus);
				}
			}

			//给各个agent 发不同的文件
			for (int i = 0; i < listModel.size(); i++) {
				try {
					String url = scriptMapper.selectById(runScriptTimer.getScriptId()).getPath();
					PhonePool phonePool = new PhonePool();
					phonePool.setAgentId((long) listModel.get(i).getAgentId());
					phonePool.setTimerId(timerModel.getId());
					phonePool = phonePoolDao.selectExist(phonePool);

					//先查询该脚本是否已占用号段
					if (phonePool == null) {
						do {
							//如果没有占用 则查询一组号段
							phonePool = phonePoolDao.selectSingle();
							if (phonePool == null) {
								return "phoneNull";
							}
							phonePool.setAgentId((long) listModel.get(i).getAgentId());
							phonePool.setTimerId(timerModel.getId());
						}
						//将未占用号段置为已使用
						while (phonePoolDao.update(phonePool) != 1);
					}

					//分配用户
					replaceFile(url, phonePool);
					timerModel.setFile(FileUtils.readFile(url));
					AgentManager.sendSingle(new TimerUpdatePacket(timerModel), listModel.get(i).getAgentIp());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}

	public void replaceFile(String url, PhonePool phonePool) {
		int UserCode = 65;
		Long phoneNumber = Long.parseLong(phonePool.getPhoneStart());
		Long deviceNumber = Long.parseLong(phonePool.getPhoneStart());
		SAXReader reader = new SAXReader();

		try {
			Document doc = reader.read(url);
			List list = doc.selectNodes("jmeterTestPlan/hashTree/hashTree/Arguments");

			for (Object o : list) {
				Element e = (Element) o;
				if ("true".equals(e.attribute("enabled").getValue())) {
					List list1 = e.selectNodes("collectionProp/elementProp");
					for (Object temp : list1) {
						Element e1 = (Element) temp;
						if (e1.attribute("name").getValue().endsWith("mobile")) {
							((Element) e1.elements().get(1)).setText(phoneNumber.toString());
							phoneNumber++;
						} else if (e1.attribute("name").getValue().endsWith("deviceId")) {
							((Element) e1.elements().get(1)).setText(((char) UserCode) + "_" + deviceNumber.toString());
							UserCode++;
							deviceNumber++;
						}
					}
				}
				FileUtils.saveDocument(doc, url);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 停止
	 * 0:未运行 1:等待运行 2:正在运行 3:停止运行 4:执行最后一次
	 *
	 * @param id timerId
	 */
	@Override
	public void stop(Long id) {
		RunScriptTimer runScriptTimer = queryTimerById(id);
		runScriptTimer.setRunStatus(0);
		runDao.update(runScriptTimer);

		AgentManager.send(new TimerStopPacket(id));
		runDao.update(runScriptTimer);
		agentStatusDao.updatebyid(id);
	}



	/**
	 * 删除任务
	 *
	 * @param id timerId
	 * @return delete row num
	 */
	@Override
	public long deleteTimer(Long id) {
		long rows = 0;
		try {

			rows = runDao.delete(id);

			RunScriptTimer runScriptTimer = queryTimerById(id);

			AgentManager.send(new TimerStopPacket(id));
			runDao.update(runScriptTimer);

			agentStatusDao.updatebyid(id);

		} catch (Exception e) {
			e.getMessage();

		}
		return rows;
	}
}
