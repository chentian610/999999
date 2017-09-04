package com.ninesky.util;

import org.apache.logging.log4j.util.PropertiesUtil;
import org.jdom.*;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.*;
import java.sql.*;
import java.util.Properties;

/**
 * 自动生成代码文件
 * @author Chenth
 */
public class AutoCreateCode {
	private static String jdbc_dbname = "classtao_user";
	private static String jdbc_url = "jdbc:mysql://localhost:3306/"+jdbc_dbname;
	private static String jdbc_driver = "com.mysql.jdbc.Driver";
	private static String jdbc_user = "root";
	private static String jdbc_password = "a";
	
	/**
	 * 表名
	 */
	private static String table_name = "kt_sys_sec";
	private static String vo_name = "Sec";
	private static String file_path = "G:/";
	private static String Tab = "\t";
	private static String enter = "\n";

	private static Properties _prop = new Properties();
	
	
	/**
	 * 创建Java实体类对象
	 * @param rs
	 * @throws IOException
	 * @throws JDOMException
	 * @throws SQLException
	 */
	public static void createJavaVO(ResultSet rs,String table_name,String vo_name,String file_path) throws IOException, JDOMException, SQLException { 
		String vo =vo_name+ "VO";
		File javaFile = new File(file_path+vo+".java");
		if(javaFile.exists()) javaFile.delete();
	    //实体类对象
	    StringBuffer javaColumn = new StringBuffer("package com.ninesky.classtao.template.vo;"+enter+enter+"import java.util.Date;" + enter + enter + "public class "+vo+" {"+enter+enter+enter);
	    //实体类方法
	    StringBuffer javaMethod = new StringBuffer();
		try {
			   FileWriter fw = new FileWriter(javaFile, true);
			   BufferedWriter bw = new BufferedWriter(fw);
			   
	    while (rs.next()) {
			String javaType = getJavaType(rs.getString("DATA_TYPE"));
			String columnName = rs.getString("COLUMN_NAME");
			javaColumn.append(Tab+"/**"+enter);
			javaColumn.append(Tab+"* "+("".equals(rs.getString("COLUMN_COMMENT"))?"未设置，请在数据库中设置":rs.getString("COLUMN_COMMENT"))+enter);
			javaColumn.append(Tab+"*/"+enter);
			javaColumn.append(Tab+"private "+ javaType + " " +columnName+";"+enter+enter);
			javaMethod.append(Tab+"public void set"+columnName.substring(0,1).toUpperCase()+columnName.substring(1)+"("+javaType +" "+columnName+")  {"+enter);
			javaMethod.append(Tab+Tab+"this."+columnName+" = "+columnName+";"+enter);
			javaMethod.append(Tab+"}"+enter+enter);
			javaMethod.append(Tab+"public "+javaType +" get"+columnName.substring(0,1).toUpperCase()+columnName.substring(1)+"()  {"+enter);
			javaMethod.append(Tab+Tab+"return "+columnName+";"+enter);
			javaMethod.append(Tab+"}"+enter+enter);
		}
	    bw.write(javaColumn.append(javaMethod).append("}").toString());
	    bw.flush();
	    bw.close();
	    fw.close();
	    System.out.println("输出JAVA对象文件成功！路径是："+file_path+vo+".java");
	  } catch (IOException e) {
		   e.printStackTrace();
		  }
	}
	
