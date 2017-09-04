package com.classtao;

import java.io.IOException;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class SystemConfig {

	private static Log logger = LogFactory.getLog(SystemConfig.class);

	private static Properties configProps;

	private static final String CONFIG = "config.properties";
	

	static {
		try {
			logger.error("loading File config.properties..................");
			configProps = PropertiesLoaderUtils.loadAllProperties(CONFIG);
		} catch (IOException e) {
			logger.error(e,e);
		}
	}

	public static String getProperty(String key) {
		if (!StringUtil.isEmpty(key)) {
			return configProps.getProperty(key);
		}
		return null;
	}
	
	public static void setProperty(String key,String value) {
		if (!StringUtil.isEmpty(key)) {
			configProps.setProperty(key, value);
		}
	}
	
	public static void remove(String key) {
		if (!StringUtil.isEmpty(key)) {
			configProps.remove(key);
		}
	}
}
