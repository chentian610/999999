package com.classtao;

import com.classtao.vo.FileVO;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncodingAttributes;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author zhusong
 * @Description url格式的文件上传工具类
 * @date 2016-11-22 10:25:57
 */
public class WechatFileOperateUtil {
	
	private static final String WECHAT_DOWNLOAD_FILE_URL = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
	
	private static Logger logger = LoggerFactory.getLogger(WechatFileOperateUtil.class);

	/**
	 * 通过mediaid调取微信多媒体文件下载接口保存文件
	 * @author zhusong
	 * @param request http请求
	 */
	public static List<FileVO> upload(HttpServletRequest request) {
		List<FileVO> fileList = new ArrayList<FileVO>();
		/** 业务模块名称 */
		String module_code = request.getParameter("module_code");
		/** 下载多媒体文件的调用接口凭证 */
		String access_token = request.getParameter("access_token");
		/** 得到所有的上传的url地址 */
		String mediaid_list[] = request.getParameterValues("mediaid_list");
		
		logger.info("需要下载的微信多媒体：" + mediaid_list );
		
		if(StringUtils.isEmpty(access_token) || mediaid_list == null || mediaid_list.length == 0){
			logger.warn("未进行任何下载操作---access_token:" + access_token + ",mediaid_list:" + mediaid_list);
			return null;
		}
		String filePath = StringUtil.getFilePathByModuleCode(module_code,0);
		File dirPath = new File(SystemConfig.getProperty("file_root") + filePath);
		if (!dirPath.exists()) {
			dirPath.mkdir();
		}
		
		for(String mediaId : mediaid_list){
			try{
				fileList.add(downloadMediaFromWx(access_token, mediaId, dirPath, filePath));
			}catch(Exception e){
				e.printStackTrace(); 
			}
		}
		
		return fileList;
	}
	
