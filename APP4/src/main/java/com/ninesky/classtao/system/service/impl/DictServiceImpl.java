package com.ninesky.classtao.system.service.impl;

import com.ninesky.classtao.contact.vo.ContactVO;
import com.ninesky.classtao.menu.service.MenuService;
import com.ninesky.classtao.news.vo.NewsVO;
import com.ninesky.classtao.redis.service.RedisService;
import com.ninesky.classtao.school.vo.SchoolVO;
import com.ninesky.classtao.system.service.DictService;
import com.ninesky.classtao.system.vo.DictSchoolVO;
import com.ninesky.classtao.system.vo.DictVO;
import com.ninesky.classtao.system.vo.SortVO;
import com.ninesky.classtao.user.service.UserService;
import com.ninesky.classtao.user.vo.TeacherVO;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.*;
import com.ninesky.framework.BusinessException;
import com.ninesky.framework.GeneralDAO;
import com.ninesky.framework.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("dictServiceImpl")
public class DictServiceImpl implements DictService {

	@Autowired
	private GeneralDAO dao;

    @Autowired
    private RedisService redisService;

	@Resource
	private UserService userService;

	@Autowired
	private MenuService menuService;
	//获取数据字典
		public List<DictVO> getDictList(DictVO vo) {
			vo.setIs_configure(0);
			vo.setSchool_id(0);
			List<DictVO> list=dao.queryForList("dictMap.getDictList", vo);//通用的字典
			vo.setSchool_id(ActionUtil.getSchoolID());
			if (IntegerUtil.isEmpty(vo.getSchool_id())) return list;
			List<DictVO> list1=dao.queryForList("dictMap.getDictList", vo);//本校的字典
			list.addAll(list1);
			return list;
		}

	//修改数据字典
		public void updateDictSort(String jsonArray,String dict_group) {
		List<SortVO> list=BeanUtil.jsonToList(jsonArray, SortVO.class);
		if(ListUtil.isEmpty(list)) return ;
		int sort=1;
		for(SortVO vo:list){
			DictVO dictVO=new DictVO();
			dictVO.setDict_group(dict_group);
			dictVO.setSort(sort);
			dictVO.setDict_code(vo.getId());
			dao.updateObject("dictMap.updateSort", dictVO);
			sort++;
			if (StringUtil.isEmpty(vo.getChildren()) || "null".equals(vo.getChildren()))
				continue;
			updateDictSort(vo.getChildren(),vo.getId());
		}
	}

	//增加数据字典
	public DictVO addDictionary(DictVO vo) {
		vo.setSchool_id(0);
		DictVO dictVO=dao.queryObject("dictMap.getDict", vo);//查询通用字典中是否有重复记录
		if(dictVO!=null) throw new BusinessException("已存在该记录");
		vo.setSchool_id(ActionUtil.getSchoolID());
		DictVO dictVO1=dao.queryObject("dictMap.getDict", vo);//查询本校字典中是否有重复记录
		if(dictVO1!=null) throw new BusinessException("已存在该记录");
		dictVO=dao.queryObject("dictMap.getMaxCode", vo.getDict_group());
		if(dictVO==null) {//该父节点还无子项时
			String code=vo.getDict_group()+"005";
			vo.setDict_code(code);
			vo.setSort(1);
		}else {//该父节点有子项时
			int j=0;
			for(int i=0;i<dictVO.getDict_code().length();i++){//查询dict_code前面有几个0
				 char s=dictVO.getDict_code().charAt(i);
				 if(s!='0'){
					 j=i;
					 break;
				 }
			}
			String s=dictVO.getDict_code().substring(0, j);
			String code=Integer.parseInt(dictVO.getDict_code())+5+"";//新加的dict_code默认比当前最大的加5
			String c=s+code;
			vo.setDict_code(c);
			vo.setSort(dictVO.getSort()+1);
		}
		vo.setDescription(vo.getDict_value());
		vo.setId(dao.insertObjectReturnID("dictMap.addDict", vo));
		return vo;
	}
	
	//删除数据字典
	public void deleteDictionary(DictVO vo) {
		dao.deleteObject("dictMap.deleteDict", vo);
	}

	//字典重命名
	public void updateDictName(DictVO vo) {
		dao.updateObject("dictMap.updateName", vo);
	}

	public DictVO getDict(String dict_code) {
		DictVO vo=dao.queryObject("dictMap.getDictInfo", dict_code);
		return vo;
	}

	@Override
	public List<DictVO> getNewsCssList(String dict_group) {
		return dao.queryForList("dictMap.getSubDictListByGroup", dict_group);
	}

	//获取学校相关配置（dict_school;兴趣班课程，请假类型）
	public List<DictVO> getDictSchoolList(DictVO vo) {
		vo.setSchool_id(0);
		vo.setIs_configure(0);
		List<DictVO> dictList=dao.queryForList("dictMap.getDictList",vo);
		for (DictVO dict:dictList)
			dict.setIs_active(1);
		vo.setSchool_id(ActionUtil.getSchoolID());
		List<DictVO> list=dao.queryForList("dictMap.getDictSchoolList", vo);
		dictList.addAll(list);
		return dictList;
	}

