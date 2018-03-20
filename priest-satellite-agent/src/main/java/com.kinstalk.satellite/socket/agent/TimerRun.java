package com.kinstalk.satellite.socket.agent;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.kinstalk.satellite.common.utils.ExecSystemCommand;
import com.kinstalk.satellite.domain.agnentmodel.TimerModel;
import com.kinstalk.satellite.domain.agnentmodel.TimerStatus;
import com.kinstalk.satellite.domain.packet.SocketPacket;
import com.kinstalk.satellite.domain.packet.TimerResultPacket;
import com.kinstalk.satellite.domain.packet.TimerRunEndPacket;
import com.kinstalk.satellite.domain.packet.TimerRunStartPacket;
import com.kinstalk.satellite.socket.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhangchuanqi on 16/6/21.
 */

public class TimerRun {

	private Logger logger = LoggerFactory.getLogger(TimerRun.class);
	private TimerStatus status;                    // 运行状态
	private TimerModel currTimer;                    // 正在运行的timer

	private Boolean isUpdate = false;
	private String jmxPath;
	private String jmeterPath;

	private ScheduledExecutorService schedule = Executors.newSingleThreadScheduledExecutor();  // 定时任务

	public TimerRun(TimerModel timer) {
		this.currTimer = timer;

		if (!this.currTimer.equals(timer)) {
			isUpdate = true;
		}
		this.status = new TimerStatus(timer);

		PropertiesUtils propertiesUtils = PropertiesUtils.getInstance();
		jmxPath = propertiesUtils.getProperty("jmeter.jmx.path");
		jmeterPath = propertiesUtils.getProperty("jmeter.run.path");
	}

	private Runnable runnable = new Runnable() {

		public void run() {
			// 判断任务是否停止
			if (status.getCommandCode() == 1
					|| isSleepTimeUpdate()) {
				schedule.shutdown();
				return;
			}

			if(StringUtils.isEmpty(jmxPath)) {
				logger.error("getProperty jmeter.jmx.path error!");
				return;
			}
			if(StringUtils.isEmpty(jmeterPath)) {
				logger.error("getProperty jmeter.run.path error!");
				return;
			}

			// 得到最新timer
			currTimer = status.getNewTimer();

			// 运行jmx
			logger.info("timer start...");

			File treeLogFile = new File(jmxPath);

			if (!treeLogFile.exists()) {
				treeLogFile.mkdirs();
			}

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm");
			Date date = new Date(System.currentTimeMillis());
			String xmlLogPath = jmxPath + "/" + currTimer.getId() + "_" + dateFormat.format(date) + ".xml";
			String txtLogPath = jmxPath + "/" + currTimer.getId() + "_" + dateFormat.format(date) + ".txt";


			try {
				String jmxPath = writeFile(currTimer.getFile(), currTimer.getId());

				String command = "sh " + jmeterPath + " -n -t " + jmxPath + " -JresultFile="
						+ xmlLogPath + " -JexcelFile=" + txtLogPath;
				logger.info("jmeterCommand:" + command);

				long start = System.currentTimeMillis();

				// send start packet
				SocketAgent.sendPacket(new TimerRunStartPacket(currTimer.getId()));

				// 执行脚本
				String[] result = ExecSystemCommand.exec(command);
				
				// 打印执行结果
				logger.error("execute command error:\r\n" + result[1]);
				logger.info("execute command info:\r\n" + result[0]);

				long end = System.currentTimeMillis();
				
				SocketPacket socketPacket;
				
				logger.info("timer send result");
				if(result[1] != null && result[1].length() > 0) {
					// send end packet
					SocketAgent.sendPacket(new TimerRunEndPacket(currTimer.getId(), currTimer.getAppId(), "jmeter run fail!"));
					
				} else {
					// send end packet
					SocketAgent.sendPacket(new TimerRunEndPacket(currTimer.getId(), currTimer.getAppId()));
					
					// send result file
					socketPacket = new TimerResultPacket(readFile(xmlLogPath), readFile(txtLogPath), start, end, currTimer, 1, isUpdate, currTimer.getAppId());
					SocketAgent.sendPacket(socketPacket);
				}

				// 执行成功,删除结果文件
				new File(xmlLogPath).delete();
				new File(txtLogPath).delete();
				
			} catch (Exception e) {
				logger.error(e.getMessage());
			} finally {
				logger.info("timer end....");
			}


		}
	};

	public boolean isSleepTimeUpdate() {
		return !Objects.equals(currTimer.getSleepTime(), status.getNewTimer().getSleepTime());
	}

	public void start(Integer sleepTime) {
		// 启动任务
		// schedule.schedule(runnable, 10, TimeUnit.DAYS);

		if (sleepTime == 0) {
			runnable.run();
		} else {
			schedule.scheduleAtFixedRate(runnable, 0, sleepTime, TimeUnit.MINUTES);
		}

	}

	public void stop() {
		// 在等待执行状态, 直接停止
		if (status.getRunStatus() == 1) {
			schedule.shutdown();

		} else if (status.getRunStatus() == 2) {
			//正在运行 标记任务停止
			status.setCommandCode(1);

		}
	}

	public void update(TimerModel t) {
		status.setNewTimer(t);
	}

	public TimerStatus getStatus() {
		return status;
	}


	/**
	 * 将文件流转成文件
	 *
	 */
	public String writeFile(byte[] bytesFile, long timerId) throws IOException {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file;
		try {
			String filePath = jmxPath;
			File dir = new File(filePath);
			if (!dir.exists() && dir.isDirectory()) {//判断文件目录是否存在
				dir.mkdirs();
			}

			String url = filePath + "/kinstalk-online-run" + timerId + ".jmx";

			file = new File(url);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(bytesFile);
			return url;

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}


	}

	/**
	 * 将文件读成流
	 *
	 */
	public byte[] readFile(String url) throws IOException {


		File f = new File(url);
		if (!f.exists()) {
			throw new FileNotFoundException(url);
		}

		FileInputStream fs = null;
		FileChannel channel = null;

		byte[] result = null;
		try {
			fs = new FileInputStream(f);
			channel = fs.getChannel();
			ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
			channel.read(byteBuffer);
			result =  byteBuffer.array();
			byteBuffer.clear();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (channel != null) {
					channel.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (fs != null) {
					fs.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;

	}
}
