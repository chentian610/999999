package com.ninesky.classtao.file.service;

import com.ninesky.classtao.file.vo.FileVO;

import java.util.List;

/**
 * Created by TOOTU on 2017/8/15.
 */
public interface FileService {

    /**
     * 插入一份文件
     * @param vo
     */
    public void addFile(FileVO vo);

    /**
     * 插入文件集合
     * @param fileList
     * @param parent_id
     * @param module_code
     */
    public void addFileList(String fileList,Integer parent_id,String module_code);

    /**
     * 获取文件集合
     * @param vo
     * @return
     */
    public List<FileVO> getFileList(FileVO vo);

    /**
     * 删除文件
     * @param vo
     */
    public void deleteFile(FileVO vo);

    /**
     * 根据ID删除文件
     * @param file_id
     */
    public void deleteFileByID(Integer file_id);
}
