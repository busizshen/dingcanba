package com.dingcan.util;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class CustomDate {
    /** ������ʱ����(���»���) yyyyMMddHHmmss */
    public static final String dtLong                  = "yyyyMMddHHmmss";
    
    /** ����ʱ�� yyyy-MM-dd HH:mm:ss */
    public static final String simple                  = "yyyy-MM-dd HH:mm:ss";
    
    /** ������(���»���) yyyyMMdd */
    public static final String dtShort                 = "yyyyMMdd";
    
    /**
     * ����ϵͳ��ǰʱ��(��ȷ������),��Ϊһ��Ψһ�Ķ������
     * @return
     *      ��yyyyMMddHHmmssΪ��ʽ�ĵ�ǰϵͳʱ��
     */
	public  static String getOrderNum(){
		Date date=new Date();
		DateFormat df=new SimpleDateFormat(dtLong);
		return df.format(date);
	}
	
	/**
	 * ��ȡϵͳ��ǰ����(��ȷ������)����ʽ��yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public  static String getDateFormatter(){
		Date date=new Date();
		DateFormat df=new SimpleDateFormat(simple);
		return df.format(date);
	}
	
	/**
	 * ��ȡϵͳ����������(��ȷ����)����ʽ��yyyyMMdd
	 * @return
	 */
	public static String getDate(){
		Date date=new Date();
		DateFormat df=new SimpleDateFormat(dtShort);
		return df.format(date);
	}
	
	/**
	 * �����������λ��
	 * @return
	 */
	public static String getThree(){
		Random rad=new Random();
		return rad.nextInt(1000)+"";
	}

	 /**
	  * ��ȡ����ʱ��
	  * 
	  * @return�����ַ�����ʽ yyyy-MM-dd HH:mm:ss
	  */
	 public static String getStringDate() {
	  Date currentTime = new Date();
	  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  String dateString = formatter.format(currentTime);
	  return dateString;
	 }

	 /**
	  * ��ȡ����ʱ��
	  * @return�����ַ�����ʽ yyyy-MM-dd HH:mm:ss
	  * 2013-5-12 ����7:04:43 2013
	  */
	 public static String getStringTime() {
		  Date currentTime = new Date();
		  SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		  String dateString = formatter.format(currentTime);
		  return dateString;
	 }
	 
	 /**
	  * ��ȡ����ʱ��
	  * 
	  * @return ���ض�ʱ���ַ�����ʽyyyy-MM-dd
	  */
	 public static String getStringDateShort() {
	  Date currentTime = new Date();
	  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	  String dateString = formatter.format(currentTime);
	  return dateString;
	 }

	 /**
	  * ��ȡʱ�� Сʱ:��;�� HH:mm:ss
	  * 
	  * @return
	  */
	 public static String getTimeShort() {
	  SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
	  Date currentTime = new Date();
	  String dateString = formatter.format(currentTime);
	  return dateString;
	 }

	 /**
	  * ����ʱ���ʽ�ַ���ת��Ϊʱ�� yyyy-MM-dd HH:mm:ss
	  * 
	  * @param strDate
	  * @return
	  */
	 public static Date strToDateLong(String strDate) {
	  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  ParsePosition pos = new ParsePosition(0);
	  Date strtodate = formatter.parse(strDate, pos);
	  return strtodate;
	 }

	 /**
	  * ����ʱ���ʽʱ��ת��Ϊ�ַ��� yyyy-MM-dd HH:mm:ss
	  * 
	  * @param dateDate
	  * @return
	  */
	 public static String dateToStrLong(java.util.Date dateDate) {
	  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  String dateString = formatter.format(dateDate);
	  return dateString;
	 }

	 /**
	  * ����ʱ���ʽʱ��ת��Ϊ�ַ��� yyyy-MM-dd
	  * 
	  * @param dateDate
	  * @param k
	  * @return
	  */
	 public static String dateToStr(java.util.Date dateDate) {
	  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	  String dateString = formatter.format(dateDate);
	  return dateString;
	 }

	 /**
	  * ����ʱ���ʽ�ַ���ת��Ϊʱ�� yyyy-MM-dd 
	  * 
	  * @param strDate
	  * @return
	  */
	 public static Date strToDate(String strDate) {
	  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	  ParsePosition pos = new ParsePosition(0);
	  Date strtodate = formatter.parse(strDate, pos);
	  return strtodate;
	 }

	 public static void main(String[] args) throws Exception {
	  try {
	   //System.out.print(Integer.valueOf(getTwoDay("2006-11-03 12:22:10", "2006-11-02 11:22:09")));
	  } catch (Exception e) {
	   throw new Exception();
	  }
	  //System.out.println("sss");
	 }
}
