package com.ninesky.classtao.capital;

import com.ninesky.classtao.capital.vo.GetuiVO;
import com.ninesky.common.util.SpringBeanUtil;
import com.ninesky.framework.GeneralDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class GetuiConfig {

	private static Logger _logger = LoggerFactory.getLogger(GetuiConfig.class);

	private static ConcurrentHashMap<Integer,GetuiVO> map = new ConcurrentHashMap<Integer,GetuiVO>();

	static {
		try {
			_logger.error("开始从数据库加载个推配置............");
			GeneralDAO dao = SpringBeanUtil.getBean(GeneralDAO.class);
			List<GetuiVO> list = dao.queryForList("getuiMap.getGetuiList");
			for (GetuiVO vo:list) map.put(vo.getSchool_id(),vo);
		} catch (Exception e) {
			e.printStackTrace();
			_logger.error(e.getMessage());
		}
	}

	public static GetuiVO getProperty(Integer key) {
		if (map.containsKey(key)) return map.get(key);
		GeneralDAO dao = SpringBeanUtil.getBean(GeneralDAO.class);
		GetuiVO getui = dao.queryObject("getuiMap.getGetuiBySchoolID",key);
		if (getui!=null) setProperty(key,getui);
		return getui;
	}
	
	public static void setProperty(Integer key,GetuiVO vo) {
			map.put(key, vo);
	}
	
	public static void remove(String key) {
			map.remove(key);
	}
}
