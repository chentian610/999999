package com.ninesky.classtao.microPortal.vo;

import com.ninesky.common.vo.BaseVO;

/**
 * Created by TOOTU on 2017/7/27.
 */
public class MicroPortalFileVO extends BaseVO {
    private Integer file_id;//主键ID
    private Integer school_id;//学校ID
    private String file_type;//文件类型
    private String file_name;//文件名称
    private String file_url;//文件原路径
    private String file_resize_url;//压缩文件路径

    public String getFile_resize_url() {
        return file_resize_url;
    }

    public void setFile_resize_url(String file_resize_url) {
        this.file_resize_url = file_resize_url;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public Integer getSchool_id() {
        return school_id;
    }

    public void setSchool_id(Integer school_id) {
        this.school_id = school_id;
    }

    public Integer getFile_id() {
        return file_id;
    }

    public void setFile_id(Integer file_id) {
        this.file_id = file_id;
    }
}