	/**
	 * 通过amr文件的地址返回MP3格式的文件格式
	 * @author zhusong
	 * @param amrUrl
	 */
	public static String amrCoverToMp3(String amrUrl) {
		
		//url 转成 本地磁盘路径
		String sourcePath = amrUrl.replace(SystemConfig.getProperty("host_ip_port"), SystemConfig.getProperty("file_root"));
		String targetPath = sourcePath.substring(0, sourcePath.indexOf(".") + 1) + "mp3";
		
		logger.info("sourcePath-----" + sourcePath);
		
		File source = new File(sourcePath);  
        File target = new File(targetPath);  
        
        if(!source.exists()){
        	logger.error(sourcePath + "----本地不存在该文件");
        	return null;
        }
        
        String mp3Url = targetPath.replace(SystemConfig.getProperty("file_root"), SystemConfig.getProperty("host_ip_port"));

        //若文件已经转换则直接返回mp3地址
        if(target.exists()){
        	return mp3Url;
        }
        
        AudioAttributes audio = new AudioAttributes();  
        Encoder encoder = new Encoder();  
        audio.setCodec("libmp3lame");  
        EncodingAttributes attrs = new EncodingAttributes();  
        attrs.setFormat("mp3");  
        attrs.setAudioAttributes(audio);  
        try {  
            encoder.encode(source, target, attrs);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
		return mp3Url;
	}
	
	/**
	 * 从微信服务器下载多媒体文件
	 * @param accessToken 调用接口的凭证
	 * @param mediaId 多媒体id
	 * @param dirPath 文件保存根路径 (例 ：/data/pic/APP4/default/)
	 * @param filePath 文件保存模块路径(例： /APP4/default/)
	 * @return FileVO
	 */
	public static FileVO downloadMediaFromWx(String accessToken, String mediaId, File dirPath, String filePath) throws IOException {
		if (StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(mediaId))
			return null;
		
		FileVO fileVo = new FileVO();
		
		String requestUrl = WECHAT_DOWNLOAD_FILE_URL.replace("ACCESS_TOKEN",
				accessToken).replace("MEDIA_ID", mediaId);
		URL url = new URL(requestUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setDoInput(true);
		conn.setDoOutput(true);
		InputStream in = conn.getInputStream();

		String ContentDisposition = conn.getHeaderField("Content-disposition");
		String fileName = ContentDisposition.substring(
				ContentDisposition.indexOf("filename") + 10,
				ContentDisposition.length() - 1);
		String newFileID = getFileID();
		String fileStoreName = newFileID + fileName.substring(fileName.lastIndexOf("."));
		String fileResizeStoreName = newFileID + Constants.RESIZE + fileName.substring(fileName.lastIndexOf("."));
		//生成文件
		File savedFile = new File(dirPath, fileStoreName);
		//写入到硬盘
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(savedFile));
		byte[] data = new byte[1024];
		int len = -1;

		while ((len = in.read(data)) != -1) {
			bos.write(data, 0, len);
		}

		bos.close();
		in.close();
		conn.disconnect();
		int rotate = getRotate(savedFile);
		if (rotate > 0) rotateImage(dirPath + "/" + fileStoreName, rotate);
		Boolean resize = resizeFix(savedFile, dirPath + "/" + fileResizeStoreName);
		
		fileVo.setFile_name(fileName);
		fileVo.setStore_name(fileStoreName);
		fileVo.setFile_url(SystemConfig.getProperty("host_ip_port") + filePath + fileStoreName);
		fileVo.setFile_resize_url(SystemConfig.getProperty("host_ip_port")+ filePath + (resize?fileResizeStoreName:fileStoreName));
		fileVo.setFile_real_name(fileName);
		fileVo.setFile_size(String.valueOf(savedFile.length()));
		fileVo.setFile_type(fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase());
		fileVo.setFile_path(dirPath + "\\" + fileStoreName);
		fileVo.setFile_index(mediaId);
		return fileVo;
	}
	
	/**
	 * 获取文件ID，通过当前时间+随机数获得
	 * @return
	 * @throws Exception
	 */
	public static String getFileID() {
		Long now = Long.parseLong(new SimpleDateFormat("yyMMddHHmmss")
				.format(new Date()));
		Long random = (long) (Math.random() * now);
		return now + "" + random;
	}
	
	/**
     * 获取图片正确显示需要旋转的角度（顺时针）
     * @return
     */
    public static int getRotate(File file){
    	try{
	        int rotate = 0;
	        Metadata metadata = ImageMetadataReader.readMetadata(file);  
	        int orientation = 0;
	        for (Directory directory : metadata.getDirectories()) {  
	        	if("ExifIFD0Directory".equalsIgnoreCase(directory.getClass().getSimpleName())){
	        		orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
	        		break;
	        	}
	        } 
           // 原图片的方向信息
           if(6 == orientation ){
               //6旋转90
        	   rotate = 90;
           }else if( 3 == orientation){
              //3旋转180
        	   rotate = 180;
           }else if( 8 == orientation){
              //8旋转90
        	   rotate = 270;
           }
           return rotate; 
	    } catch (Exception e) {
	    	return 0;
	    }
    }
    
    /**
     * 旋转手机照片
     * @return
     */
    public static void rotateImage(String fullPath, int angel){
        BufferedImage src;
        try {
            src = ImageIO.read(new File(fullPath));
            int src_width = src.getWidth(null);
            int src_height = src.getHeight(null);
            Rectangle rect_des = CalcRotatedSize(new Rectangle(new Dimension(src_width, src_height)), angel);
            BufferedImage res = new BufferedImage(rect_des.width, rect_des.height,BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = res.createGraphics();
            g2.translate((rect_des.width - src_width) / 2, (rect_des.height - src_height) / 2);
            g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);
            g2.drawImage(src, null, null);
            ImageIO.write(res, "jpg", new File(fullPath));
        } catch (IOException e) {
            e.printStackTrace();
        }  
    }
    
    /** 
     * 按照宽度还是高度进行压缩 
     */
    public static Boolean resizeFix(File savedFile,String fileStoreName) { 
    	try {
    		BufferedImage img = ImageIO.read(savedFile);
			if (img==null) return false;
	    	Integer w = img.getWidth(null);    // 得到源图宽  
	    	Integer h = img.getHeight(null);
	    	
	        if (w<h) resizeByWidth(img,w,h,fileStoreName);  
	         else   resizeByHeight(img,w,h,fileStoreName);  
//	        int rotate = getRotate(savedFile);
//	        if (rotate>0) rotateImage(fileStoreName,rotate);
	        return true;
		} catch (IOException e) {
			return false;
		}
    }  
    
    public static Rectangle CalcRotatedSize(Rectangle src, int angel) {  
        // if angel is greater than 90 degree, we need to do some conversion  
        if (angel >= 90) {  
            if(angel / 90 % 2 == 1){  
                int temp = src.height;  
                src.height = src.width;  
                src.width = temp;  
            }  
            angel = angel % 90;  
        }  
  
        double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;  
        double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;  
        double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;  
        double angel_dalta_width = Math.atan((double) src.height / src.width);  
        double angel_dalta_height = Math.atan((double) src.width / src.height);  
  
        int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha  
                - angel_dalta_width));  
        int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha  
                - angel_dalta_height));  
        int des_width = src.width + len_dalta_width * 2;  
        int des_height = src.height + len_dalta_height * 2;  
        return new java.awt.Rectangle(new Dimension(des_width, des_height));  
    }
    
    /** 
     * 以宽度为基准，等比例放缩图片 
     * @param w int 新宽度 
     */  
    public static void resizeByWidth(Image img,int w,int h,String fileStoreName) throws IOException {  
        int h1 = (int) (Constants.PICTURE_RESIZE_WIDTH * h / w);  
        resize(img,Constants.PICTURE_RESIZE_WIDTH, h1,fileStoreName);  
    }  
    
    /** 
     * 强制压缩/放大图片到固定的大小 
     * @param w int 新宽度 
     * @param h int 新高度 
     */  
    public static void resize(Image img,int w, int h,String fileStoreName) throws IOException {  
    	// SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢  
        BufferedImage image = new BufferedImage(w, h,BufferedImage.TYPE_INT_RGB); 
        image.getGraphics().drawImage(img, 0, 0, w, h, null); // 绘制缩小后的图  
        File destFile = new File(fileStoreName);  
        FileOutputStream out = new FileOutputStream(destFile); // 输出到文件流  
        // 可以正常实现bmp、png、gif转jpg  
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);  
        JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(image);  
        jep.setQuality(Constants.PICTURE_QUAILTY, true); 
        encoder.encode(image,jep); // JPEG编码  
        out.close();
    }
    
    /** 
     * 以高度为基准，等比例缩放图片 
     * @param h int 新高度 
     */  
    public static void resizeByHeight(Image img,int w,int h,String fileStoreName) throws IOException {  
        int w1 = (int) (Constants.PICTURE_RESIZE_HEIGHT * w / h);  
        resize(img,w1, Constants.PICTURE_RESIZE_HEIGHT,fileStoreName);  
    }  
	
}
