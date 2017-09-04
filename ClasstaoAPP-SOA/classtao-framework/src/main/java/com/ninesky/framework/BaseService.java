package com.ninesky.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用来统一处理事务的设置
 * @author Chenth
 */
@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=RuntimeException.class)
public class BaseService {
	private static Logger logger = LoggerFactory.getLogger(BaseService.class);
	/**
	* 异常控制，这便是异常细节可控，将来可用于支持国际化（异常信息国际化）
	**/
}
