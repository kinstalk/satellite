package com.kinstalk.satellite.socket.manager;

import com.kinstalk.satellite.dao.AgentDao;
import com.kinstalk.satellite.dao.AgentStatusDao;
import com.kinstalk.satellite.dao.RunDao;
import com.kinstalk.satellite.domain.*;
import com.kinstalk.satellite.domain.packet.TimerResultPacket;
import com.kinstalk.satellite.service.api.ReportService;
import com.kinstalk.satellite.web.HttpRequest;
import com.kinstalk.satellite.web.SendMail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangchuanqi on 16/6/21.
 */
@Service ("timerAnalysis")
public class TimerAnalysis extends Thread {


	private Logger logger = LoggerFactory.getLogger(TimerAnalysis.class);


	@Resource
	private ReportService reportService;
	@Resource
	private RunDao runDao;
	@Resource
	private AgentStatusDao agentStatusDao;
	@Resource
	private SendMail sendMail;
	@Resource
	private AgentDao agentDao;
	public void handleResult(TimerResultPacket timerResultPacket, int agentId) throws Exception {

		//如果当前批次已经运行完毕 执行解析
		if (timerResultPacket.getCsvLogFile() != null && timerResultPacket.getTimerModel().getRunType() == 1) {
			RunScriptTimerDetail timerDetail = new RunScriptTimerDetail();
			timerDetail.setTimerId(timerResultPacket.getTimerModel().getId());
			timerDetail.setStartTime(timerResultPacket.getStartTime());
			timerDetail.setEndTime(timerResultPacket.getEndTime());
			timerDetail.setCreateTime(System.currentTimeMillis());
			timerDetail.setAgentId(agentId);

			try {
				timerDetail.setResultFilePath(writeFile(timerResultPacket.getTreeLogFile(), "tree", timerResultPacket.getTimerModel().getId(), agentId));
				timerDetail.setSuccessRate(analysisResult(writeFile(timerResultPacket.getCsvLogFile(), "csv", timerResultPacket.getTimerModel().getId(), agentId), 0, agentId, timerResultPacket.getAppId(), timerResultPacket.getTimerModel().getId()));
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

			// 100%成功, 不保留成功文件
			if (timerDetail.getSuccessRate().equals("100%")) {
				new File(timerDetail.getResultFilePath()).delete();
				new File(timerDetail.getResultFilePath().replace(".xml", ".txt")).delete();
			}
			runDao.saveDetail(timerDetail);
		}


	}


	/**
	 * 解析
	 *
	 * @param url
	 * @return
	 */
	public String analysisResult(String url, Integer sleepTime, Integer agentid, Integer appid, Long timerId) {


		List<BatchInsertParam> list = new ArrayList<BatchInsertParam>();
		FileInputStream fis = null;
		InputStreamReader isr = null;
		int successcount = 0;

		int count = 0;


		BufferedReader br = null; //用于包装InputStreamReader,提高处理性能。因为BufferedReader有缓冲的，而InputStreamReader没有。
		try {
			String str = "";
			String str1 = "";

			fis = new FileInputStream(url);// FileInputStream

			// 从文件系统中的某个文件中获取字节
			isr = new InputStreamReader(fis);// InputStreamReader 是字节流通向字符流的桥梁,
			// br = new BufferedReader(isr);// 从字符输入流中读取文件中的内容,封装了一个new InputStreamReader的对象
			br = new BufferedReader(isr);


			while ((str = br.readLine()) != null) {
				if (str.length() == 0) {
					continue;
				}

				String[] strArray = str.split(",");
				if (strArray.length != 5) {
					continue;
				}
				if (strArray[4] == null || strArray[4].equals("null")) {
					continue;
				}

				String costTime = strArray[0];
				String status = strArray[1];
				String apiurl = strArray[4];

				if (!apiurl.contains("://")) {
					logger.error("txt result[{}] error! not found url at index[4]:{}", url, apiurl);
					break;
				}

				//判断是否结束
				if (apiurl.contains("/Test/end")) {
					System.out.println(apiurl);
					break;
				}

				if (apiurl.contains("download")) {
					apiurl = apiurl.substring(apiurl.indexOf("/", 10));
					int indexOfEnd = apiurl.indexOf("?");
					if (indexOfEnd > 0) {
						apiurl = apiurl.substring(0, indexOfEnd);
					}
					apiurl = "/download/download?" + apiurl;
				} else {
					apiurl = apiurl.substring(apiurl.indexOf("/", 10));
					int indexOfEnd = apiurl.indexOf("?");
					if (indexOfEnd > 0) {
						apiurl = apiurl.substring(0, indexOfEnd);
					}
				}


				BatchInsertParam batchInsertParam = new BatchInsertParam();

				//花费时间
				batchInsertParam.setCostTime(Long.parseLong(costTime));

				//是否成功
				if ("true".equals(status)) {
					successcount++;
					batchInsertParam.setStatus(1);
				} else {

					batchInsertParam.setStatus(0);
				}
				// url
				batchInsertParam.setApiUrl(apiurl);

				list.add(batchInsertParam);
				count++;


			}

			//插入流水表
			reportService.batchInsertReport(list, sleepTime, agentid, appid, timerId);


		} catch (FileNotFoundException e) {
			logger.error("找不到指定文件");
		} catch (IOException e) {
			logger.error("读取文件失败");

		} finally {
			try {
				br.close();
				isr.close();
				fis.close();
				// 关闭的时候最好按照先后顺序关闭最后开的先关闭所以先关s,再关n,最后关m
			} catch (IOException e) {
				e.printStackTrace();
				logger.info(e.getMessage());
			}
		}


		String sucRate = "";
		if (count != 0) {
			NumberFormat nf = NumberFormat.getPercentInstance();
			nf.setMaximumFractionDigits(2);
			sucRate = nf.format((float) successcount / (float) count);
		}

		// 如果执行不成功 发送邮报警邮件

		StringBuilder sendMessage = new StringBuilder();


		String agentName = agentDao.selectById((long) agentid).getAgentArea();
		RunScriptTimer runScriptTimer = runDao.selectById(timerId);
		String timerName = runScriptTimer.getName();

		StringBuilder errorUrls = new StringBuilder();
		for (BatchInsertParam reportLastApi : list) {
			if (reportLastApi.getStatus() != 0) {
				continue;
			}
			if (errorUrls.indexOf(reportLastApi.getApiUrl()) != -1) {
				continue;
			}

			errorUrls.append(reportLastApi.getApiUrl()).append("\n");

		}


		try {
			sendMessage.append("Agent名称: ").append(agentName).append("\n");
			sendMessage.append("结果文件: ").append(url.replace("txt", "xml")).append("\n");
			sendMessage.append("任务名称: ").append(timerName).append("\n");
			sendMessage.append("异常接口:").append(errorUrls).append("\n");


			if (errorUrls.length() > 0) {

				sendMail.sendMail("monitor@shuzijiayuan.com", runScriptTimer.getNoticeEmail().split(","),
						"服务监控报警",
						sendMessage.toString());
				sendMessage(appid, errorUrls);


				logger.info("发送成功");

			}

		} catch (Exception e) {
			e.printStackTrace();
			for (int i = 0; i <= 2; i++) {

				if (sendMail.sendMail("monitor@shuzijiayuan.com", runScriptTimer.getNoticeEmail().split(","), "服务监控报警",
						sendMessage.toString())) {
					break;

				}
				sleep(1000 * 5);
			}
		} finally {
			return sucRate;
		}


	}

	public static void sendMessage(Integer appid, String content) {
		sendMessage(appid, new StringBuilder(content));
	}

	public static void sendMessage(Integer appid, StringBuilder errorUrls) {
		// 中文只支持8个字符, 英文没有限制
		String appName = "";
		if (appid == 1) {
			appName = "withu";
		} else if (appid == 2) {
			appName = "Qiji";
		} else if (appid == 3) {
			appName = "qlove";
		} else if (appid == 4) {
			appName = "qlove-test";

		}
		String weixin_content = String.format("content=%s error:%s&mobile=", appName, errorUrls);
		String responsecode = HttpRequest.sendPost("http://weixin.shuzijiayuan.com/api/nweixin/send", weixin_content);
	}


	/**
	 * 将文件流转成文件
	 *
	 * @param bytesfile
	 * @return
	 * @throws IOException
	 */
	public String writeFile(byte[] bytesfile, String key, Long timerId, Integer agentId) throws IOException {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			String filePath = "/data/log/manager";
			File dir = new File(filePath);
			if (!dir.exists() && dir.isDirectory()) {//判断文件目录是否存在
				dir.mkdirs();
			}
			SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMdd_HHmmss");
			Date date = new Date(System.currentTimeMillis());

			String url = "";

			if ("csv".equals(key)) {
				url = filePath + "_" + timerId + "_" + agentId + "_" + dateFormater.format(date).toString() + ".txt";
			} else {
				url = filePath + "_" + timerId + "_" + agentId + "_" + dateFormater.format(date).toString() + ".xml";
			}


			file = new File(url);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(bytesfile);


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
}