	/**
	 * 
	 * @param rs
	 * @throws IOException
	 * @throws JDOMException
	 * @throws SQLException
	 */
    public static void createMapperXML(ResultSet rs,String table_name,String vo_name,String file_path) throws IOException, JDOMException, SQLException { 
    	String vo =vo_name+ "VO";
    	String namespace = vo_name.substring(0,1).toLowerCase()+vo_name.substring(1)+"Map";
    	String xml = namespace+".xml";
        // 创建根节点 并设置它的属性 ;     
        Element root = new Element("mapper").setAttribute("namespace",namespace);  
        // 将根节点添加到文档中；     
        Document Doc = new Document(root); 
        DocType docType = new DocType("mapper");
        docType.setPublicID("-//mybatis.org//DTD Mapper 3.0//EN");  
        docType.setSystemID("http://mybatis.org/dtd/mybatis-3-mapper.dtd");
        Doc.setDocType(docType);
       //创建插入xml;     
        Element insertXML = new Element("insert"); 
        insertXML.setAttribute("id", "insert"+vo_name);
        insertXML.setAttribute("parameterType", vo);
        
        Element selectXML = new Element("select"); 
        selectXML.setAttribute("id", "get"+vo_name+"List");
        selectXML.setAttribute("parameterType", vo);
        selectXML.setAttribute("resultType", vo);
        
        Element updateXML = new Element("update"); 
        updateXML.setAttribute("id", "update"+vo_name);
        updateXML.setAttribute("parameterType", vo);
        
        Element deleteXML = new Element("delete"); 
        deleteXML.setAttribute("id", "delete"+vo_name);
        deleteXML.setAttribute("parameterType", vo);
        
        
        StringBuffer insert =new  StringBuffer("insert into " + table_name + enter+"("+enter);
        StringBuffer insert2 =new  StringBuffer();
        StringBuffer insert3 =new  StringBuffer();
        
        
        StringBuffer update =new  StringBuffer("update " + table_name + ""+enter+"set ");
        StringBuffer update2 =new  StringBuffer();
        
        StringBuffer delete =new  StringBuffer("delete from " + table_name + ""+enter+"where id = #{id}");
        
        StringBuffer select =new  StringBuffer("select "+enter);
        StringBuffer select1 =new  StringBuffer();
        StringBuffer selectWhere =new  StringBuffer();
        
	    while (rs.next()) {
//		"\t"+
		insert.append(rs.getString("COLUMN_NAME")+","+enter);
		insert2.append("#{"+  rs.getString("COLUMN_NAME")+"},"+enter);
		update2.append(rs.getString("COLUMN_NAME")+" = #{"+  rs.getString("COLUMN_NAME")+"},"+enter);
		
		String javaType = getJavaType(rs.getString("DATA_TYPE"));
		String columnName = rs.getString("COLUMN_NAME");
		select1.append(rs.getString("COLUMN_NAME")+" as "+rs.getString("COLUMN_NAME")+","+enter);
	    }
        
        insert3.append(insert.substring(0, insert.length()-2));
        insert3.append(enter+") VALUES ("+enter);
        insert3.append(insert2.substring(0, insert2.length()-2));
        insert3.append(enter+")");
        insertXML.setText(insert3.toString());
        root.addContent(new Comment("插入表"+table_name));
        root.addContent(insertXML); 
        
        
        update.append(update2.substring(0, update2.length()-2));
        updateXML.setText(update.toString());
        root.addContent(new Comment("更新表"+table_name));
        root.addContent(updateXML);
        
        select.append(select1.substring(0, select1.length()-2));
        select.append(enter+"from "+ table_name + enter+ "where 1=1");
        root.addContent(new Comment("查询表"+table_name));
        selectXML.setText(select.toString());
        root.addContent(selectXML);
        
        deleteXML.setText(delete.toString());
        root.addContent(new Comment("删除表"+table_name));
        root.addContent(deleteXML); 
           
        
        Format format = Format.getPrettyFormat();  
        XMLOutputter XMLOut = new XMLOutputter(format);  
        XMLOut.output(Doc, new FileOutputStream(file_path+xml));  
        System.out.println("输出xmlMapper文件成功！路径是："+file_path+xml);
    }  
    
