package com.ninesky.classtao.notice.service.impl;

import com.ninesky.classtao.contact.vo.ContactListVO;
import com.ninesky.classtao.dynamic.service.DynamicService;
import com.ninesky.classtao.getui.service.GetuiService;
import com.ninesky.classtao.info.service.InfoService;
import com.ninesky.classtao.info.vo.InfoReceiveVO;
import com.ninesky.classtao.info.vo.InfoVO;
import com.ninesky.classtao.notice.service.NoticeService;
import com.ninesky.classtao.notice.vo.*;
import com.ninesky.classtao.redis.service.RedisService;
import com.ninesky.classtao.user.service.UserService;
import com.ninesky.classtao.user.vo.StudentVO;
import com.ninesky.classtao.user.vo.TeacherVO;
import com.ninesky.classtao.user.vo.UserVO;
import com.ninesky.common.Constants;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.*;
import com.ninesky.common.vo.ReceiveVO;
import com.ninesky.framework.BusinessException;
import com.ninesky.framework.GeneralDAO;
import com.ninesky.framework.JedisDAO;
import com.ninesky.framework.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service("noticeServiceImpl")
public class NoticeServiceImpl implements NoticeService {

	@Autowired
	private GeneralDAO dao;

	@Autowired
	private UserService userService;

	@Autowired
	private InfoService infoService;

	@Autowired
	private DynamicService dynamicService;

	@Autowired
	private GetuiService getuiService;

	@Autowired
	private RedisService redisService;

	@Autowired
	private JedisDAO jedisDAO;

