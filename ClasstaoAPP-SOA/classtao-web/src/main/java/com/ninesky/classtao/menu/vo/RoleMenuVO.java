package com.ninesky.classtao.menu.vo;

import com.ninesky.common.DictConstants;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.common.util.IntegerUtil;
import com.ninesky.common.util.StringUtil;
import com.ninesky.common.vo.BaseVO;
import com.ninesky.framework.BusinessException;
import com.ninesky.framework.MsgService;

import java.util.Date;

/**
 * Created by TOOTU on 2017/7/12.
 */
public class RoleMenuVO extends BaseVO {
    private Integer id;
    private Integer school_id;
    private String role_code;
    private Integer menu_id;
    private Integer is_active;
    /**
     * 创建者
     */
    private Integer create_by;

    /**
     * 创建日期
     */
    private Date create_date;

    /**
     * 更新者
     */
    private Integer update_by;

    /**
     * 更新日期
     */
    private Date update_date;

    private String module_code;

    private String user_type;

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

    public RoleMenuVO () {}

    public RoleMenuVO (Integer id) {
        if (IntegerUtil.isEmpty(id))
            throw new BusinessException(MsgService.getMsg("UNEXPECTED_EXCEPTION"));
        this.id = id;
    }

    public RoleMenuVO (Integer school_id,String role_code) {
        if (IntegerUtil.isEmpty(school_id) || StringUtil.isEmpty(role_code))
            throw new BusinessException(MsgService.getMsg("UNEXPECTED_EXCEPTION"));
        this.school_id = school_id;
        this.role_code = role_code;
    }

    @Override
    public Date getUpdate_date() {
        return update_date;
    }

    @Override
    public void setUpdate_date(Date update_date) {
        this.update_date = update_date;
    }

    @Override
    public Integer getUpdate_by() {
        return update_by;
    }

    @Override
    public void setUpdate_by(Integer update_by) {
        this.update_by = update_by;
    }

    @Override
    public Date getCreate_date() {
        return create_date;
    }

    @Override
    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    @Override
    public Integer getCreate_by() {
        return create_by;
    }

    @Override
    public void setCreate_by(Integer create_by) {
        this.create_by = create_by;
    }

    public Integer getIs_active() {
        return is_active;
    }

    public void setIs_active(Integer is_active) {
        this.is_active = is_active;
    }

    public Integer getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(Integer menu_id) {
        this.menu_id = menu_id;
    }

    public String getRole_code() {
        return role_code;
    }

    public void setRole_code(String role_code) {
        this.role_code = role_code;
    }

    public Integer getSchool_id() {
        return school_id;
    }

    public void setSchool_id(Integer school_id) {
        this.school_id = school_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RoleMenuVO(Integer school_id, String role_code, Integer menu_id){
        this.school_id = school_id;
        this.role_code = role_code;
        this.menu_id = menu_id;
        this.is_active = DictConstants.TRUE;
        this.create_by = ActionUtil.getUserID();
        this.create_date = ActionUtil.getSysTime();
    }

    public RoleMenuVO (Integer school_id,String module_code,String user_type) {
        this.school_id = school_id;
        this.module_code = module_code;
        this.user_type = user_type;
    }
}
