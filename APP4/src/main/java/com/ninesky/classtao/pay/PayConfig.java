package com.ninesky.classtao.pay;


import com.ninesky.classtao.pay.vo.AliPayVO;
import com.ninesky.classtao.pay.vo.PayVO;
import com.ninesky.common.util.SpringBeanUtil;
import com.ninesky.framework.GeneralDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PayConfig {

	private static Logger _logger = LoggerFactory.getLogger(PayConfig.class);

	private static ConcurrentHashMap<Integer,AliPayVO> map = new ConcurrentHashMap<Integer,AliPayVO>();

	static {
		try {
			_logger.error("开始从数据库加载支付宝配置............");
			GeneralDAO dao = SpringBeanUtil.getBean(GeneralDAO.class);
			List<AliPayVO> list = dao.queryForList("alipayMap.getPayList");
			for (AliPayVO vo:list) map.put(vo.getSchool_id(),vo);
		} catch (Exception e) {
			e.printStackTrace();
			_logger.error(e.getMessage());
		}
	}

	public static AliPayVO getProperty(Integer key) {
		if (map.containsKey(key)) return map.get(key);
		GeneralDAO dao = SpringBeanUtil.getBean(GeneralDAO.class);
		AliPayVO pay = dao.queryObject("alipayMap.getPayBySchoolID",key);
		if (pay==null) return map.get(0);
		else setProperty(key,pay);
		return pay;
	}
	

	public static void setProperty(Integer key,AliPayVO vo) {
			map.put(key, vo);
	}
	
	public static void remove(String key) {
			map.remove(key);
	}
}
