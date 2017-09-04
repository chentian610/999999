package com.ninesky.classtao.microPortal.vo;

import com.ninesky.common.vo.BaseVO;

/**
 * Created by TOOTU on 2017/7/27.
 */
public class MicroPortalVO extends BaseVO {
    private Integer column_id;//主键ID
    private Integer school_id;//学校ID
    private String column_name;//栏目名称
    private String column_content;//栏目内容

    public String getColumn_content() {
        return column_content;
    }

    public void setColumn_content(String column_content) {
        this.column_content = column_content;
    }

    public String getColumn_name() {
        return column_name;
    }

    public void setColumn_name(String column_name) {
        this.column_name = column_name;
    }

    public Integer getSchool_id() {
        return school_id;
    }

    public void setSchool_id(Integer school_id) {
        this.school_id = school_id;
    }

    public Integer getColumn_id() {
        return column_id;
    }

    public void setColumn_id(Integer column_id) {
        this.column_id = column_id;
    }
}
