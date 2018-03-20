package com.kinstalk.satellite.socket.manager;


import com.kinstalk.satellite.domain.packet.SocketPacket;
import com.kinstalk.satellite.domain.packet.TimerResultPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

/**
 * Created by zhangchuanqi on 16/6/22.
 */

public class TimerResultHandler {
    private Logger logger= LoggerFactory.getLogger(TimerResultHandler.class);


    public void handle(SocketPacket packet,int agentId) {

        try{
            if(! (packet instanceof TimerResultPacket)) {
                logger.error("invalid packet type!" + packet);
            }

            TimerResultPacket timerResultPacket = (TimerResultPacket)packet;
			
			ApplicationContext act = ContextLoader.getCurrentWebApplicationContext();
            TimerAnalysis timerAnalysis= (TimerAnalysis) act.getBean("timerAnalysis");
            timerAnalysis.handleResult(timerResultPacket,agentId);


        }catch (Exception e){
            e.printStackTrace();
        }



    }

}
