package com.ninesky.classtao.microPortal.service.impl;

import com.ninesky.classtao.microPortal.service.MicroPortalService;
import com.ninesky.classtao.microPortal.vo.MicroPortalFileVO;
import com.ninesky.classtao.microPortal.vo.MicroPortalInfoVO;
import com.ninesky.classtao.microPortal.vo.MicroPortalVO;
import com.ninesky.classtao.redis.service.RedisService;
import com.ninesky.common.DictConstants;
import com.ninesky.common.util.*;
import com.ninesky.framework.BusinessException;
import com.ninesky.framework.GeneralDAO;
import com.ninesky.framework.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by TOOTU on 2017/7/27.
 */
@Service(value = "MicroPortalServiceImpl")
public class MicroPortalServiceImpl implements MicroPortalService{
    @Autowired
    private GeneralDAO dao;

    @Autowired
    private RedisService redisService;

    @Override
    public Map<String, String> getMicroPortal(Map<String, String> paramMap) {
        if (IntegerUtil.isEmpty(ActionUtil.getSchoolID())) throw new BusinessException(MsgService.getMsg("UNEXPECTED_EXCEPTION"));
        paramMap.put("school_name",redisService.getSchoolName(ActionUtil.getSchoolID()));
        paramMap.put("file_list",getFileList());
        paramMap.put("school_list",getInfoList());
        paramMap.put("column_list",getColumnList());
        return paramMap;
    }

    private String getFileList(){
        List<MicroPortalFileVO> list = dao.queryForList("microPortalFileMap.getMicroPortalFileByID", ActionUtil.getSchoolID());
        return BeanUtil.ListTojson(list,false);
    }

    private String getInfoList(){
        List<MicroPortalInfoVO> list = dao.queryForList("microPortalInfoMap.getMicroPortalInfoByID", ActionUtil.getSchoolID());
        return BeanUtil.ListTojson(list,false);
    }

    private String getColumnList(){
        List<MicroPortalVO> list = dao.queryForList("microPortalMap.getMicroPortalByID", ActionUtil.getSchoolID());
        return BeanUtil.ListTojson(list,false);
    }

    @Override
    public void addMicroPortal(Map<String, String> paramMap) {
        deleteMicroPortal(paramMap);
        addFileList(paramMap);
        addInfoList(paramMap);
        addColumnList(paramMap);
    }

    private void addFileList(Map<String, String> paramMap) {
        if (StringUtil.isEmpty(paramMap.get("file_list"))) return;
        List<MicroPortalFileVO> list1 = new ArrayList<MicroPortalFileVO>();
        List<MicroPortalFileVO> list = BeanUtil.jsonToList(paramMap.get("file_list"),MicroPortalFileVO.class);
        for (MicroPortalFileVO vo:list) {
            if (IntegerUtil.isNotEmpty(vo.getFile_id())) continue;
            vo.setCreate_by(ActionUtil.getUserID());
            vo.setCreate_date(ActionUtil.getSysTime());
            list1.add(vo);
        }
        if (ListUtil.isEmpty(list1)) return;
        dao.insertObject("microPortalFileMap.insertMicroPortalFileBatch",list1);
    }

    private void addInfoList(Map<String, String> paramMap) {
        if (StringUtil.isEmpty(paramMap.get("school_list"))) return;
        List<MicroPortalInfoVO> list1 = new ArrayList<MicroPortalInfoVO>();
        List<MicroPortalInfoVO> list = BeanUtil.jsonToList(paramMap.get("school_list"),MicroPortalInfoVO.class);
        for (MicroPortalInfoVO vo:list) {
            if (IntegerUtil.isNotEmpty(vo.getInfo_id())) {
                vo.setUpdate_by(ActionUtil.getUserID());
                vo.setUpdate_date(ActionUtil.getSysTime());
                dao.updateObject("microPortalInfoMap.updateMicroPortalInfoByID",vo);
                continue;
            }
            vo.setCreate_by(ActionUtil.getUserID());
            vo.setCreate_date(ActionUtil.getSysTime());
            list1.add(vo);
        }
        if (ListUtil.isEmpty(list1)) return;
        dao.insertObject("microPortalInfoMap.insertMicroPortalInfoBatch",list1);
    }

    private void addColumnList(Map<String, String> paramMap) {
        if (StringUtil.isEmpty(paramMap.get("column_list"))) return;
        List<MicroPortalVO> list1 = new ArrayList<MicroPortalVO>();
        List<MicroPortalVO> list = BeanUtil.jsonToList(paramMap.get("column_list"),MicroPortalVO.class);
        for (MicroPortalVO vo:list) {
            if (IntegerUtil.isNotEmpty(vo.getColumn_id())) {
                vo.setUpdate_by(ActionUtil.getUserID());
                vo.setUpdate_date(ActionUtil.getSysTime());
                dao.updateObject("microPortalMap.updateMicroPortalByID",vo);
                continue;
            }
            vo.setCreate_by(ActionUtil.getUserID());
            vo.setCreate_date(ActionUtil.getSysTime());
            list1.add(vo);
        }
        if (ListUtil.isEmpty(list1)) return;
        dao.insertObject("microPortalMap.insertMicroPortalBatch",list1);
    }

    @Override
    public void deleteMicroPortal(Map<String, String> paramMap) {
        if (DictConstants.TRUE == IntegerUtil.getValue(paramMap.get("is_empty"))) {
            dao.deleteObject("microPortalFileMap.deleteMicroPortalFile",ActionUtil.getSchoolID());
            dao.deleteObject("microPortalInfoMap.deleteMicroPortalInfo",ActionUtil.getSchoolID());
            dao.deleteObject("microPortalMap.deleteMicroPortal",ActionUtil.getSchoolID());
        } else {
            deleteFile(paramMap);
            deleteInfo(paramMap);
            deleteColumn(paramMap);
        }
    }

    private void deleteFile(Map<String, String> paramMap) {
        if (StringUtil.isEmpty(paramMap.get("file_ids"))) return;
        String[] file_ids = paramMap.get("file_ids").split(",");
        for (String file_id:file_ids) {
            if (StringUtil.isEmpty(file_id)) continue;
            dao.deleteObject("microPortalFileMap.deleteMicroPortalFileByID",IntegerUtil.getValue(file_id));
        }
    }

    private void deleteInfo(Map<String, String> paramMap) {
        if (StringUtil.isEmpty(paramMap.get("info_ids"))) return;
        String[] info_ids = paramMap.get("info_ids").split(",");
        for (String info_id:info_ids) {
            if (StringUtil.isEmpty(info_id)) continue;
            dao.deleteObject("microPortalInfoMap.deleteMicroPortalInfoByID",IntegerUtil.getValue(info_id));
        }
    }

    private void deleteColumn(Map<String, String> paramMap) {
        if (StringUtil.isEmpty(paramMap.get("column_ids"))) return;
        String[] column_ids = paramMap.get("column_ids").split(",");
        for (String column_id:column_ids) {
            if (StringUtil.isEmpty(column_id)) continue;
            dao.deleteObject("microPortalMap.deleteMicroPortalByID",IntegerUtil.getValue(column_id));
        }
    }
}
