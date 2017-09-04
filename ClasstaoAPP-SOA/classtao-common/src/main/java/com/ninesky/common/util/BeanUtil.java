package com.ninesky.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ninesky.common.vo.ReceiveVO;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*******************************************************************
 * Fastjson的SerializerFeature序列化属性
 * QuoteFieldNames———-输出key时是否使用双引号,默认为true
 * WriteMapNullValue——–是否输出值为null的字段,默认为false
 * WriteNullListAsEmpty—–List字段如果为null,输出为[],而非null
 * WriteNullStringAsEmpty—字符类型字段如果为null,输出为”“,而非null
 * WriteNullBooleanAsFalse–Boolean字段如果为null,输出为false,而非null
********************************************************************/
public class BeanUtil {

	/**
	 * 将Json数组格式的字符串转换成List集合
	 * @param bean
	 * @return
	 */
	public static <T> String beanToJson(T bean){
		return JSON.toJSONString(bean, SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullStringAsEmpty,SerializerFeature.WriteNullNumberAsZero);
	}

	/**
	 * 将Json数组格式的字符串转换成List集合
	 * @param bean 业务实体
	 * @param is_show_empty_field 是否显示空值的字段
	 * @return
	 */
	public static <T> String beanToJson(T bean, boolean is_show_empty_field){
		if (is_show_empty_field) return JSON.toJSONString(bean, SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullStringAsEmpty,SerializerFeature.WriteNullNumberAsZero);
		else return JSON.toJSONString(bean);
	}

	/**
	 * 将Json数组格式的字符串转换成List集合
	 * @param jsonStr
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> jsonToList(String jsonStr, Class<T> clazz){
		List<T> list = new ArrayList<T>();
		if (StringUtil.isEmpty(jsonStr)) return list;
		return JSONArray.parseArray(jsonStr,clazz);
	}

	/**
	 * 将List数据集转换成json格式数组
	 * @param list
	 * @param is_show_empty_field 是否显示空值的字段
	 * @return
	 */
	public static String ListTojson(List<?> list, boolean is_show_empty_field){
		if (is_show_empty_field) return JSON.toJSONString(list,SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullStringAsEmpty,SerializerFeature.WriteNullNumberAsZero);
		else return  JSON.toJSONString(list);
	}

	/**
	 * 将Json数组格式的字符串转换成Map集合
	 * @param jsonStr
	 * @return
	 */
	public static Map<String,Object> jsonToMap(String jsonStr){
		Map<String,Object> map = new HashMap<String,Object>();
		if (StringUtil.isEmpty(jsonStr)) return map;
		return JSONObject.fromObject(jsonStr);
	}

	/**
	 * 将List数据集转换成json格式数组
	 * @param list
	 * @return
	 */
	public static String ListTojson(List<?> list){
		return JSON.toJSONString(list,SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullStringAsEmpty,SerializerFeature.WriteNullNumberAsZero);
	}


	/**
	 * 将Map转换成javaBean，返回需要的实体对象
	 * @param map 待转换的Map
	 * @param clazz 需要转换的实体类
	 * @author chenth
	 */
	public static <T> T formatMapToBean(Map<?, ?> map, Class<T> clazz){
		return JSON.parseObject(JSON.toJSONBytes(map),clazz);
	}

	/**
	 * 将Map转换成javaBean，返回需要的实体对象
	 * @param clazz 需要转换的实体类
	 * @author chenth
	 */
	public static <T> T formatJsonToBean(String jsonString, Class<T> clazz){
		return JSON.parseObject(jsonString,clazz);
	}


	/**
	 * 将传来的参数转换成javaBean，返回需要的实体对象
	 * @param clazz 需要转换的实体类
	 * @author chenth
	 */
	public static <T> T formatToBean(Class<T> clazz){
		return formatMapToBean(ActionUtil.getParameterMap(),clazz);
	}

