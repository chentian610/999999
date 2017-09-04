package com.ninesky.classtao.microPortal.service;


import java.util.Map;

/**
 * Created by TOOTU on 2017/7/27.
 */
public interface MicroPortalService {
    /**
     * 查询学校微门户
     * @param paramMap
     * @return
     */
    public Map<String, String> getMicroPortal(Map<String, String> paramMap);

    /**
     * 添加学校微门户信息
     * @param paramMap
     */
    public void addMicroPortal(Map<String, String> paramMap);

    /**
     * 删除学校微门户信息
     * @param paramMap
     */
    public void deleteMicroPortal(Map<String, String> paramMap);
}
