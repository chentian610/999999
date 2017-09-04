package com.classtao;

public class StringUtil {
	public static boolean isEmpty(String str){
		return str == null || str.trim().length() == 0 || "[]".equals(str);
	}
	
	/**
	 * 通过请求中的模块编码，将文件存放到不同的地方
	 * @param module_code
	 * @return file_path
	 */
	public static String getFilePathByModuleCode(String module_code,Integer school_id) {
		String file_path = "";
		switch (module_code) {
			case DictConstants.MODULE_CODE_USER://用户模块
				file_path=Constants.FILE_PATH_APP4 + school_id +Constants.FILE_PATH_USER;
				break;
			case DictConstants.MODULE_CODE_NOTICE://通知模块
				file_path=Constants.FILE_PATH_APP4 + school_id +Constants.FILE_PATH_NOTICE;
				break;
			case DictConstants.MODULE_CODE_NOTICE_SCH://校务通知
				file_path=Constants.FILE_PATH_APP4 + school_id +Constants.FILE_PATH_NOTICE;
				break;
			case DictConstants.MODULE_CODE_HOMEWORK://作业
				file_path=Constants.FILE_PATH_APP4 + school_id +Constants.FILE_PATH_HOMEWORK;
				break;
			case DictConstants.MODULE_CODE_NOTE://课件
				file_path=Constants.FILE_PATH_APP4 + school_id +Constants.FILE_PATH_NOTE;
				break;
			case DictConstants.MODULE_CODE_PHOTO://相册
				file_path=Constants.FILE_PATH_APP4 + school_id +Constants.FILE_PATH_PHOTO;
				break;
			case DictConstants.MODULE_CODE_NEWS://校园风采
				file_path=Constants.FILE_PATH_APP4 + school_id +Constants.FILE_PATH_NEWS;
				break;
			case DictConstants.MODULE_CODE_PARTYBUILD://党建
				file_path=Constants.FILE_PATH_APP4 + school_id +Constants.FILE_PATH_NEWS;
				break;
			case DictConstants.MODULE_CODE_APP://党建
				file_path=Constants.FILE_PATH_APP4 + school_id +Constants.FILE_PATH_APP;
				break;
			case DictConstants.MODULE_CODE_SCHOOL://党建
				file_path=Constants.FILE_PATH_APP4 + school_id +Constants.FILE_PATH_SCHOOL;
				break;
			case DictConstants.MODULE_CODE_FAME://党建
				file_path=Constants.FILE_PATH_APP4 + school_id +Constants.FILE_PATH_FAME;
				break;
			default :
				file_path=Constants.FILE_PATH_APP4 + school_id +Constants.FILE_PATH_DEFAULT;//公用
				break;
		}
		return file_path;
	}
}
