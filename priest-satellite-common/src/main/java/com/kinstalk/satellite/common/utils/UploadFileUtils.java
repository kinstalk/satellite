package com.kinstalk.satellite.common.utils;

import com.kinstalk.satellite.common.UploadResult;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class UploadFileUtils {

	private static final Logger logger = LoggerFactory.getLogger(UploadFileUtils.class);

	public static Properties properties = new Properties();


	public static UploadResult uploadFile(String realPath, String path, MultipartFile file1) {
		UploadResult uploadResult = null;
		String url = null;
		if (file1 != null && !file1.isEmpty()) {
			uploadResult = new UploadResult();
			String fileName = file1.getOriginalFilename();
			realPath += "/data/" + path + "/";
			File pathFile = new File(realPath);
			if (!pathFile.exists()) {
				pathFile.mkdirs();
			}

			String[] str = fileName.split("\\.");

			SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMdd_HHmmss");
			Date date = new Date(System.currentTimeMillis());
			url = realPath + "kinstalk-online-" + dateFormater.format(date) + "." + str[1];
			File saveFile = new File(url);

			try {
				file1.transferTo(saveFile);
				if (!"".equals(checkFile(saveFile))) {

					uploadResult.setUrl(checkFile(saveFile));
					saveFile.delete();
					return uploadResult;
				}
			} catch (Exception e) {
				logger.error("uploadFile fail transfer fail", e);
			}

			//saveFile.delete();
			uploadResult.setUrl(url);
		}
		return uploadResult;
	}

	public static String checkFile(File file) {

		SAXReader reader = new SAXReader();


		try {
			Document doc = reader.read(file);
			List list = doc.selectNodes("jmeterTestPlan/hashTree/hashTree/Arguments");


			int enable_nums = 0;

			for (Object o : list) {
				Element e = (Element) o;
				if ("true".equals(e.attribute("enabled").getValue())) {
					enable_nums++;
					if (enable_nums > 1) {
						// throw exception
						break;
					}

					List list1 = e.selectNodes("collectionProp/elementProp/stringProp");
					int mobileCount = 0;//用来校验用户手机号count
					int deviceCount = 0;//用来校验每个手机号的devicecount
					int csCount = 0;//用来校验线上urlcount
					for (Object temp : list1) {

						Element e1 = (Element) temp;
						String elementValue = e1.getStringValue().trim();

//                        if(("cs.withugroup.com").equals(elementValue)){
//                            csCount++;
//                        } else if("cs.kinstalk.com".equals(elementValue)) {
//
//						} else 
						if (elementValue.endsWith("mobile")) {
							mobileCount++;

						} else if (elementValue.endsWith("deviceId")) {
							deviceCount++;
						}
					}


					System.out.println("mobile count:" + mobileCount + "  device count" + deviceCount);
//                    if(csCount!=1){
//                        return "请配置线上的配置";
//                    }
					if (mobileCount == 0 || deviceCount == 0) {

						return "测试用户数量为0,请检查脚本";
					}
					if (mobileCount < deviceCount) {
						return "自定义变量中device与mobile数量不匹配, 请重新上传";
					}


				}


			}
			if (enable_nums < 1) {
				return "脚本中无可用配置,请重新上传";
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}


}
