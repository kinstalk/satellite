package com.kinstalk.satellite;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created by zhangchuanqi on 16/8/5.
 */

@Component
public class InitSchedule {

    @Resource
    private LoadAllAgent loadAllAgent;

    @Resource
    private CheckOnlineAgent checkOnlineAgent;

    @PostConstruct
    public void init() {

		//初始化连接Agent
		loadAllAgent.load();

		//check Agent 在线
		checkOnlineAgent.check();

    }
}
