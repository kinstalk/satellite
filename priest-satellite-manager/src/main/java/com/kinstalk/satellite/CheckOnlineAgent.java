package com.kinstalk.satellite;

import com.kinstalk.satellite.dao.AgentDao;
import com.kinstalk.satellite.domain.AgentModel;
import com.kinstalk.satellite.domain.packet.PingPacket;
import com.kinstalk.satellite.socket.Agent;
import com.kinstalk.satellite.socket.AgentManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhangchuanqi on 16/8/4.
 */


@Component
public class CheckOnlineAgent extends  Thread {

    private static ScheduledExecutorService schedule = Executors.newSingleThreadScheduledExecutor();  // 定时任务
    private static Logger logger = LoggerFactory.getLogger(CheckOnlineAgent.class);

    public  void check(){
        logger.info("检查在线Agent线程启动");
//        Iterator it = AgentManager.getOnlineAgent().keySet().iterator();
//        long  iniTime =System.currentTimeMillis();
//        while(it.hasNext()){
//             String key = (String) it.next();
//             AgentManager.getOnlineAgent().get(key).setLastPongTime(iniTime);
//        }


        Runnable runnable = new Runnable() {

            public void run(){
                try {
					logger.debug("check online agent start...");
					//检测在线的Agent 是否离线
                    PingPacket pingPacket=new PingPacket();
                    AgentManager.send(pingPacket);

                    long time=System.currentTimeMillis();

                    Iterator it = AgentManager.getOnlineAgent().keySet().iterator();
                    while(it.hasNext()){
                        String key = (String) it.next();
						long spendTime = time-(AgentManager.getOnlineAgent().get(key).getLastPongTime());
						logger.debug("after last pong time:{}ms, (agent [{}] )", spendTime);
                        if(spendTime > 90*1000){

                            logger.info("outlineAgent" + AgentManager.getOnlineAgent().get(key).toString());
                            Agent agent=AgentManager.getOnlineAgent().get(key);
                            it.remove();

                            for(int i = 0; i < 3; i++){
                                boolean result = AgentManager.connectSingleAgent(agent);
								if(result) {
									logger.info("reconnect agent [{}] success", agent.getIp());
									break;
								}
								logger.error("reconnect agent [{}] fail! retry nums:{} ", agent.getIp(), i+1);
                                sleep(1000 * 3);
                            }
                        }
                    }
                }catch (Exception e){

                    e.printStackTrace();
                } finally {
					logger.debug("check online agent end. online nums:{}", AgentManager.getOnlineAgent().size());
				}
            }

        };

        schedule.scheduleAtFixedRate(runnable, 30,60,TimeUnit.SECONDS);
    }


}
