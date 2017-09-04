package com.ninesky.classtao.school.vo;

import com.ninesky.common.vo.BaseVO;

/**
 * Created by TOOTU on 2017/4/13.
 */
public class SchoolConfigVO extends BaseVO {
    private Integer school_config_id;
    private Integer school_id;
    private Integer memory_id;
    private Integer memory;
    private Integer memory_price;
    private Integer disk_id;
    private Integer disk;
    private Integer disk_price;
    private Integer bandwidth_id;
    private Integer bandwidth;
    private Integer bandwidth_price;

    public void setBandwidth_price(Integer bandwidth_price) {
        this.bandwidth_price = bandwidth_price;
    }

    public void setBandwidth(Integer bandwidth) { this.bandwidth = bandwidth;}

    public Integer getBandwidth_price() {
        return bandwidth_price;
    }

    public Integer getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth_id(Integer bandwidth_id) {
        this.bandwidth_id = bandwidth_id;
    }

    public Integer getBandwidth_id() {
        return bandwidth_id;
    }

    public void setDisk_price(Integer disk_price) {
        this.disk_price = disk_price;
    }

    public Integer getDisk_price() {
        return disk_price;
    }

    public void setDisk(Integer disk) {
        this.disk = disk;
    }

    public Integer getDisk() {
        return disk;
    }

    public void setDisk_id(Integer disk_id) {
        this.disk_id = disk_id;
    }

    public Integer getDisk_id() {
        return disk_id;
    }

    public void setMemory_price(Integer memory_price) {
        this.memory_price = memory_price;
    }

    public Integer getMemory_price() {
        return memory_price;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
    }

    public Integer getMemory() {
        return memory;
    }

    public void setMemory_id(Integer memory_id) {
        this.memory_id = memory_id;
    }

    public Integer getMemory_id() {
        return memory_id;
    }

    public void setSchool_id(Integer school_id) {
        this.school_id = school_id;
    }

    public Integer getSchool_id() {
        return school_id;
    }

    public void setSchool_config_id(Integer school_config_id) {
        this.school_config_id = school_config_id;
    }

    public Integer getSchool_config_id() {
        return school_config_id;
    }
}
