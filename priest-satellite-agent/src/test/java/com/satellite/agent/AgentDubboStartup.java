package com.satellite.agent;

import com.alibaba.dubbo.container.Main;

import java.io.IOException;

/**
 * Unit test for simple App.
 */
public class AgentDubboStartup {
    public static void main(String[] args) throws IOException {
//        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/dubbo-provider.xml");
//        context.start();
//        System.in.read();

        Main.main(args);

    }
}
