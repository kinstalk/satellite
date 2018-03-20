package com.kinstalk.satellite.socket;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by digitZhang on 2018/3/1.
 */
public class PropertiesUtils {
	
	private static PropertiesUtils instance;
	private Properties properties;
	
	private PropertiesUtils() {
		properties = new Properties();
		try {
			InputStream in = PropertiesUtils.class.getResourceAsStream("/project.properties");
			properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static PropertiesUtils getInstance() {
		if(instance == null) {
			synchronized (PropertiesUtils.class) {
				if(instance == null) {
					instance = new PropertiesUtils();
				}
			}
		}
		return instance;
	}

	public String getProperty(String key) {
		Object obj = properties.get(key);
		return obj != null ? obj.toString() : "";
	}
}
