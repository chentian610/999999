package com.ninesky.classtao.file.service.impl;

import com.ninesky.classtao.file.service.FileService;
import com.ninesky.classtao.file.vo.FileVO;
import com.ninesky.classtao.redis.service.RedisService;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.common.util.IntegerUtil;
import com.ninesky.common.util.StringUtil;
import com.ninesky.framework.BusinessException;
import com.ninesky.framework.GeneralDAO;
import com.ninesky.framework.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by TOOTU on 2017/8/15.
 */
@Service("fileServiceImpl")
public class FileServiceImpl implements FileService {

    @Autowired
    private GeneralDAO dao;

    @Autowired
    private RedisService redisService;

    @Override
    public void addFile(FileVO vo) {
        if (null == vo) throw new BusinessException(MsgService.getMsg("UNEXPECTED_EXCEPTION"));
        vo.setSchool_id(ActionUtil.getSchoolID());
        vo.setCreate_by(ActionUtil.getUserID());
        vo.setCreate_date(ActionUtil.getSysTime());
        vo.setFile_id(dao.insertObjectReturnID("fileMap.insertFile",vo));
    }

    @Override
    public void addFileList(String fileList,Integer parent_id,String module_code) {
        if (StringUtil.isEmpty(fileList) || IntegerUtil.isEmpty(parent_id) || StringUtil.isEmpty(module_code))
            throw new BusinessException(MsgService.getMsg("UNEXPECTED_EXCEPTION"));
        List<FileVO> list = BeanUtil.jsonToList(fileList,FileVO.class);
        for (FileVO vo : list) {
            vo.setSchool_id(ActionUtil.getSchoolID());
            vo.setParent_id(parent_id);
            vo.setModule_code(module_code);
            vo.setCreate_by(ActionUtil.getUserID());
            vo.setCreate_date(ActionUtil.getSysTime());
        }
        dao.insertObject("fileMap.insertFileBatch",list);
    }

    @Override
    public List<FileVO> getFileList(FileVO vo) {return dao.queryForList("fileMap.getFileList",vo);}

    @Override
    public void deleteFileByID(Integer file_id) {
        dao.deleteObject("fileMap.deleteFileByID",file_id);
    }

    @Override
    public void deleteFile(FileVO vo) {
        dao.deleteObject("fileMap.deleteFile",vo);
    }
}