	//添加学校相关配置（dict_school;兴趣班课程，请假类型）
	public DictVO addDictSchool(DictVO vo) {
		vo.setSchool_id(ActionUtil.getSchoolID());
		List<DictVO> list=dao.queryForList("dictMap.getDictSchoolList", vo);
		if (ListUtil.isEmpty(list)) {//该学校还无项配置
			String code=vo.getDict_group()+"001";
			vo.setDict_code(code);
			vo.setSort(1);
		} else {//该学校已有该项配置
			List<DictVO> valueList=dao.queryForList("dictMap.getDictByValue",vo);
			if (ListUtil.isNotEmpty(valueList)) throw new BusinessException(MsgService.getMsg("VALUE_SAME"));
			DictVO dict=dao.queryObject("dictMap.getDictSchoolMax", vo);
			//if (Integer.parseInt(dict.getDict_code())==15045999) throw new BusinessException("已是最大值！");
			String newDictcode=Integer.parseInt(dict.getDict_code())+1+"";//15045001    15045999
			vo.setDict_code("0"+newDictcode);
			vo.setSort(dict.getSort()+1);
		}
		dao.insertObject("dictMap.addDictSchool", vo);
		vo.setIs_active(1);
		return vo;
	}

	//禁用学校某项配置
	public void deleteDictSchool(String dict_code) {
		ContactVO vo=new ContactVO();
		vo.setSchool_id(ActionUtil.getSchoolID());
		vo.setCourse(dict_code);
		List<ContactVO> list=dao.queryForList("contactMap.getInterestByCourse", vo);
		if (ListUtil.isNotEmpty(list)) throw new BusinessException(MsgService.getMsg("CANNOT_FORBiDDEN"));//对兴趣班课程特殊判断
		DictVO dict=new DictVO();
		dict.setSchool_id(ActionUtil.getSchoolID());
		dict.setDict_code(dict_code);
		dict.setIs_active(0);
		dict.setUpdate_date(ActionUtil.getSysTime());
		dao.updateObject("dictMap.updateDictSchool", dict);
	}

	//修改学校配置项名称
	public void updateDictSchool(DictVO vo) {
		vo.setSchool_id(ActionUtil.getSchoolID());
		if (StringUtil.isNotEmpty(vo.getDict_value())) {//不能重名
			List<DictVO> list=dao.queryForList("dictMap.getDictByValue",vo);
			if (ListUtil.isNotEmpty(list)) throw new BusinessException(MsgService.getMsg("VALUE_SAME"));
		}
		vo.setUpdate_date(ActionUtil.getSysTime());
		dao.updateObject("dictMap.updateDictSchool", vo);
		redisService.saveDictValue(vo.getDict_code(),vo.getDict_value());
	}

	public  void deleteNewsDictSchool(DictVO vo){
		ActionUtil.getActionParam().setLimit(20);
		vo.setSchool_id(ActionUtil.getSchoolID());
		Map<String, String> paramMap = ActionUtil.getParameterMap();
		paramMap.put("school_id",ActionUtil.getSchoolID()+"");
		paramMap.put("dict_group",vo.getDict_code());
		List<NewsVO> newsList = dao.queryForList("newsMap.getNewsList",paramMap);
		if (ListUtil.isNotEmpty(newsList)) throw new BusinessException(MsgService.getMsg("UN_DELETE_DICT"));
		dao.deleteObject("dictMap.deleteDictSchool",vo);
		dao.deleteObject("dictMap.deleteDictSchoolByGroup",vo);
	}

    public void updateDictSchoolSort(String jsonArray, String dict_group){
        List<SortVO> list=BeanUtil.jsonToList(jsonArray, SortVO.class);
        if(ListUtil.isEmpty(list)) return ;
        Integer sort = 1;
        for(SortVO vo:list){
            DictVO dict = new DictVO();
            dict.setDict_code(vo.getDict_code());
            dict.setSort(sort);
            dict.setSchool_id(ActionUtil.getSchoolID());
			dict.setDict_group(dict_group);
			dict.setUpdate_date(ActionUtil.getSysTime());
            dao.updateObject("dictMap.updateDictSchoolSort", dict);
            sort++;
            if (StringUtil.isEmpty(vo.getChildren()) || "null".equals(vo.getChildren())) continue;
			updateDictSchoolSort(vo.getChildren(),vo.getDict_code());
        }
    }

    public  void deleteNewsDictSchoolByCode(DictVO vo){
        vo.setSchool_id(ActionUtil.getSchoolID());
        List<NewsVO> news = dao.queryForList("newsMap.getNewsByCode", vo);
        if(ListUtil.isNotEmpty(news)) throw new BusinessException(MsgService.getMsg(""));
        dao.deleteObject("dictMap.deleteDictSchool", vo);
    }

    @Override
    public void initSchoolDict(Integer school_id) {
        dao.deleteObject("dictSchoolMap.deleteSchoolDict",school_id);
        DictVO vo = new DictVO();
        vo.setSchool_id(0);
        vo.setIs_configure(1);
        List<DictVO> list = dao.queryForList("dictMap.getDictList", vo);
        for (DictVO DictVO : list) {
            DictVO.setSchool_id(school_id);
            vo.setId(dao.insertObjectReturnID("dictSchoolMap.insertDictSchool", DictVO));
        }
    }

