package com.kinstalk.satellite.listener;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

import com.mchange.v2.c3p0.DataSources;

/**
 * C3P0连接池在context关闭的时候（关闭tomcat服务器或者reload context），不会回收资源，导致内存泄露。Tomcat会报如下错误：
 * <br />SEVERE: The web application [/xxx] appears to have started a thread named [com.mchange.v2.async.ThreadPoolAsynchronousRunner$PoolThread-#1] but has failed to stop it. This is very likely to create a memory leak.
 *
 * 本类在context关闭的时候释放c3p0连接池，解决此类存泄露问题。
 *
 * 另外，JDBC驱动包(例如ojdbc6.jar)放在$TOMCAT_HOME/lib下面，不要放在WEB-INF/lib里，可以解决JDBC驱动导致内存泄露的问题。此时Tomcat报如下错误:
 * SEVERE: A web application registered the JBDC driver [oracle.jdbc.OracleDriver] but failed to unregister it when the web application was stopped. To prevent a memory leak, the JDBC Driver has been forcibly unregistered.
 */
@WebListener
public class DbCloser implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
	}


	@Override
	public void
	contextDestroyed(ServletContextEvent sce) {
		//TODO 这里需要修改一下，得到dataSource
		DataSource dataSource = null;
		try {
			//调用c3p0的关闭数据库连接的方法
			if(dataSource != null) {
				DataSources.destroy(dataSource);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//等待连接池关闭线程退出，避免Tomcat报线程未关闭导致memory leak的错误
		try {
			Thread.sleep(
					1000
			);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}