package com.ninesky.classtao.wechatclient.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ninesky.classtao.wechatclient.vo.TemplateDateVO;
import com.ninesky.common.Constants;

public class WechatUtil {
	
	private static Logger logger = LoggerFactory.getLogger(WechatUtil.class);

	/**
     * 发送微信模板消息
     * @param accessToken 微信接口调用凭证
     * @param openIds 接收消息人的openid
     * @param templateId 指定需要发送的消息模板id，通过模板编号获得
     * @param url 模板点击跳转的地址
     * @param templateData 字符串格式的模板内容
     */
	public static void sendTemplateMessage(String accessToken, List<String> openIds, String templateId, String url, Map<String, TemplateDateVO> templateData){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("template_id", templateId);
		paramMap.put("url", url);
		paramMap.put("data", templateData);
		String turl = Constants.WECHAT_SEND_TEMPLATE_URL.replaceAll("ACCESS_TOKEN", accessToken);
		for(String openId : openIds){
			paramMap.put("touser", openId);
			String msg = JSON.toJSONString(paramMap).replaceAll("Class", "class");
			//发送模板消息
			httpRequest(turl, "POST", msg);
		}
	} 
	
	/**
     * 通过模板编号获取模板id
     * @param accessToken 微信接口调用凭证
     * @param templateNO 模板编号
     * @return 模板id
     */
	public static String getTemplateId(String accessToken, String templateNO){
		String turl = Constants.WECHAT_LOAD_TEMPLATEID_URL.replaceAll("ACCESS_TOKEN", accessToken);
		String outputStr = "{\"template_id_short\":\"" + templateNO + "\"}";
		JSONObject jo = httpRequest(turl, "POST", outputStr);
		if(jo != null && "ok".equals(jo.getString("errmsg"))){
			return jo.getString("template_id");
		}
		logger.error("获取消息模板id失败：" + jo.getString("errmsg"));
		return null;
	}
	
	
	/**
     * 发起https请求并获取结果
     * @param requestUrl 请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr 提交的数据
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try {
                // 创建SSLContext对象
                TrustManager[] tm = { new MyX509TrustManager() };
                SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
                sslContext.init(null, tm, new java.security.SecureRandom());
                // 从上述SSLContext对象中得到SSLSocketFactory对象
                SSLSocketFactory ssf = sslContext.getSocketFactory();

                URL url = new URL(requestUrl);
                HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
                httpUrlConn.setSSLSocketFactory(ssf);

                httpUrlConn.setDoOutput(true);
                httpUrlConn.setDoInput(true);
                httpUrlConn.setUseCaches(false);
                // 设置请求方式（GET/POST）
                httpUrlConn.setRequestMethod(requestMethod);

                if ("GET".equalsIgnoreCase(requestMethod))
                        httpUrlConn.connect();

                // 当有数据需要提交时
                if (null != outputStr) {
                        OutputStream outputStream = httpUrlConn.getOutputStream();
                        // 注意编码格式，防止中文乱码
                        outputStream.write(outputStr.getBytes("UTF-8"));
                        outputStream.close();
                }

                // 将返回的输入流转换成字符串
                InputStream inputStream = httpUrlConn.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String str = null;
                while ((str = bufferedReader.readLine()) != null) {
                        buffer.append(str);
                }
                bufferedReader.close();
                inputStreamReader.close();
                // 释放资源
                inputStream.close();
                inputStream = null;
                httpUrlConn.disconnect();
                jsonObject = JSON.parseObject(buffer.toString());
        } catch (ConnectException ce) {
        	logger.info("Weixin server connection timed out.");
        } catch (Exception e) {
        	logger.info("https request error:{}"+e.getMessage());
        }
        return jsonObject;
    }
    
}
