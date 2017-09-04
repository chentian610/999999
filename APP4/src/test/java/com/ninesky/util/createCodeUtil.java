package com.ninesky.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.jdom.JDOMException;
import org.springframework.core.io.support.PropertiesLoaderUtils;



/**
 * 自动生成框架代码类
 * @author Chenth
 */
public class createCodeUtil {
	private static String jdbc_dbname;
	private static String jdbc_url;
	private static String jdbc_driver;
	private static String jdbc_user;
	private static String jdbc_password;
	private static Properties configProps;
	
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException, JDOMException {
    	String table_name="kt_bus_pay_notify";
    	String vo_name = "PayNotify";
    	String file_path = "E:/";
    	AutoCreateCode.autoCreateCode(connetctDB(),table_name,vo_name,file_path,jdbc_dbname);
    }
    
    
    private static Connection connetctDB(){
    	try {
    		configProps = PropertiesLoaderUtils.loadAllProperties("jdbc.properties");
    		jdbc_driver=configProps.getProperty("jdbc.driverClassName");
    		jdbc_url=configProps.getProperty("jdbc.databaseURL");
    		jdbc_user=configProps.getProperty("jdbc.username");
    		jdbc_password=configProps.getProperty("jdbc.password");
    		jdbc_dbname=configProps.getProperty("jdbc.jdbc_dbname");
    		Class.forName(jdbc_driver);
    		Connection conn = DriverManager.getConnection(jdbc_url, jdbc_user, jdbc_password);
    		return conn;
    	} catch (Exception e) {
    	}
		return null;
    }
}
