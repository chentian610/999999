package com.ninesky.classtao.menu.vo;

import com.ninesky.common.vo.BaseVO;

/**
 * Created by TOOTU on 2017/7/12.
 */
public class MenuModuleVO extends BaseVO {
    private Integer id;
    private Integer school_id;
    private Integer menu_id;
    private String module_code;
    private String user_type;
    private Integer is_active;
    private Integer is_default;

    public Integer getSchool_id() {
        return school_id;
    }

    public void setSchool_id(Integer school_id) {
        this.school_id = school_id;
    }

    public Integer getIs_default() {
        return is_default;
    }

    public void setIs_default(Integer is_default) {
        this.is_default = is_default;
    }

    public Integer getIs_active() {
        return is_active;
    }

    public void setIs_active(Integer is_active) {
        this.is_active = is_active;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getModule_code() {
        return module_code;
    }

    public void setModule_code(String module_code) {
        this.module_code = module_code;
    }

    public Integer getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(Integer menu_id) {
        this.menu_id = menu_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MenuModuleVO() {}

    public MenuModuleVO(Integer is_default) {
        this.is_default = is_default;
    }

    public MenuModuleVO(String module_code,String user_type) {
        this.module_code = module_code;
        this.user_type = user_type;
    }

    public MenuModuleVO(Integer school_id,Integer is_active) {
        this.school_id = school_id;
        this.is_active = is_active;
    }
}
