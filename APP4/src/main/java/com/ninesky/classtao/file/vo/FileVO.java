package com.ninesky.classtao.file.vo;

import com.ninesky.common.util.IntegerUtil;
import com.ninesky.common.util.StringUtil;
import com.ninesky.common.vo.BaseVO;
import com.ninesky.framework.BusinessException;
import com.ninesky.framework.MsgService;

/**
 * Created by TOOTU on 2017/8/15.
 */
public class FileVO extends BaseVO{
	private Integer file_id;
	private Integer parent_id;
	private String module_code;
	private String file_type;
	private String file_name;
	private String file_url;
	private String file_resize_url;
	private Integer play_time;
	private Integer school_id;
	private String file_size;
	private String file_real_name;
    private String store_name;
	public FileVO() {}

	public FileVO(Integer file_id) {
		if (IntegerUtil.isEmpty(file_id))
			throw new BusinessException(MsgService.getMsg("UNEXPECTED_EXCEPTION"));
		this.file_id = file_id;
	}

	public FileVO(Integer school_id,String module_code) {
		if (StringUtil.isEmpty(module_code) || IntegerUtil.isEmpty(school_id))
			throw new BusinessException(MsgService.getMsg("UNEXPECTED_EXCEPTION"));
		this.school_id = school_id;
		this.module_code = module_code;
	}

	public FileVO(Integer school_id,String module_code,Integer parent_id) {
		if (StringUtil.isEmpty(module_code) || IntegerUtil.isEmpty(school_id) || IntegerUtil.isEmpty(parent_id))
			throw new BusinessException(MsgService.getMsg("UNEXPECTED_EXCEPTION"));
		this.school_id = school_id;
		this.module_code = module_code;
		this.parent_id = parent_id;
	}

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getFile_real_name() {
		return file_real_name;
	}

	public void setFile_real_name(String file_real_name) {
		this.file_real_name = file_real_name;
	}

	public String getFile_size() {
		return file_size;
	}

	public void setFile_size(String file_size) {
		this.file_size = file_size;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public Integer getPlay_time() {
		return play_time;
	}

	public void setPlay_time(Integer play_time) {
		this.play_time = play_time;
	}

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

	public String getModule_code() {
		return module_code;
	}

	public void setModule_code(String module_code) {
		this.module_code = module_code;
	}

	public Integer getParent_id() {
		return parent_id;
	}

	public void setParent_id(Integer parent_id) {
		this.parent_id = parent_id;
	}

	public Integer getFile_id() {
		return file_id;
	}

	public void setFile_id(Integer file_id) {
		this.file_id = file_id;
	}
}