    public static void createMapperXML2(ResultSet rs,String table_name,String vo_name,String file_path) throws IOException, JDOMException, SQLException { 
    	String vo =vo_name+ "VO";
    	String namespace = vo_name.substring(0,1).toLowerCase()+vo_name.substring(1)+"Map";
    	String xml = namespace+".xml";
    	System.out.println(xml);
        // 创建根节点 并设置它的属性 ;     
        Element root = new Element("sqlMap").setAttribute("namespace",namespace);  
        // 将根节点添加到文档中；     
        Document Doc = new Document(root); 
        DocType docType = new DocType("sqlMap");
        docType.setPublicID("-//ibatis.apache.org//DTD SQL Map 2.0//EN");  
        docType.setSystemID("http://ibatis.apache.org/dtd/sql-map-2.dtd");
        Doc.setDocType(docType);
       //创建插入xml;     
        Element insertXML = new Element("insert"); 
        insertXML.setAttribute("id", "insert"+vo_name);
        insertXML.setAttribute("parameterClass", vo);
        
        Element selectXML = new Element("select"); 
        selectXML.setAttribute("id", "get"+vo_name+"List");
        selectXML.setAttribute("parameterClass", vo);
        selectXML.setAttribute("resultClass", vo);
        
        Element updateXML = new Element("update"); 
        updateXML.setAttribute("id", "update"+vo_name);
        updateXML.setAttribute("parameterClass", vo);
        
        Element deleteXML = new Element("delete"); 
        deleteXML.setAttribute("id", "delete"+vo_name);
        deleteXML.setAttribute("parameterClass", vo);
        
        
        StringBuffer insert =new  StringBuffer("insert into " + table_name + enter+"("+enter);
        StringBuffer insert2 =new  StringBuffer();
        StringBuffer insert3 =new  StringBuffer();
        
        
        StringBuffer update =new  StringBuffer("update " + table_name + ""+enter+"set ");
        StringBuffer update2 =new  StringBuffer();
        
        StringBuffer delete =new  StringBuffer("delete from " + table_name + ""+enter+"where id = #id#");
        
        StringBuffer select =new  StringBuffer("select "+enter);
        StringBuffer select1 =new  StringBuffer();
        StringBuffer selectWhere =new  StringBuffer();
        
        File javaFile = new File(file_path+vo+".java");
        if(javaFile.exists()) javaFile.delete();
        //实体类对象
        StringBuffer javaColumn = new StringBuffer("package com.ninesky.classtao.template.vo;"+enter+enter+"import java.util.Date;" + enter + enter + "public class "+vo+" {"+enter+enter+enter);
        //实体类方法
        StringBuffer javaMethod = new StringBuffer();
		try {
			   FileWriter fw = new FileWriter(javaFile, true);
			   BufferedWriter bw = new BufferedWriter(fw);
			   
        while (rs.next()) {
			insert.append(rs.getString("COLUMN_NAME")+","+enter);
			insert2.append("#"+  rs.getString("COLUMN_NAME")+"#,"+enter);
			update2.append(rs.getString("COLUMN_NAME")+" = #"+  rs.getString("COLUMN_NAME")+"#,"+enter);
			
			String javaType = getJavaType(rs.getString("DATA_TYPE"));
			String columnName = rs.getString("COLUMN_NAME");
			javaColumn.append(Tab+"/**"+enter);
			javaColumn.append(Tab+"* "+("".equals(rs.getString("COLUMN_COMMENT"))?"未设置，请在数据库中设置":rs.getString("COLUMN_COMMENT"))+enter);
			javaColumn.append(Tab+"*/"+enter);
			javaColumn.append(Tab+"private "+ javaType + " " +columnName+";"+enter+enter);
			javaMethod.append(Tab+"public void set"+columnName.substring(0,1).toUpperCase()+columnName.substring(1)+"("+javaType +" "+columnName+")  {"+enter);
			javaMethod.append(Tab+Tab+"this."+columnName+" = "+columnName+";"+enter);
			javaMethod.append(Tab+"}"+enter+enter);
			javaMethod.append(Tab+"public "+javaType +" get"+columnName.substring(0,1).toUpperCase()+columnName.substring(1)+"()  {"+enter);
			javaMethod.append(Tab+Tab+"return "+columnName+";"+enter);
			javaMethod.append(Tab+"}"+enter+enter);
			
			select1.append(rs.getString("COLUMN_NAME")+" as "+rs.getString("COLUMN_NAME")+","+enter);
		}
        bw.write(javaColumn.append(javaMethod).append("}").toString());
        bw.flush();
        bw.close();
        fw.close();
        
		  } catch (IOException e) {
			   e.printStackTrace();
			  }
        insert3.append(insert.substring(0, insert.length()-2));
        insert3.append(enter+") VALUES ("+enter);
        insert3.append(insert2.substring(0, insert2.length()-2));
        insert3.append(enter+")");
        insertXML.setText(insert3.toString());
        root.addContent(new Comment("插入表"+table_name));
        root.addContent(insertXML); 
        
        
        update.append(update2.substring(0, update2.length()-2));
        updateXML.setText(update.toString());
        root.addContent(new Comment("更新表"+table_name));
        root.addContent(updateXML);
        
        select.append(select1.substring(0, select1.length()-2));
        select.append(enter+"from "+ table_name + enter+ "where 1=1");
        root.addContent(new Comment("查询表"+table_name));
        selectXML.setText(select.toString());
        root.addContent(selectXML);
        
        deleteXML.setText(delete.toString());
        root.addContent(new Comment("删除表"+table_name));
        root.addContent(deleteXML); 
           
        
        Format format = Format.getPrettyFormat();  
        XMLOutputter XMLOut = new XMLOutputter(format);  
        XMLOut.output(Doc, new FileOutputStream(file_path+xml));  
    }  
    
