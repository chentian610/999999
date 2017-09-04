package com.ninesky.classtao.enroll.vo;


import com.ninesky.common.vo.BaseVO;

public class StudentEnrollVO extends BaseVO{
    private Integer id;
    private Integer recruit_id;
    /**
     * 学校ID
     */
    private Integer school_id;

    /**
     * 学生姓名
     */
    private String student_name;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 身份证号
     */
    private String id_number;

    /**
     * 头像照片
     */
    private String head_url;

    /**
     * 就读初中学校
     */
    private String middle_school;

    /**
     * 报考学校
     */
    private String register_school;

    /**
     * 是否色盲
     */
    private Integer color_blindness;

    /**
     * 个人特长
     */
    private String person_specialty;

    /**
     * 获奖情况
     */
    private String award_situation;

    /**
     * 家长姓名
     */
    private String parent_name;

    /**
     * 与孩子的关系
     */
    private String relationship;

    /**
     * 家长单位
     */
    private String parent_company;

    /**
     * 家长手机号码
     */
    private String phone;

    /**
     * 是否住宿
     */
    private Integer is_accommodate;

    /**
     * 报名状态，是否已录取
     */
    private String enroll_status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRecruit_id() {
        return recruit_id;
    }

    public void setRecruit_id(Integer recruit_id) {
        this.recruit_id = recruit_id;
    }

    public Integer getSchool_id() {
        return school_id;
    }

    public void setSchool_id(Integer school_id) {
        this.school_id = school_id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public String getHead_url() {
        return head_url;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }

    public String getMiddle_school() {
        return middle_school;
    }

    public void setMiddle_school(String middle_school) {
        this.middle_school = middle_school;
    }

    public String getRegister_school() {
        return register_school;
    }

    public void setRegister_school(String register_school) {
        this.register_school = register_school;
    }

    public Integer getColor_blindness() {
        return color_blindness;
    }

    public void setColor_blindness(Integer color_blindness) {
        this.color_blindness = color_blindness;
    }

    public String getPerson_specialty() {
        return person_specialty;
    }

    public void setPerson_specialty(String person_specialty) {
        this.person_specialty = person_specialty;
    }

    public String getAward_situation() {
        return award_situation;
    }

    public void setAward_situation(String award_situation) {
        this.award_situation = award_situation;
    }

    public String getParent_name() {
        return parent_name;
    }

    public void setParent_name(String parent_name) {
        this.parent_name = parent_name;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getParent_company() {
        return parent_company;
    }

    public void setParent_company(String parent_company) {
        this.parent_company = parent_company;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getIs_accommodate() {
        return is_accommodate;
    }

    public void setIs_accommodate(Integer is_accommodate) {
        this.is_accommodate = is_accommodate;
    }

    public String getEnroll_status() {
        return enroll_status;
    }

    public void setEnroll_status(String enroll_status) {
        this.enroll_status = enroll_status;
    }
}
