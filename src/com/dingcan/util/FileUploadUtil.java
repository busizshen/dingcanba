package com.dingcan.util;

import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.dingcan.cons.Cons;
import com.dingcan.shop.model.Shop;
import com.dingcan.user.model.User;

public class FileUploadUtil {
	public static void inputstreamtofile(InputStream in,String filePath,String fileName) throws IOException{
		FileOutputStream fs = new FileOutputStream(filePath+fileName);  
        byte[] buffer = new byte[1024 * 1024];  
        int bytesum = 0;  
        int byteread = 0;  
        while ((byteread = in.read(buffer)) != -1) {  
            bytesum += byteread;  
            fs.write(buffer, 0, byteread);  
            fs.flush();  
        }  
        
        fs.close();  
        in.close();  
	}
	
	/**
	 * ����Ŀ¼
	 * @param request
	 * @return
	 */
	public static String generateImageURL(HttpServletRequest request){
		String savePath = null;
		savePath = request.getRealPath("upload")+"/";
		
		//���Ŀ¼
		File uploadDir = new File(savePath);
		if(!uploadDir.isDirectory()){
			return "�ϴ�Ŀ¼������.";
		}
		
		//���Ŀ¼дȨ��
		if(!uploadDir.canWrite()){
			return "�ϴ�Ŀ¼û��дȨ��.";
		}
		
		//�����ļ���
		savePath += ((User)request.getSession().getAttribute(Cons.USER_CONTEXT)).getUserId() + "/";
		File saveDirFile = new File(savePath);
		if (!saveDirFile.exists()) {
			saveDirFile.mkdirs();
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String ymd = sdf.format(new Date());
		savePath += ymd + "/";
		File dirFile = new File(savePath);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		return savePath;
	}
	
	/**
	 * �û�ͷ��Ŀ¼
	 * @param request
	 * @return
	 * Green Lei
	 * 2012-12-7 ����1:48:10 2012
	 */
	public static String genetateUserimgPath(HttpServletRequest request){
		String savePath = null;
		savePath = request.getRealPath("userimg")+"/";
		
		//���Ŀ¼
		File uploadDir = new File(savePath);
		if(!uploadDir.isDirectory()){
			return "�ϴ�Ŀ¼������.";
		}
		
		//���Ŀ¼дȨ��
		if(!uploadDir.canWrite()){
			return "�ϴ�Ŀ¼û��дȨ��.";
		}
		
		//�����ļ���
		savePath +=  "user/";
		File saveDirFile = new File(savePath);
		if (!saveDirFile.exists()) {
			saveDirFile.mkdirs();
		}
		
		//�����ļ���
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String ymd = sdf.format(new Date());
		savePath += ymd + "/";
		File dirFile = new File(savePath);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		return savePath;
	}
	
	/**
	 * �̼�logo
	 * @param request
	 * @return
	 * 2013-5-1 ����11:38:12 2013
	 */
	public static String genetateShopimgPath(HttpServletRequest request){
		String savePath = null;
		savePath = request.getRealPath("shoplogo")+"/";
		
		//���Ŀ¼
		File uploadDir = new File(savePath);
		if(!uploadDir.isDirectory()){
			return "�ϴ�Ŀ¼������.";
		}
		
		//���Ŀ¼дȨ��
		if(!uploadDir.canWrite()){
			return "�ϴ�Ŀ¼û��дȨ��.";
		}
		
		//�����ļ���
		savePath +=  "shop/";
		File saveDirFile = new File(savePath);
		if (!saveDirFile.exists()) {
			saveDirFile.mkdirs();
		}
		
		//�����ļ���
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String ymd = sdf.format(new Date());
		savePath += ymd + "/";
		File dirFile = new File(savePath);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		return savePath;
	}
}
