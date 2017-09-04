package com.ninesky.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ninesky.classtao.user.vo.UserVO;
import com.ninesky.common.util.BeanUtil;
import com.ninesky.common.vo.ReceiveVO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileCopy{

	public static void main(String[] args){
		ReceiveVO group = new ReceiveVO();
		group.setGroup_id(1);
		group.setSchool_id(1030);
		group.setTeam_id(null);
		List<ReceiveVO> list = new ArrayList<ReceiveVO>();
		System.out.println("llll:"+BeanUtil.ListTojson(list));
		list.add(group);

		String jsonString = JSON.toJSONString(list, SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullStringAsEmpty,SerializerFeature.WriteNullNumberAsZero);
		System.out.println(jsonString);

		System.out.println(BeanUtil.ListTojson(list));

		String jsonArray = "[{\"group_id\":1,\"school_id\":1030,\"student_id\":0,\"team_id\":0,\"team_type\":\"\",\"user_id\":0},{\"group_id\":2,\"school_id\":1030,\"student_id\":333,\"team_type\":\"\",\"user_id\":3440}]";
		List<ReceiveVO> list1 = BeanUtil.jsonToList(jsonArray,ReceiveVO.class);

		HashMap<String,String> m = new HashMap<String, String>();
		m.put("school_id","1030");
		m.put("user_id","chen");
		m.put("user_type","003005");
		m.put("user_name","陈天辉");
		UserVO u1 = BeanUtil.formatMapToBean(m,UserVO.class);
		System.out.println(list1);

	}
	
	static boolean isNull(String str){
		if(null == str || "".equals(str.trim()))
			return true;
		else
			return false;
	}

	static void oldIOCopy(String name, String copy){
		long start = System.currentTimeMillis();
		try{
			File f = new File(name);	
			File ftmp = new File(copy);
			
			FileInputStream in = new FileInputStream(f);
			FileOutputStream out = new FileOutputStream(ftmp);

			byte[] buff = new byte[1024];

			int len = 0;
			while((len = in.read(buff)) != -1){
				out.write(buff, 0, len);
			}

			out.close();
			in.close();

		}catch(Exception e){
			System.out.println("Exception.");
		}	
		System.out.println(System.currentTimeMillis() - start);
	}

	static void newIOCopy(String name, String copy){
		long start = System.currentTimeMillis();
		try{
			FileChannel in = new FileInputStream(new File(name)).getChannel();	
			FileChannel out = new FileOutputStream(new File(copy)).getChannel();

			ByteBuffer buffer = ByteBuffer.allocate(1024);
			
			while(in.read(buffer) != -1){
				buffer.flip(); // Prepare for writing
				out.write(buffer);
				buffer.clear();// Prepare for reading
			}

			out.close();
			in.close();
			System.out.println(System.currentTimeMillis() - start);
		}catch(Exception e){
			System.out.println("Exception");
		}	
	}
}

	    			