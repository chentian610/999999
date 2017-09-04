package com.ninesky.framework;

import java.util.HashMap;
import java.util.List;

import com.ninesky.common.util.SpringBeanUtil;
import com.ninesky.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;

public class SystemConfig {

	private static Logger _logger = LoggerFactory.getLogger(SystemConfig.class);

	private static HashMap<String,String> configMap = new HashMap<String,String>();

	public static HashMap<String, String> getConfigMap() {
		return configMap;
	}

	static {
		try {
			_logger.error("开始从数据库加载本地配置............");
			GeneralDAO dao = SpringBeanUtil.getBean(GeneralDAO.class);
			List<HashMap<String,String>> list = dao.queryForList("systemConfigMap.getConfigList");
			for (HashMap<String,String> map:list) configMap.put(map.get("config_key"),map.get("config_value"));
		} catch (Exception e) {
			_logger.error(e.getMessage(),e);
		}
	}

	public static String getProperty(String config_key) {
		if (StringUtil.isNotEmpty(config_key)) {
			return configMap.get(config_key);
		}
		GeneralDAO dao = SpringBeanUtil.getBean(GeneralDAO.class);
		HashMap<String,String> map = dao.queryObject("systemConfigMap.getConfigByKey",config_key);
		if (map == null) return null;
		else {
			configMap.put(map.get("config_key"),map.get("config_value"));
			return map.get("config_value");
		}
	}
}
