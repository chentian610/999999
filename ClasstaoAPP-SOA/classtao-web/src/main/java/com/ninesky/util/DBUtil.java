package com.ninesky.util;

import com.ninesky.common.DictConstants;
import com.ninesky.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;


/**
 * 数据库结构更新类
 * @author Chenth
 */
public class DBUtil {
	private static String changeLine = ";\n";
	private static String comment = "-- ";//注释
	private Connection conn;
	private Statement statement;
	private String jdbc_dbname;
	private static Logger logger = LoggerFactory.getLogger(DBUtil.class);
	public DBUtil(Connection conn,String jdbc_dbname) throws SQLException {
    	if (this.conn==null) this.conn=conn;
		// statement用来执行SQL语句
		this.statement = conn.createStatement();
		this.jdbc_dbname = jdbc_dbname;
	}
	
	/**
	 * 删除表
	 */
	public String dropTable(String table_name) {
		// 要执行的SQL语句
		logger.error("删除表["+table_name+"]开始...");
		String result = comment+"删除表["+table_name+"]开始..."+changeLine;
		String sql1 = "SELECT 1 FROM INFORMATION_SCHEMA.TABLES WHERE table_name = '"+table_name+"' AND TABLE_SCHEMA = '"+jdbc_dbname+"'" ;
		ResultSet rs = null;
		String sql = "DROP TABLE  IF EXISTS "+jdbc_dbname+ "."+table_name;
		try {
			rs = statement.executeQuery(sql1);
			while(rs.next()){
				statement.execute(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("删除表["+table_name+"]失败："+e.getMessage());
			result += comment+"删除表["+table_name+"]失败："+e.getMessage()+changeLine;
			return result;
		}
		return null;
	}
	
	/**
	 * 创建表
	 */
	public String createTable(String table_name,String SQL){
		logger.error("创建表["+table_name+"]开始...");
		String result = comment+"创建表["+table_name+"]开始..."+changeLine;
		String sql1 = "SELECT 1 FROM INFORMATION_SCHEMA.TABLES WHERE table_name = '"+table_name+"' AND TABLE_SCHEMA = '"+jdbc_dbname+"'" ;
		ResultSet rs = null;
		try {
			rs = statement.executeQuery(sql1);
			if (rs.next()) return null;//
			statement.execute(SQL);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("创建表["+table_name+"]失败："+e.getMessage());
			result += comment+"创建表["+table_name+"]失败："+e.getMessage()+changeLine;
			return result;
		}
		result += SQL+changeLine;
		logger.error("创建表["+table_name+"]成功...");
		result += comment+"创建表["+table_name+"]成功..."+changeLine;
		return result;
	}



	/**
	 * 添加字段
	 * @param table_name 表名
	 * @param column_name 添加字段名
	 * @param data_type 数据类型
	 * @param can_null 是否允许为空
	 * @param character 字符集
	 * @param default_value 默认值
	 * @param comments 字段备注
	 * @param after_column_name 被插入字段名
	 * @return
	 */
	public String addColumn(String table_name,String column_name,String data_type,boolean can_null,String character,String default_value,String comments,String after_column_name){
		logger.error("添加字段["+table_name+"."+column_name+"]开始...");
		String result = comment+"添加字段["+table_name+"."+column_name+"]开始..."+changeLine;
		String sql1 = "SELECT 1 FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = '"+jdbc_dbname+"' AND TABLE_NAME = '"+table_name+"' AND COLUMN_NAME = '"+column_name+"'" ;
		ResultSet rs = null;
		String SQL = "ALTER TABLE " + jdbc_dbname + "."+table_name	+ " ADD COLUMN "+ column_name +" " + data_type +(StringUtil.isEmpty(character)?"":" CHARACTER SET " +character+ " ")+(can_null?"":"  NOT NULL ")+(StringUtil.isEmpty(default_value)?" DEFAULT NULL":" DEFAULT '"+default_value+"'")+" COMMENT '"+comments+"'"+(after_column_name==null?"":" AFTER "+after_column_name);
		try {
			rs = statement.executeQuery(sql1);
			if (rs.next()) {
				logger.error("字段["+table_name+"."+column_name+"]已经存在，跳过...");
				return "";
			}
			statement.execute(SQL);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("添加字段["+table_name+"]失败："+e.getMessage());
			result += comment+"添加字段["+table_name+"]失败："+e.getMessage()+changeLine;
			return result;
		}
		result += SQL+changeLine;
		logger.error("添加字段["+table_name+"."+column_name+"]成功...");
		result += comment+"添加字段["+table_name+"."+column_name+"]成功..."+changeLine;
		return result;
	}

	/**
	 * 添加字段
	 * @param table_name 表名
	 * @param column_name 添加字段名
	 * @param data_type 数据类型
	 * @param comment 字段备注
	 * @param after_column_name 字段后面的名字
	 * @return
	 */
	public String addColumn(String table_name,String column_name,String data_type,String comment,String after_column_name){
		return addColumn(table_name,column_name,data_type,true,null,null,comment,after_column_name);
	}

	/**
	 * 添加字段
	 * @param table_name 表名
	 * @param column_name 添加字段名
	 * @param data_type 数据类型
	 * @param comment 字段备注
	 * @return
	 */
	public String addColumn(String table_name,String column_name,String data_type,String comment){
		return addColumn(table_name,column_name,data_type,true,null,null,comment,null);
	}

	/**
	 * 添加字段
	 * @param table_name 表名
	 * @param column_name 添加字段名
	 * @param data_type 数据类型
	 * @return
	 */
	public String addColumn(String table_name,String column_name,String data_type){
		return addColumn(table_name,column_name,data_type,true,null,null,"",null);
	}

	/**
	 * 删除字段
	 */
	public String dropColumn(String table_name,String column_name){
		logger.error("删除字段["+table_name+"."+column_name+"]开始...");
		String result = comment+"删除字段["+table_name+"."+column_name+"]开始..."+changeLine;
		String sql = "SELECT 1 FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = '"+jdbc_dbname+"' AND TABLE_NAME = '"+table_name+"' AND COLUMN_NAME = '"+column_name+"'" ;
		ResultSet rs = null;
		String SQL = "ALTER TABLE " + jdbc_dbname + "."+table_name	+ " DROP COLUMN "+ column_name;
		try {
			rs = statement.executeQuery(sql);
			if (rs.next()) statement.execute(SQL);
			else {
				logger.error("字段["+table_name+"."+column_name+"]已经不存在，跳过...");
				return "";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("删除字段["+table_name+"."+column_name+"]失败："+e.getMessage());
			result += comment+"删除字段["+table_name+"."+column_name+"]失败："+e.getMessage()+changeLine;
			return result;
		}
		result += SQL+changeLine;
		logger.error("删除字段["+table_name+"."+column_name+"]成功...");
		result += comment+"删除字段["+table_name+"."+column_name+"]成功..."+changeLine;
		return result;
	}

	/**
	 * 修改字段
	 */
	public String modifyColumn(String table_name,String column_name,String dataType) {
		logger.error("修改字段["+table_name+"."+column_name+"]开始...");
		String result = comment+"修改字段["+table_name+"."+column_name+"]开始..."+changeLine;
		String sql = "SELECT 1 FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = '"+jdbc_dbname+"' AND TABLE_NAME = '"+table_name+"' AND COLUMN_NAME = '"+column_name+"' AND DATA_TYPE = '"+dataType+"'" ;
		ResultSet rs = null;
		String SQL = "ALTER TABLE " + jdbc_dbname + "."+table_name	+ " MODIFY COLUMN "+ column_name +" " +dataType;
		try {
			rs = statement.executeQuery(sql);
			if (rs.next()) {
				logger.error("字段["+table_name+"."+column_name+"]已经是被更新，跳过...");
				return "";
			} else statement.execute(SQL);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("修改字段["+table_name+"."+column_name+"]失败："+e.getMessage());
			result += comment+"修改字段["+table_name+"."+column_name+"]失败："+e.getMessage()+changeLine;
			return result;
		}
		result += SQL+changeLine;
		logger.error("修改字段["+table_name+"."+column_name+"]成功...");
		result += comment+"修改字段["+table_name+"."+column_name+"]成功..."+changeLine;
		return result;
	}

	/**
	 * 修改表字段名称
	 * @param table_name 表
	 * @param column_name_old 原字段名
	 * @param column_name_new 新字段名
	 * @param dataType 字段类型
	 * @param is_primary_key 是否是主键
	 * @return
	 */
	public String modifyColumnName(String table_name,String column_name_old,String column_name_new,String dataType,Integer is_primary_key) {
		String result = comment+"修改字段名["+table_name+"."+column_name_old+"]开始..."+changeLine;
		String sql = "SELECT 1 FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = '"+jdbc_dbname+"' AND TABLE_NAME = '"+table_name+"' AND COLUMN_NAME = '"+column_name_old+"'" ;
		ResultSet rs = null;
		String SQL = "ALTER TABLE " + jdbc_dbname + "."+table_name	+ " CHANGE COLUMN "+ column_name_old +" " + column_name_new + " " +dataType + " " + (DictConstants.TRUE.equals(is_primary_key)?" NOT NULL AUTO_INCREMENT FIRST":"");
		try {
			rs = statement.executeQuery(sql);
			if (rs.next())
				statement.execute(SQL);
			else {
				logger.error("字段["+table_name+"."+column_name_old+"]已经被修改，跳过...");
				return "";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("修改字段名["+table_name+"."+column_name_old+"]失败："+e.getMessage());
			result += comment+"修改字段名["+table_name+"."+column_name_old+"]失败："+e.getMessage()+changeLine;
			return result;
		}
		result += SQL+changeLine;
		result += comment+"修改字段名["+table_name+"."+column_name_old+"]成功..."+changeLine;
		logger.error("修改字段名["+table_name+"."+column_name_old+"]成功...");
		return result;
	}


	/**
	 * 修改表字段名称
	 * @param check_SQL 表
	 * @param add_SQL 原字段名
	 * @return
	 */
	public String addRecord(String check_SQL,String add_SQL) {
		logger.error("添加记录["+add_SQL+"]开始...");
		String result = comment+"添加记录["+add_SQL+"]开始..."+changeLine;
		ResultSet rs = null;
		try {
			rs = statement.executeQuery(check_SQL);
			if (rs.next()) {
				logger.error("记录已经存在,跳过...");
				return "";
			}
			statement.executeQuery(add_SQL);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("添加记录["+add_SQL+"]失败："+e.getMessage());
			result += comment+"添加记录["+add_SQL+"]失败："+e.getMessage()+changeLine;
			return result;
		}
		logger.error("添加记录["+add_SQL+"]成功...");
		result += add_SQL+changeLine;
		result += comment+"添加记录["+add_SQL+"]成功..."+changeLine;
		return result;
	}

	/**
	 * 执行sql语句
	 * @param sql
	 * @return
	 */
	public String executeSql(String sql){
		logger.error("执行语句["+sql+"]开始...");
		String result ="执行语句["+sql+"]开始..."+changeLine;
		try {
			statement.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("执行语句["+sql+"]失败："+e.getMessage());
			result +="执行语句["+sql+"]失败："+e.getMessage()+changeLine;
			return result;
		}
		result += sql+changeLine;
		logger.error("执行语句["+sql+"]成功...");
		result +="执行语句["+sql+"]成功..."+changeLine;
		return result;
	}

	/**
	 *
	 * @param sql
	 * @param id:需大于数据库版本号（可查看kt_sys_config里的DB_VERSION的值）
	 * @return
	 */
	public String executeSql(String sql,Integer id){
		logger.error("执行语句["+sql+"]开始...");
		String result ="执行语句["+sql+"]开始..."+changeLine;
		try {
			ResultSet rs=statement.executeQuery("select 1 from kt_sys_config where config_key='DB_VERSION' and config_value>="+id);
			if (rs.next()) {
				logger.error("执行语句["+sql+"]失败：已执行过该条语句");
				result+="执行语句["+sql+"]失败：已执行过该条语句"+changeLine;
				return result;
			}
			statement.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("执行语句["+sql+"]失败："+e.getMessage());
			result +="执行语句["+sql+"]失败："+e.getMessage()+changeLine;
			return result;
		}
		result += sql+changeLine;
		logger.error("执行语句["+sql+"]成功...");
		result +="执行语句["+sql+"]成功..."+changeLine;
		return result;
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}
}
