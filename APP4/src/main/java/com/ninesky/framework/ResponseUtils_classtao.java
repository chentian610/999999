package com.ninesky.framework;

import com.ninesky.common.util.ActionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseUtils_classtao {

	public static final String RESPONSE_SUCCESS_KEY = "success";

	public static final String RESPONSE_FAILURE_KEY = "failure";

	public static final String RESPONSE_TEXT_KEY = "text";

	public static final String RESPONSE_EXTRA_KEY = "extra";

	public static final String PAGINATION_ROOT_PROPERTY_KEY = "root";

	public static final String PAGINATION_TOTAL_PROPERTY_KEY = "total";
	
	public static final String RESPONSE_CODE = "code";
	
	public static final String RESPONSE_MSG = "msg";
	
	public static final String RESPONSE_DATA = "data";
	
	public static final Integer RESPONSE_SUCCESS_CODE = 1;
	
	public static final Integer RESPONSE_FAILURE_CODE = 0;
	
	/**
	 * 分页数据
	 * */
	public static <T> Map<String, Object> sendPagination(List<T> T) {
		Map<String, Object> map = getInstanceMap();
		map.put(PAGINATION_TOTAL_PROPERTY_KEY, ActionUtil.getActionParam().getTotal());
		map.put(PAGINATION_ROOT_PROPERTY_KEY, T);
		return map;
	}

	public static <T> Map<String, Object> sendList(List<T> T) {
		Map<String, Object> map = getInstanceMap();
		map.put(PAGINATION_ROOT_PROPERTY_KEY, T);
		map.put(RESPONSE_SUCCESS_KEY, true);
		return map;
	}

	public static <T, V, K> Map<String, Object> sendList(Map<K,V> T) {
		Map<String, Object> map = getInstanceMap();
		map.put(PAGINATION_ROOT_PROPERTY_KEY, T);
		map.put(RESPONSE_SUCCESS_KEY, true);
		return map;
	}

	public static Map<String, Object> sendSuccess(String text, Object... extra) {
		Map<String, Object> map = getInstanceMap();
		map.put(RESPONSE_SUCCESS_KEY, true);
		map.put(RESPONSE_TEXT_KEY, text);
		if ((extra!=null)&&(extra.length > 0)) {
			map.put(RESPONSE_DATA, extra);
		}
		return map;
	}

	public static Map<String, Object> sendFailure(String text, Object extra) {
		Map<String, Object> map = getInstanceMap();
		map.put(RESPONSE_FAILURE_KEY, true);
		map.put(RESPONSE_DATA, extra);
		map.put(RESPONSE_CODE, RESPONSE_FAILURE_CODE);
		map.put(RESPONSE_MSG, text);
		return map;
	}

	private static Map<String, Object> getInstanceMap() {
		return new HashMap<String, Object>();
	}

	public static Map<String, Object> sendFailure(String message) {
		return sendFailure(message,RESPONSE_FAILURE_CODE);
	}
	
	public static Map<String, Object> sendFailure(String message,Integer errcode) {
		Map<String, Object> map = getInstanceMap();
		map.put(RESPONSE_SUCCESS_KEY, false);
		map.put(RESPONSE_CODE, errcode);
		map.put(RESPONSE_MSG, message);
		map.put(RESPONSE_DATA, null);
		return map;
	}
	
	
	public static Map<String, Object> sendSuccess(String text, Object valueVo) {
		Map<String, Object> map = getInstanceMap();
		map.put(RESPONSE_SUCCESS_KEY, true);
		map.put(RESPONSE_CODE, RESPONSE_SUCCESS_CODE);
		map.put(RESPONSE_MSG, text);
		map.put(RESPONSE_DATA, valueVo);
		map.put(PAGINATION_TOTAL_PROPERTY_KEY, ActionUtil.getActionParam()==null?0:ActionUtil.getTotal());
		return map;
	}
	
	public static Map<String, Object> sendSuccess(Object valueVo) {
		return sendSuccess("操作成功",valueVo);
	}
	
	public static Map<String, Object> sendSuccess() {
		return sendSuccess(null);
	}
	
}
