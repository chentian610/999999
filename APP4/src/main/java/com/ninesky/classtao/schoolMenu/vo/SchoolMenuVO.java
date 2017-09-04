package com.ninesky.classtao.schoolMenu.vo;

import com.ninesky.common.vo.BaseVO;

public class SchoolMenuVO extends BaseVO {
    private Integer school_menu_id;
    private Integer school_id;
    private String menu_date;
    private String menu_name;
    private String file_list;
    private String monday_data;

    public String getMonday_data() {
        return monday_data;
    }

    public void setMonday_data(String monday_data) {
        this.monday_data = monday_data;
    }

    public String getFile_list() {
        return file_list;
    }

    public void setFile_list(String file_list) {
        this.file_list = file_list;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public String getMenu_date() {
        return menu_date;
    }

    public void setMenu_date(String menu_date) {
        this.menu_date = menu_date;
    }

    public Integer getSchool_id() {
        return school_id;
    }

    public void setSchool_id(Integer school_id) {
        this.school_id = school_id;
    }

    public Integer getSchool_menu_id() {
        return school_menu_id;
    }

    public void setSchool_menu_id(Integer school_menu_id) {
        this.school_menu_id = school_menu_id;
    }
}