package com.ninesky.util;

import com.ninesky.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 数据库结构更新类
 * 
 * @author Chenth
 */
public class updateDBUtil {
	private static Logger logger = LoggerFactory.getLogger(updateDBUtil.class);
	private static String jdbc_url;
	private static String jdbc_driver;
	private static String jdbc_user;
	private static String jdbc_password;
	private static String jdbc_dbname;
	private static Properties configProps;


	public static void main(String[] args){
		SQLUpdate();
	}

	public static void SQLUpdate() {
		try {
			logger.error("更新数据库版本开始...." );
			DBUtil db = new DBUtil(connetctDB(),jdbc_dbname);
			StringBuffer sql = new StringBuffer();
			sql.append(db.addColumn("kt_sys_score_reason", "is_active", "int(1)"));
			sql.append(db.addColumn("kt_sys_attend_code", "is_active", "int(1)"));
			sql.append(db.addColumn("kt_bas_contact", "course", "varchar(9)"));
			sql.append(db.addColumn("kt_sys_dict_school", "is_active", "int(1)"));
			sql.append(db.addColumn("kt_sys_user","password_update_time","datetime"));
			sql.append(db.addColumn("kt_sys_menu", "is_active", "int(1)"));
			sql.append(db.addColumn("kt_sys_menu", "partner_code", "varchar(6)"));
			sql.append(db.addColumn("kt_sys_dict","is_configure","int(1)",false,null,"0","是否可配置 1:可配置，0:不可配置","other_field"));
			sql.append(db.addColumn("kt_sys_dict_school","update_date","datetime"));
			sql.append(db.addColumn("kt_bus_notice","readCount","int(6)",false,null,"0","通知已读人数","notice_content"));
			sql.append(db.addColumn("kt_bus_notice","replyCount","int(6)",false,null,"0","通知回复人数","notice_content"));
			sql.append(db.createTable("kt_bus_notice_read","CREATE TABLE kt_bus_notice_read (id bigint auto_increment NOT NULL, school_id int(11) not null, notice_id bigint(20) not null, user_type varchar(6) not null, user_id bigint(20), student_id bigint(20), create_by bigint(11), create_date datetime, PRIMARY KEY (id) )"));
			sql.append(db.createTable("kt_sys_error_log","CREATE TABLE kt_sys_error_log(id bigint(20) NOT NULL AUTO_INCREMENT, method varchar(80) DEFAULT NULL COMMENT '接口名称', parameter text COMMENT '参数', msg text COMMENT '错误详细信息', create_date datetime DEFAULT NULL COMMENT '创建时间', PRIMARY KEY (id))"));
			sql.append(db.dropTable("kt_sys_log"));
			sql.append(db.addColumn("kt_sys_score_reason","module_code","varchar(6)",true,null,"","模块编码（数据字典：009）","score_code"));//打分表添加模块字段，通过模块获取参数
			sql.append(db.addColumn("kt_bus_score","module_code","varchar(6)",true,null,"","模块编码（数据字典：009）","group_id"));//打分表添加模块字段，通过模块获取参数
			sql.append(db.addColumn("kt_sys_error_log","ip_address","varchar(15)",true,null,"","IP地址","msg"));//打分表添加模块字段，通过模块
			sql.append(db.addColumn("kt_bus_notice_group","team_type","varchar(6)",true,null,null,"团队类型 011","school_id"));
			sql.append(db.executeSql("insert into kt_sys_config (config_key,config_value,description) values ('DB_VERSION','0','数据库更新版本')",1));
			sql.append(db.executeSql("update kt_bus_notice_group set team_type='011015' where group_id=0 and team_id<>0",1));
            sql.append(db.executeSql("update kt_bus_notice_group set team_type='011005' where team_type is null",1));
            sql.append(db.addColumn("kt_bas_contact","is_active","int(1)",false,null,"1","1:启用状态，0：禁用状态","course"));
			sql.append(db.addColumn("kt_bus_photo","team_type","VARCHAR (6)",false,null,"011005","团队类型011","photo_type"));
			sql.append(db.addColumn("kt_bus_photo_receive","team_type","VARCHAR (6)",false,null,"011005","团队类型011","user_type"));
			sql.append(db.modifyColumnName("kt_bus_leave_change","id","change_id","int(11)",1));
			sql.append(db.modifyColumnName("kt_bas_area","areaID","area_id","int(11)",0));
			sql.append(db.modifyColumnName("kt_bas_area","area","area_name","varchar(255)",0));
			sql.append(db.modifyColumnName("kt_bas_area","father","parent_code","varchar(6)",0));
			sql.append(db.modifyColumnName("kt_bas_city","cityID","city_id","int(11)",0));
			sql.append(db.modifyColumnName("kt_bas_city","city","city_name","varchar(255)",0));
			sql.append(db.modifyColumnName("kt_bas_city","father","parent_code","varchar(6)",0));
			sql.append(db.modifyColumnName("kt_bas_province","provinceID","province_id","int(11)",0));
			sql.append(db.modifyColumnName("kt_bas_province","province","province_name","varchar(255)",0));
			sql.append(db.addColumn("kt_bas_agent","region_code","varchar (6)",true,null,"","区域code","valid_date"));
			sql.append(db.addColumn("kt_bas_agent","user_id","bigint (20)",true,null,"","用户ID","agent_id"));
			sql.append(db.addColumn("kt_bas_agent","unit_price","bigint(20)",true,null,"","学校单价","valid_date"));
			sql.append(db.addColumn("kt_bas_agent","is_enable","varchar(6)",true,null,"001005","是否启用0(启用)/1(禁用)","valid_date"));
			sql.append(db.addColumn("kt_sys_app","update_phone","varchar (255)",true,null,"","指定更新电话号码","update_url"));
			sql.append(db.addColumn("kt_sys_app","is_disable","int (1)",true,null,"0","是否具有(0(启用)/1(禁用))","is_all"));
			sql.append(db.addColumn("kt_bus_leave","leave_hours","bigint(20)",true,null,"0","请假小时","remarks"));
			sql.append(db.addColumn("kt_bus_leave","leave_days","bigint(20)",true,null,"0","请假天数","remarks"));
			sql.append(db.createTable("kt_bus_leave_file","CREATE TABLE kt_bus_leave_file (file_id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '附件ID', school_id int(11) not null COMMENT '学校ID', leave_id bigint(20) not null COMMENT '请假申请ID', file_type varchar(6) not null COMMENT '图片类型', file_url varchar(255) not null COMMENT '图片路径', file_name varchar(255) not null COMMENT '图片名称', create_by bigint(11), create_date datetime, update_by bigint(11), update_date datetime,version bigint(11), PRIMARY KEY (file_id) )"));
			sql.append(db.createTable("kt_bus_leave_cc","CREATE TABLE kt_bus_leave_cc (cc_id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '抄送ID', school_id int(11) not null COMMENT '学校ID', leave_id bigint(20) not null COMMENT '请假申请ID',user_id bigint(20) not null COMMENT '用户ID' , create_by bigint(11), create_date datetime, update_by bigint(11), update_date datetime,version bigint(11), PRIMARY KEY (cc_id) )"));
			sql.append(db.addColumn("kt_bus_leave_file","file_resize_url","VARCHAR (255)",true,null,null,"附件缩略图","file_url"));
			sql.append(db.addColumn("kt_bus_leave_file","file_resize_url","VARCHAR (255)",false,null,null,"附件缩略图","file_url"));
			sql.append(db.createTable("kt_bas_balance","CREATE TABLE kt_bas_balance (balance_id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '余额ID',user_id bigint(20) not null COMMENT '用户ID',balance double(20,2) not null COMMENT '余额' , create_by bigint(11), create_date datetime, update_by bigint(11), update_date datetime,version bigint(11), PRIMARY KEY (balance_id) )"));
			sql.append(db.createTable("kt_bas_balance_log","CREATE TABLE kt_bas_balance_log (log_id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志ID',user_id bigint(20) not null COMMENT '用户ID',original_balance double(20,2) not null COMMENT '原来余额',current_balance double(20,2) not null COMMENT '当前余额',consumption double(20,2) not null COMMENT '消费金额',content varchar(255) not null COMMENT '业务描述', create_by bigint(11), create_date datetime ,PRIMARY KEY (log_id))"));
			sql.append(db.addColumn("kt_sys_module_price","module_code","VARCHAR (6)",true,null,"","模块类型","price_id"));
			sql.append(db.addColumn("kt_sys_module_price","user_type","VARCHAR (6)",true,null,"","用户类型","module_code"));
			sql.append(db.dropColumn("kt_sys_module_price","module_id"));
			sql.append(db.createTable("kt_sys_module_price","CREATE TABLE kt_sys_module_price (price_id bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID主键',module_code varchar(6) not null COMMENT '模块类型',user_type varchar(6) not null COMMENT '用户类型',module_price bigint(20) not null COMMENT '模块价格',school_type varchar(6) not null COMMENT '学校类型',create_by bigint(11), create_date datetime, update_by bigint(11), update_date datetime,version bigint(11),PRIMARY KEY (price_id))"));
			sql.append(db.addColumn("kt_sys_module","introduce","VARCHAR (255)",true,null,"","模块介绍","module_name"));
			sql.append(db.createTable("kt_sys_server_config","CREATE TABLE kt_sys_server_config (server_config_id bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID主键',memory bigint(20) not null COMMENT '内存(单位G)',memory_price bigint(20) not null COMMENT '内存价格',disk bigint(20) not null COMMENT '硬盘(单位G)',disk_price bigint(20) not null COMMENT '硬盘价格',bandwidth bigint(20) not null COMMENT '带宽(单位mb/s)',bandwidth_price bigint(20) not null COMMENT '带宽价格',create_by bigint(11), create_date datetime, update_by bigint(11), update_date datetime,version bigint(11),PRIMARY KEY (server_config_id))"));
			sql.append(db.createTable("kt_bas_school_config","CREATE TABLE kt_bas_school_config (school_config_id bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID主键',school_id bigint(20) not null COMMENT '学校ID',memory_id bigint(20) not null COMMENT '内存ID',memory bigint(20) not null COMMENT '内存(单位G)',memory_price bigint(20) not null COMMENT '内存价格',disk_id bigint(20) not null COMMENT '硬盘ID',disk bigint(20) not null COMMENT '硬盘(单位G)',disk_price bigint(20) not null COMMENT '硬盘价格',bandwidth_id bigint(20) not null COMMENT '带宽ID',bandwidth bigint(20) not null COMMENT '带宽(单位mb/s)',bandwidth_price bigint(20) not null COMMENT '带宽价格',create_by bigint(11), create_date datetime, update_by bigint(11), update_date datetime,version bigint(11),PRIMARY KEY (school_config_id))"));
			sql.append(db.modifyColumn("kt_bas_school","school_admin_phone","VARCHAR(255)"));
			sql.append(db.addColumn("kt_bas_school_module","partner_code","VARCHAR (255)",true,null,"036005,036010","合作商code","parent_code"));
			sql.append(db.dropTable("kt_bas_school_price"));
			sql.append(db.dropTable("kt_sys_server_bandwidth"));
			sql.append(db.dropTable("kt_sys_server_hard_disk"));
			sql.append(db.dropTable("kt_sys_server_memory"));
			sql.append(db.dropTable("kt_bas_school_server_config"));
			sql.append(db.addColumn("kt_sys_server_config","remarks","VARCHAR (255)",true,null,"","备注","bandwidth_price"));
			sql.append(db.addColumn("kt_bas_contact","grade_id","int(10)",true,null,"","年级ID,0为全校","school_id"));
			sql.append(db.addColumn("kt_bas_contact","phone","varchar(11)",true,null,"","任课教师手机号","user_id"));
			sql.append(db.addColumn("kt_bas_contact","start_date","datetime",true,null,"","开课时间","course"));
			sql.append(db.addColumn("kt_bas_contact","end_date","datetime",true,null,"","课程结束时间","start_date"));
			sql.append(db.addColumn("kt_bas_contact","remark","text",true,null,"","备注","end_date"));
			sql.append(db.addColumn("kt_bas_contact","team_count","int(10)",true,null,"","班级最大人数","remark"));
			sql.append(db.addColumn("kt_bas_contact","apply_count","int(10)",true,null,"","可报名人数","team_count"));
			sql.append(db.addColumn("kt_bas_contact","apply_start_date","datetime",true,null,"","报名开始时间","apply_count"));
			sql.append(db.addColumn("kt_bas_contact","apply_end_date","datetime",true,null,"","报名结束时间","apply_start_date"));
			sql.append(db.addColumn("kt_bas_contact","schedule_url","varchar(255)",true,null,"","课程表url","apply_end_date"));
			sql.append(db.addColumn("kt_bas_contact","is_grab","int(1)",true,null,"","课程模式：1为抢报模式，0为非抢报模式","schedule_url"));
			sql.append(db.createTable("kt_bas_interest_apply","CREATE TABLE kt_bas_interest_apply(id bigint(20) NOT NULL AUTO_INCREMENT, contact_id int(10) COMMENT '兴趣班ID', student_id int(10) COMMENT '学生ID', create_by bigint(11), create_date datetime, update_by bigint(11), update_date datetime,version bigint(11), PRIMARY KEY (id))"));
			sql.append(db.createTable("kt_bas_schedule","create table kt_bas_schedule(schedule_id bigint(20) not null auto_increment, contact_id int (10) comment '兴趣班ID', class_date varchar(20) comment '上课时间', place varchar(255) comment '上课地点' , create_by bigint(11), create_date datetime, update_by bigint(11), update_date datetime,version bigint(11), PRIMARY KEY (schedule_id))"));
			sql.append(db.createTable("kt_bas_interest_out","CREATE TABLE kt_bas_interest_out(id bigint(20) NOT NULL AUTO_INCREMENT, contact_id int(10) COMMENT '兴趣班ID', student_id int(10) COMMENT '学生ID', create_by bigint(11), create_date datetime, update_by bigint(11), update_date datetime,version bigint(11), PRIMARY KEY (id))"));
			sql.append(db.modifyColumn("kt_sys_app","update_url","varchar(255)"));
			sql.append(db.addColumn("kt_bas_balance_log","out_trade_no","varchar(255)",true,null,"","流水号","content"));
			sql.append(db.addColumn("kt_bas_balance_log","ip_address","varchar(15)",true,null,"","IP地址","content"));
			sql.append(db.addColumn("kt_bas_balance_log","recharge_type","varchar(6)",true,null,"","充值类型","content"));
			sql.append(db.modifyColumnName("kt_bas_balance_log","original_balance","pre_balance","varchar(255)",0));
			sql.append(db.modifyColumnName("kt_bas_balance_log","consumption","money","bigint(20)",0));
			sql.append(db.modifyColumnName("kt_bas_balance_log","user_id","agent_id","bigint(20)",0));
			sql.append(db.modifyColumnName("kt_bas_balance","user_id","agent_id","bigint(20)",0));
			sql.append(db.createTable("kt_bus_pay_group","CREATE TABLE kt_bus_pay_group(id bigint(20) NOT NULL AUTO_INCREMENT, pay_id int(10) COMMENT '缴费ID', school_id int(10) COMMENT '学校ID',team_type varchar(6) COMMENT '团体类型',group_id int(10) COMMENT '年级ID',team_id int(10) COMMENT '班级ID',user_id int(10) COMMENT '缴费教师ID',student_id int(10) COMMENT '缴费学生ID', create_by bigint(11), create_date datetime, update_by bigint(11), update_date datetime,version bigint(11), PRIMARY KEY (id))"));
			sql.append(db.createTable("kt_bus_pay_detail","CREATE TABLE kt_bus_pay_detail(id bigint(20) NOT NULL AUTO_INCREMENT, pay_id int(10) COMMENT '缴费ID', school_id int(10) COMMENT '学校ID',user_id int(10) COMMENT '缴费教师ID',student_id int(10) COMMENT '教师学生ID',pay_type varchar(6) COMMENT '转账类型',pay_date datetime COMMENT '缴费日期',client_id varchar(255) COMMENT '客户端ID',out_trade_no varchar(255) COMMENT '流水号',trade_no varchar(255) COMMENT '支付宝流水号', create_by bigint(11), create_date datetime, update_by bigint(11), update_date datetime,version bigint(11), PRIMARY KEY (id))"));
			sql.append(db.createTable("kt_bus_pay","CREATE TABLE kt_bus_pay(pay_id bigint(20) NOT NULL AUTO_INCREMENT, school_id int(10) COMMENT '学校ID',end_date date COMMENT '截止日期',pay_title varchar(255) COMMENT '缴费标题',sender_name varchar(255) COMMENT '发件人名称',pay_content varchar(255) COMMENT '缴费内容',pay_money double(10,2) COMMENT '缴费金额',pay_type varchar(6) COMMENT '缴费类型',pay_category varchar(9) COMMENT '缴费类别',user_type varchar(6) COMMENT '用户类型', create_by bigint(11), create_date datetime, update_by bigint(11), update_date datetime,version bigint(11), PRIMARY KEY (pay_id))"));
			sql.append(db.addColumn("kt_bus_score_list","count","int(2)",false,null,"1","扣分项个数","score"));
			sql.append(db.addColumn("kt_bus_score_list","group_id","int(10)",true,null,"","年级ID","team_type"));
			sql.append(db.addColumn("kt_bus_score","student_id","int(10)",true,null,"","学生ID","module_code"));
			sql.append(db.createTable("kt_bus_student_leave","CREATE TABLE kt_bus_student_leave ( leave_id int(11) NOT NULL AUTO_INCREMENT, school_id int(11) DEFAULT NULL COMMENT '学校ID', student_id int(11) DEFAULT NULL COMMENT '学生ID', student_code varchar(20) DEFAULT NULL COMMENT '学生学号', team_type varchar(6) DEFAULT NULL COMMENT '班级类型', team_id int(11) DEFAULT NULL COMMENT '班级ID', group_id int(11) DEFAULT NULL COMMENT '年级ID', leave_type varchar(6) DEFAULT NULL COMMENT '请假类型', symptom_type varchar(6) DEFAULT NULL COMMENT '症状类型', leave_content text COMMENT '请假说明', start_date varchar(10) DEFAULT NULL COMMENT '请假开始日期', end_date varchar(10) DEFAULT NULL COMMENT '请假结束时间', approver_id int(11) DEFAULT NULL COMMENT '审批教师user_id', master_id int(11) DEFAULT NULL COMMENT '转交后的教务处教师的user_id', leave_status varchar(6) DEFAULT NULL COMMENT '请假状态', refuse_content text COMMENT '拒绝理由', create_by int(11) DEFAULT NULL COMMENT '创建者', create_date datetime DEFAULT NULL COMMENT '创建时间', update_by int(11) DEFAULT NULL COMMENT '更新者', update_date datetime DEFAULT NULL COMMENT '更新时间', version int(11) DEFAULT NULL COMMENT '版本号', PRIMARY KEY (leave_id)) "));
			sql.append(db.createTable("kt_bus_student_leave_file","CREATE TABLE kt_bus_student_leave_file (id bigint(20) NOT NULL AUTO_INCREMENT,leave_id bigint(20) DEFAULT NULL COMMENT '请假ID，外键',file_type varchar(6) DEFAULT NULL COMMENT '文件类型', file_url varchar(256) DEFAULT NULL COMMENT '文件URL,绝对路径',file_resize_url varchar(256) DEFAULT NULL COMMENT '图片缩略图URL，绝对路径', file_name varchar(100) DEFAULT NULL COMMENT '文件名称',create_by int(11) DEFAULT NULL COMMENT '创建者', create_date datetime DEFAULT NULL COMMENT '创建时间',update_by int(11) DEFAULT NULL COMMENT '更新者', update_date datetime DEFAULT NULL COMMENT '更新时间',version int(11) DEFAULT NULL COMMENT '版本号', PRIMARY KEY (id))"));
			sql.append(db.addColumn("kt_bus_pay_detail","trade_status","VARCHAR (20)",true,null,"","支付状态","pay_date"));
			sql.append(db.addColumn("kt_bas_balance_log","trade_status","VARCHAR (20)",true,null,"","支付状态","content"));
			sql.append(db.addColumn("kt_bas_balance_log","trade_no","VARCHAR (255)",true,null,"","支付宝流水号","content"));
			sql.append(db.addColumn("kt_bas_school","zip_code","varchar(10)",true,null,"","邮政编码","phone"));
			sql.append(db.addColumn("kt_bas_school","record_no","varchar(255)",true,null,"","备案号","phone"));
			sql.append(db.addColumn("kt_bas_school","town","varchar(255)",true,null,"","镇","phone"));
			sql.append(db.createTable("kt_bus_student_recruite","CREATE TABLE kt_bus_student_recruite (recruit_id int(11) NOT NULL AUTO_INCREMENT COMMENT '主键', school_id int(11) DEFAULT NULL COMMENT '学校ID', title varchar(255) DEFAULT NULL COMMENT '招生简章标题', content text COMMENT '招生简章内容', apply_start_date varchar(10) DEFAULT NULL COMMENT '报名开始时间', apply_end_date varchar(10) DEFAULT NULL COMMENT '报名结束时间', status varchar(6) DEFAULT NULL COMMENT '招生简章状态，是否已生成正式录取名单', completion_date varchar(10) DEFAULT NULL COMMENT '录取完成时间', create_by int(11) DEFAULT NULL COMMENT '创建者', create_date datetime DEFAULT NULL COMMENT '创建日期', update_by int(11) DEFAULT NULL COMMENT '更新者', update_date datetime DEFAULT NULL COMMENT '更新时间', version int(11) DEFAULT NULL COMMENT '版本号', PRIMARY KEY (recruit_id)	)"));
			sql.append(db.createTable("kt_bus_student_enroll","CREATE TABLE kt_bus_student_enroll (id int(11) NOT NULL AUTO_INCREMENT COMMENT '主键', recruit_id int(11) DEFAULT NULL COMMENT '外键', school_id int(11) DEFAULT NULL COMMENT '学校ID', student_name varchar(20) DEFAULT NULL COMMENT '学生姓名', sex int(1) DEFAULT NULL COMMENT '学生性别', id_number varchar(18) DEFAULT NULL COMMENT '身份证号', head_url varchar(100) DEFAULT NULL COMMENT '学生头像', middle_school varchar(255) DEFAULT NULL COMMENT '就读初中', register_school varchar(255) DEFAULT NULL COMMENT '报考学校', color_blindness int(1) DEFAULT NULL COMMENT '是否色盲，1为是，0为否', person_specialty varchar(255) DEFAULT NULL COMMENT '个人特长', award_situation varchar(255) DEFAULT NULL COMMENT '获奖情况', parent_name varchar(20) DEFAULT NULL COMMENT '家长姓名', relationship varchar(20) DEFAULT NULL COMMENT '与孩子关系', parent_company varchar(255) DEFAULT NULL COMMENT '家长单位', phone varchar(11) DEFAULT NULL COMMENT '家长手机号', is_accommodate int(1) DEFAULT NULL COMMENT '是否住宿，1为是，0为否', enroll_status varchar(6) DEFAULT NULL COMMENT '报名状态，已录取，未录取', create_by int(11) DEFAULT NULL COMMENT '创建者', create_date datetime DEFAULT NULL COMMENT '创建时间', update_by int(11) DEFAULT NULL COMMENT '更新者', update_date datetime DEFAULT NULL COMMENT '更新时间', version int(11) DEFAULT NULL COMMENT '版本号', PRIMARY KEY (id))"));
			sql.append(db.modifyColumn("kt_sys_menu","user_type","varchar(255)"));
            sql.append(db.createTable("kt_bus_student_register","CREATE TABLE kt_bus_student_register (register_id int(11) NOT NULL AUTO_INCREMENT COMMENT '主键', school_id int(11) DEFAULT NULL COMMENT '学校ID', enrollment_year int(4) DEFAULT NULL COMMENT '报到年份', student_name varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '报到学生姓名', sex int(1) DEFAULT NULL COMMENT '报到学生性别', id_number varchar(18) CHARACTER SET utf8 DEFAULT NULL COMMENT '身份证号码', middle_school varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '就读初中学校', is_accommodate int(1) DEFAULT NULL COMMENT '是否住宿', create_by int(11) DEFAULT NULL COMMENT '创建者', create_date datetime DEFAULT NULL COMMENT '创建时间', update_by int(11) DEFAULT NULL COMMENT '更新者', update_date datetime DEFAULT NULL COMMENT '更新时间', version int(11) DEFAULT NULL COMMENT '版本号', PRIMARY KEY (register_id))"));
			sql.append(db.addColumn("kt_bas_school","host_url","varchar(255)",true,null,"","学校官网","domain"));
			sql.append(db.addColumn("kt_bas_school","main_url","varchar(255)",true,null,"","学校自己的官网","domain"));
			sql.append(db.addColumn("kt_bas_school","main_domain","varchar(20)",true,null,"","学校官网域名","domain"));
			sql.append(db.addColumn("kt_bas_school","path","varchar(255)",true,null,"","官网模板编号","domain"));
			sql.append(db.addColumn("kt_bas_school","manager_url","varchar(255)",true,null,"","学校后台管理路径","domain"));
			sql.append(db.addColumn("kt_bas_school","copyright","varchar(255)",true,null,"","版权","domain"));
			sql.append(db.addColumn("kt_bas_school","content","longtext",true,null,"","网页配置","domain"));
			sql.append(db.addColumn("kt_bas_school","urban_district","varchar(255)",true,null,"","市/区","domain"));
			sql.append(db.dropColumn("kt_bas_school","town"));
			sql.append(db.dropColumn("kt_bas_school","county"));
			sql.append(db.dropColumn("kt_bas_school","city"));
			sql.append(db.createTable("kt_bas_role_menu","CREATE TABLE `kt_bas_role_menu` (`id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID', `school_id` int(11) DEFAULT '0' COMMENT '学校ID',  `role_code` varchar(9) DEFAULT NULL COMMENT '身份',`menu_id` int(11) DEFAULT '0' COMMENT '菜单ID',`is_active` int(11) DEFAULT '0' COMMENT '是否启用',`create_by` int(11) DEFAULT NULL COMMENT '创建人',`create_date` datetime DEFAULT NULL COMMENT '创建时间',`update_by` int(11) DEFAULT NULL COMMENT '更新者',`update_date` datetime DEFAULT NULL COMMENT '更新时间', `version` int(11) DEFAULT NULL COMMENT '版本号', PRIMARY KEY (`id`))"));
			sql.append(db.createTable("kt_sys_menu_module","CREATE TABLE `kt_sys_menu_module` (`id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键，自增长',`menu_id` int(11) DEFAULT NULL COMMENT '菜单ID',`module_code` varchar(6) CHARACTER SET utf8 DEFAULT NULL COMMENT '模块代码',`user_type` varchar(6) CHARACTER SET utf8 DEFAULT NULL COMMENT '用户类型',`is_default` int(1) DEFAULT '0' COMMENT '是否默认模块',`is_active` int(1) DEFAULT '0' COMMENT '是否启用',`create_by` int(11) DEFAULT NULL COMMENT '创建者',`create_date` datetime DEFAULT NULL COMMENT '创建时间',`update_by` int(11) DEFAULT NULL COMMENT '更新者',`update_date` datetime DEFAULT NULL COMMENT '更新时间',`version` int(11) DEFAULT '0' COMMENT '版本号',PRIMARY KEY (`id`))"));
			sql.append(db.addColumn("kt_bus_notice","total_count","int(6)",false,null,"0","通知总人数","notice_content"));
			sql.append(db.createTable("kt_bus_micro_portal","CREATE TABLE `kt_bus_micro_portal` (`column_id`  bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID' ,`school_id`  bigint(11) NULL DEFAULT 0 COMMENT '学校ID' ,`column_name`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '栏目名称' ,`column_content`  longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '栏目内容' ,`create_by`  int(11) NULL DEFAULT NULL COMMENT '创建者' ,`create_date`  datetime NULL DEFAULT NULL COMMENT '创建时间' ,`update_by`  int(11) NULL DEFAULT NULL COMMENT '更新者' ,`update_date`  datetime NULL DEFAULT NULL COMMENT '更新时间' ,`version`  int(11) NULL DEFAULT 0 COMMENT '版本号' ,PRIMARY KEY (`column_id`));"));
			sql.append(db.createTable("kt_bas_micro_portal_info","CREATE TABLE `kt_bas_micro_portal_info` (`info_id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',`school_id` bigint(11) DEFAULT '0' COMMENT '学校ID',`campus_name` varchar(50) DEFAULT NULL COMMENT '学校校区',`address` varchar(255) DEFAULT NULL COMMENT '学校校区地址',`phone` varchar(11) DEFAULT NULL COMMENT '学校校区联系电话',`email` varchar(255) DEFAULT NULL COMMENT '学校Email',`create_by` int(11) DEFAULT NULL COMMENT '创建者',`create_date` datetime DEFAULT NULL COMMENT '创建时间',`update_by` int(11) DEFAULT NULL COMMENT '更新者',`update_date` datetime DEFAULT NULL COMMENT '更新时间',`version` int(11) DEFAULT '0' COMMENT '版本号',PRIMARY KEY (`info_id`));"));
			sql.append(db.createTable("kt_bus_micro_portal_file","CREATE TABLE `kt_bus_micro_portal_file` (`file_id` bigint(20) NOT NULL AUTO_INCREMENT,`school_id` bigint(20) DEFAULT NULL COMMENT '学校ID',`file_type` varchar(6) DEFAULT NULL COMMENT '附件类型',`file_name` varchar(50) DEFAULT NULL COMMENT '文件名',`file_url` varchar(255) DEFAULT NULL COMMENT '文件URL,绝对路径',`file_resize_url` varchar(255) DEFAULT NULL COMMENT '附件缩略图url',`create_by` int(11) DEFAULT NULL COMMENT '创建者',`create_date` datetime DEFAULT NULL COMMENT '创建时间',`update_by` int(11) DEFAULT NULL COMMENT '更新者',`update_date` datetime DEFAULT NULL COMMENT '更新时间',`version` int(11) DEFAULT '0' COMMENT '版本号',PRIMARY KEY (`file_id`));"));
			sql.append(db.addColumn("kt_sys_menu","target","varchar(255)",true,null,"iframe1","指向目标","css_name"));
			sql.append(db.createTable("kt_bas_file","CREATE TABLE `kt_bas_file` (`file_id` bigint(20) NOT NULL AUTO_INCREMENT,`school_id` int(11) DEFAULT NULL,`parent_id` bigint(20) DEFAULT '0',`module_code` varchar(6) DEFAULT NULL COMMENT '模块类型',`file_type` varchar(255) DEFAULT NULL COMMENT '附件类型',`file_name` varchar(255) DEFAULT NULL COMMENT '文件名',`file_url` varchar(256) DEFAULT NULL COMMENT '文件URL,绝对路径',`file_resize_url` varchar(255) DEFAULT NULL COMMENT '附件缩略图url',`file_real_name` varchar(255) DEFAULT NULL,`file_size` varchar(255) DEFAULT NULL,`play_time` int(11) DEFAULT '0' COMMENT '音频播放时间长度',`create_by` int(11) DEFAULT NULL COMMENT '创建者',`create_date` datetime DEFAULT NULL COMMENT '创建时间',`version` int(11) DEFAULT '0' COMMENT '版本号',PRIMARY KEY (`file_id`));"));
			sql.append(db.createTable("kt_bas_school_menu","CREATE TABLE `kt_bas_school_menu` (`school_menu_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',`school_id` int(11) DEFAULT NULL COMMENT '学校ID',`menu_date` varchar(255) DEFAULT NULL COMMENT '添加学校菜谱的时间',`monday_data` varchar(255) DEFAULT NULL COMMENT '菜谱所在周的时间',`menu_name` varchar(255) DEFAULT NULL COMMENT '菜名称',`create_by` int(11) DEFAULT NULL COMMENT '创建人',`create_date` datetime DEFAULT NULL COMMENT '创建时间',`update_by` int(11) DEFAULT NULL COMMENT '更新者',`update_date` datetime DEFAULT NULL COMMENT '更新时间',`version` int(11) DEFAULT NULL COMMENT '版本号',PRIMARY KEY (`school_menu_id`));"));
			sql.append(db.createTable("kt_bus_photo_comment","CREATE TABLE `kt_bus_photo_comment` (`id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',`school_id` bigint(11) DEFAULT '0' COMMENT '学校ID',`user_id` bigint(11) DEFAULT '0' COMMENT '用户ID',`user_type` varchar(6) DEFAULT NULL COMMENT '用户类型',`student_id` bigint(11) DEFAULT '0' COMMENT '团队ID',`relation` varchar(9) DEFAULT NULL COMMENT '团队类型',`add_date` varchar(255) DEFAULT '0' COMMENT '起源时间',`point_praise` bigint(1) DEFAULT NULL COMMENT '点赞',`comment` varchar(255) DEFAULT NULL,`create_by` int(6) DEFAULT NULL COMMENT '创建者',`create_date` datetime DEFAULT NULL COMMENT '创建日期',`update_by` int(11) DEFAULT NULL COMMENT '更新者',`update_date` datetime DEFAULT NULL COMMENT '更新日期',`version` int(11) DEFAULT NULL COMMENT '版本号',PRIMARY KEY (`id`));"));
			sql.append(db.addColumn("kt_bas_school","end_work_date","varchar(50)",true,null,"","教师下班时间","app_main_url"));
			sql.append(db.addColumn("kt_bas_school","start_work_date","varchar(50)",true,null,"","教师上班时间","app_main_url"));
			sql.append(db.addColumn("kt_bas_school","end_school_date","varchar(50)",true,null,"","学生放学时间","app_main_url"));
			sql.append(db.addColumn("kt_bas_school","start_school_date","varchar(50)",true,null,"","学生上学时间","app_main_url"));
			sql.append(db.addColumn("kt_bus_notice_read","is_confirm","int(1)",false,null,"0","是否已确认通知","student_id"));
			sql.append(db.createTable("kt_bus_teacher_attend","CREATE TABLE kt_bus_teacher_attend (attend_id bigint(20) NOT NULL AUTO_INCREMENT,school_id int(11) DEFAULT NULL COMMENT '学校ID', user_id int(11) DEFAULT NULL COMMENT '打卡人用户ID',attend_type varchar(6) DEFAULT NULL COMMENT '打卡类型：上班，下班',attend_time datetime DEFAULT NULL COMMENT '打开时间',longitude varchar(20) DEFAULT NULL COMMENT '经度',latitude varchar(20) DEFAULT NULL COMMENT '纬度',address text COMMENT '地址',attend_status varchar(6) DEFAULT NULL COMMENT '考勤状态',create_by int(11) DEFAULT NULL COMMENT '创建者',create_date datetime DEFAULT NULL COMMENT '创建时间',update_by int(11) DEFAULT NULL COMMENT '更新者',update_date datetime DEFAULT NULL COMMENT '更新时间',version int(11) DEFAULT NULL COMMENT '版本号',PRIMARY KEY (attend_id))"));
			sql.append(db.createTable("kt_bus_student_attend","CREATE TABLE kt_bus_student_attend (attend_id bigint(20) NOT NULL AUTO_INCREMENT,school_id int(11) DEFAULT NULL COMMENT '学校ID', group_id int(11) DEFAULT NULL COMMENT '年级ID',team_id int(11) DEFAULT NULL COMMENT '班级ID',student_id int(11) DEFAULT NULL COMMENT '学生ID',attend_time datetime DEFAULT NULL COMMENT '打卡时间',file_url varchar(255) DEFAULT NULL COMMENT '文件URL',file_resize_url varchar(255) DEFAULT NULL COMMENT '缩略图URL',create_by int(11) DEFAULT NULL COMMENT '创建者',create_date datetime DEFAULT NULL COMMENT '创建时间',update_by int(11) DEFAULT NULL COMMENT '更新者',update_date datetime DEFAULT NULL COMMENT '更新时间',version int(11) DEFAULT NULL COMMENT '版本号',PRIMARY KEY (attend_id))"));
			sql.append(db.addColumn("kt_bas_student","card_number","varchar(18)",true,null,"","通勤卡号","sex"));
			sql.append(db.addColumn("kt_bas_school","longitude","varchar(20)",true,null,"","经度","address"));
			sql.append(db.addColumn("kt_bas_school","latitude","varchar(20)",true,null,"","纬度","longitude"));
			sql.append(db.addColumn("kt_bas_school","attend_range","int(5)",true,null,null,"考勤范围","latitude"));
			if (StringUtil.isNotEmpty(sql.toString())) { // 有实际执行语句时
				logger.error("该次执行的语句有：", sql);
				db.executeSql("update kt_sys_config set config_value=config_value+1 where config_key='DB_VERSION'");
			}else logger.error("没有需要更新的SQL，该数据库已经是最新版本...");
			db.getConn().close();
			logger.error("更新数据库版本结束...." );
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static Connection connetctDB() {
		try {
			// System.out.println("开始加载配置文件config.properties");
			configProps = PropertiesLoaderUtils.loadAllProperties("jdbc.properties");
			jdbc_driver = configProps.getProperty("jdbc.driverClassName");
			jdbc_url = configProps.getProperty("jdbc.databaseURL");
			jdbc_user = configProps.getProperty("jdbc.username");
			jdbc_password = configProps.getProperty("jdbc.password");
			jdbc_dbname = configProps.getProperty("jdbc.jdbc_dbname");
			Class.forName(jdbc_driver);
			Connection conn = DriverManager.getConnection(jdbc_url, jdbc_user,jdbc_password);
			return conn;
		} catch (Exception e) {
		}
		return null;
	}

//	// spring启动后自动执行
//	public void onApplicationEvent(ContextRefreshedEvent event) {
//		if (event.getApplicationContext().getParent() == null) {
//				SQLUpdate();
//		}
//	}
}