    public void insertSchoolAPPDict(SchoolVO vo){
		if (StringUtil.isEmpty(vo.getApp_dict())) throw new BusinessException(MsgService.getMsg("UNEXPECTED_EXCEPTION"));
		List<DictVO> list = BeanUtil.jsonToList(vo.getApp_dict(),DictVO.class);
		for (DictVO dictVO:list) {
			dao.deleteObject("dictSchoolMap.removeSchoolDict",dictVO);
			dictVO.setSchool_id(vo.getSchool_id());
			dictVO.setId(dao.insertObjectReturnID("dictSchoolMap.insertDictSchool", dictVO));
		}
	}

	public DictSchoolVO addNewsDictSchool(DictSchoolVO vo){
		vo.setSchool_id(ActionUtil.getSchoolID());
		if (StringUtil.isNotEmpty(vo.getDict_value())) {//不能重名
			List<DictVO> list=dao.queryForList("dictMap.getDictByValue",vo);
			if (ListUtil.isNotEmpty(list)) throw new BusinessException(MsgService.getMsg("VALUE_SAME"));
		}
		DictVO dictVO = new DictVO();
		dictVO.setDict_group(DictConstants.DCIT_GROUP_NEWS);
		dictVO.setSchool_id(ActionUtil.getSchoolID());
		vo.setSchool_id(ActionUtil.getSchoolID());
		String news_code = "0"+dao.queryObject("dictSchoolMap.getDictSchoolMaxCode",dictVO);
		Integer sort = dao.queryObject("dictSchoolMap.getDictSchoolMaxSort", vo);
		if (IntegerUtil.isEmpty(sort)) sort = 1;
		vo.setDict_code(news_code);
		vo.setSort(sort);
		vo.setDescription(vo.getDict_value());
		dao.insertObjectReturnID("dictSchoolMap.insertDictSchool", vo);
		return vo;
	}

	public List<DictSchoolVO> getNewsDictSchoolList(DictSchoolVO vo){
		if (StringUtil.isEmpty(vo.getDict_group())){
			vo.setDict_group(DictConstants.DCIT_GROUP_NEWS);
			List<DictSchoolVO> dict = dao.queryForList("dictSchoolMap.getMinCode",vo);
			vo.setDict_group(dict.get(0).getDict_code());
		}
		return dao.queryForList("dictSchoolMap.getDictSchoolListForNews", vo);
	}

	@Override
	public List<DictSchoolVO> getNewsDictionary(DictSchoolVO vo) {
		if (StringUtil.isEmpty(vo.getDict_group())) vo.setDict_group(DictConstants.DCIT_GROUP_NEWS);
		if (vo.getDict_group().length()>3) {
			vo.setDict_code(vo.getDict_group());
			vo.setDict_group(DictConstants.DCIT_GROUP_NEWS);
		}
		List<DictSchoolVO> dictList = dao.queryForList("dictSchoolMap.getDictListByGroup",vo);
		for (DictSchoolVO dict:dictList){
			DictSchoolVO dictSchool = new DictSchoolVO();
			dictSchool.setDict_group(dict.getDict_code());
			dictSchool.setSchool_id(ActionUtil.getSchoolID());
			List<DictSchoolVO> DictSchoolList = dao.queryForList("dictSchoolMap.getDictSchoolListForNews", dictSchool);
			dict.setNews_code_list(BeanUtil.ListTojson(DictSchoolList)+"");
		}
		return dictList;
	}

	public List<Map<String,Object>> getProvinceList(){ return dao.queryForList("dictMap.getProvinceList");}

	public List<DictVO> getPayTypeList(DictVO vo){
		List<DictVO> list = dao.queryForList("dictMap.getPayDictGroupList", vo);
		for (DictVO dictVO:list) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("dict_group",dictVO.getDict_code());
			List<Map<String,Object>> list1 = dao.queryForList("dictMap.getPayDictList",map);
			dictVO.setOther_field(BeanUtil.ListTojson(list1)+"");
		}
		return list;
	}

	public void removeSchoolRoleDict(String dict_code){
		if (dict_code == null) throw new BusinessException(MsgService.getMsg("UNEXPECTED_EXCEPTION"));
		TeacherVO teacherVO = new TeacherVO(ActionUtil.getSchoolID(),dict_code);
		List<TeacherVO> teachers = userService.getTeaUserListByDuty(teacherVO);
		if (ListUtil.isNotEmpty(teachers)) throw new BusinessException(MsgService.getMsg("ROLE_TEACHER_INFO_IS_NOT_NULL"));
		DictSchoolVO vo = new DictSchoolVO();
		vo.setSchool_id(ActionUtil.getSchoolID());
		vo.setDict_code(dict_code);
		dao.deleteObject("dictSchoolMap.removeSchoolDict",vo);
		menuService.removeSchoolRoleMenuInfo(dict_code);
	}
}