    /**
     * 通过数据库字段类型生成java对象类型,
     * http://blog.csdn.net/haofeng82/article/details/34857991
     * @param data_type
     * @return
     */
    private static String getJavaType(String data_type) {
    	String dt = data_type.toLowerCase();
    	if ("varchar".equals(dt) || "char".equals(dt)|| "text".equals(dt))
    		return "String";
    	if ("binary".equals(dt) || "blob".equals(dt)|| "longvarbinary".equals(dt)|| "varbinary".equals(dt))
    		return "Byte[]";
    	else if ("int".equals(dt)|| "integer".equals(dt))
    		return "Integer";
    	if ("bigint".equals(dt))
    		return "Integer";
    	else if ("datetime".equals(dt) || "timestamp".equals(dt))
    		return "Date";
    	else if ("double".equals(dt) )
    		return "Double";
    	else if ("float".equals(dt) )
    		return "Float";
    	else return data_type; 
	}

	public static void autoCreateCode() throws IOException, JDOMException { 
		try {
			// 加载驱动程序
			Class.forName(jdbc_driver);
			// 连续数据库
			Connection conn = DriverManager.getConnection(jdbc_url, jdbc_user, jdbc_password);
			if (!conn.isClosed())
				System.out.println("成功连接到咱们的数据库啦啦啦啦啦啦!");
			// statement用来执行SQL语句
			Statement statement = conn.createStatement();
			// 要执行的SQL语句
			String sql = "SELECT COLUMN_NAME, DATA_TYPE, COLUMN_COMMENT FROM INFORMATION_SCHEMA.COLUMNS WHERE table_name = '"+table_name+"' AND TABLE_SCHEMA = '"+jdbc_dbname+"'" ;
			ResultSet rs = statement.executeQuery(sql);
			createMapperXML2(rs,table_name,vo_name,file_path);
			rs.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void autoCreateCode(Connection conn,String table_name,String vo_name,String file_path,String jdbc_dbname) throws IOException, JDOMException, SQLException { 
			// statement用来执行SQL语句
			Statement statement = conn.createStatement();
			// 要执行的SQL语句
			String sql = "SELECT COLUMN_NAME, DATA_TYPE, COLUMN_COMMENT FROM INFORMATION_SCHEMA.COLUMNS WHERE table_name = '"+table_name+"' AND TABLE_SCHEMA = '"+jdbc_dbname+"'" ;
			ResultSet rs = statement.executeQuery(sql);
			createMapperXML(rs,table_name,vo_name,file_path);
			rs.beforeFirst();
			createJavaVO(rs,table_name,vo_name,file_path);
	}
    
    public static void main(String[] args) throws IOException, JDOMException {
		readProperties("");
    	autoCreateCode();
    }



		/**
		 * 读取配置文件
		 * @param fileName
		 */
		public static void readProperties(String fileName){
			try {
				InputStream in = PropertiesUtil.class.getResourceAsStream("jdbc.properties");
				BufferedReader bf = new BufferedReader(new InputStreamReader(in));
				_prop.load(bf);
				jdbc_dbname=getProperty("jdbc.jdbc_dbname");
				jdbc_driver=getProperty("jdbc.driverClassName");
				jdbc_user=getProperty("jdbc.username");
				jdbc_password=getProperty("jdbc.password");
				jdbc_url=getProperty("jdbc.databaseURL");
			}catch (IOException e){
				e.printStackTrace();
			}
		}

		/**
		 * 根据key读取对应的value
		 * @param key
		 * @return
		 */
		public static String getProperty(String key){
			return _prop.getProperty(key);
		}
}