	//获取通知列表
	public List<NoticeVO> getNoticeList(NoticeVO nvo) {
		List<NoticeVO> list=new ArrayList<NoticeVO>();
		if (DictConstants.MODULE_CODE_NOTICE.equals(nvo.getModule_code())) {//家校通知
			if (DictConstants.USERTYPE_TEACHER.equals(ActionUtil.getUserType()) ||
					DictConstants.USERTYPE_ADMIN.equals(ActionUtil.getUserType())) {//教师或管理员
				NoticeGroupVO vo=new NoticeGroupVO();
				vo.setUser_id(ActionUtil.getUserID());
				vo.setSchool_id(ActionUtil.getSchoolID());
				vo.setModule_code(DictConstants.MODULE_CODE_NOTICE);
				vo.setApp_sql(ActionUtil.getParameter("app_sql"));
				vo.setOrder_sql(ActionUtil.getParameter("order_sql"));
				list=dao.queryForList("noticeGroupMap.getNoticeList", vo);
			} else {//家长端
				NoticeGroupVO vo=new NoticeGroupVO();
				vo.setSchool_id(ActionUtil.getSchoolID());
				vo.setUser_type(DictConstants.USERTYPE_STUDENT);
				StudentVO svo=userService.getStudentById(ActionUtil.getStudentID());
				vo.setGroup_id(svo.getGrade_id());
				vo.setTeam_id(svo.getClass_id());
				vo.setUser_id(ActionUtil.getStudentID());
				vo.setApp_sql(ActionUtil.getParameter("app_sql"));
				vo.setOrder_sql(ActionUtil.getParameter("order_sql"));
				list=dao.queryForList("noticeGroupMap.getNoticeListToStudent", vo);
			}
			for (NoticeVO notice:list) {
				notice.setTeam_name(getReceiveTeamName(notice));//接收通知的班级名称
			}
		} else {//校务通知
			NoticeGroupVO vo=new NoticeGroupVO();
			vo.setSchool_id(ActionUtil.getSchoolID());
			vo.setUser_type(DictConstants.USERTYPE_TEACHER);
			vo.setUser_id(ActionUtil.getUserID());
			vo.setApp_sql(ActionUtil.getParameter("app_sql"));
			vo.setOrder_sql(ActionUtil.getParameter("order_sql"));
            vo.setModule_code(DictConstants.MODULE_CODE_NOTICE_SCH);
			list=dao.queryForList("noticeGroupMap.getNoticeListToTeacher", vo);
		}
		for (NoticeVO nrvo:list){
			if (StringUtil.isNotEmpty(nrvo.getNotice_content()) &&
					nrvo.getNotice_content().length()>Constants.DEFAULT_SUBSTRING_LENGTH)//在通知列表上显示截取后的通知
				nrvo.setNotice_content(nrvo.getNotice_content().substring(
						0,Constants.DEFAULT_SUBSTRING_LENGTH)+"......");
			if (ActionUtil.getUserID().equals(nrvo.getSender_id()) &&
					(DictConstants.USERTYPE_TEACHER.equals(ActionUtil.getUserType()) ||
					DictConstants.USERTYPE_ADMIN.equals(ActionUtil.getUserType()))){//发送类型时，设置统计信息
				List<NoticeCountVO> nlist=redisService.getNoticeReceiveFromRedis(nrvo);//获取统计信息
				nrvo.setCount_list(BeanUtil.ListTojson(nlist).toString());
				if (nrvo.getTotal_count()==0) {
					getNoticeTotalCount(nrvo);//通知总人数
				}
			}
			NoticeFileVO fileVO=new NoticeFileVO();
			fileVO.setNotice_id(nrvo.getNotice_id());
			List<NoticeFileVO> fileVOList=getNoticeFileByID(fileVO);
			if (ListUtil.isNotEmpty(fileVOList))
				nrvo.setHave_file(1);
			else nrvo.setHave_file(0);
			nrvo.setSender_name(redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER,
					nrvo.getSender_id(), 0));
		}
		return list;
	}

	//通知总人数
	private void getNoticeTotalCount(NoticeVO nrvo) {
		List<NoticeGroupVO> groupVOList=dao.queryForList("noticeGroupMap.getListByNoticeID",nrvo.getNotice_id());
		if (DictConstants.MODULE_CODE_NOTICE.equals(nrvo.getModule_code())) {
            List<ReceiveVO> receiveVOList=new ArrayList<ReceiveVO>();
            for (NoticeGroupVO groupVO:groupVOList) {
                if (DictConstants.USERTYPE_TEACHER.equals(groupVO.getUser_type())) continue;
                ReceiveVO receiveVO=new ReceiveVO();
                if (IntegerUtil.isEmpty(groupVO.getUser_id())) {
                    receiveVO.setTeam_type(groupVO.getTeam_type());
                    receiveVO.setGroup_id(groupVO.getGroup_id());
                    receiveVO.setUser_type(groupVO.getUser_type());
                    receiveVO.setTeam_id(groupVO.getTeam_id());
                    receiveVO.setStudent_id(0);
                    receiveVOList.add(receiveVO);
                } else {
                    receiveVO.setTeam_type(groupVO.getTeam_type());
                    receiveVO.setUser_type(groupVO.getUser_type());
                    receiveVO.setStudent_id(groupVO.getUser_id());
                    receiveVOList.add(receiveVO);
                }
            }
            List<ReceiveVO> receiveVOs= BeanUtil.removeDuplicate(receiveVOList);
            int count=addNoticeCount(nrvo,receiveVOs);
            nrvo.setTotal_count(count);
        } else {
            int count=0;
            boolean flag=true;
            for (NoticeGroupVO groupVO:groupVOList) {
                if (groupVO.getGroup_id()==0 && groupVO.getTeam_id()==0 && groupVO.getUser_id()==0) {
                    int total_count=dao.queryObject("teacherMap.getTeacherTotal", ActionUtil.getSchoolID());
                    nrvo.setTotal_count(total_count);
                    dao.updateObject("noticeMap.addNoticeCount",nrvo);
                    flag=false;
                    break;
                } else {
					if (groupVO.getUser_id().equals(nrvo.getSender_id())) continue;
                    count++;
                }
            }
            if (flag) {
                nrvo.setTotal_count(count);
                dao.updateObject("noticeMap.addNoticeCount",nrvo);
            }
        }
	}

	//接收通知的班级名称
	private String getReceiveTeamName(NoticeVO notice) {
		List<NoticeGroupVO> groupList=dao.queryForList("noticeGroupMap.getListByNoticeID",notice.getNotice_id());
		String team_name="";
		for (NoticeGroupVO group:groupList) {
			String name="";
			if (DictConstants.TEAM_TYPE_INTEREST.equals(group.getTeam_type()) && group.getUser_id()==0)
				name=redisService.getTeamName(DictConstants.TEAM_TYPE_INTEREST,group.getGroup_id(),group.getTeam_id());
			else if (DictConstants.TEAM_TYPE_CLASS.equals(group.getTeam_type()) && group.getUser_id()==0)
			    name=redisService.getTeamName(DictConstants.TEAM_TYPE_CLASS,group.getGroup_id(),group.getTeam_id());
            else if (IntegerUtil.isNotEmpty(group.getUser_id()) && DictConstants.USERTYPE_STUDENT.equals(group.getUser_type()))
                name=redisService.getUserName(ActionUtil.getSchoolID(),DictConstants.USERTYPE_STUDENT,0,group.getUser_id());
			if (StringUtil.isEmpty(team_name) && StringUtil.isNotEmpty(name))
				team_name=name;
			else if (StringUtil.isNotEmpty(name)) team_name=team_name+"；"+name;
		}
		return team_name;
	}

	// 获取通知附件
	public List<NoticeFileVO> getNoticeFileByID(NoticeFileVO vo) {
		return dao.queryForList("noticeFileMap.getNoticeFileList", vo);
	}

	public NoticeReplyVO addReplyNotice(NoticeReplyVO vo){
		if (IntegerUtil.isEmpty(vo.getReceive_id())) throw new BusinessException("receive_id不能为空或0！");
		vo.setUser_id(ActionUtil.getUserID());
		vo.setReply_time(ActionUtil.getSysTime());// 回复时间
		vo.setSchool_id(ActionUtil.getSchoolID());
		vo.setCreate_by(ActionUtil.getUserID());
		vo.setCreate_date(ActionUtil.getSysTime());
		if (DictConstants.USERTYPE_TEACHER.equals(ActionUtil.getUserType())) {
			vo.setReply_name(redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER,ActionUtil.getUserID(), 0));
		} else {
			UserVO uvo = userService.getUserByID(ActionUtil.getUserID());
			vo.setReply_name(uvo.getUser_name());
		}
		vo.setReply_id(dao.insertObjectReturnID("noticeReplyMap.insertNoticeReply",vo));
		//redis(已读，已回复数)
		NoticeVO notice=dao.queryObject("noticeMap.getNoticeList", vo.getNotice_id());
		if (DictConstants.MODULE_CODE_NOTICE.equals(notice.getModule_code()) && DictConstants.USERTYPE_PARENT.equals(ActionUtil.getUserType())){
			redisService.updateReplyCountToRedis(notice, vo);//更改已回复数
			String keys="NOTICE:SCHOOL_ID"+ActionUtil.getSchoolID()+":MODULE_CODE"+notice.getModule_code()+
                    ":USER_TYPE003005:USER_ID*:NOTICE_ID"+vo.getNotice_id();
            Set<String> list=jedisDAO.keys(keys);
            for (String key:list)
               jedisDAO.hincrBy(key,vo.getReceive_id().toString(),1);//回复列表一个老师已读后，不影响其它老师看时的未读数
		}
		if (DictConstants.MODULE_CODE_NOTICE_SCH.equals(notice.getModule_code()) && !ActionUtil.getUserID().equals(notice.getSender_id()))
			redisService.updateReplyCountToRedis(notice, vo);//更改已回复数
		return vo;
	}

	// 新增通知
	public NoticeVO addNotice(NoticeVO vo) {
		if (StringUtil.isEmpty(vo.getNotice_title()) || StringUtil.isEmpty(vo.getNotice_content()))
			throw new BusinessException(MsgService.getMsg("UN_SEND_NOTICE"));
		Date date=ActionUtil.getSysTime();//系统当前时间(通知发送时间，创建时间)
		vo.setSender_id(ActionUtil.getUserID());
		vo.setSchool_id(ActionUtil.getSchoolID());//学校ID
		vo.setUser_type(ActionUtil.getUserType());
		vo.setSender_name(redisService.getUserName(ActionUtil.getSchoolID(), ActionUtil.getUserType(),
				ActionUtil.getUserID(), 0));//发送者姓名
		vo.setVersion(0);
		vo.setCreate_by(ActionUtil.getUserID());//用户ID
		vo.setCreate_date(date);
		vo.setNotice_id(dao.insertObjectReturnID("noticeMap.insertNotice", vo));//新增通知
		vo.setSend_time(date);
		return vo;
	}

	//添加接收群体到notice_group表（家校）
	public void addNoticeGroup(NoticeVO vo, String receive_list) {
		List<ReceiveVO> list=BeanUtil.jsonToList(receive_list, ReceiveVO.class);
		for (ReceiveVO rvo:list) {
			NoticeGroupVO gvo=new NoticeGroupVO();
			gvo.setNotice_id(vo.getNotice_id());
			gvo.setSchool_id(ActionUtil.getSchoolID());
            gvo.setTeam_type(rvo.getTeam_type());
			gvo.setGroup_id(rvo.getGroup_id());
			gvo.setTeam_id(rvo.getTeam_id());
			if (IntegerUtil.isEmpty(rvo.getStudent_id())) gvo.setUser_id(0);
			else gvo.setUser_id(rvo.getStudent_id());
			gvo.setUser_type(DictConstants.USERTYPE_STUDENT);
			gvo.setCreate_by(ActionUtil.getUserID());
			gvo.setCreate_date(ActionUtil.getSysTime());
			dao.insertObject("noticeGroupMap.insertNoticeGroup", gvo);
		}
		//单个学生时
		for (ReceiveVO rvo:list) {
            if (IntegerUtil.isEmpty(rvo.getStudent_id())) break;
            NoticeGroupVO gvo=new NoticeGroupVO();
            gvo.setNotice_id(vo.getNotice_id());
            gvo.setSchool_id(ActionUtil.getSchoolID());
            gvo.setTeam_type(rvo.getTeam_type());
            gvo.setGroup_id(0);
            gvo.setTeam_id(0);
            gvo.setUser_id(ActionUtil.getUserID());
            gvo.setUser_type(DictConstants.USERTYPE_TEACHER);
            gvo.setCreate_by(ActionUtil.getUserID());
            gvo.setCreate_date(ActionUtil.getSysTime());
            dao.insertObject("noticeGroupMap.insertNoticeGroup", gvo);
            break;
        }
	}

    public NoticeVO addSchoolNoticeToReceive(String receive_list, NoticeVO vo, String teacher_duty) {
        String[] dutyList = teacher_duty.split(",");
        for (String duty : dutyList) {
            if (DictConstants.DICT_TEACHER.equals(duty)) {//有发送给全体教师
                return vo;
            }
        }
        String[] user_ids = receive_list.split(",");
        vo.setSender_name(redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, ActionUtil.getUserID(), 0));
        for (int i = 0; i < user_ids.length; i++) {//遍历集合获取接收者信息
            if (StringUtil.isNotEmpty(user_ids[i].trim())) {//只发送给已注册的用户
                NoticeGroupVO gvo = new NoticeGroupVO();
                gvo.setNotice_id(vo.getNotice_id());
                gvo.setSchool_id(ActionUtil.getSchoolID());
                gvo.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
                gvo.setGroup_id(0);
                gvo.setTeam_id(0);
                gvo.setUser_id(Integer.parseInt(user_ids[i].trim()));
                gvo.setUser_type(DictConstants.USERTYPE_TEACHER);
                gvo.setCreate_by(ActionUtil.getUserID());
                gvo.setCreate_date(ActionUtil.getSysTime());
                if (StringUtil.isNotEmpty(teacher_duty)) {
                    NoticeGroupVO ngvo = dao.queryObject("noticeGroupMap.getNoticeGroup", gvo);
                    if (ngvo != null) continue;
                }
                dao.insertObject("noticeGroupMap.insertNoticeGroup", gvo);
            }
        }
        if (StringUtil.isEmpty(teacher_duty)) {
            NoticeGroupVO gvo = new NoticeGroupVO();
            gvo.setNotice_id(vo.getNotice_id());
            gvo.setSchool_id(ActionUtil.getSchoolID());
            gvo.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
            gvo.setGroup_id(0);
            gvo.setTeam_id(0);
            gvo.setUser_id(ActionUtil.getUserID());
            gvo.setUser_type(DictConstants.USERTYPE_TEACHER);
            gvo.setCreate_by(ActionUtil.getUserID());
            gvo.setCreate_date(ActionUtil.getSysTime());
            dao.insertObject("noticeGroupMap.insertNoticeGroup", gvo);//发送者也能看到该条通知
        }
        return vo;
    }

    public NoticeVO addSchoolNoticeByDuty(String dutys,NoticeVO vo) {
        boolean flag=true;
        String[] dutyList=dutys.split(",");
        for (String duty:dutyList){
            if (DictConstants.DICT_TEACHER.equals(duty)){//有发送给全体教师
                addSchoolNoticeToAll(vo);//添加多条接收的信息到notice_receive表
                redisService.addSchoolNoticeToRedis(vo, null, DictConstants.DICT_TEACHER);
                flag=false;
                break;
            }
        }
        if (flag){//没有全体教师
            Map<String,Object> map=new HashMap<String, Object>();
            List<TeacherVO> tlist=new ArrayList<TeacherVO>();
            for(String duty:dutyList){
                vo.setSender_name(redisService.getUserName(ActionUtil.getSchoolID(),
                        DictConstants.USERTYPE_TEACHER, ActionUtil.getUserID(), 0));
                TeacherVO tvo=new TeacherVO();
                tvo.setSchool_id(ActionUtil.getSchoolID());
                getTeacherList(tlist, duty, tvo);
            }
            for (TeacherVO teacherVO:tlist){//遍历集合获取接收者信息
                if (map.containsKey(teacherVO.getPhone())) continue;
                if (IntegerUtil.isNotEmpty(teacherVO.getUser_id()) && !ActionUtil.getUserID().equals
                        (teacherVO.getUser_id())) {//只发送给已注册的用户
                    NoticeGroupVO gvo=new NoticeGroupVO();
                    gvo.setNotice_id(vo.getNotice_id());
                    gvo.setSchool_id(ActionUtil.getSchoolID());
                    gvo.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
                    gvo.setGroup_id(0);
                    gvo.setTeam_id(0);
                    gvo.setUser_id(teacherVO.getUser_id());
                    gvo.setUser_type(DictConstants.USERTYPE_TEACHER);
                    gvo.setCreate_by(ActionUtil.getUserID());
                    gvo.setCreate_date(ActionUtil.getSysTime());
                    dao.insertObject("noticeGroupMap.insertNoticeGroup", gvo);
                    map.put(teacherVO.getPhone(),teacherVO);//去重
                }
            }
            redisService.addSchoolNoticeToRedisByDuty(vo,tlist);
            NoticeGroupVO gvo=new NoticeGroupVO();
            gvo.setNotice_id(vo.getNotice_id());
            gvo.setSchool_id(ActionUtil.getSchoolID());
            gvo.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
            gvo.setGroup_id(0);
            gvo.setTeam_id(0);
            gvo.setUser_id(ActionUtil.getUserID());
            gvo.setUser_type(DictConstants.USERTYPE_TEACHER);
            gvo.setCreate_by(ActionUtil.getUserID());
            gvo.setCreate_date(ActionUtil.getSysTime());
            dao.insertObject("noticeGroupMap.insertNoticeGroup", gvo);//发送者能查看自己发的通知
        }
        return vo;
    }

    //获取接收者
    private void getTeacherList(List<TeacherVO> tlist, String duty, TeacherVO tvo) {
        if (DictConstants.DICT_TEACHER_ADVISER.equals(duty)){//班主任
            tvo.setIs_charge(1);
            List<TeacherVO> teacherlist=userService.getChargeTeacherList(tvo);
            tlist.addAll(teacherlist);
        } else {
            tvo.setDuty(duty);
            List<TeacherVO> teacherlist=userService.getDutyTeacherList(tvo);//该职务的所有教师
            tlist.addAll(teacherlist);
        }
    }

    public NoticeVO addSchoolNoticeToAll(NoticeVO vo) {
		vo.setSender_name(redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, ActionUtil.getUserID(), 0));
		NoticeGroupVO gvo=new NoticeGroupVO();
		gvo.setNotice_id(vo.getNotice_id());
		gvo.setSchool_id(ActionUtil.getSchoolID());
        gvo.setTeam_type(DictConstants.TEAM_TYPE_CLASS);
		gvo.setGroup_id(0);
		gvo.setTeam_id(0);
		gvo.setUser_id(0);
		gvo.setUser_type(DictConstants.USERTYPE_TEACHER);
		gvo.setCreate_by(ActionUtil.getUserID());
		gvo.setCreate_date(ActionUtil.getSysTime());
		dao.insertObject("noticeGroupMap.insertNoticeGroup", gvo);
		return vo;
	}

    //校务通知的动态和推送
    public void dynamicAndPush(NoticeVO vo, String receive_list, String dutys) {
        //存放一组接收者的信息
        List<ReceiveVO> receivelist = new ArrayList<ReceiveVO>();
        HashMap<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("info_title", StringUtil.subString(vo.getNotice_title(), 15));
        dataMap.put("info_content", StringUtil.subString(vo.getNotice_content(), 20));
        dataMap.put("module_code", vo.getModule_code());
        dataMap.put("module_pkid", vo.getNotice_id().toString());
        dataMap.put("link_type", DictConstants.LINK_TYPE_DETAIL);
        dataMap.put("info_url", "detail.html");
        dataMap.put("info_date", ActionUtil.getSysTime().getTime() + "");
        dataMap.put("user_type", DictConstants.USERTYPE_TEACHER);
        boolean flag = true;
        if (StringUtil.isNotEmpty(dutys)) {
            String[] dutyList = dutys.split(",");
            for (String duty : dutyList) {
                if (DictConstants.DICT_TEACHER.equals(duty)) {//有发送给全体教师
                    ReceiveVO teacher = new ReceiveVO(ActionUtil.getSchoolID(), DictConstants.TEAM_TYPE_CLASS, 0, 0);
                    teacher.setUser_type(DictConstants.USERTYPE_TEACHER);
                    receivelist.add(teacher);
                    flag = false;
                    break;
                }
            }
            if (flag) {//不含所有教师
                List<TeacherVO> tlist = new ArrayList<TeacherVO>();
                for(String duty:dutyList){
                    TeacherVO tvo=new TeacherVO();
                    tvo.setSchool_id(ActionUtil.getSchoolID());
                    getTeacherList(tlist, duty, tvo);
                }
                Map<String, Object> map = new HashMap<String, Object>();
                for (TeacherVO teacherVO : tlist) {
                    if (map.containsKey(teacherVO.getPhone())) continue;
                    if (IntegerUtil.isNotEmpty(teacherVO.getUser_id())) {//只发给已注册的
                        ReceiveVO Rvo = new ReceiveVO(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, teacherVO.getUser_id());
                        receivelist.add(Rvo);
                    }
                    map.put(teacherVO.getPhone(), teacherVO);
                }
            }
        }
        if (StringUtil.isNotEmpty(receive_list) && flag == true) {//user_id,逗号隔开，自定义
            String[] user_ids = receive_list.split(",");
            int teamCount = user_ids.length;
            for (int i = 0; i < teamCount; i++) {
                if (StringUtil.isNotEmpty(user_ids[i].trim())) {//只发给已注册的
                    ReceiveVO Rvo = new ReceiveVO(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, Integer.parseInt(user_ids[i].trim()));
                    receivelist.add(Rvo);
                }
            }
            //发送者
            ReceiveVO Rvo = new ReceiveVO(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, ActionUtil.getUserID());
            receivelist.add(Rvo);
        }
        dynamicService.insertDynamic(dataMap, receivelist);//动态
        getuiService.pushMessage(dataMap,receivelist);//推送
    }

	// 添加附件
	public List<NoticeFileVO> addFile(List<NoticeFileVO> list) {
		dao.insertObject("noticeFileMap.insertNoticeFileList", list);
		return list;
	}

	// 获取通知接收情况(以Map形式存放已收到，已查看，已回复的情况)
	public List<NoticeCountVO>  getNoticeReceiveByIDOld(Integer notice_id) {
		List<NoticeCountVO>  list = new ArrayList<NoticeCountVO>();
		NoticeReceiveVO vo = new NoticeReceiveVO();
		vo.setNotice_id(notice_id);
		vo.setSend_type(DictConstants.INFO_TYPE_RECEIVE);//只统计接收的人
		list= dao.queryForList("noticeReceiveMap.selectCount", vo);// 符合条件的人数(条数)
		return list;
	}

	//将未读通知设为已读通知
	public Integer setRead(NoticeReceiveVO vo) {
		vo.setSchool_id(ActionUtil.getSchoolID());
		int i=dao.updateObject("noticeReceiveMap.updateNoticeReceive", vo);
		if (i!=0){
			InfoReceiveVO infoReceiveVO=new InfoReceiveVO();
			infoReceiveVO.setUser_id(vo.getUser_id());
			infoReceiveVO.setStudent_id(vo.getStudent_id());
			infoReceiveVO.setModule_code(DictConstants.MODULE_CODE_NOTICE);
			infoReceiveVO.setUser_type(vo.getUser_type());
			infoReceiveVO.setCount_info("该通知您已查看过！");
			infoReceiveVO.setUpdate_by(ActionUtil.getUserID());
			infoReceiveVO.setUpdate_date(ActionUtil.getSysTime());
			infoReceiveVO.setModule_pkid(vo.getNotice_id());
			dao.updateObject("infoReceiveMap.updateNoticeCountInfo", infoReceiveVO);
			updateInfo(vo);
		}
		return i;
	}

	//获取通知对应的图片附件
	public String getPhotoList(NoticeReceiveVO nrvo) {
		NoticeFileVO fileVO=new NoticeFileVO();
		fileVO.setNotice_id(nrvo.getNotice_id());
		List<NoticeFileVO> filelist=getNoticeFileByID(fileVO);//获取通知附件
		String photolist=null;
		if (ListUtil.isNotEmpty(filelist)) {
			for (NoticeFileVO noticeFileVO:filelist){
				if (DictConstants.FILE_TYPE_PICTURE.equals(noticeFileVO.getFile_type())){//只存放图片类型的附件(缩略图)
					if (photolist==null)
						photolist=noticeFileVO.getFile_resize_url();
					else
						photolist=photolist+","+noticeFileVO.getFile_resize_url();
				}
			}
		}
		return photolist;
	}

	//设置动态list
	public List<InfoReceiveVO> setInfoReceiveList(List<NoticeReceiveVO> list,int uncollectedCount,int replyCount,
												  List<InfoReceiveVO> infoList){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		for (NoticeReceiveVO vo:list){//转换list元素对象
			InfoReceiveVO infoVO=new InfoReceiveVO();
			getInfoVO(sdf, vo, infoVO);//集合里的对象
			if (ActionUtil.getUserID()==vo.getUser_id()){//如果是发送者，才显示统计信息
				List<NoticeCountVO> receivelist=getNoticeReceiveByIDOld(vo.getNotice_id());//通知的接收情况
				for (NoticeCountVO noticeCountVO:receivelist){
					if (DictConstants.INFO_STATUS_RECEIVED.equals(noticeCountVO.getNotice_status())){//未确认的人数
						uncollectedCount=noticeCountVO.getCount();
					} else if (DictConstants.INFO_STATUS_REPLAY.equals(noticeCountVO.getNotice_status())){//已回复的人数
						replyCount=noticeCountVO.getCount();
					}
				}
				infoVO.setCount_info(uncollectedCount+"人未确认      "+replyCount+"人回复");
			} else {
				infoVO.setCount_info("您还未查看该通知！");
			}
			String photolist = getPhotoList(vo);
			infoVO.setPhoto_list(photolist);
			infoVO.setShow_type(DictConstants.SHOW_TYPE_DEFAULT);//全部显示
			infoList.add(infoVO);
		}
		return infoList;
	}

	//集合里的对象
	public void getInfoVO(SimpleDateFormat sdf, NoticeReceiveVO vo,InfoReceiveVO infoVO) {
		infoVO.setSchool_id(vo.getSchool_id());
		infoVO.setGrade_id(vo.getGrade_id());
		infoVO.setClass_id(vo.getClass_id());
		infoVO.setUser_id(vo.getUser_id());
		infoVO.setSender_id(ActionUtil.getUserID());
		infoVO.setStudent_id(vo.getStudent_id());
		infoVO.setModule_code(DictConstants.MODULE_CODE_NOTICE);//通知模块
		infoVO.setModule_pkid(vo.getNotice_id());
		infoVO.setUser_type(vo.getUser_type());
		infoVO.setReceive_name(vo.getReceive_name());
		infoVO.setInfo_date(sdf.format(ActionUtil.getSysTime()));
		infoVO.setInfo_title(vo.getNotice_title());
		infoVO.setInfo_content(vo.getNotice_content());
		infoVO.setInfo_type(DictConstants.INFO_TYPE_LOCAL);//消息推送类型:本地模块消息
		infoVO.setCreate_by(ActionUtil.getUserID());
		infoVO.setCreate_date(ActionUtil.getSysTime());
		if (DictConstants.USERTYPE_TEACHER.equals(infoVO.getUser_type()))
			infoVO.setInfo_url("detail.html");
		else infoVO.setInfo_url("pdetail.html");
	}


	//更新相应动态
	public void updateInfo(NoticeReceiveVO vo) {
		int uncollectedCount = 0;//未确认数
		int replyCount=0;
		InfoReceiveVO infoReceiveVO=new InfoReceiveVO();
		infoReceiveVO.setInfo_title(vo.getNotice_title());
		infoReceiveVO.setInfo_content(vo.getNotice_content());
		infoReceiveVO.setModule_code(DictConstants.MODULE_CODE_NOTICE);//通知模块
		infoReceiveVO.setModule_pkid(vo.getNotice_id());
		List<NoticeCountVO> list2=getNoticeReceiveByIDOld(vo.getNotice_id());//通知的接收情况
		for (NoticeCountVO noticeCountVO:list2){
			if (DictConstants.INFO_STATUS_RECEIVED.equals(noticeCountVO.getNotice_status())){//未确认人数
				uncollectedCount=noticeCountVO.getCount();
			} else if (DictConstants.INFO_STATUS_REPLAY.equals(noticeCountVO.getNotice_status())){//已回复人数
				replyCount=noticeCountVO.getCount();
			}
		}
		infoReceiveVO.setCount_info(uncollectedCount+"人未确认      "+replyCount+"人回复");//统计信息
		infoReceiveVO.setInfo_type(DictConstants.INFO_TYPE_LOCAL);
		NoticeVO noticeVO=dao.queryObject("noticeMap.getNoticeList", vo.getNotice_id());
		infoReceiveVO.setInfo_title(noticeVO.getNotice_title());
		infoReceiveVO.setInfo_content(noticeVO.getNotice_content());
		infoReceiveVO.setSend_id(vo.getSender_id());//只更新发送者的动态
		infoReceiveVO.setUser_type(DictConstants.USERTYPE_TEACHER);
		infoReceiveVO.setUpdate_by(ActionUtil.getUserID());
		infoReceiveVO.setUpdate_date(ActionUtil.getSysTime());
		infoReceiveVO.setStudent_id(vo.getStudent_id());
		infoReceiveVO.setUser_id(vo.getUser_id());
		infoReceiveVO.setSchool_id(ActionUtil.getSchoolID());
		infoService.updateInformation(infoReceiveVO);
	}

	//回复通知后，更新相应动态
	public void updateInfoByReply(NoticeReplyVO vo) {
		int uncollectedCount = 0;//未确认数
		int replyCount=0;
		InfoReceiveVO infoReceiveVO=new InfoReceiveVO();
		infoReceiveVO.setModule_code(DictConstants.MODULE_CODE_NOTICE);//通知模块
		infoReceiveVO.setModule_pkid(vo.getNotice_id());
		List<NoticeCountVO> list2=getNoticeReceiveByIDOld(vo.getNotice_id());//通知的接收情况
		for (NoticeCountVO noticeCountVO:list2){
			if (DictConstants.INFO_STATUS_RECEIVED.equals(noticeCountVO.getNotice_status())){//未确认人数
				uncollectedCount=noticeCountVO.getCount();
			} else if(DictConstants.INFO_STATUS_REPLAY.equals(noticeCountVO.getNotice_status())){//已回复人数
				replyCount=noticeCountVO.getCount();
			}
		}
		infoReceiveVO.setCount_info(uncollectedCount+"人未确认      "+replyCount+"人回复");//统计信息
		infoReceiveVO.setInfo_type(DictConstants.INFO_TYPE_LOCAL);
		NoticeVO noticeVO=dao.queryObject("noticeMap.getNoticeList", vo.getNotice_id());
		infoReceiveVO.setInfo_title(noticeVO.getNotice_title());
		infoReceiveVO.setInfo_content(noticeVO.getNotice_content());
		infoReceiveVO.setSend_id(noticeVO.getSender_id());//只更新发送者的动态
		infoReceiveVO.setUpdate_by(ActionUtil.getUserID());
		infoReceiveVO.setUpdate_date(ActionUtil.getSysTime());
		infoReceiveVO.setUser_id(noticeVO.getSender_id());
		infoReceiveVO.setStudent_id(vo.getStudent_id());
		infoReceiveVO.setSchool_id(ActionUtil.getSchoolID());
		infoReceiveVO.setUser_type(DictConstants.USERTYPE_TEACHER);
		infoReceiveVO.setInfo_url("conve.html");
		infoService.updateInformation(infoReceiveVO);
	}

	//未确认某条通知的用户列表
	public List<NoticeReceiveVO> getUserListOfUnread(Integer notice_id){
		List<NoticeGroupVO> list=dao.queryForList("noticeGroupMap.getListByNoticeID", notice_id);
		List<NoticeReceiveVO> nlist=new ArrayList<NoticeReceiveVO>();
		HashMap<Integer,Object> map=new HashMap<Integer,Object>();//过滤用
		for (NoticeGroupVO vo:list) {
			if (DictConstants.USERTYPE_STUDENT.equals(vo.getUser_type())) {
				List<StudentVO> slist = getStudentList(vo);//获取该group下的所有学生
				for (StudentVO stuvo:slist) {
					String unionKey=RedisKeyUtil.getNoticeUnionKey(vo.getModule_code(),vo.getUser_type(), ActionUtil.getSchoolID(), null, stuvo.getStudent_id())+":NOTICE_ID"+notice_id;
					boolean b=redisService.is_readFromRedis(unionKey);
					if (!b) {//未读
						if (map.containsKey(stuvo.getStudent_id())) continue;//例同时选普通班级和兴趣班
						NoticeReadVO read=new NoticeReadVO();
						read.setNotice_id(notice_id);
						read.setUser_type(DictConstants.USERTYPE_STUDENT);
						read.setStudent_id(stuvo.getStudent_id());
						NoticeReadVO readVO=dao.queryObject("noticeReadMap.getNoticeRead",read);
						if (readVO==null) {//有redis清空的情况，得判断数据库中是否有已读记录
							NoticeReceiveVO nrvo = new NoticeReceiveVO();
							nrvo.setReceive_name(redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_STUDENT, 0, stuvo.getStudent_id()));
							nrvo.setStudent_id(stuvo.getStudent_id());
							nrvo.setUser_id(0);
							nlist.add(nrvo);
							map.put(stuvo.getStudent_id(), nrvo.getReceive_name());
						}
					}
				}
			} else {
				if (IntegerUtil.isEmpty(vo.getUser_id())) {//校务通知里的全部教师情况
					ReceiveVO rvo=new ReceiveVO();
					rvo.setSchool_id(ActionUtil.getSchoolID());
					rvo.setGroup_id(vo.getGroup_id());
					rvo.setTeam_id(vo.getTeam_id());
					List<Integer> slist=userService.getTeacherUserID(rvo);
					for (Integer user_id:slist) {
						if (IntegerUtil.isEmpty(user_id) || user_id.equals(vo.getCreate_by())) continue;
						String unionKey=RedisKeyUtil.getNoticeUnionKey(vo.getModule_code(),vo.getUser_type(), ActionUtil.getSchoolID(), user_id,null)+":NOTICE_ID"+notice_id;
						boolean b=redisService.is_readFromRedis(unionKey);
						if (!b) {
							NoticeReadVO read=new NoticeReadVO();
							read.setNotice_id(notice_id);
							read.setUser_type(DictConstants.USERTYPE_TEACHER);
							read.setUser_id(user_id);
							NoticeReadVO readVO=dao.queryObject("noticeReadMap.getNoticeRead",read);
							if (readVO==null) {//有redis清空的情况，得判断数据库中是否有已读记录
								NoticeReceiveVO nrvo = new NoticeReceiveVO();
								nrvo.setReceive_name(redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER,
										user_id, 0));
								nrvo.setStudent_id(0);
								nrvo.setUser_id(user_id);
								nlist.add(nrvo);
							}
						}
					}
				} else {
					if (vo.getUser_id().equals(vo.getCreate_by())) continue;
					String unionKey=RedisKeyUtil.getNoticeUnionKey(vo.getModule_code(),vo.getUser_type(), ActionUtil.getSchoolID(), vo.getUser_id(),null)+":NOTICE_ID"+notice_id;
					boolean b=redisService.is_readFromRedis(unionKey);
					if (!b) {
						NoticeReadVO read=new NoticeReadVO();
						read.setNotice_id(notice_id);
						read.setUser_type(DictConstants.USERTYPE_TEACHER);
						read.setUser_id(vo.getUser_id());
						NoticeReadVO readVO=dao.queryObject("noticeReadMap.getNoticeRead",read);
						if (readVO==null) {//有redis清空的情况，得判断数据库中是否有已读记录
							NoticeReceiveVO nrvo = new NoticeReceiveVO();
							nrvo.setReceive_name(redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER,
									vo.getUser_id(), 0));
							nrvo.setStudent_id(0);
							nrvo.setUser_id(vo.getUser_id());
							nlist.add(nrvo);
						}
					}
				}
			}
		}
		return nlist;
	}

	//groupVO下的所有学生
	private List<StudentVO> getStudentList(NoticeGroupVO vo) {
		List<StudentVO> slist=new ArrayList<StudentVO>();
		if (DictConstants.TEAM_TYPE_INTEREST.equals(vo.getTeam_type()) && IntegerUtil.isEmpty(vo.getUser_id())) {//兴趣班
            ContactListVO cvo=new ContactListVO();
            cvo.setSchool_id(ActionUtil.getSchoolID());
            cvo.setContact_id(vo.getTeam_id());
            cvo.setUser_type(DictConstants.USERTYPE_STUDENT);
            slist=dao.queryForList("contactListMap.getContactList", cvo);
        } else if (IntegerUtil.isNotEmpty(vo.getUser_id())){//以学生为个体发送
            StudentVO svo=new StudentVO();
            svo.setStudent_id(vo.getUser_id());
            slist.add(svo);
        } else {//行政班级
            StudentVO svo=new StudentVO();
            svo.setSchool_id(ActionUtil.getSchoolID());
            svo.setGrade_id(vo.getGroup_id());
            svo.setClass_id(vo.getTeam_id());
            svo.setApp_sql(ActionUtil.getParameter("app_sql"));
            svo.setOrder_sql(ActionUtil.getParameter("order_sql"));
            slist=userService.getStuUserList(svo);
        }
		return slist;
	}

	//获取回复记录列表（除了发送者的）
	public List<NoticeReplyVO> getReplyList(NoticeReplyVO vo) {
		if (IntegerUtil.isEmpty(vo.getReceive_id())){//所有人回复的列表
			List<NoticeReplyVO> list=dao.queryForList("noticeReplyMap.getNoticeReplyListByNoticeid", vo.getNotice_id());
			if (ListUtil.isEmpty(list)) return list;
			NoticeVO noticeVO=dao.queryObject("noticeMap.getNoticeList", vo.getNotice_id());//获取通知的发送者
			for (NoticeReplyVO nrvo:list){
				if (DictConstants.MODULE_CODE_NOTICE.equals(noticeVO.getModule_code())) {
					String key="NOTICE:SCHOOL_ID"+ActionUtil.getSchoolID()+":MODULE_CODE"+DictConstants.MODULE_CODE_NOTICE+
							":USER_TYPE003005:USER_ID"+ActionUtil.getUserID()+":NOTICE_ID"+vo.getNotice_id();
					if (jedisDAO.hget(key,nrvo.getReceive_id().toString())==null)
						nrvo.setCount(0);
					else
						nrvo.setCount(Integer.parseInt(jedisDAO.hget(key,nrvo.getReceive_id().toString()).trim()));
				} else {
					NoticeReplyVO replyVO = new NoticeReplyVO();
					replyVO.setNotice_id(nrvo.getNotice_id());
					replyVO.setReceive_id(nrvo.getReceive_id());
					replyVO.setUser_type(ActionUtil.getUserType());
					replyVO.setSender_id(ActionUtil.getUserID());
					int count = dao.queryObject("noticeReplyMap.getCountOfReply1", replyVO);
					nrvo.setCount(count);//回复的未读数（某一个人）
				}
				if (DictConstants.USERTYPE_STUDENT.equals(nrvo.getReceive_type())) {
					nrvo.setReceive_name(redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_STUDENT,0, nrvo.getReceive_id()));
				} else if (DictConstants.USERTYPE_TEACHER.equals(nrvo.getReceive_type())){
					nrvo.setReceive_name(redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER,nrvo.getReceive_id(), 0));
				}
				if (DictConstants.USERTYPE_TEACHER.equals(nrvo.getUser_type())) {
					nrvo.setReply_name(redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, nrvo.getUser_id(),0));
				} else {
					UserVO uvo = userService.getUserByID(nrvo.getUser_id());
					nrvo.setReply_name(uvo.getUser_name());
				}
			}
			return list;
		} else {
			List<NoticeReplyVO> list=dao.queryForList("noticeReplyMap.getNoticeReplyList1", vo);//某条通知的所有回复列表
			for (NoticeReplyVO nrvo:list) {
				if (DictConstants.USERTYPE_STUDENT.equals(nrvo.getReceive_type())) {
					nrvo.setReceive_name(redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_STUDENT,0, nrvo.getReceive_id()));
				} else if (DictConstants.USERTYPE_TEACHER.equals(nrvo.getReceive_type())){
					nrvo.setReceive_name(redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER,
							nrvo.getReceive_id(), 0));
				}
				if (DictConstants.USERTYPE_TEACHER.equals(nrvo.getUser_type())) {
					nrvo.setReply_name(redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, nrvo.getUser_id(),0));
				} else {
					UserVO uvo = userService.getUserByID(nrvo.getUser_id());
					nrvo.setReply_name(uvo.getUser_name());
				}
			}
			NoticeVO noticeVO=dao.queryObject("noticeMap.getNoticeList", vo.getNotice_id());
			if (DictConstants.MODULE_CODE_NOTICE.equals(noticeVO.getModule_code()) &&
					DictConstants.USERTYPE_TEACHER.equals(ActionUtil.getUserType())){
				String key="NOTICE:SCHOOL_ID"+ActionUtil.getSchoolID()+":MODULE_CODE"+DictConstants.MODULE_CODE_NOTICE+
						":USER_TYPE003005:USER_ID"+ActionUtil.getUserID()+":NOTICE_ID"+vo.getNotice_id();
				jedisDAO.hset(key,vo.getReceive_id().toString(),0+"");
				dao.updateObject("noticeReplyMap.updateIsRead1", vo);//更新成已读
			}
			if (DictConstants.MODULE_CODE_NOTICE_SCH.equals(noticeVO.getModule_code()) &&
					ActionUtil.getUserID().equals(noticeVO.getSender_id()))
				dao.updateObject("noticeReplyMap.updateIsRead1", vo);//更新成已读
			return list;
		}
	}

	//回复通知后，添加动态
	public Integer addInfoOfReply(NoticeReplyVO vo) {
		InfoVO info=new InfoVO();
		info.setSchool_id(ActionUtil.getSchoolID());
		info.setSender_id(0);//特殊处理，与普通通知区分
		info.setModule_code(DictConstants.MODULE_CODE_NOTICE);
		info.setModule_pkid(vo.getNotice_id());
		InfoVO ivo=dao.queryObject("infoMap.getInfoOfReply", info);//判断是否是第一条回复
		NoticeVO notice=dao.queryObject("noticeMap.getNoticeList", vo.getNotice_id());
		notice.setSender_name(redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER,
				notice.getSender_id(), 0));
		InfoVO infoVO=new InfoVO();
		infoVO.setInfo_content(vo.getReply_content());
		try {
			infoVO.setInfo_date(DateUtil.formatDate(ActionUtil.getSysTime(), "yyyy-MM-dd"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		InfoReceiveVO irvo=new InfoReceiveVO();
		irvo.setInfo_date(infoVO.getInfo_date());
		//第一条回复
		if (ivo==null) return addReplyInfo(vo, notice, infoVO, irvo);//添加回复动态
		else updateReplyInfo(vo, ivo, notice, infoVO, irvo);//修改回复动态
		return ivo.getInfo_id();
	}

	//修改回复动态
	private void updateReplyInfo(NoticeReplyVO vo, InfoVO ivo, NoticeVO notice, InfoVO infoVO, InfoReceiveVO irvo) {
		infoVO.setInfo_id(ivo.getInfo_id());
		List<NoticeCountVO> list=getNoticeReceiveByIDOld(vo.getNotice_id());
		for (NoticeCountVO count:list){
			if (DictConstants.INFO_STATUS_REPLAY.equals(count.getNotice_status())){
				infoVO.setInfo_title("["+notice.getNotice_title()+"]有新的回复（回复总数"+count.getCount()+"条）");
				break;
			}
		}
		infoVO.setUpdate_by(ActionUtil.getUserID());
		infoVO.setUpdate_date(ActionUtil.getSysTime());
		infoVO.setInfo_url("conve.html");
		dao.updateObject("infoMap.updateInfo", infoVO);
		irvo.setInfo_id(ivo.getInfo_id());
		irvo.setInfo_title(infoVO.getInfo_title());
		irvo.setInfo_content(vo.getReply_content());
		irvo.setUpdate_by(ActionUtil.getUserID());
		irvo.setUpdate_date(ActionUtil.getSysTime());
		dao.updateObject("infoReceiveMap.updateInformation", irvo);
	}

	//添加回复动态
	private Integer addReplyInfo(NoticeReplyVO vo, NoticeVO notice, InfoVO infoVO,InfoReceiveVO irvo) {
		infoVO.setSchool_id(ActionUtil.getSchoolID());
		infoVO.setUser_type(ActionUtil.getUserType());
		infoVO.setSender_id(0);//特殊处理，与普通通知区分
		infoVO.setModule_code(DictConstants.MODULE_CODE_NOTICE);
		infoVO.setModule_pkid(vo.getNotice_id());//放notice_id
		infoVO.setInfo_type(DictConstants.INFO_TYPE_LOCAL);
		infoVO.setInfo_title("["+notice.getNotice_title()+"]有新的回复（回复总数1条）");
		infoVO.setShow_type(DictConstants.SHOW_TYPE_DEFAULT);
		infoVO.setCreate_by(ActionUtil.getUserID());
		infoVO.setCreate_date(ActionUtil.getSysTime());
		infoVO.setInfo_url("conve.html");
		List<InfoReceiveVO> list=new ArrayList<InfoReceiveVO>();
		irvo.setSchool_id(ActionUtil.getSchoolID());
		NoticeReceiveVO nrvo=dao.queryObject("noticeReceiveMap.getReceiveInfo", vo.getReceive_id());
		irvo.setClass_id(nrvo.getClass_id());
		irvo.setGrade_id(nrvo.getGrade_id());
		irvo.setUser_id(notice.getSender_id());//动态是给发这条通知的人
		irvo.setSender_id(0);
		irvo.setModule_code(DictConstants.MODULE_CODE_NOTICE);
		irvo.setModule_pkid(vo.getNotice_id());//放notice_id
		irvo.setUser_type(DictConstants.USERTYPE_TEACHER);
		irvo.setStudent_id(0);
		irvo.setReceive_name(notice.getSender_name());
		irvo.setInfo_type(infoVO.getInfo_type());
		irvo.setInfo_title(infoVO.getInfo_title());
		irvo.setInfo_content(infoVO.getInfo_content());
		irvo.setShow_type(infoVO.getShow_type());
		irvo.setCreate_by(ActionUtil.getUserID());
		irvo.setCreate_date(ActionUtil.getSysTime());
		list.add(irvo);
		return infoService.addInfo(infoVO, list);
	}

	@Override
	public NoticeVO getNoticeByID(Integer notice_id) {
		NoticeVO vo=dao.queryObject("noticeMap.getNoticeList", notice_id);
		vo.setSender_name(redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER,
				vo.getSender_id(), 0));
		return vo;
	}

    public NoticeVO getNoticeById(Integer notice_id) {
        NoticeVO vo = dao.queryObject("noticeMap.getNoticeList", notice_id);
        vo.setSender_name(redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER,
                vo.getSender_id(), 0));
        //家校通知
        if (DictConstants.MODULE_CODE_NOTICE.equals(vo.getModule_code()) && ActionUtil.getUserType().equals(DictConstants.USERTYPE_PARENT)) {//接收者查阅,更改已读
			redisService.updateIs_readCount(vo);
			NoticeReadVO read=new NoticeReadVO();
			read.setNotice_id(notice_id);
			read.setUser_type(DictConstants.USERTYPE_STUDENT);
			read.setStudent_id(ActionUtil.getStudentID());
			NoticeReadVO readVO=dao.queryObject("noticeReadMap.getNoticeRead",read);
			if (readVO.getIs_confirm()==1) vo.setHave_confirm(true);//已确认过
		}else if (DictConstants.MODULE_CODE_NOTICE_SCH.equals(vo.getModule_code()) &&
                !ActionUtil.getUserID().equals(vo.getSender_id())) {//校务通知
			redisService.updateIs_readCount(vo);
			NoticeReadVO read=new NoticeReadVO();
			read.setNotice_id(notice_id);
			read.setUser_type(DictConstants.USERTYPE_TEACHER);
			read.setUser_id(ActionUtil.getUserID());
			NoticeReadVO readVO=dao.queryObject("noticeReadMap.getNoticeRead",read);
			if (readVO.getIs_confirm()==1) vo.setHave_confirm(true);
		}
		NoticeFileVO fileVO = new NoticeFileVO();
        fileVO.setNotice_id(vo.getNotice_id());
        List<NoticeFileVO> filelist = getNoticeFileByID(fileVO);//获取通知附件
        if (ListUtil.isNotEmpty(filelist))
            vo.setFile_list(BeanUtil.ListTojson(filelist).toString());
        if (DictConstants.MODULE_CODE_NOTICE.equals(vo.getModule_code()))
            vo.setTeam_name(getReceiveTeamName(vo));//接收通知的班级名称
        vo.setUnreadCount(vo.getTotal_count()-vo.getReadCount());//未读人数
        String key=RedisKeyUtil.getNoticeRemindKey(vo.getModule_code(),notice_id,ActionUtil.getSchoolID());
		//是否可进行未读提醒
        if (jedisDAO.exists(key)) vo.setCan_remind(false);
        else vo.setCan_remind(true);
        return vo;
    }

    public Integer addNoticeCount(NoticeVO vo, List<ReceiveVO> list) {
        int count = 0;
        for (ReceiveVO rvo : list) {
            if (IntegerUtil.isNotEmpty(rvo.getStudent_id())) {
                count++;
            } else if (DictConstants.TEAM_TYPE_CLASS.equals(rvo.getTeam_type())) {
                StudentVO svo = new StudentVO();
                svo.setSchool_id(ActionUtil.getSchoolID());
                svo.setGrade_id(rvo.getGroup_id());
                svo.setClass_id(rvo.getTeam_id());
				ActionUtil.setPage_app(false);
				ActionUtil.setPage_web(false);
                List<StudentVO> slist = userService.getStuUserList(svo);
                count = count + slist.size();
            }
        }
        vo.setTotal_count(count);
        dao.updateObject("noticeMap.addNoticeCount",vo);
        return count;
    }

    public Integer addSchoolNoticeCount(NoticeVO vo,String receivelist,String teacherduty) {
        int count=0;
        boolean flag = true;
        String[] dutyList = teacherduty.split(",");
        for (String duty : dutyList) {
            if (DictConstants.DICT_TEACHER.equals(duty)) {//有发送给全体教师
                count=dao.queryObject("teacherMap.getTeacherTotal",ActionUtil.getSchoolID());
                flag = false;
                break;
            }
        }
        if (flag) {//没有全体教师
            Map<String, Object> map = new HashMap<String, Object>();
            List<TeacherVO> tlist = new ArrayList<TeacherVO>();
            for (String duty : dutyList) {
                TeacherVO tvo = new TeacherVO();
                tvo.setSchool_id(ActionUtil.getSchoolID());
                getTeacherList(tlist, duty, tvo);
            }
            for (TeacherVO teacherVO : tlist) {//遍历集合获取接收者信息
                if (map.containsKey(teacherVO.getPhone())) continue;
                if (IntegerUtil.isNotEmpty(teacherVO.getUser_id()) && !ActionUtil.getUserID().equals
                        (teacherVO.getUser_id())) {//只发送给已注册的用户
                    count++;
                    map.put(teacherVO.getPhone(), teacherVO);//去重
                }
            }
            String[] user_ids = receivelist.split(",");
            for (int i = 0; i < user_ids.length; i++) {//遍历集合获取接收者信息
                if (StringUtil.isNotEmpty(user_ids[i].trim()) && !ActionUtil.getUserID().equals
                        (Integer.parseInt(user_ids[i].trim()))) {//只发送给已注册的用户
                    UserVO user=dao.queryObject("userMap.getUserByID",Integer.parseInt(user_ids[i].trim()));
                    if (map.containsKey(user.getPhone())) continue;
                    count++;
                    map.put(user.getPhone(), user);//去重
                }
            }
        }
        vo.setTotal_count(count);
        dao.updateObject("noticeMap.addNoticeCount",vo);
        return count;
    }

    //向未读用户发送提醒
    public void sendRemind(NoticeVO notice) {
        List<NoticeReceiveVO> list=getUserListOfUnread(notice.getNotice_id());
        List<ReceiveVO> receiveList=new ArrayList<ReceiveVO>();
        for (NoticeReceiveVO vo:list) {
            ReceiveVO receive=new ReceiveVO();
            if (IntegerUtil.isNotEmpty(vo.getStudent_id())) {
                receive.setSchool_id(ActionUtil.getSchoolID());
                receive.setUser_type(DictConstants.USERTYPE_STUDENT);
                receive.setStudent_id(vo.getStudent_id());
            } else {
                receive.setSchool_id(ActionUtil.getSchoolID());
                receive.setUser_type(DictConstants.USERTYPE_TEACHER);
                receive.setUser_id(vo.getUser_id());
            }
            receiveList.add(receive);
        }
        HashMap<String,String> dataMap = new HashMap<String,String>();
        dataMap.put("info_title","您有一条通知未查看");
        dataMap.put("module_code",notice.getModule_code());
        dataMap.put("module_pkid",notice.getNotice_id().toString());
        dataMap.put("link_type", DictConstants.LINK_TYPE_DETAIL);
        dataMap.put("info_url", "detail.html");
        dataMap.put("info_date", ActionUtil.getSysTime().getTime()+"");
        dataMap.put("user_type", DictConstants.MODULE_CODE_NOTICE.equals(notice.getModule_code())?DictConstants.USERTYPE_ALL:DictConstants.USERTYPE_TEACHER);
        getuiService.pushMessage(dataMap,receiveList);
        String key=RedisKeyUtil.getNoticeRemindKey(notice.getModule_code(),notice.getNotice_id(),ActionUtil.getSchoolID());
        jedisDAO.set(key,notice.getNotice_id().toString());
        jedisDAO.expire(key,86400);//24小时
    }

    public List<NoticeReceiveVO> getUnreadUserList(Integer notice_id) {
        List<NoticeGroupVO> list = dao.queryForList("noticeGroupMap.getListByNoticeID", notice_id);
        List<NoticeReceiveVO> nlist = new ArrayList<NoticeReceiveVO>();
        HashMap<Integer, Object> map = new HashMap<Integer, Object>();//过滤用
        for (NoticeGroupVO vo : list) {
            if (DictConstants.USERTYPE_STUDENT.equals(vo.getUser_type())) {
				List<StudentVO> slist = getStudentList(vo);//该group下的所有学生
                for (StudentVO stuvo : slist) {
                    String unionKey = RedisKeyUtil.getNoticeUnionKey(vo.getModule_code(), vo.getUser_type(), ActionUtil.getSchoolID(), null, stuvo.getStudent_id()) + ":NOTICE_ID" + notice_id;
                    boolean b = redisService.is_readFromRedis(unionKey);
                    if (!b) {//未读
                        if (map.containsKey(stuvo.getStudent_id())) continue;//例同时选普通班级和兴趣班
                        NoticeReadVO read = new NoticeReadVO();
                        read.setNotice_id(notice_id);
                        read.setUser_type(DictConstants.USERTYPE_STUDENT);
                        read.setStudent_id(stuvo.getStudent_id());
                        NoticeReadVO readVO = dao.queryObject("noticeReadMap.getNoticeRead", read);
                        if (readVO == null) {//有redis清空的情况，得判断数据库中是否有已读记录
                            NoticeReceiveVO nrvo = new NoticeReceiveVO();
                            nrvo.setReceive_name(redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_STUDENT, 0, stuvo.getStudent_id()));
                            nrvo.setStudent_id(stuvo.getStudent_id());
                            nrvo.setUser_id(0);
                            nlist.add(nrvo);
                            if (nlist.size()==100) return nlist;
                            map.put(stuvo.getStudent_id(), nrvo.getReceive_name());
                        }
                    }
                }
            } else {
                if (IntegerUtil.isEmpty(vo.getUser_id())) {//校务通知里的全部教师情况
                    ReceiveVO rvo = new ReceiveVO();
                    rvo.setSchool_id(ActionUtil.getSchoolID());
                    rvo.setGroup_id(vo.getGroup_id());
                    rvo.setTeam_id(vo.getTeam_id());
                    List<Integer> slist = userService.getTeacherUserID(rvo);
                    for (Integer user_id : slist) {
                        if (IntegerUtil.isEmpty(user_id) || user_id.equals(vo.getCreate_by())) continue;
                        String unionKey = RedisKeyUtil.getNoticeUnionKey(vo.getModule_code(), vo.getUser_type(), ActionUtil.getSchoolID(), user_id, null) + ":NOTICE_ID" + notice_id;
                        boolean b = redisService.is_readFromRedis(unionKey);
                        if (!b) {
                            NoticeReadVO read = new NoticeReadVO();
                            read.setNotice_id(notice_id);
                            read.setUser_type(DictConstants.USERTYPE_TEACHER);
                            read.setUser_id(user_id);
                            NoticeReadVO readVO = dao.queryObject("noticeReadMap.getNoticeRead", read);
                            if (readVO == null) {//有redis清空的情况，得判断数据库中是否有已读记录
                                NoticeReceiveVO nrvo = new NoticeReceiveVO();
                                nrvo.setReceive_name(redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER,
                                        user_id, 0));
                                nrvo.setStudent_id(0);
                                nrvo.setUser_id(user_id);
                                UserVO user=dao.queryObject("userMap.getUserByID",user_id);
                                nrvo.setDuty_list(BeanUtil.ListTojson(redisService.getTeacherDuty(user.getPhone()),false));
                                nlist.add(nrvo);
                                if (nlist.size()==100) return nlist;
                            }
                        }
                    }
                } else {
                    if (vo.getUser_id().equals(vo.getCreate_by())) continue;
                    String unionKey = RedisKeyUtil.getNoticeUnionKey(vo.getModule_code(), vo.getUser_type(), ActionUtil.getSchoolID(), vo.getUser_id(), null) + ":NOTICE_ID" + notice_id;
                    boolean b = redisService.is_readFromRedis(unionKey);
                    if (!b) {
                        NoticeReadVO read = new NoticeReadVO();
                        read.setNotice_id(notice_id);
                        read.setUser_type(DictConstants.USERTYPE_TEACHER);
                        read.setUser_id(vo.getUser_id());
                        NoticeReadVO readVO = dao.queryObject("noticeReadMap.getNoticeRead", read);
                        if (readVO == null) {//有redis清空的情况，得判断数据库中是否有已读记录
                            NoticeReceiveVO nrvo = new NoticeReceiveVO();
                            nrvo.setReceive_name(redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER,
                                    vo.getUser_id(), 0));
                            nrvo.setStudent_id(0);
                            nrvo.setUser_id(vo.getUser_id());
                            UserVO user=dao.queryObject("userMap.getUserByID",vo.getUser_id());
                            nrvo.setDuty_list(BeanUtil.ListTojson(redisService.getTeacherDuty(user.getPhone()),false));
                            nlist.add(nrvo);
                            if (nlist.size()==100) return nlist;
                        }
                    }
                }
            }
        }
        return nlist;
    }

	//确认通知
	public void confirmNotice(Integer notice_id){
		NoticeReadVO vo=new NoticeReadVO();
		vo.setNotice_id(notice_id);
		if (DictConstants.USERTYPE_TEACHER.equals(ActionUtil.getUserType())) {
			vo.setUser_id(ActionUtil.getUserID());
			vo.setUser_type(ActionUtil.getUserType());
		} else {
			vo.setStudent_id(ActionUtil.getStudentID());
			vo.setUser_type(DictConstants.USERTYPE_STUDENT);
		}
		NoticeReadVO readVO=dao.queryObject("noticeReadMap.getNoticeRead",vo);
		if (readVO.getIs_confirm()==1) throw new BusinessException(MsgService.getMsg("NOTICE_CONFIRMED"));
		dao.updateObject("noticeReadMap.confirmNotice",vo);
	}

	//获取已读列表，含回复
	public List<NoticeReplyVO> getReadList(Integer notice_id){
        //回复列表
		List<NoticeReplyVO> list=dao.queryForList("noticeReplyMap.getNoticeReplyListByNoticeid", notice_id);
		for (NoticeReplyVO nrvo:list){
			String key="NOTICE:SCHOOL_ID"+ActionUtil.getSchoolID()+":MODULE_CODE"+DictConstants.MODULE_CODE_NOTICE+
					":USER_TYPE003005:USER_ID"+ActionUtil.getUserID()+":NOTICE_ID"+notice_id;
			if (jedisDAO.hget(key,nrvo.getReceive_id().toString())==null)
				nrvo.setCount(0);
			else
				nrvo.setCount(Integer.parseInt(jedisDAO.hget(key,nrvo.getReceive_id().toString()).trim()));
			nrvo.setReceive_name(redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_STUDENT,0, nrvo.getReceive_id()));
			if (DictConstants.USERTYPE_TEACHER.equals(nrvo.getUser_type())) {
				nrvo.setReply_name(redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_TEACHER, nrvo.getUser_id(),0));
			} else {
				UserVO uvo = userService.getUserByID(nrvo.getUser_id());
				nrvo.setReply_name(uvo.getUser_name());
			}
			NoticeReadVO readVO=new NoticeReadVO();
			readVO.setNotice_id(notice_id);
			readVO.setUser_type(nrvo.getReceive_type());
			readVO.setStudent_id(nrvo.getReceive_id());
			NoticeReadVO noticeReadVO=dao.queryObject("noticeReadMap.getNoticeRead",readVO);
			if (noticeReadVO.getIs_confirm()==1) nrvo.setHave_confirm(true);//判断是否已确认
			else nrvo.setHave_confirm(false);
		}
		//已读未回复列表
		List<NoticeReadVO> readList=dao.queryForList("noticeReadMap.getReadListNoReply",notice_id);
		for (NoticeReadVO read:readList) {
			NoticeReplyVO reply=new NoticeReplyVO();
			reply.setReceive_name(redisService.getUserName(ActionUtil.getSchoolID(), DictConstants.USERTYPE_STUDENT,0, read.getStudent_id()));
			reply.setNotice_id(read.getNotice_id());
			reply.setStudent_id(read.getStudent_id());
			if (read.getIs_confirm()==1) reply.setHave_confirm(true);
			else reply.setHave_confirm(false);
			list.add(reply);
		}
		return list;
	}
}