	/**
	 * 将重复的接收单位去重复
	 * @param list
	 * @author chenth
	 * @return
	 */
	public static List<ReceiveVO> removeDuplicate(List<ReceiveVO> list) {
		List<ReceiveVO> returnList=new ArrayList<ReceiveVO>();
		HashMap<String,ReceiveVO> tempMap = new HashMap<String,ReceiveVO>();
        HashMap<Integer,ReceiveVO> stuMap=new HashMap<Integer,ReceiveVO>();
		//先将所有接受者存放到HashMap中
		for (ReceiveVO rvo:list) {
			if (IntegerUtil.isEmpty(rvo.getStudent_id()))
				tempMap.put(RedisKeyUtil.getGroupKey(rvo),rvo);
		}
		//遍历，去重复
//		GeneralDAO dao= SpringBeanUtil.getBean(GeneralDAO.class);
//		for (ReceiveVO rvo:list) {
//			if (IntegerUtil.isNotEmpty(rvo.getStudent_id())){ //单个学生
//				StudentVO student=dao.queryObject("studentMap.getStudentById",rvo.getStudent_id());
//				String teamKey= RedisKeyUtil.getGroupKey(ActionUtil.getSchoolID(), rvo.getTeam_type(),student.getGrade_id(),student.getClass_id());
//				String groupKey = RedisKeyUtil.getGroupKey(ActionUtil.getSchoolID(),rvo.getTeam_type(),student.getGrade_id(),0);
//				String schoolKey = RedisKeyUtil.getGroupKey(ActionUtil.getSchoolID(),rvo.getTeam_type(),0,0);
//				if (tempMap.containsKey(teamKey) || tempMap.containsKey(groupKey) || tempMap.containsKey(schoolKey) || stuMap.containsKey(rvo.getStudent_id())) continue;
//				returnList.add(rvo);
//                stuMap.put(rvo.getStudent_id(),rvo);
//			}
//			//班级或者寝室
//			else if (IntegerUtil.isNotEmpty(rvo.getGroup_id()) && IntegerUtil.isNotEmpty(rvo.getTeam_id()))
//			{
//				String groupKey = RedisKeyUtil.getGroupKey(ActionUtil.getSchoolID(),rvo.getTeam_type(),rvo.getGroup_id(),0);
//				String schoolKey = RedisKeyUtil.getGroupKey(ActionUtil.getSchoolID(),rvo.getTeam_type(),0,0);
//				if (tempMap.containsKey(groupKey) || tempMap.containsKey(schoolKey)) continue;
//				returnList.add(rvo);
//			}
//			//年级
//			else if (IntegerUtil.isNotEmpty(rvo.getGroup_id()) && IntegerUtil.isEmpty(rvo.getTeam_id()))
//			{
//				String schoolKey = RedisKeyUtil.getGroupKey(ActionUtil.getSchoolID(),rvo.getTeam_type(),0,0);
//				if (tempMap.containsKey(schoolKey)) continue;
//				returnList.add(rvo);
//			} else if (DictConstants.TEAM_TYPE_INTEREST.equals(rvo.getTeam_type())) {
//                ContactListVO cvo=new ContactListVO();
//                cvo.setContact_id(rvo.getTeam_id());
//                cvo.setUser_type(DictConstants.USERTYPE_STUDENT);
//                cvo.setSchool_id(ActionUtil.getSchoolID());
//                List<StudentVO> slist=dao.queryForList("contactListMap.getContactList", cvo);
//                for (StudentVO svo :slist) {
//                    StudentVO student=dao.queryObject("studentMap.getStudentById",svo.getStudent_id());
//                    String teamKey= RedisKeyUtil.getGroupKey(ActionUtil.getSchoolID(), DictConstants.TEAM_TYPE_CLASS,student.getGrade_id(),student.getClass_id());
//                    String groupKey = RedisKeyUtil.getGroupKey(ActionUtil.getSchoolID(), DictConstants.TEAM_TYPE_CLASS,student.getGrade_id(),0);
//                    String schoolKey = RedisKeyUtil.getGroupKey(ActionUtil.getSchoolID(), DictConstants.TEAM_TYPE_CLASS,0,0);
//                    if (tempMap.containsKey(teamKey) || tempMap.containsKey(groupKey) || tempMap.containsKey(schoolKey) || stuMap.containsKey(svo.getStudent_id())) continue;
//                    ReceiveVO receiveVO=new ReceiveVO(ActionUtil.getSchoolID(), DictConstants.USERTYPE_STUDENT,svo.getStudent_id());
//                    returnList.add(receiveVO);
//                    stuMap.put(svo.getStudent_id(),receiveVO);
//                }
//            }else returnList.add(rvo);
//		}
		return returnList;


	}
}
