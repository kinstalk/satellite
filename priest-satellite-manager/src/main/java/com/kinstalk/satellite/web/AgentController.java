package com.kinstalk.satellite.web;

import com.kinstalk.satellite.common.constant.CommonConstant;
import com.kinstalk.satellite.common.utils.SatelliteConstants;
import com.kinstalk.satellite.dao.RunDao;
import com.kinstalk.satellite.domain.*;
import com.kinstalk.satellite.service.api.AgentService;
import com.kinstalk.satellite.socket.Agent;
import com.kinstalk.satellite.socket.AgentManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangchuanqi on 16/6/24.
 */

@Controller
@RequestMapping(value = "/agent", method = {RequestMethod.GET, RequestMethod.POST})
public class AgentController {

	private static final Logger logger = LoggerFactory.getLogger(AgentController.class);

	@Resource
	private AgentService agentService;

	@Resource
	private RunDao runDao;


	/**
	 * agent list
	 *
	 * @param currentPage
	 * @param view
	 */
	@RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
	public void list(@RequestParam(value = "currentPage", required = false, defaultValue = "0") int currentPage,
					 Model view) {


		List<AgentModel> result = agentService.selectList();//reportLastApiService

		for (AgentModel agentModel : result) {
			Agent agent = AgentManager.getOnlineAgent().get(agentModel.getAgentIp());
			if (agent != null && agent.isConnecting()) {
				agentModel.setStatus(1);
			}

		}

		if (result == null || result.size() == 0) {
			String.format(SatelliteConstants.WEB_IFRAME_SCRIPT, "查询失败！");

		}
		//放入list对象。
		view.addAttribute("list", result);
	}

	/**
	 * single Agent
	 *
	 * @param id
	 */
	@RequestMapping(value = "/new_edit", method = {RequestMethod.POST})
	public String selectById(Long id, Model view) {
		if (id != null && id != 0) {
			AgentModel result = agentService.selectById(id);
			if (result == null) {
				String.format(SatelliteConstants.WEB_IFRAME_SCRIPT, "查询失败！");

			}
			view.addAttribute("Agent", result);
		}

		return "/agent/new_edit";
	}

	@RequestMapping(value = "/save", method = {RequestMethod.POST})
	@ResponseBody
	public String save(AgentModel agentModel, Model view) {
		try {
			long rows = agentService.save(agentModel);
			view.addAttribute("AgentModel", agentModel);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "success";
	}

	@RequestMapping(value = "/delete", method = {RequestMethod.DELETE})
	@ResponseBody
	public String delete(@RequestParam(value = "id", required = false, defaultValue = "0") Long id, Model view) {
		try {
			agentService.delete(id);
			return "success";
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "fail";
		}
	}

	/**
	 * 查看任务状态详细
	 */
	@RequestMapping(value = "/showlist", method = {RequestMethod.POST})
	public String showlist(Model view) {


		List<AgentStatus> result = agentService.findStatusList();

		if (result == null || result.size() == 0) {
			String.format(SatelliteConstants.WEB_IFRAME_SCRIPT, "查询失败！");

		}

		//放入list对象。
		view.addAttribute("detailList", result);

		return "/agent/detail";

	}

	/**
	 * 查看执行详细
	 */
	@RequestMapping(value = "/finddetail", method = {RequestMethod.POST})
	public String finddetail(@RequestParam(value = "agentid", required = false, defaultValue = "0") Integer agentid, @RequestParam(value = "timerid", required = false, defaultValue = "0") Long timerid, Model view) {


		RunScriptTimerDetail runScriptTimerDetail = new RunScriptTimerDetail();
		runScriptTimerDetail.setAgentId(agentid);
		runScriptTimerDetail.setTimerId(timerid);

		Long createTime = System.currentTimeMillis() - CommonConstant.TWENTY_FOUR_HOURS;
		runScriptTimerDetail.setCreateTime(createTime);
		List<RunScriptTimerDetail> result = runDao.selectAgentDetail(runScriptTimerDetail);
		;

		if (result == null || result.size() == 0) {
			String.format(SatelliteConstants.WEB_IFRAME_SCRIPT, "查询失败！");

		}

		//放入list对象。
		view.addAttribute("detailList", result);

		return "/run/detail";
	}

	/**
	 * checkAgent
	 * 检查离线agent是否在线
	 */
	@RequestMapping(value = "/check", method = {RequestMethod.POST})
	@ResponseBody
	public String checkAgent(Model view) {

		List<AgentModel> listModel = agentService.selectList();

		List<AgentModel> retryList = new ArrayList<>();

		for (AgentModel agentModel : listModel) {
			Agent agent = AgentManager.getOnlineAgent().get(agentModel.getAgentIp());
			if (agent == null || !agent.isConnecting()) {
				retryList.add(agentModel);
			}
		}

		if (retryList.size() > 0) {
			List<Agent> listAgent = new ArrayList<>();
			for (int i = 0; i < retryList.size(); i++) {
				Agent agent = new Agent(retryList.get(i).getAgentId(), retryList.get(i).getAgentIp(), 9000, "token");
				listAgent.add(agent);
			}


			String strOutLine = AgentManager.connectAgent(listAgent);


			if ("".equals(strOutLine)) {
				return "success";
			} else {
				return strOutLine;
			}
		} else {
			return "none";
		}
	}
}
