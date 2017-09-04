package com.ninesky.classtao.teacherDisk.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ninesky.classtao.user.service.UserService;
import com.ninesky.framework.BusinessException;
import com.ninesky.framework.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninesky.common.vo.ReceiveVO;
import com.ninesky.classtao.teacherDisk.service.CloudDiskService;
import com.ninesky.classtao.teacherDisk.vo.DiskReceiveVO;
import com.ninesky.classtao.teacherDisk.vo.DiskVO;
import com.ninesky.classtao.user.vo.StudentVO;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.framework.GeneralDAO;

@Service("CloudDiskServiceImpl")
public class CloudDiskServiceImpl implements CloudDiskService {

	@Autowired
	private GeneralDAO dao;

	@Autowired
	private UserService userService;
	
	/**
	 * 获取云盘列表
	 */
	public List<?> getCloudDiskList(DiskVO disk){
		if(DictConstants.USERTYPE_ADMIN.equals(ActionUtil.getUserType()) ||
				DictConstants.USERTYPE_TEACHER.equals(ActionUtil.getUserType())){//教师获取云盘列表
			return dao.queryForList("diskMap.getDiskListByUser", disk);
		} else {//家长获取教师发送云盘列表
			DiskReceiveVO rvo = new DiskReceiveVO();
			rvo.setSchool_id(ActionUtil.getSchoolID());
			StudentVO svo = userService.getStudentById(ActionUtil.getStudentID());
			rvo.setGroup_id(svo.getGrade_id());
			rvo.setTeam_id(svo.getClass_id());
			rvo.setUser_id(ActionUtil.getStudentID());
			rvo.setUser_type(DictConstants.USERTYPE_STUDENT);
			rvo.setApp_sql(ActionUtil.getParameter("app_sql"));
			rvo.setOrder_sql(ActionUtil.getParameter("order_sql"));
			List<DiskReceiveVO> receiveList = dao.queryForList("diskReceiveMap.getDiskListByStu", rvo);
			return receiveList;
		}
	}
	
	/**
	 * 获所有文件类型
	 */
	public List<DiskVO> getFileType(DiskVO vo){
		return dao.queryForList("diskMap.getFileType", vo);
	}
	
	/**
	 * 添加文件到云盘（或新建文件夹）
	 */
	public DiskVO addCloudDisk(DiskVO vo){
		vo.setUpdate_date(ActionUtil.getSysTime());
		vo.setSchool_id(ActionUtil.getSchoolID());
		vo.setUser_id(ActionUtil.getUserID());
		vo.setCreate_by(ActionUtil.getUserID());
		vo.setCreate_date(ActionUtil.getSysTime());
		vo.setFile_id(dao.insertObjectReturnID("diskMap.insertDisk", vo));
		return vo;
	}
	
	/**
	 * 修改云盘文件(移动到)
	 */
	public void updateCloudDisk(DiskVO vo){
		dao.updateObject("diskMap.updateCloudDisk", vo);
	}
	
	/**
	 * 修改云盘文件名
	 */
	public void updateCloudDiskName(DiskVO vo){
		dao.updateObject("diskMap.updateCloudDiskName", vo);
	}
	
	/**
	 * 删除文件
	 */
	public void deleteCloudDisk(List<DiskVO> list){
		for (DiskVO vo :list) {
			int count = dao.queryObject("diskMap.getCountByParentID", vo.getFile_id());
			if (count>0) throw new BusinessException(MsgService.getMsg("CANNOT_DELETE",vo.getFile_name()));
			dao.updateObject("diskMap.deleteCloudDisk", vo.getFile_id());
		}
	}
	
	/**
	 * 发送文件到班级
	 */
	public void sendCloudDisk(DiskVO vo,String receive_list){
		List<DiskReceiveVO> receiveList =new ArrayList<DiskReceiveVO>();
		List<ReceiveVO> list=BeanUtil.jsonToList(receive_list, ReceiveVO.class);
		for(ReceiveVO rvo :list){
			if (DictConstants.TEAM_TYPE_INTEREST.equals(rvo.getTeam_type())) {
				DiskReceiveVO dvo = new DiskReceiveVO();
				dvo.setFile_id(vo.getFile_id());
				dvo.setFile_name(vo.getFile_name());
				dvo.setFile_type(vo.getFile_type());
				dvo.setSchool_id(vo.getSchool_id());
				dvo.setTeam_type(rvo.getTeam_type());
				dvo.setGroup_id(0);
				dvo.setTeam_id(rvo.getTeam_id());
				dvo.setCreate_by(vo.getCreate_by());
				dvo.setCreate_date(vo.getCreate_date());
				receiveList.add(dvo);
			} else {
				DiskReceiveVO dvo = new DiskReceiveVO();
				dvo.setFile_id(vo.getFile_id());
				dvo.setFile_name(vo.getFile_name());
				dvo.setFile_type(vo.getFile_type());
				dvo.setSchool_id(vo.getSchool_id());
				dvo.setTeam_type(rvo.getTeam_type());
				dvo.setGroup_id(rvo.getGroup_id());
				dvo.setTeam_id(rvo.getTeam_id());
				dvo.setCreate_by(vo.getCreate_by());
				dvo.setCreate_date(vo.getCreate_date());
				receiveList.add(dvo);
			}
		}
		dao.insertObject("diskReceiveMap.insertDiskReceive", receiveList);
	}
}
